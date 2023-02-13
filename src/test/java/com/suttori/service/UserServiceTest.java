package com.suttori.service;


import com.suttori.dao.UserDAO;
import com.suttori.entity.Pagination;
import com.suttori.entity.Sort;
import com.suttori.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDAO daoMock;

    @Mock
    private EmailSenderService mailService;

    @InjectMocks
    private UserService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() {
        when(daoMock.insert(any(User.class))).thenReturn(true);
        User user = new User();
        user.setPassword("12345");
        user.setFirstName("Name");
        user.setLastName("Surname");
        user.setEmail("email");
        user.setPhoneNumber("380999999999");
        assertThat(service.save(user), is(notNullValue()));
    }

    @Test
    public void loginTest(){
        User user = new User();
        user.setPassword("2f1982d030a065ad181040d76c4fca58");
        user.setSalt(new byte[10]);
        user.setEmailActivated("true");
        when(daoMock.findByEmail(any(String.class))).thenReturn(user);
        assertTrue(service.userLogin("email", "password"));
        assertNull(service.error);
    }

    @Test
    public void loginTest_userNotFound(){
        when(daoMock.findByEmail(any(String.class))).thenReturn(null);
        assertFalse(service.userLogin("email", "password"));
        assertEquals(service.error, "userNotFoundError");
    }

    @Test
    public void loginTest_passwordDoNotMatch(){
        User user = new User();
        user.setPassword("");
        user.setSalt(new byte[10]);
        user.setEmailActivated("true");
        when(daoMock.findByEmail(any(String.class))).thenReturn(user);
        assertFalse(service.userLogin("email", "password"));
        assertEquals(service.error, "passwordError");
    }

    @Test
    public void loginTest_emailNotActivated(){
        User user = new User();
        user.setPassword("2f1982d030a065ad181040d76c4fca58");
        user.setSalt(new byte[10]);
        user.setEmailActivated("false");
        when(daoMock.findByEmail(any(String.class))).thenReturn(user);
        assertFalse(service.userLogin("email", "password"));
        assertEquals(service.error, "emailNotActivateError");
    }

    @Test
    public void testChangePassword(){
        User user = new User();
        user.setPassword("2f1982d030a065ad181040d76c4fca58");
        user.setSalt(new byte[10]);
        assertTrue(service.changePassword(user, "password", "newPassword", "newPassword"));
        assertNull(service.error);
    }

    @Test
    public void testChangePassword_oldPasswordWrong(){
        User user = new User();
        user.setPassword("2f1982d030a065ad181040d76c4fca58");
        user.setSalt(new byte[10]);
        assertFalse(service.changePassword(user, "oldPass", "newPass", "newPass"));
        assertEquals(service.error, "oldPasswordError");
    }

    @Test
    public void testChangePassword_newPasswordShort(){
        User user = new User();
        user.setPassword("2f1982d030a065ad181040d76c4fca58");
        user.setSalt(new byte[10]);
        assertFalse(service.changePassword(user, "password", "Pas", "Pas"));
        assertEquals(service.error, "passwordShortError");
    }


    @Test
    public void testChangePassword_differentNewPasswords(){
        User user = new User();
        user.setPassword("2f1982d030a065ad181040d76c4fca58");
        user.setSalt(new byte[10]);
        assertFalse(service.changePassword(user, "password", "newPass", "newPasss"));
        assertEquals(service.error, "repeatPasswordError");
    }

    @Test
    public void testEmailActivate() {
        User user = new User();
        when(daoMock.findByActivationCode(any(String.class))).thenReturn(user);
        assertTrue(service.activateEmail("code"));
        assertNull(service.error);
    }

    @Test
    public void testEmailActivate_error() {
        when(daoMock.findByActivationCode(any(String.class))).thenReturn(null);
        assertFalse(service.activateEmail("code"));
        assertEquals(service.error, "emailActivateError");
    }

    @Test
    public void editProfile_shouldChangeFullNameAndPhone() {
        User user = new User();
        user.setId(0);
        user.setFirstName("name");
        user.setLastName("surname");
        user.setPhoneNumber("380999999999");

        assertTrue(service.editProfile(user, "newName", "newLastName", "380999999990"));
        verify(daoMock).setVariable("first_name", user.getId(), "newName");
        verify(daoMock).setVariable("last_name", user.getId(), "newLastName");
        verify(daoMock).setVariable("phone_number", user.getId(), "380999999990");
    }

    @Test
    public void getSortedUsers() {
        List<User> userList = new ArrayList<>();
        User user = new User();
        Sort sort = new Sort();
        Pagination pagination = new Pagination();

        Map<String, Object> filterParams = new HashMap<>();
        String sortingParams;
        Map<String, Integer> limitingParams = new LinkedHashMap<>();

        filterParams.put("email", "email");
        sortingParams = "date DESC";
        limitingParams.put("OFFSET", 6);
        limitingParams.put("LIMIT", 6);
//        Map<String, Object> filterParamsExpected = new HashMap<>();
//        String sortingParamsExpected;
//        Map<String, Integer> limitingParamsExpected = new LinkedHashMap<>();
//
//        filterParamsExpected.put("email", "email");
//        sortingParamsExpected = "date DESC";
        sort.setUser(user);
        sort.setEmail("email");
        sort.setSort("date DESC");

        pagination.setOffset(6);

        assertEquals(service.getSortedUsers(sort, pagination), userList);
//        assertEquals(filterParams, filterParamsExpected);
//        assertEquals(sortingParams, sortingParamsExpected);
//       // assertEquals(limitingParams, limitingParamsExpected);

        verify(daoMock).findBy(filterParams, sortingParams, limitingParams);
    }

    @Test
    public void generateSalt() {
        assertThat(service.generateSalt(), is(notNullValue()));
    }
}