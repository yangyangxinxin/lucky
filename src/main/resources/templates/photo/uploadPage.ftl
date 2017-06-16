<#assign contextPath=request.getContextPath()>
<#assign title="上传文件">
<#include '/common/head.ftl' >
<#assign action='photo'>
<@defaultLayout>

<form class="layui-form" action="#">
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">文件：</label>
            <div class="layui-input-inline">
                <input type="file" name="file" class="layui-upload-file" multiple/>
            </div>
        </div>
    </div>

</form>
<script>
    layui.use('upload', function(){
        var index;
        // 默认的上传文件类型是图片类型
        layui.upload({
            url: '/photo/doUploadMultipart', //上传接口,
            before : function (input) {
                 index= layer.load(1, {
                    shade: [0.1,'#fff'] //0.1透明度的白色背景
                });
            },
            title: '请选择文件',
            success: function(res,input){ //上传成功后的回调
                layer.close(index);
                //window.location.href = "/photo/list"
                layer.msg("上传成功");

            }
        });

    });

</script>
</@defaultLayout>