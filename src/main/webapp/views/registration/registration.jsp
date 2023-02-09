<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html>
<head>
    <title><fmt:message key="registration"/></title>
    <jsp:include page="../includes/headerStartPage.jsp"/>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
</head>
<body>
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


<div class="container-fluid">
    <div class="row">
        <div class="col-4">
            <form action="/registration" method="post">
                <div class="form-group">
                    <label for="name"><fmt:message key="name"/></label>
                    <input type="text" class="form-control" required id="name" name="firstName"
                           placeholder="<fmt:message key="enterName"/>">
                </div>
                <div class="form-group">
                    <label for="lastName"><fmt:message key="surname"/></label>
                    <input type="text" class="form-control" id="lastName" required name="lastName"
                           placeholder="<fmt:message key="enterSurname"/>">
                </div>
                <div class="form-group">
                    <label for="InputEmail"><fmt:message key="emailAddress"/></label>
                    <input type="email" class="form-control" required id="InputEmail" name="email"
                           aria-describedby="emailHelp" placeholder="<fmt:message key="enterEmail"/>">
                </div>
                <div class="form-group">
                    <label for="InputEmail"><fmt:message key="phoneNumber"/></label>
                    <input type="number" class="form-control" required id="phoneNumber" name="phoneNumber"
                           aria-describedby="emailHelp" placeholder="<fmt:message key="enterPhone"/>">
                </div>
                <div class="form-group">
                    <label for="oldPassword"><fmt:message key="password"/></label>
                    <input type="password" class="form-control" required id="oldPassword" name="password"
                           placeholder="<fmt:message key="enterPassword"/>">
                </div>
                <br>
                <div class="g-recaptcha" data-sitekey="6Lcy78sjAAAAANoBlcbai4Yz0qbsZ05y5W-9wlzs"></div>
                <br>
                <button type="submit" class="btn btn-primary"><fmt:message key="registration"/></button>
            </form>
        </div>

        <div class="col-4"></div>
        <div class="col-4"></div>
    </div>

</div>
</body>
</html>
