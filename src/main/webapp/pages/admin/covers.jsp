<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="封面审核"/>
</jsp:include>

<ul class="nav nav-tabs" style="margin-bottom:15px;">
    <li><a href="admin_users.action">用户管理</a></li>
    <li><a href="admin_notes.action">笔记管理</a></li>
    <li><a href="admin_comments.action">评论管理</a></li>
    <li><a href="admin_books.action">书籍管理</a></li>
    <li><a href="admin_bookAudits.action">书籍审核</a></li>
    <li class="active"><a href="admin_covers.action">封面审核</a></li>
</ul>

<div class="panel panel-default">
    <div class="panel-heading"><strong>待审核封面（${pendingCovers.size()}）</strong></div>
    <table class="table table-hover">
        <thead>
        <tr><th>书籍名称</th><th>当前封面</th><th>上传人</th><th>上传时间</th><th>操作</th></tr>
        </thead>
        <tbody>
        <c:if test="${empty pendingCovers}">
            <tr><td colspan="5" class="intro-text">暂无待审核的封面。</td></tr>
        </c:if>
        <c:forEach items="${pendingCovers}" var="b">
            <tr>
                <td><a href="book_detail.action?id=${b.id}"><c:out value="${b.name}"/></a></td>
                <td>
                    <c:choose>
                        <c:when test="${not empty b.cover}">
                            <img src="${pageContext.request.contextPath}/upload/${b.cover}"
                                 class="cover-thumb" alt="待审核封面">
                        </c:when>
                        <c:otherwise><span class="intro-text">无</span></c:otherwise>
                    </c:choose>
                </td>
                <td><c:out value="${empty b.coverUserName ? '未知' : b.coverUserName}"/></td>
                <td><fmt:formatDate value="${b.coverTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td>
                    <a href="admin_approveCover.action?id=${b.id}" class="btn btn-success btn-xs">通过审核</a>
                    <a href="admin_rejectCover.action?id=${b.id}" class="btn btn-danger btn-xs"
                       onclick="return confirm('驳回后所有用户都看不到该封面，确定吗？')">驳回</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="../common/footer.jsp"/>
