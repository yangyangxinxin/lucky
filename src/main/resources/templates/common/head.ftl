<#assign contextPath=request.getContextPath()>
<#macro defaultLayout>
<!DOCTYPE html>
<html lang="zh-cn">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>${title!}</title>
<link rel="stylesheet" href="${contextPath}/layui/css/layui.css" media="all">
<script src="${contextPath}/layui/layui.js" charset="utf-8"></script>
<script src="${contextPath}/js/jquery.min.js"></script>
<script>
    layui.use(['form', 'layedit', 'laydate', 'laypage'], function () {
        var form = layui.form()
                , layer = layui.layer
                , layedit = layui.layedit
                , laydate = layui.laydate, laypage = layui.laypage
    });
</script>


<body>
    <#nested>
</body>

</html>
</#macro>