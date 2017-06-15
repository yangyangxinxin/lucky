<#assign contextPath=request.getContextPath()>
<#assign title="个人资料">
<#assign action='profile'>
<#include '/common/head.ftl' >
<@defaultLayout>
username : ${user.username!}<br/>
mobilePhone : ${user.mobilePhone!}<br/>
<img src="${user.httpUrl!}"/>
</@defaultLayout>