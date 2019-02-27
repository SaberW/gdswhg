/**
 * Created by rbg on 2017/4/6.
 */


function RoomOrderTool(config){
    this.settings = jQuery.extend(true, {}, config ||{});
    this.weekDiv= '.orderPayCont';
    this.trDome = '<tr>\
        <td class="choose"></td>\
        <td class="choose"></td>\
        <td class="choose"></td>\
        <td class="choose"></td>\
    </tr>';
}

RoomOrderTool.prototype = {
    
    initWeekDay: function (day) {
        //week Ul
        this.weekUl = jQuery(this.weekDiv).find('.week-groups ul');
        //table
        this.timesTable = jQuery(this.weekDiv).find('.tableListCont table');
        //按日期取7天内的预定信息数据，用以输出预定操作表格
        this.loadWeekRoomTimes();
        this.setItemEvent();
        this.setButtonEvent();
    },

    setButtonEvent: function(){
        if (this.__setButtonEvent) return;
        this.__setButtonEvent = true;
        var that = this;
        mui('.mui-tab-item').on('tap','.goNext',function(){
            that.buttonSubmit();
        });
    },

    buttonSubmit: function(){
        if (!this.settings.userId || this.settings.userId == ''){
            mui.toast('请登录');
            return;
        }

        if (!this.selectValue){
            mui.toast("请选择要预订的时间");
            window.location.href='#roomTime';
            return;
        }

        var that = this;
        dataInit.ajax({
            api: that.settings.orderCheckUrl,
            params: {roomTimeId: that.selectValue.id, userId: that.settings.userId},
            fn: function(data){
                if (data.code == 0) {
                    var params = {
                        roomtimeid: that.selectValue.id,
                        timeday: that.selectValue.timeday,
                        timestart: that.selectValue.timestart + '-' + that.selectValue.timeend,
                        roomId: that.settings.roomId
                    }
                    dataInit.setCache("venue-params", params);
                    window.location.href = that.settings.basePathHeader + '/venue/step'
                }else if (data.code==200){
                    mui.confirm(data.msg, '友情提示',['确定','取消'], function(e){
                        if (e.index==0){
                            window.location.href = that.settings.basePathHeader + '/usercenter/myusersecurityreal'
                        }
                    });
                } else {
                    mui.toast(data.msg || '正在操作此功能的用户众多，请稍后尝试');
                    return;
                }
            }
        });
    },

    setItemEvent: function(){
        if (this.__setItemEvent) return;
        this.__setItemEvent = true;

        var that = this;
        //this.timesTable.on("click", "td.choose:not(.gray) a", function(){
        this.timesTable.on("tap", "td.choose:not(.gray) a", function(){
            var td = jQuery(this).parents("td");
            var data = td.data('jsData.roomTime');
            that.selectValue = data;
            $("#roomTimeValue").val(data.id);//记录 选择roomid
            that.viewselectItem();
        });
    },

    viewselectItem: function(){
        this.timesTable.find("td.choosed:not(.gray) span").each(function(){
            var td = jQuery(this).parents("td");
            td.removeClass().addClass('choose').html('<a href="javascript:void(0)">'+jQuery(this).text()+'</a>');
        });
        if (!this.selectValue) return;
        var that = this;
        this.timesTable.find("td.choose:not(.gray) a").each(function(){
            var td = jQuery(this).parents("td");
            var data = td.data('jsData.roomTime');
            if (data.id == that.selectValue.id){
                td.removeClass().addClass('choosed').html('<i class="iconfont icon-gou"></i><span>'+jQuery(this).text()+'</span>');
            }
        });
    },
    
    loadWeekRoomTimes: function () {
        var bday = this.getWeekDayBegin();
        var eday = this.getWeekDayEnd();

        var that = this;
        dataInit.ajax({
            api: that.settings.basePath,
            async: false,
            fn: function (data) {
                that.nowDate = new Date(data.nowDate);

                that.viewRoomTimes(data);

                that.viewselectItem();
            },
            params: {startDay: bday, endDay: eday, roomid: that.settings.roomId,userId:that.settings.userId},
        });
    },

    viewRoomTimes: function(data){
        var tbody = this.timesTable.find("tbody");
        tbody.empty();
        //预订时段对象
        var viewTimes = this.parseTimes2view(data.data.timeList);
        //成功的预订
        var roomOrderOK = this.parseOrders2view(data.data.successList);
        //当前用户的申请
        var roomOrderUser = null;
        if (data.data.applyList){
            roomOrderUser = this.parseOrders2view(data.data.applyList);
        }

        //装入行列框架
        for(var i=0; i<viewTimes.maxDayTimesNum; i++){
            tbody.append(this.trDome);
        }

        //装入初始的预定时段数据
        var that = this;
        this.weekUl.find("li").each(function(i){
            //对应星期的日期
            var weekday = that.getWeekDay(i);
            //对应的数据
            var daytimes = viewTimes[weekday];
            //对应的 列 td
            var dayTds = that.timesTable.find("tr").find("td:eq("+i+")");
            //没有数据时
            if (!daytimes){
                dayTds.removeClass("choose").addClass("choosed").text('-');
                return true;
            }
            //处理数据显示
            dayTds.each(function (i) {
                //空的
                if (i>=daytimes.length || daytimes[i].state==0){
                    jQuery(this).removeClass("choose").addClass("choosed").text('-');
                    return true;
                }

                var showText = daytimes[i].timestart+'-'+daytimes[i].timeend;

                //小于当前时间的变灰
                var anow = that.isGtNowDate(daytimes[i]);
                if (!anow){
                    jQuery(this).removeClass("choose").addClass("choosed gray").html(showText);
                    return true;
                }

                var key = daytimes[i].timeday+''+daytimes[i].timestart+''+daytimes[i].timeend;
                key = key.replace(/:/g, '-');
                //成功的预订变灰
                if (roomOrderOK && roomOrderOK[key]){
                    jQuery(this).removeClass("choose").addClass("choosed gray").html(showText);
                    return true;
                }
                //当前用户订的变灰打标记
                if (roomOrderUser && roomOrderUser[key]){
                    jQuery(this).removeClass('choose').addClass('choosed  gray').html('<i></i><span>'+showText+'</span>');
                    return true;
                }

                //可以操作的
                jQuery(this).html('<a href="javascript:void(0)">'+showText+'</a>');
                jQuery(this).data('jsData.roomTime', daytimes[i]);
            })
        })

    },

    parseTimes2view : function(data){
        var times = {};
        times.maxDayTimesNum = 0;
        for(var i in data) {
            var row = data[i];
            if (row.state == 0) continue;
            var timeday = moment(row.timeday).format('YYYY-MM-DD');
            var timestart = moment(row.timestart).format('HH:mm');
            var timeend = moment(row.timeend).format('HH:mm');
            times[timeday] = times[timeday] ? times[timeday] : [];
            times[timeday].push({
                id: row.id,
                roomid: row.roomid,
                state: row.state,
                timeday: timeday,
                timestart: timestart,
                timeend: timeend
            });

            times.maxDayTimesNum = times.maxDayTimesNum < times[timeday].length ? times[timeday].length : times.maxDayTimesNum;
        }
        return times;
    },

    parseOrders2view: function (data) {
        var times = {};
        for(var i in data){
            var row = data[i];

            var timeday = moment(row.timeday).format('YYYY-MM-DD');
            var timestart = moment(row.timestart).format('HH:mm');
            var timeend = moment(row.timeend).format('HH:mm');

            var key = timeday+''+timestart+''+timeend;
            key = key.replace(/:/g, '-');
            times[key] = {
                roomid: row.roomid,
                state: row.state,
                timeday: timeday,
                timestart: timestart,
                timeend: timeend
            };
        }
        return times;
    },


    isGtNowDate: function(item){
        var now = new Date();
        var stime = this.myParseDay(item.timeday, item.timestart);
        return stime.getTime() > now.getTime();
    },

    getWeekDayBegin: function(){
        var daystr = this.weekUl.find("li:first p").text();
        var day = this.myParseDay(daystr);
        return moment(day).format('YYYY-MM-DD');
    },
    getWeekDayEnd: function(){
        var daystr = this.weekUl.find("li:last p").text();
        var day = this.myParseDay(daystr);
        return moment(day).format('YYYY-MM-DD');
    },
    getWeekDay: function (idx) {
        var daystr = this.weekUl.find("li:eq("+idx+") p").text();
        var day = this.myParseDay(daystr);
        return moment(day).format('YYYY-MM-DD');
    },

    myParseDay: function(daystr, timestr){
        var _day = new Date();
        var dstrs = /(\d{4})-(\d{1,2})-(\d{1,2})/.exec(daystr);
        if (dstrs){
            _day.setFullYear( dstrs[1],parseInt(dstrs[2])-1,dstrs[3] );
        }
        if (timestr){
            var timestrs = /(\d{2}):(\d{2})/.exec(timestr);
            if (timestrs){
                _day.setHours(timestrs[1]);
                _day.setMinutes(timestrs[2]);
                _day.setSeconds(0);
            }
        }else{
            _day.setHours(0);
            _day.setMinutes(0);
            _day.setSeconds(0);
        }
        return _day;
    }


};
