/**
 * Created by LENCON on 2016/11/11.
 */
$(document).ready(function () {
  $('.openCurriculum').on('click',function(){
		rongDetailDialog('.js__cardA_popup');
	});

	$(".tab li").click(function() {
		$(this).addClass("active").siblings().removeClass('active');
		$(".list1").eq($(".tab li").index(this)).addClass("on").siblings().removeClass('on');
	})

	//立即报名按钮点击事件
    $('#showbm').on('click', function(){
    	var trainId= $(this).attr("trainId");
        var url = basePath+"/agdpxyz/checkTrainApply.do?";
        // $("#showbm").css("display","none");
        // $("#hiddenbm").css("display","");
        $.ajax({
            type: 'POST',
            url: url,
            data: {"trainId":trainId},
            dataType:'json',
            success: function (resultCode) {
                if ("001" == resultCode) {
                    var loginUrl = basePath+"/login";
                    window.location.href = loginUrl;
                } else if ("100" == resultCode) {
                    rongDialog({ type : true, title : "培训已下架,请选择其他培训", time : 3*1000 });
                }else if ("101" == resultCode) {
                    rongDialog({ type : true, title : "培训报名已结束,请选择其他培训", time : 3*1000 });
                }else if ("102" == resultCode) {
                    rongDialog({ type : true, title : "培训报名名额已满,请选择其他培训", time : 3*1000 });
                }else if ("103" == resultCode) {
                    rongDialog({ type : true, title : "您已报名了该培训,请不要重复报名", time : 3*1000 });
                }else if ("205" == resultCode) {
                    rongAlertDialog({ title: "提示信息", desc : "您还未绑定手机，请先去绑定手机！", closeBtn : false, icoType : 1 }, function(){
                        window.location.href = basePath+"/center/safely-phone";
                    });
                    //rongDialog({ type : true, title : "请绑定手机", time : 3*1000 });
                }else if ("206" == resultCode) {
                    rongDialog({ type : true, title : "您报名的培训和已报名的培训课程时间有冲突", time : 3*1000 });
                }else if ("104" == resultCode) {
                    //rongDialog({ type : true, title : "您还未进行实名制验证,请先实名制", time : 3*1000 });
                    rongAlertDialog({ title: "提示信息", desc : "您还未进行实名制验证,请先实名制", closeBtn : false, icoType : 1 }, function(){
                        window.location.href = basePath+"/center/safely-userReal";
                    });
                }else if ("106" == resultCode) {
                    rongDialog({ type : true, title : "您已经报名了多场培训，不可以再报名", time : 3*1000 });
                }else{
                    window.location.href=basePath+"/agdpxyz/toTrainApply.do?trainId="+trainId;
				}
            }
        })
    })
});