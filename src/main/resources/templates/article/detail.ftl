<#assign contextPath=request.getContextPath()>
<#assign title="查看文本">
<#include '/common/head.ftl' >
<#assign action='article'>
<@defaultLayout>

<fieldset class="layui-elem-field layui-field-title">
    <legend id="title"></legend>
    <div class="layui-field-box" id="content">
    </div>
</fieldset>
<script>
    $(document).ready(function () {

        var id = $app.getRequest().articleId;

        $("#edit").click(function () {
            window.location.href = "/article/editPage?articleId=" + id;
        });

        $.get("/article/getDetail", {'articleId': id}, function (data) {
            if (data.success) {
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
        }, 'json')
    })


</script>
</@defaultLayout>