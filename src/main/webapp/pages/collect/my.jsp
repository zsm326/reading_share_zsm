<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="我的收藏"/>
</jsp:include>

<div class="panel panel-default">
    <div class="panel-heading"><strong>收藏的书单（${listCollects.size()}）</strong></div>
    <ul class="list-group">
        <c:if test="${empty listCollects}">
            <li class="list-group-item intro-text">还没有收藏书单。</li>
        </c:if>
        <c:forEach items="${listCollects}" var="c">
            <li class="list-group-item">
                <a href="booklist_detail.action?id=${c.targetId}"><c:out value="${c.targetName}"/></a>
                <span class="intro-text">
                    收藏于 <fmt:formatDate value="${c.createTime}" pattern="yyyy-MM-dd"/>
                </span>
                <a href="collect_toggle.action?targetType=1&targetId=${c.targetId}"
                   class="btn btn-default btn-xs pull-right">取消收藏</a>
            </li>
        </c:forEach>
    </ul>
</div>

<div class="panel panel-default">
    <div class="panel-heading"><strong>收藏的笔记（${noteCollects.size()}）</strong></div>
    <ul class="list-group">
        <c:if test="${empty noteCollects}">
            <li class="list-group-item intro-text">还没有收藏笔记。</li>
        </c:if>
        <c:forEach items="${noteCollects}" var="c">
            <li class="list-group-item">
                <a href="note_detail.action?id=${c.targetId}"><c:out value="${c.targetName}"/></a>
                <span class="intro-text">
                    收藏于 <fmt:formatDate value="${c.createTime}" pattern="yyyy-MM-dd"/>
                </span>
                <a href="collect_toggle.action?targetType=2&targetId=${c.targetId}"
                   class="btn btn-default btn-xs pull-right">取消收藏</a>
            </li>
        </c:forEach>
    </ul>
</div>

<jsp:include page="../common/footer.jsp"/>
