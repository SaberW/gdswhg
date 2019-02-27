/**
 * Created by JackZeng on 2017/11/15.
 */
var fixed = {
    option : {
        topHeight:300,
        panelCls:"#model-list"
    },
    init : function(options){
        var m = this;
        var opt = $.extend({}, m.option, options || {});
        m.resize(opt);

    },
    resize : function(opt){
        var m = this;
        $(window).scroll(function(){
            var topScroll = $(document).scrollTop();
            var panel = $(opt.panelCls);
            if(topScroll > opt.topHeight){
                panel.css({
                    paddingTop:46
                })
                $("#newbility-nav-fixed").css({
                    position:"fixed",
                    left:"0",
                    top:"0",
                    width:"100%",
                    zIndex:10
                })
            }else{
                panel.css({
                    paddingTop:0
                })
                $("#newbility-nav-fixed").css({
                    position:'static'
                })
            }
        })
    }
}
