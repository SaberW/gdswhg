/* Created by JackZeng on 2017/11/04 */

var cont = {
    option: {
        row:2,
        panelCls:""
    },
    row:1,
    init:function(options){
        var m = this;
        var opt = jQuery.extend({}, m.option, options || {});
        var panels = opt.panelCls.split(',');
        for(var i in panels){
            var panel = jQuery(panels[i]);
            var temp_height = panel.attr("data-height") || 20;
            panel.prepend("<div class='newbility-arrow down none' onclick='cont.open(this)' data-flag='1'><i class='iconfont icon-unfold'></i></div>")
            temp_height = Number(temp_height);
            panel.css({
                position:'relative',
                lineHeight: temp_height+'px'
            })
            var row = opt.row;
            m.row = row;
            var max_height = temp_height*row;
            if(panel.height() > max_height){
                panel.height(temp_height+temp_height*row + 10);
                panel.css({
                    overflow:'hidden'
                })
                panel.children(".newbility-arrow").show();
            }
        }
    },
    open:function(dom){
        var m = this;
        var dom = jQuery(dom);
        var flag = jQuery(dom).attr("data-flag");
        if(flag == 1){
            jQuery(dom).attr("data-flag",0);
            dom.parent().css({
                height:"auto"
            });
            var _temp_h = dom.parent().height() + 40;
            dom.parent().height(_temp_h);
            dom.children("i").removeClass("icon-unfold").addClass("icon-packup");
        }else{
            jQuery(dom).attr("data-flag",1   );
            var temp_height = dom.parent().attr("data-height") || 20;
            var row = m.row;
            temp_height = Number(temp_height);
            dom.parent().height(temp_height + temp_height*row + 10);
            dom.children("i").removeClass("icon-packup").addClass("icon-unfold");
        }

    }
}
