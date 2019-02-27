/**
 * Created by JackZeng on 2017/06/12.
 */
jQuery.support.cors = true;
var dataInit = {
    option: {
        fn: {},
        params: {},
        async: true,
        type: "POST"
    },
    dict: {},
    tags: {},
    ajax: function (options) {
        var m = this;
        var opt = options || {params: {}};
        var api = opt.api;
        var userAgent = navigator.userAgent;
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        //如果是IE9-、Edge、Safari就用控制器调AJAX接口
        if (fIEVersion <= 9 && fIEVersion!=0) {
            //alert(fIEVersion);
            opt.params = opt.params ? opt.params : opt.params = {};
            opt.params.api = api;
            $.ajax({
                url: $("#temp_url").val() + "/sys_api",
                data: opt.params || m.option.params,
                type: opt.type || m.option.type,
                async: opt.async || m.option.async,
                dataType: 'json',
                cache: false,
                beforeSend: m.LoadFunction,
                error: m.erryFunction,
                success: function (data) {
                    if (opt.fn && $.isFunction(opt.fn)) {
                        opt.fn(data);
                    }
                }
            })
        } else {
            $.ajax({
                url: opt.api,
                data: opt.params || m.option.params,
                type: opt.type || m.option.type,
                async: opt.async || m.option.async,
                dataType: 'json',
                cache: false,
                beforeSend: m.LoadFunction,
                error: m.erryFunction,
                success: function (data) {
                    if (opt.fn && $.isFunction(opt.fn)) {
                        // console.info(data);
                        opt.fn(data);
                    }
                }
            });
        }
    },
    LoadFunction: function () {
        /*rongDialog.init({
         ico : 3,
         type : 1,
         showTime: 300,
         closeTime: 300,
         overTime: 100,
         desc : '数据加载中'
         });*/
    },
    erryFunction: function (XMLHttpRequest, textStatus, errorThrown) {
        rongDialog.init({
            ico: 2,
            type: 1,
            desc: textStatus || errorThrown || '数据加载失败'
        });
    },
    getSmallImage: function (url) {
        var img = url.split('.');
        if (img.length > 1)
            return img[0] + '_300_200.' + img[1];
        return url;
    },
    getMidImage: function (url) {
        var img = url.split('.');
        if (img.length > 1)
            return img[0] + '_750_500.' + img[1];
        return url;
    },
    getBigImage: function (url) {
        var img = url.split('.');
        if (img.length > 1)
            return img[0] + '_740_555.' + img[1];
        return url;
    },
    //取地址栏的参数
    getUrlParam: function (id) {

        var reg = new RegExp("(^|&)" + id + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null)
            return unescape(r[2]);
        return null;
    },
    //分页
    pageInit: function (divID, page, pageSize, total, _Fun) {
        if (total > 0 && total > pageSize) {//大于一页
            var totalPage = Math.ceil(total / pageSize);//总页数
            var _html = '';
            //前5页
            var _lastCnt = 0;
            var _prePage = parseInt(page);
            for (var i = 1; i < 6; i++) {
                _prePage = page - i;
                if (_prePage > 0) {
                    var __url = _prePage;//_url+'&page='+_prePage;
                    _html = '<a href="' + __url + '">' + _prePage + '</a>' + _html;
                } else {
                    _lastCnt = 5 + 1 - i;
                    break;
                }
            }
            if (_prePage > 1) {
                _html = '...' + _html;
            }
            if (page == 1) {
                //var __url = page-1;//_url+'&page=1';
                //_html = '<span class="pre" page="'+__url+'"></span>'+_html;
            } else {
                var __url = parseInt(page) - 1;//_url+'&page=1';
                _html = '<span class="disabled prev" href="' + __url + '">&nbsp;</span>' + _html;
            }

            //当前页
            _html += '<span class="current">' + page + '</span>';

            //后5页
            var len = 6 + _lastCnt;
            var nextPage = parseInt(page);
            for (var i = 1; i < len; i++) {
                nextPage = parseInt(page) + i;
                if (nextPage <= totalPage) {
                    var __url = nextPage;
                    _html += '<a href="' + __url + '">' + nextPage + '</a>';
                }
            }
            if (nextPage < totalPage) {
                _html += '...';
            }
            if (page == totalPage) {
                //_html += '<span class="next" page="'+__url+'">&nbsp;</span>';
            } else {
                var __url = parseInt(page) + 1;
                _html += '<span class="disabled next" href="' + __url + '">&nbsp;</span>';
            }

            //输出到界面
            $('#' + divID).html(_html);

            //注册事件
            $('#' + divID + ' a').bind('click', function (e) {
                e.preventDefault();
                var curpage = $(this).attr('href');
                if ($.isFunction(_Fun)) {
                    _Fun(curpage, pageSize);
                }
            });
            $('#' + divID + ' span.disabled').bind('click', function (e) {
                e.preventDefault();
                var curpage = $(this).attr('href');
                if ($.isFunction(_Fun)) {
                    _Fun(curpage, pageSize);
                }
            });
        } else {
            $('#' + divID).html('');
        }
    },
    //分享
    share: function (title, imgPath) {
        $('a.fxweibo').each(function (i) {
            var _url = escape(window.location.href);
            //var _url = encodeURIComponent('http://hn.creatoo.cn/');
            var _title = title;
            var _imgUrl = imgPath;
            $(this).removeClass('wxldisabled').attr('target', '_blank').attr('href', 'http://service.weibo.com/share/share.php?title=' + _title + '&url=' + _url + '&source=bookmark&appkey=2992571369&pic=' + _imgUrl + '&ralateUid=');
        });
        $('a.fxqq').each(function (i) {
            var _url = encodeURIComponent(window.location.href);
            //var _url = encodeURIComponent('http://hn.creatoo.cn/');
            var _title = title;
            var _imgUrl = imgPath;
            $(this).removeClass('wxldisabled').attr('target', '_blank').attr('href', 'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=' + _url + '&title=' + _title + '&pics=' + _imgUrl + '&summary=');
        });
        /**
         * 分享到微信朋友圈
         */
        if ($('a.fxweix').size() > 0) {
            $.getScript('http://v3.jiathis.com/code/jia.js?uid=1', function () {
                $('a.fxweix').each(function (i) {
                    $(this).attr('class', 'jiathis_button_weixin ' + $(this).attr('class'));
                });
            });
        }
    },
    //打开字典
    getDict: function (api) {
        var m = this;
        m.ajax({
            api: api,
            fn: function (data) {
                m.dict = data;
            }
        })
    },
    //开始查分类字典
    checkDict: function (id) {
        var m = this;
        var dict = m.dict;
        var checkName = "";
        for (var i in dict.data) {
            if (dict.data[i].id == id) {
                checkName = dict.data[i].name;
                break;
            }
        }
        return checkName;
    },
    //开始查标签字典
    getTag: function (api) {
        var m = this;
        m.ajax({
            api: api,
            fn: function (data) {
                m.tags = data;
            }
        })
    },
    //开始查分类字典
    checkTag: function (id) {
        var m = this;
        var tags = m.tags;
        var checkName = "";
        for (var i in tags.data) {
            if (tags.data[i].id == id) {
                checkName = tags.data[i].name;
                break;
            }
        }
        return checkName;
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
    }
}
