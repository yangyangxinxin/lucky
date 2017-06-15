<#assign contextPath=request.getContextPath()>
<#macro defaultLayout>
<#if Session.userInfo?exists>
    <#assign userInfo = Session.userInfo>
</#if>
<!DOCTYPE html>
<html lang="zh-cn">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

<title>${title!}</title>
<link rel="stylesheet" href="/css/global.css"/>
<link rel="stylesheet" href="/js/editormd/css/editormd.preview.min.css"/>
<link rel="stylesheet" href="/js/editormd/css/editormd.css"/>
<link rel="stylesheet" href="${contextPath}/layui/css/layui.css" media="all">
<#--<script src="${contextPath}/layui/lay/dest/layui.all.js" charset="utf-8"></script>-->
<script src="${contextPath}/js/jquery.min.js"></script>
<script src="${contextPath}/js/common/app.js"></script>
<script src="/layui/layui.js"></script>


<script src="/js/editormd/lib/marked.min.js"></script>
<script src="/js/editormd/lib/prettify.min.js"></script>
<script src="/js/editormd/editormd.min.js"></script>

<script src="/js/editormd/lib/raphael.min.js"></script>
<script src="/js/editormd/lib/underscore.min.js"></script>
<script src="/js/editormd/lib/sequence-diagram.min.js"></script>
<script src="/js/editormd/lib/flowchart.min.js"></script>
<script src="/js/editormd/lib/jquery.flowchart.min.js"></script>
<script>
   // $(document).ready(function () {
        //var form = layui.form();
        //var layer = layui.layer;
        //var layedit = layui.layedit;
        /*layui.use('element', function(){
            var element = layui.element();
        });*/

        //layui模块的使用
        layui.use(['element', 'layer'], function(args){
            var element = layui.element;
            var layer = layui.layer;
            //……

        });
    //})

</script>


<body class="">
<ul class="layui-nav">
    <li class="layui-nav-item <#if action?exists && action=='index'> layui-this </#if>" >
        <a href="/index">首页</a>
    </li>
    <li class="layui-nav-item <#if action?exists && action=='article'> layui-this </#if> >">
        <a href="javascript:;">文章</a>
        <dl class="layui-nav-child">
            <dd><a href="/article/create">写文章</a></dd>
            <dd><a href="">我的文章</a></dd>
            <dd><a href="/article/list">文章列表</a></dd>
        </dl>
    </li>
    <li class="layui-nav-item <#if action?exists && action=='photo'> layui-this </#if>">
        <a href="javascript:;">相册</a>
        <dl class="layui-nav-child">
            <dd><a href="/photo/uploadPage">上传相片</a></dd>
            <dd><a href="">我的相册</a></dd>
            <dd><a href="/photo/list">相册列表</a></dd>
        </dl>
    </li>
    <#if userInfo?exists>
        <li class="layui-nav-item <#if action?exists && action=='profile'> layui-this </#if>" style="margin-left:950px;"><a href="/profile/index">${userInfo.username!}</a></li>
        <li class="layui-nav-item"><a href="/account/logout">退出</a></li>
        <#else>
        <li class="layui-nav-item <#if action?exists && action=='loginPage'> layui-this </#if>" style="margin-left:950px;"><a href="/account/loginPage">登录</a></li>
        <li class="layui-nav-item <#if action?exists && action=='registerPage'> layui-this </#if>" style=""><a href="/account/registerPage">注册</a></li>
    </#if>

</ul>
<div class="body-content">
    <#nested>
</div>

<div class="layui-footer footer footer-doc">
    <div class="layui-main">
        <p>2017 © <a href="/">luckysweetheart.com</a></p>
        <p>
            <a href="http://www.luckysweetheart.com" target="_blank">yangxin</a>
        </p>
    </div>
</div>
</body>
</html>
<script>

</script>
</#macro>