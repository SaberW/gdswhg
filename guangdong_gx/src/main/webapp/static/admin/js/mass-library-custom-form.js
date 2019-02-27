/**
 * 自定义表单
 * Created by wangxl on 2017/11/2.
 */
WhgCustomForm = (function () {
    //下拉列表数据
    var __comboboxData__ = {};
    var __ID = 1001;

    /**
     * 生成单页面不同的序号
     * @returns {number}
     * @private
     */
    function _getId(){
        return __ID++;
    }

    /**
     * 弹出Dialog-iframe， 此方法依赖easyui-dialog
     * @param url 页面
     * @param title 标题
     * @param width dialog宽
     * @param height dialog高
     * @private
     */
    function ___openDialog(url, title, width, height, closeFn){
        var id_dialog = 'whg-dialog-'+WhgCustomForm.getID();
        window.__WHG_CUSTOM_FORM_DIALOG_ID = id_dialog;
        if( $('#'+id_dialog).size() > 0 ){
            $('#Btn_'+id_dialog).remove();
            $('#'+id_dialog).dialog('destroy');
            $('#'+id_dialog).remove();
        }
        width = width || 800;
        height = height || 550;
        var _height = $(window).height() < height ? $(window).height() -50 : height;
        var _width = $(window).width() < width ? $(window).width() -50 : width;

        var _iframe = '<iframe id="iframe_'+id_dialog+'" frameborder="0" src="' + url+ '" style="width:100%;height:100%;overflow: hidden;"></iframe>';
        var dialogHTML = '\
            <div id="'+id_dialog+'" style="overflow:hidden"></div>\
            <div id="Btn_'+id_dialog+'" style="text-align: center; display: none">\
                <div style="display: inline-block; margin: 0 auto">\
                    <button type="button" class="btn btn-primary" id="'+id_dialog+'_ok"><span class="glyphicon glyphicon-ok"></span>&nbsp;确  认</button>\
                    <button type="button" class="btn btn-default" id="'+id_dialog+'_no"><span class="glyphicon glyphicon-remove"></span>&nbsp;取  消</button>\
                </div>\
            </div>\
        ';
        $(dialogHTML).appendTo($("body"));
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
                delete window.__WHG_CUSTOM_FORM_DIALOG_ID;
                if($.isFunction(closeFn)){
                    closeFn();
                }
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
        return $('#'+window.__WHG_CUSTOM_FORM_DIALOG_ID+'_ok');
    }

    /**
     * 关闭Dialog
     * @private
     */
    function ___closeDialog(){
        $('#'+window.__WHG_CUSTOM_FORM_DIALOG_ID).dialog('close');
    }

    /**
     *
     * @param containerid
     * @private
     */
    function __createRowOneColumn(containerid) {
        var libid = $('#libid').val();
        ___openDialog(basePath+'/admin/mass/library/view/column_type?libid='+libid+'&column=1&formid='+containerid, '单行一列配置', 800, 350);
    }

    /**
     *
     * @param containerid
     * @private
     */
    function __createRowTwoColumn(containerid) {
        var libid = $('#libid').val();
        ___openDialog(basePath+'/admin/mass/library/view/column_type?libid='+libid+'&column=2&formid='+containerid, '单行两列配置', 800, 500);
    }

    /**
     * 一列单字段的HTML
     * @param rowTotalColumns 表单中一行有几列
     * @param labelText label值
     * @param columnid 表单行列在数据库中的值
     * @param noedit 是否需要编辑按钮
     * @returns {string|*}
     * @private
     */
    function ___createColumnOneFieldHTML(rowTotalColumns, labelText, columnid, noedit){
        var col_sm_size = rowTotalColumns == 2 ? 'col-sm-4' : 'col-sm-10';//只支持两列或者一列
        var display = noedit ? 'style="display:none;"' : '';
        //<button type="button" class="btn btn-danger btn-xs field-del"><span class="glyphicon glyphicon-remove"></span></button>
        return '\
            <label class="col-sm-2 control-label" id="labelEle_'+columnid+'">'+labelText+'：</label>\
            <div class="'+col_sm_size+'" id="inputEle_'+columnid+'">\
                <div class="opreation" '+display+'>\
                    <button type="button" class="btn btn-success btn-xs field-edit" columnid="'+columnid+'"><span class="glyphicon glyphicon-pencil"></span></button>\
                </div>\
            </div>\
        ';
    }

    /**
     * 创建组合列
     * @param rowTotalColumns 表单中一行有几列
     * @param labelText label值
     * @param columnid 表单行列在数据库中的值
     * @param noedit 是否需要编辑按钮
     * @param size 组合列有多少个字段
     * @returns {string|*}
     * @private
     */
    function ___createColumnGroupFieldsHTML(rowTotalColumns, labelText, columnid, size, noedit){
        var col_sm_size = rowTotalColumns == 2 ? 'col-sm-4' : 'col-sm-10';
        var display = noedit ? 'style="display:none;"' : '';
        var tds = '';
        for(var i=0; i<size; i++){
            tds += '\
                <td id="inputEle_'+i+'_'+columnid+'">\
                    <div class="opreation" '+display+'>\
                        <button type="button" class="btn btn-success btn-xs field-edit" columnid="'+columnid+'" idx="'+i+'"><span class="glyphicon glyphicon-pencil"></span></button>\
                    </div>\
                </td>\
            ';
        }
        return '\
            <label class="col-sm-2 control-label" id="labelEle_'+columnid+'">'+labelText+'：</label>\
            <div class="'+col_sm_size+'">\
                <table>\
                    <tbody>\
                        <tr>'+tds+'</tr>\
                    </tbody>\
                </table>\
            </div>\
        ';
    }

    /**
     * 创建多列字段
     * @param rowTotalColumns 表单中一行有几列
     * @param labelText label值
     * @param columnid 表单行列在数据库中的值
     * @param noedit 是否需要编辑按钮
     * @param size 组合列有多少个字段
     * @returns {string|*}
     * @private
     */
    function ___createColumnListFieldsHTML(rowTotalColumns, labelText, columnid, size, noedit) {
        var col_sm_size = rowTotalColumns == 2 ? 'col-sm-4' : 'col-sm-10';
        var display = noedit ? 'style="display:none;"' : '';
        var tds = '';var ths = '';
        for(var i=0; i<=size; i++){
            if(i == size){//最后一列
                tds += '\
                    <td>\
                        <span class="glyphicon glyphicon-plus" style="cursor: pointer;"></span>\
                        <span class="glyphicon glyphicon-minus" style="cursor: pointer;"></span>\
                    </td>\
                ';
                ths += '<th>&nbsp;</th>';
            }else{//定义的字段列
                tds += '\
                    <td id="inputEle_'+i+'_'+columnid+'">\
                        <div class="opreation" '+display+'>\
                            <button type="button" class="btn btn-success btn-xs field-edit" columnid="'+columnid+'" idx="'+i+'"><span class="glyphicon glyphicon-pencil"></span></button>\
                        </div>\
                    </td>\
                ';
                ths += '<th id="labelEle_'+i+'_'+columnid+'">&nbsp;&nbsp;字段名&nbsp;&nbsp;</th>';
            }
        }
        return '\
            <label class="col-sm-2 control-label" id="labelEle_'+columnid+'">'+labelText+'：</label>\
            <div class="'+col_sm_size+'">\
                <table>\
                    <thead>\
                        <tr>'+ths+'</tr>\
                    </thead>\
                    <tbody>\
                        <tr>'+tds+'</tr>\
                    </tbody>\
                </table>\
            </div>\
        ';
    }

    /**
     * 创建表单中的一行
     * @param columns 这一行有几列
     * @param columninfo 每列的信息
     * @param noedit 是否需要编辑
     * @returns {string}
     * @private
     */
    function ___createFromRowHTML(columninfo, noedit){
        var columnHtml = '';
        for(var i=0; i<columninfo.length; i++){
            var _cif = columninfo[i];
            if(_cif.columntype == '0'){//一列中只有一个字段
                columnHtml += ___createColumnOneFieldHTML(_cif.totalcolumns, _cif.labelname, _cif.id, noedit);
            }else if(_cif.columntype == '1'){//一列中有多个组合字段
                columnHtml += ___createColumnGroupFieldsHTML(_cif.totalcolumns, _cif.labelname, _cif.id, _cif.size, noedit);
            }else if(_cif.columntype == '2'){//一列中有多个列表字段
                columnHtml += ___createColumnListFieldsHTML(_cif.totalcolumns, _cif.labelname, _cif.id, _cif.size, noedit);
            }
        }
        var display = noedit ? 'style="display:none;"' : '';
        return '\
            <div class="form-group form-row">\
                '+columnHtml+'\
                <div class="row-opreation" '+display+'>\
                    <button type="button" class="btn btn-danger btn-xs row-remove"><span class="glyphicon glyphicon-remove"></span></button>\
                    <button type="button" class="btn btn-warning btn-xs row-up"><span class="glyphicon glyphicon-arrow-up"></span></button>\
                </div>\
            </div>\
        ';
    }


    /**
     * 生成表单中的一行元素,返回表单行元素的ID
     * @param containerid 表单标识
     * @param columns 共几列
     * @param columntypes 列形式
     * @private
     */
    function __createFromRow(containerid, columninfo, noedit){
        //生成HTML
        var html = ___createFromRowHTML(columninfo, noedit);

        //添加到容器最后面
        $('#'+containerid).append(html);

        //添加事件
        if(!noedit){
            _addEvent($('#'+containerid));
        }
    }

    /**
     * 添加表单元素后给按钮添加事件
     * @param containerEle 容器元素
     * @private
     */
    function _addEvent(containerEle) {
        $('.field-edit,.listfield-edit', containerEle).off('click').on('click', function () {
            __editField(this);
        });

        //删除表单中的一行
        $('.row-remove', containerEle).off('click').on('click', function () {
            $(this).parents('.form-group').remove();
        });

        //上移
        $('.row-up', containerEle).each(function(i){
            if(i != 0){
                $(this).show();
                $(this).off('click').on('click', function () {
                    var curt_div = $(this).parents("div.form-group");
                    var prev_div = curt_div.prev('div.form-group');
                    curt_div.insertBefore(prev_div);

                    _addEvent(containerEle);
                });
            }else{
                $(this).hide();
            }
        });
    }

    /**
     * 编辑一个自定义属性
     * @param btn 编辑按钮
     * @private
     */
    function __editField(btn){
        var fid1 = $(btn).attr("fid1") || ''; //已经生效的扩展字段ID
        var fid2 = $(btn).attr("fid2") || ''; //维护中的扩展字段ID
        var columnid = $(btn).attr("columnid") || ''; // 表单列ID
        var fieldidx = $(btn).attr("idx") || '0'; //字段排序索引
        var libid = $('#id').val();//资源库标识

        var url = basePath+'/admin/mass/library/view/field?libid='+libid+'&fid1='+fid1+'&fid2='+fid2+'&columnid='+columnid+'&idx='+fieldidx;
        window.__editBtnObj = $(btn);//设置按钮对象

        //弹出编辑窗口
        ___openDialog(url, '字段编辑', 1000, 570, function () {
            if(typeof(window.__editBtnObj) != "undefined"){
                delete window.__editBtnObj;
            }
        });
    }

    /**
     * 编辑字段成功后的处理
     * @param field 字段信息
     * @private
     */
    function __afterEditField(field){
        if(typeof(window.__editBtnObj) != "undefined"){
            var editBtnObj = window.__editBtnObj;
            var fid1 =  editBtnObj.attr("fid1") || ''; //已生效的扩展字段ID

            //设置操作和字段ID
            if(fid1 == ''){
                editBtnObj.attr("opt", 'add'); //添加操作
            }else{
                editBtnObj.attr("opt", 'edit'); //编辑操作
            }
            editBtnObj.attr("fid2", field.id); //维护中的扩展字段ID

            //如果是多列字段属性，还需要设置
            var columnid = field.formid;
            var idx = editBtnObj.attr('idx') || '';

            //修改表单label
            if(typeof(field.labelname) != 'undefined'){
                $('#labelEle_'+columnid).html(field.labelname+"：");
            }

            //多列字段，显示字段名
            var labelEle = $('#labelEle_'+idx+'_'+columnid);
            if(labelEle.size() == 1){
                labelEle.html("&nbsp;&nbsp;"+field.fieldname+"&nbsp;&nbsp;");
            }

            //生成表单元素
            var inputEle = $('#inputEle_'+columnid);
            if(idx != ''){
                inputEle = $('#inputEle_'+idx+'_'+columnid);
            }
            WhgCustomFormField.createField(field, true, inputEle);

            //删除临时对象
            delete window.__editBtnObj;
        }
    }


    /**
     * 获取自定义的表单数据
     * @param containerEle
     * @private
     */
    function _getFormData(containerEle){
        var add = []; var edit = []; var del = []; var keep = [];
        $('div.form-row', containerEle).each(function (i) {
            var row = i;//行

            //一行两列
            $('div.col-sm-4,div.col-sm-10', $(this)).each(function(j){
                var column = j;//列
                $('button.field-edit', $(this)).each(function(k){
                    __getBtnData(add, del, edit, keep, $(this), row, column);
                });
            });
        });
        return {"add":add.join(), "edit":edit.join(), "del":del.join(), "keep":keep.join()};
    }

    /**
     * 根据按钮解析增删改的数据
     * @param addArr 添加的数据
     * @param delArr 删除的数据
     * @param editArr 修改的数据
     * @param btn 按钮
     * @param row 行
     * @param column 列
     * @private
     */
    function __getBtnData(addArr, delArr, editArr, keepArr, btn, row, column){
        var opt = btn.attr('opt');
        var fid1 = btn.attr('fid1');
        var fid2 = btn.attr('fid2');
        var columnid = btn.attr('columnid');
        if(opt == 'add'){
            addArr.push(columnid+'@'+row+'@'+column+'@'+fid2);
        }else if(opt == 'del'){
            delArr.push(columnid+'@'+row+'@'+column+'@'+fid1);
        }else if(opt == 'edit'){
            editArr.push(columnid+'@'+row+'@'+column+'@'+fid1+'@'+fid2);
        }else{
            keepArr.push(columnid+'@'+row+'@'+column+'@'+fid1);
        }
    }



    return {
        /**
         * 获取一个单页面不重复的ID
         * @returns {*}
         */
        getID: function(){
            return _getId();
        }

        /**
         * 弹出Dialog
         * @param url 连接地址
         * @param title 标题
         * @param width 宽度
         * @param height 高度
         */
        ,openDialog: function (url, title, width, height) {
            ___openDialog(url, title, width, height);
        }

        /**
         * 关闭Dialog
         */
        ,closeDialog: function () {
            ___closeDialog();
        }

        /**
         * 获得弹出Dialog的提交按钮
         * @returns {*|jQuery|HTMLElement}
         */
        ,getDialogSubmitBtn: function () {
            return ___getDialogSubmitBtn();
        }

        /**
         * 在表单中添加单行一列的表单行，只是打开Dialog
         * @param formId
         */
        ,createRowOneColumn: function (containerid) {
            return __createRowOneColumn(containerid);
        }

        /**
         * 在表单中添加单行两列的表单行，只是打开Dialog
         * @param formId
         */
        ,createRowTwoColumn: function (containerid) {
            return __createRowTwoColumn(containerid);
        }

        /**
         * 根据参数在指定的表单中生成一行
         * @param containerid 容器标识
         * @param columns 一行几列
         * @param columnstype 每列形式。一个字段/组合多个字段|多个列表列表
         */
        ,createRow: function (containerid, columninfo, noedit) {
            __createFromRow(containerid, columninfo, noedit);
        }

        /**
         * 编辑字段成功后的处理
         * @param field
         */
        ,afterEditField: function (field) {
            __afterEditField(field);
        }

        /**
         * 获取自定义的表单数据
         * @param containerEle
         */
        ,getFormData: function (containerEle) {
            return _getFormData(containerEle);
        }
    };
})();