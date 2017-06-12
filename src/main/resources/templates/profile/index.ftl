<#assign contextPath=request.getContextPath()>
<#assign title="">
<#include '/common/head.ftl' >
<@defaultLayout>
username : ${user.username!}<br/>
mobilePhone : ${user.mobilePhone!}<br/>
<img src="${user.httpUrl!}"/>
</@defaultLayout>