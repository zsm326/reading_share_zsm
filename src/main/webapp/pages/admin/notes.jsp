<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="笔记管理"/>
</jsp:include>

<ul class="nav nav-tabs" style="margin-bottom:15px;">
    <li><a href="admin_users.action">用户管理</a></li>
    <li class="active"><a href="admin_notes.action">笔记管理</a></li>
    <li><a href="admin_comments.action">评论管理</a></li>
    <li><a href="admin_books.action">书籍管理</a></li>
    <li><a href="admin_bookAudits.action">书籍审核</a></li>
    <li><a href="admin_covers.action">封面审核</a></li>
</ul>

<div class="panel panel-default">
    <div class="panel-heading"><strong>全部笔记（${notes.size()}）</strong></div>
    <table class="table table-hover">
        <thead><tr><th>ID</th><th>标题</th><th>书籍</th><th>作者</th><th>发布时间</th><th>操作</th></tr></thead>
        <tbody>
        <c:forEach items="${notes}" var="n">
            <tr>
                <td>${n.id}</td>
                <td><a href="note_detail.action?id=${n.id}"><c:out value="${n.title}"/></a></td>
                <td>《<c:out value="${n.bookName}"/>》</td>
                <td><c:out value="${n.nickname}"/></td>
                <td><fmt:formatDate value="${n.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td>
                    <a href="admin_deleteNote.action?id=${n.id}" class="btn btn-danger btn-xs"
                       onclick="return confirm('确定删除该违规笔记吗？')">删除</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="../common/footer.jsp"/>
