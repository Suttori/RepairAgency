<%--
  Created by IntelliJ IDEA.
  User: y
  Date: 18.12.2022
  Time: 17:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="En">
<head>
    <title>Зміна профіля</title>
</head>
<body>
<jsp:include page="../includes/header.jsp"/>
<div>
<form action="/profile/edit/data" method="post" title="form">
    Заповніть поля, які хочете змінити
    <br>
    <br>
    <div>
        <label> Ім'я</label>
        <br>
        <input type="text" name=firstName>
        <br>
        <label> Прізвище</label>
        <br>
        <input type="text" name=lastName>
        <br>
        <label> Email</label>
        <br>
        <input type="text" name=email>
        <br>
        <label> Номер телефону</label>
        <br>
        <input type="text" name=phoneNumber>
        <br>
        <br>
        <input value="Редагувати дані" type="submit">
        <br>
        <hr>
    </div>
</form>
<form action="/profile/edit/password" method="post">
    <div>
        <label> Старий пароль</label>
        <br>
        <input type="text" name=oldPassword>
        <br>
        <label> Новий пароль</label>
        <br>
        <input type="text" name=newPassword>
        <br>
        <label> Повторіть новий пароль</label>
        <br>
        <input type="text" name=newPasswordRepeat>
        <br>
        <br>
        <input value="Змінити пароль" type="submit">
    </div>
</form>
</div>
</body>
</html>
