/*! Rong voteup Plugin
 * http://hn.creatoo.cn/
 * JackyZeng zrongs@vip.ccom*/

var voteup = {
    add: function (option) {
        var m = this;
        if (option.userId == null) {
            rongDialog.init({
                ico: 2,
                type: 1,
                desc: '请先登录'
            })
        } else if (option.cmrefid == null) {
            rongDialog.init({
                ico: 2,
                type: 1,
                desc: '参数错误'
            })
        } else {
            var userId = option.userId;
            var refid = option.cmrefid;
            var reftyp = option.cmreftyp;
            dataInit.ajax({
                api: option.api2,
                params: {
                    userId: userId,
                    cmrefid: refid,
                    cmreftyp: reftyp
                },
                fn: function (data) {
                    if(data.data.success == "0"){
                        rongDialog.init({
                            ico: 1,
                            type: 1,
                            desc: '赞 +1',
                            nextFn:function(){
                                m.init({
                                    api1 : option.api1,
                                    userId : userId,
                                    dom:"#good",
                                    cmrefid: refid,
                                    cmreftyp: reftyp
                                })
                            }
                        })
                        /*m.init({
                            api1 : option.api1,
                            userId : userId,
                            dom:"#good",
                            cmrefid: refid,
                            cmreftyp: reftyp
                        })*/
                    }else{
                        rongDialog.init({
                            ico: 2,
                            type: 1,
                            desc: data.errMsg || "点赞异常"
                        })
                    }
                }
            })
        }
    },
    init:function(option){
        var reftyp = option.cmreftyp;
        var refid = option.cmrefid;
        var userId = option.userId;
        // $(option.dom).next("span").html("0");
        dataInit.ajax({
            api:option.api1,
            params:{
                userId:userId,
                cmreftyp:reftyp,
                cmrefid:refid
            },
            fn:function(data){
                if(data.data.success != "2"){
                    if(data.data.success == "1"){
                        $(option.dom).removeClass("icon-praise");
                    }
                    $(option.dom).next("span").html(data.data.num);
                }else{
                    rongDialog.init({
                        ico: 2,
                        type: 1,
                        desc: '点赞数据异常'
                    })
                }
            }
        })
    }
}