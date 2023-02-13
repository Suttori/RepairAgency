package com.suttori.controllers.authorization;

import com.suttori.ProjectProperties;
import com.suttori.entity.User;
import com.suttori.entity.enams.Locales;
import org.apache.log4j.Logger;
import com.suttori.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * the servlet processes the input data and passes it to the service
 * for further user registration
 */
@WebServlet(name = "registration")
public class RegistrationServlet extends HttpServlet {

    private final Logger log = Logger.getLogger(RegistrationServlet.class);
    UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/registration/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (!isCaptchaFill(req.getParameter("g-recaptcha-response"))) {
            req.setAttribute("error", "captchaError");
            doGet(req, resp);
            return;
        }

        User user = new User();
        user.setFirstName(req.getParameter("firstName"));
        user.setLastName(req.getParameter("lastName"));
        user.setEmail(req.getParameter("email"));
        user.setPhoneNumber(req.getParameter("phoneNumber"));
        user.setPassword(req.getParameter("password"));
        user.setLocale(Locales.valueOf((String) req.getSession().getAttribute("lang")));
        if (userService.save(user)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            log.info("Registration successful");
            resp.sendRedirect("/views/registration/registrationSuccessful.jsp");
        } else {
            req.setAttribute("error", userService.error);
            doGet(req, resp);
        }
    }

    public boolean isCaptchaFill(String recaptchaResponse) {
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
