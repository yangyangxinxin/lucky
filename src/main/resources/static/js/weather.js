/**
 * Created by yangxin on 2017/6/16.
 */
<!--天气-->
<!--天气-->
(function (T, h, i, n, k, P, a, g, e) {
    g = function () {
        P = h.createElement(i);
        a = h.getElementsByTagName(i)[0];
        P.src = k;
        P.charset = "utf-8";
        P.async = 1;
        a.parentNode.insertBefore(P, a)
    };
    T["ThinkPageWeatherWidgetObject"] = n;
    T[n] || (T[n] = function () {
        (T[n].q = T[n].q || []).push(arguments)
    });
    T[n].l = +new Date();
    if (T.attachEvent) {
        T.attachEvent("onload", g)
    } else {
        T.addEventListener("load", g, false)
    }
}(window, document, "script", "tpwidget", "//widget.thinkpage.cn/widget/chameleon.js"))
tpwidget("init", {
    "flavor": "slim",
    "location": "WWFJ5S4DNJ0T",
    "geolocation": "enabled",
    "language": "zh-chs",
    "unit": "c",
    "theme": "chameleon",
    "container": "tp-weather-widget",
    "bubble": "disabled",
    "alarmType": "badge",
    "color": "#FFFFFF",
    "uid": "U4C6D842DC",
    "hash": "6b1556c41efa37989cc49a09e316c66a"
});
tpwidget("show");