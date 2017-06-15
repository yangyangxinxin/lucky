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
<div id="demo1"></div>
</@defaultLayout>
<script>

    var pages = ${pages!};
    var currentPage = ${currentPage!};
    $(document).ready(function(){
        layui.use(['laypage', 'layer'], function() {
            var laypage = layui.laypage, layer = layui.layer;

            laypage({
                cont: 'demo1',
                pages: pages, //总页数,
                groups: 5, //连续显示分页数,
                skip:true,
                curr:function () {
                    return currentPage;
                }(),
                jump: function(obj, first){
                    //得到了当前页，用于向服务端请求对应数据
                    console.log(obj);
                    var curr = obj.curr;
                    if(!first){
                        window.location.href="/article/list?itemPage="+curr;
                    }

                }
            });
        });
    })
</script>