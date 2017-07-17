<#assign contextPath=request.getContextPath()>
<#assign title="登录">
<#assign action="loginPage">
<#include '/common/mobile_header.ftl' >
<@mobileLayout>

<div class="page-login">
    <div class="list-block inset text-center">
        <h3>登录</h3>
        <ul>
            <li>
                <div class="item-content">
                    <div class="item-media"><i class="icon icon-form-name"></i></div>
                    <div class="item-inner">
                        <div class="item-input">
                            <input type="text" placeholder="mobile" name="mobile" id="mobile">
                        </div>
                    </div>
                </div>
            </li>
            <li>
                <div class="item-content">
                    <div class="item-media"><i class="icon icon-form-email"></i></div>
                    <div class="item-inner">
                        <div class="item-input">
                            <input type="password" placeholder="Password" name="password" id="password">
                        </div>
                    </div>
                </div>
            </li>
        </ul>
    </div>
    <div class="content-block">
        <p><a class="button button-big button-fill external" href="#" data-transition="fade" id="login">Login</a></p>
        <p class="text-center signup">
            <a href="#" class="pull-left">No Accout?</a>
            <a href="#" class="pull-right">Forget Password?</a>
        </p>
    </div>
</div>
</@mobileLayout>
<script>
    $(document).ready(function () {
        var returnUrl = $app.getRequest().returnUrl;
        console.log(returnUrl);
        $("#login").click(function () {
            var mobile = $("#mobile").val();
            var password = $("#password").val();
            $.post("/m/account/doLogin", {"mobile": mobile, "password": password}, function (data) {
                if (data.success) {
                    if (returnUrl) {
                        window.location.href = encodeURIComponent(returnUrl);
                    } else {
                        window.location.href = "/m/index";
                    }
                } else {
                    $.alert(data.msg);
                }
            }, "json")
        })
    })
</script>