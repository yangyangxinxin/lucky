<#assign contextPath=request.getContextPath()>
<#assign title="新建文章">
<#include '/common/head.ftl' >
<#assign action='article'>
<@defaultLayout>
<form class="layui-form" action="#">
    <div class="layui-form-item" style="float: left;margin-left:30px;">
        <label class="layui-form-label">文章标题</label>
        <div class="layui-input-block">
            <input type="text" name="title" lay-verify="title" autocomplete="off" placeholder="请输入标题" class="layui-input">

        </div>
    </div>
    <div style="float: right;margin-right:30px;">
        <button class="layui-btn" type="button">提交</button>
    </div>
    <div class="editormd" id="content-editormd">
    </div>

</form>
<script>
    var editor;
    editor = editormd("content-editormd", {
        width: "100%",
        markdown: '',
        htmlDecode: "style,script,iframe",
        height: 900,
        syncScrolling: "single",
        path: "/js/editormd/lib/",
        saveHTMLToTextarea: true,
        emoji: true,
        flowChart: true
    });

    $(".layui-btn").click(function () {
        var content = editor.getMarkdown();
        var title = $("input[name='title']").val();
        $.post("/article/doCreate",{'title':title,'content' :content},function (data) {
            if(data.success){
                layer.alert("提交成功",function () {
                    window.location.href="/article/detail?articleId=" + data.data;
                });
            }else{
                layer.alert(data.msg);
            }
        },'json')
    })
</script>
</@defaultLayout>