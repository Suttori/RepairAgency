<%@ page import="com.suttori.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<%User user = (User) request.getSession(false).getAttribute("user");%>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>
<html>
<head>
    <title><fmt:message key="header.userList"/></title>
</head>
<body>
<jsp:include page="../views/includes/header.jsp"/>

<div class="container mt-5">
    <form method="get" action="/userList" class="form-inline">
        <div class="row justify-content-evenly">
            <div class="col-4">
                <input type="email" name="email" class="form-control"
                       placeholder="<fmt:message key="search.byEmail"/>">
            </div>

            <div class="col-4">
                <select name="sort" class="form-control ml-2">
                    <c:choose>
                        <c:when test="${param['sort'] != null}">
                            <option selected hidden value="${param['sort']}">
                                <fmt:message key="${param['sort']}"/>
                            </option>
                            <option value="none"><fmt:message key="none"/></option>
                        </c:when>
                        <c:otherwise>
                            <option value="none" selected><fmt:message key="none"/></option>
                        </c:otherwise>
                    </c:choose>
                    <option value="first_name ASC"><fmt:message key="name ASC"/></option>
                    <option value="first_name DESC"><fmt:message key="name DESC"/></option>
                    <option value="balance ASC"><fmt:message key="balance ASC"/></option>
                    <option value="balance DESC"><fmt:message key="balance DESC"/></option>
                </select>
            </div>
            <div class="col-4">
                <button type="submit" class="btn btn-primary ml-2"><fmt:message key="search"/></button>
            </div>
        </div>
    </form>
    <ul class="list-group">
        <c:choose>
            <c:when test="${!users.isEmpty()}">
                <c:forEach var="user" items="${users}">
                    <li class="list-group-item ">
                        <div class="media">
                            <div class="media-body">
                                <div class="row justify-content-evenly">
                                    <div class="col-10">
                                        <div class="ms-2 me-auto">
                                            <div class="fw-bold">
                                                    ${user.fullName}
                                            </div>
                                                ${user.id}
                                            <fmt:message key="user.email"/>: ${user.email} <br>
                                            <fmt:message key="user.status"/>: ${user.role} <br>
                                            <fmt:message key="user.balance"/>: ${user.balance} UAH
                                        </div>
                                    </div>
                                    <div class="col-2">
                                        <c:if test="${!user.isManager()}">
                                        <button type="button" class="btn btn-primary d-flex justify-content-end"
                                                data-bs-toggle="modal"
                                                data-bs-target="#exampleModal${user.id}"><fmt:message
                                                key="user.topUpBalance"/>
                                        </button>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>

                    <!-- Modal -->
                    <div class="modal fade" id="exampleModal${user.id}" tabindex="-1"
                         aria-labelledby="exampleModalLabel"
                         aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h1 class="modal-title fs-5" id="exampleModalLabel">
                                        <fmt:message key="user.topUpBalanceHead"/>
                                    </h1>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <form method="post" action="/userList">
                                        <input type="hidden" name="userId" value="${user.id}">
                                        <fmt:message key="users.sure"/>
                                        <div class="input-wrapper">
                                            <input class="form-control input-with-icon" type="number"
                                                   name="sum">
                                            <label class="fa fa-dollar input-icon"></label>
                                            <br>
                                            <br>
                                            <button type="submit" class="btn btn-primary d-flex justify-content-end">
                                                <fmt:message key="user.submit"/></button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info" role="alert">
                    <fmt:message key="user.noUsers"/>
                </div>
            </c:otherwise>
        </c:choose>
    </ul>
    <br>
</div>

<jsp:include page="./includes/pagination.jsp"/>


</body>
</html>


