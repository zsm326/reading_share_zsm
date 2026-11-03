<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="书籍审核"/>
</jsp:include>

<ul class="nav nav-tabs" style="margin-bottom:15px;">
    <li><a href="admin_users.action">用户管理</a></li>
    <li><a href="admin_notes.action">笔记管理</a></li>
    <li><a href="admin_comments.action">评论管理</a></li>
    <li><a href="admin_books.action">书籍管理</a></li>
    <li class="active"><a href="admin_bookAudits.action">书籍审核</a></li>
    <li><a href="admin_covers.action">封面审核</a></li>
</ul>

<div class="panel panel-default">
    <div class="panel-heading"><strong>待审核书籍（${pendingBooks.size()}）</strong></div>
    <table class="table table-hover">
        <thead>
        <tr><th>书名</th><th>作者</th><th>ISBN</th><th>提交人</th><th>提交时间</th><th>操作</th></tr>
        </thead>
        <tbody>
        <c:if test="${empty pendingBooks}">
            <tr><td colspan="6" class="intro-text">暂无待审核的书籍。</td></tr>
        </c:if>
        <c:forEach items="${pendingBooks}" var="b">
            <tr>
                <td><a href="book_detail.action?id=${b.id}"><c:out value="${b.name}"/></a></td>
                <td><c:out value="${b.author}"/></td>
                <td><c:out value="${b.isbn}"/></td>
                <td><c:out value="${empty b.submitUserName ? '未知' : b.submitUserName}"/></td>
                <td><fmt:formatDate value="${b.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td>
                    <a href="admin_approveBook.action?id=${b.id}" class="btn btn-success btn-xs">通过发布</a>
                    <a href="admin_rejectBook.action?id=${b.id}" class="btn btn-danger btn-xs"
                       onclick="return confirm('驳回后该书不会对外展示，确定吗？')">驳回</a>
                </td>
            </tr>
            <c:if test="${not empty b.intro}">
                <tr class="intro-text"><td colspan="6">简介：<c:out value="${b.intro}"/></td></tr>
            </c:if>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="../common/footer.jsp"/>
