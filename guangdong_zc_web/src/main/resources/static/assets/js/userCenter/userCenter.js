$(document).ready(function(e) {
  $('.zcproject').sly({
    itemNav: "centered",
    easing: "easeOutExpo",
    prev: ".prev",
    next: ".next",
    cycleBy: 'items',
    cycleInterval: 2000,
    speed: 400,
    horizontal: 1,
    touchDragging: 1,
    dragContent: 1,
	scrollBar:".scrollbar"
  });
})