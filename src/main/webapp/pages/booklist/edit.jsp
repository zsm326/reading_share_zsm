<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="编辑书单"/>
</jsp:include>

<div class="row">
    <div class="col-sm-6 col-sm-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><strong>编辑书单</strong></div>
            <div class="panel-body">
                <c:if test="${not empty fieldErrors['name']}">
                    <div class="alert alert-danger">${fieldErrors['name'][0]}</div>
                </c:if>
                <form action="booklist_update.action" method="post" onsubmit="return checkName()">
                    <input type="hidden" name="id" value="${bookList.id}">
                    <div class="form-group">
                        <label>书单名称</label>
                        <input type="text" name="name" id="name" class="form-control" value="<c:out value='${bookList.name}'/>">
                    </div>
                    <div class="form-group">
                        <label>书单简介</label>
                        <textarea name="intro" class="form-control" rows="3"><c:out value="${bookList.intro}"/></textarea>
                    </div>
                    <div class="form-group">
                        <label>可见性</label>
                        <select name="isPublic" class="form-control">
                            <option value="1" ${bookList.isPublic == 1 ? 'selected' : ''}>公开（所有人可见）</option>
                            <option value="0" ${bookList.isPublic == 0 ? 'selected' : ''}>私有（仅自己可见）</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">保存</button>
                    <a href="booklist_my.action" class="btn btn-default">返回</a>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
function checkName() {
    if (!document.getElementById('name').value.trim()) { alert('书单名称不能为空'); return false; }
    return true;
}
</script>

<jsp:include page="../common/footer.jsp"/>
