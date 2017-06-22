<#assign contextPath=request.getContextPath()>
<#assign title="个人资料">
<#assign action='profile'>
<#include '/common/head.ftl' >
<@defaultLayout>
<fieldset class="layui-elem-field layui-field-title">
    <legend id="title">个人资料</legend>
    <div class="layui-field-box" id="content">
        <div>
            用户名：${user.username!}
        </div>
        <div>
            电话号码：${user.mobilePhone!}
        </div>

    </div>
</fieldset>
</@defaultLayout>