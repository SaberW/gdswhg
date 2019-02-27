/**
 * Created by JackZeng on 2017/11/01.
 */

var dataInit = {
    option: {
        fn: {},
        params: {},
        async: true,
        type: "POST",
        forceUpdate : false,
        page:1,
        keepTime:300000,
        jsTicket:"http://kunnly.s1.natapp.cc/back/api/wechat/jsticket"
    },
    params:{},
    ajax: function (options) {
        var m = this;
        var opt = $.extend({}, m.option, options || {});
        var list_control = $("#list_control").val() || 1;
        //缓存为true、页码为第一页、缓存有值的情况下 取缓存。否则调用Ajax取数据
        if(opt.forceUpdate && opt.page <= 1 && m.isUpToDate(m.getStorage(opt.api)) && list_control == 1){
            var data = m.getStorage(opt.api);
            if (opt.fn && $.isFunction(opt.fn)) {
                opt.fn(data.Data);
                console.log('▄︻┻═┳一 ......正在获取缓存数据...... 一┳═┻︻▄');
            }
        }else {
            $.ajax({
                url: opt.api,
                data: opt.params,
                type: opt.type,
                async: opt.async,
                dataType: 'json',
                cache: false,
                beforeSend: m.LoadFunction,
                error: m.erryFunction,
                success: function (data) {
                    if(opt.page == 1 && opt.forceUpdate && list_control == 1){
                        var cached = {Updated:new Date().getTime(), Data:data};
                        m.setStorage(opt.api,cached);
                    }
                    if (opt.fn && $.isFunction(opt.fn)) {
                        opt.fn(data);
                    }
                }
            });
        }
    },
    LoadFunction: function () {

    },

    erryFunction: function (XMLHttpRequest, textStatus, errorThrown) {
        mui.toast(textStatus || errorThrown || '数据加载失败')
    },

    setStorage: function (key, value) {
        window.localStorage.setItem(key, JSON.stringify(value));
    },

    isUpToDate: function (cache) {
        var m = this;
        var d = new Date();
        if(cache){
            var s = d.getTime() - cache.Updated;
            var keepTime =  m.option.keepTime;
            return s < keepTime;
        }
        return false;
    },

    getStorage: function (key) {
        var val = window.localStorage[key];
        if (val) {
            return JSON.parse(val);
        }
        else
            return null;
    },

    resetStorage: function (key) {
        window.localStorage.removeItem(key);
    },
    //设置cache
    setCache: function (key,value) {
        window.sessionStorage.setItem(key, JSON.stringify(value));
    },
    //获取cache
    getCache: function (key) {
        var val = window.sessionStorage[key];
        if (val)
            return JSON.parse(window.sessionStorage.getItem(key));
        else
            return null;
    },
    //刷新cache
    resetCache: function resetCache(key) {
        window.sessionStorage.removeItem(key);
    },
    getSmallImage: function (url) {
        /*var img = url.split('.');
        if (img.length > 1)
            return img[0] + '_300_200.' + img[1];
        return url;*/

        if (/\.\w{3}$/.test(url)){
            return url.replace(/(\.\w{3})$/, '_300_200$1');
        }
        return url;
    },
    getMidImage: function (url) {
        /*var img = url.split('.');
        if (img.length > 1)
            return img[0] + '_750_500.' + img[1];
        return url;*/

        if (/\.\w{3}$/.test(url)){
            return url.replace(/(\.\w{3})$/, '_750_500$1');
        }
        return url;
    },
    getBigImage: function (url) {
        /*var img = url.split('.');
        if (img.length > 1)
            return img[0] + '_750_500.' + img[1];
        return url;*/

        if (/\.\w{3}$/.test(url)){
            return url.replace(/(\.\w{3})$/, '_750_500$1');
        }
        return url;
    },

    getFileTypeIcon: function(fileUrl, baseIconUrl){
        if (!fileUrl || fileUrl == '') return baseIconUrl;
        //文件后缀名小写处理
        var _matchs = fileUrl.match(/\.(\w+)$/);
        var ext = _matchs ? RegExp.$1 : '';
        ext = ext.toLowerCase();
        //组装不同的文档显示图片
        var icontag = '';
        switch (ext){
            //zip
            case 'rar': case 'zip': case 'gzip': case 'gz': case 'z': case '7z': case 'arj':
            icontag = "_zip"; break;
            //pdf
            case 'pdf':
                icontag = "_pdf"; break;
            //excel
            case 'xls': case 'xlsx':
            icontag = "_xls"; break;
            //word
            case 'doc': case 'docx':
            icontag = "_doc"; break;
        }
        if (icontag!='' && /\.\w{3}$/.test(baseIconUrl)){
            return baseIconUrl.replace(/(\.\w{3})$/, icontag+'$1');
        }else{
            return baseIconUrl;
        }
    },

    //取地址栏的参数
    getUrlParam: function (id) {
        var reg = new RegExp("(^|&)" + id + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null)
            return unescape(r[2]);
        return null;
    },
    //时间内容转换
    getTimeDiff: function (dateTimeStamp) {
        var minute = 1000 * 60;
        var hour = minute * 60;
        var day = hour * 24;
        var halfamonth = day * 15;
        var month = day * 30;
        return getTimeDiffHelp(dateTimeStamp);
        function getTimeDiffHelp(dateTimeStamp) {
            var now = new Date().getTime();
            var diffValue = now - dateTimeStamp;
            var result = '';
            if (diffValue >= 0) {
                var monthC = diffValue / month;
                var weekC = diffValue / (7 * day);
                var dayC = diffValue / day;
                var hourC = diffValue / hour;
                var minC = diffValue / minute;
                if (monthC >= 1) {
                    result = "" + parseInt(monthC) + "个月前";
                }
                else if (weekC >= 1) {
                    result = "" + parseInt(weekC) + "周前";
                }
                else if (dayC >= 1) {
                    result = "" + parseInt(dayC) + "天前";
                }
                else if (hourC >= 1) {
                    result = "" + parseInt(hourC) + "个小时前";
                }
                else if (minC >= 1) {
                    result = "" + parseInt(minC) + "分钟前";
                } else
                    result = "刚刚";
            }
            return result;
        }
    },
    //上拉翻页
    pullUp: function (pages, _Fun) {
        var m = this;
        m.params.size = pages.size ? pages.size : 10; //每页数
        m.params.page = pages.page ? pages.page : 1; //当前页码
        m.params.total = pages.total; //总条数
        m.params.maxPage = Math.ceil(m.params.total / m.params.size); //总页数
        mui.init({
            pullRefresh: {
                container: "#model-list-panel",//待刷新区域标识
                up: {
                    height: 100,//可选.默认50.触发上拉加载拖动距离
                    auto: false,//可选,默认false.自动上拉加载一次
                    contentrefresh: "文汇通数据正在加载",//可选，正在加载状态时，上拉加载控件上显示的标题内容
                    contentnomore: '没有更多数据',//可选，请求完毕若没有更多数据时显示的提醒内容；
                    callback: function () {
                        setTimeout(function() {
                            mui('#model-list-panel').pullRefresh().refresh(true);
                            mui('#model-list-panel').pullRefresh().endPullupToRefresh((m.params.page >= m.params.maxPage)); //参数为true代表没有更多数据了。
                            //参数为true代表没有更多数据了。
                            if(m.params.maxPage > 1) {
                                if(m.params.page < m.params.maxPage){
                                    m.params.page++;
                                    m.option.page = m.params.page;
                                    if ($.isFunction(_Fun)) {
                                        _Fun(m.params.page, m.params.size);

                                    }
                                }
                            }
                        },400);
                    }
                },down : {
                    height: 50,
                    callback : function () {
                        setTimeout(function() {
                            if ($.isFunction(_Fun)) {
                                _Fun(1, m.params.size);
                            }
                            m.option.page = 0;
                            mui('#model-list-panel').pullRefresh().endPulldownToRefresh();
                            mui('#model-list-panel').pullRefresh().refresh(true);
                        },400)
                    }
                }
            }
        });
    },

    //加载数据
    htmlAdd:function(page,html){
        $(".newbility-no-msg").remove();
        var m = this;
        var __temp_html = html;
        var __html = $(__temp_html);
        __html.find(".lazy").each(function(){
            var src  = $(this).attr("src");
            $(this).attr("data-original",src);
        })
        __html.find(".lazy").attr("src",$('#basePath').val()+"/assets/img/public/no-img.png");
        if(m.option.page == 0){
            $("#model-list").html("");
            m.option.page = 1;
        }
        $("#model-list").append(__html);
        $('img.lazy').lazyload({
            threshold : 600,
            skip_invisible : false,
            effect : "fadeIn",
            effectspeed:400
        });
        $(".newbility-no-msg").height($(window).height()-45);
    },

    //锚记加特效
    anchor: function () {
        $('a[href*=#],area[href*=#]').click(function () {
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
                var $target = $(this.hash);
                $target = $target.length && $target || $('[name=' + this.hash.slice(1) + ']');
                if ($target.length) {
                    var targetOffset = $target.offset().top;
                    $('html,body').animate({
                            scrollTop: targetOffset
                        },
                        400);
                    return false;
                }
            }
        });
    },
    //微信初始化
    initWechat:function(options){
        var m = this;
        var opt = $.extend({}, m.option, options || {});
        // alert(opt.jsTicket);
        m.ajax({
            api:opt.jsTicket,
            fn:function(data){
                m.configWechat(data.data);
            }
        })
    },
    //wechat config
    configWechat:function(ticket){
        //alert("ticket: " + JSON.stringify(ticket));
        wx.config({
            debug: false,
            appId: ticket.appId,
            timestamp: ticket.timestamp,
            nonceStr: ticket.nonceStr,
            signature: ticket.signature,
            jsApiList: [
                'checkJsApi',
                'onMenuShareTimeline',
                'onMenuShareAppMessage',
                'onMenuShareQQ',
                'onMenuShareWeibo',
                'onMenuShareQZone',
                'hideMenuItems',
                'showMenuItems',
                'hideAllNonBaseMenuItem',
                'showAllNonBaseMenuItem',
                'translateVoice',
                'chooseImage',
                'previewImage',
                'uploadImage',
                'downloadImage',
                'getNetworkType',
                'openLocation',
                'getLocation',
                'hideOptionMenu',
                'showOptionMenu',
                'closeWindow',
                'scanQRCode',
                'openProductSpecificView',
                'addCard',
                'chooseCard',
                'openCard'
            ]
        });
    },
    //微信地图
    menuMap:function (params) {
        var href = window.location.href;
        if(dataInit.isWeixin()){
            wx.openLocation({
                latitude: Number(params.latitude),
                longitude: Number(params.longitude),
                name: params.name,
                address: params.address,
                scale: 16, // 地图缩放级别,整形值,范围从1~28。默认为最大
                infoUrl: href
            });
        }else {
            mui.toast('请在微信中使用');
        }
    },
    //初始化LEFT-Bar
    createLeftBar:function(){
        var m = this;
        $('#page').prepend('<div class="assistive-wrap">' +
            '  <div class="assistive-touch">' +
            '    <a href="#menu" id="fix-menu">菜单</a>' +
            '  </div>' +
            '</div>' +
            '<div class="assistive-wrap assistive-top">' +
            '  <div class="assistive-touch">' +
            '    <a href="javascript:void(0)"><i class="iconfont icon-packup"></i></a>' +
            '  </div>' +
            '</div>');
        $('nav#menu').mmenu({
            extensions: ['effect-slide-menu', 'pageshadow'],
            searchfield: true,
            counters: true,
            navbars: [
                {
                    position: 'top'
                }, {
                    position: 'bottom',
                    content: ['<a href="javascript:void(0)">广东省文汇通</a>']
                }
            ]
        });
        $("#page,#menu").height($(window).height());

    },
    showAre:function(id,api) {
        var __cityName = "", cityList = [];
        var areList = $("#areList");
        var __city = $.cookie('city');
        $("#areList").html("");
        if(id=='' || id == undefined || id == 'undefined'){
           // jQuery("#city").val(__city?__city:'');
            __cityName = '广东省';
            cityList = changeProvince(__cityName);
            areList.append('<li class="active city" _province="广东省" _city="" _area="" item-data="" data-txt="区域">全部</li>');
            for (var i in cityList) {
                areList.append('<li class="city" _province="广东省" _city="'+cityList[i].name+'" _area="" item-data="'+cityList[i].name+'">'+cityList[i].name+'</li>');
            }
        }else {
            //不做城市筛选
           // jQuery("#city").val(__city?__city:'');
            __cityName = id;
            cityList = changeCity(__cityName);
            if(cityList.length>0) {
                areList.append('<li class="active city" _province="广东省" _city="'+__cityName+'" _area="" item-data="" data-txt="区域">全部</li>');
                for (var i in cityList) {
                    areList.append('<li class="city" _province="广东省" _city="'+__cityName+'" _area="'+cityList[i].name+'" item-data="'+cityList[i].name+'">'+cityList[i].name+'</li>');
                }
            }else{//为空时 说明检索的是 区域以下的镇街，此时要用部门来替换
                jQuery(".mui-control-item[data-index='c-1']").html('服务站' + '<i></i>');
                jQuery("#city").val('');
                dataInit.ajax({
                    api: api,
                    fn: function (data) {
                        areList.append('<li class="active area" item-data="" data-txt="服务站">全部</li>');
                        if (data.code == 0 && data.data) {
                            if (data.rows.length) {
                                for (var i = 0; i < data.rows.length; i++) {
                                    var model = data.rows[i];
                                    areList.append('<li class="area" item-data="'+model.id+'">'+model.name+'</li>');
                                }
                            }
                        }
                    }, //文化联盟
                    params: {
                        cultid: $.cookie('cultid')
                    }
                });
            }
        }
    },
    //过滤HTML
    removeHTMLTag:function(str) {
       // str = str.replace(/<\/?[^>]*>/g,''); //去除HTML tag
        str = str.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
        //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
        str=str.replace(/&nbsp;/ig,'');//去掉&nbsp;
        return str;
    },
    isWeixin:function() {
        var ua = navigator.userAgent.toLowerCase();
        return ua.indexOf('micromessenger') != -1;
    },
    isAndroid:function() {
        var ua = navigator.userAgent.toLowerCase();
        return ua.indexOf('android') != -1;
    },
    isIos:function() {
        var ua = navigator.userAgent.toLowerCase();
        return (ua.indexOf('iphone') != -1) || (ua.indexOf('ipad') != -1);
    },
    encodeUrl:function(url){
        var url = url ? url : window.location.href;

        var basePath = $("#basePath").val();
        url = url.substring(basePath.length, url.length);

        url = encodeURIComponent(url);
        return url;
    }
}
