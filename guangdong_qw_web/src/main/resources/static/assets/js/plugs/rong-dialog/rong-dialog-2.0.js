/*!
 *
 RongDialog - v2.0.0 (2017-06-02T14:55:51+0800)
 *
 zrongs@vip.qq.com
 *
 http://hn.creatoo.cn
 */
var rongDialog = {
    option: {
        ico: 1,
        type: 1,
        size: 0,
        title: "友情提示",
        showTime: 200,
        closeTime: 200,
        overTime: 2000,
        nextFn: function () {
        },
        closeFn: function () {
        },
        imgUrl : "",
        imgSize: "4:3",
        zoomImg: 'rong-zoom',
        vedioSkin: "vodTransparent",
        vedioType: "mp4",
        basePath : "",
        zIndex: 1000
    },

    init: function (options) {
        var m = this;
        //合并参数到主对象OPTION
        m.opt = $.extend({}, m.option, options || {});
        m.closeMaskPanel();
        m.change();
        m.btnClosePanel();
    },

    //弹出层绝对居中
    positionCenter: function (dom) {
        var m = this;
        var zNum = m.opt.zIndex >= 0 ? m.opt.zIndex : 1000;
        dom.css({
            position: 'fixed',
            left: ($(window).width() - dom.outerWidth()) / 2,
            top: ($(window).height() - dom.outerHeight()) / 2,
            zIndex: zNum
        })
    },

    //关闭弹出层和弹出的阴影
    closeMaskPanel: function () {
        var m = this;
        var dom = $(".r_dialog,.rong_Alert_Dialog,.rong_DIY_Dialog,.rong_vedio_Dialog,.rong_select_Dialog,.r_mask");
        if(m.opt.type == 6){
            $('.imgareaselect-outer').remove();
            $('.imgareaselect-selection').parent("div").remove();
        }
        dom.fadeOut(m.opt.closeTime);
        setTimeout(function () {
            dom.remove();
        }, m.opt.closeTime);
    },

    //初始化层HTML
    change: function () {
        var m = this;
        var type = m.opt.type ? m.opt.type : 1;
        var panelHTML = "";
        var panelName = "";
        //普通提示弹出层
        if (type == 1) {
            var ico = "s";
            if(m.opt.ico == 2){
                ico = "e";
            }else if(m.opt.ico == 3){
                ico = "l";
            }
            var desc = m.opt.desc ? m.opt.desc : "";
            panelHTML = "<div class=\"r_dialog r_none\"><i class=\"" + ico + "\"></i><p>" + desc + "</p></div><div class=\"r_mask\"></div>";
            panelName = "r_dialog"
        }

        //带说明的弹出层
        if (type == 2) {
            var ico = m.opt.ico >= 1 && m.opt.ico <= 3 ? m.opt.ico : 1;
            var title = m.opt.title ? "<span>" + m.opt.title + "</span>" : "";
            var desc = m.opt.desc ? "<span>" + m.opt.desc + "</span>" : "";
            var closeIco = m.opt.closeIco ? "" : "none";
            var okBtn = "<div class=\"r_btn goNext float-left\"> <a href=\"javascript:void(0)\" class=\"js__r_ok\">确定</a> </div>";
            var coloeBtn = "<div class=\"r_btn goBack float-left\"> <a href=\"javascript:void(0)\" class=\"js__r_close\">取消</a> </div>";
            panelHTML =
                "<div class=\"rong_Alert_Dialog r_none clearfix\">" +
                "<a href=\"javascript:void(0)\" class=\"r_close js__r_close\" title=\"关闭\"></a>" +
                "<div class=\"r_content\"><p>" + title + "</p></div>" +
                "<div class=\"r_main\"><div class=\"r_ico r_ico_" + ico + " " + closeIco + "\"></div>" + desc + "</div>" + okBtn + coloeBtn + "</div>" +
                "<div class=\"r_mask\"></div>";
            panelName = "rong_Alert_Dialog"
        }

        //自定义的弹出层
        if (type == 3) {
            if (!$(m.opt.panelDIV).length) {
                alert("type3为自定义弹出层，panelDIV属性不能为空");
            }
            var panelHTML = "<div class=\"rong_DIY_Dialog r_none\"><a href=\"javascript:void(0)\" class=\"r_close js__r_close\" title=\"关闭\"></a>" + $(m.opt.panelDIV).prop("outerHTML") + "</div>" +
                "<div class=\"r_mask\"></div>";
            panelName = "rong_DIY_Dialog"
        }

        //相册弹出层
        if (type == 4) {
            var script = document.createElement('script');
            if ($(document).find("script[src$='viewer.min.js']").length == 0) {
                script.type = "text/javascript";
                script.src = m.opt.basePath+"/assets/js/plugs/rong-dialog/viewer/viewer.min.js";
                $(m.opt.imgGroupId).prepend(script);
                var imgOpt = {
                    url: m.opt.zoomImg
                }
                $(m.opt.imgGroupId).viewer(imgOpt);
            }
            panelName = "rong_img_Dialog";
        }

        //多媒体弹出层
        if (type == 5) {
            var panelHTML = "<div class=\"rong_vedio_Dialog\"><a href=\"javascript:void(0)\" class=\"r_close js__r_close\" title=\"关闭\"></a><div id=\"rong_vedio\"></div></div><div class=\"r_mask\"></div>";
            panelName = "rong_vedio_Dialog";
        }

        //截图弹出层
        if (type == 6) {
            var imgUrl = m.opt.imgUrl;
            var title = m.opt.title;
            imgUrl = "<img src='"+imgUrl+"' id='bee'>";
            var panelHTML =
                "<div class='rong_select_Dialog none'>" +
                "<a href='javascript:void(0)' class='r_close js__r_close' title='关闭'></a>" +
                "<div id='rong_select'>"+imgUrl+"</div>" +
                "<h2>"+title+"</h2>"+
                // "<div class='submit-cont'><a class='js__r_ok caijian'>裁剪</a>" +
                //      "<input type='hidden' name='x1' />" +
                //      "<input type='hidden' name='y1' />" +
                //      "<input type='hidden' name='x2' />" +
                //      "<input type='hidden' name='y2' />" +
                // "</div>" +
                "</div>" +
                "<div class='r_mask'></div>";
            panelName = "rong_select_Dialog";
        }
        //在BODY中加入弹出层HTML
        $("body").prepend(panelHTML);

        if (type == 3) {
            $(".rong_DIY_Dialog " + m.opt.panelDIV).show();
        }
        if (type == 5) {
            m.addPlayer();
        }
        if(type != 6){
            m.initDialogSize($("." + panelName));
        }else{
            setTimeout(function(){
                m.initDialogSize($("." + panelName));
                m.positionCenter($("." + panelName));
            },10);
            /*$('#bee').imgAreaSelect({ aspectRatio: m.opt.imgSize,
             onSelectEnd: function (img, selection) {
             $('input[name="x1"]').val(selection.x1);
             $('input[name="y1"]').val(selection.y1);
             $('input[name="x2"]').val(selection.x2);
             $('input[name="y2"]').val(selection.y2);
             }
             });*/
            /*$("#select-img-btn").on("click",function () {
             alert($('input[name="x1"]').val());
             alert($('input[name="y1"]').val());
             alert($('input[name="x2"]').val());
             alert($('input[name="y2"]').val());
             })*/
        }
        var dom = $(".r_dialog,.rong_Alert_Dialog,.rong_DIY_Dialog,.rong_vedio_Dialog,.rong_select_Dialog,.r_mask");
        dom.fadeIn(m.opt.showTime);
        dom.fadeIn(m.opt.showTime);
        if (type == 1) {
            setTimeout(function () {
                m.closeMaskPanel();
                m.opt.nextFn();
            }, m.opt.overTime + m.opt.showTime);
        }
        //窗口变化的时候给弹出层更新定位
        $(window).resize(function () {
            m.positionCenter($("." + panelName));
        })
    },
    //给弹出层加自定义宽高
    initDialogSize: function (dom) {
        var m = this;
        var size = "";
        if (m.opt.size != 0) {
            size = m.opt.size;
        } else {
            if (m.opt.type == 1) {
                size = "auto,142";
                m.resizePanel(dom,size)
            } else if (m.opt.type == 5) {
                size = "auto,700";
                m.resizePanel(dom,size)
            } else if (m.opt.type == 6) {
                var img = $("#rong_select img");
                var temp_img = new Image();
                temp_img.src = $("#rong_select img").attr('src');
                var timer = setInterval(function() {
                    if(temp_img.complete){
                        var height = img.height();
                        var width = img.width();
                        size = height+','+width;
                        m.resizePanel(dom,size)
                        clearInterval(timer);
                    }
                },50);
                $("#rong_select img").css({
                    maxHeight:$(window).height()-200,
                    maxWidth:$(window).width()
                });
            } else {
                size = "auto,520";
                m.resizePanel(dom,size)
            }
        }

    },
    //窗口大小设置
    resizePanel:function(dom,size){
        var m = this;
        var size = size.split(",");
        var h = size[0];
        var w = size[1];
        dom.css({
            height: h,
            width: w
        });
        m.positionCenter(dom);
    },
    //按钮加事件
    btnClosePanel: function () {
        var m = this;
        $(".js__r_close").on("click", function () {
            if (m.opt.type == 5) {
                m.removePlayer();
            }
            m.opt.closeFn();
            m.closeMaskPanel();
        });
        $(".js__r_ok").on("click", function () {
            m.opt.nextFn();
            m.closeMaskPanel();
        })
    },
    state: "removed",
    //添加视频插件
    addPlayer: function () {
        var m = this;
        var srcPath = m.opt.basePath+"/assets/js/plugs/rong-dialog/player/sewise.player.min.js?server=vod&type=" +
            m.opt.vedioType + "&videourl=" +
            m.opt.vedioUrl + "&sourceid=&autostart=true&starttime=0&lang=en_US&logo=" +
            m.opt.vedioImg + "&title=" +
            encodeURIComponent(m.opt.vedioTitle) +
            "&buffer=5&skin=" + m.opt.vedioSkin;
        var fallbackurls = {
            ogg: "http://www.w3schools.com/html/mov_bbb.ogg",
            webm: "http://www.w3schools.com/html/mov_bbb.webm"
        }
        if (m.state == "removed") {
            var script = document.createElement('script');
            script.type = "text/javascript";
            script.src = srcPath + "&fallbackurls=" + encodeURIComponent(JSON.stringify(fallbackurls, "", "\t"));
            $("#rong_vedio").get(0).appendChild(script);
            m.state = "added";
        }
    },

    //关闭视频插件
    removePlayer: function () {
        var m = this;
        /*if(window.SewisePlayer){
         SewisePlayer.doStop();
         }*/
        if (m.state == "added") {
            $("#rong_vedio").empty();
            m.state = "removed";

            if (window.vedioInterval) {
                window.clearInterval(window.vedioInterval);
            }
        }
    }
}