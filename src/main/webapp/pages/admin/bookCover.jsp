<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="上传书籍封面"/>
</jsp:include>

<%-- 正常打开时 book 由 Action 查出；若被 fileUpload 拦截器提前拦截（如超过2MB），
     book 为空，此时用请求参数 id 兜底，保证表单仍可再次提交 --%>
<c:set var="bookId" value="${not empty book ? book.id : param.id}"/>

<div class="row">
    <div class="col-sm-6 col-sm-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><strong>上传书籍封面</strong></div>
            <div class="panel-body">

                <%-- 普通用户提交后进入待审核，管理员直接生效 --%>
                <c:if test="${not empty sessionScope.loginUser and sessionScope.loginUser.role != 1}">
                    <div class="alert alert-info">提交后需管理员审核通过，封面才会对其他用户展示。</div>
                </c:if>

                <%-- 字段级错误（Action 里的 addFieldError） --%>
                <c:if test="${not empty fieldErrors['cover']}">
                    <div class="alert alert-danger">${fieldErrors['cover'][0]}</div>
                </c:if>
                <%-- 拦截器级错误（大小/后缀不合法时由 fileUpload 拦截器写入 actionErrors） --%>
                <c:forEach items="${actionErrors}" var="err">
                    <div class="alert alert-danger"><c:out value="${err}"/></div>
                </c:forEach>

                <c:choose>
                    <c:when test="${empty bookId}">
                        <div class="alert alert-warning">未读取到书籍信息，请返回书籍列表后重试。</div>
                        <a href="book_list.action" class="btn btn-default">返回书籍列表</a>
                    </c:when>
                    <c:otherwise>
                        <p>书名：<strong><c:out value="${book.name}"/></strong></p>

                        <%-- 当前封面预览：有封面显示图片，无封面显示占位文字 --%>
                        <div class="text-center" style="margin-bottom:15px;">
                            <c:choose>
                                <c:when test="${not empty book.cover}">
                                    <img src="${pageContext.request.contextPath}/upload/${book.cover}"
                                         class="book-cover-lg" alt="当前封面">
                                </c:when>
                                <c:otherwise>
                                    <div class="no-cover">暂无封面</div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <%-- 上传表单：enctype 必须是 multipart/form-data，否则文件传不上去 --%>
                        <form action="book_uploadCover.action" method="post"
                              enctype="multipart/form-data" onsubmit="return checkCover()">
                            <input type="hidden" name="id" value="${bookId}">
                            <div class="form-group">
                                <label>选择封面图片（仅 jpg / png，不超过 2MB）</label>
                                <input type="file" name="cover" id="coverFile" class="form-control"
                                       accept="image/jpeg,image/png">
                            </div>
                            <button type="submit" class="btn btn-primary">上传并保存</button>
                            <a href="book_detail.action?id=${bookId}" class="btn btn-default">返回详情</a>
                            <c:if test="${sessionScope.loginUser.role == 1}">
                                <a href="admin_books.action" class="btn btn-default">书籍管理</a>
                            </c:if>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<script>
function checkCover() {
    var f = document.getElementById('coverFile');
    if (!f || f.files.length === 0) { alert('请选择要上传的封面图片'); return false; }
    var fileName = f.files[0].name.toLowerCase();
    if (!/\.(jpg|jpeg|png)$/.test(fileName)) { alert('封面只能是 jpg 或 png 格式'); return false; }
    if (f.files[0].size > 2 * 1024 * 1024) { alert('封面图片不能超过 2MB'); return false; }
    return true;
}
</script>

<jsp:include page="../common/footer.jsp"/>
