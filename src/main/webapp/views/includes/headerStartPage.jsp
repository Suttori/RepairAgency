<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="suttoriTags" prefix="stt" %>
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
        <a class="navbar-brand"> <fmt:message key="logo"/></a>
        <div class="container">
            <ul class="nav justify-content-end">
                <li class="nav-item">
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
                <li>
                    __
                </li>
                <li style="color: aliceblue">
                    <stt:dateTag/>
                </li>
            </ul>
        </div>
    </div>
</nav>
</body>
</html>
