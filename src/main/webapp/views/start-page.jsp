<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<!doctype html>
<html lang="en">
<head>
    <title><fmt:message key="logo"/></title>
    <jsp:include page="../views/includes/headerStartPage.jsp"/>

</head>

<body>
<div> </div>
<c:if test="${message != null}">
    <div class="alert alert-success" role="alert">
        <fmt:message key="${message}"/>
    </div>
</c:if>
<c:if test="${error != null}">
    <div class="alert alert-danger" role="alert">
        <fmt:message key="${error}"/>
    </div>
</c:if>
<div class="container mt-5">
    <form action="/main" method="post">
        <div class="form-group">
            <label for="exampleInputEmail1"><fmt:message key="emailAddress"/></label>
            <input type="text" class="form-control" required id="exampleInputEmail1" name="email"
                   aria-describedby="emailHelp" placeholder="<fmt:message key="enterEmail"/>">
        </div>
        <div class="form-group">
            <label for="exampleInputPassword1"><fmt:message key="password"/> </label>
            <input type="password" class="form-control" required id="exampleInputPassword1" name="password"
                   placeholder="<fmt:message key="enterPassword"/>">
        </div>
        <div class="form-group form-check">
            <input type="checkbox" class="form-check-input" id="check" name="rememberMe">
            <label class="form-check-label" for="check"><fmt:message key="rememberMe"/></label>
        </div>
        <br>
        <div class="col-sm-6 mt-3">
            <button type="submit" class="btn btn-primary"><fmt:message key="login"/> </button>
        </div>
    </form>
    <br>
    <form action="/registration" method="get">
        <button type="submit" class="btn btn-primary"><fmt:message key="registration"/> </button>
    </form>
</div>
</body>
</html>
