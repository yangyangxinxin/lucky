<#assign contextPath=request.getContextPath()>
<#assign title="list">
<#assign action="list">
<#include '/common/head.ftl' >
<@defaultLayout>
<table class="layui-table" lay-even lay-skin="nob">
    <colgroup>
        <col width="150">
        <col width="200">
        <col>
    </colgroup>
    <thead>
    <tr>
        <th>文章标题</th>
        <th>作者</th>
        <th>创建时间</th>
        <th>最后修改时间</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <#if (size>0) && articles?exists >
        <#list articles as article>
        <tr>
            <td>${article.title!}</td>
            <td>${article.author!}</td>
            <td>${article.createTime!}</td>
            <td>${article.updateTime!}</td>
            <td><a href="/article/detail?articleId=${article.articleId!}">查看</a></td>
        </tr>
        </#list>
    </#if>

    </tbody>
</table>
</@defaultLayout>
<script>
    $(document).ready(function(){
        
    })
</script>