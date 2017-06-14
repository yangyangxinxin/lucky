<#assign contextPath=request.getContextPath()>
<#assign title="编辑文本">
<#include '/common/head.ftl' >
<#assign action='article'>
<@defaultLayout>

文章标题：<input id="title" value=""/>
<div class="editormd" id="content-editormd">
</div>
<button class="layui-btn">提交</button>
</@defaultLayout>
<script>
    $(document).ready(function () {
        var editor;
        var id = $app.getRequest().articleId;
        $.get("/article/getDetail", {'articleId': id}, function (data) {
            if (data.success) {
                $("#title").val(data.data.title);
                $('.editormd').html('');
                editor = editormd("content-editormd", {
                    width: "100%",
                    markdown: data.data.content,
                    htmlDecode: "style,script,iframe",
                    height: 900,
                    syncScrolling: "single",
                    path: "/js/editormd/lib/",
                    saveHTMLToTextarea: true,
                    emoji: true,
                    flowChart: true
                });
            }
        }, 'json');

        $(".layui-btn").click(function () {
            var title = $("#title").val();
            var content = editor.getMarkdown();
            $.post("/article/modify", {'content': content, 'title': title, 'articleId': id}, function (data) {
                if (data.success) {
                    layer.alert("修改成功", function () {
                        window.location.href = "/article/detail?articleId=" + id;
                    })
                }else{
                    layer.msg(data.msg);
                }
            }, 'json');
        })

    })
</script>