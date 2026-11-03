<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="书籍管理"/>
</jsp:include>

<ul class="nav nav-tabs" style="margin-bottom:15px;">
    <li><a href="admin_users.action">用户管理</a></li>
    <li><a href="admin_notes.action">笔记管理</a></li>
    <li><a href="admin_comments.action">评论管理</a></li>
    <li class="active"><a href="admin_books.action">书籍管理</a></li>
    <li><a href="admin_bookAudits.action">书籍审核</a></li>
    <li><a href="admin_covers.action">封面审核</a></li>
</ul>

<div class="panel panel-default">
    <div class="panel-heading">
        <strong>全部书籍（${books.size()}）</strong>
        <a href="admin_bookAddPage.action" class="btn btn-primary btn-xs pull-right">+ 录入书籍</a>
    </div>
    <table class="table table-hover">
        <thead><tr><th>ID</th><th>封面</th><th>书名</th><th>作者</th><th>ISBN</th><th>操作</th></tr></thead>
        <tbody>
        <c:forEach items="${books}" var="b">
            <tr>
                <td>${b.id}</td>
                <td>
                    <c:if test="${not empty b.cover}">
                        <img src="upload/${b.cover}" class="cover-thumb" alt="封面">
                    </c:if>
                </td>
                <td><a href="book_detail.action?id=${b.id}"><c:out value="${b.name}"/></a></td>
                <td><c:out value="${b.author}"/></td>
                <td><c:out value="${b.isbn}"/></td>
                <td>
                    <a href="book_coverPage.action?id=${b.id}" class="btn btn-default btn-xs">改封面</a>
                    <a href="admin_deleteBook.action?id=${b.id}" class="btn btn-danger btn-xs"
                       onclick="return confirm('删除书籍将同时删除其笔记，确定吗？')">删除</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="../common/footer.jsp"/>
