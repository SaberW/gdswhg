/**
 * 自定义扩展字段
 * Created by wangxl on 2017/11/2.
 */
WhgCustomField = (function () {
    //下拉列表数据
    var __comboboxData__ = {};
    var __ID = 1001;

    function _getId(){
        return __ID++;
    }

    /**
     * 根据表单元素信息生成表单元素
     * @param field 表单元素信息
     * @param isTemplate true-维护模板时生成, false-生成表单时生成
     * @returns {string}
     */
    function _createField(field, isTemplate, labelEl, inputEl) {
        var html = '';
        if(field && field.fieldtype == 'easyui-textbox'){//普通文本
            html =  __createField4textbox(field, isTemplate, labelEl, inputEl);
        }else if(field && field.fieldtype == 'easyui-combobox'){//下拉选择
            html =  __createField4combobox(field, isTemplate, labelEl, inputEl);
        }else if(field && field.fieldtype == 'easyui-datebox'){//日期
            html =  __createField4datebox(field, isTemplate, labelEl, inputEl);
        }else if(field && field.fieldtype == 'easyui-datetimebox'){//日期时间
            html =  __createField4datetimebox(field, isTemplate, labelEl, inputEl);
        }else if(field && field.fieldtype == 'easyui-numberspinner'){//数字
            html =  __createField4numberspinner(field, isTemplate, labelEl, inputEl);
        }else if(field && field.fieldtype == 'radio'){//单选
            html =  __createField4radio(field, isTemplate, labelEl, inputEl);
        }else if(field && field.fieldtype == 'checkbox'){//多选
            html =  __createField4checkbox(field, isTemplate, labelEl, inputEl);
        }else if(field && field.fieldtype == 'textarea'){//多行文本
            html =  __createField4textarea(field, isTemplate, labelEl, inputEl);
        }else if(field && field.fieldtype == 'imginput'){//图片
            html =  __createField4imginput(field, isTemplate, labelEl, inputEl);
        }else if(field && field.fieldtype == 'fileinput'){//附件
            html =  __createField4fileinput(field, isTemplate, labelEl, inputEl);
        }else if(field && field.fieldtype == 'richtext'){//富文本
            html =  __createField4richtext(field, isTemplate, labelEl, inputEl);
        }

        if(html != ''){
            if(field.islistchildfield == 1){
                labelEl.text(field.fieldname);
            }else{
                labelEl.text(field.fieldname+"：");
            }
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.append(html);
            }
            $.parser.parse('#extFieldDiv');
        }
        return html;
    }

    /**
     * 生成表单元素-普通文本
     * @param field 表单元素信息
     * @param isTemplate true-维护模板时生成, false-生成表单时生成
     * @returns {string}
     */
    function __createField4textbox(field, isTemplate, labelEl, inputEl){
        var html = '';
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'true' : 'false';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            var maxLength = typeof(field.maxlength) != 'undefined' ? field.maxlength : false;
            var validType = maxLength ? 'length[0,'+maxLength+']': '';
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';
            html = '\
                <input \
                class="easyui-textbox" \
                style="width: '+field.fieldwidth+'; height: '+height+';" \
                id="'+field.id+'" name="'+field.id+'" value="'+defaultVal+'" \
                data-options="prompt:\''+field.fieldprompt+'\',required:'+required+',validType:[\''+validType+'\'],novalidate:true" \
                />\
            ';
        }
        return html;
    }

    function __createField4combobox(field, isTemplate, labelEl, inputEl){
        var html = '';
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'true' : 'false';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            var maxLength = typeof(field.maxlength) != 'undefined' ? field.maxlength : false;
            var validType = maxLength ? ',validType:\'length[0,'+maxLength+']\'': '';
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';
            var limitToList = typeof(field.fieldlimit) != 'undefined' && field.fieldlimit == 1 ?  ',limitToList:true': '';
            var editable = typeof(field.fieldeditable) != 'undefined' && field.fieldeditable == 1 ?  ',editable:false': '';
            //下拉列表可选值
            __comboboxData__[field.id] = [];
            if(typeof(field.fieldlistdata)  != 'undefined'){
                var _arr = field.fieldlistdata.split(',');
                for(var i=0; i<_arr.length; i++){
                    __comboboxData__[field.id].push({'value':_arr[i], 'text':_arr[i]});
                }
            }

            html = '\
                <input \
                class="easyui-combobox" \
                style="width: '+field.fieldwidth+'; height: '+height+';" \
                id="'+field.id+'" name="'+field.id+'" value="'+defaultVal+'" \
                data-options="prompt:\''+field.fieldprompt+'\',required:'+required+validType+limitToList+editable+', data:WhgCustomField.getComboboxData(\''+field.id+'\'),novalidate:true" \
                />\
            ';
        }
        return html;
    }

    function __createField4datebox(field, isTemplate, labelEl, inputEl){
        var html = '';
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'true' : 'false';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';
            html = '\
                <input \
                class="easyui-datebox" \
                style="width: '+field.fieldwidth+'; height: '+height+';" \
                id="'+field.id+'" name="'+field.id+'" value="'+defaultVal+'" \
                data-options="prompt:\''+field.fieldprompt+'\',required:'+required+',novalidate:true" \
                />\
            ';
        }
        return html;
    }
    function __createField4datetimebox(field, isTemplate, labelEl, inputEl){
        var html = '';
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'true' : 'false';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';
            html = '\
                <input \
                class="easyui-datetimebox" \
                style="width: '+field.fieldwidth+'; height: '+height+';" \
                id="'+field.id+'" name="'+field.id+'" value="'+defaultVal+'" \
                data-options="prompt:\''+field.fieldprompt+'\',required:'+required+',novalidate:true" \
                />\
            ';
        }
        return html;
    }
    function __createField4numberspinner(field, isTemplate, labelEl, inputEl){
        var html = '';
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'true' : 'false';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';
            html = '\
                <input \
                class="easyui-numberspinner" \
                style="width: '+field.fieldwidth+'; height: '+height+';" \
                id="'+field.id+'" name="'+field.id+'" value="'+defaultVal+'" \
                data-options="prompt:\''+field.fieldprompt+'\',required:'+required+',novalidate:true" \
                />\
            ';
        }
        return html;
    }
    function __createField4radio(field, isTemplate, labelEl, inputEl){
        var html = '';
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'required="required"' : '';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            if(typeof(field.fieldlistdata)  != 'undefined'){
                html += '<div class="custom-field custom-field-radio" '+required+'>';
                var _arr = field.fieldlistdata.split(',');
                for(var i=0; i<_arr.length; i++){
                    var checked = defaultVal == _arr[i] ? ' checked="checked"' : '';
                    html += '<label style="padding-top: 7px;"><input type="radio" name="'+field.id+'" value="'+_arr[i]+'"'+checked+'/>'+_arr[i]+'</label>&nbsp;&nbsp;';
                }
                html += '</div>';
            }
        }
        return html;
    }
    function __createField4checkbox(field, isTemplate, labelEl, inputEl){
        var html = '';
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'required="required"' : '';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            if(typeof(field.fieldlistdata)  != 'undefined'){
                html += '<div class="custom-field custom-field-checkbox" '+required+'>';
                var _arr = field.fieldlistdata.split(',');
                for(var i=0; i<_arr.length; i++){
                    var checked = defaultVal == _arr[i] ? ' checked="checked"' : '';
                    html += '<label style="padding-top: 7px;"><input type="checkbox" name="'+field.id+'" value="'+_arr[i]+'"'+checked+'/>'+_arr[i]+'</label>&nbsp;&nbsp;';
                }
                html += '</div>';
            }
        }
        return html;
    }
    function __createField4textarea(field, isTemplate, labelEl, inputEl){
        var html = '';
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'required="required"' : '';
            if(isTemplate) required = false;
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';
            var width = typeof(field.fieldwidth) != 'undefined' ? field.fieldwidth : '100%';
            var maxLength = typeof(field.maxlength) != 'undefined' ? field.maxlength : '2000';

            html = '\
                <div class="custom-field custom-field-textarea" '+required+'>\
                    <textarea id="'+field.id+'" name="'+field.id+'" style="width:'+width+';height:'+height+';" maxlength="'+maxLength+'"></textarea>\
                </div>\
            ';
        }
        return html;
    }
    function __createField4imginput(field, isTemplate, labelEl, inputEl){
        var html = '';
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'required="required"' : '';
            if(isTemplate) required = false;

            html = '\
                <div class="custom-field custom-field-imginput" '+required+'>\
                    <input type="hidden" id="'+field.id+'" name="'+field.id+'" >\
                    <div class="whgff-row-input-imgview" id="preview_'+field.id+'" style="height: 200px; width: 300px; border: 2px dashed #ccc; border-radius: 10px; text-align: center; overflow: hidden;"></div>\
                    <div class="whgff-row-input-imgfile">\
                        <i><button type="button" class="btn btn-default btn-sm" id="uploadBtn_'+field.id+'"><span class="glyphicon glyphicon-folder-open"></span>&nbsp;选择图片</button></i>\
                    </div>\
                </div>\
            ';
            //自己处理页面渲染和添加事件, 所有不返回html
            if(field.islistchildfield == 1){
                labelEl.text(field.fieldname);
            }else{
                labelEl.text(field.fieldname+"：");
            }
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.append(html);
            }
            WhgUploadImg.init({basePath: basePath , uploadBtnId: 'uploadBtn_'+field.id, hiddenFieldId: field.id, previewImgId: 'preview_'+field.id, needCut:false});
        }
        return '';
    }
    function __createField4fileinput(field, isTemplate, labelEl, inputEl){
        var html = '';
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'required="required"' : '';
            if(isTemplate) required = false;

            html = '\
                <div class="custom-field custom-field-fileinput" '+required+'>\
                    <input type="hidden" id="'+field.id+'" name="'+field.id+'" >\
                    <div class="whgff-row-input-imgfile">\
                        <i><button type="button" class="btn btn-default btn-sm" id="uploadBtn_'+field.id+'"><span class="glyphicon glyphicon-folder-open"></span>&nbsp;选择附件</button></i>\
                    </div>\
                </div>\
            ';

            //自己处理页面渲染和添加事件, 所有不返回html
            if(field.islistchildfield == 1){
                labelEl.text(field.fieldname);
            }else{
                labelEl.text(field.fieldname+"：");
            }
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.append(html);
            }
            WhgUploadFile.init({basePath: basePath, uploadBtnId: 'uploadBtn_'+field.id, hiddenFieldId: field.id});
        }
        return '';
    }
    function __createField4richtext(field, isTemplate, labelEl, inputEl) {
        var html = '';
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'required="required"' : '';
            if(isTemplate) required = false;
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '500px';
            var width = typeof(field.fieldwidth) != 'undefined' ? field.fieldwidth : '700px';
            html = '\
                <div class="custom-field custom-field-richtext" '+required+'>\
                    <script id="'+field.id+'" name="'+field.id+'" type="text/plain" style="width:'+width+'; height:'+height+';"></script>\
                </div>\
            ';

            //自己处理页面渲染和添加事件, 所有不返回html
            if(field.islistchildfield == 1){
                labelEl.text(field.fieldname);
            }else{
                labelEl.text(field.fieldname+"：");
            }
            if(inputEl.find('div.opreation').size() > 0){
                //先删除
                if(inputEl.find('div.edui-default').size() > 0){
                    var _id = inputEl.find('div.edui-default').attr("id");
                    UE.getEditor(_id).destroy();
                }
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.append(html);
            }
            UE.getEditor(field.id, {readonly:false});
        }
        return '';
    }


    /**
     * 生成单行一列表单元素
     * @param containerEle 添加元素的父容器
     * @param nextRowEle 添加元素的后一个元素
     * @param preRowEle 添加元素的前一个元素
     * @private
     */
    function _createRowOneField(containerEle, nextRowEle, preRowEle, field, notEdit) {
        var edit_btn_id_1 = 'custom_field_edit_btn_'+WhgCustomField.getID();
        var label_btn_id_1 = 'custom_field_label_btn_'+WhgCustomField.getID();

        var fid1 = field && field.id ? field.id : '';
        var html = '\
            <div class="form-group form-custom-row form-row">\
                <label class="col-sm-2 control-label" id="'+label_btn_id_1+'">字段名</label>\
                <div class="col-sm-10" id="'+edit_btn_id_1+'">\
                    <div class="opreation">\
                        <button type="button" class="btn btn-success btn-xs field-edit" fid1="'+fid1+'"><span class="glyphicon glyphicon-pencil"></span></button>\
                        <button type="button" class="btn btn-danger btn-xs field-del"><span class="glyphicon glyphicon-remove"></span></button>\
                    </div>\
                </div>\
                <div class="row-opreation">\
                    <button type="button" class="btn btn-danger btn-xs row-remove"><span class="glyphicon glyphicon-remove"></span></button>\
                    <button type="button" class="btn btn-warning btn-xs row-up"><span class="glyphicon glyphicon-arrow-up"></span></button>\
                </div>\
            </div>\
        ';
        if(typeof(containerEle) != 'undefined' && containerEle.size() == 1){
            containerEle.append(html);
        }else if(typeof(nextRowEle) != 'undefined' && nextRowEle.size() == 1){
            nextRowEle.before(html);
            containerEle = nextRowEle.parent();
        }else if(typeof(preRowEle) != 'undefined' && preRowEle.size() == 1){
            preRowEle.after(html);
            containerEle = preRowEle.parent();
        }
        //不需要编辑
        if(notEdit){
            $('div.opreation,div.row-opreation').hide();
        }else{
            _addEvent(containerEle);
        }

        //初始
        if(field){
            WhgCustomField.createField(field, true, $('#'+label_btn_id_1), $('#'+edit_btn_id_1));
        }
    }

    /**
     * 生成单行一列(多字段)表单元素
     * @param containerEle 添加元素的父容器
     * @param nextRowEle 添加元素的后一个元素
     * @param preRowEle 添加元素的前一个元素
     * @param num 几列
     * @private
     */
    function _createRowOneFieldMulti(containerEle, nextRowEle, preRowEle, num, field, childFields, notEdit){
        var ths = '';
        var tds1 = '';
        var tds2 = '';

        var initCreatFields = [];
        for(var i=0; i<num; i++){
            //默认属性ID
            //创建HTML元素
            var edit_btn_id_1 = 'custom_field_edit_btn_'+WhgCustomField.getID();
            var label_btn_id_1 = 'custom_field_label_btn_'+WhgCustomField.getID();
            var fid1 = '';
            if($.isArray(childFields) && childFields.length == num){
                var curt_field = childFields[i];
                fid1 = curt_field.id;
                initCreatFields.push( {"field":curt_field, "labelEle":label_btn_id_1, "editEle":edit_btn_id_1} );
            }

            if(i == num){
                ths += '<th>&nbsp;</th>';
                tds1 += '<td><span class="glyphicon glyphicon-minus"></span><span class="glyphicon glyphicon-plus"></span></td>';
                tds2 += '<td></td>';
            }else{
                ths += '<th>&nbsp;&nbsp;<label id="'+label_btn_id_1+'">字段名'+(i+1)+'</label>&nbsp;&nbsp;</th>';
                tds1 += '<td></td>';

                tds2 += '\
                    <td id="'+edit_btn_id_1+'">\
                        <div class="opreation">\
                        <button type="button" class="btn btn-success btn-xs field-edit" fid1="'+fid1+'" islistchildfield="1" idx="'+i+'"><span class="glyphicon glyphicon-pencil"></span></button>\
                        </div>\
                    </td>\
                ';
            }
        }


        //初始默认值
        var listFieldLabelName = '多列字段名';var fid1 = ''; var fid3 = '';
        if(field){
            listFieldLabelName = field.fieldname + "：";
            fid1 = field.id; fid3 = field.id;
        }

        var _html = '\
            <div class="form-group form-custom-row form-row">\
                <label class="col-sm-2 control-label"><span>'+listFieldLabelName+'</span><span class="glyphicon glyphicon-pencil listfield-edit" style="cursor: pointer" fid1="'+fid1+'" fid3="'+fid3+'" islistfield="1"></span></label>\
                <div class="col-sm-10">\
                    <table>\
                        <thead>\
                            <tr>'+ths+'</tr>\
                        </thead>\
                        <tbody>\
                            <tr>'+tds2+'</tr>\
                        </tbody>\
                    </table>\
                </div>\
                <div class="row-opreation">\
                    <button type="button" class="btn btn-danger btn-xs row-remove"><span class="glyphicon glyphicon-remove"></span></button>\
                    <button type="button" class="btn btn-warning btn-xs row-up"><span class="glyphicon glyphicon-arrow-up"></span></button>\
                </div>\
            </div>\
            ';

        if(typeof(containerEle) != 'undefined' && containerEle.size() == 1){
            containerEle.append(_html);
        }else if(typeof(nextRowEle) != 'undefined' && nextRowEle.size() == 1){
            nextRowEle.before(_html);
            containerEle = nextRowEle.parent();
        }else if(typeof(preRowEle) != 'undefined' && preRowEle.size() == 1){
            preRowEle.after(_html);
            containerEle = preRowEle.parent();
        }
        if(notEdit){
            $('div.opreation,div.row-opreation').hide();
            $('span.listfield-edit').hide();
        }else{
            _addEvent(containerEle);
        }

        //生成HTML元素
        for(var i=0; i<initCreatFields.length; i++){
            var fieldObj = initCreatFields[i];
            WhgCustomField.createField(fieldObj["field"], true, $('#'+fieldObj["labelEle"]), $('#'+fieldObj["editEle"]));
        }
    }

    /**
     * 生成单行两列表单元素
     * @param containerEle 添加元素的父容器
     * @param nextRowEle 添加元素的后一个元素
     * @param preRowEle 添加元素的前一个元素
     * @param num 几列
     * @private
     */
    function _createRowTwoField(containerEle, nextRowEle, preRowEle, fields, notEdit){
        var edit_btn_id_1 = 'custom_field_edit_btn_'+WhgCustomField.getID();
        var label_btn_id_1 = 'custom_field_label_btn_'+WhgCustomField.getID();
        var edit_btn_id_2 = 'custom_field_edit_btn_'+WhgCustomField.getID();
        var label_btn_id_2 = 'custom_field_label_btn_'+WhgCustomField.getID();
        var fid1_1 = fields && fields[0] && fields[0].id ? fields[0].id : '';
        var fid1_2 = fields && fields[1] && fields[1].id ? fields[1].id : '';
        html = '\
            <div class="form-group form-custom-row form-row">\
                <label class="col-sm-2 control-label" id="'+label_btn_id_1+'">字段名</label>\
                <div class="col-sm-4" id="'+edit_btn_id_1+'">\
                    <div class="opreation">\
                        <button type="button" class="btn btn-success btn-xs field-edit" fid1="'+fid1_1+'"><span class="glyphicon glyphicon-pencil"></span></button>\
                        <button type="button" class="btn btn-danger btn-xs field-del"><span class="glyphicon glyphicon-remove"></span></button>\
                    </div>\
                </div>\
                <label class="col-sm-2 control-label" id="'+label_btn_id_2+'">字段名</label>\
                <div class="col-sm-4" id="'+edit_btn_id_2+'">\
                    <div class="opreation">\
                        <button type="button" class="btn btn-success btn-xs field-edit" fid1="'+fid1_2+'"><span class="glyphicon glyphicon-pencil"></span></button>\
                        <button type="button" class="btn btn-danger btn-xs field-del"><span class="glyphicon glyphicon-remove"></span></button>\
                    </div>\
                </div>\
                <div class="row-opreation">\
                    <button type="button" class="btn btn-danger btn-xs row-remove"><span class="glyphicon glyphicon-remove"></span></button>\
                    <button type="button" class="btn btn-warning btn-xs row-up"><span class="glyphicon glyphicon-arrow-up"></span></button>\
                </div>\
            </div>\
        ';
        if(typeof(containerEle) != 'undefined' && containerEle.size() == 1){
            containerEle.append(html);
        }else if(typeof(nextRowEle) != 'undefined' && nextRowEle.size() == 1){
            nextRowEle.before(html);
            containerEle = nextRowEle.parent();
        }else if(typeof(preRowEle) != 'undefined' && preRowEle.size() == 1){
            preRowEle.after(html);
            containerEle = preRowEle.parent();
        }
        if(notEdit){
            $('div.opreation,div.row-opreation').hide();
        }else{
            _addEvent(containerEle);
        }


        if(fields && fields.length == 2){
            WhgCustomField.createField(fields[0], true, $('#'+label_btn_id_1), $('#'+edit_btn_id_1));
            WhgCustomField.createField(fields[1], true, $('#'+label_btn_id_2), $('#'+edit_btn_id_2));
        }

    }

    /**
     * 添加表单元素后给按钮添加事件
     * @param containerEle 容器元素
     * @private
     */
    function _addEvent(containerEle) {
        //删除表单中的一行
        $('.row-remove', containerEle).off('click').on('click', function () {
            $(this).parents('.form-group').remove();
        });

        $('.field-edit,.listfield-edit', containerEle).off('click').on('click', function () {
            __editField(this);
        });

        //删除一个自定义属性
        $('.field-del', containerEle).off('click').on('click', function () {
            __delField(this);
        });

        //上移
        $('.row-up', containerEle).each(function(i){
            if(i != 0){
                $(this).off('click').on('click', function () {
                    var curt_div = $(this).parents("div.form-group");
                    var prev_div = curt_div.prev('div.form-group');
                    curt_div.insertBefore(prev_div);

                    _addEvent(containerEle);
                });
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
        var islistchildfield = $(btn).attr("islistchildfield") || '0'; //是列表属性的子属性
        var islistfield = $(btn).attr("islistfield") || '0'; //是列表属性
        var listfieldid = '';//列表属性ID

        if(islistchildfield == "1"){//列表属性的子列
            //必须先添加列表属性
            var ele = $(btn).parents('.form-group').find('.listfield-edit');
            listfieldid = ele.attr('fid3');//列表属性最终的ID
            if(typeof(listfieldid) == 'undefined'){
                $.messager.alert('提示', '请先确定多列字段名!');
                return;
            }
        }
        //弹出编辑窗口
        __editFieldOpenDialog(fid1, fid2, islistchildfield, islistfield, listfieldid, $(btn));
    }

    /**
     * 弹出编辑属性的窗口
     * @param fid1 已生效的属性ID
     * @param fid2 临时的属性ID
     * @param islistchildfield  1-列属性子字段
     * @param islistfield 1-列属性
     * @param listfieldid 列属性ID
     * @param elObj 编辑按钮JQ对象
     * @private
     */
    function __editFieldOpenDialog(fid1, fid2, islistchildfield, islistfield, listfieldid, elObj) {
        window.__elObj__ = elObj;//给全局设置一个对象：编辑按钮
        var id = '_whg_feiyi_config_dialog';
        var id_dialog = id;
        var url = basePath+'/admin/mass/form/view/field?fid1='+fid1+'&fid2='+fid2+'&islistchildfield='+islistchildfield+'&islistfield='+islistfield+'&listfieldid='+listfieldid;
        if( $('#'+id).size() > 0 ){
            $('#'+id).dialog('destroy');
            $('#'+id).remove();
        }
        var _height = $(window).height() < 550 ? $(window).height() -50 : 550;
        var _width = $(window).width() < 800 ? $(window).width() -50 : 800;
        var _iframe = '<iframe id="iframe_'+id+'" frameborder="0"  src="' + url+ '" style="width:100%;height:100%;overflow: hidden;"></iframe>';
        var dialogHTML = '\
            <div id="'+id+'" style="overflow:hidden"></div>\
            <div id="Btn_'+id_dialog+'" style="text-align: center; display: none">\
                <div style="display: inline-block; margin: 0 auto">\
                    <button type="button" class="btn btn-primary" id="'+id+'_ok"><span class="glyphicon glyphicon-ok"></span>&nbsp;确  认</button>\
                    <button type="button" class="btn btn-default" id="'+id+'_no"><span class="glyphicon glyphicon-remove"></span>&nbsp;取  消</button>\
                </div>\
            </div>\
        ';
        $(dialogHTML).appendTo($("body"));
        $('#'+id).dialog({
            title: '字段配置',
            closable: true,
            width: ''+_width,
            height: ''+_height,
            border: false,
            modal: true,
            content: _iframe,
            buttons: '#Btn_'+id_dialog
        });
        $('#'+id_dialog+'_no').off('click').one("click", function () {
            $('#'+id_dialog).dialog('close');
        });
    }

    /**
     * 保存自定义属性后的处理
     * @param field 属性信息
     */
    function _afterSaveFieldSuccess(field){
        if(typeof(window.__elObj__) != "undefined"){
            var fid1 =  window.__elObj__.attr("fid1") || ''; //已生效的扩展字段ID
            var fid3 =  window.__elObj__.attr("fid3") || ''; //维护中的扩展字段ID
            var islistchildfield = window.__elObj__.attr("islistchildfield") || '0'; //是列表属性的子属性
            var islistfield = window.__elObj__.attr("islistfield") || '0'; //是列表属性

            //如果是列表属性，并且没有ID，需要得设置
            if(islistfield == '1' && fid3 == ''){
                window.__elObj__.attr("fid3", field.id); //多列属性最终ID
            }
            window.__elObj__.attr("fid2", field.id); //维护中的扩展字段ID

            //设置操作
            if(fid1 == ''){
                window.__elObj__.attr("opt", 'add'); //添加操作
            }else{
                window.__elObj__.attr("opt", 'edit'); //编辑操作
            }

            //设置字段名
            if(islistfield == '1'){
                window.__elObj__.parent('label').find('span:first').text(field.fieldname+"：");
            }else{
                //设置元素
                if(islistchildfield == '1'){//列属性子字段
                    var idx = window.__elObj__.attr("idx");
                    WhgCustomField.createField(field, true,
                        window.__elObj__.parents('table').find('thead>tr>th:eq('+idx+')>label'),
                        window.__elObj__.parent('div.opreation').parent('td'));
                }else{//普通属性
                    WhgCustomField.createField(field, true,
                        window.__elObj__.parent('div').parent('div').prev('label'),
                        window.__elObj__.parent('div.opreation').parent('div'));
                }
            }

            delete window.__elObj__;
        }
    }

    /**
     * 删除表单中的一个属性
     * @param btn 删除按钮对象
     * @private
     */
    function __delField(btn){
        var editBtn = $(btn).prev('button');
        var fid1 = editBtn.attr("fid1") || ''; //已经生效的扩展字段ID
        var fid2 = editBtn.attr("fid2") || ''; //维护中的扩展字段ID
        if(fid2 != '' && fid1 == ''){
            editBtn.attr('fid2', '');
            editBtn.attr('opt', '');
        }else{
            editBtn.attr('opt', 'del');
        }
        $(btn).parents('div.opreation').prevAll().remove();
        $(btn).parent('div').parent('div').prev('label').text('字段名');
    }


    /**
     * 获取自定义的表单数据
     * @param containerEle
     * @private
     */
    function _getFormData(containerEle){
        var add = []; var edit = []; var del = []; var keep = [];
        $('div.form-custom-row', containerEle).each(function (i) {
            var row = i;//行
            var column = 0;//列

            //一行两列
            $('.col-sm-4', $(this)).each(function(j){
                column = j;//列
                __getBtnData(add, del, edit, keep, $('button.field-edit', $(this)), row, column);
            });

            var listEditBtn = $('span.listfield-edit', $(this));
            if(listEditBtn.size() == 1){//列表属性
                __getBtnData(add, del, edit, keep, listEditBtn, row, column);
                $('button.field-edit', $(this)).each(function(j){
                    __getBtnData(add, del, edit, keep, $(this), 0, j);
                });
            }else{//普通属性
                $('.col-sm-10', $(this)).each(function(j){
                    column = j;//列
                    __getBtnData(add, del, edit, keep, $('button.field-edit', $(this)), row, column);
                });
            }
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
        if(opt == 'add'){
            addArr.push(row+'@'+column+'@'+fid2);
        }else if(opt == 'del'){
            delArr.push(row+'@'+column+'@'+fid1);
        }else if(opt == 'edit'){
            editArr.push(row+'@'+column+'@'+fid1+'@'+fid2);
        }else{
            keepArr.push(row+'@'+column+'@'+fid1);
        }
    }



    return {
        /**
         * 根据表单元素信息生成表单元素
         * @param field 表单元素信息
         * @param isTemplate true-维护模板时生成, false-生成表单时生成
         * @returns html
         */
        createField: function (field, isTemplate, labelEl, inputEl) {
            return _createField(field, isTemplate, labelEl, inputEl);
        }


        /**
         * 获取下拉框的数据集
         * @param fieldid 字段信息
         * @returns {*}
         */
        ,getComboboxData: function(fieldid){
            return __comboboxData__[fieldid];
        }

        /**
         * 生成单行一列的表单HTML
         * @param containerEle 添加元素的父容器
         * @param nextRowEle 添加元素的后一个相对元素
         * @param preRowEle 添加元素的前一个相对元素
         */
        ,createRowOneField: function(containerEle, nextRowEle, preRowEle, field, notEdit){
            _createRowOneField(containerEle, nextRowEle, preRowEle, field, notEdit);
        }


        /**
         * 生成单行一列(多列)的表单HTML
         * @param containerEle 添加元素的父容器
         * @param nextRowEle 添加元素的后一个相对元素
         * @param preRowEle 添加元素的前一个相对元素
         * @param num 添加多少列
         */
        ,createRowOneFieldMulti: function (containerEle, nextRowEle, preRowEle, num, field, childFields, notEdit) {
            _createRowOneFieldMulti(containerEle, nextRowEle, preRowEle, num, field, childFields, notEdit);
        }

        /**
         * 生成单行两列的表单HTML
         * @param containerEle
         * @param nextRowEle
         * @param preRowEle
         */
        ,createRowTwoField: function (containerEle, nextRowEle, preRowEle, fields, notEdit) {
            _createRowTwoField(containerEle, nextRowEle, preRowEle, fields, notEdit);
        }


        /**
         * 保存属性成功后的处理
         * @param fieldinfo 自定义属性信息
         */
        , afterSaveField: function (fieldinfo) {
            _afterSaveFieldSuccess(fieldinfo);
        }

        /**
         * 获取自定义的表单数据
         * @param containerEle
         */
        ,getFormData: function (containerEle) {
            return _getFormData(containerEle);
        }

        /**
         * 获取一个单页面不重复的ID
         * @returns {*}
         */
        ,getID: function(){
            return _getId();
        }
    };
})();