<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="书籍"/>
</jsp:include>

<div class="panel panel-default">
    <div class="panel-heading">
        <form action="book_list.action" method="get" class="form-inline">
            <div class="form-group">
                <input type="text" name="keyword" class="form-control" value="<c:out value='${keyword}'/>" placeholder="书名 / 作者">
            </div>
            <button type="submit" class="btn btn-primary">搜索书籍</button>
        </form>
        <%-- 登录用户可分享书籍，提交后待管理员审核 --%>
        <c:if test="${not empty sessionScope.loginUser}">
            <a href="book_addPage.action" class="btn btn-success btn-xs pull-right" style="margin-top:3px;">+ 分享书籍</a>
        </c:if>
    </div>
    <div class="panel-body">
        <c:if test="${empty books}">
            <p class="intro-text">暂无书籍。</p>
        </c:if>
        <div class="row">
            <c:forEach items="${books}" var="b">
                <div class="col-sm-3">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <c:if test="${not empty b.cover}">
                                <img src="upload/${b.cover}" class="cover-img" alt="封面">
                            </c:if>
                            <h4><c:out value="${b.name}"/></h4>
                            <p class="intro-text"><c:out value="${b.author}"/></p>
                            <a href="book_detail.action?id=${b.id}" class="btn btn-primary btn-sm">查看详情</a>
                            <c:if test="${not empty sessionScope.loginUser}">
                                <a href="note_addPage.action?bookId=${b.id}" class="btn btn-default btn-sm">写笔记</a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
