/*!
 *
 RongDatePicker - v1.0.0 (2017-09-07T14:44:52+0800)
 *
 zrongs@vip.qq.com
 *
 http://hn.creatoo.cn
 */

var rongDatepicker = {

	//基本参数
    option: {
    	domId:'#rong-date-init',   //日历框窗口
        sltDomId:'#rong-date-selected',  //已选中的日期展示
        dateFormat: 'yy-mm-dd', //日期格式
        inline: true,
        monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        dayNamesMin: ["日", "一", "二", "三", "四", "五", "六"],
        defaultDate: "",
        openDays: [],
        fstMonthApi:"", //获取最近一月 接口路径
        openDaysApi:"", //获取当前选中月的数据 接口路径
        id:"", //数据的ID
        maxSelect:7, //最多可选择的天数 设置为0时代表不做任何限制,
        openDays:[],
        userDays:[]

    },

	//初始化数据
	init:function(options){
        var m = this;
        m.opt = $.extend({}, m.option, options || {});
        m.getFstYMD();
	},

    //获取最近有数据的一个月的年月日
    getFstYMD:function(){
	    var m = this;
	    alert('0-0-0');
        dataInit.ajax({
            api: m.opt.fstMonthApi,
            params: {id:id},
            fn: function(data){
                alert(data);
                if(data.code == 0){
                    m.opt.defaultDate = data.data;
                    m.createDatePanel();
                }else{
                   /* rongDialog.init({
                        ico : 2,
                        type : 1,
                        desc : data.msg || "后台报错"
                    });*/
                }
            }
        })
    },
	//创建日历基本面板
	createDatePanel:function(){
		var m = this;
		var dom = $(m.opt.domId);
        dom.datepicker({
            dateFormat: m.opt.dateformat,
            inline: m.opt.inline,
            monthNames: m.opt.monthNames,
            dayNamesMin:  m.opt.dayNamesMin,
            /*onSelect: m.opt.onSelect,*/
            defaultDate: m.internationalDate(m.opt.defaultDate),
            onChangeMonthYear:function(year,month,inst){
                m.changeMonth(year,month,inst);
            }
        });
        var params = m.opt.defaultDate.split("-");
        m.changeMonth(params[0],params[1]);
	},

	//统一浏览器标准时间
    internationalDate:function(daystr, timestr){
        var day = new Date();
        var dstrs = /(\d{4})-(\d{1,2})-(\d{1,2})/.exec(daystr);
        if (dstrs){
            day.setFullYear( dstrs[1] );
            day.setMonth(parseInt(dstrs[2])-1);
            day.setDate( dstrs[3] );
        }
        if (timestr){
            var timestrs = /(\d{2}):(\d{2})/.exec(timestr);
            if (timestrs){
                day.setHours(timestrs[1]);
                day.setMinutes(timestrs[2]);
                day.setSeconds(0);
            }
        }else{
            day.setHours(0);
            day.setMinutes(0);
            day.setSeconds(0);
        }
        return day;
    },

    //切换月份
    changeMonth:function(year,month,inst){
        var m = this;
        var params = {
            id: m.opt.id,
            year:year,
            month:month
        }
        dataInit.ajax({
            api: m.opt.openDaysApi,
            params: params,
            fn:function(data){
                /*data.data.useDays = [28,30]; //数据DEMO */
                m.restructure(data.data.days,data.data.useDays,m.opt.openDays);
            }
        })
    },

    //渲染可预订的坐位
    restructure:function(openDays,useDays,tempDays){
        var m = this;
        var dom=$(".ui-datepicker-calendar tbody tr td");
        dom.children('a').attr('href','javascript:void(0)');
        dom.unbind('click');
        dom.each(function(i){
            var _temp_num = $(this).children('a').text();
            _temp_num = Number(_temp_num);
            if($.inArray(_temp_num,openDays) != -1){
                $(this).addClass('openDay');
                $(this).bind('click',function(){
                    m.changeDay($(this));
                })
            }
            if($.inArray(_temp_num,useDays) != -1){
                $(this).unbind('click');
                $(this).removeClass('openDay');
                $(this).addClass('booked');
            }
            var date = m.dateNumInit($(this));
            if($.inArray(date,tempDays) != -1){
                $(this).attr('data-date',date);
                $(this).addClass('select');
            }

        })
    },

    //数字补0
    appendZero:function(num){
        if(num < 10) return "0" +""+ num;
        else return num;
    },

    //选择日期
    changeDay:function (dom){
        var m = this;
        if(dom.hasClass('select')){
            dom.removeClass('select');
            m.delDay(dom);
        }else{
            var cut = $(m.opt.sltDomId).children("span").size();
            if(cut < m.opt.maxSelect || m.opt.maxSelect == 0){
                dom.addClass('select');
                m.showDay(dom);
            }else{
                /*rongDialog.init({
                    ico : 2,
                    type : 1,
                    desc : "最多可选中"+m.opt.maxSelect+"天"
                });*/
            }
        }
        m.addTempData()
        //console.log(m.opt.openDays);
    },

    //显示已选择时间
    showDay:function(dom){
        var m = this;
        var date = m.dateNumInit(dom);
        var td = $(".ui-datepicker-calendar tbody tr td");
        dom.attr('data-date',date);
        $(m.opt.sltDomId).append("<span>"+date+"<i></i></span>");
        $(m.opt.sltDomId).find("i").on('click',function(){
            $(this).parent().fadeOut(function(){
                for(var i in m.opt.openDays){
                    if(m.opt.openDays[i]== date){
                        m.opt.openDays.splice(i,1);
                    }
                }
                td.each(function(i){
                    if($(this).attr('data-date') == date){
                        $(this).removeClass('select').removeAttr('data-date');
                    }
                })
                $(this).remove();
            })
        })
    },

    //取消已选择时间
    delDay:function(dom){
        var m = this;
        $(m.opt.sltDomId).children("span").each(function(i){
            if($(this).text() == dom.attr('data-date')){
                $(this).remove();
            }
        });
        dom.removeAttr('data-date');
    },

    //拼接日期
    dateNumInit:function(dom){
        var m = this;
        var year = dom.attr('data-year');
        var month = Number(dom.attr('data-month'))+1;
        month = month > 12 ? 1 : month;
        month =  m.appendZero(month);
        var day = dom.children('a').text();
        day = m.appendZero(day);
        var date = year+'-'+month+'-'+day;
        return date;
    },

    //存储在本地TEMP
    addTempData:function(){
        var m = this;
        m.opt.openDays = [];
        $(m.opt.sltDomId).children("span").each(function(i) {
            m.opt.openDays[i] = $(this).text();
        });
    }
}
