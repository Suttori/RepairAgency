<%--
  Created by IntelliJ IDEA.
  User: y
  Date: 18.12.2022
  Time: 17:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="En">
<head>

</head>
<body>
<jsp:include page="../views/includes/header.jsp"/>

<div class="container-fluid">
    <div class="row">
        <div class="col-2"></div>
        <div class="col-10">


            <form method="get" action="/views/managerPage" class="form-inline">
                <div class="row justify-content-evenly">

                    <div class="col-3">
                        <select name="master" class="form-control">
                            <c:choose>
                                <c:when test="${param['master'] != null && param['master'] != -1}">
                                    <option selected hidden value="${param["master"]}">
                                        Майстер
                                    </option>
                                    <option value="-1"> Майстер не призначений</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="-1" selected> Всі майстри </option>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="master" items="${masters}">
                                <option value="${master.id}">${master.fullName}
                                    , ${master.email}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-3">
                        <select name="status" class="form-control ml-2">
                            <option value="ALL"> Всі</option>
                            <c:choose>
                                <c:when test="${param['status'] != null}">
                                    <option selected hidden value="${param['status']}">
                                        <fmt:message key="${param['status']}"/>
                                    </option>
                                    <option value="ACCEPTED">ACCEPTED</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="ACCEPTED" selected>ACCEPTED</option>
                                </c:otherwise>
                            </c:choose>
                            <option value="PENDING_PAYMENT">PENDING_PAYMENT</option>
                            <option value="PAID">PAID</option>
                            <option value="CANCELED">CANCELED</option>
                            <option value="IN_PROGRESS">IN_PROGRESS</option>
                            <option value="COMPLETED">COMPLETED</option>
                        </select>
                    </div>
                    <div class="col-3">
                        <select name="sort" class="form-control ml-2">
                            <c:choose>
                                <c:when test="${param['sort'] != null}">
                                    <option selected hidden value="${param['sort']}">
                                        <fmt:message key="${param['sort']}"/>
                                    </option>
                                    <option value="none">none</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="none" selected> Без сортування</option>
                                </c:otherwise>
                            </c:choose>
                            <option value="date ASC">date ASC</option>
                            <option value="date DESC">date DESC</option>
                            <option value="price ASC">price ASC</option>
                            <option value="price DESC">price DESC</option>
                        </select>
                    </div>
                    <div class="col-3">
                        <button type="submit" class="btn btn-primary ml-2"> Пошук </button>
                    </div>


                </div>
            </form>

            <br>

            <div class="row row-cols-1 row-cols-md-3 g-4">
                <c:forEach var="order" items="${orders}">
                    <div class="col">
                        <div class="card h-100">
                            <div class="card-body">
                                <h5 class="card-title"> Проблема: ${order.orderName}</h5>
                                <br>
                                <h6 class="card-subtitle mb-2"> Статус: ${order.status}</h6>
                                <br>
                                <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                        data-bs-target="#exampleModal"
                                        data-bs-whatever="${order.description}"> Переглянути детальний опис проблеми
                                </button>
                                <br>
                                <br>
                                <c:set var="orderUser" value="${order.getUser()}"/>
                                <h6 class="mt-0"> Замовник: ${orderUser.fullName}</h6>
                                <br>
                                <c:if test="${order.craftsmanId > 0}">
                                    <c:set var="orderCraftsman" value="${order.getCraftsman()}"/>
                                    <h6 class="card-subtitle mb-2"> Майстер: ${orderCraftsman.id} ${orderCraftsman.getFullName()} </h6>
                                </c:if>
                                <c:if test="${order.price > 0}">
                                    <br>
                                    <h6 class="card-subtitle mb-2"> Вартість послуги: ${order.price}</h6>
                                </c:if>

                                <c:if test="${order.isNew()}">
                                <div class="dropdown-divider"></div>
                                <form action="/views/managerPage" method="post">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <div class="form-group">
                                        <h6 class="card-subtitle mb-2"> Призначити майстра: </h6>

                                        <select required name="selectedMaster" class="form-control">
                                            <c:forEach var="master" items="${masters}">
                                                <option value="${master.id}">${master.fullName}
                                                    , ${master.email}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>Вартість послуги: </label>
                                        <input required type="number" name="price" class="form-control">
                                    </div>
                                    <button type="submit" class="btn btn-primary">Призначити</button>
                                </form>
                                </c:if>

                            </div>


                            <div class="card-footer">
                                <small class="text-muted"> Дата замовлення: ${order.date}</small>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <br>
            <jsp:include page="../views/includes/paginationManager.jsp"/>
        </div>
    </div>
</div>
<!-- Модальное окно -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel"></h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Закрити"></button>
            </div>
            <div class="modal-body">
                <div class="mb-xxl">
                    <textarea class="form-control" id="recipient-name" rows="10"></textarea>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрити</button>
            </div>
        </div>
    </div>
</div>
<script>
    const exampleModal = document.getElementById('exampleModal')
    if (exampleModal) {
        exampleModal.addEventListener('show.bs.modal', event => {
            // Button that triggered the modal
            const button = event.relatedTarget
            // Extract info from data-bs-* attributes
            const recipient = button.getAttribute('data-bs-whatever')
            // Update the modal's content.
            const modalTitle = exampleModal.querySelector('.modal-title')
            const modalBodyInput = exampleModal.querySelector('.modal-body textarea')

            modalTitle.textContent = `Детальний опис проблеми ${recipient}`
            modalBodyInput.value = recipient
        })
    }
</script>
<%--<jsp:include page="../includes/footer.jsp"/>--%>
</body>
</html>
