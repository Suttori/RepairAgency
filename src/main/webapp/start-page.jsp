<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false"%>


<!doctype html>
<html lang="en">
<head>
    <title>Початкова сторінка</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://bootstrap-4.ru/docs/5.2/assets/css/docs.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body>
<c:if test="${error != null}">
    <div class="alert alert-danger" role="alert">
        <fmt:message key="${error}"/>
    </div>
</c:if>
<form action="/main" method="post">
    <div>
        <input type="text" name=email placeholder="E-mail">
        <br>
        <input name="password" type="password" placeholder="Пароль">
        <br>
        <label for="remember_me"> Запам'ятати мене</label>
        <input type="checkbox" id="remember_me" name="rememberMe">
        <br>
        <a href="views/authorization/password-reset.jsp"> Забули пароль?</a>
        <br>
        <div class="g-recaptcha" data-sitekey="6Lcy78sjAAAAANoBlcbai4Yz0qbsZ05y5W-9wlzs"></div>
        <br>
        <input value="Увійти" type="submit">
        <br>
    </div>
    <br>
</form>
<form action="/registration" method="get">
    <input value="Зареєструватися" type="submit">
</form>
</body>
</html>
