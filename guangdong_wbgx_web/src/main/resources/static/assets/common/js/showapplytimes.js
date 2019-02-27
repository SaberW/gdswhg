
var ShowApplyTimes = {

    defaultOptions: function(){
        return {
            apiUrl: '',
            supplyId: '',
            supplyType: '',
            pageSize: 10,
            timesEleid: 'timeList',
            paggingEleid: 'artPagging',
            basePath:'',
            userId:''
        }
    },

    init: function(options){
        this.options = $.extend({}, this.defaultOptions(), options||{});

        this.paggingEle = $("#"+this.options.paggingEleid);
        this.timesEle = $("#"+this.options.timesEleid);

        this.showTimeList();
    },

    showTimeList: function(page, rows){
        var self = this;
        var params = {
            page: page || 1,
            pageSize: rows || self.options.pageSize,
            id: self.options.supplyId
        };

        dataInit.ajax({
            api: self.options.apiUrl,
            fn: function (data) {
                self.timeList(data, params.page, params.pageSize);
                dataInit.pageInit(self.options.paggingEleid, params.page, params.pageSize, data.total, function(p, r){self.showTimeList(p, r)});
            },
            params: params
        });
    },

    timeList: function(data, page, pageSize){
        var self = this;

        var _timeList = data.rows || [];
        if(!_timeList.length || data.total<= pageSize){
            self.paggingEle.hide();
        }
        self.timesEle.empty();

        if (!data || data.code !=0){
            rongDialog.init({
                ico : 2, //1 勾  2 叉
                type : 1,
                desc : data.msg || '请求配送时间信息失败'
            });
            return;
        }

        var TimeListHtml = "";
        TimeListHtml += '<tr>' +
            '<th style="width: 30px;">序号</th>' +
            '<th style="width: 140px;">配送开始时间</th>' +
            '<th style="width: 140px;">配送结束时间</th>' +
            '<th style="width: 60px;">是否收费</th>' +
            '<th >配送范围</th>' +
            '<th style="width: 70px;"></th>' +
            '</tr>';

        var isCurrOpt = (self.options.userId && self.options.crtuser && self.options.userId == self.options.crtuser);
        for (var i in _timeList) {
            var activeTime = moment(_timeList[i].timestart).format('YYYY-MM-DD HH:mm:ss');
            var endTime = moment(_timeList[i].timeend).format('YYYY-MM-DD HH:mm:ss');
            var hasfees = (_timeList[i].hasfees && _timeList[i].hasfees ==1)? '是' : '否';

            var optTxt = '';
            var isEnd = (data.data.now > _timeList[i].timeend) ? true : false;
            if (!isEnd){
                if (!isCurrOpt){
                    var _href = self.options.basePath+'/res-center/stepOpenDay';
                    _href += '?id='+self.options.supplyId+'&timeid='+_timeList[i].id;
                    _href += '&fktype='+self.options.fav_type ||'';
                    _href += '&crtuser='+self.options.crtuser ||'';
                    _href += '&title='+encodeURI(encodeURI(self.options.title));
                    optTxt = '<a href="'+_href+'">申请配送</a>';
                }
            }else {
                optTxt = '配送过期';
            }

            var j = parseInt(i) + 1 + (page-1)*pageSize;
            TimeListHtml += '<tr>' +
                '<td class="not-conform">' + j + '</td>' +
                '<td class="not-conform">' + activeTime + '</td>' +
                '<td class="not-conform">' + endTime + '</td>' +
                '<td class="not-conform">' + hasfees + '</td>' +
                '<td class="not-conform">' + _timeList[i].pscity + '</td>' +
                '<td class="not-conform">'+ optTxt +'</td>' +
                '</tr>';
        }
        self.timesEle.html(TimeListHtml);
    }
};