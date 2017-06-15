<#assign contextPath=request.getContextPath()>
<#assign title="list">
<#assign action='photo'>
<#include '/common/head.ftl' >
<@defaultLayout>

    <#if  photos?exists  >
        <div class="site-demo-flow" id="LAY_demo3">
        <#list photos as photo>
            <img lay-src="${photo.httpUrl!}" style="width: 30%;">
        </#list>
        </div>
    <#-- <img src="${photo.httpUrl!}"/><a photoId="${photo.photoId!}" name="delete">删除</a><a href="/download/photo?photoId=${photo.photoId!}">下载</a>-->
    </#if>

    <button onclick="window.location.href='/photo/uploadPage'" class="layui-btn">上传文件</button>

<script>
    $(document).ready(function () {
        //按屏加载图片
        layui.use('flow', function() {
            var flow = layui.flow;
            flow.lazyimg({
                elem: '#LAY_demo3 img',
                done: function(page, next){
                    //请注意：layui 1.0.5 之前的版本是从第2页开始返回，也就是说你的第一页数据并非done来触发加载
                    //从 layui 1.0.5 的版本开始，page是从1开始返回，初始时即会执行一次done回调。
                    //console.log(page) //获得当前页

                    //执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
                    //只有当前页小于总页数的情况下，才会继续出现加载更多
                    next('列表HTML片段', page < res.pages);
                }
            });
        });
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