<#assign contextPath=request.getContextPath()>
<#assign title="list">
<#assign action='photo'>
<#include '/common/head.ftl' >
<@defaultLayout>
    <div class="site-demo-flow" id="LAY_demo3">
    </div>
    <#--<button onclick="window.location.href='/photo/uploadPage'" class="layui-btn">上传文件</button>-->
<script>
    $(document).ready(function () {
        layui.use('flow', function() {
            var flow = layui.flow;
            flow.load({
                elem: '#LAY_demo3' //流加载容器
                ,isAuto: true
                ,isLazyimg: true
                ,done: function(page, next){ //加载下一页
                    setTimeout(function(){
                        $.get("/photo/queryPage?itemPage=" + page,function (data) {
                            layer.msg("加载第" + page + "页");
                            if(data.success){
                                var list = data.data.list;
                                if(list.length > 0){
                                    var lis = [];
                                    for(var i = 0; i <list.length; i++){
                                        lis.push("<img lay-src='" + list[i].httpUrl + "' style='width:30%;'>")
                                    }
                                    next(lis.join(''), page < data.data.totalPage); //假设总页数为 6
                                }
                            }else{
                                layer.alert(data.msg);
                            }
                        },'json')
                    }, 500);
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