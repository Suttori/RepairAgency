package service;

import dao.UserDAO;
import entity.User;
import org.apache.log4j.Logger;

public class UserService {
    UserDAO userDAO = new UserDAO();
    public String error;
    private final Logger log = Logger.getLogger(UserService.class);


    public boolean save(User user) {
        log.info("save working");
        return userDAO.insert(user);
    }

    public boolean userLogin(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        User userFromDB = userDAO.findByEmail(email);

        //хэширование

        if (userFromDB == null) {
            log.info("user not register");
            error = "userNotFoundError";
            return false;
        }
        if (!userFromDB.getPassword().equals(user.getPassword())) {
            log.info("Passwords do not match");
            error = "passwordError";
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


}
