<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>
<html>
<head>
    <title><fmt:message key="title.registerSuccess"/></title>
    <jsp:include page="../includes/headerStartPage.jsp"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-4">
        </div>
        <div class="col-4">
            <fmt:message key="registerSuccess"/>
            <br>
            <br>
            <form action="/views/start-page.jsp" method="get">
                <button type="submit" class="btn btn-primary"><fmt:message key="understand"/></button>
            </form>
        </div>
        <div class="col-4">
        </div>
    </div>
</div>
</body>
</html>
