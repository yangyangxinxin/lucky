/**
 * Created by yangxin on 2017/5/30.
 */
$(document).ready(function(){
    $("#submitBtn").click(function(){
        var mobile = $("input[name='mobilePhone']").val();
        var password = $("input[name='password']").val();
        var data = {
            'mobile' : mobile,
            'password' : password
        };
        $.ajax({
            url:"/account/doLogin",
            data:data,
            async:false,
            dataType:"json",
            type:"post",
            success:function(data){
                if(data.success){
                    console.log(data);
                    layer.alert("登录成功！",function () {
                        var returnUrl = $app.getRequest().returnUrl;
                        if(returnUrl){
                            window.location.href = decodeURIComponent(returnUrl);
                        }
                    });
                }else{
                    layer.alert(data.msg);
                }
            },
            error:function(e){
                console.log(e);
                layer.alert("服务器繁忙，请稍后再试");
            }
        })
    })
})