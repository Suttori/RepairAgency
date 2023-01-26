<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!doctype html>


<fmt:setLocale value="${sessionScope.lang}" scope="session"/>
<fmt:setBundle basename="resources"/>


<head>

    <title></title>
    <jsp:include page="../includes/links.jsp"/>
    <c:set var="user" value='${sessionScope["user"]}'/>


</head>

<body class="p-3 m-0 border-0 bd-example">
<nav class="navbar navbar-dark bg-dark fixed-top">
    <div class="container-fluid">
        <a class="navbar-brand" href="/views/profile.jsp"> <fmt:message key="logo"/></a>

        <div class="container">
            <ul class="nav justify-content-end">
                <li class="nav-item">


                    <div class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle" type="button" aria-haspopup="true"
                                data-bs-toggle="dropdown"
                                aria-expanded="false">
                            <c:out value="${user.balance}"/> UAH
                        </button>
                        <form class="dropdown-menu p-3" action="/payment" method="post">
                            <div class="mb-3">
                                <label for="sum" class="form-label"> <fmt:message key="header.topUpBalance"/></label>
                                <input type="text" class="form-control" name="sum" id="sum"
                                       placeholder="<fmt:message key="header.enterSum"/>">
                            </div>
                            <button type="submit" class="btn btn-primary"><fmt:message key="header.replenish"/></button>
                        </form>
                    </div>
                </li>
                <li>
                    <label>__</label>
                </li>
                <li class="nav-item">
                    <div class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown"
                                aria-expanded="false">
                            <c:out value="${sessionScope.lang}"/>
                        </button>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="?lang=En"><fmt:message key="header.en"/></a></li>
                            <li><a class="dropdown-item" href="?lang=Ua"><fmt:message key="header.ua"/></a></li>
                        </ul>
                    </div>
                </li>
            </ul>
        </div>
        <button class="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasDarkNavbar"
                aria-controls="offcanvasDarkNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="offcanvas offcanvas-end text-bg-dark" tabindex="-1" id="offcanvasDarkNavbar"
             aria-labelledby="offcanvasDarkNavbarLabel">
            <div class="offcanvas-header">
                <h5 class="offcanvas-title" id="offcanvasDarkNavbarLabel"> Головне меню </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="offcanvas"
                        aria-label="Close"></button>
            </div>
            <div class="offcanvas-body">
                <ul class="navbar-nav justify-content-end flex-grow-1 pe-3">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/views/profile.jsp"> Головна </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/profile/editData"> Редагувати профіль </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/profile/addOrder"> Додати замовлення </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/profile/orders"> Мої замовлення </a>
                    </li>
                    <c:if test="${user.isManager()}">
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="/views/managerPage"> Сторінка
                                менеджера </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="/userList"> Список користувачів </a>
                        </li>
                    </c:if>
                    <c:if test="${user.isCraftsman()}">
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="/views/craftsman"> Сторінка
                                майстра </a>
                        </li>
                    </c:if>
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/profile/logout"> Вихід </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</nav>
</body>
</html>
