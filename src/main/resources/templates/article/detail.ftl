<#assign contextPath=request.getContextPath()>
<#assign title="查看文本">
<#include '/common/head.ftl' >
<#assign action='article'>
<@defaultLayout>
<div>
    <p>
        <span id="title"></span>
    <p>
    <div id="content">
    </div>
    <button id="edit" class="layui-btn">修改文章</button>
</div>
<script>
    $(document).ready(function () {

        var id = $app.getRequest().articleId;

        $("#edit").click(function () {
            window.location.href="/article/editPage?articleId="+id;
        })

        $.get("/article/getDetail",{'articleId':id},function (data) {
            if(data.success){
                $("#title").text(data.data.title);
                $('#content').html('');
                editormd.markdownToHTML("content", {
                    markdown: data.data.content,
                    htmlDecode: "style,script,iframe",
                    path: "/js/editormd/lib/",
                    syncScrolling: "single",
                    emoji: true,
                    taskList: true,
                    tex: true,
                    flowChart: true
                });
            }
        },'json')
    })


</script>
</@defaultLayout>