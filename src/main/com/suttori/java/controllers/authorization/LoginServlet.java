package controllers.authorization;

import entity.User;
import org.apache.log4j.Logger;
import service.EmailSenderService;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "main")
public class LoginServlet extends HttpServlet {
    private final Logger logger = Logger.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("doGet working");
        if (req.getSession().getAttribute("user") == null) {
            HttpSession session = req.getSession();
            User user = new User();
            session.setAttribute("user", user);

        }
        RequestDispatcher rd = req.getRequestDispatcher("/start-page.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //капча
        logger.info("doPost working");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        UserService userService = new UserService();

        boolean remember = req.getParameter("rememberMe") != null;
        if (userService.userLogin(email, password)) {
            User user = userService.getUserByEmail(email);
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            //локализация
            //remember me


            EmailSenderService emailSenderService = new EmailSenderService();
            emailSenderService.send("Test letter", "Hello!", "kyepta888@gmail.com");


            Cookie cookie = new Cookie("RepairAgencyCookie", String.valueOf(user.getId()));
            if (remember) {
                cookie.setMaxAge(60 * 60 * 24 * 30); //30 days
            }
            resp.addCookie(cookie);
            resp.sendRedirect("/views/profile.jsp");
        } else {
            req.setAttribute("error", userService.error);
            doGet(req, resp);
        }
    }
}
