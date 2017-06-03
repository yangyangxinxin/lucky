/**
 * Created by yangxin on 2017/6/3.
 */
function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition, onError);
    } else {
        var str = "Geolocation is not supported by this browser";
        console.log(str);
    }

    if (window.coord) {
        return window.coord;
    }

}

function showPosition(position) {
    window.coord = {
        'latitude': position.coords.latitude,
        'longitude': position.coords.longitude
    };
}

//失败时
function onError(error) {
    switch (error.code) {
        case 1:
            console.log("位置服务被拒绝");
            break;

        case 2:
            console.log("暂时获取不到位置信息");
            break;

        case 3:
            console.log("获取信息超时");
            break;

        case 4:
            console.log("未知错误");
            break;
    }

}
