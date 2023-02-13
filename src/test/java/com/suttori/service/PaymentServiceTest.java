package com.suttori.service;

import com.suttori.dao.OrderDAO;
import com.suttori.dao.UserDAO;
import com.suttori.entity.Order;
import com.suttori.entity.User;
import com.suttori.entity.enams.OrderStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    OrderService orderService;

    @InjectMocks
    private PaymentService paymentService;

    private User user = new User();

    private Order order = new Order();

    @Before
    public void setUp() {
        orderService = new OrderService();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void topUpBalance() {
        User user = new User();
        assertTrue(paymentService.topUpBalance(user, 2));
        assertNull(paymentService.error);
    }

    @Test
    public void topUpBalance_negativeSum() {
        User user = new User();
        assertFalse(paymentService.topUpBalance(user, -5));
        assertEquals(paymentService.error, "negativeSumError");
    }

    @Test
    public void topUpBalance_tooBigAmount() {
        User user = new User();
        assertFalse(paymentService.topUpBalance(user, Integer.MAX_VALUE));
        assertEquals(paymentService.error, "tooBigAmount");
    }

    @Test
    public void payForOrder() {
        user.setId(0);
        user.setBalance(10);
        order.setId(0);
        order.setPrice(5);
        order.setUserId(user.getId());
        order.setStatus(OrderStatus.ACCEPTED);
        when(orderService.getById(order.getId())).thenReturn(order);
        assertTrue(paymentService.payForOrder(user, order.getId()));
        assertNull(paymentService.error);
    }

    @Test
    public void payForOrder_notUsersOrder() {
        user.setId(0);
        user.setBalance(10);
        order.setId(0);
        order.setPrice(5);
        order.setUserId(1);
        order.setStatus(OrderStatus.ACCEPTED);
        when(orderService.getById(order.getId())).thenReturn(order);
        assertFalse(paymentService.payForOrder(user, order.getId()));
        assertEquals(paymentService.error, "notUsersOrderError");
    }

    @Test
    public void payForOrder_alreadyPay() {
        user.setId(0);
        user.setBalance(10);
        order.setId(0);
        order.setPrice(5);
        order.setUserId(user.getId());
        order.setStatus(OrderStatus.PAID);
        when(orderService.getById(order.getId())).thenReturn(order);
        assertFalse(paymentService.payForOrder(user, order.getId()));
        assertEquals(paymentService.error, "alreadyPayError");
    }

    @Test
    public void payForOrder_notEnoughMoney() {
        user.setId(0);
        user.setBalance(10);
        order.setId(0);
        order.setPrice(20);
        order.setUserId(user.getId());
        order.setStatus(OrderStatus.ACCEPTED);
        when(orderService.getById(order.getId())).thenReturn(order);
        assertFalse(paymentService.payForOrder(user, order.getId()));
        assertEquals(paymentService.error, "notEnoughMoneyError");
    }
}
