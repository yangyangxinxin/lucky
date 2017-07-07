<#assign contextPath=request.getContextPath()>
<#assign title="查看文本">
<#include '/common/head.ftl' >
<#assign action='article'>
<@defaultLayout>

<fieldset class="layui-elem-field layui-field-title">
    <legend id="title"></legend>
    <div class="layui-field-box" id="content">
    </div>
    <div class="layui-field-box" id="">
        创建时间：<span id="createTime"></span><br/>
        作者：<span id="author"></span><br/>
        最后修改时间：<span id="updateTime"></span><br/>
    </div>
    <button class="layui-btn" id="edit">编辑文章</button>
    <button class="layui-btn" id="commentBtn">添加评论</button>
</fieldset>


<fieldset class="layui-elem-field layui-field-title">
    <legend id="">评论列表</legend>
    <div class="layui-field-box" id="">

    </div>
</fieldset>
<script>
    $(document).ready(function () {
        var id = $app.getRequest().articleId;
        var loginUserId = ${loginUserId!};
        var obj;
        $("#edit").click(function () {
            window.location.href = "/article/editPage?articleId=" + id;
        });

        $.get("/article/getDetail", {'articleId': id}, function (data) {
            if (data.success) {
                $("#title").text(data.data.title);
                $('#content').html('');
                $("#createTime").text(data.data.createTimeFormat);
                $("#author").text(data.data.author);
                $("#updateTime").text(data.data.updateTimeFormat);
                obj = data.data;
                editormd.markdownToHTML("content", {
                    markdown: data.data.content,
                    htmlDecode: "style,script,iframe",
                    path: "/js/editormd/lib/",
                    syncScrolling: "single",
                    emoji: true,
                    taskList: true,
                    tex: true,
                    flowChart: true
                });
                if(data.data.ownerUserId != loginUserId){
                    $(".layui-btn").hide();
                }
                $("#edit").click(function () {
                    window.location.href="/article/editPage?articleId=" + data.data.articleId;
                });

                //
                $("#commentBtn").click(function () {
                    //例子1
                    layer.prompt({formType:2,title:'添加评论'},function(value, index, elem){
                        var data = {
                            "content" : value,
                            "articleId" : id
                        };
                        $.post("/articleComments/addComments",data,function (data) {
                            if(data.success){
                                layer.msg("评论成功");
                            }else{
                                layer.msg(data.msg);
                            }
                        },"json");
                        layer.close(index);
                    });
                })
            }
        }, 'json')


    })


</script>
</@defaultLayout>