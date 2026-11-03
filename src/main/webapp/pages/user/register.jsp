<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="注册"/>
</jsp:include>

<div class="row">
    <div class="col-sm-4 col-sm-offset-4">
        <div class="panel panel-default">
            <div class="panel-heading"><strong>用户注册</strong></div>
            <div class="panel-body">
                <c:if test="${not empty fieldErrors['register']}">
                    <div class="alert alert-danger">${fieldErrors['register'][0]}</div>
                </c:if>
                <form action="user_register.action" method="post" onsubmit="return checkRegister()">
                    <div class="form-group">
                        <label>用户名</label>
                        <input type="text" name="username" id="username" class="form-control" placeholder="登录账号">
                    </div>
                    <div class="form-group">
                        <label>昵称</label>
                        <input type="text" name="nickname" id="nickname" class="form-control" placeholder="显示名称">
                    </div>
                    <div class="form-group">
                        <label>密码</label>
                        <input type="password" name="password" id="password" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>确认密码</label>
                        <input type="password" name="confirmPassword" id="confirmPassword" class="form-control">
                    </div>
                    <button type="submit" class="btn btn-success btn-block">注册</button>
                </form>
                <p class="intro-text" style="margin-top:10px;">
                    已有账号？<a href="pages/user/login.jsp">去登录</a>
                </p>
            </div>
        </div>
    </div>
</div>

<script>
function checkRegister() {
    if (!document.getElementById('username').value.trim()) { alert('用户名不能为空'); return false; }
    if (!document.getElementById('nickname').value.trim()) { alert('昵称不能为空'); return false; }
    var p1 = document.getElementById('password').value;
    var p2 = document.getElementById('confirmPassword').value;
    if (!p1.trim()) { alert('密码不能为空'); return false; }
    if (p1 !== p2) { alert('两次输入的密码不一致'); return false; }
    return true;
}
</script>

<jsp:include page="../common/footer.jsp"/>
