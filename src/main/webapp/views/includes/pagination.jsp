<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">


<nav aria-label="Navigation for countries">
    <ul class="pagination justify-content-center">
        <c:if test="${page != 1}">
            <li class="page-item">
                <a class="page-link" href="orders?page=${page-1}"> Попередня </a>
            </li>
        </c:if>

        <c:forEach begin="1" end="${nOfPages}" var="i">
            <c:choose>
                <c:when test="${page eq i}">
                    <li class="page-item active"><a class="page-link">
                            ${i} </a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item"><a class="page-link" href="orders?page=${i}">${i}</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${page lt nOfPages}">
            <li class="page-item"><a class="page-link" href="orders?page=${page+1}"> Наступна </a>
            </li>
        </c:if>
    </ul>
</nav>

<script>
    function addUrlParameter(value) {
        var searchParams = new URLSearchParams(window.location.search)
        searchParams.set('page', value)
        window.location.search = searchParams.toString()
    }
</script>
