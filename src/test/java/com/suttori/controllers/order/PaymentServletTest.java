package com.suttori.controllers.order;

import com.suttori.controllers.authorization.LoginServlet;
import com.suttori.controllers.order.PaymentServlet;
import com.suttori.entity.Order;
import com.suttori.entity.User;
import com.suttori.service.OrderService;
import com.suttori.service.PaginationService;
import com.suttori.service.PaymentService;
import com.suttori.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
public class PaymentServletTest {


    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    User user;
    @Mock
    RequestDispatcher requestDispatcher;
    @Mock
    PaymentService paymentService;


    @InjectMocks
    PaymentServlet paymentServlet;


    @Test
    public void doGet_verifyTrue() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute("user")).thenReturn(user);
        when(req.getParameter("orderId")).thenReturn("1");
        when(paymentService.payForOrder(user, 1)).thenReturn(true);
        when(req.getRequestDispatcher("/profile/orders")).thenReturn(requestDispatcher);
        paymentServlet.doGet(req, resp);
        verify(req).setAttribute("message", "orderPaymentSuccess");
    }

    @Test
    public void doGet_verifyFalse() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute("user")).thenReturn(user);
        when(req.getParameter("orderId")).thenReturn("1");
        when(paymentService.payForOrder(user, 1)).thenReturn(false);
        when(req.getRequestDispatcher("/profile/orders")).thenReturn(requestDispatcher);
        paymentServlet.doGet(req, resp);
        verify(req).setAttribute("error", paymentService.error);
    }

    @Test
    public void doPost_verifyErrorSum() throws ServletException, IOException {
        when(req.getParameter("sum")).thenReturn("");
        paymentServlet.doPost(req, resp);
        verify(resp).sendRedirect("/profile/orders");
    }
}
