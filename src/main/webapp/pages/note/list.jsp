<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="全部笔记"/>
</jsp:include>

<div class="panel panel-default">
    <div class="panel-heading">
        <form action="note_list.action" method="get" class="form-inline">
            <div class="form-group">
                <input type="text" name="keyword" class="form-control" value="<c:out value='${keyword}'/>" placeholder="笔记标题 / 内容">
            </div>
            <button type="submit" class="btn btn-primary">搜索笔记</button>
        </form>
    </div>
    <ul class="list-group">
        <c:if test="${empty notes}">
            <li class="list-group-item intro-text">暂无笔记。</li>
        </c:if>
        <c:forEach items="${notes}" var="n">
            <li class="list-group-item">
                <h4>
                    <a href="note_detail.action?id=${n.id}"><c:out value="${n.title}"/></a>
                </h4>
                <p class="intro-text">
                    《<c:out value="${n.bookName}"/>》 ·
                    <c:out value="${n.nickname}"/> ·
                    <fmt:formatDate value="${n.createTime}" pattern="yyyy-MM-dd HH:mm"/>
                </p>
            </li>
        </c:forEach>
    </ul>
</div>

<jsp:include page="../common/footer.jsp"/>
