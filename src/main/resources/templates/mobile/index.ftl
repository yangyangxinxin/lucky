<#assign contextPath=request.getContextPath()>
<#assign title="首页">
<#assign action="index">
<#include '/common/mobile_header.ftl' >
<@mobileLayout>
<div class="content-block">
    <h1>欢迎访问本网站</h1>
    <p>
        亲爱的
        <#if userInfo??>
                ${userInfo.username!}
           <#else>
                访客
        </#if>

        您好：</p>
    <p>
        现在电脑版有相对“完整”的功能，手机版还在路上！如果您方便的话，可以用电脑访问我的网站。
    </p>
    <p>
        如果您是开发者，有兴趣的话可以查看项目源码：<a href="https://github.com/yangyangxinxin/lucky">github</a>，查看README可以查看项目说明和相关更新日志
    </p>
    <h3><a href="/m/account/loginPage">点这里去登录页</a></h3>
</div>
</@mobileLayout>
<script>
    $(document).ready(function(){
        
    })
</script>