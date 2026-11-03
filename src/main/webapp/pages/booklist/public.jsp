<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="书单广场"/>
</jsp:include>

<div class="panel panel-default">
    <div class="panel-heading"><strong>公开书单</strong></div>
    <div class="panel-body">
        <c:if test="${empty bookLists}">
            <p class="intro-text">暂无公开书单。</p>
        </c:if>
        <div class="row">
            <c:forEach items="${bookLists}" var="bl">
                <div class="col-sm-4">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <h4>
                                <a href="booklist_detail.action?id=${bl.id}"><c:out value="${bl.name}"/></a>
                            </h4>
                            <p class="intro-text"><c:out value="${bl.intro}"/></p>
                            <p class="intro-text">
                                创建者：<c:out value="${bl.nickname}"/> ·
                                共 ${bl.bookCount} 本书 ·
                                <fmt:formatDate value="${bl.createTime}" pattern="yyyy-MM-dd"/>
                            </p>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
