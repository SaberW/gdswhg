/**
 * Created by rbg on 2017/3/24.
 */

function Gridopts(config){
    this.grid = "#whgdg";   //表格选择器
    this.pageType = null;   //页面类型值
    this.pageTypeE = 'listedit';   //编辑页面类型
    this.pageTypeC = 'listcheck';   //审核页面类型
    this.pageTypeP = 'listpublish'; //发布页面类型
    this.pageTypeD = 'listdel'; //回收页面类型
    this.pageTypeS = 'syslistpublish'; //总馆发布页面类型

    this.modeUrlTag = '/view';  //模块URL截取参考
    this.rowIdKey = 'id';   //行对像的ID参考, 单ID不同时可用，大量不同可重写处理 __getGridRow 的返回对像

    if (config && typeof config =='object'){
        this.grid = config.grid || this.grid;
        this.pageType = config.pageType || this.pageType;
        this.pageTypeE = config.pageTypeE || this.pageTypeE;
        this.pageTypeC = config.pageTypeC || this.pageTypeC;
        this.pageTypeP = config.pageTypeP || this.pageTypeP;
        this.pageTypeD = config.pageTypeD || this.pageTypeD;
        this.pageTypeS = config.pageTypeS || this.pageTypeS;

        this.modeUrlTag = config.modeUrlTag || this.modeUrlTag;
        this.rowIdKey = config.rowIdKey || this.rowIdKey;
    }

    this.modeUrl = window.location.href;
    this.modeUrl = this.modeUrl.substring(0, this.modeUrl.indexOf(this.modeUrlTag));
}

Gridopts.prototype = {

    /**
     * 列表状态选项data
     * @param pageType
     * @returns {*}
     */
    getDataState4PageType: function(pageType){
        var _pageType = pageType || this.pageType;
        var stateList = [
            {id: 1, text: '可编辑'},
            {id: 9, text: '待审核'},
            {id: 2, text: '待发布'},
            {id: 4, text: '已下架'},
            {id: 6, text: '已发布'}
        ];
        switch (_pageType){
            case this.pageTypeC:
            case this.pageTypeP:
                return stateList.slice(1); break;
            case this.pageTypeS:
                return stateList.slice(3); break;
            default: return stateList;
        }
    },
    /**
     * 列表状态选项初值
     * @param pageType
     * @returns {*}
     */
    getDefaultState4PageType: function(pageType){
        var _pageType = pageType || this.pageType;
        switch (_pageType){
            case this.pageTypeE: return 1; break;
            case this.pageTypeC: return 9; break;
            case this.pageTypeP: return 2; break;
            case this.pageTypeS: return 6; break;
            default: return '';
        }
    },

    /**
     * 重新加载列表
     */
    reload: function(){
        $(this.grid).datagrid("reload");
    },

    getGridJq: function(){
        return $(this.grid);
    },

    __getGridRow: function(idx){
        return $(this.grid).datagrid("getRows")[idx];
    },

    __getGridSelectRows: function(){
        return $(this.grid).datagrid("getSelections");
    },

    __getGridSelectIds: function(){
        var rows = $(this.grid).datagrid("getSelections");
        if (rows.length){
            var ids = [];
            for(var i in rows){
                ids.push(rows[i][this.rowIdKey]);
            }
            return ids.join();
        }else{
            return '';
        }
    },

    __ajaxError: function(xhr, ts, e){
        $.messager.progress('close');
        $.messager.alert("提示信息", "处理请求发生错误,建议重新登录后操作", 'error');
        $.error("ajax error info : "+ts);
    },

    __ajaxSuccess: function(data){
        $(this.grid).datagrid('reload');
        if (!data.success || data.success != "1"){
            $.messager.alert("错误", data.errormsg||'操作失败', 'error');
        }
        $.messager.progress('close');
    },

    __ajaxTempSend: function (uri, data){
        $.messager.progress();
        var mmx = this;
        $.ajax({
            url: mmx.modeUrl+uri,
            data: data||{},
            type: 'post',
            dataType: 'json',
            success: function(data){ mmx.__ajaxSuccess(data) },
            error: function(xhr, ts, e){ mmx.__ajaxError(xhr, ts, e) }
        });
    },

    view: function(idx){
        var row = this.__getGridRow(idx);
        WhgComm.editDialog( this.modeUrl+'/view/show?id='+row[this.rowIdKey] );
    },

    add: function(){
        WhgComm.editDialog( this.modeUrl+'/view/add');
    },

    edit: function(idx){
        var row = this.__getGridRow(idx);
        WhgComm.editDialog( this.modeUrl+'/view/edit?id='+row[this.rowIdKey] );
    },

    /**
     * 删除
     * @param idx
     */
    del: function(idx){
        var row = this.__getGridRow(idx);
        var confireStr = '确定要删除选中的项吗？';
        if (this.pageType || row.delstate == 1){ //为了老列表处理模式提示正确的处理，列表改完后去除这个if
            confireStr = '此操作将会永久性删除数据！'+ confireStr;
        }
        var mmx = this;
        WhgComm.confirm("确认信息", confireStr, function(r){
            //if (r){
                mmx.__ajaxTempSend("/del", {id: row[mmx.rowIdKey],delstate:row.delstate});
            //}
        })
    },

    /**
     * 回收
     */
    recovery: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", '确定要回收选中的项吗？', function(r){
            //if (r){
                mmx.__ajaxTempSend("/recovery", {id: row[mmx.rowIdKey]});
            //}
        })
    },

    /**
     * 还原删除
     * @param idx
     */
    undel: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定要还原选中的项吗？", function(r){
            //if (r){
                mmx.__ajaxTempSend("/undel", {id: row[mmx.rowIdKey]});
            //}
        })
    },

    __updateStateSend: function (ids, formstates, tostate, optTime) {
        var mmx = this;
        var params = {ids: ids, formstates: formstates, tostate: tostate, optTime: optTime};
        mmx.__ajaxTempSend("/updstate", params);
    },

    /**
     * 发布 [2,4]->6
     * @param idx
     */
    publish: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;

        var optDialog =$('<div></div>').dialog({
            title: '设置发布时间时段',
            width: 400,
            height: 200,
            cache: false,
            modal: true,
            content: '<div class="dialog-content-add" style="padding: 30px 30px;"></div>',
            onOpen: function(){
                var content = $(this).find('.dialog-content-add');
                content.append('发布时间：<input class= "easyui-datetimebox" data-options="width:200,required:true"/>');
                $.parser.parse(content);
                content.find(".easyui-datetimebox:eq(0)").datetimebox('setValue', new Date().Format("yyyy-MM-dd hh:mm:ss"));
            },
            buttons: [{
                text:'确认',
                iconCls: 'icon-ok',
                handler:function(){
                    $.messager.progress();
                    var valid = optDialog.find(".easyui-datetimebox:eq(0)").datetimebox('isValid');
                    if (!valid) {
                        $.messager.progress('close');
                        return;
                    }
                    var optTime = optDialog.find(".easyui-datetimebox:eq(0)").datetimebox('getValue');
                    mmx.__updateStateSend(row[mmx.rowIdKey], "2,4", 6, optTime);
                    optDialog.dialog('close');
                    optDialog.remove();
                }
            },{
                text:'取消',
                iconCls: 'icon-no',
                handler:function(){ optDialog.dialog('close');optDialog.remove();}
            }]
        });

    },

    /**
     * 取消发布 [6]->4
     * @param idx
     */
    publishoff: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定撤销发布选中的项吗？", function(r){
            //if (r){
                mmx.__updateStateSend(row[mmx.rowIdKey], 6, 4);
            //}
        })
    },

    /**
     * 区域管理员取消发布 [6]->4
     */
    sysPublishoff: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;

        var optDialog =$('<div></div>').dialog({
            title: '撤销原因说明',
            width: 450,
            height: 300,
            cache: false,
            modal: true,
            content: '<div class="dialog-content-info" style="padding: 30px 30px;"></div>',
            onOpen: function(){
                var content = $(this).find('.dialog-content-info');

                content.append('撤销原因：<input class= "easyui-textbox" data-options="width:300, height:120,required:true,validType:\'length[1,100]\',multiline:true" />');
                content.append('<div class="radio-group" style="width:230px; margin: 10px auto 0px" >\
                     <input type="radio" name="issms" value="1" checked="checked"><label>发送短信</label>\
                     <input type="radio" name="issms" value="0"><label>不发送短信</label>\
                    </div>');

                $.parser.parse(content);
                content.on("click", ".radio-group label", function(){
                    $(this).prev("input").click();
                })
            },
            buttons: [{
                text:'确认',
                iconCls: 'icon-ok',
                handler:function(){
                    $.messager.progress();
                    var textbox = optDialog.find(".easyui-textbox:eq(0)");
                    var valid = textbox.textbox('isValid');
                    if (!valid) {
                        $.messager.progress('close');
                        return;
                    }
                    var reason = textbox.textbox('getValue');
                    var issms = optDialog.find("[name='issms']:radio:checked").val();

                    var params = {ids: row[mmx.rowIdKey], formstates: 6, tostate: 4, reason: reason, issms: issms};

                    mmx.__ajaxTempSend("/updstate", params);

                    optDialog.dialog('close');
                    optDialog.remove();
                }
            },{
                text:'取消',
                iconCls: 'icon-no',
                handler:function(){ optDialog.dialog('close');optDialog.remove();}
            }]
        });
    },

    /**
     * 送审 [1]->9
     * @param idx
     */
    checkgo: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定提交审核选中的项吗？", function(r){
            //if (r){
                mmx.__updateStateSend(row[mmx.rowIdKey], "1,5", 9);
            //}
        })
    },

    /**
     * 审通过 [9]->2
     * @param idx
     */
    checkon: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定审核通过选中的项吗？", function(r){
            //if (r){
                mmx.__updateStateSend(row[mmx.rowIdKey], 9, 2);
            //}
        })
    },

    /**
     * 审不通过 [9]->1
     * @param idx
     */
    checkoff: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定审核不通过选中的项吗？", function(r){
            //if (r){
                mmx.__updateStateSend(row[mmx.rowIdKey], 9, 1);
            //}
        })
    },

    /**
     * 打回到编辑 [9,2]->1
     * @param idx
     */
    back: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定打回编辑选中的项吗？", function(r){
            //if (r){
                mmx.__updateStateSend(row[mmx.rowIdKey], "9,2", 1);
            //}
        })
    },

    /**
     * 推荐
     */
    recommend: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定设置推荐选中的项吗？", function(r){
            //if (r){
                mmx.__ajaxTempSend("/recommend", {id: row[mmx.rowIdKey], recommend:1});
            //}
        })
    },

    /**
     * 取消推荐
     * @param idx
     */
    recommendoff: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定取消推荐选中的项吗？", function(r){
            //if (r){
                mmx.__ajaxTempSend("/recommend", {id: row[mmx.rowIdKey], recommend:0});
            //}
        })
    },

    /**
     * 省级推荐
     * @param idx
     */
    toprovince: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定选中的项设置省级推荐吗？", function(r){
            mmx.__ajaxTempSend("/setToprovince", {id: row[mmx.rowIdKey], toprovince:1});
        })
    },
    /**
     * 取消省级推荐
     * @param idx
     */
    untoprovince: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定选中的项取消省级推荐吗？", function(r){
            mmx.__ajaxTempSend("/setToprovince", {id: row[mmx.rowIdKey], toprovince:0});
        })
    },
    /**
     * 市级推荐
     * @param idx
     */
    tocity: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定选中的项设置市级推荐吗？", function(r){
            mmx.__ajaxTempSend("/setTocity", {id: row[mmx.rowIdKey], tocity:1});
        })
    },
    /**
     * 取消市级推荐
     * @param idx
     */
    untocity: function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定选中的项取消市级推荐吗？", function(r){
            mmx.__ajaxTempSend("/setTocity", {id: row[mmx.rowIdKey], tocity:0});
        })
    },


    recommendVfun: function(idx){
        var row = this.__getGridRow(idx);
        return (row.state == 6 && row.delstate == 0 && (!row.recommend || row.recommend==0 ));
    },
    recommendOffVfun: function(idx){
        var row = this.__getGridRow(idx);
        return (row.state == 6 && row.delstate == 0 && (row.recommend && row.recommend==1 ));
    },


    /**
     * 验证操作项界面相关状态
     * @param idx
     * @returns {boolean}
     */
    optValid4PageTypeState: function(idx){
        var row = this.__getGridRow(idx);
        var _pageType = this.pageType;
        var states = [];
        switch (_pageType){
            case this.pageTypeE: states = [1]; break;
            case this.pageTypeC: states = [9]; break;
            case this.pageTypeP: states = [2,4,6]; break;
        }
        return $.inArray(row.state, states) != -1;
    },
    /**
     * 验证操作项编辑相关状态
     * @param idx
     * @returns {boolean}
     */
    optValid4EditState: function(idx){
        var row = this.__getGridRow(idx);
        /*var _pageType = this.pageType;
        var states = [1,9,2,4,6];
        switch (_pageType){
            case this.pageTypeE: states = [1]; break;
            case this.pageTypeC: states = [9]; break;
            case this.pageTypeP: states = [2,4]; break;
        }
        return $.inArray(row.state, states) != -1;*/
        return row.state!=6 && this.optValid4PageTypeState(idx);
    },
    /**
     * 验证操作项回收相关状态
     * @param idx
     * @returns {boolean}
     */
    optValid4RecoveryState: function(idx){
        var row = this.__getGridRow(idx);
        var _pageType = this.pageType;
        var states = [];
        switch (_pageType){
            case this.pageTypeE: states = [1]; break;
            case this.pageTypeC: states = [9]; break;
            case this.pageTypeP: states = [2,4]; break;
        }
        return $.inArray(row.state, states) != -1;
    },
    /**
     * 验证操作项删除
     * @param idx
     */
    optValid4Del: function(idx){
        var row = this.__getGridRow(idx);
        var _pageType = this.pageType;
        switch (_pageType){
            case this.pageTypeE: return row.state == 1; break;
            case this.pageTypeD: return row.delstate == 1; break;
            default: return false;
        }
    }

};
