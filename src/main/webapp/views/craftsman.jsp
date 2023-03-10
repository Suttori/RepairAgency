<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>
<!DOCTYPE html>
<html lang="En">
<head>
    <title><fmt:message key="header.craftsmanPage"/></title>
</head>
<body>
<jsp:include page="../views/includes/header.jsp"/>

<div class="container-fluid">
    <div class="row">
        <div class="col-2"></div>
        <div class="col-10">
            <form method="get" action="/views/craftsman" class="form-inline">
                <div class="row justify-content-evenly">
                    <div class="col-3">
                        <select name="status" class="form-control ml-2">
                            <option value="ALL"><fmt:message key="ALL"/></option>
                            <c:choose>
                                <c:when test="${param['status'] != null}">
                                    <option selected hidden value="${param['status']}">
                                        <fmt:message key="${param['status']}"/>
                                    </option>
                                    <option value="PAID"><fmt:message key="PAID"/></option>
                                </c:when>
                                <c:otherwise>
                                    <option value="PAID" selected><fmt:message key="PAID"/></option>
                                </c:otherwise>
                            </c:choose>
                            <option value="IN_PROGRESS"><fmt:message key="IN_PROGRESS"/></option>
                            <option value="COMPLETED"><fmt:message key="COMPLETED"/></option>
                            <option value="CANCELED"><fmt:message key="CANCELED"/></option>
                        </select>
                    </div>
                    <div class="col-3">
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

                            <option value="date ASC"><fmt:message key="date ASC"/></option>
                            <option value="date DESC"><fmt:message key="date DESC"/></option>
                            <option value="price ASC"><fmt:message key="price ASC"/></option>
                            <option value="price DESC"><fmt:message key="price DESC"/></option>
                        </select>
                    </div>
                    <div class="col-3">
                        <button type="submit" class="btn btn-primary ml-2"><fmt:message key="search"/></button>
                    </div>
                    <div class="col-3">
                    </div>
                </div>
            </form>
            <br>
            <div class="row row-cols-1 row-cols-md-3 g-4">
                <c:forEach var="order" items="${orders}">
                    <div class="col">
                        <div class="card h-100">
                            <div class="card-body">
                                <h5 class="card-title"><fmt:message key="orders.problem"/>: ${order.orderName}</h5>
                                <br>
                                <h6 class="card-subtitle mb-2"><fmt:message key="orders.status"/>: ${order.status}</h6>
                                <br>
                                <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                        data-bs-target="#exampleModal"
                                        data-bs-whatever="${order.description}"><fmt:message
                                        key="orders.showDescription"/>
                                </button>
                                <br>
                                <br>
                                <c:set var="orderUser" value="${order.getUser()}"/>
                                <h6 class="mt-0"><fmt:message key="orders.customer"/>: ${orderUser.fullName}</h6>
                                <br>
                                <c:if test="${order.craftsmanId > 0}">
                                    <c:set var="orderCraftsman" value="${order.getCraftsman()}"/>
                                    <h6 class="card-subtitle mb-2">
                                        <fmt:message
                                                key="orders.craftsman"/>: ${orderCraftsman.id}. ${orderCraftsman.getFullName()} </h6>
                                </c:if>
                                <c:if test="${order.price > 0}">
                                    <br>
                                    <h6 class="card-subtitle mb-2"><fmt:message
                                            key="orders.price"/>: ${order.price}</h6>
                                </c:if>
                            </div>
                            <div class="card-footer">
                                <div class="row">
                                    <div class="col">
                                        <small class="text-muted"> <fmt:message
                                                key="orders.date"/>: ${order.date}</small>
                                    </div>
                                    <div class="col">
                                        <c:if test="${!order.isCompleted() && !order.isPendingPayment() && !order.isCanceled()}">
                                            <div class="dropdown gap-2 d-md-flex justify-content-md-end">
                                                <button class="btn btn-outline-primary dropdown-toggle" type="button"
                                                        data-bs-toggle="dropdown" aria-expanded="false">
                                                    <fmt:message key="orders.changeStatus"/>
                                                </button>
                                                <ul class="dropdown-menu">
                                                    <c:if test="${order.isPaid()}">
                                                        <li><a class="dropdown-item"
                                                               href="/views/craftsman/execute?orderId=${order.id}">
                                                            <fmt:message key="IN_PROGRESS"/></a></li>
                                                    </c:if>
                                                    <c:if test="${order.isInProgress()}">
                                                        <li><a class="dropdown-item"
                                                               href="/views/craftsman/complete?orderId=${order.id}">
                                                            <fmt:message key="COMPLETED"/> </a></li>
                                                    </c:if>
                                                </ul>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <br>
            <jsp:include page="../views/includes/pagination.jsp"/>
        </div>
    </div>
</div>
<!-- ?????????????????? ???????? -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel"></h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-xxl">
                    <textarea class="form-control" id="recipient-name" rows="10"></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message
                        key="orders.close"/></button>
            </div>
        </div>
    </div>
</div>


<script>
    const exampleModal = document.getElementById('exampleModal')
    if (exampleModal) {
        exampleModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget
            const recipient = button.getAttribute('data-bs-whatever')
            const modalTitle = exampleModal.querySelector('.modal-title')
            const modalBodyInput = exampleModal.querySelector('.modal-body textarea')
            modalTitle.textContent = `<fmt:message key="orders.description"/> ${recipient}`
            modalBodyInput.value = recipient
        })
    }
</script>
</body>
</html>
