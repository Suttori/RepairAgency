<%--
  Created by IntelliJ IDEA.
  User: y
  Date: 26.11.2022
  Time: 23:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Реєстрація</title>
</head>
<body>
<form action="/registration" method="post">
    <div>
        <label> Ім'я</label>
        <br>
        <input type="text" name=firstName>
        <br>
        <label> Прізвище</label>
        <br>
        <input type="text" name=lastName>
        <br>
        <label> Електронна адреса</label>
        <br>
        <input type="text" name=email>
        <br>
        <label> Номер телефону</label>
        <br>
        <input type="text" name=phoneNumber>
        <br>
        <label> Пароль</label>
        <br>
        <input type="text" name=password>

        <br>
        <input value="Зареєструватись" type="submit">
        <br>
    </div>
    <br>
</form>
</body>
</html>
