<#assign contextPath=request.getContextPath()>
<#assign title="activeUser">
<#assign action="activeUser">
<#include '/common/head.ftl' >
<@defaultLayout>
    <#if code == 0>
        激活成功！
    <#else >
        ${msg!}
    </#if>
</@defaultLayout>
<script>
    $(document).ready(function(){
        
    })
</script>