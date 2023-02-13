package com.suttori.service;

import com.suttori.dao.CommentDAO;
import com.suttori.dao.OrderDAO;
import com.suttori.entity.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderDAO daoMock;

    @Mock
    private CommentDAO commentDAO;

    @Mock
    private User user;

    @InjectMocks
    private OrderService service;

    Order order = new Order();
    Sort sort = new Sort();
    Pagination pagination = new Pagination();

    @Before
    public void setUp() {
        order.setId(0);
        order.setUserId(0);
        order.setOrderName("problem problem");
        order.setDescription("problem problem");
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setSort() {
        sort.setUser(user);
        sort.setMasterId("0");
        sort.setStatus("0");
        sort.setEmail("email");
        sort.setSort("date DESC");
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setPagination() {
        pagination.setOffset(6);
        pagination.setPage(1);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() {
        when(daoMock.insert(order)).thenReturn(true);
        assertTrue(service.save(order));
        assertNull(service.error);
    }

    @Test
    public void testSave_nameValidation() {
        order.setOrderName("short");
        assertFalse(service.save(order));
        assertEquals(service.error, "problemNameError");
    }

    @Test
    public void testSave_descriptionValidation() {
        order.setDescription("short");
        assertFalse(service.save(order));
        assertEquals(service.error, "descriptionNameError");
    }

    @Test
    public void testSaveManagerAnswer() {
        when(daoMock.findById(order.getId())).thenReturn(order);
        assertTrue(service.saveManagerAnswer(10, 0, 0));
        assertNull(service.error);
    }

    @Test
    public void testSaveManagerAnswer_lowPrice() {
        assertFalse(service.saveManagerAnswer(0, 0, 0));
        assertEquals(service.error, "priceLowError");
    }


    @Test
    public void testRateOrder() {
        Comment comment = new Comment();
        comment.setDescription("DescriptionLongLong ");
        assertTrue(service.saveComment(0, comment));
        assertNull(service.error);
    }

    @Test
    public void testRateOrder_shortDescription() {
        Comment comment = new Comment();
        comment.setDescription("1");
        assertFalse(service.saveComment(0, comment));
        assertEquals(service.error, "descriptionShortError");
    }

    @Test
    public void getSortedUsers() {
        List<Order> userList = new ArrayList<>();
        User user = new User();
        Sort sort = new Sort();
        Pagination pagination = new Pagination();

        Map<String, Object> filterParams = new HashMap<>();
        String sortingParams;
        Map<String, Integer> limitingParams = new LinkedHashMap<>();

        filterParams.put("user_id", 0);
        sortingParams = "date DESC";
        limitingParams.put("LIMIT", 6);
        limitingParams.put("OFFSET", 6);

//        Map<String, Object> filterParamsExpected = new HashMap<>();
//        String sortingParamsExpected;
//        Map<String, Integer> limitingParamsExpected = new LinkedHashMap<>();
//
//        filterParamsExpected.put("email", "email");
//        sortingParamsExpected = "date DESC";
        sort.setUser(user);
        sort.setSort("date DESC");

        pagination.setOffset(6);

        assertEquals(service.getSortedOrders(true, sort, pagination), userList);
//        assertEquals(filterParams, filterParamsExpected);
//        assertEquals(sortingParams, sortingParamsExpected);
//       // assertEquals(limitingParams, limitingParamsExpected);

        verify(daoMock).findBy(filterParams, sortingParams, limitingParams);
    }

    @Test
    public void getSortedUsers_notUser() {
        List<Order> orderList = new ArrayList<>();
        User user = new User();
        Sort sort = new Sort();
        Pagination pagination = new Pagination();

        Map<String, Object> filterParams = new HashMap<>();
        String sortingParams;
        Map<String, Integer> limitingParams = new LinkedHashMap<>();

        sortingParams = "date DESC";
        limitingParams.put("LIMIT", 6);
        limitingParams.put("OFFSET", 6);

        sort.setUser(user);
        sort.setSort("date DESC");
        pagination.setOffset(6);

        assertEquals(service.getSortedOrders(false, sort, pagination), orderList);
        verify(daoMock).findBy(filterParams, sortingParams, limitingParams);
    }
}