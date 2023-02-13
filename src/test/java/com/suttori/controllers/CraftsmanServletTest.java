package com.suttori.controllers;

import com.suttori.controllers.order.OrdersServlet;
import com.suttori.entity.*;
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
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CraftsmanServletTest {

    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    User craftsman;
    @Mock
    OrderService orderService;
    @Mock
    PaginationService paginationService;
    @Mock
    Pagination pagination;
    @Mock
    Sort sort;
    @Mock
    List<Order> orders;
    @Mock
    RequestDispatcher requestDispatcher;

    @InjectMocks
    CraftsmanServlet craftsmanServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void doGet() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(craftsman);
        when(craftsman.isCraftsman()).thenReturn(true);
        when(req.getRequestURI()).thenReturn("");
        when(paginationService.getLimitOffsetPage(req)).thenReturn(pagination);
        when(paginationService.setSortParams(req)).thenReturn(sort);
        when(orderService.getSortedOrders(true, sort, pagination)).thenReturn(orders);
        when(req.getRequestDispatcher("/views/craftsman.jsp")).thenReturn(requestDispatcher);
        craftsmanServlet.doGet(req, resp);
        verify(requestDispatcher).forward(req, resp);
    }
}
