package com.suttori.controllers.authorization;

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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServletTest {

    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    RequestDispatcher requestDispatcher;

    @InjectMocks
    RegistrationServlet registrationServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void doPost_CaptchaFalse() throws IOException, ServletException {
        when(req.getParameter("g-recaptcha-response")).thenReturn("g-recaptcha-response");
        when(req.getRequestDispatcher("/views/registration/registration.jsp")).thenReturn(requestDispatcher);
        registrationServlet.doPost(req, resp);
        verify(req).setAttribute("error", "captchaError");
    }
}
