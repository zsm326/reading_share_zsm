<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="个人中心"/>
</jsp:include>

<div class="row">
    <%-- 左侧：基本信息与头像 --%>
    <div class="col-sm-4">
        <div class="panel panel-default">
            <div class="panel-heading"><strong>我的资料</strong></div>
            <div class="panel-body text-center">
                <c:choose>
                    <c:when test="${not empty user.avatar}">
                        <img src="upload/${user.avatar}" class="avatar-img" alt="头像">
                    </c:when>
                    <c:otherwise>
                        <span class="glyphicon glyphicon-user" style="font-size:80px;color:#ccc;"></span>
                    </c:otherwise>
                </c:choose>
                <h4><c:out value="${user.nickname}"/></h4>
                <p class="intro-text">
                    账号：<c:out value="${user.username}"/><br>
                    角色：<c:out value="${user.role == 1 ? '管理员' : '普通用户'}"/><br>
                    注册时间：<fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd"/>
                </p>
                <form action="user_updateAvatar.action" method="post" enctype="multipart/form-data" onsubmit="return checkAvatar()">
                    <div class="form-group text-left">
                        <label>更换头像（jpg/png/gif，不超过2MB）</label>
                        <input type="file" name="avatar" id="avatar" accept="image/*">
                    </div>
                    <c:if test="${not empty fieldErrors['avatar']}">
                        <div class="alert alert-danger">${fieldErrors['avatar'][0]}</div>
                    </c:if>
                    <button type="submit" class="btn btn-default btn-sm">上传头像</button>
                </form>
            </div>
        </div>
    </div>

    <%-- 右侧：修改昵称、修改密码 --%>
    <div class="col-sm-8">
        <div class="panel panel-default">
            <div class="panel-heading"><strong>修改昵称</strong></div>
            <div class="panel-body">
                <c:if test="${not empty fieldErrors['nickname']}">
                    <div class="alert alert-danger">${fieldErrors['nickname'][0]}</div>
                </c:if>
                <form action="user_updateNickname.action" method="post" class="form-inline" onsubmit="return checkNickname()">
                    <div class="form-group">
                        <input type="text" name="nickname" id="nickname" class="form-control" value="<c:out value='${user.nickname}'/>">
                    </div>
                    <button type="submit" class="btn btn-primary">保存</button>
                </form>
            </div>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading"><strong>修改密码</strong></div>
            <div class="panel-body">
                <c:if test="${not empty fieldErrors['password']}">
                    <div class="alert alert-danger">${fieldErrors['password'][0]}</div>
                </c:if>
                <form action="user_updatePassword.action" method="post" onsubmit="return checkPwd()">
                    <div class="form-group">
                        <label>原密码</label>
                        <input type="password" name="oldPassword" id="oldPassword" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>新密码</label>
                        <input type="password" name="password" id="newPassword" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>确认新密码</label>
                        <input type="password" name="confirmPassword" id="confirmPassword" class="form-control">
                    </div>
                    <button type="submit" class="btn btn-primary">修改密码</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
function checkAvatar() {
    var f = document.getElementById('avatar').value;
    if (!f) { alert('请选择图片文件'); return false; }
    return true;
}
function checkNickname() {
    if (!document.getElementById('nickname').value.trim()) { alert('昵称不能为空'); return false; }
    return true;
}
function checkPwd() {
    if (!document.getElementById('oldPassword').value.trim()) { alert('原密码不能为空'); return false; }
    var p1 = document.getElementById('newPassword').value;
    var p2 = document.getElementById('confirmPassword').value;
    if (!p1.trim()) { alert('新密码不能为空'); return false; }
    if (p1 !== p2) { alert('两次输入的新密码不一致'); return false; }
    return true;
}
</script>

<jsp:include page="../common/footer.jsp"/>
