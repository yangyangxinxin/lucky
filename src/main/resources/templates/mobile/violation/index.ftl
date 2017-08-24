<#assign contextPath=request.getContextPath()>
<#assign title="查违章">
<#assign action="loginPage">
<#include '/common/mobile_header.ftl' >
<#assign hasFooter = false>
<@mobileLayout>
<!-- 这里是页面内容区 -->

        <input type="file" id="file" name="file" style="display: none;"/>

    <div class="content-block content-padded">
        <p><a href="javascript:void(0);" class="button button-big button-round" onclick="document.getElementById('file').click()">请上传行驶证</a></p>
        <p>
            注意：<br/>
            1.上传行驶证主要是为了不让你手动输入车牌号、车架号和发动机号码，系统自动识别。本网站不会记录你的个人信息。请放心使用。
        </p>
    </div>


</@mobileLayout>
<script>
    $(document).ready(function () {
        $("#file").fileupload({
            url:"/m/violation/query",
            add:function (e,data) {
                $.showPreloader('正在查询中，请稍后');
                data.submit();
            },
            done:function (e,data) {
                $.hidePreloader();
                var result = data.result;
                if(result.success){
                    data = result.data;
                    var plateNumber = data.plateNumber;
                    var engineNo = data.engineNo;
                    var vin = data.vin;
                    var amount = data.amount;
                    var untreated = data.untreated == null ? 0 : data.untreated;
                    var totalFine = data.totalFine== null ? 0 : data.totalFine;
                    var totalPoints = data.totalPoints== null ? 0 : data.totalPoints;
                    var str = "车牌号:" + plateNumber +  "共" + amount + " 条违章记录。" + " 未处理违章条数 ： " + untreated + ",未处理违章总罚款: " + totalFine+
                            "未处理违章总扣分 :" + totalPoints + " .";
                    $.alert(str);
                }else{
                    $.alert(data.msg);
                }
            }
                    
        })
    })
</script>