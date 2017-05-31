<#assign contextPath=request.getContextPath()>
<#assign title="注册">
<#include '/common/head.ftl' >
<@defaultLayout>
<h1 class="layui-layer-title">登录</h1>

<form class="layui-form" action="">
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">手机号码</label>
            <div class="layui-input-inline">
                <input type="tel" name="mobilePhone" lay-verify="phone" autocomplete="off" class="layui-input">
            </div>
            <div class="layui-form-mid layui-word-aux">请输入手机号码</div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">密码</label>
            <div class="layui-input-inline">
                <input type="password" name="password" lay-verify="password" autocomplete="off" class="layui-input">
            </div>
            <div class="layui-form-mid layui-word-aux">请填写6到12位密码</div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" type="button" id="submitBtn" lay-submit="">立即提交</button>
        </div>
    </div>
</form>

<script type="text/javascript" src="/js/login.js"></script>
</@defaultLayout>