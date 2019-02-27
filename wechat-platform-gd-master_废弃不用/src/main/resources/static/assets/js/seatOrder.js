var seatSplitter = '-';
function prepareSeat(paramMap, paramLabel, paramMax,seatSizeUser, splitter) {
    var map = paramMap ? paramMap : [
        [0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0],
        [1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1],
        [1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1],
        [1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1],
        [1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1],
        [1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1],
        [0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0]
    ];

    var mapType = paramLabel ? paramLabel : [
        ['1-1', '2-1', '3-1', '4-1', '5-1', '6-1', '7-1', '8-1', '9-1', '10-1', '11-1', '12-1', '13-1', '14-1', '15-1', '16-1', '17-1', '18-1', '19-1', '20-1', '21-1', '22-1'],
        ['1-1', '2-1', '3-1', '4-1', '5-1', '6-1', '7-1', '8-1', '9-1', '10-1', '11-1', '12-1', '13-1', '14-1', '15-1', '16-1', '17-1', '18-1', '19-1', '20-1', '21-1', '22-1'],
        ['1-1', '2-1', '3-1', '4-1', '5-1', '6-1', '7-1', '8-1', '9-1', '10-1', '11-0', '12-0', '13-0', '14-1', '15-1', '16-1', '17-1', '18-1', '19-1', '20-1', '21-1', '22-1'],
        ['1-1', '2-1', '3-1', '4-1', '5-1', '6-1', '7-1', '8-1', '9-1', '10-1', '11-1', '12-0', '13-1', '14-1', '15-1', '16-1', '17-1', '18-1', '19-1', '20-1', '21-1', '22-1'],
        ['1-1', '2-1', '3-0', '4-1', '5-1', '6-1', '7-1', '8-1', '9-1', '10-1', '11-1', '12-0', '13-0', '14-0', '15-1', '16-1', '17-1', '18-1', '19-1', '20-1', '21-1', '22-1'],
        ['1-1', '2-1', '3-1', '4-1', '5-0', '6-0', '7-1', '8-1', '9-1', '10-1', '11-1', '12-1', '13-1', '14-1', '15-1', '16-1', '17-1', '18-1', '19-1', '20-0', '21-0', '22-0'],
        ['1-1', '2-1', '3-1', '4-1', '5-0', '6-0', '7-1', '8-1', '9-1', '10-1', '11-1', '12-1', '13-1', '14-1', '15-1', '16-1', '17-1', '18-1', '19-1', '20-1', '21-1', '22-1']
    ];

    var maxCount = paramMax ? paramMax : 5;
    if (splitter)
        seatSplitter = splitter;
    var seatSizeUser = seatSizeUser;
    var option = {
        map: map,      //座位地图
        mapType: mapType,  //座位号和状态
        seatMax: maxCount,  //票数
        seatSizeUser: seatSizeUser  //票数
    };
    initSeat(option);
}

function prepareMap(data) {
    var map = new Array();
    for (var i = 0; i < data.length; i++) {
        for (var j = 0; j < data[i].length; j++) {
            var col = new Array();
            col.push();
        }
    }
}

//新建场地
function initSeat(option) {
    var html = '<div class="led-bg"></div><div class="seat-item-container"></div>';
    $('#seat').append(html);
    $('#seat-on-message').prepend('<span class="msg">您尚未选座</span>');
    analytical_map(option);
    analytical_map_type(option);
    mui('.seat-item-container').on('tap', 'ul li', function (event) {
        var seatNo = $(this).attr('data-no').split(seatSplitter);
        if ($(this).attr('data-type') == 'Y' && !$(this).hasClass('selected')) {
            if ($('#seat .seat-item-container ul').children('.selected').length >= (option.seatMax - option.seatSizeUser)) {
                mui.toast('每人限订' + option.seatMax + '张');
            } else {
                $('#seat-on-message').prepend('<span class="seat-list" data-val="'+$(this).attr('data-no')+'">' + seatNo[1] + '</span>');
                $(this).addClass('selected');
            }
        } else {
            $('#seat-on-message span[data-val='+$(this).attr('data-no')+']').remove();
            $(this).removeClass('selected');
        }
        if ($('#seat-on-message').children('span').length > 0) {
            $('#seat-on-message .msg').remove()
        } else {
            $('#seat-on-message').prepend('<span class="msg">您尚未选座</span>');
        }
    });
}

//获得场地宽度让它是否可滚动
function initSeatWidth(liNum) {
    var seatWidth = liNum * 39;
    if (liNum > 23) {
        $('#seat').parents(".seat").css({
            "overflow-y": "scroll"
        });
    }
    $('#seat').width(seatWidth + 100);
}

//解析座位信息
function analytical_map(option) {
    var mapList = option.map;
    var html = "";
    var html2 = "";
    var seatMapCount = 0;
    var rowCount = mapList.length;
    for (var i = 0; i < rowCount; i++) {
        html += "<ul>";
        html2 += "<li>" + (i + 1) + "</li>";
        for (var j = 0; j < mapList[i].length; j++) {
            var className = "available";
            if (mapList[i][j] == "2" || mapList[i][j] == "4")
                className = "unavailable";
            else if (mapList[i][j] == "3")
                className = "del";
            html += "<li class='" + className + "'></li>";
            seatMapCount++;
        }
        html += "</ul>";
    }
    $('#seatWrapper').find('.row-num ul').append(html2);
    $('#seat').find('.seat-item-container').append(html);
    //渲染座位场地DIV的宽度
    seatMapCount = seatMapCount / rowCount;
    initSeatWidth(seatMapCount);
}

//解析座位状态
/*function analytical_map_type(option) {
    var mapItemType = option.mapType;
    for (var i = 0; i < mapItemType.length; i++) {
        //排数
        var rowNum = i + 1;
        for (var j = 0; j < mapItemType[i].length; j++) {
            var dataNo = mapItemType[i][j];
            var className = "";
            var $li = $('.seat-item-container ul:eq(' + i + ') li:eq(' + j + ')');
            //加Y 表示可预定
            if ($li.hasClass('unavailable') || $li.hasClass('del')) {
                $li.attr('data-type', 'N');
            }
            else {
                $li.attr('data-type', 'Y');
            }
            if (className != "") {
                $li.addClass(className);
            }
            $li.attr('data-no', dataNo);
        }
    }
}*/
//解析座位状态
function analytical_map_type(option) {
    var mapItemType = option.mapType;
    for (var i = 0; i < mapItemType.length; i++) {
        //排数
        var rowNum = i + 1;
        for (var j = 0; j < mapItemType[i].length; j++) {
            var mapItem = mapItemType[i][j].split('-');
            var dataNo = rowNum + '-' + mapItem[0];
            var className = "";
            var $li = $('.seat-item-container ul:eq(' + i + ') li:eq(' + j + ')');
            //加Y 表示可预定
            if($li.hasClass('del')){
                $li.attr('data-type', 'N');
            }else{
                $li.attr('data-type', 'Y');
            }
            if (mapItem[1] == '3') {
                className = "unavailable";
                //加N  表示已经被占
                $li.attr('data-type', 'N');
            } else if (mapItem[1] == '2'||mapItem[1] == '4') {
                className = "unavailable";
                // className = "unavailable";
                $li.attr('data-type', 'N');
            }
            if (className != "") {
                $li.addClass(className);
            }
            $li.attr('data-no', dataNo);
        }
    }
}