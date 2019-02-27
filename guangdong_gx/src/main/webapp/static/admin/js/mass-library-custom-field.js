/**
 * Created by wangxl on 2017/11/11.
 */
WhgCustomFormField = (function () {
    var __comboboxData__ = {};

    /**
     * 根据字段信息生成表单元素
     * @param field 字段信息
     * @param isTemplate true-维护模板时生成, false-生成表单时生成
     * @param inputEle 添加表单元素的容器JQUERY对象
     * @private
     */
    function _createField(field, isTemplate, inputEl) {
        if(field && field.fieldtype == 'easyui-textbox'){//普通文本
            __createField4textbox(field, isTemplate, inputEl);
        }else if(field && field.fieldtype == 'easyui-combobox'){//下拉选择
            __createField4combobox(field, isTemplate, inputEl);
        }else if(field && field.fieldtype == 'easyui-datebox'){//日期
            __createField4datebox(field, isTemplate, inputEl);
        }else if(field && field.fieldtype == 'easyui-datetimebox'){//日期时间
            __createField4datetimebox(field, isTemplate, inputEl);
        }else if(field && field.fieldtype == 'easyui-numberspinner'){//数字
            __createField4numberspinner(field, isTemplate, inputEl);
        }else if(field && field.fieldtype == 'radio'){//单选
            __createField4radio(field, isTemplate, inputEl);
        }else if(field && field.fieldtype == 'checkbox'){//多选
            __createField4checkbox(field, isTemplate, inputEl);
        }else if(field && field.fieldtype == 'textarea'){//多行文本
            __createField4textarea(field, isTemplate, inputEl);
        }else if(field && field.fieldtype == 'imginput'){//图片
            __createField4imginput(field, isTemplate, inputEl);
        }else if(field && field.fieldtype == 'fileinput'){//附件
            __createField4fileinput(field, isTemplate, inputEl);
        }else if(field && field.fieldtype == 'richtext'){//富文本
            __createField4richtext(field, isTemplate, inputEl);
        }

        var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'true' : 'false';
        if(required){
            if($('#labelEle_'+field.formid+' i').size() < 1)$('#labelEle_'+field.formid).prepend('<i>*</i>');
        }
    }

    /**
     * 生成表单元素-普通文本
     * @param field 表单元素信息
     * @param isTemplate true-维护模板时生成, false-生成表单时生成
     * @param inputEle 添加表单元素的容器JQUERY对象
     */
    function __createField4textbox(field, isTemplate, inputEl){
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'true' : 'false';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            var validType = 'length[0,999]';
            if(typeof(field.fieldvalidtype) != 'undefined'){
                validType = field.fieldvalidtype;
                if('length' == validType){
                    var min = typeof(field.fieldminval) == 'undefined' ? 0 : field.fieldminval;
                    var max = typeof(field.fieldmaxval) == 'undefined' ? 999 : field.fieldmaxval;
                    validType = validType + '['+min+', '+max+']';
                }
            }
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';
            var preHtml = typeof(field.fieldprefix) != 'undefined' ? '<span>'+field.fieldprefix+'</span>' : '';
            var sufHtml = typeof(field.fieldsuffix) != 'undefined' ? '<span>'+field.fieldsuffix+'</span>' : '';
            var html = '\
               '+preHtml+'<input \
                class="easyui-textbox" \
                style="width: '+field.fieldwidth+'; height: '+height+';" \
                id="'+field.id+'" name="'+field.fieldcode+'" value="'+defaultVal+'" \
                data-options="prompt:\''+field.fieldprompt+'\',required:'+required+',validType:\''+validType+'\',novalidate:true" \
                />'+sufHtml+'\
            ';
            //添加到界面中
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.empty();
                inputEl.append(html);
            }
            $.parser.parse(inputEl);
        }
    }

    function __createField4combobox(field, isTemplate, inputEl){
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'true' : 'false';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';
            var limitToList = typeof(field.fieldlimittolist) != 'undefined' && field.fieldlimittolist == 1 ?  ',limitToList:true': '';
            var editable = typeof(field.fieldeditable) != 'undefined' && field.fieldeditable == 1 ?  ',editable:false': '';
            //下拉列表可选值
            __comboboxData__[field.id] = [];
            if(typeof(field.fieldlistdata)  != 'undefined'){
                var _arr = field.fieldlistdata.split(',');
                for(var i=0; i<_arr.length; i++){
                    __comboboxData__[field.id].push({'value':_arr[i], 'text':_arr[i]});
                }
            }
            var preHtml = typeof(field.fieldprefix) != 'undefined' ? '<span>'+field.fieldprefix+'</span>' : '';
            var sufHtml = typeof(field.fieldsuffix) != 'undefined' ? '<span>'+field.fieldsuffix+'</span>' : '';
            var html = '\
                '+preHtml+'<input \
                class="easyui-combobox" \
                style="width: '+field.fieldwidth+'; height: '+height+';" \
                id="'+field.id+'" name="'+field.fieldcode+'" value="'+defaultVal+'" \
                data-options="prompt:\''+field.fieldprompt+'\',required:'+required+limitToList+editable+', data:WhgCustomFormField.getComboboxData(\''+field.id+'\'),novalidate:true" \
                />'+sufHtml+'\
            ';

            //添加到界面中
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.empty();
                inputEl.append(html);
            }
            $.parser.parse(inputEl);
        }
    }

    function __createField4datebox(field, isTemplate, inputEl){
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'true' : 'false';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';
            var preHtml = typeof(field.fieldprefix) != 'undefined' ? '<span>'+field.fieldprefix+'</span>' : '';
            var sufHtml = typeof(field.fieldsuffix) != 'undefined' ? '<span>'+field.fieldsuffix+'</span>' : '';
            var html = '\
                '+preHtml+'<input \
                class="easyui-datebox" \
                style="width: '+field.fieldwidth+'; height: '+height+';" \
                id="'+field.id+'" name="'+field.fieldcode+'" value="'+defaultVal+'" \
                data-options="prompt:\''+field.fieldprompt+'\',required:'+required+',novalidate:true,editable:false" \
                />'+sufHtml+'\
            ';
            //添加到界面中
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.empty();
                inputEl.append(html);
            }
            $.parser.parse(inputEl);
        }
    }
    function __createField4datetimebox(field, isTemplate, inputEl){
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'true' : 'false';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';
            var preHtml = typeof(field.fieldprefix) != 'undefined' ? '<span>'+field.fieldprefix+'</span>' : '';
            var sufHtml = typeof(field.fieldsuffix) != 'undefined' ? '<span>'+field.fieldsuffix+'</span>' : '';
            var html = '\
                '+preHtml+'<input \
                class="easyui-datetimebox" \
                style="width: '+field.fieldwidth+'; height: '+height+';" \
                id="'+field.id+'" name="'+field.fieldcode+'" value="'+defaultVal+'" \
                data-options="prompt:\''+field.fieldprompt+'\',required:'+required+',novalidate:true,editable:false" \
                />'+sufHtml+'\
            ';
            //添加到界面中
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.empty();
                inputEl.append(html);
            }
            $.parser.parse(inputEl);
        }
    }
    function __createField4numberspinner(field, isTemplate, inputEl){
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'true' : 'false';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';
            var min = typeof(field.fieldminval) != 'undefined' ? field.fieldminval : 0;
            var max = typeof(field.fieldmaxval) != 'undefined' ? field.fieldmaxval : 999;
            var preHtml = typeof(field.fieldprefix) != 'undefined' ? '<span>'+field.fieldprefix+'</span>' : '';
            var sufHtml = typeof(field.fieldsuffix) != 'undefined' ? '<span>'+field.fieldsuffix+'</span>' : '';
            var html = '\
                '+preHtml+'<input \
                class="easyui-numberspinner" \
                style="width: '+field.fieldwidth+'; height: '+height+';" \
                id="'+field.id+'" name="'+field.fieldcode+'" value="'+defaultVal+'" \
                data-options="prompt:\''+field.fieldprompt+'\',required:'+required+',novalidate:true,min:'+min+',max:'+max+'" \
                />'+sufHtml+'\
            ';
            //添加到界面中
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.empty();
                inputEl.append(html);
            }
            $.parser.parse(inputEl);
        }
    }
    function __createField4radio(field, isTemplate, inputEl){
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'required="required"' : '';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            var html = '';
            if(typeof(field.fieldlistdata)  != 'undefined'){
                html += '<div class="custom-field custom-field-radio" '+required+'>';
                var _arr = field.fieldlistdata.split(',');
                if(!defaultVal && required != '') defaultVal = _arr[0];//没有默认值，必填时默认选中第一个值
                for(var i=0; i<_arr.length; i++){
                    var checked = defaultVal == _arr[i] ? ' checked="checked"' : '';
                    html += '<label style="padding-top: 7px;"><input type="radio" name="'+field.fieldcode+'" value="'+_arr[i]+'"'+checked+'/>'+_arr[i]+'</label>&nbsp;&nbsp;';
                }
                html += '</div>';
            }
            //添加到界面中
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.empty();
                inputEl.append(html);
            }
        }
    }
    function __createField4checkbox(field, isTemplate, inputEl){
        if(typeof(field) != 'undefined'){
            var html = '';
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'required="required"' : '';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            if(typeof(field.fieldlistdata)  != 'undefined'){
                html += '<div class="custom-field custom-field-checkbox" '+required+'>';
                var _arr = field.fieldlistdata.split(',');
                if(!defaultVal && required != '') defaultVal = _arr[0];//没有默认值，必填时默认选中第一个值
                for(var i=0; i<_arr.length; i++){
                    var checked = defaultVal == _arr[i] ? ' checked="checked"' : '';
                    html += '<label style="padding-top: 7px;"><input type="checkbox" name="'+field.fieldcode+'" value="'+_arr[i]+'"'+checked+'/>'+_arr[i]+'</label>&nbsp;&nbsp;';
                }
                html += '</div>';
            }
            //添加到界面中
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.empty();
                inputEl.append(html);
            }
        }
    }
    function __createField4textarea(field, isTemplate, inputEl){
        if(typeof(field) != 'undefined'){
            //var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'required="required"' : '';
            // if(isTemplate) required = false;
            // var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';
            // var width = typeof(field.fieldwidth) != 'undefined' ? field.fieldwidth : '100%';
            // var maxLength = typeof(field.maxlength) != 'undefined' ? field.maxlength : '2000';
            // var html = '\
            //     <div class="custom-field custom-field-textarea" '+required+'>\
            //         <textarea id="'+field.id+'" name="'+field.id+'" style="width:'+width+';height:'+height+';" maxlength="'+maxLength+'"></textarea>\
            //     </div>\
            // ';

            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'true' : 'false';
            if(isTemplate) required = false;
            var defaultVal = typeof(field.fielddefaultval) != 'undefined' ? field.fielddefaultval : '';
            var validType = 'length[0,999]';
            if(typeof(field.fieldvalidtype) != 'undefined'){
                validType = field.fieldvalidtype;
                if('length' == validType){
                    var min = typeof(field.fieldminval) != 'undefined' ? 0 : field.fieldminval;
                    var max = typeof(field.fieldmaxval) != 'undefined' ? 999 : field.fieldmaxval;
                    validType = validType + '['+min+', '+max+']';
                }
            }
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '34px';

            var html = '\
               <textarea style="display: none" id="textarea_'+field.fieldcode+'">'+defaultVal+'</textarea>\
               <input \
                class="easyui-textbox" \
                style="width: '+field.fieldwidth+'; height: '+height+';" \
                id="'+field.id+'" name="'+field.fieldcode+'" \
                data-options="prompt:\''+field.fieldprompt+'\', multiline:true, required:'+required+', validType:\''+validType+'\',novalidate:true" \
                />\
            ';

            //添加到界面中
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.empty();
                inputEl.append(html);
            }
            $('#'+field.id).textbox('setValue', $('#textarea_'+field.id).val());
        }
    }
    function __createField4imginput(field, isTemplate, inputEl){
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'required="required"' : '';
            if(isTemplate) required = false;

            var html = '\
                <div class="custom-field custom-field-imginput" '+required+'>\
                    <input type="hidden" id="'+field.fieldcode+'" name="'+field.fieldcode+'" >\
                    <div class="whgff-row-input-imgview" id="preview_'+field.fieldcode+'" style="height: 200px; width: 300px; border: 2px dashed #ccc; border-radius: 10px; text-align: center; overflow: hidden;"></div>\
                    <div class="whgff-row-input-imgfile">\
                        <i><button type="button" class="btn btn-default btn-sm" id="uploadBtn_'+field.fieldcode+'"><span class="glyphicon glyphicon-folder-open"></span>&nbsp;选择图片</button></i>\
                    </div>\
                </div>\
            ';
            //自己处理页面渲染和添加事件, 所有不返回html
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.empty();
                inputEl.append(html);
            }
            WhgUploadImg.init({basePath: basePath , uploadBtnId: 'uploadBtn_'+field.fieldcode, hiddenFieldId: field.fieldcode, previewImgId: 'preview_'+field.fieldcode, needCut:false});
        }
    }
    function __createField4fileinput(field, isTemplate, inputEl){
        if(typeof(field) != 'undefined'){
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'required="required"' : '';
            if(isTemplate) required = false;

            var html = '\
                <div class="custom-field custom-field-fileinput" '+required+'>\
                    <input type="hidden" id="'+field.fieldcode+'" name="'+field.fieldcode+'" >\
                    <div class="whgff-row-input-imgfile">\
                        <i><button type="button" class="btn btn-default btn-sm" id="uploadBtn_'+field.fieldcode+'"><span class="glyphicon glyphicon-folder-open"></span>&nbsp;选择附件</button></i>\
                    </div>\
                </div>\
            ';

            //自己处理页面渲染和添加事件, 所有不返回html
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.empty();
                inputEl.append(html);
            }
            WhgUploadFile.init({basePath: basePath, uploadBtnId: 'uploadBtn_'+field.fieldcode, hiddenFieldId: field.fieldcode});
        }
    }
    function __createField4richtext(field, isTemplate, inputEl) {
        if(typeof(field) != 'undefined'){
            var html = '';
            var required = typeof(field.fieldrequired) != 'undefined' && field.fieldrequired == '1' ? 'required="required"' : '';
            if(isTemplate) required = false;
            var height = typeof(field.fieldheight) != 'undefined' ? (field.fieldheight+'px') : '500px';
            var width = typeof(field.fieldwidth) != 'undefined' ? field.fieldwidth : '700px';
            html = '\
                <div class="custom-field custom-field-richtext" '+required+'>\
                    <script id="'+field.id+'" name="'+field.fieldcode+'" type="text/plain" style="width:'+width+'; height:'+height+';"></script>\
                </div>\
            ';

            //自己处理页面渲染和添加事件, 所有不返回html
            //先删除
            if(inputEl.find('div.edui-default').size() > 0){
                var _id = inputEl.find('div.edui-default').attr("id");
                UE.getEditor(_id).destroy();
            }
            if(inputEl.find('div.opreation').size() > 0){
                inputEl.find('div.opreation').prevAll().remove();
                inputEl.find('div.opreation').before(html);
            }else{
                inputEl.empty();
                inputEl.append(html);
            }
            UE.getEditor(field.id, {readonly:false});
        }
        return '';
    }

    return {
        /**
         * 根据字段信息生成表单元素
         * @param field 字段信息
         * @param isTemplate true-维护模板时生成, false-生成表单时生成
         * @param inputEle 添加表单元素的容器JQUERY对象
         */
        createField: function (field, isTemp, inputEle) {
            _createField(field, isTemp, inputEle);
        }

        ,getComboboxData: function (fieldid) {
            return __comboboxData__[fieldid];
        }
    }
})();