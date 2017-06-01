<#assign contextPath=request.getContextPath()>
<#assign title="注册">
<#include '/common/head.ftl' >
<@defaultLayout>
<input type="file" name="file" class="layui-upload-file" multiple/>上传文件
<hr/>
<img style="width:100%;"/>

<script>
    layui.use('upload', function(){
        layui.upload({
            url: '/photo/doUploadMultipart', //上传接口,
            success: function(res){ //上传成功后的回调
                console.log(res);
            }
        });

    });

</script>
</@defaultLayout>