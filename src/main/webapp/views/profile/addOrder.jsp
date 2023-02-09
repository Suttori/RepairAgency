<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" scope="session"/>
<fmt:setBundle basename="resources"/>
<!DOCTYPE html>
<head>
    <title><fmt:message key="header.addOrder"/></title>
</head>
<body>
<jsp:include page="../includes/header.jsp"/>
<br>
<c:if test="${error != null}">
    <div class="alert alert-danger" role="alert">
        <fmt:message key="${error}"/>
    </div>
</c:if>
<div>
    <form action="/profile/addOrder" method="post">
        <div class="row">
            <div class="col-2">
            </div>
            <div class="col-4">
                <div class="mb-3">
                    <label for="exampleFormControlInput1" class="form-label"><fmt:message key="addOrder.name"/></label>
                    <input type="text" name=nameOrder class="form-control" id="exampleFormControlInput1"
                           placeholder="<fmt:message key="addOrder.placeholderName"/>">
                </div>
                <div class="mb-4">
                    <label for="exampleFormControlTextarea1" class="form-label"><fmt:message key="addOrder.description"/></label>
                    <textarea class="form-control" name=description id="exampleFormControlTextarea1" rows="3"
                              placeholder="<fmt:message key="addOrder.placeholderDescription"/>"></textarea>
                    <br>
                    <button type="submit" class="btn btn-primary"> <fmt:message key="addOrder.submit"/></button>
                </div>
            </div>
            <div class="col-2">
            </div>
        </div>
    </form>
</div>
</body>
</html>
