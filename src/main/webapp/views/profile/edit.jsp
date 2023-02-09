<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>
<!DOCTYPE html>
<html lang="En">
<head>
    <title><fmt:message key="header.editProfile"/></title>
</head>
<body>
<jsp:include page="../includes/header.jsp"/>
<c:if test="${error != null}">
    <div class="alert alert-danger" role="alert">
        <fmt:message key="${error}"/>
    </div>
</c:if>

<div class="container mt-5">

<div>


    <form action="/profile/edit/data" method="post" title="form">
        <fmt:message key="titleEdit"/>
        <br>

        <div class="form-group">
            <label for="name"><fmt:message key="name"/></label>
            <input type="text" class="form-control" id="name"  name="firstName"
                   placeholder="<fmt:message key="enterName"/>">
        </div>
        <div class="form-group">
            <label for="lastName"><fmt:message key="surname"/></label>
            <input type="text" class="form-control" id="lastName" name="lastName"
                   placeholder="<fmt:message key="enterSurname"/>">
        </div>

        <div class="form-group">
            <label for="phoneNumber"><fmt:message key="phoneNumber"/></label>
            <input type="number" class="form-control" id="phoneNumber" name="phoneNumber"
                   aria-describedby="emailHelp" placeholder="<fmt:message key="enterPhone"/>">
        </div>
        <div class="col-sm-6 mt-3">
            <button type="submit" class="btn btn-primary"><fmt:message key="editData"/></button>
        </div>
    </form>

        <br>
    <br>
    <form action="/profile/edit/password" method="post">
        <div class="form-group">
            <label for="oldPassword"><fmt:message key="oldPassword"/></label>
            <input type="password" class="form-control" required id="oldPassword" name="oldPassword"
                   placeholder="<fmt:message key="enterOldPassword"/>">
        </div>
        <div class="form-group">
            <label for="newPassword"><fmt:message key="newPassword"/></label>
            <input type="password" class="form-control" required id="newPassword" name="newPassword"
                   placeholder="<fmt:message key="enterNewPassword"/>">
        </div>
        <div class="form-group">
            <label for="newPasswordRepeat"><fmt:message key="newPasswordRepeat"/></label>
            <input type="password" class="form-control" required id="newPasswordRepeat" name="newPasswordRepeat"
                   placeholder="<fmt:message key="enterNewPasswordRepeat"/>">
        </div>
        <div class="col-sm-6 mt-3">
            <button type="submit" class="btn btn-primary"><fmt:message key="changePassword"/></button>
        </div>
    </form>
</div>
</div>
</body>
</html>
