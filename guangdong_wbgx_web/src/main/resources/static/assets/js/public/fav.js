/*! Rong voteup Plugin
 * http://hn.creatoo.cn/
 * JackyZeng zrongs@vip.ccom*/

var fav = {
    add: function (option) {
        var m = this;
        if (option.userId == null) {
            rongDialog.init({
                ico: 2,
                type: 1,
                desc: '请先登录'
            })
        } else {
            var params = {
                cmuid:option.userId,
                cmrefid:option.itemId,
                cmurl:option.refurl,
                toproject:option.toproject,
                cmopttyp:$("#commType").val(),
                cmreftyp:option.type //活动1 场馆2 活动室3
            }
            dataInit.ajax({
                api: option.api,  //api1 添加  api2 删除 api3 查询
                params: params,
                fn: function (data) {
                    if(data.code == "0"){
                        rongDialog.init({
                            ico: 1,
                            type: 1,
                            desc: "收藏成功"
                        })
                    }else{
                        rongDialog.init({
                            ico: 2,
                            type: 1,
                            desc: data.errMsg || "收藏异常"
                        })
                    }
                }
            })
        }
    },
    del:function (option) {
        var m = this;
        if (option.userId == null) {
            rongDialog.init({
                ico: 2,
                type: 1,
                desc: '请先登录'
            })
        } else {
            var params = {
                cmuid:option.userId,
                cmrefid:option.itemId,
                cmreftyp:option.type //活动1 场馆2 活动室3
            }
            dataInit.ajax({
                api: option.api,
                params: params,
                fn: function (data) {
                    if(data.code == 0){
                        rongDialog.init({
                            ico: 1,
                            type: 1,
                            desc: "收藏已取消"
                        })
                        $("#collection").addClass("icon-collection");
                    }else{
                        rongDialog.init({
                            ico: 2,
                            type: 1,
                            desc: data.errMsg || "收藏异常"
                        })
                    }
                }
            })
        }
    },
    init:function(option){
        var params = {
            userId : option.userId,
            cmrefid : option.cmrefid,
            cmreftyp : option.cmreftyp //活动4 场馆2 活动室3
        }
        dataInit.ajax({
            api:option.api,
            params:params,
            fn:function(data){
                $("#commType").val(data.data.success);
                $("#collection").next("span").html(data.data.scNum);
                if(option.userId && data.data.success == '1'){
                    $("#collection").removeClass("icon-collection");
                }
            }
        })
    }
}