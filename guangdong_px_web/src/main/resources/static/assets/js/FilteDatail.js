

var FilteDatail = {
    options: function(option){
        if (this.filteEnd) {
            return this;
        }
        var defOption = {
            info : {},   //详情页数据信息
            toUrl : '', //拦fwy
            state : 'state',//发布状态key
            stateVal: 6, //发布状态有效值
            delstate : 'delstate',//删除状态key
            delstataVal: 0, //删除状态有效值
            filteMsg : '信息已不存在'
        };

        this.option = $.extend({}, defOption, option || {});
        return this;
    },

    filte: function(){
        if (this.filteEnd) {
            return this;
        }

        var me = this;
        var options = me.option;

        var goUrl = options.toUrl;
        if (!goUrl || goUrl == ''){
            var loc = window.location;
            goUrl = loc.href;
            goUrl = goUrl.substring(0, goUrl.indexOf(loc.pathname));
        }

        if (!options.info || options.info[options.state] != options.stateVal || options.info[options.delstate] != options.delstataVal){
            rongDialog.init({
                ico : 2, //1 勾  2 叉
                type : 1,
                desc : options.filteMsg,
                nextFn : function(){
                    window.location.href = goUrl;
                }
            });
            me.filteEnd = true;
        }

        return this;
    }
};