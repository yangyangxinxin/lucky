<#assign contextPath=request.getContextPath()>
<#assign title="新建文章">
<#include '/common/head.ftl' >
<#assign action='article'>
<@defaultLayout>
<#--<input name="title"/>
    <textarea id="demo" style="display: none;"></textarea>
    <button class="layui-btn">提交</button>
    <script>
        var layedit = layui.layedit;

        var str = "&lt;script&gt;alert('123')&lt;/script&gt;";

        $("#demo").text(str);
        var index = layedit.build('demo'); //建立编辑器
        
        $(".layui-btn").click(function () {
            var content = layedit.getContent(index);
            var title = $("input[name='title']").val();
            $.post("/article/doCreate",{'title':title,'content' :content},function (data) {
                if(data.success){
                    layer.alert("提交成功");
                }else{
                    layer.alert(data.msg);
                }
            },'json')
        })
        
    </script>-->

<input name="title"/>
<div class="editormd" id="content-editormd">
</div>
<button class="layui-btn">提交</button>
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