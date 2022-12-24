<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false"%>

<html>
<body>
<form action="/main" method="post">
    <div>
        <input type="email" name=email placeholder="E-mail">
        <br>
        <input name="password" type="password" placeholder="Пароль">
        <br>
        <label for="remember_me"> Запам'ятати мене</label>
        <input type="checkbox" id="remember_me" name="rememberMe">
        <br>
        <a href="views/password-reset.jsp"> Забули пароль?</a>
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
