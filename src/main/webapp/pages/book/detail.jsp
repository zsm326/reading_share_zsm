<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="书籍详情"/>
</jsp:include>

<%-- 封面上传结果提示（redirect 后从 session 取，读取即清除） --%>
<c:if test="${not empty sessionScope.message}">
    <div class="alert alert-success">${sessionScope.message}</div>
    <c:remove var="message" scope="session"/>
</c:if>

<div class="panel panel-default">
    <div class="panel-body">
        <%-- 书名上方展示封面：有封面显示图片，没有则显示“暂无封面” --%>
        <div class="text-center" style="margin-bottom:15px;">
            <c:choose>
                <c:when test="${not empty book.cover}">
                    <img src="${pageContext.request.contextPath}/upload/${book.cover}"
                         class="book-cover-lg" alt="书籍封面">
                </c:when>
                <c:otherwise>
                    <div class="no-cover">暂无封面</div>
                </c:otherwise>
            </c:choose>
        </div>

        <h3><c:out value="${book.name}"/></h3>
        <p>作者：<c:out value="${book.author}"/></p>
        <p>ISBN：<c:out value="${book.isbn}"/></p>
        <p class="intro-text"><c:out value="${book.intro}"/></p>
        <c:if test="${not empty sessionScope.loginUser}">
            <a href="note_addPage.action?bookId=${book.id}" class="btn btn-primary">为这本书写笔记</a>
            <%-- 上传封面按钮仅该书分享者/管理员可见；其他登录用户隐藏（后端 Action 仍有校验兜底） --%>
            <c:if test="${sessionScope.loginUser.role == 1 or sessionScope.loginUser.id == book.userId}">
                <a href="book_coverPage.action?id=${book.id}" class="btn btn-default">上传封面</a>
            </c:if>
        </c:if>
    </div>
</div>

<div class="panel panel-default">
    <div class="panel-heading"><strong>相关读书笔记（${notes.size()}）</strong></div>
    <ul class="list-group">
        <c:if test="${empty notes}">
            <li class="list-group-item intro-text">还没有人为这本书写笔记。</li>
        </c:if>
        <c:forEach items="${notes}" var="n">
            <li class="list-group-item">
                <a href="note_detail.action?id=${n.id}"><strong><c:out value="${n.title}"/></strong></a>
                <span class="intro-text">
                    — <c:out value="${n.nickname}"/>
                    <fmt:formatDate value="${n.createTime}" pattern="yyyy-MM-dd HH:mm"/>
                </span>
            </li>
        </c:forEach>
    </ul>
</div>

<jsp:include page="../common/footer.jsp"/>
