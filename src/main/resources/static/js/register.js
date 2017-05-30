/**
 * Created by yangxin on 2017/5/30.
 */
$(document).ready(function(){
    $("#submitBtn").click(function(){
        var data = $("form").serialize();
        $.ajax({
            url:"/account/doRegister",
            data:data,
            async:false,
            dataType:"json",
            type:"post",
            success:function(data){
                if(data.success){
                    console.log(data);
                    layer.alert("注册成功！",function () {
                        window.location.href = "/account/loginPage";
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