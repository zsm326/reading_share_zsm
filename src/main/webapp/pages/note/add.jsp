<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="写笔记"/>
</jsp:include>

<div class="row">
    <div class="col-sm-8 col-sm-offset-2">
        <div class="panel panel-default">
            <div class="panel-heading">
                <strong>写笔记</strong> — 《<c:out value="${book.name}"/>》（<c:out value="${book.author}"/>）
            </div>
            <div class="panel-body">
                <form action="note_add.action" method="post" onsubmit="return checkNote()">
                    <input type="hidden" name="bookId" value="${book.id}">
                    <div class="form-group">
                        <label>笔记标题</label>
                        <input type="text" name="title" id="title" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>笔记内容</label>
                        <textarea name="content" id="content" class="form-control" rows="10"></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">发布</button>
                    <a href="book_detail.action?id=${book.id}" class="btn btn-default">返回</a>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
function checkNote() {
    if (!document.getElementById('title').value.trim()) { alert('笔记标题不能为空'); return false; }
    if (!document.getElementById('content').value.trim()) { alert('笔记内容不能为空'); return false; }
    return true;
}
</script>

<jsp:include page="../common/footer.jsp"/>
