WhgCustomFieldSave = (function () {
    return{
        /**
         * 切换组件类型
         * @param newVal 切换之后的值
         * @param oldVal 切换之前的值
         */
        changeComponentType: function (newVal, oldVal) {
            if('easyui-textbox' == newVal){//普通文体
                //WhgCustomFieldSave.showLabelName(true,false);
                //WhgCustomFieldSave.showFieldCode(true,false);
                //WhgCustomFieldSave.showFieldName(true,false);
                WhgCustomFieldSave.showOptionVal(false,false);//不显示可选值
                //WhgCustomFieldSave.showFieldRequired(true,false);
                WhgCustomFieldSave.showFieldDefaultval(newVal,false);
                WhgCustomFieldSave.showFieldWidth(true,false);
                WhgCustomFieldSave.showFieldHeight(true,false);
                WhgCustomFieldSave.showFieldPrompt(true,false);
                WhgCustomFieldSave.showFieldValidType(true,false);
                WhgCustomFieldSave.showFieldMinVal(true,false);
                WhgCustomFieldSave.showFieldMaxVal(true,false);
                WhgCustomFieldSave.showFieldEditable(false,false);
                WhgCustomFieldSave.showFieldLimitToList(false,false);
                WhgCustomFieldSave.showFieldPrefix(true,false);
                WhgCustomFieldSave.showFieldSuffix(true,false);
                //WhgCustomFieldSave.showIsShowFront(true,false);
                //WhgCustomFieldSave.showIsShowList(true,false);
            }else if('easyui-combobox' == newVal){
                WhgCustomFieldSave.showOptionVal(true,false);//不显示可选值
                WhgCustomFieldSave.showFieldDefaultval(newVal,false);
                WhgCustomFieldSave.showFieldWidth(true,false);
                WhgCustomFieldSave.showFieldHeight(true,false);
                WhgCustomFieldSave.showFieldPrompt(true,false);
                WhgCustomFieldSave.showFieldValidType(false,false);
                WhgCustomFieldSave.showFieldMinVal(false,false);
                WhgCustomFieldSave.showFieldMaxVal(false,false);
                WhgCustomFieldSave.showFieldEditable(true,false);
                WhgCustomFieldSave.showFieldLimitToList(true,false);
                WhgCustomFieldSave.showFieldPrefix(true,false);
                WhgCustomFieldSave.showFieldSuffix(true,false);
            }else if('easyui-datebox' == newVal){
                WhgCustomFieldSave.showOptionVal(false,false);//不显示可选值
                WhgCustomFieldSave.showFieldDefaultval(newVal,false);
                WhgCustomFieldSave.showFieldWidth(true,false);
                WhgCustomFieldSave.showFieldHeight(true,false);
                WhgCustomFieldSave.showFieldPrompt(true,false);
                WhgCustomFieldSave.showFieldValidType(false,false);
                WhgCustomFieldSave.showFieldMinVal(false,false);
                WhgCustomFieldSave.showFieldMaxVal(false,false);
                WhgCustomFieldSave.showFieldEditable(false,false);
                WhgCustomFieldSave.showFieldLimitToList(false,false);
                WhgCustomFieldSave.showFieldPrefix(true,false);
                WhgCustomFieldSave.showFieldSuffix(true,false);
            }else if('easyui-datetimebox' == newVal){
                WhgCustomFieldSave.showOptionVal(false,false);//不显示可选值
                WhgCustomFieldSave.showFieldDefaultval(newVal,false);
                WhgCustomFieldSave.showFieldWidth(true,false);
                WhgCustomFieldSave.showFieldHeight(true,false);
                WhgCustomFieldSave.showFieldPrompt(true,false);
                WhgCustomFieldSave.showFieldValidType(false,false);
                WhgCustomFieldSave.showFieldMinVal(false,false);
                WhgCustomFieldSave.showFieldMaxVal(false,false);
                WhgCustomFieldSave.showFieldEditable(false,false);
                WhgCustomFieldSave.showFieldLimitToList(false,false);
                WhgCustomFieldSave.showFieldPrefix(true,false);
                WhgCustomFieldSave.showFieldSuffix(true,false);
            }else if('easyui-numberspinner' == newVal){
                WhgCustomFieldSave.showOptionVal(false,false);//不显示可选值
                WhgCustomFieldSave.showFieldDefaultval(newVal,false);
                WhgCustomFieldSave.showFieldWidth(true,false);
                WhgCustomFieldSave.showFieldHeight(true,false);
                WhgCustomFieldSave.showFieldPrompt(true,false);
                WhgCustomFieldSave.showFieldValidType(false,false);
                WhgCustomFieldSave.showFieldMinVal(true,false);
                WhgCustomFieldSave.showFieldMaxVal(true,false);
                WhgCustomFieldSave.showFieldEditable(false,false);
                WhgCustomFieldSave.showFieldLimitToList(false,false);
                WhgCustomFieldSave.showFieldPrefix(true,false);
                WhgCustomFieldSave.showFieldSuffix(true,false);
            }else if('radio' == newVal || 'checkbox' == newVal){
                WhgCustomFieldSave.showOptionVal(true,false);//不显示可选值
                WhgCustomFieldSave.showFieldDefaultval(newVal,false);
                WhgCustomFieldSave.showFieldWidth(false,false);
                WhgCustomFieldSave.showFieldHeight(false,false);
                WhgCustomFieldSave.showFieldPrompt(false,false);
                WhgCustomFieldSave.showFieldValidType(false,false);
                WhgCustomFieldSave.showFieldMinVal(false,false);
                WhgCustomFieldSave.showFieldMaxVal(false,false);
                WhgCustomFieldSave.showFieldEditable(false,false);
                WhgCustomFieldSave.showFieldLimitToList(false,false);
                WhgCustomFieldSave.showFieldPrefix(false,false);
                WhgCustomFieldSave.showFieldSuffix(false,false);
            }else if('textarea' == newVal){
                WhgCustomFieldSave.showOptionVal(false,false);//不显示可选值
                WhgCustomFieldSave.showFieldDefaultval(newVal,false);
                WhgCustomFieldSave.showFieldWidth(true,false);
                WhgCustomFieldSave.showFieldHeight(true,false);
                WhgCustomFieldSave.showFieldPrompt(false,false);
                WhgCustomFieldSave.showFieldValidType(true,false);
                //WhgCustomFieldSave.showFieldMinVal(false,false);
                //WhgCustomFieldSave.showFieldMaxVal(false,false);
                WhgCustomFieldSave.showFieldEditable(false,false);
                WhgCustomFieldSave.showFieldLimitToList(false,false);
                WhgCustomFieldSave.showFieldPrefix(false,false);
                WhgCustomFieldSave.showFieldSuffix(false,false);
            }else if('imginput' == newVal || 'fileinput' == newVal){
                WhgCustomFieldSave.showOptionVal(false,false);//不显示可选值
                WhgCustomFieldSave.showFieldDefaultval(newVal,false);
                WhgCustomFieldSave.showFieldWidth(false,false);
                WhgCustomFieldSave.showFieldHeight(false,false);
                WhgCustomFieldSave.showFieldPrompt(false,false);
                WhgCustomFieldSave.showFieldValidType(false,false);
                WhgCustomFieldSave.showFieldMinVal(false,false);
                WhgCustomFieldSave.showFieldMaxVal(false,false);
                WhgCustomFieldSave.showFieldEditable(false,false);
                WhgCustomFieldSave.showFieldLimitToList(false,false);
                WhgCustomFieldSave.showFieldPrefix(false,false);
                WhgCustomFieldSave.showFieldSuffix(false,false);
            }else if('richtext' == newVal){
                WhgCustomFieldSave.showOptionVal(false,false);//不显示可选值
                WhgCustomFieldSave.showFieldDefaultval(newVal,false);
                WhgCustomFieldSave.showFieldWidth(true,false);
                WhgCustomFieldSave.showFieldHeight(true,false);
                WhgCustomFieldSave.showFieldPrompt(false,false);
                WhgCustomFieldSave.showFieldValidType(false,false);
                WhgCustomFieldSave.showFieldMinVal(false,false);
                WhgCustomFieldSave.showFieldMaxVal(false,false);
                WhgCustomFieldSave.showFieldEditable(false,false);
                WhgCustomFieldSave.showFieldLimitToList(false,false);
                WhgCustomFieldSave.showFieldPrefix(false,false);
                WhgCustomFieldSave.showFieldSuffix(false,false);
            }
        }

        /**
         * 表单元素Label
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showLabelName: function (enable, defaultVal) {
            $('#labelname').textbox({required:true,disabled:!enable,novalidate:true});
            $('#labelname').textbox('clear');
            if(defaultVal){
                $('#labelname').textbox('setValue', defaultVal);
            }
        }

        /**
         * 字段编码
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldCode: function (enable, defaultVal, readonly) {
            readonly = readonly || false;
            $('#fieldcode').textbox({required:true,disabled:!enable,novalidate:true, readonly:readonly});
            $('#fieldcode').textbox('clear');
            if(defaultVal){
                $('#fieldcode').textbox('setValue', defaultVal);
            }
        }

        /**
         * 字段名称
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldName: function (enable, defaultVal) {
            $('#fieldname').textbox({required:true,disabled:!enable,novalidate:true});
            $('#fieldname').textbox('clear');
            if(defaultVal){
                $('#fieldname').textbox('setValue', defaultVal);
            }
        }

        /**
         * 字段类型
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldType: function (enable, defaultVal, readonly) {
            readonly = readonly || false;
            $('#fieldtype').combobox({required:true,disabled:!enable,novalidate:true, readonly:readonly});
            $('#fieldtype').combobox('clear');
            if(defaultVal){
                $('#fieldtype').combobox('setValue', defaultVal);
            }
        }

        /**
         * 是否必须
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldRequired: function (enable, defaultVal) {
            if(enable){
                $(':radio[name="fieldrequired"]').attr('disabled', false);
                if(defaultVal && defaultVal === 0 || defaultVal === 1){
                    $(':radio[name="fieldrequired"][value="'+defaultVal+'"]').attr("checked",true);
                }
            }else{
                $(':radio[name="fieldrequired"]').attr('disabled', true);
            }
        }

        /**
         * 默认值
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldDefaultval: function (type, defaultVal) {
            var divEl = $('#defaultValDiv');
            divEl.empty();

            if(typeof(window.__fielddefaultval) == 'undefined'){
                defaultVal = init_fielddefaultval;
                window.__fielddefaultval = true;
            }
            defaultVal = defaultVal || '';
            if(true){
                var _html = '';
                if('easyui-textbox' == type || 'easyui-combobox' == type || 'radio' == type || 'checkbox' == type){
                    _html = '<input class="easyui-textbox" style="width: 100%; height: 34px;" id="fielddefaultval" name="fielddefaultval" value="'+defaultVal+'" data-options="prompt:\'请输入字段默认值\', validType:[\'length[1,30]\']" />';
                }else if('easyui-datebox' == type){
                    _html = '<input class="easyui-datebox" style="width: 100%; height: 34px;" id="fielddefaultval" name="fielddefaultval" value="'+defaultVal+'" data-options="prompt:\'请输入字段默认值\'" />';
                }else if('easyui-datetimebox' == type){
                    _html = '<input class="easyui-datetimebox" style="width: 100%; height: 34px;" id="fielddefaultval" name="fielddefaultval" value="'+defaultVal+'" data-options="prompt:\'请输入字段默认值\'" />';
                }else if('easyui-numberspinner' == type){
                    _html = '<input class="easyui-numberspinner" style="width: 100%; height: 34px;" id="fielddefaultval" name="fielddefaultval" value="'+defaultVal+'" data-options="prompt:\'请输入字段默认值\'" />';
                }else if('textarea' == type || 'imginput' == type || 'fileinput' == type || 'richtext' == type){
                    //此三类没有默认值
                }
                divEl.append(_html);
                $.parser.parse('#defaultValDiv');
            }
        }

        /**
         * 可选值
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showOptionVal: function (enable, defaultVal) {
            if(typeof(window.__fieldlistdata) == 'undefined'){
                defaultVal = init_fieldlistdata;
                window.__fieldlistdata = true;
            }

            if(enable){
                $('#setOptionDiv').show();
                if(defaultVal){
                    var _arr = defaultVal;
                    if(!$.isArray(defaultVal)){
                        _arr = defaultVal.split(",");
                    }
                    for(var i=0; i<_arr.length; i++){
                        if( i == 0){
                            $('.easyui-textbox', $('#setOptionDiv')).textbox({required:true, disabled:false, novalidate:true, value:_arr[i]});
                        }else{
                            WhgCustomFieldSave.addTR(_arr[i]);
                        }
                    }
                }else{
                    $('.easyui-textbox', $('#setOptionDiv')).textbox({required:true, disabled:false, novalidate:true});
                    $('.easyui-textbox', $('#setOptionDiv')).textbox('clear');
                }
            }else{
                $('#setOptionDiv table tbody tr').each(function (i) {
                    if(i!=0){
                        WhgCustomFieldSave.removeTR($(this).find('span.glyphicon-minus').get(0));
                    }
                });
                $('.easyui-textbox', $('#setOptionDiv')).textbox({required:false, disabled:true, novalidate:true});
                $('.easyui-textbox', $('#setOptionDiv')).textbox('clear');
                $('#setOptionDiv').hide();
            }
        }

        /**
         * 是否必须
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldWidth: function (enable, defaultVal) {
            if(typeof(window.__fieldwidth) == 'undefined'){
                defaultVal = init_fieldwidth;
                window.__fieldwidth = true;
            }

            if(enable){
                var cptwidth1='100', cptwidth2 ='%';
                if(defaultVal){
                    if('%' == defaultVal.substring(defaultVal.length-1)){
                        cptwidth1 = defaultVal.substring(0, defaultVal.length-1);
                        cptwidth2 = defaultVal.substring(defaultVal.length-1);
                    }else{
                        cptwidth1 = defaultVal.substring(0, defaultVal.length-2);
                        cptwidth2 = defaultVal.substring(defaultVal.length-2);
                    }
                }
                $('#cptwidth1').numberspinner({required:true, disabled:false, novalidate:true, value:cptwidth1});
                $('#cptwidth2').combobox({required:true, disabled:false, novalidate:true});
                $('#cptwidth2').combobox('setValue', cptwidth2);
            }else{
                $('#cptwidth1').numberspinner({required:false, disabled:true, novalidate:true, value:''});
                $('#cptwidth2').combobox({required:false, disabled:true, novalidate:true, value:''});
            }
        }

        /**
         * 是否必须
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldHeight: function (enable, defaultVal) {
            if(typeof(window.__fieldheight) == 'undefined'){
                defaultVal = init_fieldheight;
                window.__fieldheight = true;
            }
            var height = defaultVal || 34;
            if(enable){
                $('#cptheight').numberspinner({required:true, disabled:false, novalidate:true, value:height});
            }else{
                $('#cptheight').numberspinner({required:false, disabled:true, novalidate:true, value:height});
            }
        }

        /**
         * 输入提示
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldPrompt: function (enable, defaultVal) {
            $('#fieldprompt').textbox({required:false, disabled:!enable, novalidate:true});
            $('#fieldprompt').textbox('clear');
            if(typeof(window.__fieldprompt) == 'undefined'){
                defaultVal = init_fieldprompt;
                window.__fieldprompt = true;
            }
            if(defaultVal){
                $('#fieldprompt').textbox('setValue', defaultVal);
            }
        }

        /**
         * 限制类型
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldValidType: function (enable, defaultVal) {
            $('#fieldvalidtype').combobox({required:enable, disabled:!enable, novalidate:true});
            $('#fieldvalidtype').combobox('clear');
            if(typeof(window.__fieldvalidtype) == 'undefined'){
                defaultVal = init_fieldvalidtype;
                window.__fieldvalidtype = true;
            }
            if(defaultVal){
                $('#fieldvalidtype').combobox('setValue', defaultVal);
            }
        }

        /**
         * 最小值
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldMinVal: function (enable, defaultVal) {
            var oldVal = $('#fieldminval').numberspinner('getValue');
            $('#fieldminval').numberspinner({required:true,disabled:!enable, novalidate:true, value:oldVal});
            //$('#fieldminval').numberspinner('clear');
            if(typeof(window.__fieldminval) == 'undefined'){
                defaultVal = init_fieldminval;
                window.__fieldminval = true;
            }
            if(defaultVal){
                $('#fieldminval').numberspinner('setValue', defaultVal);
            }
        }

        /**
         * 最大值
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldMaxVal: function (enable, defaultVal) {
            var oldVal = $('#fieldmaxval').numberspinner('getValue');
            $('#fieldmaxval').numberspinner({required:true,disabled:!enable, novalidate:true, value: oldVal});
            //$('#fieldmaxval').numberspinner('clear');
            if(typeof(window.__fieldmaxval) == 'undefined'){
                defaultVal = init_fieldmaxval;
                window.__fieldmaxval = true;
            }
            if(defaultVal){
                $('#fieldmaxval').numberspinner('setValue', defaultVal);
            }
        }

        /**
         * combobox能否编辑
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldEditable: function (enable, defaultVal) {
            if(typeof(window.__fieldeditable) == 'undefined'){
                defaultVal = init_fieldeditable;
                window.__fieldeditable = true;
            }
            if(enable){
                $(':radio[name="fieldeditable"]').attr('disabled', false);
                if(defaultVal && defaultVal === 0 || defaultVal === 1){
                    $(':radio[name="fieldeditable"][value="'+defaultVal+'"]').attr("checked",true);
                }
            }else{
                $(':radio[name="fieldeditable"]').attr('disabled', true);
            }
        }

        /**
         * combobox能否选择其它值
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldLimitToList: function (enable, defaultVal) {
            if(typeof(window.__fieldlimittolist) == 'undefined'){
                defaultVal = init_fieldlimittolist;
                window.__fieldlimittolist = true;
            }
            if(enable){
                $(':radio[name="fieldlimittolist"]').attr('disabled', false);
                if(defaultVal && defaultVal === 0 || defaultVal === 1){
                    $(':radio[name="fieldlimittolist"][value="'+defaultVal+'"]').attr("checked",true);
                }
            }else{
                $(':radio[name="fieldlimittolist"]').attr('disabled', true);
            }
        }

        /**
         * 前缀
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldPrefix: function (enable, defaultVal) {
            $('#fieldprefix').textbox({required:false,disabled:!enable,novalidate:true});
            $('#fieldprefix').textbox('clear');
            if(typeof(window.__fieldprefix) == 'undefined'){
                defaultVal = init_fieldprefix;
                window.__fieldprefix = true;
            }
            if(defaultVal){
                $('#fieldprefix').textbox('setValue', defaultVal);
            }
        }

        /**
         * 前缀
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showFieldSuffix: function (enable, defaultVal) {
            $('#fieldsuffix').textbox({required:false,disabled:!enable,novalidate:true});
            $('#fieldsuffix').textbox('clear');
            if(typeof(window.__fieldsuffix) == 'undefined'){
                defaultVal = init_fieldsuffix;
                window.__fieldsuffix = true;
            }
            if(defaultVal){
                $('#fieldsuffix').textbox('setValue', defaultVal);
            }
        }

        /**
         * combobox能否选择其它值
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showIsShowFront: function (enable, defaultVal) {
            if(enable){
                $(':radio[name="isshowfront"]').attr('disabled', false);
                if(typeof(defaultVal) != 'undefined' && (defaultVal == '0' || defaultVal == '1')){
                    $(':radio[name="isshowfront"][value="'+defaultVal+'"]').attr("checked",true);
                }
            }else{
                $(':radio[name="isshowfront"]').attr('disabled', true);
            }
        }

        /**
         * combobox能否选择其它值
         * @param enable true-启用 false-停用
         * @param defaultVal 默认值
         */
        ,showIsShowList: function (enable, defaultVal) {
            if(enable){
                $(':radio[name="isshowlist"]').attr('disabled', false);
                if(typeof(defaultVal) != 'undefined' && (defaultVal == '0' || defaultVal == '1')){
                    $(':radio[name="isshowlist"][value="'+defaultVal+'"]').attr("checked",true);
                }
            }else{
                $(':radio[name="isshowlist"]').attr('disabled', true);
            }
            $(':radio[name="isshowlist"]').hide();
        }

        /**
         * 添加可选值输入表单元素
         * @param defaultVal
         */
        ,addTR: function(defaultVal) {
            defaultVal = defaultVal || '';
            var _html = '\
                <tr>\
                    <td>\
                        <input class="easyui-textbox" style="width: 150px; height: 34px;" name="optionval" value="'+defaultVal+'" data-options="prompt:\'可选值\',required:true,validType:\'length[1,30]\',novalidate:true" />\
                    </td>\
                    <td>\
                        <span class="glyphicon glyphicon-plus" style="cursor: pointer;" onclick="WhgCustomFieldSave.addTR()"></span>\
                        <span class="glyphicon glyphicon-minus" style="cursor: pointer;" onclick="WhgCustomFieldSave.removeTR(this)"></span>\
                    </td>\
                </tr>\
                ';
            $('#setOptionDiv table tbody').append(_html);
            $.parser.parse('#setOptionDiv');
        }

        /**
         * 删除可选值表单输入框
         * @param el
         */
        ,removeTR: function(el){
            $(el).parents('tr').remove();
        }

    }
})();