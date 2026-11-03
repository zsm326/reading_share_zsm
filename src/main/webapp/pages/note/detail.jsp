<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="笔记详情"/>
</jsp:include>

<c:set var="isAuthor" value="${not empty sessionScope.loginUser and sessionScope.loginUser.id == note.userId}"/>

<div class="panel panel-default">
    <div class="panel-body">
        <h3><c:out value="${note.title}"/></h3>
        <p class="intro-text">
            书籍：《<a href="book_detail.action?id=${note.bookId}"><c:out value="${note.bookName}"/></a>》 ·
            作者：<c:out value="${note.nickname}"/> ·
            <fmt:formatDate value="${note.createTime}" pattern="yyyy-MM-dd HH:mm"/> ·
            被收藏 ${collectCount} 次
        </p>
        <hr>
        <div class="note-content"><c:out value="${note.content}"/></div>
        <hr>
        <c:if test="${not empty sessionScope.loginUser and !isAuthor}">
            <a href="collect_toggle.action?targetType=2&targetId=${note.id}" class="btn btn-warning btn-sm">
                ${collected ? '取消收藏' : '收藏笔记'}
            </a>
        </c:if>
        <c:if test="${isAuthor}">
            <a href="note_editPage.action?id=${note.id}" class="btn btn-default btn-sm">编辑</a>
            <a href="note_delete.action?id=${note.id}" class="btn btn-danger btn-sm"
               onclick="return confirm('确定删除该笔记吗？')">删除</a>
        </c:if>
    </div>
</div>

<%-- 评论区 --%>
<div class="panel panel-default">
    <div class="panel-heading"><strong>评论（${comments.size()}）</strong></div>
    <ul class="list-group">
        <c:if test="${empty comments}">
            <li class="list-group-item intro-text">还没有评论。</li>
        </c:if>
        <c:forEach items="${comments}" var="c">
            <li class="list-group-item">
                <strong><c:out value="${c.nickname}"/>：</strong>
                <c:out value="${c.content}"/>
                <span class="intro-text">
                    <fmt:formatDate value="${c.createTime}" pattern="yyyy-MM-dd HH:mm"/>
                </span>
                <c:if test="${not empty sessionScope.loginUser and sessionScope.loginUser.id == c.userId}">
                    <a href="comment_delete.action?id=${c.id}" class="btn btn-danger btn-xs pull-right"
                       onclick="return confirm('确定删除该评论吗？')">删除</a>
                </c:if>
            </li>
        </c:forEach>
    </ul>
    <div class="panel-body">
        <c:choose>
            <c:when test="${not empty sessionScope.loginUser}">
                <form action="comment_add.action" method="post" onsubmit="return checkComment()">
                    <input type="hidden" name="noteId" value="${note.id}">
                    <div class="form-group">
                        <textarea name="content" id="content" class="form-control" rows="3" placeholder="写下你的评论..."></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">发表评论</button>
                </form>
            </c:when>
            <c:otherwise>
                <p class="intro-text"><a href="pages/user/login.jsp">登录</a> 后才能发表评论。</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
function checkComment() {
    if (!document.getElementById('content').value.trim()) { alert('评论内容不能为空'); return false; }
    return true;
}
</script>

<jsp:include page="../common/footer.jsp"/>
