/* Created by JackZeng on 2017/11/02 */

var select = {
    control: true, //菜单打开时为true,否则为false
    create: function (_fn) {
        var m = this;
        //缓存为true、页码为第一页、缓存有值的情况下 取缓存。否则调用Ajax取数据
        m.create_hidden_input();
        m.navWidthInit();
        m.change(_fn);
    },
    change: function (_fn) {
        var m = this;
        mui("#category-cont .mui-scroll").on("tap", "a", function () {
            if (m.control) {
                jQuery(".category-mask").fadeIn(function () {
                    m.control = false;
                });
            } else {
                jQuery("#category-cont .mui-scroll .mui-control-item").children("i").removeClass("up");
                jQuery(".category-group").hide();
            }
            jQuery(this).children("i").addClass("up");
            var _cont_index = jQuery(this).attr("data-index");
            jQuery(".category-group[data-index='" + _cont_index + "']").fadeIn();
        });
        mui(".category-group ul").on("tap", "li:not(.city)", function () {
            if(jQuery(this).hasClass('area')){return;}
            jQuery(this).addClass("active").siblings().removeClass("active");
            var _cont_index = jQuery(this).parents('.category-group').attr("data-index");
            var _temp_txt = jQuery(this).attr('data-txt') ? jQuery(this).attr('data-txt') : jQuery(this).text();
            var _value = jQuery(this).attr("item-data");
            jQuery("#"+_cont_index).val(_value);
            jQuery(".mui-control-item[data-index='" + _cont_index + "']").html(_temp_txt + '<i></i>');
            setTimeout(function () {
                if(jQuery.isFunction(_fn)) {
                    jQuery("#model-list").html("");
                    if(!jQuery("body").find("#list_control").length>0){
                        jQuery("body").prepend("<input type='hidden' id='list_control' value='0'>");
                    }
                    mui('#model-list-panel').pullRefresh().scrollTo(0,0,0);
                    mui('#model-list-panel').pullRefresh().refresh(true);
                    _fn();
                }
                m.close();
            }, 200);
        })
        mui(".category-group ul").on("tap", "li.city", function () {
            jQuery(this).addClass("active").siblings().removeClass("active");
            var _cont_index = jQuery(this).parents('.category-group').attr("data-index");
            var _temp_txt = jQuery(this).attr('data-txt') ? jQuery(this).attr('data-txt') : jQuery(this).text();
            var _value = jQuery(this).attr("item-data");
            var _province = jQuery(this).attr("_province");
            var _city = jQuery(this).attr("_city");
            var _area = jQuery(this).attr("_area");
            jQuery("#province").val("");
            jQuery("#city").val("");
            jQuery("#area").val("");
            if (_province && _province != "") {
                jQuery("#province").val(_province);
            }
            if (_city && _city != "") {
                jQuery("#city").val(_city);
            }
            if (_area && _area != "") {
                jQuery("#area").val(_area);
            }
            jQuery(".mui-control-item[data-index='" + _cont_index + "']").html(_temp_txt + '<i></i>');
            setTimeout(function () {
                if(jQuery.isFunction(_fn)) {
                    jQuery("#model-list").html("");
                    if(!jQuery("body").find("#list_control").length>0){
                        jQuery("body").prepend("<input type='hidden' id='list_control' value='0'>");
                    }
                    mui('#model-list-panel').pullRefresh().scrollTo(0,0,0);
                    mui('#model-list-panel').pullRefresh().refresh(true);
                    _fn();
                }
                m.close();
            }, 200);
        })
        mui(".category-group ul").on("tap", "li.area", function () {
            jQuery(this).addClass("active").siblings().removeClass("active");
            var _cont_index = jQuery(this).parents('.category-group').attr("data-index");
            var _temp_txt = jQuery(this).attr('data-txt') ? jQuery(this).attr('data-txt') : jQuery(this).text();
            var _value = jQuery(this).attr("item-data");
            jQuery("#deptid").val(_value);
            jQuery(".mui-control-item[data-index='" + _cont_index + "']").html(_temp_txt + '<i></i>');
            setTimeout(function () {
                if(jQuery.isFunction(_fn)) {
                    jQuery("#model-list").html("");
                    if(!jQuery("body").find("#list_control").length>0){
                        jQuery("body").prepend("<input type='hidden' id='list_control' value='0'>");
                    }
                    mui('#model-list-panel').pullRefresh().scrollTo(0,0,0);
                    mui('#model-list-panel').pullRefresh().refresh(true);
                    _fn();
                }
                m.close();
            }, 200);
        })
        jQuery(".category-mask").on("click", function () {
            m.close();
        })
    },
    close: function () {
        var m = this;
        jQuery("#category-cont .mui-scroll .mui-control-item").children("i").removeClass("up");
        jQuery(".category-mask").fadeOut(function () {
            jQuery(".category-group").hide();
            m.control = true;
        })
    },
    //生成隐藏域
    create_hidden_input: function () {
        var m = this;
        var __hd_html = "";
        jQuery("#category-cont .mui-scroll .mui-control-item").each(function(i){
            var _city_select = $.cookie('city');
            if(i == 0){
                __hd_html += "<input type='hidden' id='"+jQuery(this).attr('data-index')+"' value='"+_city_select+"'>";
            }else {
                __hd_html += "<input type='hidden' id='"+jQuery(this).attr('data-index')+"'>";
            }
        })
        __hd_html +="<input type='hidden' id='deptid'>"
        jQuery("body").prepend(__hd_html);
    },
    //初始化宽度
    navWidthInit:function(){
        var length = jQuery("#category-cont .mui-scroll").find("a").length;
        if(length < 4){
            jQuery("#category-cont .mui-scroll").css({
                width:'100%'
            })
        }
        if(length == 1){
            jQuery("#category-cont .mui-scroll a").css({
                width:'100%',
                padding:'0px'
            })
        }else if(length == 2){
            jQuery("#category-cont .mui-scroll a").css({
                width:'50%',
                padding:'0px'
            })
        }else if(length == 3){
            jQuery("#category-cont .mui-scroll a").css({
                width:'33.33%',
                padding:'0px'
            })
        }else{
            jQuery("#category-cont .mui-scroll a").css({
                padding:'0px 25px'
            })
        }
    }
}
