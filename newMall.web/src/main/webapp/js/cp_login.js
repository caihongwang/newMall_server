(function (F) {
    function l(b) {
        return "'" + b.replace(/('|\\)/g, "\\$1").replace(/\r/g, "\\r").replace(/\n/g, "\\n") + "'"
    }

    function j(b, a) {
        function c(a) {
            j += a.split(/\n/).length - 1;
            o && (a = a.replace(/\s+/g, " ").replace(/<\!--[\w\W]*?--\>/g, ""));
            a && (a = s[1] + l(a) + s[2] + "\n");
            return a
        }

        function i(c) {
            var b = j;
            f ? c = f(c, a) : p && (c = c.replace(/\n/g, function () {
                    j++;
                    return "$line=" + j + ";"
                }));
            if (c.indexOf("=") === 0) {
                var e = t && !/^=[=#]/.test(c),
                    c = c.replace(/^=[=#]?|[\s;]*$/g, "");
                e ? (e = c.replace(/\s*\([^\)]+\)/, ""), !h[e] && !/^(include|print)$/.test(e) &&
                (c = "$escape(" + c + ")")) : c = "$string(" + c + ")";
                c = s[1] + c + s[2]
            }
            p && (c = "$line=" + b + ";" + c);
            k(c.replace(v, "").replace(E, ",").replace(r, "").replace(g, "").replace(A, "").split(B), function (a) {
                a && !u[a] && (q += a + "=" + (a === "print" ? x : a === "include" ? J : h[a] ? "$utils." + a : w[a] ? "$helpers." + a : "$data." + a) + ",", u[a] = !0)
            });
            return c + "\n"
        }

        var p = a.debug, d = a.closeTag, f = a.parser, o = a.compress,
            t = a.escape, j = 1,
            u = {
                $data: 1,
                $filename: 1,
                $utils: 1,
                $helpers: 1,
                $out: 1,
                $line: 1
            }, m = "".trim,
            s = m ? ["$out='';", "$out+=", ";", "$out"] : ["$out=[];", "$out.push(", ");",
                "$out.join('')"],
            m = m ? "$out+=text;return $out;" : "$out.push(text);",
            x = "function(){var text=''.concat.apply('',arguments);" + m + "}",
            J = "function(filename,data){data=data||$data;var text=$utils.$include(filename,data,$filename);" + m + "}",
            q = "'use strict';var $utils=this,$helpers=$utils.$helpers," + (p ? "$line=0," : ""),
            e = s[0],
            m = "return new String(" + s[3] + ");";
        k(b.split(a.openTag), function (a) {
            var a = a.split(d), b = a[0], x = a[1];
            a.length === 1 ? e += c(b) : (e += i(b), x && (e += c(x)))
        });
        m = q + e + m;
        p && (m = "try{" + m + "}catch(e){throw {filename:$filename,name:'Render Error',message:e.message,line:$line,source:" +
            l(b) + ".split(/\\n/)[$line-1].replace(/^\\s+/,'')};}");
        try {
            var P = new Function("$data", "$filename", m);
            P.prototype = h;
            return P
        } catch (n) {
            throw n.temp = "function anonymous($data,$filename) {" + m + "}", n;
        }
    }

    var f = function (b, a) {
        return typeof a === "string" ? o(a, {filename: b}) : C(b, a)
    };
    f.version = "3.0.0";
    f.config = function (b, a) {
        t[b] = a
    };
    var t = f.defaults = {
            openTag: "<%",
            closeTag: "%>",
            escape: !0,
            cache: !0,
            compress: !1,
            parser: null
        },
        y = f.cache = {};
    f.render = function (b, a) {
        return o(b, a)
    };
    var C = f.renderFile = function (b, a) {
        var c = f.get(b) ||
            z({
                filename: b,
                name: "Render Error",
                message: "Template not found"
            });
        return a ? c(a) : c
    };
    f.get = function (b) {
        var a;
        if (y[b]) a = y[b]; else if (typeof document === "object") {
            var c = document.getElementById(b);
            c && (a = (c.value || c.innerHTML).replace(/^\s*|\s*$/g, ""), a = o(a, {filename: b}))
        }
        return a
    };
    var D = function (b, a) {
        typeof b !== "string" && (a = typeof b, a === "number" ? b += "" : b = a === "function" ? D(b.call(b)) : "");
        return b
    }, G = {
        "<": "&#60;",
        ">": "&#62;",
        '"': "&#34;",
        "'": "&#39;",
        "&": "&#38;"
    }, u = function (b) {
        return G[b]
    }, d = Array.isArray || function (b) {
            return {}.toString.call(b) ===
                "[object Array]"
        }, h = f.utils = {
        $helpers: {}, $include: C, $string: D, $escape: function (b) {
            return D(b).replace(/&(?![\w#]+;)|[<>"']/g, u)
        }, $each: function (b, a) {
            var c, i;
            if (d(b))for (c = 0, i = b.length; c < i; c++)a.call(b, b[c], c, b); else for (c in b)a.call(b, b[c], c)
        }
    };
    f.helper = function (b, a) {
        w[b] = a
    };
    var w = f.helpers = h.$helpers;
    f.onerror = function (b) {
        var a = "Template Error\n\n", c;
        for (c in b)a += "<" + c + ">\n" + b[c] + "\n\n";
        typeof console === "object" && console.error(a)
    };
    var z = function (b) {
            f.onerror(b);
            return function () {
                return "{Template Error}"
            }
        },
        o = f.compile = function (b, a) {
            function c(c) {
                try {
                    return new f(c, p) + ""
                } catch (i) {
                    return !a.debug ? (a.debug = !0, o(b, a)(c)) : z(i)()
                }
            }

            var a = a || {}, i;
            for (i in t)a[i] === void 0 && (a[i] = t[i]);
            var p = a.filename;
            try {
                var f = j(b, a)
            } catch (d) {
                return d.filename = p || "anonymous", d.name = "Syntax Error", z(d)
            }
            c.prototype = f.prototype;
            c.toString = function () {
                return f.toString()
            };
            p && a.cache && (y[p] = c);
            return c
        }, k = h.$each,
        v = /\/\*[\w\W]*?\*\/|\/\/[^\n]*\n|\/\/[^\n]*$|"(?:[^"\\]|\\[\w\W])*"|'(?:[^'\\]|\\[\w\W])*'|\s*\.\s*[$\w\.]+/g,
        E = /[^\w$]+/g,
        r = RegExp("" + ("\\b" + "break,case,catch,continue,debugger,default,delete,do,else,false,finally,for,function,if,in,instanceof,new,null,return,switch,this,throw,true,try,typeof,var,void,while,with,abstract,boolean,byte,char,class,const,double,enum,export,extends,final,float,goto,implements,import,int,interface,long,native,package,private,protected,public,short,static,super,synchronized,throws,transient,volatile,arguments,let,yield,undefined".replace(/,/g, "\\b|\\b") + "\\b"), "g"),
        g = /^\d[^,]*|,\d[^,]*/g, A =
            /^,+|,+$/g, B = /^$|,+/;
    t.openTag = "{{";
    t.closeTag = "}}";
    t.parser = function (b) {
        var b = b.replace(/^\s/, ""), a = b.split(" "), c = a.shift(),
            i = a.join(" ");
        switch (c) {
            case "if":
                b = "if(" + i + "){";
                break;
            case "else":
                a = a.shift() === "if" ? " if(" + a.join(" ") + ")" : "";
                b = "}else" + a + "{";
                break;
            case "/if":
                b = "}";
                break;
            case "each":
                c = a[0] || "$data";
                b = (a[2] || "$value") + "," + (a[3] || "$index");
                (a[1] || "as") !== "as" && (c = "[]");
                b = "$each(" + c + ",function(" + b + "){";
                break;
            case "/each":
                b = "});";
                break;
            case "echo":
                b = "print(" + i + ");";
                break;
            case "print":
            case "include":
                b =
                    c + "(" + a.join(",") + ");";
                break;
            default:
                if (/^\s*\|\s*[\w\$]/.test(i)) {
                    a = !0;
                    b.indexOf("#") === 0 && (b = b.substr(1), a = !1);
                    for (var c = 0, b = b.split("|"), i = b.length, d = b[c++]; c < i; c++) {
                        var g = b[c].split(":"), h = g.shift();
                        (g = g.join(":") || "") && (g = ", " + g);
                        d = "$helpers." + h + "(" + d + g + ")"
                    }
                    b = (a ? "=" : "=#") + d
                } else b = f.helpers[c] ? "=#" + c + "(" + a.join(",") + ");" : "=" + b
        }
        return b
    };
    (function () {
        var b, a, c = "html append prepend after before".split(" ");
        for (b = 0, a = c.length; b < a; b++)f[c[b]] = function (a) {
            return function (c, b, d) {
                $(c)[a](f.compile(b)(d))
            }
        }(c[b])
    })();
    F.template = f
})(window.sg || window);
(function () {
    function F(a) {
        if (!g) {
            D = a.app_id || "";
            G = a.app_version || "";
            d = a.user_id || "";
            z = a.client_id || "";
            o = a.product_name || "";
            E = a.from || l("from") || "";
            k = a.env || "DEV";
            u = a.lc || "zh-cn";
            (w = a.get_page_id || "") && (h = w());
            v = k === "ONLINE" || k === "PRE" ? "https://logio.intsig.net/logapi/cc.gif" : "https://logio-sandbox.intsig.net/logapi/cc.gif";
            $(document).bind("click", function (a) {
                (a = $(a.target).closest("[data-stat-key]").attr("data-stat-key")) && y("click_" + a)
            });
            a = "beforeunload";
            if (/iphone|ipad|ios/i.test(navigator.userAgent) &&
                (window.onpagehide || window.onpagehide === null)) a = "pagehide";
            $(window).bind(a, function () {
                C("residence_time", {time: +new Date - r})
            });
            g = !0
        }
    }

    function l(a) {
        var c, b, d = "", g, f;
        b = location.href;
        b = b.replace(/#.+$/, "");
        c = b.indexOf("?");
        c > -1 && (b = b.substring(c, b.length));
        if (b.substr(0, 1) === "?" && b.length > 1) {
            c = b.substring(1, b.length);
            g = c.split("&");
            for (c = 0, b = g.length; c < b; c++)if (f = g[c].split("="), f[0] === a && f.length === 2) {
                d = f[1];
                break
            }
        }
        return d
    }

    function j(a) {
        window.console && window.console.log && window.console.log(a)
    }

    function f(a) {
        if (v &&
            o && a && typeof a === "object") {
            var c = v, b = {
                appid: D,
                ui: d,
                pn: o,
                pv: G,
                ci: z,
                rf: E,
                lc: u,
                ul: location.href,
                sr: window.outerWidth + "*" + window.outerHeight,
                vp: window.innerWidth + "*" + window.innerHeight,
                t: +new Date
            }, g;
            for (g in a)a.hasOwnProperty(g) && (b[g] = a[g]);
            a = !0;
            g = "";
            for (var f in b)b.hasOwnProperty(f) && (a ? (g += "?", a = !1) : g += "&", g += encodeURIComponent(f) + "=" + encodeURIComponent(b[f]));
            c += g;
            if (!navigator.sendBeacon || !navigator.sendBeacon(c)) (new Image).src = c
        }
    }

    function t(a, c) {
        h = a || w && w() || "/";
        f({
            d: c ? JSON.stringify(c) : "",
            pi: h
        })
    }

    function y(a, c, b) {
        a ? !h && !b ? j("page_id not set") : f({
            d: c ? JSON.stringify(c) : "",
            pi: b || h,
            ai: a
        }) : j("action_id not set")
    }

    function C(a, b) {
        a ? h ? f({
            d: b ? JSON.stringify(b) : "",
            pi: h,
            ti: a
        }) : j("page_id not set") : j("trace_id not set")
    }

    var D = "", G = "", u = "", d = "", h = "", w = "", z = "", o = "",
        k = "DEV", v, E, r = +new Date, g = !1,
        A = document.getElementById("page_config");
    if (A)try {
        var A = JSON.parse(A.textContent), B = A.LOG_CONFIG;
        if (B) B.get_page_id = function () {
            return location.pathname.substr(1).split("/").join("_")
        }, F(B), B.auto_page && t()
    } catch (b) {
    }
    window.Log =
        {
            page: t, action: y, trace: C, tracePerformance: function (a) {
            window.performance && performance.timing && setTimeout(function () {
                var b = performance.timing;
                C(a || "performance_timing", {
                    readyStart: b.fetchStart - b.navigationStart,
                    redirectTime: b.redirectEnd - b.redirectStart,
                    unloadEventTime: b.unloadEventEnd - b.unloadEventStart,
                    lookupDomainTime: b.domainLookupEnd - b.domainLookupStart,
                    connectTime: b.connectEnd - b.connectStart,
                    requestTime: b.responseEnd - b.requestStart,
                    initDomTreeTime: b.domInteractive - b.responseEnd,
                    domReadyTime: b.domComplete -
                    b.domInteractive,
                    loadTime: b.loadEventEnd - b.navigationStart
                })
            }, 0)
        }, config: F
        }
})();
$(function () {
    function F() {
        --N ? $(".get_code").html(getText("resend_code") + xss(N)) : (clearInterval(m), $(".get_code").html(getText("get_vcode_first")), $(".get_code").removeClass("disable"))
    }

    function l(b, a) {
        if (a) {
            var c = getText(a);
            a === "err_loginerror" && (c += ', <a class="link" href="/user/findpwd">' + getText("title_mo_findpwd") + "</a>");
            b.html(xss(c, !1)).show()
        } else b.hide().html("")
    }

    function j(a) {
        return isappFormValidator.isEmail(a) === 0 || isappFormValidator.isMobile(a) === 0
    }

    function f(a, b) {
        function c() {
            iu.promptareacode("#area_code",
                {
                    langid: isapp.getLangId(),
                    showtext: 1,
                    sltwidth: 348,
                    btnwidth: 80,
                    top: 1,
                    left: 0,
                    margintop: -1,
                    singleHeight: 34
                });
            $(".iu_mselect_text").addClass("hidelong");
            $(".iu_mselect_button").bind("click", function () {
                h.hasClass("hidden_ele") || d.hasClass("hidden_ele") || d.addClass("hidden_ele")
            });
            $(".iu_mselect_option").bind("click", function () {
                var a = "" + $(this).attr("value");
                j = a;
                $("#area_code").text(xss(a));
                d.addClass("hidden_ele")
            })
        }

        var e = $(a), g = $(".dialog_close"), n = $(".account"),
            f = n.find(".inner_account"), d = $(".account_list"),
            M = d.find(".account_item"), i = $(".getvcode"),
            h = $("#area_code"), k = $(".iu_mselect_frame"),
            l = "+" + b.AREA_CODE, j = l, m = b.ACCOUNT,
            o = f.data("ecy_account"), p = f.data("account"),
            r = isapp.getUrlParam("language") || isapp.getLangId(),
            s = -parseInt(b.ERR_CODE, 10);
        g.bind("click", function () {
            Log.action("close_security_verify");
            i.removeClass("btn_loading_gif");
            e.remove()
        });
        n.bind("click", function () {
            d.toggleClass("hidden_ele");
            n.find("#triangle").toggleClass("iu_mselect_triangle_down").toggleClass("iu_mselect_triangle_up");
            k.hasClass("iu_mselect_selected") && k.removeClass("iu_mselect_selected")
        });
        n.data("hascode") === "0" && c();
        M.length > 0 && M.bind("click", function () {
            var a = $(this), b = a.data("type"), x = a.data("account"),
                J = a.data("ecy_account"),
                e = a.data("hascode"), a = a.data("code") || l;
            b === "email" && (n.find(".bind").removeClass("bind_right"), h.removeClass("area_code").text(""), d.addClass("hidden_ele").addClass("normal"), n.addClass("no_area_code").removeClass("has_area_code"));
            b === "phone" && (e !== 0 && a ? (n.find(".bind").removeClass("bind_right"),
                d.addClass("hidden_ele").addClass("normal"), h.addClass("hidden_ele"), n.addClass("no_area_code").removeClass("has_area_code"), j = a) : (c(), n.find(".bind").addClass("bind_right"), d.removeClass("normal"), h.removeClass("hidden_ele").addClass("area_code").text(xss(a)), n.removeClass("hidden_ele").removeClass("no_area_code").addClass("has_area_code"), $(".wrapper_left").removeClass("hidden_ele"), d.addClass("hidden_ele")));
            o = J;
            p = x;
            n.find("#triangle").toggleClass("iu_mselect_triangle_down").toggleClass("iu_mselect_triangle_up");
            n.find(".account_text").text() !== p && Log.action("change_account");
            f.data("account", x).data("ecy_account", J);
            f.find(".account_text").html(xss(x))
        });
        h.bind("click", function () {
            h.hasClass("hidden_ele") || d.hasClass("hidden_ele") || d.addClass("hidden_ele")
        });
        i.bind("click", function () {
            Log.action("get_vcode");
            e.addClass("hidden_ele");
            j = ("" + j).indexOf("+") !== 0 ? j : ("" + j).substr(1);
            var a = u("verify_modal").model({c_account: p}),
                b = {
                    params: {
                        account: m,
                        area_code: j,
                        ecy_account: o,
                        err_code: s,
                        language: r
                    }, l_account: m
                };
            Log.trace("show_input_vcode");
            $("#main").append(xss(a, !1));
            t(b)
        })
    }

    function t(a) {
        var c = $(".send"), q = $(".verify"), e = $(".dialog_back");
        y(a);
        c.bind("click", function () {
            Log.action("reget_vcode");
            if ($(this).hasClass("disable"))return !1; else $(".verify_input").val(""), y(a)
        });
        q.bind("click", function () {
            q.addClass("btn_loading_gif");
            var c = $(".verify_input").val();
            if (c && K) Log.action("done"), ajax_loading(), isappAjax({
                url: "/account/verifyvcode", data: {
                    account: a.params.account,
                    ecy_account: a.params.ecy_account,
                    vcode: c,
                    vcode_token: K,
                    next_login: o.hasClass("iu_checkbox_select") ?
                        1 : 0
                }
            }, function (b) {
                Log.trace("verify_success");
                ajax_loading_stop();
                $("#account_modal").remove();
                if (b.modify_password_token) {
                    var c = u("unsecurity_modal").model({}),
                        b = {
                            token: b.modify_password_token,
                            account: a.params.account
                        };
                    $("#verify_modal").remove();
                    Log.trace("show_dialog_changepwd");
                    $("#main").append(xss(c, !1));
                    C(b)
                } else(c = $("#redirect_url").val()) ? location.href = c : location.reload()
            }, function () {
                ajax_loading_stop();
                q.removeClass("btn_loading_gif");
                $(".verify_input").val("");
                $(".err_msg").html(getText(b))
            });
            else return q.removeClass("btn_loading_gif"), !1
        });
        $(".verify_input").focus(function () {
            $(".err_msg").html("")
        });
        e.bind("click", function () {
            Log.action("back_input_vcode");
            $("#verify_modal").remove();
            $("#account_modal").removeClass("hidden_ele")
        })
    }

    function y(b) {
        function c(b) {
            e === 0 ? (b.removeClass("disable"), b.html(getText(a)), e = 60) : (b.addClass("disable"), b.html(getText(a) + xss(e)), e--, setTimeout(function () {
                c(b)
            }, 1E3))
        }

        var q = $(".send"), e = 60;
        isappAjax({url: "/account/sendvcode", data: b.params}, function (a) {
            if (a.vcode_token) K =
                a.vcode_token
        }, function () {
            l($(".err_msg"), getText(B));
            return !1
        });
        c(q)
    }

    function C(a) {
        var b = $(".modify_pwd"), c = $(".next_time"), e = $(".dialog_close"),
            d = u("modifypwd_modal").model({});
        c.bind("click", function () {
            Log.action("close_dialog_changepwd");
            c.addClass("btn_loading_gif");
            ajax_loading();
            var a = $("#redirect_url").val();
            a ? location.href = a : location.reload()
        });
        b.bind("click", function () {
            Log.action("changepwd");
            b.addClass("btn_loading_gif");
            $("#unsecurity_modal").remove();
            $("#main").append(xss(d, !1));
            Log.trace("show_changepwd");
            D(a)
        });
        e.bind("click", function () {
            Log.action("close_dialog_changepwd");
            var a = $("#redirect_url").val();
            ajax_loading();
            a ? location.href = a : location.reload();
            $("#unsecurity_modal").remove()
        })
    }

    function D(a) {
        var b = $(".password_wrapper").find(".password"),
            q = $(".confirm_password_wrapper").find(".password"),
            e = $(".err"), d = $(".err_second"), f = $(".dialog_close"),
            h = $(".confirm"), i = /\s/;
        b.bind("blur", function () {
            var a = $(this).val();
            if (a) {
                if (i.test(a))return !1; else e.empty().hide();
                if (isappFormValidator.isLength(a,
                        "6,18") === 0) e.empty().hide(); else return e.html(getText(g)).show(), !1;
                if (isappFormValidator.isSeriesNumber(a)) e.empty().hide(); else return Log.trace("show_pwderror", {key: g}), e.html(getText(g)).show(), !1
            } else return e.empty().hide(), !1
        }).bind("focus", function () {
            e.empty().hide();
            d.empty().hide()
        }).bind("input", function () {
            var a = $(this).val();
            if (a)if (i.test(a))return Log.trace("show_pwderror", {key: r}), e.html(getText(r)).show(), !1; else e.empty().hide(); else e.empty().hide()
        }).bind("keyup", function (a) {
            if (a.which ===
                13) {
                if (a = $(this).val()) {
                    if (i.test(a))return !1; else e.empty().hide();
                    if (isappFormValidator.isLength(a, "6,18") === 0) e.empty().hide(); else return Log.trace("show_pwderror", {key: g}), e.html(getText(g)).show(), !1;
                    if (isappFormValidator.isSeriesNumber(a)) e.empty().hide(); else return Log.trace("show_pwderror", {key: g}), e.html(getText(g)).show(), !1
                } else return Log.trace("show_pwderror", {key: g}), e.html(getText(g)).show(), !1;
                q.focus()
            }
        });
        q.bind("blur", function () {
            var a = b.val(), c = $(this).val();
            a === c && e.html("").hide()
        }).bind("focus",
            function () {
                d.empty().hide()
            }).bind("keyup", function (a) {
            var c = b.val(), x = $(this).val();
            a.which === 13 && (e.text() ? q.blur() : c === x ? h.trigger("click") : (d.html(getText(E)).show(), q.blur()))
        });
        h.bind("click", function () {
            var f = a.token, n = a.account, j = b.val(), k = q.val();
            if (!f || !n)return !1;
            if (i.test(j))return Log.trace("show_pwderror", {key: r}), e.html(getText(r)).show(), !1;
            if (isappFormValidator.isLength(j, "6,18") === 0) e.empty().hide(); else return Log.trace("show_pwderror", {key: g}), e.html(getText(g)).show(), !1;
            if (!isappFormValidator.isSeriesNumber(j))return Log.trace("show_pwderror",
                {key: g}), e.html(getText(g)).show(), !1;
            e.html("").hide();
            if (k !== j)return d.html(getText(E)).show(), !1;
            d.html("").hide();
            h.addClass("btn_loading_gif");
            ajax_loading();
            isappAjax({
                url: "/account/modifypwd",
                data: {
                    account: n,
                    token: f,
                    password: j,
                    next_login: o.hasClass("iu_checkbox_select") ? 1 : 0
                }
            }, function () {
                Log.trace("changepwd_success");
                iu.notice("body", {text: getText(c), level: 2});
                var a = $("#redirect_url").val();
                a ? location.href = a : location.reload()
            }, function () {
                ajax_loading_stop();
                h.removeClass("btn_loading_gif");
                l($(".err"), getText(A))
            })
        });
        f.bind("click", function () {
            Log.action("close_changepwd");
            var a = $("#redirect_url").val();
            ajax_loading();
            a ? location.href = a : location.reload();
            $("#modifypwd_modal").remove()
        })
    }

    function G() {
        var a = $("#findpwd_modal"), b = $(".dialog_close"), c = $(".cancel"),
            e = $(".find");
        b.bind("click", function () {
            Log.action("close_dialog_overtimes");
            a.remove()
        });
        c.bind("click", function () {
            Log.action("close_dialog_overtimes");
            a.remove()
        });
        e.bind("click", function () {
            Log.action("resetpwd");
            location.href =
                "/user/findpwd";
            a.remove()
        })
    }

    function u(a) {
        var b = {};
        I && I.length && I.forEach(function (c) {
            c.id === a && (b = c)
        });
        return b
    }

    Log.config(JSON.parse($("#page_config").text()).LOG_CONFIG);
    Log.page("d_signin");
    var d = $("#input_email"), h = $("#input_pwd"), w = $("#btn_login"),
        z = $("#error_top"),
        o = $(".iu_checkbox_button"), k = $("#input_mobile_num"),
        v = $("#input_code"), E = "err_repwdnotmatch",
        r = "pwd_no_space", g = "pwd_rule2", A = "modify_fail",
        B = "err_networkbusy", b = "wrong_code", a = "resend",
        c = "modify_ok", i = {};
    i[CPConfError.USER_NOTREG] =
        "err_notreg";
    i[CPConfError.USER_BIND] = "err_bind";
    i[CPConfError.ACCOUNT_NOT_EXISTS] = "err_notreg";
    i[CPConfError.LOGIN_INFO_ERROR] = "err_loginerror";
    var p = isapp.getUrlParam("email"), O = isapp.getUrlParam("mobile"),
        L = $("#_cclog_account").val(), K = "", I = [],
        H = /\s/;
    (function () {
        $("[type='text/template']").each(function () {
            var a = {}, b = $(this).attr("id"), c = $(this).html();
            a.id = b;
            a.html = c;
            a.model = template.compile(c);
            I.push(a);
            $(this).remove()
        })
    })();
    p ? d.val(p) : O ? d.val(O) : L && (d.val() || d.val(L), d.addClass("remember"));
    d.val() === "" ? d.trigger("focus") : h.trigger("focus");
    d.bind("input", function () {
        d.val() === "" && d.removeClass("remember")
    });
    o.bind("click", function () {
        $(this).hasClass("iu_checkbox_select") ? $(this).removeClass("iu_checkbox_select") : $(this).addClass("iu_checkbox_select")
    });
    d.bind("blur", function () {
        var a = isapp.trim($(this).val()), b = $(this).siblings(".error");
        a ? j(a) ? l(b) : l(b, "account_invalid") : (b.html("").hide(), d.removeClass("remember"))
    }).bind("keyup", function (a) {
        if (a.which === 13) {
            var a = isapp.trim($(this).val()),
                b = h.val(), c = $(this).siblings(".error");
            j(a) ? b ? w.trigger("click") : h.focus() : c.html(getText("account_invalid")).show()
        }
        $.trim($(this).val()) || $(this).removeClass("remember")
    }).bind("focus", function () {
        z.html("").hide()
    }).bind("mousedown", function () {
        $.trim($(this).val()) || $(this).removeClass("remember")
    });
    h.bind("keyup", function (a) {
        var b = $(this).val();
        a.which === 13 && b && w.trigger("click")
    }).bind("blur", function () {
        $(this).val() && z.html("").hide()
    });
    $(".personal .inner_ltop_title").bind("click", function () {
        $(this).hasClass("login_title") ?
            ($(".view_login").show(), $(".view_login_mobile").hide()) : ($(".view_login").hide(), $(".view_login_mobile").show())
    });
    $(".get_code").bind("click", function () {
        var a = k.val();
        if (a)if (a && isappFormValidator.isNumber(a) !== 0) k.focus().siblings(".error").html(getText("err_invalidphone")).show(); else {
            if (!$(this).hasClass("disable")) {
                var b = k.siblings(".iu_mselect_frame").find(".iu_mselect_text").text();
                if (isappFormValidator.isMobile(a) !== 0 || b === "+86" && a.length !== 11) k.focus().siblings(".error").html(getText("err_invalidphone")).show();
                else {
                    var c = $(this);
                    isappAjax({
                        url: "/user/sendforlogin",
                        data: {account: a, code: b}
                    }, function (a) {
                        a && (c.addClass("disable"), m = setInterval(F, 1E3));
                        iu.notice("body", {
                            text: getText("resend_code_ok"),
                            level: 2
                        })
                    }, function (a) {
                        a === -211 ? iu.notice("body", {text: getText("err_vcodetoomany")}) : iu.notice("body", {text: getText("err_networkbusy")})
                    })
                }
            }
        } else k.focus().siblings(".error").html(getText("id_label_phone")).show()
    });
    var N = 60, m;
    k.bind("input", function () {
        $(this).siblings(".error").hide()
    }).bind("blur", function () {
        var a =
            $(this).val();
        a && isappFormValidator.isNumber(a) !== 0 && $(this).siblings(".error").html(getText("err_invalidphone")).show()
    }).bind("keyup", function (a) {
        a.which === 13 && ((a = $(this).val()) && isappFormValidator.isNumber(a) !== 0 ? $(this).siblings(".error").html(getText("err_invalidphone")).show() : a && $(".get_code").trigger("click"))
    });
    v.bind("input", function () {
        $(this).siblings(".error").hide()
    }).bind("keyup", function (a) {
        a.which === 13 && $("#btn_login_mobile").trigger("click")
    });
    $("#btn_login_mobile").bind("click",
        function () {
            var a = k.val(), b = v.val(),
                c = k.siblings(".iu_mselect_frame").find(".iu_mselect_text").text(),
                d = $("#redirect_url").val();
            a ? isappFormValidator.isNumber(a) !== 0 ? k.focus().siblings(".error").html(getText("err_invalidphone")).show() : b ? (ajax_loading(), isappAjax({
                url: "/user/verifyforlogin",
                data: {
                    account: a,
                    code: c,
                    vcode: b,
                    redirect: d,
                    from: isapp.getUrlParam("from") || ""
                }
            }, function (a) {
                ajax_loading_stop();
                if (a.type === 2) {
                    if (a.redirect) {
                        location.href = a.redirect;
                        return
                    } else if (d) {
                        location.href = d;
                        return
                    }
                    location.reload()
                }
                a.type ===
                1 && ($(".view_login_mobile").hide(), $(".view_set_pwd").show(), $("#old_hash").val(a.old))
            }, function (a) {
                ajax_loading_stop();
                CPConfError.TEMPORARILY_UNAVAILABLE === a ? v.siblings(".error").html(getText("err_vcode_toomany")).show() : CPConfError.VERIFICATION_CODE_ERROR === a && v.siblings(".error").html(getText("err_check_code")).show()
            })) : v.focus().siblings(".error").html(getText("input_vcode")).show() : k.focus().siblings(".error").html(getText("id_label_phone")).show()
        });
    $("#input_set_password").bind("blur", function () {
        var a =
            $("#input_set_password"), b = a.siblings(".error"), c = a.val();
        if (!c)return b.html(getText("err_enterpwd")).show(), a.focus(), !1;
        if (isappFormValidator.isLength(c, "6,18") !== 0)return b.html(getText(g)).show(), !1;
        if (H.test(c))return b.html(getText(r)).show(), !1; else b.empty().hide();
        if (isappFormValidator.isSeriesNumber(c)) b.empty().hide(); else return b.html(getText(g)).show(), !1
    }).bind("focus", function () {
        var a = $("#input_set_password"), b = a.siblings(".error");
        b.empty().hide();
        a.bind("input propertychange", function () {
            var a =
                $(this).val();
            if (a)if (H.test(a))return b.html(getText(r)).show(), !1; else b.empty().hide(); else b.empty().hide()
        })
    }).bind("keyup", function (a) {
        var b = $("#input_set_password"), c = b.siblings(".error");
        if (a.which === 13) {
            if (a = $(this).val()) {
                if (isappFormValidator.isLength(a, "6,18") !== 0)return c.html(getText(g)).show(), !1;
                if (H.test(a))return c.html(getText(r)).show(), !1; else c.empty().hide();
                if (isappFormValidator.isSeriesNumber(a)) c.empty().hide(); else return c.html(getText(g)).show(), !1
            } else return c.html(getText(g)).show(),
                !1;
            b.blur();
            $("#btn_mobile_finish").trigger("click")
        }
    });
    $("#btn_mobile_finish").bind("click", function () {
        var a = $("#input_set_password"), b = a.siblings(".error"), c = a.val(),
            d = $("#redirect_url").val();
        if (!c)return b.html(getText("err_enterpwd")).show(), a.focus(), !1;
        if (isappFormValidator.isLength(c, "6,18") !== 0)return b.html(getText(g)).show(), !1;
        if (H.test(c))return b.html(getText(r)).show(), !1; else b.empty().hide();
        if (isappFormValidator.isSeriesNumber(c)) b.empty().hide(); else return b.html(getText(g)).show(),
            !1;
        b.html("").hide();
        isappAjax({
            url: "/user/setpwdforprereg",
            data: {
                account: k.val(),
                old_hash: $("#old_hash").val(),
                new_pwd: a.val(),
                redirect: d,
                from: isapp.getUrlParam("from") || ""
            }
        }, function (a) {
            a.redirect ? location.href = a.redirect : location.reload()
        })
    });
    iu.promptareacode(".country_select", {
        langid: isapp.getLangId(),
        showtext: 1,
        sltwidth: 348,
        btnwidth: 80,
        top: 1,
        left: 0,
        margintop: -1,
        singleHeight: 34
    });
    $(".iu_mselect_text").addClass("hidelong");
    var s = !1;
    w.bind("click", function () {
        if (s)return !1;
        var a = isapp.trim(d.val()),
            b = h.val(), c, e = $("#redirect_url").val();
        c = d.siblings(".error");
        if (!a)return l(c, "err_enteraccount"), d.focus(), !1;
        if (!j(a))return l(c, "account_invalid"), d.focus(), !1;
        l(c);
        c = h.siblings(".error");
        if (!b)return l(c, "err_enterpwd"), h.focus(), !1;
        l(c);
        $(".input_text").blur();
        s = !0;
        ajax_loading();
        isappAjax({
            url: "/user/auth",
            data: {
                account: a,
                password: b,
                next_login: o.hasClass("iu_checkbox_select") ? 1 : 0,
                redirect: e,
                from: isapp.getUrlParam("from") || ""
            }
        }, function (a) {
            s = !1;
            var b;
            b = !1;
            if (navigator.cookieEnabled) b = !0; else if (document.cookie =
                    "testcookie=yes;", document.cookie.indexOf("testcookie=yes") > -1) b = new Date, b.setTime(b.getTime() - 1), document.cookie = "testcookie=; expires=" + b.toGMTString(), b = !0;
            if (!b)return l(d.siblings(".error"), "err_nocookie"), ajax_loading_stop(), !1;
            location.href = $("#redirect_url").val();
            a.redirect ? location.href = a.redirect : location.reload()
        }, function (b) {
            s = !1;
            ajax_loading_stop();
            if (b === -114 || b === -113) {
                var c = d.val() || k.val();
                c && isappAjax({
                        url: "/account/verifylist",
                        data: {account: c, areacode: "86", language: "zh-cn"}
                    },
                    function (a) {
                        var c = u("account_modal").model(a),
                            a = {
                                ACCOUNT: a.ACCOUNT,
                                AREA_CODE: a.AREA_CODE,
                                ECY_ACCOUNT: a.ECY_ACCOUNT,
                                ERR_CODE: b
                            };
                        Log.trace("show_security_verify");
                        $("#main").append(xss(c, !1));
                        f("#account_modal", a)
                    }, function () {
                        return !1
                    });
                return !1
            }
            if (b === -116)return Log.trace("show_dialog_overtimes"), c = u("findpwd_modal").model({}), $("#main").append(xss(c, !1)), G(), !1;
            a.substr(-4) === ".ccb" && CPConfError.LOGIN_INFO_ERROR === b ? l(d.siblings(".error"), "forget_pwd_ccb") : l(d.siblings(".error"), i[b] || "err_networkbusy")
        });
        return !1
    });
    isapp.cookie("_cp_msg", null)
});
