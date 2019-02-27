!function(o) {
    o.fn.toTop = function(t) {
        var e = this,
            i = o(window),
            s = o("html, body"),
            n = o.extend({
                    autohide: !0,
                    offset: 120,
                    speed: 100,
                    right: 0,
                    bottom: 60
                },
                t);
        e.css({
            position: "fixed",
            right: n.right,
            bottom: n.bottom,
            cursor: "pointer"
        }),
        n.autohide && e.css("display", "none"),
            e.click(function() {
                s.animate({
                        scrollTop: 0
                    },
                    n.speed)
            }),
            i.scroll(function() {
                var o = i.scrollTop();
                n.autohide && (o > n.offset ? e.fadeIn(n.speed) : e.fadeOut(n.speed))
            })
    }
} (jQuery);