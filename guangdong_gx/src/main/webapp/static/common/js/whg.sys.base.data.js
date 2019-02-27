/**
 * 获得关键字|标签|分类|文化品牌|状态|文化馆六类数据
 * author: wangxinlin
 * version: 1-201703
 */
WhgSysData = (function(){
    //是否初始
    var _init_type, _init_cult_value, _init_big_type, _init_key, _init_tag, _init_brand, _init_project, _init_fktype, _init_cult, _init_user, _init_province, _init_city, _init_menu;

    //获取分类数据
    var _typeData = {};
    function _getTypeData(type) {
        if(!_init_type){
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/type/srchList"),
                success: function(data){
                    _init_type = true;
                    if(data && data.success == "1" && typeof data["data"] != "undefined" ){
                        if($.isArray(data.data)){
                            for(var i=0; i<(data.data).length; i++){
                                var __data = (data.data)[i];
                                var __id = __data.id;
                                var __name = __data.name;
                                var __type = __data.type+"";
                                if(typeof _typeData[__type] == 'undefined'){
                                    _typeData[__type] = [];
                                }
                                _typeData[__type].push({"id":__id, "text":__name});
                            }
                        }
                    }
                }
            });
        }
        return (typeof _typeData[type] != 'undefined') ? _typeData[type] : [];
    }
    //获取总分类数据
    var _bigTypeData = {};
    function _getBigTypeData(type) {
        if(!_init_big_type){
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/type/srchBigTypeList?type="+type),
                success: function(data){
                    _init_big_type = true;
                    if(data && data.success == "1" && typeof data["data"] != "undefined" ){
                        if($.isArray(data.data)){
                            for(var i=0; i<(data.data).length; i++){
                                var __data = (data.data)[i];
                                var __id = __data.id;
                                var __name = __data.name;
                                var __type = __data.type+"";
                                if(typeof _typeData[__type] == 'undefined'){
                                    _bigTypeData[__type] = [];
                                }
                                _bigTypeData[__type].push({"id":__id, "text":__name});
                            }
                        }
                    }
                }
            });
        }
        return (typeof _bigTypeData[type] != 'undefined') ? _bigTypeData[type] : [];
    }



    //获取所属系统数据
    var _projectData =  [];
    function _getFromProject() {
        if(!_init_project) {
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/project/srchList"),
                success: function (data) {
                    _init_project = true;
                    if (data && data.success == "1" && typeof data["data"] != "undefined") {
                        if ($.isArray(data.data)) {
                            for (var i = 0; i < (data.data).length; i++) {
                                var __data = (data.data)[i];
                                _projectData.push({"id": __data.id, "text": __data.name});
                            }
                        }
                    }
                }
            });
        }
        return _projectData;
    } //获取所属系统数据
    var _menuData =  [];
    function _getFrontMenu() {
        if(!_init_menu) {
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/frontmenu/srchList"),
                success: function (data) {
                    _init_menu = true;
                    if (data && data.success == "1" && typeof data["data"] != "undefined") {
                        if ($.isArray(data.data)) {
                            for (var i = 0; i < (data.data).length; i++) {
                                var __data = (data.data)[i];
                                _menuData.push({"id": __data.id, "text": __data.name});
                            }
                        }
                    }
                }
            });
        }
        return _menuData;
    }

    //获取关联类型
    var _fktypeData =  [];
    function _getFkType() {
        if(!_init_fktype) {
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/fktype/srchList"),
                success: function (data) {
                    _init_fktype = true;
                    if (data && data.success == "1" && typeof data["data"] != "undefined") {
                        if ($.isArray(data.data)) {
                            for (var i = 0; i < (data.data).length; i++) {
                                var __data = (data.data)[i];
                                _fktypeData.push({"id": __data.id, "text": __data.name});
                            }
                        }
                    }
                }
            });
        }
        return _fktypeData;
    }

    //获取关键数据
    var _keyData = {};
    function _getKeyData(type) {
        if(!_init_key){
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/key/srchList"),
                success: function(data){
                    _init_key = true;
                    if(data && data.success == "1" && typeof data["data"] != "undefined" ){
                        if($.isArray(data.data)){
                            for(var i=0; i<(data.data).length; i++){
                                var __data = (data.data)[i];
                                var __id = __data.id;
                                var __name = __data.name;
                                var __type = __data.type+"";
                                if(typeof _keyData[__type] == 'undefined'){
                                    _keyData[__type] = [];
                                }
                                _keyData[__type].push({"id":__id, "text":__name});
                            }
                        }
                    }
                }
            });
        }
        return (typeof _keyData[type] != 'undefined') ? _keyData[type] : [];
    }

    //获取标签数据
    var _tagData = {};
    function _getTagData(type) {
        if(!_init_tag){
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/tag/srchList"),
                success: function(data){
                    _init_tag = true;
                    if(data && data.success == "1" && typeof data["data"] != "undefined" ){
                        if($.isArray(data.data)){
                            for(var i=0; i<(data.data).length; i++){
                                var __data = (data.data)[i];
                                var __id = __data.id;
                                var __name = __data.name;
                                var __type = __data.type+"";
                                if(typeof _tagData[__type] == 'undefined'){
                                    _tagData[__type] = [];
                                }
                                _tagData[__type].push({"id":__id, "text":__name});
                            }
                        }
                    }
                }
            });
        }
        return (typeof _tagData[type] != 'undefined') ? _tagData[type] : [];
    }

    //获取状态数据
    var _stateDate = {};

    function _getStateData(type, biz) {
        if(typeof _stateDate[type] == 'undefined'){
            _stateDate[type] = [];//[{"id":"", "text":"全部"}];
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/state/srchList/"+type),
                success: function(data){
                    if(data && data.success == "1" && typeof data["data"] != "undefined" ){
                        if($.isArray(data.data)){
                            for(var i=0; i<(data.data).length; i++){
                                var __data = (data.data)[i];
                                if (biz && biz != "") {
                                    if (biz.indexOf(__data.value) != -1) {// 可编辑状态 过滤掉
                                        continue;
                                    }
                                }
                                _stateDate[type].push({"id":__data.value, "text":__data.name});
                            }
                        }
                    }
                }
            });
        }
        return _stateDate[type];
    }

    //获取品牌数据
    var _brandData = [];
    function _getBrandData() {
        if(!_init_brand){
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/brand/srchList"),
                success: function(data){
                    _init_brand = true;
                    if(data && data.success == "1" && typeof data["data"] != "undefined" ){
                        if($.isArray(data.data)){
                            for(var i=0; i<(data.data).length; i++){
                                var __data = (data.data)[i];
                                _brandData.push({"id":__data.id, "text":__data.name});
                            }
                        }
                    }
                }
            });
        }
        return _brandData;
    }

    //获取文化馆数据
    var _cultData = [];
    function _getCultData() {
        if(!_init_cult){
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/cult/srchList"),
                success: function(data){
                    _init_cult = true;
                    _cultData.push( {"id":"TOP", "text":"总馆"} );
                    if(data && data.success == "1" && typeof data["data"] != "undefined" ){
                        if($.isArray(data.data)){
                            for(var i=0; i<(data.data).length; i++){
                                var __data = (data.data)[i];
                                _cultData.push({"id":__data.id, "text":__data.name});
                            }
                        }
                    }
                }
            });
        }
        return _cultData;
    }

    //获取用户数据
    var _userData = [];

    function _getUserData() {
        if (!_init_user) {
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/user/srchList"),
                success: function (data) {
                    _init_user = true;
                    if (data && data.success == "1" && typeof data["data"] != "undefined") {
                        if ($.isArray(data.data)) {
                            for (var i = 0; i < (data.data).length; i++) {
                                var __data = (data.data)[i];
                                _userData.push({"id": __data.id, "text": __data.account});
                            }
                        }
                    }
                }
            });
        }
        return _userData;
    }

    //获取省
    var _Province = "";
    function _getProvinceData() {
        if(!_init_province){
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/cult/getProvince"),
                success: function(data){
                    _init_province = true;
                    if(data && data.success == "1" && typeof data != "undefined" ){
                        _Province = data.data;
                    }
                }
            });
        }
        return _Province;
    }

    //获取市
    var _City = "";
    function _getCityData() {
        if(!_init_city){
            $.ajax({
                async: false,
                cache: false,
                url: getFullUrl("/comm/cult/getCity"),
                success: function(data){
                    _init_city = true;
                    if(data && data.success == "1" && typeof data != "undefined" ){
                        _City = data.data;
                    }
                }
            });
        }
        return _City;
    }

    return {
        /**
         * 根据ID获取Text
         * @param val ID
         * @param map Text
         */
        FMT: function (val, data) {
            if (val != "" && val == '0000000000000000') {
                return '总站';
            }
            if(typeof data != 'undefined' && $.isArray(data)){
                for(var i=0; i<data.length; i++){
                    if(data[i].id == val){
                        val = data[i].text;
                    }
                }
            }
            return val;
        }
        
        /**
         * 获取分类数据
         * @param type
         */
        ,getTypeData: function (type) {
            return _getTypeData(type);
        }/**
         * 获取分类数据
         * @param type
         */
        ,getBigTypeData: function (type) {
            return _getBigTypeData(type);
        }
        /**
         * 获取所属系统
         */
        ,getFromProject: function () {
            return _getFromProject();
        }/**
         * 菜单自定义
         */
        ,getFrontMenu: function () {
            return _getFrontMenu();
        }
        /**
         * 获取所属系统
         */
        ,getFkType: function () {
            return _getFkType();
        }

        /**
         * 获标签数据
         * @param type
         */
        ,getTagData: function (type) {
            return _getTagData(type);
        }

        /**
         * 获取关键字数据
         * @param type
         */
        ,getKeyData: function (type) {
            return _getKeyData(type);
        }

        /**
         * 获取状态数据
         * @param type
         */
        ,getStateData: function (type) {
            return _getStateData(type, null);
        }
        , getStateData: function (type, biz) {
            return _getStateData(type, biz);
        }

        /**
         * 获取文化品牌数据
         */
        ,getBrandData: function () {
            return _getBrandData();
        }

        /**
         * 获取文化馆数据
         */
        ,getCultData: function () {
            return _getCultData();
        } /**
         * 获取用户数据
         */
        , getUserData: function () {
            return _getUserData();
        }

        /**
         * 获取省数据
         */
        ,getProvinceData: function () {
            return _getProvinceData();
        }

        /**
         * 获取市数据
         */
        ,getCityData: function () {
            return _getCityData();
        }
    }
})();

/**
 * 控制端公共方法
 * @type {{optFMT, stateFMT, delStateFMT, bmStateFMT, bizStateFMT, artTypeFMT, venueTypeFMT, roomTypeFMT, activityTypeFMT, trainTypeFMT, venueTagFMT, roomTagFMT, activityTagFMT, trainTagFMT, zxTagFMT, venueKeyFMT, roomKeyFMT, activityKeyFMT, trainKeyFMT, zxKeyFMT}}
 */
WhgComm = (function(){
    //打开Dialog的ID
    var __WhgDialog4EditId = 'WhgDialog4Edit';

    return {
        confirm: function(title, message, fn){
            $.messager.confirm(title, message, function(r){
                if (r){
                    fn();
                }
            })
        },
        search: function (dgId, tbId, url) {
            var srchForm = $(tbId).find('form');
            if(srchForm && srchForm.size() > 0){
                if(!srchForm.form('validate')){
                    return;
                }
            }

            var dgId = dgId || '#whgdg';
            if (!dgId.match(/^[#\.]/)){dgId = "#"+dgId;}
            var tbId = tbId || 'whgdg-tb-srch';
            if (!tbId.match(/^[#\.]/)){tbId = "#"+tbId;}

            var dgObj = $(dgId);
            var tbObj = $(tbId);
            if(url) dgObj.datagrid('options').url = url;
            var _queryParams = dgObj.datagrid('options').queryParams || {};
            tbObj.find('input[name]').each(function(){
                var propName = $(this).attr('name');
                if($(this).val() != ''){
                    _queryParams[$(this).attr('name')] = $(this).val();
                }else{
                    delete _queryParams[propName];
                }
            });
            dgObj.datagrid({
                url: dgObj.datagrid('options').url,
                queryParams: _queryParams
            });
        }

        ,FMTOpt: function(val, rowData, index){
            var optDivId = this.optDivId;
            if (!optDivId.match(/^[#\.]/)){
                optDivId = "#"+optDivId;
            }
            var optDiv = $(optDivId);
            if (optDiv.size() == 0) return "";

            //按钮是否显示控制
            optDiv.find('a').each(function(){
                var _show = true;

                //根据validKey控制显示
                var validKey = $(this).attr("validKey");
                $(this).linkbutton({ plain: true });
                if(typeof validKey != "undefined"){
                    var validVal = $(this).attr("validVal");
                    var vvl = validVal.split(/,\s*/);
                    $(this).linkbutton('disable');
                    _show = false;
                    for(var k in vvl){
                        var v = vvl[k];
                        if (rowData[validKey] == v){
                            $(this).linkbutton('enable');
                            _show = true;
                            break;
                        }
                    }
                }

                //根据验证函数判断是否显示按钮
                var validFun = $(this).attr("validFun");
                if(typeof validFun != "undefined"){
                    _show = eval(validFun+'('+index+')');
                    if(_show){
                        $(this).linkbutton('enable');
                    }else{
                        $(this).linkbutton('disable');
                    }
                }

                //设置属性
                var prop = $(this).attr("prop");
                var propVal = '';
                if(typeof prop != "undefined"){
                    propVal = rowData[prop];
                }

                //注册点击事件
                var method = $(this).attr("method");
                if(_show && method){
                    $(this).attr("onClick", method+"("+index+", '"+propVal+"');");
                }else{
                    $(this).removeAttr("onClick");
                }
            });
            return optDiv.html();
        }

        ,FMTTreeGridOpt: function(val, rowData, index){
            index = rowData.id;//treeGrid，没有index,只有rowData对象
            var optDivId = this.optDivId;
            if (!optDivId.match(/^[#\.]/)){
                optDivId = "#"+optDivId;
            }
            var optDiv = $(optDivId);
            if (optDiv.size() == 0) return "";

            //按钮是否显示控制
            optDiv.find('a').each(function(){
                var _show = true;

                //根据validKey控制显示
                var validKey = $(this).attr("validKey");
                $(this).linkbutton({ plain: true });
                if(typeof validKey != "undefined"){
                    var validVal = $(this).attr("validVal");
                    var vvl = validVal.split(/,\s*/);
                    $(this).linkbutton('disable');
                    _show = false;
                    for(var k in vvl){
                        var v = vvl[k];
                        if (rowData[validKey] == v){
                            $(this).linkbutton('enable');
                            _show = true;
                            break;
                        }
                    }
                }

                //根据验证函数判断是否显示按钮
                var validFun = $(this).attr("validFun");
                if(typeof validFun != "undefined"){
                    _show = eval(validFun+'('+index+')');
                    if(_show){
                        $(this).linkbutton('enable');
                    }else{
                        $(this).linkbutton('disable');
                    }
                }

                //设置属性
                var prop = $(this).attr("prop");
                var propVal = '';
                if(typeof prop != "undefined"){
                    propVal = rowData[prop];
                }

                //注册点击事件
                var method = $(this).attr("method");
                if(_show && method){
                    $(this).attr("onClick", method+"("+index+", '"+propVal+"');");
                }else{
                    $(this).removeAttr("onClick");
                }
            });
            return optDiv.html();
        }
        
        ,FMTImg: function (val) {
            if(!val) return '';
            return '<img src="'+WhgComm.getImgServerAddr()+val+'" style="height:80px;"/>';
        }

        ,FMTResourceImg: function (val) {
            if(!val) return '';
            return '<img src="'+val+'" style="height:80px; width: auto;"/>';
        }

        ,getImg750_500: function (imgAddr) {
            if(imgAddr && imgAddr.length > 10){
                var idx = imgAddr.lastIndexOf(".");
                if(idx !== -1){
                    imgAddr = imgAddr.substring(0, idx)+"_750_500"+imgAddr.substring(idx);
                }
            }
            return imgAddr;
        }

        ,getImg300_200: function (imgAddr) {
            if(imgAddr && imgAddr.length > 10){
                var idx = imgAddr.lastIndexOf(".");
                if(idx !== -1){
                    imgAddr = imgAddr.substring(0, idx)+"_300_200"+imgAddr.substring(idx);
                }
            }
            return imgAddr;
        }

        ,getImg740_555: function (imgAddr) {
            if(imgAddr && imgAddr.length > 10){
                var idx = imgAddr.lastIndexOf(".");
                if(idx !== -1){
                    imgAddr = imgAddr.substring(0, idx)+"_740_555"+imgAddr.substring(idx);
                }
            }
            return imgAddr;
        }

        ,FMTDate: function (val) {
            if(!val) return val;
            return new Date(val).Format("yyyy-MM-dd");
        }

        ,FMTDateTime: function (val) {
            if(!val) return val;
            return new Date(val).Format("yyyy-MM-dd hh:mm:ss");
        }, FMTTime: function (val) {
            if(isNaN(val)) return val;
            return new Date(val).Format("hh:mm:ss");
        }

        ,FMTState: function (val) {
            return WhgSysData.FMT(val, WhgComm.getState());
        }

        ,getState: function () {
            return WhgSysData.getStateData("EnumState");
        }

        ,FMTDelState: function(val){
            return WhgSysData.FMT(val, WhgComm.getDelState());
        }

        ,getDelState: function () {
            return WhgSysData.getStateData("EnumDelState");
        }

        ,FMTBmState: function (val) {
            return WhgSysData.FMT(val, WhgComm.getBmState());
        }

        ,getBmState: function () {
            return WhgSysData.getStateData("EnumBMState");
        }

        ,FMTBizState: function (val,idx) {
            if(1 == idx.delstate){
                return "已回收";
            }
            if (idx.state && idx.tempstorage && idx.tempstorage == '1' && idx.state == 1) {
                return "编辑中-草稿";
            }
            return WhgSysData.FMT(val, WhgComm.getBizState());
        }

        , getBizState: function (biz) {
            if (biz && biz != "") {
                return WhgSysData.getStateData("EnumBizState", biz);
            } else {
                return WhgSysData.getStateData("EnumBizState");
            }
        }
        ,FMTBrand: function(val){
        	return WhgSysData.FMT(val, WhgComm.getBrand());
        }
        
        ,getBrand: function(){
        	return WhgSysData.getBrandData();
        }

        ,FMTCult: function (val) {
            return WhgSysData.FMT(val, WhgComm.getCult());
        }

        , getCult: function () {
            return WhgSysData.getCultData();
        }

        //格式化管理员ID为账号
        , FMTUserName: function (val) {
            var userAccount = val == '2015121200000000' ? 'administrator' : WhgSysData.FMT(val, WhgComm.getUserName()) || '';
            if(userAccount != '' && userAccount != 'administrator'){
                userAccount = '<a href="javascript:void(0);" onclick="WhgComm.showUserInfo(\''+val+'\');">'+userAccount+'</a>';
            }
            return userAccount;
        }
        //格式化身份证号
        , FMTUserCard: function (val) {
            var idCard = val;
            var birthday;
            if (idCard != null && idCard != "") {
                if (idCard.length == 15) {
                    birthday = "19" + idCard.substr(6, 6);
                } else if (idCard.length == 18) {
                    birthday = idCard.substr(6, 8);
                }
                idCard = idCard.replace(birthday, "********");
            }
            return idCard;
        }
        //管理员数据
        , getUserName: function () {
            return WhgSysData.getUserData();
        }

        //显示管理员详情
        ,showUserInfo: function (userid) {
            WhgComm.openDialog4size('管理员详情', basePath+'/admin/system/user/view/info?id='+userid, 650, 480, {hideSubmitBtn:true, cancelBtnText:'关  闭'});
        }


        //获取省
        ,getProvince: function () {
            return WhgSysData.getProvinceData();
        }
        //获取市
        ,getCity: function () {
            return WhgSysData.getCityData();
        }
        ,FMTArtType: function (val) {
            return WhgSysData.FMT(val, WhgComm.getArtType());
        },getArtType: function () {
            return WhgSysData.getTypeData("1");//1代表艺术分类
        }
        ,FMTBigType: function (val) {
            return WhgSysData.FMT(val, WhgComm.getBigType());
        }
       ,getBigType: function (type) {
            return WhgSysData.getBigTypeData(type);//省馆分类
        }
        ,FMTFromProject: function (val) {
            return WhgSysData.FMT(val, WhgComm.getFromProject());
        },getFromProject: function () {
            return WhgSysData.getFromProject();//所属系统
        },FMTFrontMenu: function (val) {
            return WhgSysData.FMT(val, WhgComm._getFrontMenu());
        },getFrontMenu: function () {
            return WhgSysData.getFrontMenu();//所属系统
        },FMTFkType: function (val) {
            return WhgSysData.FMT(val, WhgComm.getFkType());
        },getFkType: function () {
            return WhgSysData.getFkType();//关联类型
        }
        ,FMTVenueType: function (val) {
            return WhgSysData.FMT(val, WhgComm.getVenueType());
        }

        ,getVenueType: function () {
            return WhgSysData.getTypeData("2");//2代表场馆分类
        }

        ,FMTRoomType: function (val) {
            return WhgSysData.FMT(val, WhgComm.getRoomType());
        }

        ,getRoomType: function () {
            return WhgSysData.getTypeData("3");//3代表活动室分类
        }

        ,FMTActivityType: function(val){
            return WhgSysData.FMT(val, WhgComm.getActivityType());
        }

        ,getActivityType: function () {
            return WhgSysData.getTypeData("4");//4代表活动分类
        }

        ,FMTTrainType: function(val){
            return WhgSysData.FMT(val, WhgComm.getTrainType());
        }

        ,getTrainType: function () {
            return WhgSysData.getTypeData("5");//5代表培训分类
        }

        ,FMTAreaType: function(val){
            return WhgSysData.FMT(val, WhgComm.getAreaType());
        }
        , FMTCult: function (val) {
            return WhgSysData.FMT(val, WhgComm.getCult());
        }

        ,getAreaType: function () {
            return WhgSysData.getTypeData("6");//6代表区域分类
        }

        ,FMTSBType: function(val){
            return WhgSysData.FMT(val, WhgComm.getSBType());
        }

        ,getSBType: function () {
            return WhgSysData.getTypeData("7");//7代表活动室设备分类
        }
        ,FMTTEAType: function(val){
            return WhgSysData.FMT(val, WhgComm.getTEAType());
        }
        ,getTEAType: function () {
            return WhgSysData.getTypeData("11");//11代表老师专长分类
        }
        ,FMTZYFLType: function(val){
            return WhgSysData.FMT(val, WhgComm.getZYFLType());
        }
        ,getZYFLType: function () {
            return WhgSysData.getTypeData("14");//14代表资源分类
        }
        ,FMTCULType: function(val){
            return WhgSysData.FMT(val, WhgComm.getCULType());
        }
        ,getCULType: function () {
            return WhgSysData.getTypeData("15");//15文化展类型分类
        }
        ,FMTLIVEType: function(val){
            return WhgSysData.FMT(val, WhgComm.getCULType());
        }
        ,getLiveType: function () {
            return WhgSysData.getTypeData("16");//16直播分类
        }
        ,FMTGoodsType: function (val) {
            return WhgSysData.FMT(val, WhgComm.getGoodsType());
        }
        ,getGoodsType: function () {
            return WhgSysData.getTypeData("20");
        }
        //展演类商品
        ,FMTShowGoodsType: function (val) {
            return WhgSysData.FMT(val, WhgComm.getShowGoodsType());
        }
        ,getShowGoodsType: function () {
            return WhgSysData.getTypeData("21");
        }
        //展品
        ,FMTExhGoodsType: function (val) {
            return WhgSysData.FMT(val, WhgComm.getExhGoodsType());
        }
        ,getExhGoodsType: function () {
            return WhgSysData.getTypeData("22");
        }
        //展览类
        ,FMTExhType: function (val) {
            return WhgSysData.FMT(val, WhgComm.getExhType());
        }
        ,getExhType: function () {
            return WhgSysData.getTypeData("23");
        }
        //人才
        ,FMTPerType: function (val) {
            return WhgSysData.FMT(val, WhgComm.getPerType());
        }
        ,getPerType: function () {
            return WhgSysData.getTypeData("25");
        }
        
        ,FMTVenueTag: function (val) {
            return WhgSysData.FMT(val, WhgComm.getVenueTag());
        }

        ,getVenueTag: function () {
            return WhgSysData.getTagData("2");//1代表场馆标签
        }

        ,FMTRoomTag: function (val) {
            return WhgSysData.FMT(val, WhgComm.getRoomTag());
        }

        ,getRoomTag: function () {
            return WhgSysData.getTagData("3");//代表活动室标签
        }

        ,FMTActivityTag: function(val){
            return WhgSysData.FMT(val, WhgComm.getActivityTag());
        }

        ,getActivityTag: function () {
            return WhgSysData.getTagData("4");//代表活动标签
        }

        ,FMTTrainTag: function(val){
            return WhgSysData.FMT(val, WhgComm.getTrainTag());
        }

        ,getTrainTag: function () {
            return WhgSysData.getTagData("5");//4代表培训标签
        }

        ,FMTZxTag: function(val){
            return WhgSysData.FMT(val, WhgComm.getZxTag());
        }

        ,getZxTag: function () {
            return WhgSysData.getTagData("25");//5代表资讯标签
        }
        ,FMTPxszTag: function(val){
            return WhgSysData.FMT(val, WhgComm.getZxTag());
        }

        ,getPxszTag: function () {
            return WhgSysData.getTagData("27");//5代表资讯标签
        }
        ,FMTGoodsTag: function (val) {
            return WhgSysData.FMT(val, WhgComm.getGoodsTag());
        }
        ,getGoodsTag: function () {
            return WhgSysData.getTagData("20");
        }
        
        ,FMTVenueKey: function (val) {
            return WhgSysData.FMT(val, WhgComm.getVenueKey());
        }

        ,getVenueKey: function () {
            return WhgSysData.getKeyData("2");
        }

        ,FMTRoomKey: function (val) {
            return WhgSysData.FMT(val, WhgComm.getRoomKey());
        }

        ,getRoomKey: function () {
            return WhgSysData.getKeyData("3");
        }

        ,FMTActivityKey: function(val){
            return WhgSysData.FMT(val, WhgComm.getActivityKey());
        }

        ,getActivityKey: function () {
            return WhgSysData.getKeyData("4");
        }

        ,FMTTrainKey: function(val){
            return WhgSysData.FMT(val, WhgComm.getTrainKey());
        }

        ,getTrainKey: function () {
            return WhgSysData.getKeyData("5");
        }

        ,FMTZxKey: function(val){
            return WhgSysData.FMT(val, WhgComm.getZxKey());
        }

        ,getZxKey: function () {
            return WhgSysData.getKeyData("25");
        }
        ,FMTPxszKey: function(val){
            return WhgSysData.FMT(val, WhgComm.getZxKey());
        }

        ,getPxszKey: function () {
            return WhgSysData.getKeyData("27");
        }
        ,FMTLiveKey: function(val){
            return WhgSysData.FMT(val, WhgComm.getZxKey());
        }

        ,getLiveKey: function () {
            return WhgSysData.getKeyData("8");
        }
        ,FMTGoodsKey: function (val) {
            return WhgSysData.FMT(val, WhgComm.getGoodsKey());
        }
        ,getGoodsKey: function (){
            return WhgSysData.getKeyData("20");
        }

        ,FMTSupplyType: function (val,idx) {
            return WhgSysData.FMT(val, WhgComm.getSupplyType());
        }

        ,getSupplyType: function () {
            return WhgSysData.getStateData("EnumSupplyType");
        }

        //资源类型 图片/音频/视频/文档
        ,FMTResourceType:function(val){
            return WhgSysData.FMT(val, WhgComm.getResourceTypeData());
        }
        //资源类型 图片/音频/视频/文档
        ,getResourceTypeData: function () {
            return WhgSysData.getStateData("EnumUploadType");
        }

        //资源类型数字 图片/音频/视频/文档
        ,FMTResType:function(val){
            return WhgSysData.FMT(val, WhgComm.getResTypeData());
        }
        //资源类型数字 图片/音频/视频/文档
        ,getResTypeData: function () {
            return WhgSysData.getStateData("EnumResType");
        }

        //实体类型
        ,FMTTypeClazz: function (val) {
            return WhgSysData.FMT(val, WhgComm.getTypeClazzData());
        }
        ,getTypeClazzData: function () {
            return WhgSysData.getStateData("EnumTypeClazz");
        }

        /**
         * 列表页编辑时打开的新页面
         * @param url
         */
        ,editDialog: function (url) {
            var id = __WhgDialog4EditId;
            if( $('#'+id).size() > 0 ){
                $('#'+id).dialog('destroy');
                $('#'+id).remove();
            }
            $('<div id="'+id+'" style="overflow:hidden"></div>').appendTo($("body"));
            var _iframe = '<iframe scrolling="auto" frameborder="0"  src="' + url+ '" style="width:100%;height:100%;"></iframe>';
            $('#'+id).dialog({
                title: '',
                closable: true,
                maximized: true,
                border: false,
                modal: true,
                content: _iframe
            });
        }

        /**
         * 使用WhgComm.editDialog打开编辑里面，用此方法可以关闭打开的Dialog，模拟返回操作
         */
        ,editDialogClose: function () {
            window.parent.$('#'+__WhgDialog4EditId).dialog('close');
        }

        /**
         * 弹出窗口
         * @param url 窗口中url
         * @param width 窗口width
         * @param height 窗口height
         */
        ,openDialog4size: function (title, url, width, height, options) {
            var _options = $.extend({}, {
                hideSubmitBtn: false,
                hideCancelBtn: false,
                submitBtnText: '保  存',
                cancelBtnText: '取  消',
                closeFun: false
            }, options);
            var submit_display = _options.hideSubmitBtn ? "display:none" : "";
            var cancel_display = _options.hideCancelBtn ? "display:none" : "";
            var id = __WhgDialog4EditId;
            var id_dialog = id;
            if( $('#'+id).size() > 0 ){
                $('#'+id).dialog('destroy');
                $('#'+id).remove();
            }
            width = width || 800;
            height = height || 500;
            var _height = $(window).height() < height ? $(window).height() -50 : height;
            var _width = $(window).width() < width ? $(window).width() -50 : width;
            var _iframe = '<iframe id="iframe_'+id+'" frameborder="0"  src="' + url+ '" style="width:100%;height:100%;overflow: hidden;"></iframe>';
            var dialogHTML = '\
            <div id="'+id+'" style="overflow:hidden"></div>\
            <div id="Btn_'+id_dialog+'" style="text-align: center; display: none">\
                <div style="display: inline-block; margin: 0 auto">\
                    <a id="'+id+'_ok" style="'+submit_display+'" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:\'icon-save\'">'+_options.submitBtnText+'</a>\
                    <a id="'+id+'_no" style="'+cancel_display+'" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:\'icon-cancel\'">'+_options.cancelBtnText+'</a>\
                </div>\
            </div>\
        ';
            $(dialogHTML).appendTo($("body"));
            $.parser.parse('#Btn_'+id_dialog);
            $('#'+id).dialog({
                title: title,
                closable: true,
                width: ''+_width,
                height: ''+_height,
                border: false,
                modal: true,
                content: _iframe,
                buttons: '#Btn_'+id_dialog,
                onClose: function () {
                    if($.isFunction(_options.closeFun)){
                        _options.closeFun();
                    }
                }
            });
            $('#'+id_dialog+'_no').off('click').one("click", function () {
                $('#'+id_dialog).dialog('close');
            });
        }

        /**
         * 获取弹出层提交按钮
         * @returns {*|jQuery|HTMLElement}
         */
        ,getOpenDialogSubmitBtn: function () {
            return window.parent.$('#'+__WhgDialog4EditId+'_ok');
        }

        /**
         * 图片服务器地址
         * @returns {string}
         */
        ,getImgServerAddr: function () {
            if(typeof window.imgServerAddr != 'undefined'){
                return imgServerAddr;
            }
            return '';
        }

        /**
         * 将字符串yyyy-MM-dd转换成Date对象
         * @param dateVal yyyy-MM-dd字符串
         */
        ,parseDate: function (dateVal) {
            var d = new Date();
            try{
                dateVal += " 00:00:00";
                d = new Date(Date.parse(dateVal.replace(/-/g, "/")));
            }catch (e){
            }
            return d;
        }

        /**
         * 将字符串yyyy-MM-dd HH:mm:ss转换成Date对象
         * @param dateTimeVal yyyy-MM-dd HH:mm:ss字符串
         */
        ,parseDateTime: function (dateTimeVal) {
            var d = new Date();
            try{
                d = new Date(Date.parse(dateTimeVal.replace(/-/g, "/")));
            }catch (e){
            }
            return d;
        }

        /**
         * 获取权限分馆
         * @returns {Array}
         */
        ,getMgrCults: function () {
            var arr = [];
            if(typeof window.managerCults != 'undefined'){
                if (managerCults.length > 0) {
                    return managerCults;
                }
            }
            return arr;
        }
        /**
         * 获取权限分馆--带全部
         * @returns {Array}
         */
        , getMgrAllCults: function () {
            var arr = [];
            if (typeof window.managerCults != 'undefined') {
                if (managerCults.length > 0) {
                    //arr.push({"id": '', "text": '全部'});
                    arr = arr.concat(managerCults);
                }
            }
            return arr;
        }

        /**
         * 获取权限分馆的第一个分馆ID
         * @returns {string}
         */
        ,getMgrCultsFirstId: function () {
            var cultid = "";
            var arr = WhgComm.getMgrCults();
            if (arr.length > 0) {
                cultid = arr[0].id;
            }
            return cultid;
        }

        /**
         * 获取权限分馆及其子馆
         * @returns {Array}
         */
        ,getMgrCultsAndChild: function () {
            if(typeof window.managerCultsAndChild != 'undefined'){
                return managerCultsAndChild;
            }
            return [];
        }

        /**
         * 获取权限分馆及其子馆ids
         * @returns {Array}
         */
        ,getMgrCultsAndChildIds: function () {
            var cultid = '';
            var cultArr = WhgComm.getMgrCultsAndChild();
            if(cultArr.length > 0){
                var sp = "";
                for(var i=0; i<cultArr.length; i++){
                    cultid += sp+cultArr[i].id;
                    sp = ",";
                }
            }
            return cultid;
        }

        /**
         * 获取权限分馆下的部门
         * @returns {Array} [{"id":"", "text":""},...]
         */
        ,getChildDept: function (cultid) {
            var arr = [];
            if(typeof window.managerDepts != 'undefined'){
                if(managerDepts.hasOwnProperty(cultid)){
                    var depts = managerDepts[cultid];
                    for(var i=0; i<depts.length; i++){
                        arr.push({"id":depts[i].id, "text":depts[i].name});
                    }
                }
            }
            return arr;
        }/**
         * 获取权限分馆下的部门--全部
         * @returns {Array} [{"id":"", "text":""},...]
         */
        , getChildAllDept: function (cultid) {
            var arr = [];
            if (typeof window.managerDepts != 'undefined') {
                if (managerDepts.hasOwnProperty(cultid)) {
                    var depts = managerDepts[cultid];
                    if (depts.length > 0) {
                        arr.push({"id": '', "text": '全部'});
                    }
                    for (var i = 0; i < depts.length; i++) {
                        arr.push({"id": depts[i].id, "text": depts[i].name});
                    }
                }
            }
            return arr;
        }

        /**
         * 获取指定权限文化馆的第一个权限部门的ID
         * @param cultid 权限文化馆ID
         * @returns {string}
         */
        ,getFirstChildDeptId: function (cultid) {
            var deptid = '';
            var arr = WhgComm.getChildDept(cultid);
            if(arr.length > 0){
                deptid = arr[0].id;
            }
            return deptid;
        }

        /**
         * 获取指定权限文化馆的所有权限部门的ID,多个用逗号分隔
         * @param cultid 权限文化馆IDs
         * @returns {string}
         */
        ,getAllChildDeptIds: function (cultid) {
            var deptid = '';
            var arr = WhgComm.getChildDept(cultid);
            if(arr.length > 0){
                var sp = "";
                for(var i=0; i<arr.length; i++){
                    deptid += sp+arr[i].id;
                    sp = ",";
                }
            }
            return deptid;
        }

        /**
         * 选择权限文化馆，自动设置文化馆下面的部门
         * @param newVal
         * @param oldVal
         */
        ,changeCultId: function(newVal, oldVal){
            var _data = [];
            if(newVal){
                _data = WhgComm.getChildDept(newVal);
            }
            $('#deptid').combobox('loadData', _data);
            $('#deptid').combobox('setValue', '');
        }

        ,initPMS: function(options){
            var opts = $.extend({}, {
                basePath:'',
                cultEid: false, cultValue: false, cultOnChange: false,
                cultEditable: false, allcult: false, alldept: false,
                deptEid:false, deptValue:false,
                ywiWhppEid:false, ywiWhppValue:false,
                ywiBigTypeEid:false, ywiBigTypeValue:false,ywiBigType:1,
                ywiArtTypeEid:false, ywiArtTypeValue:false,ywiArtTypeClass:1,
                ywiTypeEid:false, ywiTypeValue:false, ywiTypeClass:1,
                ywiKeyEid:false, ywiKeyValue:false, ywiKeyClass:1,
                ywiTagEid:false, ywiTagValue:false, ywiTagClass:1,
                ywiTagEid2:false,
                venEid:false, venValue:false,
                artistId: false, artistValue: false,
                //加载更多的场馆,活动室使用时设为false
                moreVen:true,
                actEid:false, actValue:false,
                traEid:false, traValue:false,
                roomEid:false, roomValue:false,
                bmEnd:false,
                //省市区
                provinceEid:false, provinceValue:false,
                cityEid:false, cityValue:false,
                areaEid:false, areaValue:false,
                //配送省市
                psprovinceEid:false, psprovinceValue:false,
                pscityEid:false, pscityValue:false
            }, options);

            $.ajaxSetup({cache: false});

            function psRefPCA(cultid, oldcultid){
                //无相关指定配送省市,就不需要处理了
                if (!opts.psprovinceEid || !opts.pscityEid){
                    return;
                }

                //所选的文化馆信息,后台加入了省市区的数据项
                var cultOptions = $('#' + opts.cultEid).combobox("options") || {};
                var cultdata = cultOptions.data || [];
                var cultInfo = {};
                for(var i in cultdata){
                    if (cultid == cultdata[i].id){
                        cultInfo = $.extend({}, cultdata[i]);
                        break;
                    }
                }
                cultInfo.psprovince = cultInfo.province || '广东省';

                setPsProvince();

                function setPsProvince(){
                    var provinces = getProvinceData() || [];
                    if (!provinces || !provinces.length){
                        setTimeout(function(){
                            setPsProvince();
                        }, 300);
                        return;
                    }

                    var el = $('#'+opts.psprovinceEid);
                    el.combobox("loadData", provinces);

                    if (opts.pscityEid){
                        var cityel = $('#'+opts.pscityEid);
                        cityel.combobox({
                            novalidate: true,
                            onChange: function (val, oldval) {
                                if (val.length>1 && val[0]==''){
                                    val.shift();
                                    $(this).combobox("setValues", val);
                                }
                            },
                            loadFilter: function(data){
                                if (data && data.length){
                                    data.unshift({name:"全省"});
                                }
                                return data;
                            },
                            onSelect: function(record){
                                if (record.name == "全省"){

                                    var data = $(this).combobox("getData");
                                    if (data && data.length){
                                        var values = [];
                                        for(var i in data){
                                            if (data[i].name=="全省") continue;
                                            values.push(data[i].name);
                                        }
                                        var that = this;
                                        setTimeout(function(){
                                            $(that).combobox("setValues", values);
                                            $(that).combobox("hidePanel");
                                        }, 100);

                                    }
                                }
                            }
                        });
                        cityel.combobox('setValues', '');
                        el.combobox({
                            "onChange":function(newVal, oldVal){
                                if (newVal){
                                    getCityData(newVal, function(citys){
                                        cityel.combobox('loadData', citys);
                                        cityel.combobox('setValues', '');

                                        var ps_city = cultInfo.pscity || '';
                                        if (opts.pscityValue && opts.pscityValue!=''){
                                            ps_city = opts.pscityValue;
                                            opts.pscityValue = ''; //初始值用一次清理
                                        }
                                        cityel.combobox('setValues', ps_city);
                                        cultInfo.pscity = ""; //值用过清理
                                    });
                                }
                            }
                        });
                    }

                    var ps_province = cultInfo.psprovince || '';
                    if (opts.psprovinceValue && opts.psprovinceValue!=''){
                        ps_province = opts.psprovinceValue;
                        opts.psprovinceValue = ''; //初始值用一次清理
                    }
                    el.combobox('setValue', ps_province);
                }
            }

            function refPCA(cultid, oldcultid){
                //无相关指定省市区,就不需要处理了
                if (!opts.provinceEid && !opts.cityEid && !opts.areaEid){
                    return;
                }

                //所选的文化馆信息,后台加入了省市区的数据项
                var cultOptions = $('#' + opts.cultEid).combobox("options") ||{};
                var cultdata = cultOptions.data || [];
                var cultInfo = {};
                for(var i in cultdata){
                    if (cultid == cultdata[i].id){
                        cultInfo = $.extend({}, cultdata[i]);
                        break;
                    }
                }
                cultInfo.province = cultInfo.province || '广东省';

                if (opts.provinceEid){
                    setProvince();
                }else if (opts.cityEid){
                    setCity();
                }else if (opts.areaEid){
                    setArea();
                }

                function setProvince(){
                    //el.combobox("loadData", __PROVINCE);
                    var provinces = getProvinceData() || [];
                    if (!provinces || !provinces.length){
                        setTimeout(function(){
                            setProvince();
                        }, 300);
                        return;
                    }

                    var el = $('#'+opts.provinceEid);
                    el.combobox("loadData", provinces);

                    if (opts.cityEid){
                        $('#'+opts.cityEid).combobox('setValue', '');
                        el.combobox({
                            "onChange":function(newVal, oldVal){
                                loadCityData(newVal);
                                setCity();
                            }
                        });
                    }

                    var _province = cultInfo.province || '';
                    if (opts.provinceValue && opts.provinceValue!=''){
                        _province = opts.provinceValue;
                        opts.provinceValue = ''; //初始值用一次清理
                    }
                    el.combobox('setValue', _province);
                }
                function setCity(){
                    if (!opts.provinceEid){
                        loadCityData(cultInfo.province);
                    }
                    var el = $('#'+opts.cityEid);

                    if (opts.areaEid){
                        $('#'+opts.areaEid).combobox('setValue', '');
                        el.combobox({
                            "onChange":function(newVal, oldVal){
                                loadAreaData(newVal);
                                setArea();
                            }
                        });
                    }

                    var _city = cultInfo.city || '';
                    if (opts.cityValue && opts.cityValue!=''){
                        _city = opts.cityValue;
                        opts.cityValue = ''; //初始值用一次清理
                    }
                    el.combobox('setValue', _city);
                    cultInfo.city = ""; //值用过清理
                }
                function setArea(){
                    if (!opts.cityEid){
                        loadAreaData(cultInfo.city);
                    }
                    var _area = cultInfo.area || '';
                    if (opts.areaValue && opts.areaValue!=''){
                        _area = opts.areaValue;
                        opts.areaValue = '';    //初始值用一次清理
                    }
                    $('#'+opts.areaEid).combobox('setValue', _area);
                    cultInfo.area = ""; //值用过清理
                }
                function loadCityData(province){
                    //var citys = [];
                    if(province){
                        /*for(var i=0; i<__CITY.length; i++){
                            if(__CITY[i].proname == province){
                                citys.push(__CITY[i]);
                            }
                        }*/

                        getCityData(province, function(citys){
                            $('#'+opts.cityEid).combobox('loadData', citys);
                        });
                    }

                    //$('#'+opts.cityEid).combobox('loadData', citys);
                }
                function loadAreaData(city){
                    //var areas = [];
                    if(city){
                        /*for(var i=0; i<__AREA.length; i++){
                            if(__AREA[i].cityname == city){
                                areas.push(__AREA[i]);
                            }
                        }*/
                        getAreaData(city, function(areas){
                            $('#'+opts.areaEid).combobox('loadData', areas);
                        })
                    }
                    //$('#'+opts.areaEid).combobox('loadData', areas);
                }

            };

            //切换场馆
            function changeVenue(newVal, oldVal){
                //定位活动室
                if(opts.roomEid){
                    $('#'+opts.roomEid).combobox({limitToList:true, valueField:'id', textField:'title', novalidate:true, url:opts.basePath+'/admin/venue/room/srchList?state=6&delstate=0&venid='+newVal});
                    if(typeof(window.__initVenue) != 'undefined'){
                        window.setTimeout(function(){
                            var s = $('#'+opts.roomEid).combobox('getData');
                            var val = s.length > 0 ? s[0].id : '';
                           // $('#'+opts.roomEid).combobox('setValue', val);
                        },200);
                    }else{
                        $('#'+opts.roomEid).combobox('setValue', opts.roomValue || '');
                        window.__initVenue = true;
                    }
                }
            }

            //切换文化馆
            function changeCult(newVal, oldVal) {
                refPCA(newVal, oldVal);

                psRefPCA(newVal, oldVal);

                if(opts.roomEid){
                    $('#'+opts.roomEid).combobox('loadData', []);
                    $('#'+opts.roomEid).combobox('clear');
                }


                //部门
                if(opts.deptEid){
                    var deptValue = '';
                    if (newVal) {
                        if (opts.alldept == true) {
                            $('#' + opts.deptEid).combobox({
                                //limitToList: true,
                                valueField: 'id',
                                textField: 'text',
                                editable: false,
                                data: WhgComm.getChildDept(newVal),
                                novalidate: true
                            });
                            deptValue = opts.deptValue;
                        } else {
                            $('#' + opts.deptEid).combobox({
                                //limitToList: true,
                                valueField: 'id',
                                textField: 'text',
                                editable: false,
                                data: WhgComm.getChildDept(newVal),
                                novalidate: true
                            });
                            deptValue = opts.deptValue || WhgComm.getFirstChildDeptId(newVal);
                        }
                        if(typeof(window.__initDeptid) == 'undefined'){
                            if (opts.alldept == true) {
                                deptValue = opts.deptValue;
                            } else {
                                deptValue = opts.deptValue || WhgComm.getFirstChildDeptId(newVal);
                            }
                            window.__initDeptid = true;
                        }else{
                            if (opts.alldept == true) {
                                deptValue = '';
                            } else {
                                deptValue = WhgComm.getFirstChildDeptId(newVal) || '';
                            }
                        }
                        $('#'+opts.deptEid).combobox('setValue', deptValue);
                    }else{
                        $('#'+opts.deptEid).combobox({limitToList:true, valueField:'id', textField:'text', data:[], novalidate:true});
                        $('#'+opts.deptEid).combobox('clear');
                    }
                }

                //场馆
                if(opts.venEid){
                    if(newVal){
                        $('#'+opts.venEid).combobox({
                            limitToList:true, valueField:'id', textField:'title', url:opts.basePath+'/admin/venue/srchList?state=6&delstate=0&cultid='+newVal+'&more='+opts.moreVen
                            , novalidate:true, onChange: changeVenue
                        });
                        if(typeof(window.__init_Cult_Venue) != 'undefined'){
                            window.setTimeout(function(){
                                var s = $('#'+opts.venEid).combobox('getData');
                                var val = s.length > 0 ? s[0].id : '';
                                $('#'+opts.venEid).combobox('setValue', val);
                            },200);
                        }else{
                            $('#'+opts.venEid).combobox('setValue', opts.venValue || '');
                            window.__init_Cult_Venue = true;
                        }
                    }else{
                        $('#'+opts.venEid).combobox({limitToList:true, valueField:'id', textField:'title', data:[], novalidate:true});
                        $('#'+opts.venEid).combobox('clear');
                    }
                }
                //活动
                if(opts.actEid){
                    if(newVal){

                        var isBmEnd = '';
                        if (opts.bmEnd){
                            isBmEnd = '1';
                        }
                        $('#'+opts.actEid).combobox({
                            multiple:true, editable:true, novalidate:true,
                            valueField:'id', textField:'name', url:opts.basePath+'/admin/activity/srchList?bmEnd='+isBmEnd+'&state=6&delstate=0&cultid='+newVal

                        });
                        if(typeof(window.__init_Cult_ACT) != 'undefined'){
                            window.setTimeout(function(){
                                var s = $('#'+opts.actEid).combobox('getData');
                                var val = s.length > 0 ? s[0].id : '';
                                $('#'+opts.actEid).combobox('setValue', val);
                            },200);
                        }else{
                            $('#'+opts.actEid).combobox('setValues', opts.actValue.split(',') || '');
                            window.__init_Cult_ACT = true;
                        }
                    }else{
                        $('#'+opts.actEid).combobox({limitToList:true, valueField:'id', textField:'name', data:[], novalidate:true});
                        $('#'+opts.actEid).combobox('clear');
                    }
                }
                //培训
                if(opts.traEid){
                    if(newVal){
                        var isBmEnd = '';
                        if (opts.bmEnd){
                            isBmEnd = '1';
                        }
                        $('#'+opts.traEid).combobox({
                            multiple:true, editable:true, novalidate:true,
                            //limitToList:true,
                            valueField:'id', textField:'title', url:opts.basePath+'/admin/train/srchList?bmEnd='+isBmEnd+'&state=6&delstate=0&cultid='+newVal
                            , novalidate:true
                        });
                        if(typeof(window.__init_Cult_TRAIN) != 'undefined'){
                            window.setTimeout(function(){
                                var s = $('#'+opts.traEid).combobox('getData');
                                var val = s.length > 0 ? s[0].id : '';
                                $('#'+opts.traEid).combobox('setValue', val);
                            },200);
                        }else{
                            $('#'+opts.traEid).combobox('setValues', opts.traValue.split(',') || '');
                            window.__init_Cult_TRAIN = true;
                        }
                    }else{
                        $('#'+opts.traEid).combobox({limitToList:true, valueField:'id', textField:'title', data:[], novalidate:true});
                        $('#'+opts.traEid).combobox('clear');
                    }
                }

                //文化品牌
                if(opts.ywiWhppEid){
                    if(newVal){
                        $('#' + opts.ywiWhppEid).combobox({
                            panelHeight: 'auto',
                            multiple: true,
                            editable: true,
                            valueField: 'id',
                            textField: 'name',
                            novalidate: true,
                            url: opts.basePath + '/admin/yunwei/whpp/srchList?state=1&&cultid=' + newVal
                        });
                        var _values = [];
                        if(opts.ywiWhppValue && opts.ywiWhppValue.length > 0){
                            if(opts.ywiWhppValue.substring(0,1) == ',') opts.ywiWhppValue = opts.ywiWhppValue.substring(1);
                            _values = opts.ywiWhppValue.split(',');
                        }
                        $('#'+opts.ywiWhppEid).combobox('setValues', _values);
                    }else{
                        $('#' + opts.ywiWhppEid).combobox({
                            panelHeight: 'auto',
                            multiple: true,
                            editable: true,
                            valueField: 'id',
                            textField: 'name',
                            novalidate: true,
                            data: []
                        });
                        $('#'+opts.ywiWhppEid).combobox('clear');
                    }
                }
                //文化品牌
                if(opts.ywiBigTypeEid){
                    if(newVal){
                        $('#'+opts.ywiBigTypeEid).combobox({panelHeight:'auto', multiple:false,limitToList:true, editable:false, valueField:'id', textField:'name', novalidate:true, url:opts.basePath+'/admin/yunwei/type/srchList?cultid=0000000000000000&type='+opts.ywiBigType});
                        var _values = [];
                        if(opts.ywiBigTypeValue && opts.ywiBigTypeValue.length > 0){
                            if(opts.ywiBigTypeValue.substring(0,1) == ',') opts.ywiBigTypeValue = opts.ywiBigTypeValue.substring(1);
                            _values = opts.ywiBigTypeValue.split(',');
                        }
                        //$('#'+opts.ywiBigTypeEid).combobox('setValues', _values);
                    }else{
                        $('#'+opts.ywiBigTypeEid).combobox({panelHeight:'auto', multiple:false,limitToList:true, editable:false, valueField:'id', textField:'name', novalidate:true, data:[]});
                        $('#'+opts.ywiBigTypeEid).combobox('clear');
                    }
                }

                //艺术分类
                if(opts.ywiArtTypeEid){
                    $('#'+opts.ywiArtTypeEid).html('');
                    if(newVal){
                        debugger;
                        //$.get(opts.basePath+'/admin/yunwei/type/srchList', {type:1, cultid:newVal}, function(data){
                        $.get(opts.basePath+'/admin/yunwei/type/srchList', {type:opts.ywiArtTypeClass, cultid:newVal}, function(data){
                            if($.isArray(data)){
                                var _values = [];
                                if(opts.ywiArtTypeValue && opts.ywiArtTypeValue != ''){
                                    _values = opts.ywiArtTypeValue.split(',');
                                }
                                var _type = 'radio', __cls = '';
                                if ($('#'+opts.ywiArtTypeEid).hasClass("checkbox")){
                                    _type = "checkbox";
                                    __cls = 'class="styled"';
                                }
                                var html = '';
                                for(var i=0; i<data.length; i++){
                                    var row = data[i];
                                    var _name = $('#'+opts.ywiArtTypeEid).attr('name');
                                    var ischecked = $.inArray(row.id, _values) > -1 ? ' checked="checked"' : '';
                                    html += '<input type="'+_type+'" id="'+_name+"_"+row.id+'" name="'+_name+'" value="'+row.id+'" '+__cls+' '+ischecked+'>';
                                    html += '<label for="'+_name+"_"+row.id+'">'+row.name+'</label>';
                                }
                                $('#'+opts.ywiArtTypeEid).html(html);
                            }
                        });
                    }
                }

                //分类
                if(opts.ywiTypeEid){
                    //转换成数组
                    if(!$.isArray(opts.ywiTypeEid)){
                        opts.ywiTypeEid = [opts.ywiTypeEid];
                        opts.ywiTypeValue = [opts.ywiTypeValue];
                        opts.ywiTypeClass = [opts.ywiTypeClass];
                    }
                    for(var i=0; i<opts.ywiTypeEid.length; i++){
                        var _Eid = opts.ywiTypeEid[i];
                        var _Val = opts.ywiTypeValue[i];
                        var _Cls = opts.ywiTypeClass[i];
                        ajaxCall(_Eid, _Val, _Cls, newVal);
                    }
                }

                //关键字
                if(opts.ywiKeyEid){
                    if(newVal){
                        $('#'+opts.ywiKeyEid).combobox({multiple:true, editable:true, valueField:'id', textField:'name', novalidate:true, url:opts.basePath+'/admin/yunwei/key/srchList?type='+opts.ywiKeyClass+'&&cultid='+newVal});
                        var def_val = '';
                        if(typeof(window.__init_Cult_KEY) == 'undefined'){
                            def_val = opts.ywiKeyValue.split(',') || '';
                            window.__init_Cult_KEY = true;
                        }
                        $('#'+opts.ywiKeyEid).combobox('setValues', def_val);
                    }else{
                        $('#'+opts.ywiKeyEid).combobox({multiple:true, editable:true, valueField:'id', textField:'name', novalidate:true, data:[]});
                        $('#'+opts.ywiKeyEid).combobox('clear');
                    }
                }

                //文艺专家
                if(opts.artistId){
                    if(newVal){
                        $('#'+opts.artistId).combobox({multiple:false, editable:false, valueField:'id', textField:'name', novalidate:true, url:opts.basePath+'/admin/personnel/srchList?cultid='+newVal});
                    }else{
                        $('#'+opts.artistId).combobox({multiple:false, editable:false, valueField:'id', textField:'name', novalidate:true});
                        $('#'+opts.artistId).combobox('clear');
                    }
                }


                //标签2
                if(opts.ywiTagEid2){
                    if(newVal){
                        $('#'+opts.ywiTagEid2).combobox({multiple:false,valueField:'id', textField:'name', novalidate:true, url:opts.basePath+'/admin/yunwei/tag/srchList?type='+opts.ywiKeyClass});
                        var def_val = '';
                        if(typeof(window.__init_Cult_KEY) == 'undefined'){
                            def_val = opts.ywiTagValue.split(',') || '';
                            window.__init_Cult_KEY = true;
                        }
                        $('#'+opts.ywiTagEid2).combobox('setValues', def_val);

                    }else{
                        $('#'+opts.ywiTagEid2).combobox({multiple:true, editable:true, valueField:'id', textField:'name', novalidate:true, data:[]});
                        $('#'+opts.ywiTagEid2).combobox('clear');
                    }
                }

                //标签
                if(opts.ywiTagEid){
                    $('#'+opts.ywiTagEid).html('');
                    if(newVal){
                        $.get(opts.basePath+'/admin/yunwei/tag/srchList', {type:opts.ywiTagClass, cultid:newVal}, function(data){
                            if($.isArray(data)){
                                var _values = [];
                                if(opts.ywiTagValue && opts.ywiTagValue != ''){
                                    _values = opts.ywiTagValue.split(',');
                                }
                                var _type = 'radio', __cls = '';
                                if ($('#'+opts.ywiTagEid).hasClass("checkbox")){
                                    _type = "checkbox";
                                    __cls = 'class="styled"';
                                }
                                var html = '';
                                for(var i=0; i<data.length; i++){
                                    var row = data[i];
                                    var _name = $('#'+opts.ywiTagEid).attr('name');
                                    var ischecked = $.inArray(row.id, _values) > -1 ? ' checked="checked"' : '';
                                    html += '<input type="'+_type+'" id="'+_name+"_"+row.id+'" name="'+_name+'" value="'+row.id+'" '+__cls+'  '+ischecked+'>';
                                    html += '<label for="'+_name+"_"+row.id+'">'+row.name+'</label>';
                                }
                                $('#'+opts.ywiTagEid).html(html);
                            }
                        });
                    }
                }
                //如果有外部onchange事件
                if($.isFunction(opts.cultOnChange)){
                    opts.cultOnChange(newVal, oldVal);
                }
            }

            //配置文化馆
            if(opts.cultEid){
                var editable = opts.cultEditable;
                var cultValue = '';
                   if (opts.allcult == true) {  //此处因为总分馆 屏蔽掉 列表选全部
                    $('#' + opts.cultEid).combobox({
                        //limitToList: true,
                        valueField: 'id',
                        textField: 'text',
                        editable: false,
                        data: WhgComm.getMgrCults(),
                        onChange: changeCult,
                        novalidate: true
                    });
                    cultValue = opts.cultValue;
                 } else {
                    $('#' + opts.cultEid).combobox({
                        //limitToList: true,
                        valueField: 'id',
                        textField: 'text',
                        editable: false,
                        data: WhgComm.getMgrCults(),
                        onChange: changeCult,
                        novalidate: true
                    });
                    cultValue = opts.cultValue || WhgComm.getMgrCultsFirstId();
                  }
                _init_cult_value=cultValue;
                $('#'+opts.cultEid).combobox('setValue', cultValue);
            }


            function ajaxCall(_Eid, _Val, _Cls, newVal){
                $('#'+_Eid).html('');
                if(newVal){
                    $.get(opts.basePath+'/admin/yunwei/type/srchList', {type:_Cls, cultid:newVal}, function(data){
                        if($.isArray(data)){
                            var _values = [];
                            if(_Val && _Val != ''){
                                _values = _Val.split(',');
                            }
                            var html = '';
                            var _type = "radio";
                            var __cls = "";
                            if ($('#'+_Eid).hasClass("checkbox")){
                                _type = "checkbox";
                                __cls = 'class="styled"';
                            }
                            for(var i=0; i<data.length; i++){
                                var row = data[i];
                                var _name = $('#'+_Eid).attr('name');
                                var ischecked = $.inArray(row.id, _values) > -1 ? ' checked="checked"' : '';
                                if(_values.length == 0 && i==0 && _type=="radio"){
                                    ischecked = ' checked="checked"';
                                }
                                html += '<input type="'+_type+'" id="'+_name+"_"+row.id+'" name="'+_name+'" value="'+row.id+'" '+__cls+' '+ischecked+'>';
                                html += '<label for="'+_name+"_"+row.id+'">'+row.name+'</label>';
                            }
                            $('#'+_Eid).html(html);
                        }
                    });
                }
            }


        }// end initPMS
    }
})();