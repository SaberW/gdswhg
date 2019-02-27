//jquery index 1.0
//create by zengrong (zrongs@vip.qq.com)

$(document).ready(function (e) {


    $(".tab ul li").click(function () {
        $(this).addClass("active").siblings().removeClass('active');
        $(".detail").eq($(".tab ul li").index(this)).addClass("on").siblings().removeClass('on');
    })

    $('.union-cont').sly({
        itemNav: "basic",
        easing: "easeOutExpo",
        pagesBar: ".u-nav",
        pageBuilder: function (dom) {
            return "<span></span>";
        },
        horizontal: 1
    });

});