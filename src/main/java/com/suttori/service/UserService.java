package com.suttori.service;

import com.suttori.dao.UserDAO;
import com.suttori.entity.Pagination;
import com.suttori.entity.Sort;
import com.suttori.entity.User;
import com.suttori.entity.enams.Locales;
import com.suttori.entity.enams.Role;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import java.security.SecureRandom;
import java.util.*;

public class UserService {
    UserDAO userDAO = new UserDAO();
    public String error;
    EmailSenderService emailSenderService = new EmailSenderService();
    private final Logger logger = Logger.getLogger(UserService.class);
    final String PHONE_TEMPLATE = "^380\\d{3}\\d{2}\\d{2}\\d{2}$";

    public boolean save(User user) {
        User userFromDB = getUserByEmail(user.getEmail());

        if (userFromDB != null) {
            logger.info("user already register");
            error = "emailAlreadyUse";
            return false;
        }
        if (user.getFirstName().length() < 3) {
            logger.info("shortFirstNameError");
            error = "shortFirstNameError";
            return false;
        }
        if (user.getLastName().length() < 3) {
            logger.info("lastNameShortError");
            error = "lastNameShortError";
            return false;
        }
        if (!user.getPhoneNumber().matches(PHONE_TEMPLATE)) {
            logger.info("errorFormatPhone");
            error = "errorFormatPhone";
            return false;
        }
        if (user.getPassword().length() < 5) {
            logger.info("passwordShortError");
            error = "passwordShortError";
            return false;
        }

        byte[] salt = generateSalt();
        user.setSalt(salt);
        byte[] pass = user.hashPassword(user.getPassword(), salt);
        String hashedPassword = Hex.encodeHexString(pass);
        user.setPassword(hashedPassword);

        user.setActivationCode(UUID.randomUUID().toString());
        if (emailSenderService.sendActivationCode(user)) {
            logger.info("user saved");
            return userDAO.insert(user);
        }
        logger.info("failed to save user");
        return false;
    }

    public boolean activateEmail(String code) {
        User user = userDAO.findByActivationCode(code);
        if (user == null) {
            error = "emailActivateError";
            logger.info("Email address don't activated");
            return false;
        }
        user.setEmailActivated("true");
        userDAO.setEmailActivated(user);
        logger.info("Email address activated");
        return true;
    }

    public boolean userLogin(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        User userFromDB = userDAO.findByEmail(email);

        if (userFromDB == null) {
            logger.info("user not register");
            error = "userNotFoundError";
            return false;
        }

        byte[] hashPassword = user.hashPassword(user.getPassword(), userFromDB.getSalt());
        String hashPasswordString = Hex.encodeHexString(hashPassword);

        if (!userFromDB.getPassword().equals(hashPasswordString)) {
            logger.info("Passwords do not match");
            error = "passwordError";
            return false;
        }

        if (!userFromDB.getEmailActivated().equals("true")) {
            logger.info("Email not activated");
            error = "emailNotActivateError";
            return false;
        }
        logger.info("User logged in");
        return true;
    }

    public User getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public User getUserById(int id) {
        return userDAO.findById(id);
    }

    public List<User> getAllMasters() {
        return userDAO.findByRole(Role.CRAFTSMAN);
    }


    public List<User> getUserMasters(int user_id) {
        return userDAO.findUserMasters("user_id", "craftsman_id", user_id, 0);
    }

    /**
     * the method fills in user sorting parameters and passes them to the dao
     * @param sort - set of parameters for sorting
     * @param pagination - limit and offset
     * @return sorted list from dao
     */
    public List<User> getSortedUsers(Sort sort, Pagination pagination) {
        Map<String, Object> filterParams = new HashMap<>();
        String sortingParams = null;
        Map<String, Integer> limitingParams = new LinkedHashMap<>();

        if (sort.getEmail() != null && !sort.getEmail().isEmpty()) {
            filterParams.put("email", sort.getEmail());
        }

        if (sort.getSort() != null && !sort.getSort().equals("none")) {
            sortingParams = sort.getSort();
        }

        limitingParams.put("LIMIT", pagination.getOrdersOnPage());
        limitingParams.put("OFFSET", pagination.getOffset());

        return userDAO.findBy(filterParams, sortingParams, limitingParams);
    }

    public boolean changePassword(User user, String oldPassword, String newPassword, String newPasswordReaped) {
        byte[] salt = user.getSalt();

        byte[] oldPass = user.hashPassword(oldPassword, salt);
        String hashedOldPassword = Hex.encodeHexString(oldPass);

        byte[] newPass = user.hashPassword(newPassword, salt);
        String hashedNewPassword = Hex.encodeHexString(newPass);

        if (!user.getPassword().equals(hashedOldPassword)) {
            logger.info("oldPasswordError");
            error = "oldPasswordError";
            return false;
        }
        if (newPassword.length() < 4) {
            logger.info("passwordShortError");
            error = "passwordShortError";
            return false;
        }
        if (!newPasswordReaped.equals(newPassword)) {
            logger.info("repeatPasswordError");
            error = "repeatPasswordError";
            return false;
        }
        if (user.getPassword().equals(hashedNewPassword)) {
            logger.info("passwordSame");
            error = "passwordSame";
            return false;
        }

        if (newPassword.length() < 5) {
            logger.info("passwordShortError");
            error = "passwordShortError";
            return false;
        }

        user.setPassword(hashedNewPassword);

        userDAO.setPassword(user.getId(), hashedNewPassword);
        logger.info("User: " + user.getEmail() + " change his password");
        return true;
    }

    public boolean editProfile(User user, String firstName, String lastName, String phoneNumber) {
        if (!firstName.equals("")) {
            if (firstName.length() < 3) {
                logger.info("shortFirstNameError");
                error = "shortFirstNameError";
                return false;
            }
            if (user.getFirstName().equals(firstName)) {
                logger.info("firstNameSame");
                error = "firstNameSame";
                return false;
            }
            user.setFirstName(firstName);
            userDAO.setVariable("first_name", user.getId(), firstName);
        }

        if (!lastName.equals("")) {
            if (lastName.length() < 3) {
                logger.info("lastNameShortError");
                error = "lastNameShortError";
                return false;
            }
            if (user.getLastName().equals(lastName)) {
                logger.info("lastNameSame");
                error = "lastNameSame";
                return false;
            }
            user.setLastName(lastName);
            userDAO.setVariable("last_name", user.getId(), lastName);
        }

        if (!phoneNumber.equals("")) {

            if (!phoneNumber.matches(PHONE_TEMPLATE)) {
                logger.info("errorFormatPhone");
                error = "errorFormatPhone";
                return false;
            }
            if (user.getPhoneNumber().equals(phoneNumber)) {
                logger.info("phoneNumberSame");
                error = "phoneNumberSame";
                return false;
            }
            user.setPhoneNumber(phoneNumber);
            userDAO.setVariable("phone_number", user.getId(), phoneNumber);
        }
        logger.info("profile has been edited");
        return true;
    }

    public void setLocale(User user, Locales locales) {
        user.setLocale(locales);
        userDAO.setLocale(user);
    }

    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public int getNumberOfRows() {
        return userDAO.totalRows;
    }
}
