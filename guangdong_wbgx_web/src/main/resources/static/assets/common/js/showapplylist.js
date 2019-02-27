/**
 * 显示配送成功信息
 * Created by rbg on 2017/11/22.
 */

;var ShowApplyList = {

    targetDiv: ".left-cont .item-cont",

    init: function(url, id){
        this.id = id;
        this.url = url;


        this.tarJQ = $(this.targetDiv);
        this.tarJQ.append('<div class="sys-detail-list ShowApplyList-item">\
                            <div class="title">供给信息内容</div>\
                            <div class="html-panel">\
                                <div class="dg-train-info dg-train-table" style="margin-bottom: 20px">\
                                    <table>\
                                        <tbody>\
                                        </tbody>\
                                    </table>\
                                </div>\
                                <!--分页开始-->\
                                <div class="green-black" id="ShowApplyList_Pagging" style="margin-bottom: 40px"></div>\
                                <!--分页结束-->\
                            </div>\
                        </div>');


        this.initRun();
    },

    initRun: function(page, pageSize){
        var me = this;

        me.params =  me.params|| { id: me.id };
        me.params.page = page || 1;
        me.params.pageSize = pageSize || 10;

        $.post(me.url, me.params, function (data) {
            if (!data.rows || !data.rows.length){
                me.tarJQ.find(".ShowApplyList-item").hide();
                return;
            }
            me.tarJQ.find(".ShowApplyList-item").show();

            var _tbody = me.tarJQ.find(".ShowApplyList-item tbody");
            for(var i in data.rows){
                var row = data.rows[i];

                //var starttime = moment(row.statemdfdate).format('YYYY-MM-DD');
                //var endtime = moment(row.endtime).format('YYYY-MM-DD');
                var index = Number(i)+1;
                var item = '<tr>' +
                    '<td class="not-conform">' + index + '</td>' +
                    '<td class="not-conform">' + row.cultname + '</td>' +
                    //'<td class="not-conform">' + row.crtusername + '</td>' +
                    '<td class="not-conform">' + row.deliverytime + '</td>' +
                    '</tr>';

                _tbody.append(item);
            }

            dataInit.pageInit('ShowApplyList_Pagging', me.params.page, me.params.pageSize, data.total, function(page, pageSize){
                me.initRun(page, pageSize);
            });
        }, "JSON");
    }

};