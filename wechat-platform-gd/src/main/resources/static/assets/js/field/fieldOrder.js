/**
 * Created by Zengrong on 2016/11/03 0017.
 */
jQuery(document).ready(function(e) {
    jQuery('.dateChange').on('click',
        function() {
            laydate({
                elem: '#dateCont'
            });
        });
    jQuery('.week-next').on('click',
        function() {
            initWeekList(nextWeek());
        });
    jQuery('.week-prev').on('click',
        function() {
            initWeekList(prevWeek());
        });
    jQuery('.week-groups ul').delegate('li', 'click',
        function() {
            jQuery(this).addClass('active').siblings().removeClass('active');
        });
    jQuery('.place i').mouseover(function(){
        jQuery(this).children('.infoMianCont').stop(0,1).show();
    });
    jQuery('.place i').mouseout(function(){
        jQuery(this).children('.infoMianCont').stop(0,1).fadeOut(300);
    });

    jQuery(".tab li").click(function() {
        jQuery(this).addClass("active").siblings().removeClass('active');
        jQuery(".list1").eq(jQuery(".tab li").index(this)).addClass("on").siblings().removeClass('on');
    })


})

function initWeekList(onToday) {

    var today = new Array('星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六');
    var htmlVal = "";
    var weekDay = onToday ? onToday: new Date();
    var year =  weekDay.getFullYear();
    var month = weekDay.getMonth();
    var date = weekDay.getDate();
    for (var i = 0; i < 4; i++) {
        weekDay.setYear(year);
        weekDay.setMonth(month);
        weekDay.setDate(date);
        thisYear = weekDay.getFullYear();
        thisMonth = weekDay.getMonth()+1;
        thisDay = weekDay.getDate();
        var result = today[weekDay.getDay()];
        var active = i == 0 ? 'class="active"': '';
        //alert(today[weekDay.getDay()]+'-'+parseInt(weekDay.getDay()) );
        htmlVal += '<li ' + active + '>' + '<h4>' + result + '</h4>' + '<p>' + thisYear + '-' + thisMonth + '-' + thisDay + '</p>' + '<div class="day-border"></div>' + '</li>';
        date++;
    }
    jQuery('.week-groups ul').html(htmlVal);
}

function nextWeek() {
    var thisDate = jQuery('.week-groups ul li p:first').html();
    var str = thisDate.split('-');
    var date = new Date(str[0],str[1]-1,str[2]);
    date.setDate(date.getDate() + 4);
    return date;

}

function prevWeek() {
    var thisDate = jQuery('.week-groups ul li p:first').html();
    var str = thisDate.split('-');
    var date = new Date(str[0],str[1]-1,str[2]);
    date.setDate(date.getDate() - 4);
    return date;
}