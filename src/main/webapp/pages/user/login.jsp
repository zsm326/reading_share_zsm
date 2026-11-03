<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="登录"/>
</jsp:include>

<%-- 从 session 读取上一次操作留下的提示（注册后会写 message），读完立即清掉避免残留 --%>
<%
    Object sessionMsg = session.getAttribute("message");
    if (sessionMsg != null) {
        out.println("<div class='alert alert-success'>" + sessionMsg + "</div>");
        session.removeAttribute("message");
    }
%>

<div class="row">
    <div class="col-sm-4 col-sm-offset-4">
        <div class="panel panel-default">
            <div class="panel-heading"><strong>用户登录</strong></div>
            <div class="panel-body">
                <c:if test="${not empty fieldErrors['login']}">
                    <div class="alert alert-danger">${fieldErrors['login'][0]}</div>
                </c:if>
                <form action="user_login.action" method="post" onsubmit="return checkLogin()">
                    <div class="form-group">
                        <label>用户名</label>
                        <input type="text" name="username" id="username" class="form-control" placeholder="请输入用户名">
                    </div>
                    <div class="form-group">
                        <label>密码</label>
                        <input type="password" name="password" id="password" class="form-control" placeholder="请输入密码">
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">登录</button>
                </form>
                <p class="intro-text" style="margin-top:10px;">
                    还没有账号？<a href="pages/user/register.jsp">立即注册</a>
                </p>
            </div>
        </div>
    </div>
</div>

<script>
// 前端非空校验
function checkLogin() {
    if (!document.getElementById('username').value.trim()) { alert('用户名不能为空'); return false; }
    if (!document.getElementById('password').value.trim()) { alert('密码不能为空'); return false; }
    return true;
}
</script>

<jsp:include page="../common/footer.jsp"/>
