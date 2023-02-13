package com.suttori.controllers.authorization;

import com.suttori.controllers.authorization.LoginServlet;
import com.suttori.controllers.order.AddOrderServlet;
import com.suttori.entity.Order;
import com.suttori.entity.User;
import com.suttori.service.OrderService;
import com.suttori.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginServletTest {

    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    User user;
    @Mock
    UserService userService;
    @Mock
    RequestDispatcher requestDispatcher;

    @InjectMocks
    LoginServlet loginServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void doPost_verifyTrue() throws ServletException, IOException {
        when(req.getParameter("email")).thenReturn("email");
        when(req.getParameter("password")).thenReturn("password");
        when(userService.userLogin("email", "password")).thenReturn(true);
        when(userService.getUserByEmail("email")).thenReturn(user);
        when(req.getSession()).thenReturn(session);
        loginServlet.doPost(req, resp);
        verify(resp).sendRedirect("/profile/orders");
    }

    @Test
    public void doPost_verifyFalse() throws ServletException, IOException {
        when(req.getParameter("email")).thenReturn("email");
        when(req.getParameter("password")).thenReturn("password");
        when(userService.userLogin("email", "password")).thenReturn(false);
        when(userService.getUserByEmail("email")).thenReturn(user);
        when(req.getSession()).thenReturn(session);
        when(req.getRequestDispatcher("/views/start-page.jsp")).thenReturn(requestDispatcher);
        loginServlet.doPost(req, resp);
        verify(req).setAttribute("error", userService.error);
    }


}
