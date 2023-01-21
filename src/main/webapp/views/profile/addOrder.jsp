<%--
  Created by IntelliJ IDEA.
  User: y
  Date: 05.01.2023
  Time: 18:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Додати замовлення</title>
</head>
<body>
<jsp:include page="../includes/header.jsp"/>
<br>
<div>
    <form action="/profile/addOrder" method="post">
        <div>
            <label> Загальний опис проблеми</label>
            <br>
            <input type="text" name=nameOrder>
            <br>
            <label> Детальний опис проблеми</label>
            <br>
            <input type="text" name=description>
            <br>
            <br>
            <input value="Подати заявку" type="submit">
        </div>
    </form>
</div>
</body>
</html>
