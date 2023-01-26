package com.suttori.controllers.authorization;

import com.suttori.ProjectProperties;
import com.suttori.entity.User;
import org.apache.log4j.Logger;
import com.suttori.service.EmailSenderService;
import com.suttori.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
//        if (!isCaptchaFill(req.getParameter("g-recaptcha-response"))) {
//            req.setAttribute("error", "captchaError");
//            doGet(req, resp);
//            return;
//        }
        logger.info("doPost working");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        UserService userService = new UserService();

        boolean remember = req.getParameter("rememberMe") != null;
        if (userService.userLogin(email, password)) {
            User user = userService.getUserByEmail(email);
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            session.setAttribute("lang", user.getLocale());

            Cookie cookie = new Cookie("RepairAgencyCookie", String.valueOf(user.getId()));
            if (remember) {
                cookie.setMaxAge(60 * 60 * 24 * 30); //30 days
            }
            resp.addCookie(cookie);
            resp.sendRedirect("/profile/orders");
        } else {
            req.setAttribute("error", userService.error);
            doGet(req, resp);
        }
    }

    public static boolean isCaptchaFill(String recaptchaResponse) {
        String googleCaptcha = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(googleCaptcha, ProjectProperties.getProperty("recaptcha.secret"), recaptchaResponse)))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            return response.body().split(": ")[1].split(",")[0].equals("true");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
