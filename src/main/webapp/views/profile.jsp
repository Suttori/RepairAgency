<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>

<html>
<body>
<form>Головна сторінка</form>



<form action="/profile/editData" method="get">
    <input value="Редагувати профіль" type="submit">
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
