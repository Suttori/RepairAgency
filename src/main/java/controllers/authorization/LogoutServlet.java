package controllers.authorization;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;



@WebServlet(name = "logout")
public class LogoutServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(LogoutServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("do get");
        HttpSession session = req.getSession(false);
        if (session != null) {
            Cookie cookie = new Cookie("RepairAgencyCookie", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            resp.addCookie(cookie);
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}
