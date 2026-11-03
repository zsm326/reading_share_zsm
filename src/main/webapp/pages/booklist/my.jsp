<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="我的书单"/>
</jsp:include>

<div class="panel panel-default">
    <div class="panel-heading">
        <strong>我的书单</strong>
        <a href="pages/booklist/add.jsp" class="btn btn-primary btn-xs pull-right">+ 创建书单</a>
    </div>
    <table class="table table-hover">
        <thead>
        <tr>
            <th>书单名称</th><th>简介</th><th>书籍数</th><th>可见性</th><th>创建时间</th><th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty bookLists}">
            <tr><td colspan="6" class="intro-text">还没有书单，点击右上角创建吧。</td></tr>
        </c:if>
        <c:forEach items="${bookLists}" var="bl">
            <tr>
                <td><a href="booklist_detail.action?id=${bl.id}"><c:out value="${bl.name}"/></a></td>
                <td><c:out value="${bl.intro}"/></td>
                <td>${bl.bookCount}</td>
                <td>
                    <c:choose>
                        <c:when test="${bl.isPublic == 1}"><span class="label label-success">公开</span></c:when>
                        <c:otherwise><span class="label label-default">私有</span></c:otherwise>
                    </c:choose>
                </td>
                <td><fmt:formatDate value="${bl.createTime}" pattern="yyyy-MM-dd"/></td>
                <td>
                    <a href="booklist_editPage.action?id=${bl.id}" class="btn btn-default btn-xs">编辑</a>
                    <a href="booklist_delete.action?id=${bl.id}" class="btn btn-danger btn-xs"
                       onclick="return confirm('确定删除该书单吗？')">删除</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="../common/footer.jsp"/>
