package com.suttori.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter {

    private static final Logger logger = Logger.getLogger(SecurityFilter.class);

    private final List<String> START_PAGES = Arrays.asList("/", "/start-page.jsp", "/main", "/registration", "/password-reset");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getServletPath();

        if (req.getSession().getAttribute("user") == null) {
            if (!START_PAGES.contains(uri)) {
                logger.info(uri + ": forbidden");
                resp.sendRedirect("/start-page.jsp");
                return;
            }
        }
//        } else if (START_PAGES.contains(uri)){
//                log.info(uri + ": forbidden");
//                resp.sendRedirect("/views/orders");
//                return;
//            }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info( "The filter: {} has finished its work");
    }

    @Override
    public void init(FilterConfig arg0) {
        logger.info( "The filter: {} has begun its work");
    }
}
