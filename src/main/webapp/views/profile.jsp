<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setLocale value="${sessionScope.lang}" scope="session"/>
<fmt:setBundle basename="resources"/>

<html>
<body>
<jsp:include page="../views/includes/header.jsp"/>



<p>${sessionScope.lang}</p>
<fmt:message key="logo"/>

<form>Головна сторінка</form>

<form action="/profile/editData" method="get">
    <input value="Редагувати профіль" type="submit">
</form>
<br>
<form action="/profile/addOrder" method="get">
    <input value="Додати замовлення" type="submit">
</form>
<br>
<form action="/profile/orders" method="get">
    <input value="Мої замовлення" type="submit">
</form>
<br>
<form action="/profile/logout" method="get">
    <input value="Вихід" type="submit">
</form>
</body>
</html>
