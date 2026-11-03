<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="我的笔记"/>
</jsp:include>

<div class="panel panel-default">
    <div class="panel-heading">
        <strong>我的笔记</strong>
        <a href="book_list.action" class="btn btn-primary btn-xs pull-right">+ 写笔记（先选书）</a>
    </div>
    <table class="table table-hover">
        <thead><tr><th>标题</th><th>对应书籍</th><th>更新时间</th><th>操作</th></tr></thead>
        <tbody>
        <c:if test="${empty notes}">
            <tr><td colspan="4" class="intro-text">还没有写过笔记。</td></tr>
        </c:if>
        <c:forEach items="${notes}" var="n">
            <tr>
                <td><a href="note_detail.action?id=${n.id}"><c:out value="${n.title}"/></a></td>
                <td>《<c:out value="${n.bookName}"/>》</td>
                <td><fmt:formatDate value="${n.updateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td>
                    <a href="note_editPage.action?id=${n.id}" class="btn btn-default btn-xs">编辑</a>
                    <a href="note_delete.action?id=${n.id}" class="btn btn-danger btn-xs"
                       onclick="return confirm('确定删除该笔记吗？')">删除</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="../common/footer.jsp"/>
