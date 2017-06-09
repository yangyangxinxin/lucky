<#assign contextPath=request.getContextPath()>
<#assign title="list">
<#include '/common/head.ftl' >
<@defaultLayout>

    <#if  photos?exists  >
        <#list photos as photo>
            <img src="${photo.httpUrl!}"/><a photoId="${photo.photoId!}" name="delete">删除</a>
        </#list>
    </#if>

    <button onclick="window.location.href='/photo/uploadPage'" class="layui-btn">上传文件</button>

<script>
    $(document).ready(function () {
        $("a[name='delete']").click(function () {
            var photoId = $(this).attr("photoId");
            $.post("/photo/delete",{'photoId':photoId},function (data) {
                if(data.success){
                    layer.alert("删除成功！",function () {
                        parent.location.reload();
                    })
                }else{
                    layer.alert(data.msg);
                }
            },"json")
        })
    })
</script>

</@defaultLayout>