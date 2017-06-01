<#assign contextPath=request.getContextPath()>
<#assign title="detail">
<#include '/common/head.ftl' >
<@defaultLayout>

    <#if photo?? >
        <span>name:${photo.name!}</span><br/>
        <span>创建时间:${photo.createTime!}</span><br/>
        <img style="width:10%;height:10%" src="${photo.httpUrl!}"/>
    </#if>

</@defaultLayout>