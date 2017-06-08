<#assign contextPath=request.getContextPath()>
<#assign title="编辑文本">
<#include '/common/head.ftl' >
<@defaultLayout>
    <textarea id="demo" style="display: none;"></textarea>
    <button class="layui-btn">提交</button>
    <script>
        var layedit = layui.layedit;

        var str = "&lt;script&gt;alert('123')&lt;/script&gt;";

        $("#demo").text(str);
        var index = layedit.build('demo'); //建立编辑器
        
        $(".layui-btn").click(function () {
            var content = layedit.getContent(index);
            console.log(content);
            var text = layedit.getText(index);
            console.log(text);

        })
        
    </script>
</@defaultLayout>