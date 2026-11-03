<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="首页"/>
</jsp:include>

<div class="jumbotron">
    <h2>读书笔记分享系统</h2>
    <p>记录读书心得，创建专属书单，与同学分享阅读的乐趣。</p>
    <p>
        <a class="btn btn-primary btn-lg" href="note_list.action">浏览笔记</a>
        <a class="btn btn-default btn-lg" href="booklist_public.action">书单广场</a>
    </p>
</div>

<div class="row">
    <div class="col-sm-4">
        <div class="panel panel-default">
            <div class="panel-body text-center">
                <h4>写笔记</h4>
                <p class="intro-text">为每一本读过的书留下思考</p>
                <a href="book_list.action" class="btn btn-primary">去找书</a>
            </div>
        </div>
    </div>
    <div class="col-sm-4">
        <div class="panel panel-default">
            <div class="panel-body text-center">
                <h4>建书单</h4>
                <p class="intro-text">整理主题书单，公开或私有随你设置</p>
                <a href="booklist_my.action" class="btn btn-primary">我的书单</a>
            </div>
        </div>
    </div>
    <div class="col-sm-4">
        <div class="panel panel-default">
            <div class="panel-body text-center">
                <h4>互动交流</h4>
                <p class="intro-text">收藏喜欢的内容，在笔记下评论讨论</p>
                <a href="note_list.action" class="btn btn-primary">看看大家</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="common/footer.jsp"/>
