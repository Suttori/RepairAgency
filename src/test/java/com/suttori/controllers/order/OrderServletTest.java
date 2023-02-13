package com.suttori.controllers.order;

import com.suttori.entity.*;
import com.suttori.entity.enams.OrderStatus;
import com.suttori.service.OrderService;
import com.suttori.service.PaginationService;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServletTest {

    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    User user;
    @Mock
    OrderService orderService;
    @Mock
    UserService userService;
    @Mock
    PaginationService paginationService;
    @Mock
    Pagination pagination;
    @Mock
    Sort sort;
    @Mock
    List<Order> orders;
    @Mock
    List<User> masters;
    @Mock
    RequestDispatcher requestDispatcher;

    @InjectMocks
    OrdersServlet ordersServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void doPost_True() throws ServletException, IOException {
        when(req.getParameter("orderId")).thenReturn("1");
        when(req.getParameter("craftsmanId")).thenReturn("1");
        when(req.getParameter("userId")).thenReturn("1");
        when(req.getParameter("description")).thenReturn("description");
        when(orderService.saveComment(anyInt(), any())).thenReturn(true);
        ordersServlet.doPost(req, resp);
        verify(resp).sendRedirect("/profile/orders");
    }

    @Test
    public void doPost_False() throws ServletException, IOException {
        when(req.getParameter("orderId")).thenReturn("1");
        when(req.getParameter("craftsmanId")).thenReturn("1");
        when(req.getParameter("userId")).thenReturn("1");
        when(req.getParameter("description")).thenReturn("description");
        when(orderService.saveComment(anyInt(), any())).thenReturn(false);
        when(req.getRequestDispatcher("/views/profile/orders.jsp")).thenReturn(requestDispatcher);
        when(req.getRequestURI()).thenReturn("/profile/orders/cancel");
        ordersServlet.doPost(req, resp);
        verify(req).setAttribute("error", orderService.error);
    }

    @Test
    public void doGet_Cancel() throws ServletException, IOException {
        when(req.getRequestURI()).thenReturn("/profile/orders/cancel");
        when(req.getContextPath()).thenReturn("");
        when(req.getParameter("orderId")).thenReturn("1");

        ordersServlet.doGet(req, resp);
        verify(orderService).setOrderStatus(1, OrderStatus.CANCELED);
        verify(resp).sendRedirect("/profile/orders");
    }

    @Test
    public void doGet() throws ServletException, IOException {
        when(req.getRequestURI()).thenReturn("/profile/orders/");
        when(paginationService.getLimitOffsetPage(req)).thenReturn(pagination);
        when(paginationService.setSortParams(req)).thenReturn(sort);
        when(orderService.getSortedOrders(true, sort, pagination)).thenReturn(orders);
        when(sort.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(1);
        when(userService.getUserMasters(sort.getUser().getId())).thenReturn(masters);
        when(req.getRequestDispatcher("/views/profile/orders.jsp")).thenReturn(requestDispatcher);
        ordersServlet.doGet(req, resp);
        verify(requestDispatcher).forward(req, resp);
    }
}
