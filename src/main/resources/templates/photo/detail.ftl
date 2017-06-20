<#assign contextPath=request.getContextPath()>
<#assign title="相片详情">
<#include '/common/head.ftl' >
<#assign action='photo'>
<@defaultLayout>
    <#if photo?? >
        <fieldset class="layui-elem-field layui-field-title">
            <legend id="title">${photo.name!}</legend>
            <div class="layui-field-box" id="content">
                <img class="" src="${photo.httpUrl!}"/>
                <div>上传用户：${photo.username!}</div>
                <div>上传时间：${photo.createTime!}</div>
                <div>
                    <#if userInfo?exists && userInfo.userId == photo.userId>
                        <a title="删除" style="cursor: pointer;" name="delete" photoId="${photo.photoId!}">
                            <i class="layui-icon" style="font-size: 36px;">
                                &#xe640;
                            </i>
                        </a>
                    </#if>
                    <a title="下载" href="/download/photo?photoId=${photo.photoId!}"><i class="layui-icon" style="font-size: 36px;">&#xe601;</i></a>
                </div>
            </div>
        </fieldset>
    <#else>
        ${msg!}
    </#if>
</@defaultLayout>
<script>
    $(document).ready(function () {
        $("a[name='delete']").click(function () {
            var id = $(this).attr("photoId");
            layer.confirm("你确定删除吗？",function () {
                deletePhoto(id);
            })
        });

        function deletePhoto(id) {
            $.post("/photo/delete",{'photoId':id},function(data){
                if(data.success){
                    layer.alert("删除成功！",function(){
                        location.href = "/photo/list";
                    })
                }else{
                    layer.msg(data.msg);
                }
            },'json');
        }
    })
</script>