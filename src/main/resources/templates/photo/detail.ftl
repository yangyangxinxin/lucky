<#assign contextPath=request.getContextPath()>
<#assign title="detail">
<#include '/common/head.ftl' >
<@defaultLayout>

    <#if photo?? >
        <span>name:${photo.name!}</span><br/>
        <span>创建时间:${photo.createTime!}</span><br/>
        <img class="" src="${photo.httpUrl!}"/>
    <#else>
        ${msg!}
    </#if>

</@defaultLayout>