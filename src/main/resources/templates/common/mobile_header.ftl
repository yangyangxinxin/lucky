<#macro mobileLayout>
    <#assign contextPath=request.getContextPath()>
    <#if Session.userInfo?exists>
        <#assign userInfo = Session.userInfo>
        <#assign loginUserId = Session.userInfo.userId>
    <#else>
        <#assign loginUserId = 0>
    </#if>
    <#if domainUtils?exists>
        <#assign indexUrl = domainUtils.indexUrl>
    </#if>


    <#if Session.night?exists>
        <#assign nightShift = Session.night>
    <#else>
        <#assign nightShift = false>
    </#if>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>${title!}</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <meta name="screen-orientation" content="portrait">
    <meta name="full-screen" content="yes">
    <meta name="browsermode" content="application">
    <meta name="x5-orientation" content="portrait">
    <meta name="x5-fullscreen" content="true">
    <meta name="x5-page-mode" content="app">
    <meta name="mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-title" content=""/>
    <meta name="msapplication-TileColor" content="#20b5b3">
    <link rel="stylesheet" type="text/css" href="/mobile/css/light7.css">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="-1">

    <script src="${contextPath}/js/jquery.min.js"></script>
    <script src="/mobile/js/light7.js"></script>
    <script src="${contextPath}/js/common/app.js"></script>
</head>

<body>
<!-- page 容器 -->
<div class="page <#if nightShift> theme-dark </#if>" >
    <!-- 标题栏 -->
    <header class="bar bar-nav">
        <a class="icon icon-me pull-left open-panel"></a>
        <h1 class="title">${title!}</h1>
    </header>

    <!-- 工具栏 -->
    <nav class="bar bar-tab">
        <a class="tab-item active" href="/m/index">
            <span class="icon icon-home"></span>
            <span class="tab-label">首页</span>
        </a>
        <a class="tab-item" href="#">
            <span class="icon icon-star"></span>
            <span class="tab-label">相册</span>
        </a>
        <a class="tab-item" href="#">
            <span class="icon icon-settings"></span>
            <span class="tab-label">文章</span>
        </a>
    </nav>

    <!-- 这里是页面内容区 -->
    <div class="content">
        <#nested>

    </div>
</div>

<!-- popup, panel 等放在这里 -->
<div class="panel-overlay"></div>
<!-- Left Panel with Reveal effect -->
<div class="panel panel-left panel-reveal">
    <div class="content-block">
        <ul>
            <li>
                <div class="item-content">
                    <div class="item-inner">
                        <div class="item-title label" style="width: 60%;">Night Mode</div>
                        <div class="item-input">
                            <label class="label-switch">
                                <input type="checkbox" id="dark-switch" <#if nightShift> checked </#if> >
                                <div class="checkbox"></div>
                            </label>
                        </div>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</div>
</body>
</html>
<script>
    $(document).ready(function () {
        $("#dark-switch").change(function () {
            var status = 0;
            if($(this).prop("checked")){
                status = 1;
            }else{
                status = 0;
            }
            $.post("/m/setNightShift",{"status":status},function (data) {
                if(data.success){
                    if(data.data == 1){
                        $("body").addClass("theme-dark");
                    }else{
                        $("*").removeClass("theme-dark");
                    }
                }
            },"json");
        })
    })
</script>
</#macro>