<#assign contextPath=request.getContextPath()>
<#assign title="查看文本">
<#include '/common/head.ftl' >
<@defaultLayout>
<#if article?exists>
    <p>
        <span>${article.title!}</span>
        <p>
            ${article.content!}
        </p>
    </p>
</#if>
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

</script>
</@defaultLayout>