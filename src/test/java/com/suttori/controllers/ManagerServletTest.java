package com.suttori.controllers;

import com.suttori.entity.Order;
import com.suttori.entity.Pagination;
import com.suttori.entity.Sort;
import com.suttori.entity.User;
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
public class ManagerServletTest {


    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    User manager;
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
    ManagerServlet managerServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void doGet() throws ServletException, IOException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(manager);
        when(manager.isManager()).thenReturn(true);
        when(req.getRequestURI()).thenReturn("");
        when(paginationService.getLimitOffsetPage(req)).thenReturn(pagination);
        when(paginationService.setSortParams(req)).thenReturn(sort);
        when(orderService.getSortedOrders(true, sort, pagination)).thenReturn(orders);
        when(userService.getAllMasters()).thenReturn(masters);
        when(req.getRequestDispatcher("/views/managerPage.jsp")).thenReturn(requestDispatcher);
        managerServlet.doGet(req, resp);
        verify(requestDispatcher).forward(req, resp);
    }
}
