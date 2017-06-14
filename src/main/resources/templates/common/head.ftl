<#assign contextPath=request.getContextPath()>
<#macro defaultLayout>
<!DOCTYPE html>
<html lang="zh-cn">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>${title!}</title>
<link rel="stylesheet" href="/css/global.css"/>
<link rel="stylesheet" href="/js/editormd/css/editormd.preview.min.css"/>
<link rel="stylesheet" href="/js/editormd/css/editormd.css"/>
<link rel="stylesheet" href="${contextPath}/layui/css/layui.css" media="all">
<script src="${contextPath}/layui/lay/dest/layui.all.js" charset="utf-8"></script>
<script src="${contextPath}/js/jquery.min.js"></script>
<script src="${contextPath}/js/common/app.js"></script>


<script src="/js/editormd/lib/marked.min.js"></script>
<script src="/js/editormd/lib/prettify.min.js"></script>
<script src="/js/editormd/editormd.min.js"></script>

<script src="/js/editormd/lib/raphael.min.js"></script>
<script src="/js/editormd/lib/underscore.min.js"></script>
<script src="/js/editormd/lib/sequence-diagram.min.js"></script>
<script src="/js/editormd/lib/flowchart.min.js"></script>
<script src="/js/editormd/lib/jquery.flowchart.min.js"></script>
<script>
    var form = layui.form();
    var layer = layui.layer;
    var layedit = layui.layedit;
</script>


<body class="">
<div class="layui-header header header-doc">
    <div class="content-main">
        <a class="logo" href="/">
        </a>
        <ul class="layui-nav layui-nav-menu" lay-filter="">
            <li class="layui-nav-item <#if action?exists && action=='index'> layui-this  </#if>">
                <a href="/">首页</a>
            </li>
            <span class="layui-nav-bar" style="left: 0px; top: 55px; width: 0px; opacity: 0;"></span>

            <table class="body-menu">
                <tbody>
                    <tr>
                        <td><a>首页首页</a></td>
                    </tr>
                    <tr>
                        <td><a>首页2</a></td>
                    </tr>
                </tbody>
            </table>
        </ul>
        <ul class="layui-nav layui-nav-menu" lay-filter="" style="margin-left:200px;">
            <li class="layui-nav-item <#if action?exists && action=='article'> layui-this  </#if>">
                <a href="/article/list">文章</a>
            </li>
            <span class="layui-nav-bar" style="left: 0px; top: 55px; width: 0px; opacity: 0;"></span>
        </ul>
        <ul class="layui-nav layui-nav-menu" lay-filter="" style="margin-left:400px;">
            <li class="layui-nav-item <#if action?exists && action=='photo'> layui-this  </#if>">
                <a href="/photo/list">相册</a>
            </li>
            <span class="layui-nav-bar" style="left: 0px; top: 55px; width: 0px; opacity: 0;"></span>
        </ul>

        <ul class="layui-nav" lay-filter="" style="margin-left:500px;right:100px;">
            <li class="layui-nav-item <#if action?exists && action=='registerPage'> layui-this  </#if>">
                <a href="/account/registerPage" id="js-login-btn">注册</a>
            </li>
            <span class="layui-nav-bar" style="left: 0px; top: 55px; width: 0px; opacity: 0;"></span>
        </ul>

        <ul class="layui-nav" lay-filter="" style="margin-left:600px;">
            <li class="layui-nav-item <#if action?exists && action=='loginPage'> layui-this  </#if>">
                <a href="/account/loginPage" id="js-login-btn">登录</a>
            </li>
            <span class="layui-nav-bar" style="left: 0px; top: 55px; width: 0px; opacity: 0;"></span>
        </ul>
    </div>
</div>

<div class="body-content">
    <#nested>
</div>

<div  class="layui-footer footer footer-doc">
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
    $(document).ready(function () {
        $(".layui-nav-item").mouseover(function () {
            $(this).parent().find("table").show();
        });
        $(".layui-nav-item").parent().find("table").mouseleave(function () {
            $(this).hide();
        })
    })
</script>
</#macro>