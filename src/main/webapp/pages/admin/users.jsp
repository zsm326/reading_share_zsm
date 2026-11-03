<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="用户管理"/>
</jsp:include>

<ul class="nav nav-tabs" style="margin-bottom:15px;">
    <li class="active"><a href="admin_users.action">用户管理</a></li>
    <li><a href="admin_notes.action">笔记管理</a></li>
    <li><a href="admin_comments.action">评论管理</a></li>
    <li><a href="admin_books.action">书籍管理</a></li>
    <li><a href="admin_bookAudits.action">书籍审核</a></li>
    <li><a href="admin_covers.action">封面审核</a></li>
</ul>

<div class="panel panel-default">
    <div class="panel-heading"><strong>全部用户（${users.size()}）</strong></div>
    <table class="table table-hover">
        <thead><tr><th>ID</th><th>用户名</th><th>昵称</th><th>角色</th><th>注册时间</th></tr></thead>
        <tbody>
        <c:forEach items="${users}" var="u">
            <tr>
                <td>${u.id}</td>
                <td><c:out value="${u.username}"/></td>
                <td><c:out value="${u.nickname}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${u.role == 1}"><span class="label label-danger">管理员</span></c:when>
                        <c:otherwise><span class="label label-default">普通用户</span></c:otherwise>
                    </c:choose>
                </td>
                <td><fmt:formatDate value="${u.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="../common/footer.jsp"/>
