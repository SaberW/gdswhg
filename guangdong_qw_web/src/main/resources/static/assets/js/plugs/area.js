/** 省 */
var __PROVINCE = [/*{"name": "广东省", "id": "11"}*/];
/** 市 */
var __CITY = {};//[/*{"proid": "11", "name": "市辖区", "proname": "北京市", "id": "1101"}*/];
/** 区 */
var __AREA = {};//[/*{"cityname": "市辖区", "name": "东城区", "id": "110101", "cityid": "1101"}*/];
/** 接口地址 */
var apiPath = __api_path_root+'admin/yunwei/area/findProvinceCityArea';

/** 加载省数据 */
$.ajax({
    url: apiPath,
    type: 'GET',
    async: false,
    dataType: 'jsonp',
    jsonp: "callback",
    success: function (data) {
        var items = [];
        if($.isArray(data)){
            for(var i=0; i<data.length; i++){
                items.push({"name":data[i].name, "id":data[i].id});
            }
            __PROVINCE = items;
        }
    }
});

/** 获取省数据 */
function getProvinceData(){
    return __PROVINCE;
}

/**
 * 异步获取城市数组
 * @param province 省名称
 * @param fn 回调函数, 参数data为城市数组: function processCity(cityArr){}
 */
function getCityData(province, fn){
    if(province){
        if(typeof(__CITY[''+province+'']) != "undefined" && __CITY[''+province+''].length){
            if($.isFunction(fn)){
                fn(__CITY[''+province+'']);
            }
        }else{
            __CITY[''+province+''] = [];
            $.ajax({
                url: apiPath,
                type: 'GET',
                async: false,
                dataType: 'jsonp',
                jsonp: "callback",
                data: {area: province},
                success: function (data) {
                    var items = [];
                    if($.isArray(data)){
                        for(var i=0; i<data.length; i++){
                            items.push({"name":data[i].name, "id":data[i].id});
                        }
                        __CITY[''+province+''] = items;
                    }
                    if($.isFunction(fn)){
                        fn(items);
                    }
                }
            });
        }
    }
    return __CITY[''+province+''];
}

/**
 * 异步获取城市数组
 * @param city 城市名称
 * @param fn 回调函数, 参数data为区域数组: function processArea(areaArr){}
 */
function getAreaData(city, fn){
    if(typeof(__AREA[''+city+'']) != "undefined"){
        if($.isFunction(fn)){
            fn(__AREA[''+city+'']);
        }
    }else{
        __AREA[''+city+''] = [];
        $.ajax({
            url: apiPath,
            type: 'GET',
            async: false,
            dataType: 'jsonp',
            jsonp: "callback",
            data: {area: city},
            success: function (data) {
                var items = [];
                if($.isArray(data)){
                    for(var i=0; i<data.length; i++){
                        items.push({"name":data[i].name, "id":data[i].id});
                    }
                    __AREA[''+city+''] = items;
                }
                if($.isFunction(fn)){
                    fn(items);
                }
            }
        });
    }
    return __AREA[''+city+''];
}

/** 根据省加载市数据 */
function changeProvince(newVal, oldVal, fn){
    var citys = [];
    if(newVal){
        citys = getCityData(newVal, function(cityArr){
            if($('#__CITY_ELE').size() > 0){
                $('#__CITY_ELE').combobox('loadData', cityArr);
                $('#__CITY_ELE').combobox('setValue', '');
            }
            if($('#__AREA_ELE').size() > 0){
                $('#__AREA_ELE').combobox('loadData', []);
                $('#__AREA_ELE').combobox('setValue', '');
            }
            if($.isFunction(fn)){
                fn(cityArr);
            }
        });
    }
    return citys;
}

/** 根据市加载区数据 */
function changeCity(newVal, oldVal, fn){
    var areas = [];
    if(newVal){
        areas = getAreaData(newVal, function(areaArr){
            if($('#__AREA_ELE').size() > 0) {
                $('#__AREA_ELE').combobox('loadData', areaArr);
                $('#__AREA_ELE').combobox('setValue', '');
            }
            if($.isFunction(fn)){
                fn(areaArr);
            }
        });
    }
    return areas;
}