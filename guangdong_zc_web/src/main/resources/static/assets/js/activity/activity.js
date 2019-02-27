/**
 * Created by JackZeng on 2017/06/09.
 */
var dateInit = {
    option:{
        api:"http://localhost:8080",
    },
    init:function(options){
        var m  = this;
        m.opt = $.extend({}, m.option, options || {});
        m.ajax();
    },
    ajax:function(){
        var m = this;
        $.ajax({
            url: m.opt.api,
            type: 'POST',
            dataType: 'json',
            timeout: 1000,
            cache: false,
            beforeSend: m.LoadFunction,
            error: m.erryFunction,
            success: function (data) {
                that.succFunction(data)
            }
        })
    },
    LoadFunction:function(){
        rongDialog.init({
            ico : 3, //1 勾  2 叉  3Loading
            type : 1,
            desc : '数据加载中'
        });
    },
    erryFunction:function(){
        rongDialog.init({
            ico : 3, //1 勾  2 叉  3Loading
            type : 1,
            desc : '数据加载失败'
        });
    },
    success:function(){

    }

}