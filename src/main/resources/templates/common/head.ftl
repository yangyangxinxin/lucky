<#assign contextPath=request.getContextPath()>
<#macro defaultLayout>
<#if Session.userInfo?exists>
    <#assign userInfo = Session.userInfo>
    <#assign loginUserId = Session.userInfo.userId>

    <#else>
        <#assign loginUserId = 0>
</#if>
<#if domainUtils?exists>
    <#assign indexUrl = domainUtils.indexUrl>

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
<#--<script src="/js/weather.js"></script>-->
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
    <#--<li class="layui-nav-item">
        <span id="time"></span>
    </li>-->
    <#--<li class="layui-nav-item">
        <div id="tp-weather-widget"></div>
    </li>-->
    <#if userInfo?exists>
        <li class="layui-nav-item <#if action?exists && action=='profile'> layui-this </#if>" style="margin-left:800px"><a href="/profile/index">${userInfo.username!}</a></li>
        <li class="layui-nav-item" style="margin-left:0px;"><a href="/account/logout">退出</a></li>
        <#else>
        <li class="layui-nav-item <#if action?exists && action=='loginPage'> layui-this </#if>" style="margin-left:800px;"><a href="javascript:window.location.href='/account/loginPage?returnUrl=' + encodeURIComponent(location.href)">登录</a></li>
        <li class="layui-nav-item <#if action?exists && action=='registerPage'> layui-this </#if>"  style="margin-left:0px;"><a href="/account/registerPage">注册</a></li>
    </#if>

</ul>
<div class="body-content">
    <#nested>
</div>

<div class="layui-footer footer footer-doc">
    <div class="layui-main">
        <p>2017 © <a href="/">${indexUrl!}</a></p>
        <p>
            <a href="/" target="_blank">yangxin</a>
            <a href="https://github.com/yangyangxinxin/lucky" target="_blank">github</a>
        </p>
    </div>
</div>
</body>
</html>
<script>
    $(document).ready(function () {
        // 对Date的扩展，将 Date 转化为指定格式的String
        // 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
        // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
        // 例子：
        // (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
        // (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
        Date.prototype.Format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }

        getTime();

        setInterval(function () {
            getTime();
        },1000);

       function getTime() {
           var time2 = new Date().Format("yyyy-MM-dd hh:mm:ss");
           $("#time").text(time2);
       }

    })
</script>
</#macro>