<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<c:set value='${sessionScope["user"]}' var="user"/>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>
<!DOCTYPE html>

<title><fmt:message key="orders"/></title>
<body>
<jsp:include page="../includes/header.jsp"/>

<div class="container-fluid">
    <div class="row">
        <div class="col-2">
            <c:if test="${error != null}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="${error}"/>
                </div>
            </c:if>
            <c:if test="${message != null}">
                <div class="alert alert-success" role="alert">
                    <fmt:message key="${message}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Закрыть"></button>
                </div>
            </c:if>
        </div>
        <div class="col-10">
            <form method="get" action="/profile/orders" class="form-inline">
                <div class="row justify-content-evenly">
                    <div class="col-3">
                        <select name="master" class="form-control">
                            <c:choose>
                                <c:when test="${param['master'] != null && param['master'] != -1}">
                                    <option selected hidden value="${param["master"]}">
                                        <fmt:message key="search.selectedMaster"/>
                                    </option>
                                    <option value="-1"><fmt:message key="search.noneMaster"/></option>
                                </c:when>
                                <c:otherwise>
                                    <option value="-1" selected><fmt:message key="search.noneMaster"/></option>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="master" items="${masters}">
                                <option value="${master.id}">${master.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-3">
                        <select name="status" class="form-control ml-2">
                            <c:choose>
                                <c:when test="${param['status'] != null}">
                                    <option selected hidden value="${param['status']}">
                                        <fmt:message key="${param['status']}"/>
                                    </option>
                                    <option value="ALL"><fmt:message key="ALL"/></option>
                                </c:when>
                                <c:otherwise>
                                    <option value="ALL" selected><fmt:message key="ALL"/></option>
                                </c:otherwise>
                            </c:choose>
    <option value="ACCEPTED"><fmt:message key="ACCEPTED"/></option>
                            <option value="PENDING_PAYMENT"><fmt:message key="PENDING_PAYMENT"/></option>
                            <option value="PAID"><fmt:message key="PAID"/></option>
                            <option value="CANCELED"><fmt:message key="CANCELED"/></option>
                            <option value="IN_PROGRESS"><fmt:message key="IN_PROGRESS"/></option>
                            <option value="COMPLETED"><fmt:message key="COMPLETED"/></option>
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
                </div>
            </form>
            <br>

            <c:choose>
                <c:when test="${orders != null }">
                    <div class="row row-cols-1 row-cols-md-3 g-4">
                        <c:forEach var="order" items="${orders}">
                            <div class="col">
                                <div class="card h-100">
                                    <div class="card-body">
                                        <h5 class="card-title"><fmt:message key="orders.problem"/>: ${order.orderName}
                                        </h5>
                                        <br>
                                        <h6 class="card-subtitle mb-2"><fmt:message
                                                key="orders.status"/>: ${order.status}</h6>
                                        <br>
                                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                                data-bs-target="#exampleModal"
                                                data-bs-whatever="${order.description}"><fmt:message
                                                key="orders.showDescription"/>
                                        </button>
                                        <br>
                                        <br>
                                        <c:if test="${order.craftsmanId > 0}">
                                            <c:set var="orderCraftsman" value="${order.getCraftsman()}"/>
                                            <h6 class="card-subtitle mb-2"><fmt:message
                                                    key="orders.craftsman"/>: ${orderCraftsman.getFullName()}</h6>
                                        </c:if>
                                        <c:if test="${order.price > 0}">
                                            <br>
                                            <h6 class="card-subtitle mb-2"><fmt:message
                                                    key="orders.price"/>: ${order.price}</h6>
                                        </c:if>

                                        <c:if test="${order.isPendingPayment()}">
                                            <form action="/payment" method="get">
                                                <input type="hidden" name="orderId" value="${order.id}">
                                                <button type="submit" class="btn btn-primary"><fmt:message
                                                        key="orders.pay"/></button>
                                            </form>
                                        </c:if>
                                        <c:if test="${order.isCompleted() && order.commentId <= 0}">
                                            <form action="/profile/orders" method="post">
                                                <input type="hidden" name="orderId" value="${order.id}">
                                                <input type="hidden" name="craftsmanId" value="${order.craftsmanId}">
                                                <input type="hidden" name="userId" value="${order.userId}">
                                                <label for="textarea"> <fmt:message key="orders.comment"/></label>
                                                <br>
                                                <div class="form-group">
                                                    <small class="form-text text-muted"> <fmt:message
                                                            key="orders.noComment"/> </small>
                                                    <textarea class="form-control" id="textarea"
                                                              name="description"></textarea>
                                                </div>
                                                <br>
                                                <button type="submit" class="btn btn-primary"><fmt:message
                                                        key="orders.submit"/></button>
                                            </form>
                                        </c:if>
                                    </div>
                                    <div class="card-footer">
                                        <div class="row">
                                            <div class="col">
                                                <small class="text-muted"> <fmt:message
                                                        key="orders.date"/>: ${order.date}</small>
                                            </div>
                                            <div class="col">
                                                <c:if test="${!order.isCanceled() && !order.isCompleted()}">
                                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                                        <a href="/profile/orders/cancel?orderId=${order.id}"
                                                           class="btn btn-outline-danger btn-sm float-right">
                                                            <fmt:message key="orders.canceled"/></a>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info" role="alert">
                        <fmt:message key="orders.noOrders"/>
                    </div>
                </c:otherwise>
            </c:choose>
            <br>
            <jsp:include page="../includes/pagination.jsp"/>
        </div>
    </div>
</div>

<!-- Modal -->
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
