/**
 * Created by wangxl on 2017/5/31.
 */
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD (Register as an anonymous module)
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        // Node/CommonJS
        module.exports = factory(require('jquery'));
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {

    var pluses = /\+/g;

    function encode(s) {
        return config.raw ? s : encodeURIComponent(s);
    }

    function decode(s) {
        return config.raw ? s : decodeURIComponent(s);
    }

    function stringifyCookieValue(value) {
        return encode(config.json ? JSON.stringify(value) : String(value));
    }

    function parseCookieValue(s) {
        if (s.indexOf('"') === 0) {
            // This is a quoted cookie as according to RFC2068, unescape...
            s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
        }

        try {
            // Replace server-side written pluses with spaces.
            // If we can't decode the cookie, ignore it, it's unusable.
            // If we can't parse the cookie, ignore it, it's unusable.
            s = decodeURIComponent(s.replace(pluses, ' '));
            return config.json ? JSON.parse(s) : s;
        } catch(e) {}
    }

    function read(s, converter) {
        var value = config.raw ? s : parseCookieValue(s);
        return $.isFunction(converter) ? converter(value) : value;
    }

    var config = $.cookie = function (key, value, options) {

        // Write

        if (arguments.length > 1 && !$.isFunction(value)) {
            options = $.extend({}, config.defaults, options);

            if (typeof options.expires === 'number') {
                var days = options.expires, t = options.expires = new Date();
                t.setMilliseconds(t.getMilliseconds() + days * 864e+5);
            }

            return (document.cookie = [
                encode(key), '=', stringifyCookieValue(value),
                options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
                options.path    ? '; path=' + options.path : '',
                options.domain  ? '; domain=' + options.domain : '',
                options.secure  ? '; secure' : ''
            ].join(''));
        }

        // Read

        var result = key ? undefined : {},
            // To prevent the for loop in the first place assign an empty array
            // in case there are no cookies at all. Also prevents odd result when
            // calling $.cookie().
            cookies = document.cookie ? document.cookie.split('; ') : [],
            i = 0,
            l = cookies.length;

        for (; i < l; i++) {
            var parts = cookies[i].split('='),
                name = decode(parts.shift()),
                cookie = parts.join('=');

            if (key === name) {
                // If second argument (value) is a function it's a converter...
                result = read(cookie, value);
                break;
            }

            // Prevent storing a cookie that we couldn't decode.
            if (!key && (cookie = read(cookie)) !== undefined) {
                result[name] = cookie;
            }
        }

        return result;
    };

    config.defaults = {};

    $.removeCookie = function (key, options) {
        // Must not alter options, thus extending a fresh object...
        $.cookie(key, '', $.extend({}, options, { expires: -1 }));
        return !$.cookie(key);
    };

}));

//UUID
function uuid(len, radix) {
    var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var uuid = [], i;
    radix = radix || chars.length;

    if (len) {
        // Compact form
        for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
    } else {
        // rfc4122, version 4 form
        var r;

        // rfc4122 requires these characters
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
        uuid[14] = '4';

        // Fill in random data.  At i==19 set the high bits of clock sequence as
        // per rfc4122, sec. 4.1.5
        for (i = 0; i < 36; i++) {
            if (!uuid[i]) {
                r = 0 | Math.random()*16;
                uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
            }
        }
    }

    return uuid.join('');
}

//日期处理
Date.prototype.Format = function(fmt){
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

//记录访问量
function vistor(_ip){
    var now = new Date();

    //参数
    //var _page = window.location.href;//访问地址
    var _page = window.location.href;//访问地址
    //var _ip = returnCitySN["cip"];//IP
    var _vdate = now.Format("yyyy-MM-dd");//日期
    var _vistor = '';//访客标识
    var _cnt = 1;//访问次数

    //获取访客
    var vistor_val = $.cookie("cookie_whg_vistor_val");
    if(vistor_val){
        _vistor = vistor_val;
    }else{
        _vistor = 'v'+uuid(16, 16);
        $.cookie("cookie_whg_vistor_val", _vistor, { expires: 7, path:'/' });//访客标识
    }

    //访客的访问次数
    var cookie_name = encodeURIComponent(_vdate+_vistor+_page);
    var cookie_val = $.cookie(cookie_name);
    if(cookie_val){
        _cnt = parseInt(cookie_val)+1;
        $.cookie(cookie_name, _cnt);
    }else{
        $.cookie(cookie_name, 1);
    }

    //解析type 1-pc 2-微信 3-安卓 4-IOS
    var tongji_pv = document.getElementById("whgtongji");
    var t_src = tongji_pv.src;
    var t_type = getType(t_src);

    console.log("_page:"+_page+
        "\n_ip:"+_ip+
        "\n_vdate:"+_vdate+
        "\n_vistor:"+_vistor+
        "\n_cnt:"+_cnt+
        "\nt_type:"+t_type+
        "\nt_src:"+t_src+
        "\nbasePath:"+getBasePath(t_src)+"------------");

    $.ajax({
        type: "POST",
        url: getBasePath(t_src)+"/visit/note",//服务端代码
        data: {
            vType: t_type,
            vDate: _vdate,
            vIp: _ip,
            visitor: _vistor,
            vPage: _page,
            vCount: _cnt
        },
        cache: false,
        success: function(msg){
            console.log(msg);
        }
    });
}

function getBasePath(tsrc) {
    var __basePath = '';

    if(tsrc && tsrc.length > 0){
        var _path = tsrc;
        for(var i=0; i<3; i++){
            var _idx = _path.indexOf("/");
            _path = _path.substring(_idx+1);
        }
        var __idx = tsrc.indexOf(_path);
        __basePath = tsrc.substring(0, __idx-1)
    }

   return __basePath;
}

function getType(tsrc) {
    var type = "1";//1-pc 2-微信 3-安卓 4-IOS
    var idx = tsrc.indexOf("?");
    if(idx > -1){
        var arr = tsrc.split("?");
        if(arr.length > 0){
            var params = arr[1].split("&");
            for(var i=0; i<params.length; i++){
                var param = params[i];
                if(param.indexOf("=") > -1){
                    var paraArr = param.split("=");
                    if(paraArr[0] == 'type'){
                        type = paraArr[1];
                    }
                }
            }
        }
    }
    return type;
}

$(function () {
    //取cookie中的IP
    var _ip =  $.cookie("cookie_whg_vistor_ip");
    if(!_ip){
        var tongji_pv = document.getElementById("tongji_pv");
        if(!tongji_pv){
            var hm = document.createElement("script");
            hm.src = "http://pv.sohu.com/cityjson?ie=utf-8";
            hm.id = 'tongji_pv';
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
            hm.onload = hm.onreadystatechange = function() {
                if (!this.readyState || this.readyState === "loaded" || this.readyState === "complete" ) {
                    $.cookie("cookie_whg_vistor_ip", returnCitySN["cip"]);
                    vistor(returnCitySN["cip"]);
                    hm.onload = hm.onreadystatechange = null;
                }
            };
        }
    }else{
        vistor(_ip);
    }
});
