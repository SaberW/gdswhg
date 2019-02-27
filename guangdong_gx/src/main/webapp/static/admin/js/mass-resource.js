/**
 * Created by wangxl on 2017/12/4.
 */
WhgMassResource = (function () {
    //打开资源库的初始参数
    var this_option = {};

    function _openResource(aOption) {
        //保存初始参数
        this_option = $.extend({}, {
            cultid: '', //管理员权限文化馆
            deptid: '', //管理员权限部门
            restype: '', //限制的资源类型, 如：img表示只能从资源库中选择图片类资源类型
            submitFn: false //选择资源后的回调函数, 方法参数: {libid:'资源库标识', resid:'资源标识', restype:'资源类型', resname:'资源名称', respicture:'资源封面图片地址', resurl:'resurl'}
        }, aOption);

        //打开弹出窗口
        ___openDialog(basePath+'/admin/mass/libres/view/list_choice?state=6&restype='+this_option.restype+'&cultid='+this_option.cultid+'&deptid='+this_option.deptid, '选择群文库资源', 800, 500);
    }

    /**
     * 弹出Dialog-iframe， 此方法依赖easyui-dialog
     * @param url 页面
     * @param title 标题
     * @param width dialog宽
     * @param height dialog高
     * @private
     */
    function ___openDialog(url, title, width, height){
        //弹出窗口的唯一标识
        var id_dialog = 'whg-dialog-resource-1001';
        window.__WHG_DIALOG_RESOURCE_ID = id_dialog;

        //銷毀之前的
        if( $('#'+id_dialog).size() > 0 ){
            $('#Btn_'+id_dialog).remove();
            $('#'+id_dialog).dialog('destroy');
            $('#'+id_dialog).remove();
        }

        //设置宽高，根据屏幕设置最佳尺寸
        width = width || 800;
        height = height || 550;
        var _height = $(window).height() < height ? $(window).height() -50 : height;
        var _width = $(window).width() < width ? $(window).width() -50 : width;

        //Dialog HTML
        var _iframe = '<iframe id="iframe_'+id_dialog+'" frameborder="0" src="' + url+ '" style="width:100%;height:100%;overflow: hidden;"></iframe>';
        var dialogHTML = '\
            <div id="'+id_dialog+'" style="overflow:hidden"></div>\
            <div id="Btn_'+id_dialog+'" style="text-align: center; display: none">\
                <div style="display: inline-block; margin: 0 auto">\
                    <a id="'+id_dialog+'_ok" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:\'icon-save\'">确  定</a>\
                    <a id="'+id_dialog+'_no" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:\'icon-cancel\'">取  消</a>\
                </div>\
            </div>\
        ';
        $(dialogHTML).appendTo($("body"));
        $.parser.parse('#Btn_'+id_dialog);

        $('#'+id_dialog).dialog({
            title: title,
            closable: true,
            width: ''+_width,
            height: ''+_height,
            border: false,
            modal: true,
            content: _iframe,
            buttons: '#Btn_'+id_dialog,
            onClose: function () {
                delete window.__WHG_DIALOG_RESOURCE_ID;
            }
        });
        $('#'+id_dialog+'_no').off('click').one("click", function () {
            $('#'+id_dialog).dialog('close');
        });
    }

    /**
     * 获得最新弹出Dialog的提交按钮的ID
     * @returns {*|jQuery|HTMLElement}
     * @private
     */
    function ___getDialogSubmitBtn(){
        return $('#'+window.__WHG_DIALOG_RESOURCE_ID+'_ok');
    }

    /**
     * 关闭Dialog
     * @private
     */
    function ___closeDialog(){
        $('#'+window.__WHG_DIALOG_RESOURCE_ID).dialog('close');
    }

    /**
     * 选择资源后的回调函数
     * @private
     */
    function _doSubmit(option){
        if(this_option.submitFn && $.isFunction(this_option.submitFn)){
            this_option.submitFn(option);
            ___closeDialog();
        }
    }

    return {
        /**
         * 弹出选择资源窗口
         * @param options.cultid 管理员权限文化馆，可以为空
         * @param options.deptid 管理员权限部门，可以为空
         * @param options.restype 限制的资源类型，可以为空, 如：img表示只能从资源库中选择图片类资源类型
         * @param options.submitFn 选择资源后的回调函数, 方法参数: {libid:'资源库标识', resid:'资源标识', restype:'资源类型', resname:'资源名称', respicture:'资源封面图片地址', resurl:'resurl'}
         */
        openResource: function (_option) {
            _openResource(_option);
        }

        /**
         * 关闭选择资源窗口
         */
        ,closeDialog: function(){
            ___closeDialog();
        }

        /**
         * 获取资源窗口提交按钮对象
         */
        ,getSubmitBtn: function () {
            return ___getDialogSubmitBtn();
        }

        /**
         * 选择资源后的回调函数
         * @param option
         */
        ,doSubmit: function (option) {
            _doSubmit(option);
        }
    }
})();