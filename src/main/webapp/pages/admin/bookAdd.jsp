<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="录入书籍"/>
</jsp:include>

<div class="row">
    <div class="col-sm-6 col-sm-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><strong>录入书籍</strong></div>
            <div class="panel-body">
                <c:if test="${not empty fieldErrors['book']}">
                    <div class="alert alert-danger">${fieldErrors['book'][0]}</div>
                </c:if>
                <c:if test="${not empty fieldErrors['cover']}">
                    <div class="alert alert-danger">${fieldErrors['cover'][0]}</div>
                </c:if>
                <form action="book_add.action" method="post" enctype="multipart/form-data" onsubmit="return checkBook()">
                    <div class="form-group">
                        <label>书名</label>
                        <input type="text" name="name" id="name" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>作者</label>
                        <input type="text" name="author" id="author" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>ISBN</label>
                        <input type="text" name="isbn" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>封面图（jpg/png/gif，不超过2MB，可选）</label>
                        <input type="file" name="cover" accept="image/*">
                    </div>
                    <div class="form-group">
                        <label>简介</label>
                        <textarea name="intro" class="form-control" rows="4"></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">录入</button>
                    <a href="admin_books.action" class="btn btn-default">返回</a>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
function checkBook() {
    if (!document.getElementById('name').value.trim()) { alert('书名不能为空'); return false; }
    if (!document.getElementById('author').value.trim()) { alert('作者不能为空'); return false; }
    return true;
}
</script>

<jsp:include page="../common/footer.jsp"/>
