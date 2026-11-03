﻿<%-- 公共头部：导航栏（所有页面通过 jsp:include 引入） --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${param.title} - 读书笔记分享</title>
    <%--
      base 标签：把浏览器所有"相对路径"解析基准设为项目名根
      所有 JSP 里的 action/href/src 现在都是纯相对路径，
      base 会自动拼上项目前缀，彻底解决 contextPath 丢失问题
    --%>
    <base href="${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="pages/index.jsp">读书笔记分享</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="booklist_public.action">书单广场</a></li>
            <li><a href="note_list.action">全部笔记</a></li>
            <li><a href="book_list.action">书籍</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <c:choose>
                <c:when test="${empty sessionScope.loginUser}">
                    <li><a href="pages/user/login.jsp">登录</a></li>
                    <li><a href="pages/user/register.jsp">注册</a></li>
                </c:when>
                <c:otherwise>
                    <c:if test="${sessionScope.loginUser.role == 1}">
                        <li><a href="admin_users.action">后台管理</a></li>
                    </c:if>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <c:out value="${sessionScope.loginUser.nickname}"/> <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a href="booklist_my.action">我的书单</a></li>
                            <li><a href="note_my.action">我的笔记</a></li>
                            <li><a href="collect_my.action">我的收藏</a></li>
                            <li><a href="user_profile.action">个人中心</a></li>
                            <li role="separator" class="divider"></li>
                            <li><a href="user_logout.action">退出登录</a></li>
                        </ul>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>
<div class="container">
