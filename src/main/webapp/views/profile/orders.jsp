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
<jsp:include page="../includes/header.jsp"/>

<div class="container-fluid">
    <div class="row">
        <div class="col-2">Тут може бути Ваша реклама</div>
        <div class="col-10">
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
                                <c:if test="${order.craftsmanId > 0}">
                                    <c:set var="orderCraftsman" value="${order.getCraftsman()}"/>
                                    <h6 class="card-subtitle mb-2"> Майстер: ${orderCraftsman.getFullName()}</h6>
                                    <h6 class="card-subtitle mb-2"> Майстер: ${order.getCraftsman()}</h6>
                                </c:if>
                                <c:if test="${order.price > 0}">
                                    <br>
                                    <h6 class="card-subtitle mb-2"> Вартість послуги: ${order.price}</h6>
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
            <jsp:include page="../includes/pagination.jsp"/>
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
