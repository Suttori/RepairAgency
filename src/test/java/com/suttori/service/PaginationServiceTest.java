package com.suttori.service;

import com.suttori.entity.Pagination;
import com.suttori.entity.Sort;
import com.suttori.entity.User;
import com.suttori.entity.enams.OrderStatus;
import jakarta.mail.Session;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaginationServiceTest {

    @Mock
    HttpServletRequest req;
    @Mock
    HttpSession session;
    @Mock
    Sort sort = new Sort();
    @Mock
    Pagination pagination = new Pagination();

    @Mock
    private User user;

    @InjectMocks
    private PaginationService paginationService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setSortParams() {
        User user = new User();
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute("user")).thenReturn(user);
        assertEquals(paginationService.setSortParams(req), sort);
    }

    @Test
    public void getLimitOffsetPage() {
        assertEquals(paginationService.getLimitOffsetPage(req), pagination);
    }
}
