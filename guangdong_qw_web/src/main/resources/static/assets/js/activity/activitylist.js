/**
 * Created by JackZeng on 2017/06/09.
 */
var dataInit = {
	option:{
		fn:{},
		params : {},
        type:"POST",
	},
	ajax:function(options){
		var m = this;
        var opt = options || {}; //$.extend({}, m.option, options || {});
		$.ajax({
			url: opt.api,
			data: opt.params || m.option.params,
			type: opt.type || m.option.type,
			dataType: 'json',
			timeout: 1000,
			cache: false,
			beforeSend: m.LoadFunction,
			error: m.erryFunction,
			success: function (data) {
                if (opt.fn && $.isFunction(opt.fn)){
                	console.info(data);
                    opt.fn(data);
				}
				//console.log(data);
			}
		});
	},
	LoadFunction:function(){
			rongDialog.init({
				ico : 3,
				type : 1,
				showTime: 300,
				closeTime: 300,
				overTime: 100,
				desc : '数据加载中'
			});
	},
	erryFunction:function(){
		rongDialog.init({
			ico : 2,
			type : 1,
			desc : '数据加载失败'
		});
	}
}
