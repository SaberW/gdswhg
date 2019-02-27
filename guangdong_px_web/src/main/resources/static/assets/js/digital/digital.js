function renderMyList() {
    $('.container .list li div.img').mouseover(function () {
        $(this).children('.title').stop(1, 0).fadeOut(1000);
        $(this).children('.detail').stop(1, 0).fadeIn(500);
    });
    $('.container .list li div.img').mouseout(function () {
        $(this).children('.title').stop(1, 0).fadeIn(500);
        $(this).children('.detail').stop(1, 0).fadeOut(1000);
    });

    $('.container .detail li').mouseover(function () {
        $(this).find('.detail').stop(1, 0).animate({height:"185px"});
        $(this).find('.mask').stop(1, 0).fadeIn(1000);
        $(this).find('p').stop(1, 0).css("height","72px");
        // $(this).find('h2').stop(1, 0).css("color","#fff");
        // $(this).find('p').stop(1, 0).css("color","#fff");
        $(this).find('i').stop(1, 0).css("display","block");
    });
    $('.container .detail li').mouseout(function () {
        $(this).find('.detail').stop(1, 0).animate({height:"84px"});
        $(this).find('.mask').stop(1, 0).fadeOut(1000);
        $(this).find('p').stop(1, 0).css("height","24px");
        // $(this).find('h2').stop(1, 0).css("color","#333");
        // $(this).find('p').stop(1, 0).css("color","#666");
        $(this).find('i').stop(1, 0).css("display","none");
    });

    $('.container .venue-list .lists ul li div.img').mouseover(function () {
        $(this).children('.title').stop(1, 0).fadeOut(1000);
        $(this).children('.detail').stop(1, 0).fadeIn(500);
    });
    $('.container .venue-list .lists ul li div.img').mouseout(function () {
        $(this).children('.title').stop(1, 0).fadeIn(500);
        $(this).children('.detail').stop(1, 0).fadeOut(1000);
    });
}

function renderMyListForReading() {
    $('.list-3-type-1 .list li div.img').mouseover(function () {
        $(this).children('.title').stop(1, 0).fadeOut(1000);
        $(this).children('.detail').stop(1, 0).fadeIn(500);
    });
    $('.list-3-type-1 .list li div.img').mouseout(function () {
        $(this).children('.title').stop(1, 0).fadeIn(500);
        $(this).children('.detail').stop(1, 0).fadeOut(1000);
    });

    $('.list-3-type-1 .detail li').mouseover(function () {
        $(this).find('.detail').stop(1, 0).animate({height:"185px"});
        $(this).find('.mask').stop(1, 0).fadeIn(1000);
        $(this).find('p').stop(1, 0).css("height","72px");
        // $(this).find('h2').stop(1, 0).css("color","#fff");
        // $(this).find('p').stop(1, 0).css("color","#fff");
        $(this).find('i').stop(1, 0).css("display","block");
    });
    $('.list-3-type-1 .detail li').mouseout(function () {
        $(this).find('.detail').stop(1, 0).animate({height:"84px"});
        $(this).find('.mask').stop(1, 0).fadeOut(1000);
        $(this).find('p').stop(1, 0).css("height","24px");
        // $(this).find('h2').stop(1, 0).css("color","#333");
        // $(this).find('p').stop(1, 0).css("color","#666");
        $(this).find('i').stop(1, 0).css("display","none");
    });

    $('.list-3-type-1 .venue-list .lists ul li div.img').mouseover(function () {
        $(this).children('.title').stop(1, 0).fadeOut(1000);
        $(this).children('.detail').stop(1, 0).fadeIn(500);
    });
    $('.list-3-type-1 .venue-list .lists ul li div.img').mouseout(function () {
        $(this).children('.title').stop(1, 0).fadeIn(500);
        $(this).children('.detail').stop(1, 0).fadeOut(1000);
    });
}

$(document).ready(function(e) {
    renderMyList();
});