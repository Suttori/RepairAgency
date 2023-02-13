package com.suttori.controllers.order;

import com.suttori.controllers.order.AddOrderServlet;
import com.suttori.entity.Order;
import com.suttori.entity.User;
import com.suttori.service.OrderService;
import com.suttori.service.PaginationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Or;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddOrderServletTest {

    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    User user;
    @Mock
    OrderService orderService;
    @Mock
    Order order;
    @Mock
    RequestDispatcher requestDispatcher;

    @InjectMocks
    AddOrderServlet addOrderServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void doPost_verifyTrue() throws IOException, ServletException {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute("user")).thenReturn(user);
        when(req.getContextPath()).thenReturn("");
        user.setId(0);
        when(req.getParameter("nameOrder")).thenReturn("name");
        when(req.getParameter("description")).thenReturn("description");
        when(orderService.save(any())).thenReturn(true);
        addOrderServlet.doPost(req, resp);
        verify(orderService).save(any());
    }

    @Test
    public void doPost_verifyFalse() throws IOException, ServletException {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute("user")).thenReturn(user);
        when(req.getContextPath()).thenReturn("");
        user.setId(0);
        when(req.getParameter("nameOrder")).thenReturn("name");
        when(req.getParameter("description")).thenReturn("description");
        when(orderService.save(order)).thenReturn(false);
        when(req.getRequestDispatcher("/views/profile/addOrder.jsp")).thenReturn(requestDispatcher);
        addOrderServlet.doPost(req, resp);
        verify(req).setAttribute("error", orderService.error);
    }
}
