/**
 * 添加|编辑 群文资源
 * Created by wangxl on 2017/11/2.
 */
WhgCustomComm = (function () {
    return {
        /**
         * 根据容器元素标识和文化馆标识，生成艺术分类
         * @param containerEleId 容器元素标识
         * @param cultid 文化馆标识
         * @param vals 初始值
         */
        createArtTypeHtml: function (containerEleId, cultid, vals) {
            $('#'+containerEleId).empty();
            $.get(basePath+'/admin/yunwei/type/srchList', {type:1, cultid:cultid}, function(data){
                if($.isArray(data)){
                    var _values = [];
                    if(vals && vals != ''){
                        _values = vals.split(',');
                    }
                    var _type = 'checkbox';
                    var html = '';
                    for(var i=0; i<data.length; i++){
                        var row = data[i];
                        var _name = '';
                        var ischecked = $.inArray(row.id, _values) > -1 ? ' checked="checked"' : '';
                        html += '<label style="margin-right: 6px;"><input type="'+_type+'" name="resarttype" value="'+row.id+'"'+ischecked+'>'+row.name+'</label>';
                    }
                    $('#'+containerEleId).html(html);
                }
            });
        }

        /**
         * 设置艺术分类的值
         * @param val 被设置的值
         */
        ,setArtTypeVal: function (vals) {
            var _values = [];
            if(vals && vals != ''){
                _values = vals.split(',');
            }
            $(':checkbox[name="resarttype"]').attr("checked",false);
            for(var i=0; i<_values.length; i++){
                $(':checkbox[name="resarttype"][value="'+_values[i]+'"]').attr("checked",true);
            }
        }

        /**
         * 根据容器元素标识和文化馆标识，生成艺术分类
         * @param containerEleId 容器元素标识
         * @param cultid 文化馆标识
         * @param vals 初始值
         */
        ,createTagHtml: function (containerEleId, cultid, vals) {
            $('#'+containerEleId).empty();
            $.get(basePath+'/admin/yunwei/tag/srchList', {type:53, cultid:cultid}, function(data){
                if($.isArray(data)){
                    var _values = [];
                    if(vals && vals != ''){
                        _values = vals.split(',');
                    }
                    var _type = 'checkbox';
                    var html = '';
                    for(var i=0; i<data.length; i++){
                        var row = data[i];
                        var _name = 'restag';
                        var ischecked = $.inArray(row.id, _values) > -1 ? ' checked="checked"' : '';
                        html += '<label style="margin-right: 6px;"><input type="'+_type+'" name="restag" value="'+row.id+'"'+ischecked+'>'+row.name+'</label>';
                    }
                    $('#'+containerEleId).html(html);
                }
            });
        }

        /**
         * 设置界面中标签的值
         * @param vals
         */
        ,setTagVal: function (vals) {
            var _values = [];
            if(vals && vals != ''){
                _values = vals.split(',');
            }
            $(':checkbox[name="restag"]').attr("checked",false);
            for(var i=0; i<_values.length; i++){
                $(':checkbox[name="restag"][value="'+_values[i]+'"]').attr("checked",true);
            }
        }
    };
})();