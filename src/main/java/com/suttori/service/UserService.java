package com.suttori.service;

import com.suttori.dao.UserDAO;
import com.suttori.entity.User;
import com.suttori.entity.enams.Role;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

public class UserService {
    UserDAO userDAO = new UserDAO();
    public String error;
    EmailSenderService emailSenderService = new EmailSenderService();
    private final Logger log = Logger.getLogger(UserService.class);

    public boolean save(User user) {
        User userFromDB = getUserByEmail(user.getEmail());
        if(userFromDB != null) {
            log.info("user already register");
            error = "emailAlreadyUse";
            return false;
        }
        if (user.getFirstName().length() < 3) {
            error = "shortFirstNameError";
            return false;
        }
        if (user.getLastName().length() < 3) {
            error = "lastNameShortError";
            return false;
        }
//        if (user.getPassword().length() < 5) {
//            error = "passwordShortError";
//            return false;
//        }

        byte[] salt = generateSalt();
        user.setSalt(salt);
        byte[] pass = user.hashPassword(user.getPassword(), salt);
        String hashedPassword = Hex.encodeHexString(pass);
        user.setPassword(hashedPassword);

        user.setActivationCode(UUID.randomUUID().toString());

        emailSenderService.sendActivationCode(user);

        log.info("save working");
        return userDAO.insert(user);
    }

    public boolean activateEmail(String code) {
        User user = userDAO.findByActivationCode(code);
        if (user == null) {
            error = "emailActivateError";
            log.info("Email address don't activated");
            return false;
        }
        user.setEmailActivated("true");
        userDAO.setEmailActivated(user);
        log.info("Email address activated");
        return true;
    }

    public boolean userLogin(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        User userFromDB = userDAO.findByEmail(email);

        if (userFromDB == null) {
            log.info("user not register");
            error = "userNotFoundError";
            return false;
        }

        byte[] hashPassword = user.hashPassword(user.getPassword(), userFromDB.getSalt());
        String hashPasswordString = Hex.encodeHexString(hashPassword);

        if (!userFromDB.getPassword().equals(hashPasswordString)) {
            log.info("Passwords do not match");
            error = "passwordError";
            return false;
        }

        if(!userFromDB.getEmailActivated().equals("true")) {
            log.info("Email not activated");
            error = "emailActivateError";
            return false;
        }
        log.info("User logged in");
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

    public boolean changePassword(User user, String oldPassword, String newPassword, String newPasswordReaped) {
        if (newPassword.length() > 20) {
            error = "passwordShortError";
            return false;
        }
        if (!newPasswordReaped.equals(newPassword)) {
            error = "repeatPasswordError";
            return false;
        }
        if (user.getPassword().equals(newPassword)) {
            error = "passwordSame";
        }
        user.setPassword(newPassword);
        userDAO.setPassword(user.getId(), newPassword);
        log.info("User: " + user.getEmail() + " change his password");
        return true;
    }

    public boolean editProfile(User user, String firstName, String lastName, String email, String phoneNumber) {
        if (!user.getFirstName().equals(firstName) && !firstName.equals("")) {
            if (firstName.length() < 3) {
                error = "shortFirstNameError";
                return false;
            }
            user.setFirstName(firstName);
            userDAO.setVariable("first_name", user.getId(), firstName);
        } else {
            error = "firstNameSame";
        }

        if (!user.getLastName().equals(lastName) && !lastName.equals("")) {
            if (lastName.length() < 3) {
                error = "shortLastNameError";
                return false;
            }
            user.setLastName(lastName);
            userDAO.setVariable("last_name", user.getId(), lastName);
        } else {
            error = "lastNameSame";
        }

        if (!user.getEmail().equals(email) && !email.equals("")) {
            // проверка
            user.setEmail(email);
            userDAO.setVariable("email", user.getId(), email);
        } else {
            error = "emailSame";
        }

        if (!user.getPhoneNumber().equals(phoneNumber) && !phoneNumber.equals("")) {
            // проверка
            user.setPhoneNumber(phoneNumber);
            userDAO.setVariable("phone_number", user.getId(), phoneNumber);
        } else {
            error = "phoneNumberSame";
        }
        return true;
    }

    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

}
