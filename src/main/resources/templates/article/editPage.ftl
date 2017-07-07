<#assign contextPath=request.getContextPath()>
<#assign title="编辑文本">
<#include '/common/head.ftl' >
<#assign action='article'>
<@defaultLayout>

<#--
文章标题：<input id="title" value=""/>
<div class="editormd" id="content-editormd">
</div>
<button class="layui-btn">提交</button>
-->

<form class="layui-form" action="#">
    <div class="layui-form-item" style="float: left;margin-left:30px;">
        <label class="layui-form-label">文章标题</label>
        <div class="layui-input-block">
            <input type="text" name="title" id="title" lay-verify="title" autocomplete="off" placeholder="请输入标题" class="layui-input">
        </div>
    </div>
    <div style="float: right;margin-right:30px;">
        <i class="layui-icon" style="font-size:30px;cursor: pointer;" title="发布" id="submit">&#xe609;</i>
    </div>
    <div class="editormd" id="content-editormd">
    </div>

</form>


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

        $("#submit").click(function () {
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