<#assign contextPath=request.getContextPath()>
<#macro defaultLayout>
<!DOCTYPE html>
<html lang="zh-cn">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>${title!}</title>
<link rel="stylesheet" href="${contextPath}/layui/css/layui.css" media="all">
<script src="${contextPath}/layui/lay/dest/layui.all.js" charset="utf-8"></script>
<script src="${contextPath}/js/jquery.min.js"></script>
<script>
    var form = layui.form();
    var layer = layui.layer;
    var layedit = layui.layedit;
</script>


<body class="layui-body">
    <#nested>
</body>

</html>
</#macro>