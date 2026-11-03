<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="书单详情"/>
</jsp:include>

<c:set var="isOwner" value="${not empty sessionScope.loginUser and sessionScope.loginUser.id == bookList.userId}"/>

<div class="panel panel-default">
    <div class="panel-body">
        <h3>
            <c:out value="${bookList.name}"/>
            <c:choose>
                <c:when test="${bookList.isPublic == 1}"><span class="label label-success">公开</span></c:when>
                <c:otherwise><span class="label label-default">私有</span></c:otherwise>
            </c:choose>
        </h3>
        <p class="intro-text"><c:out value="${bookList.intro}"/></p>
        <p class="intro-text">创建者：<c:out value="${bookList.nickname}"/> · 被收藏 ${collectCount} 次</p>
        <c:if test="${not empty sessionScope.loginUser and !isOwner}">
            <a href="collect_toggle.action?targetType=1&targetId=${bookList.id}" class="btn btn-warning btn-sm">
                ${collected ? '取消收藏' : '收藏书单'}
            </a>
        </c:if>
    </div>
</div>

<div class="panel panel-default">
    <div class="panel-heading"><strong>书单中的书籍（${books.size()}）</strong></div>
    <table class="table table-hover">
        <thead><tr><th>封面</th><th>书名</th><th>作者</th><th>操作</th></tr></thead>
        <tbody>
        <c:if test="${empty books}">
            <tr><td colspan="4" class="intro-text">书单还是空的。</td></tr>
        </c:if>
        <c:forEach items="${books}" var="b">
            <tr>
                <td>
                    <c:if test="${not empty b.cover}">
                        <img src="upload/${b.cover}" class="cover-thumb" alt="封面">
                    </c:if>
                </td>
                <td><a href="book_detail.action?id=${b.id}"><c:out value="${b.name}"/></a></td>
                <td><c:out value="${b.author}"/></td>
                <td>
                    <c:if test="${isOwner}">
                        <a href="booklist_removeBook.action?id=${bookList.id}&bookId=${b.id}"
                           class="btn btn-danger btn-xs"
                           onclick="return confirm('确定从书单移除该书吗？')">移除</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%-- 仅创建者可以搜索书籍加入书单 --%>
<c:if test="${isOwner}">
    <div class="panel panel-default">
        <div class="panel-heading">
            <form action="booklist_detail.action" method="get" class="form-inline">
                <input type="hidden" name="id" value="${bookList.id}">
                <div class="form-group">
                    <input type="text" name="keyword" class="form-control" value="<c:out value='${keyword}'/>" placeholder="搜索书名/作者以添加">
                </div>
                <button type="submit" class="btn btn-primary">搜索书籍</button>
            </form>
        </div>
        <c:if test="${not empty searchBooks}">
            <table class="table">
                <tbody>
                <c:forEach items="${searchBooks}" var="b">
                    <tr>
                        <td><c:out value="${b.name}"/></td>
                        <td><c:out value="${b.author}"/></td>
                        <td>
                            <a href="booklist_addBook.action?id=${bookList.id}&bookId=${b.id}"
                               class="btn btn-success btn-xs">加入书单</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</c:if>

<jsp:include page="../common/footer.jsp"/>
