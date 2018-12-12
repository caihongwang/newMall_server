window.cc_loadtime && cc_loadtime.push(new Date);
(function (b, e) {
    function q(a) {
        return c.isWindow(a) ? a : a.nodeType === 9 ? a.defaultView || a.parentWindow : !1
    }

    function r(a) {
        if (!ka[a]) {
            var d = v.body, g = c("<" + a + ">").appendTo(d),
                f = g.css("display");
            g.remove();
            if (f === "none" || f === "") {
                Q || (Q = v.createElement("iframe"), Q.frameBorder = Q.width = Q.height = 0);
                d.appendChild(Q);
                if (!Y || !Q.createElement) Y = (Q.contentWindow || Q.contentDocument).document, Y.write((v.compatMode === "CSS1Compat" ? "<!doctype html>" : "") + "<html><body>"), Y.close();
                g = Y.createElement(a);
                Y.body.appendChild(g);
                f = c.css(g, "display");
                d.removeChild(Q)
            }
            ka[a] = f
        }
        return ka[a]
    }

    function o(a, d) {
        var g = {};
        c.each(sa.concat.apply([], sa.slice(0, d)), function () {
            g[this] = a
        });
        return g
    }

    function p() {
        ea = e
    }

    function j() {
        setTimeout(p, 0);
        return ea = c.now()
    }

    function k() {
        try {
            return new b.XMLHttpRequest
        } catch (a) {
        }
    }

    function m(a, d, g, f) {
        if (c.isArray(d)) c.each(d, function (d, n) {
            g || Va.test(a) ? f(a, n) : m(a + "[" + (typeof n == "object" || c.isArray(n) ? d : "") + "]", n, g, f)
        }); else if (!g && d != null && typeof d == "object")for (var n in d)m(a + "[" + n + "]", d[n], g, f); else f(a,
            d)
    }

    function z(a, d) {
        var g, f, n = c.ajaxSettings.flatOptions || {};
        for (g in d)d[g] !== e && ((n[g] ? a : f || (f = {}))[g] = d[g]);
        f && c.extend(!0, a, f)
    }

    function y(a, d, c, f, n, b) {
        n = n || d.dataTypes[0];
        b = b || {};
        b[n] = !0;
        for (var n = a[n], l = 0, k = n ? n.length : 0, j = a === la, h; l < k && (j || !h); l++)h = n[l](d, c, f), typeof h == "string" && (!j || b[h] ? h = e : (d.dataTypes.unshift(h), h = y(a, d, c, f, h, b)));
        (j || !h) && !b["*"] && (h = y(a, d, c, f, "*", b));
        return h
    }

    function C(a) {
        return function (d, g) {
            typeof d != "string" && (g = d, d = "*");
            if (c.isFunction(g))for (var f = d.toLowerCase().split(va),
                                         n = 0, b = f.length, e, l, h; n < b; n++)e = f[n], h = /^\+/.test(e), h && (e = e.substr(1) || "*"), l = a[e] = a[e] || [], l[h ? "unshift" : "push"](g)
        }
    }

    function D(a, d, g) {
        var f = d === "width" ? a.offsetWidth : a.offsetHeight,
            n = d === "width" ? Wa : Xa, b = 0, e = n.length;
        if (f > 0) {
            if (g !== "border")for (; b < e; b++)g || (f -= parseFloat(c.css(a, "padding" + n[b])) || 0), g === "margin" ? f += parseFloat(c.css(a, g + n[b])) || 0 : f -= parseFloat(c.css(a, "border" + n[b] + "Width")) || 0;
            return f + "px"
        }
        f = Z(a, d, d);
        if (f < 0 || f == null) f = a.style[d] || 0;
        f = parseFloat(f) || 0;
        if (g)for (; b < e; b++)f += parseFloat(c.css(a,
                "padding" + n[b])) || 0, g !== "padding" && (f += parseFloat(c.css(a, "border" + n[b] + "Width")) || 0), g === "margin" && (f += parseFloat(c.css(a, g + n[b])) || 0);
        return f + "px"
    }

    function t(a, d) {
        d.src ? c.ajax({
            url: d.src,
            async: !1,
            dataType: "script"
        }) : c.globalEval((d.text || d.textContent || d.innerHTML || "").replace(Ya, "/*$0*/"));
        d.parentNode && d.parentNode.removeChild(d)
    }

    function s(a) {
        var d = (a.nodeName || "").toLowerCase();
        d === "input" ? u(a) : d !== "script" && typeof a.getElementsByTagName != "undefined" && c.grep(a.getElementsByTagName("input"),
                u)
    }

    function u(a) {
        if (a.type === "checkbox" || a.type === "radio") a.defaultChecked = a.checked
    }

    function h(a) {
        return typeof a.getElementsByTagName != "undefined" ? a.getElementsByTagName("*") : typeof a.querySelectorAll != "undefined" ? a.querySelectorAll("*") : []
    }

    function O(a, d) {
        var g;
        if (d.nodeType === 1) {
            d.clearAttributes && d.clearAttributes();
            d.mergeAttributes && d.mergeAttributes(a);
            g = d.nodeName.toLowerCase();
            if (g === "object") d.outerHTML = a.outerHTML; else if (g !== "input" || a.type !== "checkbox" && a.type !== "radio")if (g === "option") d.selected =
                a.defaultSelected; else {
                if (g === "input" || g === "textarea") d.defaultValue = a.defaultValue
            } else a.checked && (d.defaultChecked = d.checked = a.checked), d.value !== a.value && (d.value = a.value);
            d.removeAttribute(c.expando)
        }
    }

    function G(a, d) {
        if (d.nodeType === 1 && c.hasData(a)) {
            var g, f, n;
            f = c._data(a);
            var b = c._data(d, f), e = f.events;
            if (e)for (g in delete b.handle, b.events = {}, e)for (f = 0, n = e[g].length; f < n; f++)c.event.add(d, g + (e[g][f].namespace ? "." : "") + e[g][f].namespace, e[g][f], e[g][f].data);
            b.data && (b.data = c.extend({}, b.data))
        }
    }

    function x(a) {
        var d = wa.split("|"), a = a.createDocumentFragment();
        if (a.createElement)for (; d.length;)a.createElement(d.pop());
        return a
    }

    function K(a, d, g) {
        d = d || 0;
        if (c.isFunction(d))return c.grep(a, function (a, c) {
            return !!d.call(a, c, a) === g
        });
        if (d.nodeType)return c.grep(a, function (a) {
            return a === d === g
        });
        if (typeof d == "string") {
            var f = c.grep(a, function (a) {
                return a.nodeType === 1
            });
            if (Za.test(d))return c.filter(d, f, !g);
            d = c.filter(d, f)
        }
        return c.grep(a, function (a) {
            return c.inArray(a, d) >= 0 === g
        })
    }

    function P() {
        return !0
    }

    function I() {
        return !1
    }

    function E(a, d, g) {
        var f = d + "defer", n = d + "queue", b = d + "mark", e = c._data(a, f);
        e && (g === "queue" || !c._data(a, n)) && (g === "mark" || !c._data(a, b)) && setTimeout(function () {
            !c._data(a, n) && !c._data(a, b) && (c.removeData(a, f, !0), e.fire())
        }, 0)
    }

    function J(a) {
        for (var d in a)if (!(d === "data" && c.isEmptyObject(a[d])) && d !== "toJSON")return !1;
        return !0
    }

    function R(a, d, g) {
        if (g === e && a.nodeType === 1)if (g = "data-" + d.replace(B, "-$1").toLowerCase(), g = a.getAttribute(g), typeof g == "string") {
            try {
                g = g === "true" ? !0 : g === "false" ?
                    !1 : g === "null" ? null : c.isNumeric(g) ? parseFloat(g) : A.test(g) ? c.parseJSON(g) : g
            } catch (f) {
            }
            c.data(a, d, g)
        } else g = e;
        return g
    }

    function S(a) {
        var d = l[a] = {}, c, f, a = a.split(/\s+/);
        for (c = 0, f = a.length; c < f; c++)d[a[c]] = !0;
        return d
    }

    var v = b.document, fa = b.navigator, ca = b.location, c = function () {
        function a() {
            if (!d.isReady) {
                try {
                    v.documentElement.doScroll("left")
                } catch (c) {
                    setTimeout(a, 1);
                    return
                }
                d.ready()
            }
        }

        var d = function (a, c) {
                return new d.fn.init(a, c, n)
            }, c = b.jQuery, f = b.$, n,
            l = /^(?:[^#<]*(<[\w\W]+>)[^>]*$|#([\w\-]*)$)/, N = /\S/,
            h = /^\s+/, j = /\s+$/, k = /^<(\w+)\s*\/?>(?:<\/\1>)?$/,
            o = /^[\],:{}\s]*$/,
            p = /\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,
            m = /"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
            A = /(?:^|:|,)(?:\s*\[)+/g,
            w = /(webkit)[ \/]([\w.]+)/,
            r = /(opera)(?:.*version)?[ \/]([\w.]+)/, s = /(msie) ([\w.]+)/,
            u = /(mozilla)(?:.*? rv:([\w.]+))?/, q = /-([a-z]|[0-9])/ig,
            x = /^-ms-/, t = function (a, d) {
                return (d + "").toUpperCase()
            }, G = fa.userAgent, F, ha, aa, $a = Object.prototype.toString,
            ma = Object.prototype.hasOwnProperty,
            na = Array.prototype.push,
            B = Array.prototype.slice, xa = String.prototype.trim,
            z = Array.prototype.indexOf, O = {};
        d.fn = d.prototype = {
            constructor: d, init: function (a, c, g) {
                var f, n, b;
                if (!a)return this;
                if (a.nodeType)return this.context = this[0] = a, this.length = 1, this;
                if (a === "body" && !c && v.body)return this.context = v, this[0] = v.body, this.selector = a, this.length = 1, this;
                if (typeof a == "string") {
                    a.charAt(0) !== "<" || a.charAt(a.length - 1) !== ">" || a.length < 3 ? f = l.exec(a) : f = [null, a, null];
                    if (f && (f[1] || !c)) {
                        if (f[1])return c = c instanceof d ? c[0] : c, b = c ? c.ownerDocument ||
                            c : v, n = k.exec(a), n ? d.isPlainObject(c) ? (a = [v.createElement(n[1])], d.fn.attr.call(a, c, !0)) : a = [b.createElement(n[1])] : (n = d.buildFragment([f[1]], [b]), a = (n.cacheable ? d.clone(n.fragment) : n.fragment).childNodes), d.merge(this, a);
                        if ((c = v.getElementById(f[2])) && c.parentNode) {
                            if (c.id !== f[2])return g.find(a);
                            this.length = 1;
                            this[0] = c
                        }
                        this.context = v;
                        this.selector = a;
                        return this
                    }
                    return !c || c.jquery ? (c || g).find(a) : this.constructor(c).find(a)
                }
                if (d.isFunction(a))return g.ready(a);
                a.selector !== e && (this.selector = a.selector,
                    this.context = a.context);
                return d.makeArray(a, this)
            }, selector: "", jquery: "1.7.1", length: 0, size: function () {
                return this.length
            }, toArray: function () {
                return B.call(this, 0)
            }, get: function (a) {
                return a == null ? this.toArray() : a < 0 ? this[this.length + a] : this[a]
            }, pushStack: function (a, c, g) {
                var f = this.constructor();
                d.isArray(a) ? na.apply(f, a) : d.merge(f, a);
                f.prevObject = this;
                f.context = this.context;
                c === "find" ? f.selector = this.selector + (this.selector ? " " : "") + g : c && (f.selector = this.selector + "." + c + "(" + g + ")");
                return f
            }, each: function (a,
                               c) {
                return d.each(this, a, c)
            }, ready: function (a) {
                d.bindReady();
                ha.add(a);
                return this
            }, eq: function (a) {
                a = +a;
                return a === -1 ? this.slice(a) : this.slice(a, a + 1)
            }, first: function () {
                return this.eq(0)
            }, last: function () {
                return this.eq(-1)
            }, slice: function () {
                return this.pushStack(B.apply(this, arguments), "slice", B.call(arguments).join(","))
            }, map: function (a) {
                return this.pushStack(d.map(this, function (d, c) {
                    return a.call(d, c, d)
                }))
            }, end: function () {
                return this.prevObject || this.constructor(null)
            }, push: na, sort: [].sort, splice: [].splice
        };
        d.fn.init.prototype = d.fn;
        d.extend = d.fn.extend = function () {
            var a, c, g, f, n, b, F = arguments[0] || {}, l = 1,
                ta = arguments.length, N = !1;
            for (typeof F == "boolean" && (N = F, F = arguments[1] || {}, l = 2), typeof F != "object" && !d.isFunction(F) && (F = {}), ta === l && (F = this, --l); l < ta; l++)if ((a = arguments[l]) != null)for (c in a)g = F[c], f = a[c], F !== f && (N && f && (d.isPlainObject(f) || (n = d.isArray(f))) ? (n ? (n = !1, b = g && d.isArray(g) ? g : []) : b = g && d.isPlainObject(g) ? g : {}, F[c] = d.extend(N, b, f)) : f !== e && (F[c] = f));
            return F
        };
        d.extend({
            noConflict: function (a) {
                b.$ ===
                d && (b.$ = f);
                a && b.jQuery === d && (b.jQuery = c);
                return d
            }, isReady: !1, readyWait: 1, holdReady: function (a) {
                a ? d.readyWait++ : d.ready(!0)
            }, ready: function (a) {
                if (a === !0 && !--d.readyWait || a !== !0 && !d.isReady) {
                    if (!v.body)return setTimeout(d.ready, 1);
                    d.isReady = !0;
                    a !== !0 && --d.readyWait > 0 || (ha.fireWith(v, [d]), d.fn.trigger && d(v).trigger("ready").off("ready"))
                }
            }, bindReady: function () {
                if (!ha) {
                    ha = d.Callbacks("once memory");
                    if (v.readyState === "complete")return setTimeout(d.ready, 1);
                    if (v.addEventListener) v.addEventListener("DOMContentLoaded",
                        aa, !1), b.addEventListener("load", d.ready, !1); else if (v.attachEvent) {
                        v.attachEvent("onreadystatechange", aa);
                        b.attachEvent("onload", d.ready);
                        var c = !1;
                        try {
                            c = b.frameElement == null
                        } catch (g) {
                        }
                        v.documentElement.doScroll && c && a()
                    }
                }
            }, isFunction: function (a) {
                return d.type(a) === "function"
            }, isArray: Array.isArray || function (a) {
                return d.type(a) === "array"
            }, isWindow: function (a) {
                return a && typeof a == "object" && "setInterval" in a
            }, isNumeric: function (a) {
                return !isNaN(parseFloat(a)) && isFinite(a)
            }, type: function (a) {
                return a ==
                null ? String(a) : O[$a.call(a)] || "object"
            }, isPlainObject: function (a) {
                if (!a || d.type(a) !== "object" || a.nodeType || d.isWindow(a))return !1;
                try {
                    if (a.constructor && !ma.call(a, "constructor") && !ma.call(a.constructor.prototype, "isPrototypeOf"))return !1
                } catch (c) {
                    return !1
                }
                for (var g in a);
                return g === e || ma.call(a, g)
            }, isEmptyObject: function (a) {
                for (var d in a)return !1;
                return !0
            }, error: function (a) {
                throw Error(a);
            }, parseJSON: function (a) {
                if (typeof a != "string" || !a)return null;
                a = d.trim(a);
                if (b.JSON && b.JSON.parse)return b.JSON.parse(a);
                if (o.test(a.replace(p, "@").replace(m, "]").replace(A, "")))return (new Function("return " + a))();
                d.error("Invalid JSON: " + a)
            }, parseXML: function (a) {
                var c, g;
                try {
                    b.DOMParser ? (g = new DOMParser, c = g.parseFromString(a, "text/xml")) : (c = new ActiveXObject("Microsoft.XMLDOM"), c.async = "false", c.loadXML(a))
                } catch (f) {
                    c = e
                }
                (!c || !c.documentElement || c.getElementsByTagName("parsererror").length) && d.error("Invalid XML: " + a);
                return c
            }, noop: function () {
            }, globalEval: function (a) {
                a && N.test(a) && (b.execScript || function (a) {
                    b.eval.call(b,
                        a)
                })(a)
            }, camelCase: function (a) {
                return a.replace(x, "ms-").replace(q, t)
            }, nodeName: function (a, d) {
                return a.nodeName && a.nodeName.toUpperCase() === d.toUpperCase()
            }, each: function (a, c, g) {
                var f, n = 0, b = a.length, F = b === e || d.isFunction(a);
                if (g)if (F)for (f in a) {
                    if (c.apply(a[f], g) === !1)break
                } else for (; n < b;) {
                    if (c.apply(a[n++], g) === !1)break
                } else if (F)for (f in a) {
                    if (c.call(a[f], f, a[f]) === !1)break
                } else for (; n < b;)if (c.call(a[n], n, a[n++]) === !1)break;
                return a
            }, trim: xa ? function (a) {
                return a == null ? "" : xa.call(a)
            } : function (a) {
                return a ==
                null ? "" : (a + "").replace(h, "").replace(j, "")
            }, makeArray: function (a, c) {
                var g = c || [];
                if (a != null) {
                    var f = d.type(a);
                    a.length == null || f === "string" || f === "function" || f === "regexp" || d.isWindow(a) ? na.call(g, a) : d.merge(g, a)
                }
                return g
            }, inArray: function (a, d, c) {
                var g;
                if (d) {
                    if (z)return z.call(d, a, c);
                    for (g = d.length, c = c ? c < 0 ? Math.max(0, g + c) : c : 0; c < g; c++)if (c in d && d[c] === a)return c
                }
                return -1
            }, merge: function (a, d) {
                var c = a.length, g = 0;
                if (typeof d.length == "number")for (var f = d.length; g < f; g++)a[c++] = d[g]; else for (; d[g] !== e;)a[c++] =
                    d[g++];
                a.length = c;
                return a
            }, grep: function (a, d, c) {
                for (var g = [], f, c = !!c, n = 0, b = a.length; n < b; n++)f = !!d(a[n], n), c !== f && g.push(a[n]);
                return g
            }, map: function (a, c, g) {
                var f, n, b = [], F = 0, l = a.length;
                if (a instanceof d || l !== e && typeof l == "number" && (l > 0 && a[0] && a[l - 1] || l === 0 || d.isArray(a)))for (; F < l; F++)f = c(a[F], F, g), f != null && (b[b.length] = f); else for (n in a)f = c(a[n], n, g), f != null && (b[b.length] = f);
                return b.concat.apply([], b)
            }, guid: 1, proxy: function (a, c) {
                if (typeof c == "string") {
                    var g = a[c];
                    c = a;
                    a = g
                }
                if (!d.isFunction(a))return e;
                var f = B.call(arguments, 2), g = function () {
                    return a.apply(c, f.concat(B.call(arguments)))
                };
                g.guid = a.guid = a.guid || g.guid || d.guid++;
                return g
            }, access: function (a, c, g, f, n, b) {
                var F = a.length;
                if (typeof c == "object") {
                    for (var l in c)d.access(a, l, c[l], f, n, g);
                    return a
                }
                if (g !== e) {
                    f = !b && f && d.isFunction(g);
                    for (l = 0; l < F; l++)n(a[l], c, f ? g.call(a[l], l, n(a[l], c)) : g, b);
                    return a
                }
                return F ? n(a[0], c) : e
            }, now: function () {
                return (new Date).getTime()
            }, uaMatch: function (a) {
                a = a.toLowerCase();
                a = w.exec(a) || r.exec(a) || s.exec(a) || a.indexOf("compatible") <
                    0 && u.exec(a) || [];
                return {browser: a[1] || "", version: a[2] || "0"}
            }, sub: function () {
                function a(d, c) {
                    return new a.fn.init(d, c)
                }

                d.extend(!0, a, this);
                a.superclass = this;
                a.fn = a.prototype = this();
                a.fn.constructor = a;
                a.sub = this.sub;
                a.fn.init = function (g, f) {
                    f && f instanceof d && !(f instanceof a) && (f = a(f));
                    return d.fn.init.call(this, g, f, c)
                };
                a.fn.init.prototype = a.fn;
                var c = a(v);
                return a
            }, browser: {}
        });
        d.each("Boolean Number String Function Array Date RegExp Object".split(" "), function (a, d) {
            O["[object " + d + "]"] = d.toLowerCase()
        });
        F = d.uaMatch(G);
        F.browser && (d.browser[F.browser] = !0, d.browser.version = F.version);
        d.browser.webkit && (d.browser.safari = !0);
        N.test("\u00a0") && (h = /^[\s\xA0]+/, j = /[\s\xA0]+$/);
        n = d(v);
        v.addEventListener ? aa = function () {
            v.removeEventListener("DOMContentLoaded", aa, !1);
            d.ready()
        } : v.attachEvent && (aa = function () {
                v.readyState === "complete" && (v.detachEvent("onreadystatechange", aa), d.ready())
            });
        return d
    }(), l = {};
    c.Callbacks = function (a) {
        var a = a ? l[a] || S(a) : {}, d = [], g = [], f, n, b, N, h,
            j = function (g) {
                var f, n, b, e;
                for (f = 0, n = g.length; f <
                n; f++)b = g[f], e = c.type(b), e === "array" ? j(b) : e === "function" && (!a.unique || !o.has(b)) && d.push(b)
            }, k = function (c, e) {
                for (e = e || [], f = !a.memory || [c, e], n = !0, h = b || 0, b = 0, N = d.length; d && h < N; h++)if (d[h].apply(c, e) === !1 && a.stopOnFalse) {
                    f = !0;
                    break
                }
                n = !1;
                d && (a.once ? f === !0 ? o.disable() : d = [] : g && g.length && (f = g.shift(), o.fireWith(f[0], f[1])))
            }, o = {
                add: function () {
                    if (d) {
                        var a = d.length;
                        j(arguments);
                        n ? N = d.length : f && f !== !0 && (b = a, k(f[0], f[1]))
                    }
                    return this
                }, remove: function () {
                    if (d)for (var c = arguments, g = 0, f = c.length; g < f; g++)for (var b =
                        0; b < d.length; b++)if (c[g] === d[b] && (n && b <= N && (N--, b <= h && h--), d.splice(b--, 1), a.unique))break;
                    return this
                }, has: function (a) {
                    if (d)for (var c = 0, g = d.length; c < g; c++)if (a === d[c])return !0;
                    return !1
                }, empty: function () {
                    d = [];
                    return this
                }, disable: function () {
                    d = g = f = e;
                    return this
                }, disabled: function () {
                    return !d
                }, lock: function () {
                    g = e;
                    (!f || f === !0) && o.disable();
                    return this
                }, locked: function () {
                    return !g
                }, fireWith: function (d, c) {
                    g && (n ? a.once || g.push([d, c]) : (!a.once || !f) && k(d, c));
                    return this
                }, fire: function () {
                    o.fireWith(this, arguments);
                    return this
                }, fired: function () {
                    return !!f
                }
            };
        return o
    };
    var w = [].slice;
    c.extend({
        Deferred: function (a) {
            var d = c.Callbacks("once memory"), g = c.Callbacks("once memory"),
                f = c.Callbacks("memory"),
                n = "pending", b = {resolve: d, reject: g, notify: f}, e = {
                    done: d.add,
                    fail: g.add,
                    progress: f.add,
                    state: function () {
                        return n
                    },
                    isResolved: d.fired,
                    isRejected: g.fired,
                    then: function (a, d, c) {
                        l.done(a).fail(d).progress(c);
                        return this
                    },
                    always: function () {
                        l.done.apply(l, arguments).fail.apply(l, arguments);
                        return this
                    },
                    pipe: function (a, d, g) {
                        return c.Deferred(function (f) {
                            c.each({
                                done: [a,
                                    "resolve"],
                                fail: [d, "reject"],
                                progress: [g, "notify"]
                            }, function (a, d) {
                                var g = d[0], n = d[1], b;
                                c.isFunction(g) ? l[a](function () {
                                    b = g.apply(this, arguments);
                                    b && c.isFunction(b.promise) ? b.promise().then(f.resolve, f.reject, f.notify) : f[n + "With"](this === l ? f : this, [b])
                                }) : l[a](f[n])
                            })
                        }).promise()
                    },
                    promise: function (a) {
                        if (a == null) a = e; else for (var d in e)a[d] = e[d];
                        return a
                    }
                }, l = e.promise({}), h;
            for (h in b)l[h] = b[h].fire, l[h + "With"] = b[h].fireWith;
            l.done(function () {
                n = "resolved"
            }, g.disable, f.lock).fail(function () {
                    n = "rejected"
                },
                d.disable, f.lock);
            a && a.call(l, l);
            return l
        }, when: function (a) {
            function d(a) {
                return function (d) {
                    e[a] = arguments.length > 1 ? w.call(arguments, 0) : d;
                    h.notifyWith(j, e)
                }
            }

            function g(a) {
                return function (d) {
                    f[a] = arguments.length > 1 ? w.call(arguments, 0) : d;
                    --l || h.resolveWith(h, f)
                }
            }

            var f = w.call(arguments, 0), n = 0, b = f.length, e = Array(b),
                l = b,
                h = b <= 1 && a && c.isFunction(a.promise) ? a : c.Deferred(),
                j = h.promise();
            if (b > 1) {
                for (; n < b; n++)f[n] && f[n].promise && c.isFunction(f[n].promise) ? f[n].promise().then(g(n), h.reject, d(n)) : --l;
                l || h.resolveWith(h,
                    f)
            } else h !== a && h.resolveWith(h, b ? [a] : []);
            return j
        }
    });
    c.support = function () {
        var a, d, g, f, n, e, l, h, j, k, o, p, m = v.createElement("div");
        m.setAttribute("className", "t");
        m.innerHTML = "   <link/><table></table><a href='/a' style='top:1px;float:left;opacity:.55;'>a</a><input type='checkbox'/>";
        d = m.getElementsByTagName("*");
        g = m.getElementsByTagName("a")[0];
        if (!d || !d.length || !g)return {};
        f = v.createElement("select");
        n = f.appendChild(v.createElement("option"));
        e = m.getElementsByTagName("input")[0];
        a = {
            leadingWhitespace: m.firstChild.nodeType ===
            3,
            tbody: !m.getElementsByTagName("tbody").length,
            htmlSerialize: !!m.getElementsByTagName("link").length,
            style: /top/.test(g.getAttribute("style")),
            hrefNormalized: g.getAttribute("href") === "/a",
            opacity: /^0.55/.test(g.style.opacity),
            cssFloat: !!g.style.cssFloat,
            checkOn: e.value === "on",
            optSelected: n.selected,
            getSetAttribute: m.className !== "t",
            enctype: !!v.createElement("form").enctype,
            html5Clone: v.createElement("nav").cloneNode(!0).outerHTML !== "<:nav></:nav>",
            submitBubbles: !0,
            changeBubbles: !0,
            focusinBubbles: !1,
            deleteExpando: !0,
            noCloneEvent: !0,
            inlineBlockNeedsLayout: !1,
            shrinkWrapBlocks: !1,
            reliableMarginRight: !0
        };
        e.checked = !0;
        a.noCloneChecked = e.cloneNode(!0).checked;
        f.disabled = !0;
        a.optDisabled = !n.disabled;
        try {
            delete m.test
        } catch (A) {
            a.deleteExpando = !1
        }
        !m.addEventListener && m.attachEvent && m.fireEvent && (m.attachEvent("onclick", function () {
            a.noCloneEvent = !1
        }), m.cloneNode(!0).fireEvent("onclick"));
        e = v.createElement("input");
        e.value = "t";
        e.setAttribute("type", "radio");
        a.radioValue = e.value === "t";
        e.setAttribute("checked",
            "checked");
        m.appendChild(e);
        h = v.createDocumentFragment();
        h.appendChild(m.lastChild);
        a.checkClone = h.cloneNode(!0).cloneNode(!0).lastChild.checked;
        a.appendChecked = e.checked;
        h.removeChild(e);
        h.appendChild(m);
        m.innerHTML = "";
        b.getComputedStyle && (l = v.createElement("div"), l.style.width = "0", l.style.marginRight = "0", m.style.width = "2px", m.appendChild(l), a.reliableMarginRight = (parseInt((b.getComputedStyle(l, null) || {marginRight: 0}).marginRight, 10) || 0) === 0);
        if (m.attachEvent)for (o in{submit: 1, change: 1, focusin: 1})k =
            "on" + o, p = k in m, p || (m.setAttribute(k, "return;"), p = typeof m[k] == "function"), a[o + "Bubbles"] = p;
        h.removeChild(m);
        h = f = n = l = m = e = null;
        c(function () {
            var d, g, f, n, b, e = v.getElementsByTagName("body")[0];
            !e || (d = v.createElement("div"), d.style.cssText = "visibility:hidden;border:0;width:0;height:0;position:static;top:0;margin-top:1px", e.insertBefore(d, e.firstChild), m = v.createElement("div"), d.appendChild(m), m.innerHTML = "<table><tr><td style='padding:0;border:0;display:none'></td><td>t</td></tr></table>", j = m.getElementsByTagName("td"),
                p = j[0].offsetHeight === 0, j[0].style.display = "", j[1].style.display = "none", a.reliableHiddenOffsets = p && j[0].offsetHeight === 0, m.innerHTML = "", m.style.width = m.style.paddingLeft = "1px", c.boxModel = a.boxModel = m.offsetWidth === 2, typeof m.style.zoom != "undefined" && (m.style.display = "inline", m.style.zoom = 1, a.inlineBlockNeedsLayout = m.offsetWidth === 2, m.style.display = "", m.innerHTML = "<div style='width:4px;'></div>", a.shrinkWrapBlocks = m.offsetWidth !== 2), m.style.cssText = "position:absolute;top:0;left:0;width:1px;height:1px;margin:0;visibility:hidden;border:0;",
                m.innerHTML = "<div style='position:absolute;top:0;left:0;width:1px;height:1px;margin:0;border:5px solid #000;padding:0;'><div></div></div><table style='position:absolute;top:0;left:0;width:1px;height:1px;margin:0;border:5px solid #000;padding:0;' cellpadding='0' cellspacing='0'><tr><td></td></tr></table>", g = m.firstChild, f = g.firstChild, n = g.nextSibling.firstChild.firstChild, b = {
                doesNotAddBorder: f.offsetTop !== 5,
                doesAddBorderForTableAndCells: n.offsetTop === 5
            }, f.style.position = "fixed", f.style.top = "20px",
                b.fixedPosition = f.offsetTop === 20 || f.offsetTop === 15, f.style.position = f.style.top = "", g.style.overflow = "hidden", g.style.position = "relative", b.subtractsBorderForOverflowNotVisible = f.offsetTop === -5, b.doesNotIncludeMarginInBodyOffset = e.offsetTop !== 1, e.removeChild(d), m = null, c.extend(a, b))
        });
        return a
    }();
    var A = /^(?:\{.*\}|\[.*\])$/, B = /([A-Z])/g;
    c.extend({
        cache: {},
        uuid: 0,
        expando: "jQuery" + (c.fn.jquery + Math.random()).replace(/\D/g, ""),
        noData: {
            embed: !0,
            object: "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000",
            applet: !0
        },
        hasData: function (a) {
            a = a.nodeType ? c.cache[a[c.expando]] : a[c.expando];
            return !!a && !J(a)
        },
        data: function (a, d, g, f) {
            if (c.acceptData(a)) {
                var n, b, l = c.expando, h = typeof d == "string",
                    j = a.nodeType, k = j ? c.cache : a,
                    m = j ? a[l] : a[l] && l, o = d === "events";
                if (m && k[m] && (o || f || k[m].data) || !(h && g === e)) {
                    m || (j ? a[l] = m = ++c.uuid : m = l);
                    k[m] || (k[m] = {}, j || (k[m].toJSON = c.noop));
                    if (typeof d == "object" || typeof d == "function") f ? k[m] = c.extend(k[m], d) : k[m].data = c.extend(k[m].data, d);
                    a = n = k[m];
                    f || (n.data || (n.data = {}), n = n.data);
                    g !== e && (n[c.camelCase(d)] =
                        g);
                    if (o && !n[d])return a.events;
                    h ? (b = n[d], b == null && (b = n[c.camelCase(d)])) : b = n;
                    return b
                }
            }
        },
        removeData: function (a, d, g) {
            if (c.acceptData(a)) {
                var f, n, b, e = c.expando, l = a.nodeType, h = l ? c.cache : a,
                    k = l ? a[e] : e;
                if (h[k]) {
                    if (d && (f = g ? h[k] : h[k].data)) {
                        c.isArray(d) || (d in f ? d = [d] : (d = c.camelCase(d), d in f ? d = [d] : d = d.split(" ")));
                        for (n = 0, b = d.length; n < b; n++)delete f[d[n]];
                        if (!(g ? J : c.isEmptyObject)(f))return
                    }
                    if (!g && (delete h[k].data, !J(h[k])))return;
                    c.support.deleteExpando || !h.setInterval ? delete h[k] : h[k] = null;
                    l && (c.support.deleteExpando ?
                        delete a[e] : a.removeAttribute ? a.removeAttribute(e) : a[e] = null)
                }
            }
        },
        _data: function (a, d, g) {
            return c.data(a, d, g, !0)
        },
        acceptData: function (a) {
            if (a.nodeName) {
                var d = c.noData[a.nodeName.toLowerCase()];
                if (d)return d !== !0 && a.getAttribute("classid") === d
            }
            return !0
        }
    });
    c.fn.extend({
        data: function (a, d) {
            var g, f, n, b = null;
            if (typeof a == "undefined") {
                if (this.length && (b = c.data(this[0]), this[0].nodeType === 1 && !c._data(this[0], "parsedAttrs"))) {
                    f = this[0].attributes;
                    for (var l = 0, h = f.length; l < h; l++)n = f[l].name, n.indexOf("data-") ===
                    0 && (n = c.camelCase(n.substring(5)), R(this[0], n, b[n]));
                    c._data(this[0], "parsedAttrs", !0)
                }
                return b
            }
            if (typeof a == "object")return this.each(function () {
                c.data(this, a)
            });
            g = a.split(".");
            g[1] = g[1] ? "." + g[1] : "";
            return d === e ? (b = this.triggerHandler("getData" + g[1] + "!", [g[0]]), b === e && this.length && (b = c.data(this[0], a), b = R(this[0], a, b)), b === e && g[1] ? this.data(g[0]) : b) : this.each(function () {
                var f = c(this), n = [g[0], d];
                f.triggerHandler("setData" + g[1] + "!", n);
                c.data(this, a, d);
                f.triggerHandler("changeData" + g[1] + "!", n)
            })
        }, removeData: function (a) {
            return this.each(function () {
                c.removeData(this,
                    a)
            })
        }
    });
    c.extend({
        _mark: function (a, d) {
            a && (d = (d || "fx") + "mark", c._data(a, d, (c._data(a, d) || 0) + 1))
        }, _unmark: function (a, d, g) {
            a !== !0 && (g = d, d = a, a = !1);
            if (d) {
                var g = g || "fx", f = g + "mark";
                (a = a ? 0 : (c._data(d, f) || 1) - 1) ? c._data(d, f, a) : (c.removeData(d, f, !0), E(d, g, "mark"))
            }
        }, queue: function (a, d, g) {
            var f;
            if (a)return d = (d || "fx") + "queue", f = c._data(a, d), g && (!f || c.isArray(g) ? f = c._data(a, d, c.makeArray(g)) : f.push(g)), f || []
        }, dequeue: function (a, d) {
            var d = d || "fx", g = c.queue(a, d), f = g.shift(), n = {};
            f === "inprogress" && (f = g.shift());
            f &&
            (d === "fx" && g.unshift("inprogress"), c._data(a, d + ".run", n), f.call(a, function () {
                c.dequeue(a, d)
            }, n));
            g.length || (c.removeData(a, d + "queue " + d + ".run", !0), E(a, d, "queue"))
        }
    });
    c.fn.extend({
        queue: function (a, d) {
            typeof a != "string" && (d = a, a = "fx");
            return d === e ? c.queue(this[0], a) : this.each(function () {
                var g = c.queue(this, a, d);
                a === "fx" && g[0] !== "inprogress" && c.dequeue(this, a)
            })
        }, dequeue: function (a) {
            return this.each(function () {
                c.dequeue(this, a)
            })
        }, delay: function (a, d) {
            a = c.fx ? c.fx.speeds[a] || a : a;
            d = d || "fx";
            return this.queue(d,
                function (d, c) {
                    var n = setTimeout(d, a);
                    c.stop = function () {
                        clearTimeout(n)
                    }
                })
        }, clearQueue: function (a) {
            return this.queue(a || "fx", [])
        }, promise: function (a) {
            function d() {
                --b || g.resolveWith(f, [f])
            }

            typeof a != "string" && (a = e);
            a = a || "fx";
            var g = c.Deferred(), f = this, n = f.length, b = 1,
                l = a + "defer", h = a + "queue";
            a += "mark";
            for (var k; n--;)if (k = c.data(f[n], l, e, !0) || (c.data(f[n], h, e, !0) || c.data(f[n], a, e, !0)) && c.data(f[n], l, c.Callbacks("once memory"), !0)) b++, k.add(d);
            d();
            return g.promise()
        }
    });
    var M = /[\n\t\r]/g, H = /\s+/, da = /\r/g,
        ab = /^(?:button|input)$/i,
        bb = /^(?:button|input|object|select|textarea)$/i, cb = /^a(?:rea)?$/i,
        ya = /^(?:autofocus|autoplay|async|checked|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped|selected)$/i,
        za = c.support.getSetAttribute, T, Aa, Ba;
    c.fn.extend({
        attr: function (a, d) {
            return c.access(this, a, d, !0, c.attr)
        }, removeAttr: function (a) {
            return this.each(function () {
                c.removeAttr(this, a)
            })
        }, prop: function (a, d) {
            return c.access(this, a, d, !0, c.prop)
        }, removeProp: function (a) {
            a = c.propFix[a] || a;
            return this.each(function () {
                try {
                    this[a] =
                        e, delete this[a]
                } catch (d) {
                }
            })
        }, addClass: function (a) {
            var d, g, f, n, b, e, l;
            if (c.isFunction(a))return this.each(function (d) {
                c(this).addClass(a.call(this, d, this.className))
            });
            if (a && typeof a == "string") {
                d = a.split(H);
                for (g = 0, f = this.length; g < f; g++)if (n = this[g], n.nodeType === 1)if (!n.className && d.length === 1) n.className = a; else {
                    b = " " + n.className + " ";
                    for (e = 0, l = d.length; e < l; e++)~b.indexOf(" " + d[e] + " ") || (b += d[e] + " ");
                    n.className = c.trim(b)
                }
            }
            return this
        }, removeClass: function (a) {
            var d, g, f, n, b, l, h;
            if (c.isFunction(a))return this.each(function (d) {
                c(this).removeClass(a.call(this,
                    d, this.className))
            });
            if (a && typeof a == "string" || a === e) {
                d = (a || "").split(H);
                for (g = 0, f = this.length; g < f; g++)if (n = this[g], n.nodeType === 1 && n.className)if (a) {
                    b = (" " + n.className + " ").replace(M, " ");
                    for (l = 0, h = d.length; l < h; l++)b = b.replace(" " + d[l] + " ", " ");
                    n.className = c.trim(b)
                } else n.className = ""
            }
            return this
        }, toggleClass: function (a, d) {
            var g = typeof a, f = typeof d == "boolean";
            return c.isFunction(a) ? this.each(function (g) {
                c(this).toggleClass(a.call(this, g, this.className, d), d)
            }) : this.each(function () {
                if (g === "string")for (var n,
                                            b = 0, e = c(this), l = d, h = a.split(H); n = h[b++];)l = f ? l : !e.hasClass(n), e[l ? "addClass" : "removeClass"](n); else if (g === "undefined" || g === "boolean") this.className && c._data(this, "__className__", this.className), this.className = this.className || a === !1 ? "" : c._data(this, "__className__") || ""
            })
        }, hasClass: function (a) {
            for (var a = " " + a + " ", d = 0, c = this.length; d < c; d++)if (this[d].nodeType === 1 && (" " + this[d].className + " ").replace(M, " ").indexOf(a) > -1)return !0;
            return !1
        }, val: function (a) {
            var d, g, f, n = this[0];
            if (arguments.length)return f =
                c.isFunction(a), this.each(function (g) {
                var n = c(this), b;
                if (this.nodeType === 1 && (f ? b = a.call(this, g, n.val()) : b = a, b == null ? b = "" : typeof b == "number" ? b += "" : c.isArray(b) && (b = c.map(b, function (a) {
                            return a == null ? "" : a + ""
                        })), d = c.valHooks[this.nodeName.toLowerCase()] || c.valHooks[this.type], !d || !("set" in d) || d.set(this, b, "value") === e)) this.value = b
            });
            if (n) {
                if ((d = c.valHooks[n.nodeName.toLowerCase()] || c.valHooks[n.type]) && "get" in d && (g = d.get(n, "value")) !== e)return g;
                g = n.value;
                return typeof g == "string" ? g.replace(da, "") :
                    g == null ? "" : g
            }
        }
    });
    c.extend({
        valHooks: {
            option: {
                get: function (a) {
                    var d = a.attributes.value;
                    return !d || d.specified ? a.value : a.text
                }
            }, select: {
                get: function (a) {
                    var d, g, f = a.selectedIndex, n = [], b = a.options,
                        e = a.type === "select-one";
                    if (f < 0)return null;
                    for (a = e ? f : 0, g = e ? f + 1 : b.length; a < g; a++)if (d = b[a], d.selected && (c.support.optDisabled ? !d.disabled : d.getAttribute("disabled") === null) && (!d.parentNode.disabled || !c.nodeName(d.parentNode, "optgroup"))) {
                        d = c(d).val();
                        if (e)return d;
                        n.push(d)
                    }
                    return e && !n.length && b.length ? c(b[f]).val() :
                        n
                }, set: function (a, d) {
                    var g = c.makeArray(d);
                    c(a).find("option").each(function () {
                        this.selected = c.inArray(c(this).val(), g) >= 0
                    });
                    g.length || (a.selectedIndex = -1);
                    return g
                }
            }
        },
        attrFn: {
            val: !0,
            css: !0,
            html: !0,
            text: !0,
            data: !0,
            width: !0,
            height: !0,
            offset: !0
        },
        attr: function (a, d, g, f) {
            var n, b, l, h = a.nodeType;
            if (a && h !== 3 && h !== 8 && h !== 2) {
                if (f && d in c.attrFn)return c(a)[d](g);
                if (typeof a.getAttribute == "undefined")return c.prop(a, d, g);
                l = h !== 1 || !c.isXMLDoc(a);
                l && (d = d.toLowerCase(), b = c.attrHooks[d] || (ya.test(d) ? Aa : T));
                if (g !==
                    e) {
                    if (g === null) {
                        c.removeAttr(a, d);
                        return
                    }
                    if (b && "set" in b && l && (n = b.set(a, g, d)) !== e)return n;
                    a.setAttribute(d, "" + g);
                    return g
                }
                if (b && "get" in b && l && (n = b.get(a, d)) !== null)return n;
                n = a.getAttribute(d);
                return n === null ? e : n
            }
        },
        removeAttr: function (a, d) {
            var g, f, n, b, e = 0;
            if (d && a.nodeType === 1)for (f = d.toLowerCase().split(H), b = f.length; e < b; e++)n = f[e], n && (g = c.propFix[n] || n, c.attr(a, n, ""), a.removeAttribute(za ? n : g), ya.test(n) && g in a && (a[g] = !1))
        },
        attrHooks: {
            type: {
                set: function (a, d) {
                    if (ab.test(a.nodeName) && a.parentNode) c.error("type property can't be changed");
                    else if (!c.support.radioValue && d === "radio" && c.nodeName(a, "input")) {
                        var g = a.value;
                        a.setAttribute("type", d);
                        g && (a.value = g);
                        return d
                    }
                }
            }, value: {
                get: function (a, d) {
                    return T && c.nodeName(a, "button") ? T.get(a, d) : d in a ? a.value : null
                }, set: function (a, d, g) {
                    if (T && c.nodeName(a, "button"))return T.set(a, d, g);
                    a.value = d
                }
            }
        },
        propFix: {
            tabindex: "tabIndex",
            readonly: "readOnly",
            "for": "htmlFor",
            "class": "className",
            maxlength: "maxLength",
            cellspacing: "cellSpacing",
            cellpadding: "cellPadding",
            rowspan: "rowSpan",
            colspan: "colSpan",
            usemap: "useMap",
            frameborder: "frameBorder",
            contenteditable: "contentEditable"
        },
        prop: function (a, d, g) {
            var f, n, b, l = a.nodeType;
            if (a && l !== 3 && l !== 8 && l !== 2)return b = l !== 1 || !c.isXMLDoc(a), b && (d = c.propFix[d] || d, n = c.propHooks[d]), g !== e ? n && "set" in n && (f = n.set(a, g, d)) !== e ? f : a[d] = g : n && "get" in n && (f = n.get(a, d)) !== null ? f : a[d]
        },
        propHooks: {
            tabIndex: {
                get: function (a) {
                    var d = a.getAttributeNode("tabindex");
                    return d && d.specified ? parseInt(d.value, 10) : bb.test(a.nodeName) || cb.test(a.nodeName) && a.href ? 0 : e
                }
            }
        }
    });
    c.attrHooks.tabindex = c.propHooks.tabIndex;
    Aa = {
        get: function (a, d) {
            var g, f = c.prop(a, d);
            return f === !0 || typeof f != "boolean" && (g = a.getAttributeNode(d)) && g.nodeValue !== !1 ? d.toLowerCase() : e
        }, set: function (a, d, g) {
            var f;
            d === !1 ? c.removeAttr(a, g) : (f = c.propFix[g] || g, f in a && (a[f] = !0), a.setAttribute(g, g.toLowerCase()));
            return g
        }
    };
    za || (Ba = {name: !0, id: !0}, T = c.valHooks.button = {
        get: function (a, d) {
            var c;
            return (c = a.getAttributeNode(d)) && (Ba[d] ? c.nodeValue !== "" : c.specified) ? c.nodeValue : e
        }, set: function (a, d, c) {
            var f = a.getAttributeNode(c);
            f || (f = v.createAttribute(c),
                a.setAttributeNode(f));
            return f.nodeValue = d + ""
        }
    }, c.attrHooks.tabindex.set = T.set, c.each(["width", "height"], function (a, d) {
        c.attrHooks[d] = c.extend(c.attrHooks[d], {
            set: function (a, c) {
                if (c === "")return a.setAttribute(d, "auto"), c
            }
        })
    }), c.attrHooks.contenteditable = {
        get: T.get, set: function (a, d, c) {
            d === "" && (d = "false");
            T.set(a, d, c)
        }
    });
    c.support.hrefNormalized || c.each(["href", "src", "width", "height"], function (a, d) {
        c.attrHooks[d] = c.extend(c.attrHooks[d], {
            get: function (a) {
                a = a.getAttribute(d, 2);
                return a === null ? e : a
            }
        })
    });
    c.support.style || (c.attrHooks.style = {
        get: function (a) {
            return a.style.cssText.toLowerCase() || e
        }, set: function (a, d) {
            return a.style.cssText = "" + d
        }
    });
    c.support.optSelected || (c.propHooks.selected = c.extend(c.propHooks.selected, {
        get: function () {
            return null
        }
    }));
    c.support.enctype || (c.propFix.enctype = "encoding");
    c.support.checkOn || c.each(["radio", "checkbox"], function () {
        c.valHooks[this] = {
            get: function (a) {
                return a.getAttribute("value") === null ? "on" : a.value
            }
        }
    });
    c.each(["radio", "checkbox"], function () {
        c.valHooks[this] =
            c.extend(c.valHooks[this], {
                set: function (a, d) {
                    if (c.isArray(d))return a.checked = c.inArray(c(a).val(), d) >= 0
                }
            })
    });
    var oa = /^(?:textarea|input|select)$/i, Ca = /^([^\.]*)?(?:\.(.+))?$/,
        db = /\bhover(\.\S+)?\b/, eb = /^key/,
        fb = /^(?:mouse|contextmenu)|click/,
        Da = /^(?:focusinfocus|focusoutblur)$/,
        gb = /^(\w*)(?:#([\w\-]+))?(?:\.([\w\-]+))?$/, hb = function (a) {
            (a = gb.exec(a)) && (a[1] = (a[1] || "").toLowerCase(), a[3] = a[3] && RegExp("(?:^|\\s)" + a[3] + "(?:\\s|$)"));
            return a
        }, Ea = function (a) {
            return c.event.special.hover ? a : a.replace(db,
                "mouseenter$1 mouseleave$1")
        };
    c.event = {
        add: function (a, d, g, f, n) {
            var b, l, h, k, j, m, o, p, A, w;
            if (!(a.nodeType === 3 || a.nodeType === 8 || !d || !g || !(b = c._data(a)))) {
                g.handler && (p = g, g = p.handler);
                g.guid || (g.guid = c.guid++);
                h = b.events;
                h || (b.events = h = {});
                l = b.handle;
                l || (b.handle = l = function (a) {
                    return typeof c != "undefined" && (!a || c.event.triggered !== a.type) ? c.event.dispatch.apply(l.elem, arguments) : e
                }, l.elem = a);
                d = c.trim(Ea(d)).split(" ");
                for (b = 0; b < d.length; b++) {
                    k = Ca.exec(d[b]) || [];
                    j = k[1];
                    m = (k[2] || "").split(".").sort();
                    w =
                        c.event.special[j] || {};
                    j = (n ? w.delegateType : w.bindType) || j;
                    w = c.event.special[j] || {};
                    o = c.extend({
                        type: j,
                        origType: k[1],
                        data: f,
                        handler: g,
                        guid: g.guid,
                        selector: n,
                        quick: hb(n),
                        namespace: m.join(".")
                    }, p);
                    A = h[j];
                    if (!A && (A = h[j] = [], A.delegateCount = 0, !w.setup || w.setup.call(a, f, m, l) === !1)) a.addEventListener ? a.addEventListener(j, l, !1) : a.attachEvent && a.attachEvent("on" + j, l);
                    w.add && (w.add.call(a, o), o.handler.guid || (o.handler.guid = g.guid));
                    n ? A.splice(A.delegateCount++, 0, o) : A.push(o);
                    c.event.global[j] = !0
                }
                a = null
            }
        },
        global: {},
        remove: function (a, d, g, f, n) {
            var b = c.hasData(a) && c._data(a), l, e, h, k, j, m, o, p, A, w, r,
                s;
            if (b && (p = b.events)) {
                d = c.trim(Ea(d || "")).split(" ");
                for (l = 0; l < d.length; l++)if (e = Ca.exec(d[l]) || [], h = k = e[1], j = e[2], h) {
                    A = c.event.special[h] || {};
                    h = (f ? A.delegateType : A.bindType) || h;
                    r = p[h] || [];
                    m = r.length;
                    j = j ? RegExp("(^|\\.)" + j.split(".").sort().join("\\.(?:.*\\.)?") + "(\\.|$)") : null;
                    for (o = 0; o < r.length; o++)s = r[o], (n || k === s.origType) && (!g || g.guid === s.guid) && (!j || j.test(s.namespace)) && (!f || f === s.selector || f === "**" && s.selector) &&
                    (r.splice(o--, 1), s.selector && r.delegateCount--, A.remove && A.remove.call(a, s));
                    r.length === 0 && m !== r.length && ((!A.teardown || A.teardown.call(a, j) === !1) && c.removeEvent(a, h, b.handle), delete p[h])
                } else for (h in p)c.event.remove(a, h + d[l], g, f, !0);
                c.isEmptyObject(p) && (w = b.handle, w && (w.elem = null), c.removeData(a, ["events", "handle"], !0))
            }
        },
        customEvent: {getData: !0, setData: !0, changeData: !0},
        trigger: function (a, d, g, f) {
            if (!g || g.nodeType !== 3 && g.nodeType !== 8) {
                var n = a.type || a, l = [], h, j, k, m, o, p, A;
                if (!Da.test(n + c.event.triggered) &&
                    (n.indexOf("!") >= 0 && (n = n.slice(0, -1), h = !0), n.indexOf(".") >= 0 && (l = n.split("."), n = l.shift(), l.sort()), g && !c.event.customEvent[n] || c.event.global[n]))if (a = typeof a == "object" ? a[c.expando] ? a : new c.Event(n, a) : new c.Event(n), a.type = n, a.isTrigger = !0, a.exclusive = h, a.namespace = l.join("."), a.namespace_re = a.namespace ? RegExp("(^|\\.)" + l.join("\\.(?:.*\\.)?") + "(\\.|$)") : null, l = n.indexOf(":") < 0 ? "on" + n : "", g) {
                    if (a.result = e, a.target || (a.target = g), d = d != null ? c.makeArray(d) : [], d.unshift(a), h = c.event.special[n] || {}, !(h.trigger &&
                        h.trigger.apply(g, d) === !1)) {
                        p = [[g, h.bindType || n]];
                        if (!f && !h.noBubble && !c.isWindow(g)) {
                            for (A = h.delegateType || n, k = Da.test(A + n) ? g : g.parentNode, m = null; k; k = k.parentNode)p.push([k, A]), m = k;
                            m && m === g.ownerDocument && p.push([m.defaultView || m.parentWindow || b, A])
                        }
                        for (j = 0; j < p.length && !a.isPropagationStopped(); j++)k = p[j][0], a.type = p[j][1], o = (c._data(k, "events") || {})[a.type] && c._data(k, "handle"), o && o.apply(k, d), o = l && k[l], o && c.acceptData(k) && o.apply(k, d) === !1 && a.preventDefault();
                        a.type = n;
                        !f && !a.isDefaultPrevented() &&
                        (!h._default || h._default.apply(g.ownerDocument, d) === !1) && (n !== "click" || !c.nodeName(g, "a")) && c.acceptData(g) && l && g[n] && (n !== "focus" && n !== "blur" || a.target.offsetWidth !== 0) && !c.isWindow(g) && (m = g[l], m && (g[l] = null), c.event.triggered = n, g[n](), c.event.triggered = e, m && (g[l] = m));
                        return a.result
                    }
                } else for (j in g = c.cache, g)g[j].events && g[j].events[n] && c.event.trigger(a, d, g[j].handle.elem, !0)
            }
        },
        dispatch: function (a) {
            var a = c.event.fix(a || b.event),
                d = (c._data(this, "events") || {})[a.type] || [],
                g = d.delegateCount,
                f = [].slice.call(arguments,
                    0), n = !a.exclusive && !a.namespace, l = [], h, k, j, m, o,
                p, A, w, r;
            f[0] = a;
            a.delegateTarget = this;
            if (g && !a.target.disabled && (!a.button || a.type !== "click")) {
                j = c(this);
                j.context = this.ownerDocument || this;
                for (k = a.target; k != this; k = k.parentNode || this) {
                    o = {};
                    A = [];
                    j[0] = k;
                    for (h = 0; h < g; h++) {
                        w = d[h];
                        r = w.selector;
                        if (o[r] === e) {
                            var s = o, u = r, q;
                            if (w.quick) {
                                q = w.quick;
                                var x = k.attributes || {};
                                q = (!q[1] || k.nodeName.toLowerCase() === q[1]) && (!q[2] || (x.id || {}).value === q[2]) && (!q[3] || q[3].test((x["class"] || {}).value))
                            } else q = j.is(r);
                            s[u] = q
                        }
                        o[r] &&
                        A.push(w)
                    }
                    A.length && l.push({elem: k, matches: A})
                }
            }
            d.length > g && l.push({elem: this, matches: d.slice(g)});
            for (h = 0; h < l.length && !a.isPropagationStopped(); h++) {
                p = l[h];
                a.currentTarget = p.elem;
                for (d = 0; d < p.matches.length && !a.isImmediatePropagationStopped(); d++)if (w = p.matches[d], n || !a.namespace && !w.namespace || a.namespace_re && a.namespace_re.test(w.namespace)) a.data = w.data, a.handleObj = w, m = ((c.event.special[w.origType] || {}).handle || w.handler).apply(p.elem, f), m !== e && (a.result = m, m === !1 && (a.preventDefault(), a.stopPropagation()))
            }
            return a.result
        },
        props: "attrChange attrName relatedNode srcElement altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),
        fixHooks: {},
        keyHooks: {
            props: "char charCode key keyCode".split(" "),
            filter: function (a, d) {
                a.which == null && (a.which = d.charCode != null ? d.charCode : d.keyCode);
                return a
            }
        },
        mouseHooks: {
            props: "button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),
            filter: function (a, d) {
                var c, f, n,
                    b = d.button, l = d.fromElement;
                a.pageX == null && d.clientX != null && (c = a.target.ownerDocument || v, f = c.documentElement, n = c.body, a.pageX = d.clientX + (f && f.scrollLeft || n && n.scrollLeft || 0) - (f && f.clientLeft || n && n.clientLeft || 0), a.pageY = d.clientY + (f && f.scrollTop || n && n.scrollTop || 0) - (f && f.clientTop || n && n.clientTop || 0));
                !a.relatedTarget && l && (a.relatedTarget = l === a.target ? d.toElement : l);
                !a.which && b !== e && (a.which = b & 1 ? 1 : b & 2 ? 3 : b & 4 ? 2 : 0);
                return a
            }
        },
        fix: function (a) {
            if (a[c.expando])return a;
            var d, g, f = a, n = c.event.fixHooks[a.type] ||
                    {}, b = n.props ? this.props.concat(n.props) : this.props,
                a = c.Event(f);
            for (d = b.length; d;)g = b[--d], a[g] = f[g];
            a.target || (a.target = f.srcElement || v);
            a.target.nodeType === 3 && (a.target = a.target.parentNode);
            a.metaKey === e && (a.metaKey = a.ctrlKey);
            return n.filter ? n.filter(a, f) : a
        },
        special: {
            ready: {setup: c.bindReady},
            load: {noBubble: !0},
            focus: {delegateType: "focusin"},
            blur: {delegateType: "focusout"},
            beforeunload: {
                setup: function (a, d, g) {
                    c.isWindow(this) && (this.onbeforeunload = g)
                }, teardown: function (a, d) {
                    this.onbeforeunload ===
                    d && (this.onbeforeunload = null)
                }
            }
        },
        simulate: function (a, d, g, f) {
            a = c.extend(new c.Event, g, {
                type: a,
                isSimulated: !0,
                originalEvent: {}
            });
            f ? c.event.trigger(a, null, d) : c.event.dispatch.call(d, a);
            a.isDefaultPrevented() && g.preventDefault()
        }
    };
    c.event.handle = c.event.dispatch;
    c.removeEvent = v.removeEventListener ? function (a, d, c) {
        a.removeEventListener && a.removeEventListener(d, c, !1)
    } : function (a, d, c) {
        a.detachEvent && a.detachEvent("on" + d, c)
    };
    c.Event = function (a, d) {
        if (!(this instanceof c.Event))return new c.Event(a, d);
        a && a.type ?
            (this.originalEvent = a, this.type = a.type, this.isDefaultPrevented = a.defaultPrevented || a.returnValue === !1 || a.getPreventDefault && a.getPreventDefault() ? P : I) : this.type = a;
        d && c.extend(this, d);
        this.timeStamp = a && a.timeStamp || c.now();
        this[c.expando] = !0
    };
    c.Event.prototype = {
        preventDefault: function () {
            this.isDefaultPrevented = P;
            var a = this.originalEvent;
            !a || (a.preventDefault ? a.preventDefault() : a.returnValue = !1)
        },
        stopPropagation: function () {
            this.isPropagationStopped = P;
            var a = this.originalEvent;
            !a || (a.stopPropagation &&
            a.stopPropagation(), a.cancelBubble = !0)
        },
        stopImmediatePropagation: function () {
            this.isImmediatePropagationStopped = P;
            this.stopPropagation()
        },
        isDefaultPrevented: I,
        isPropagationStopped: I,
        isImmediatePropagationStopped: I
    };
    c.each({mouseenter: "mouseover", mouseleave: "mouseout"}, function (a, d) {
        c.event.special[a] = {
            delegateType: d, bindType: d, handle: function (a) {
                var f = a.relatedTarget, n = a.handleObj, b;
                if (!f || f !== this && !c.contains(this, f)) a.type = n.origType, b = n.handler.apply(this, arguments), a.type = d;
                return b
            }
        }
    });
    c.support.submitBubbles ||
    (c.event.special.submit = {
        setup: function () {
            if (c.nodeName(this, "form"))return !1;
            c.event.add(this, "click._submit keypress._submit", function (a) {
                a = a.target;
                (a = c.nodeName(a, "input") || c.nodeName(a, "button") ? a.form : e) && !a._submit_attached && (c.event.add(a, "submit._submit", function (a) {
                    this.parentNode && !a.isTrigger && c.event.simulate("submit", this.parentNode, a, !0)
                }), a._submit_attached = !0)
            })
        }, teardown: function () {
            if (c.nodeName(this, "form"))return !1;
            c.event.remove(this, "._submit")
        }
    });
    c.support.changeBubbles || (c.event.special.change =
        {
            setup: function () {
                if (oa.test(this.nodeName)) {
                    if (this.type === "checkbox" || this.type === "radio") c.event.add(this, "propertychange._change", function (a) {
                        a.originalEvent.propertyName === "checked" && (this._just_changed = !0)
                    }), c.event.add(this, "click._change", function (a) {
                        this._just_changed && !a.isTrigger && (this._just_changed = !1, c.event.simulate("change", this, a, !0))
                    });
                    return !1
                }
                c.event.add(this, "beforeactivate._change", function (a) {
                    a = a.target;
                    oa.test(a.nodeName) && !a._change_attached && (c.event.add(a, "change._change",
                        function (a) {
                            this.parentNode && !a.isSimulated && !a.isTrigger && c.event.simulate("change", this.parentNode, a, !0)
                        }), a._change_attached = !0)
                })
            }, handle: function (a) {
            var d = a.target;
            if (this !== d || a.isSimulated || a.isTrigger || d.type !== "radio" && d.type !== "checkbox")return a.handleObj.handler.apply(this, arguments)
        }, teardown: function () {
            c.event.remove(this, "._change");
            return oa.test(this.nodeName)
        }
        });
    c.support.focusinBubbles || c.each({
        focus: "focusin",
        blur: "focusout"
    }, function (a, d) {
        var g = 0, f = function (a) {
            c.event.simulate(d,
                a.target, c.event.fix(a), !0)
        };
        c.event.special[d] = {
            setup: function () {
                g++ === 0 && v.addEventListener(a, f, !0)
            }, teardown: function () {
                --g === 0 && v.removeEventListener(a, f, !0)
            }
        }
    });
    c.fn.extend({
        on: function (a, d, g, f, b) {
            var l, h;
            if (typeof a == "object") {
                typeof d != "string" && (g = d, d = e);
                for (h in a)this.on(h, d, g, a[h], b);
                return this
            }
            g == null && f == null ? (f = d, g = d = e) : f == null && (typeof d == "string" ? (f = g, g = e) : (f = g, g = d, d = e));
            if (f === !1) f = I; else if (!f)return this;
            b === 1 && (l = f, f = function (a) {
                c().off(a);
                return l.apply(this, arguments)
            }, f.guid =
                l.guid || (l.guid = c.guid++));
            return this.each(function () {
                c.event.add(this, a, f, g, d)
            })
        }, one: function (a, d, c, f) {
            return this.on.call(this, a, d, c, f, 1)
        }, off: function (a, d, g) {
            if (a && a.preventDefault && a.handleObj) {
                var f = a.handleObj;
                c(a.delegateTarget).off(f.namespace ? f.type + "." + f.namespace : f.type, f.selector, f.handler);
                return this
            }
            if (typeof a == "object") {
                for (f in a)this.off(f, d, a[f]);
                return this
            }
            if (d === !1 || typeof d == "function") g = d, d = e;
            g === !1 && (g = I);
            return this.each(function () {
                c.event.remove(this, a, g, d)
            })
        }, bind: function (a,
                           d, c) {
            return this.on(a, null, d, c)
        }, unbind: function (a, d) {
            return this.off(a, null, d)
        }, live: function (a, d, g) {
            c(this.context).on(a, this.selector, d, g);
            return this
        }, die: function (a, d) {
            c(this.context).off(a, this.selector || "**", d);
            return this
        }, delegate: function (a, d, c, f) {
            return this.on(d, a, c, f)
        }, undelegate: function (a, d, c) {
            return arguments.length == 1 ? this.off(a, "**") : this.off(d, a, c)
        }, trigger: function (a, d) {
            return this.each(function () {
                c.event.trigger(a, d, this)
            })
        }, triggerHandler: function (a, d) {
            if (this[0])return c.event.trigger(a,
                d, this[0], !0)
        }, toggle: function (a) {
            var d = arguments, g = a.guid || c.guid++, f = 0, b = function (g) {
                var b = (c._data(this, "lastToggle" + a.guid) || 0) % f;
                c._data(this, "lastToggle" + a.guid, b + 1);
                g.preventDefault();
                return d[b].apply(this, arguments) || !1
            };
            for (b.guid = g; f < d.length;)d[f++].guid = g;
            return this.click(b)
        }, hover: function (a, d) {
            return this.mouseenter(a).mouseleave(d || a)
        }
    });
    c.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error contextmenu".split(" "),
        function (a, d) {
            c.fn[d] = function (a, c) {
                c == null && (c = a, a = null);
                return arguments.length > 0 ? this.on(d, null, a, c) : this.trigger(d)
            };
            c.attrFn && (c.attrFn[d] = !0);
            eb.test(d) && (c.event.fixHooks[d] = c.event.keyHooks);
            fb.test(d) && (c.event.fixHooks[d] = c.event.mouseHooks)
        });
    (function () {
        function a(a, d, c, g, b, n) {
            for (var b = 0, l = g.length; b < l; b++) {
                var e = g[b];
                if (e) {
                    for (var h = !1, e = e[a]; e;) {
                        if (e[f] === c) {
                            h = g[e.sizset];
                            break
                        }
                        if (e.nodeType === 1)if (n || (e[f] = c, e.sizset = b), typeof d != "string") {
                            if (e === d) {
                                h = !0;
                                break
                            }
                        } else if (p.filter(d,
                                [e]).length > 0) {
                            h = e;
                            break
                        }
                        e = e[a]
                    }
                    g[b] = h
                }
            }
        }

        function d(a, d, c, g, b, n) {
            for (var b = 0, l = g.length; b < l; b++) {
                var e = g[b];
                if (e) {
                    for (var h = !1, e = e[a]; e;) {
                        if (e[f] === c) {
                            h = g[e.sizset];
                            break
                        }
                        e.nodeType === 1 && !n && (e[f] = c, e.sizset = b);
                        if (e.nodeName.toLowerCase() === d) {
                            h = e;
                            break
                        }
                        e = e[a]
                    }
                    g[b] = h
                }
            }
        }

        var g = /((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^\[\]]*\]|['"][^'"]*['"]|[^\[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g,
            f = "sizcache" + (Math.random() + "").replace(".", ""), b = 0,
            l = Object.prototype.toString, h = !1,
            k = !0, j =
                /\\/g, m = /\r\n/g, o = /\W/;
        [0, 0].sort(function () {
            k = !1;
            return 0
        });
        var p = function (a, d, c, f) {
            c = c || [];
            d = d || v;
            var b = d;
            if (d.nodeType !== 1 && d.nodeType !== 9)return [];
            if (!a || typeof a != "string")return c;
            var n, e, h, k, j, m, o, A = !0, q = p.isXML(d), s = [], N = a;
            do if (g.exec(""), n = g.exec(N), n && (N = n[3], s.push(n[1]), n[2])) {
                k = n[3];
                break
            } while (n);
            if (s.length > 1 && r.exec(a))if (s.length === 2 && w.relative[s[0]]) e = B(s[0] + s[1], d, f); else for (e = w.relative[s[0]] ? [d] : p(s.shift(), d); s.length;)a = s.shift(), w.relative[a] && (a += s.shift()), e = B(a, e, f);
            else if (!f && s.length > 1 && d.nodeType === 9 && !q && w.match.ID.test(s[0]) && !w.match.ID.test(s[s.length - 1]) && (j = p.find(s.shift(), d, q), d = j.expr ? p.filter(j.expr, j.set)[0] : j.set[0]), d)for (j = f ? {
                expr: s.pop(),
                set: u(f)
            } : p.find(s.pop(), s.length === 1 && (s[0] === "~" || s[0] === "+") && d.parentNode ? d.parentNode : d, q), e = j.expr ? p.filter(j.expr, j.set) : j.set, s.length > 0 ? h = u(e) : A = !1; s.length;)m = s.pop(), o = m, w.relative[m] ? o = s.pop() : m = "", o == null && (o = d), w.relative[m](h, o, q); else h = [];
            h || (h = e);
            h || p.error(m || a);
            if (l.call(h) === "[object Array]")if (A)if (d &&
                d.nodeType === 1)for (a = 0; h[a] != null; a++)h[a] && (h[a] === !0 || h[a].nodeType === 1 && p.contains(d, h[a])) && c.push(e[a]); else for (a = 0; h[a] != null; a++)h[a] && h[a].nodeType === 1 && c.push(e[a]); else c.push.apply(c, h); else u(h, c);
            k && (p(k, b, c, f), p.uniqueSort(c));
            return c
        };
        p.uniqueSort = function (a) {
            if (t && (h = k, a.sort(t), h))for (var d = 1; d < a.length; d++)a[d] === a[d - 1] && a.splice(d--, 1);
            return a
        };
        p.matches = function (a, d) {
            return p(a, null, null, d)
        };
        p.matchesSelector = function (a, d) {
            return p(d, null, null, [a]).length > 0
        };
        p.find = function (a,
                           d, c) {
            var f, g, b, n, e, l;
            if (!a)return [];
            for (g = 0, b = w.order.length; g < b; g++)if (e = w.order[g], n = w.leftMatch[e].exec(a))if (l = n[1], n.splice(1, 1), l.substr(l.length - 1) !== "\\" && (n[1] = (n[1] || "").replace(j, ""), f = w.find[e](n, d, c), f != null)) {
                a = a.replace(w.match[e], "");
                break
            }
            f || (f = typeof d.getElementsByTagName != "undefined" ? d.getElementsByTagName("*") : []);
            return {set: f, expr: a}
        };
        p.filter = function (a, d, c, f) {
            for (var g, b, n, l, h, j, k, m, o = a, A = [], s = d, q = d && d[0] && p.isXML(d[0]); a && d.length;) {
                for (n in w.filter)if ((g = w.leftMatch[n].exec(a)) !=
                    null && g[2])if (j = w.filter[n], h = g[1], b = !1, g.splice(1, 1), h.substr(h.length - 1) !== "\\") {
                    s === A && (A = []);
                    if (w.preFilter[n])if (g = w.preFilter[n](g, s, c, A, f, q)) {
                        if (g === !0)continue
                    } else b = l = !0;
                    if (g)for (k = 0; (h = s[k]) != null; k++)h && (l = j(h, g, k, s), m = f ^ l, c && l != null ? m ? b = !0 : s[k] = !1 : m && (A.push(h), b = !0));
                    if (l !== e) {
                        c || (s = A);
                        a = a.replace(w.match[n], "");
                        if (!b)return [];
                        break
                    }
                }
                if (a === o)if (b == null) p.error(a); else break;
                o = a
            }
            return s
        };
        p.error = function (a) {
            throw Error("Syntax error, unrecognized expression: " + a);
        };
        var A = p.getText =
            function (a) {
                var d, c;
                d = a.nodeType;
                var f = "";
                if (d)if (d === 1 || d === 9) {
                    if (typeof a.textContent == "string")return a.textContent;
                    if (typeof a.innerText == "string")return a.innerText.replace(m, "");
                    for (a = a.firstChild; a; a = a.nextSibling)f += A(a)
                } else {
                    if (d === 3 || d === 4)return a.nodeValue
                } else for (d = 0; c = a[d]; d++)c.nodeType !== 8 && (f += A(c));
                return f
            }, w = p.selectors = {
            order: ["ID", "NAME", "TAG"],
            match: {
                ID: /#((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,
                CLASS: /\.((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,
                NAME: /\[name=['"]*((?:[\w\u00c0-\uFFFF\-]|\\.)+)['"]*\]/,
                ATTR: /\[\s*((?:[\w\u00c0-\uFFFF\-]|\\.)+)\s*(?:(\S?=)\s*(?:(['"])(.*?)\3|(#?(?:[\w\u00c0-\uFFFF\-]|\\.)*)|)|)\s*\]/,
                TAG: /^((?:[\w\u00c0-\uFFFF\*\-]|\\.)+)/,
                CHILD: /:(only|nth|last|first)-child(?:\(\s*(even|odd|(?:[+\-]?\d+|(?:[+\-]?\d*)?n\s*(?:[+\-]\s*\d+)?))\s*\))?/,
                POS: /:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^\-]|$)/,
                PSEUDO: /:((?:[\w\u00c0-\uFFFF\-]|\\.)+)(?:\((['"]?)((?:\([^\)]+\)|[^\(\)]*)+)\2\))?/
            },
            leftMatch: {},
            attrMap: {"class": "className", "for": "htmlFor"},
            attrHandle: {
                href: function (a) {
                    return a.getAttribute("href")
                },
                type: function (a) {
                    return a.getAttribute("type")
                }
            },
            relative: {
                "+": function (a, d) {
                    var c = typeof d == "string", f = c && !o.test(d),
                        c = c && !f;
                    f && (d = d.toLowerCase());
                    for (var f = 0, g = a.length, b; f < g; f++)if (b = a[f]) {
                        for (; (b = b.previousSibling) && b.nodeType !== 1;);
                        a[f] = c || b && b.nodeName.toLowerCase() === d ? b || !1 : b === d
                    }
                    c && p.filter(d, a, !0)
                }, ">": function (a, d) {
                    var c, f = typeof d == "string", g = 0, b = a.length;
                    if (f && !o.test(d))for (d = d.toLowerCase(); g < b; g++) {
                        if (c = a[g]) c = c.parentNode, a[g] = c.nodeName.toLowerCase() === d ? c : !1
                    } else {
                        for (; g < b; g++)c =
                            a[g], c && (a[g] = f ? c.parentNode : c.parentNode === d);
                        f && p.filter(d, a, !0)
                    }
                }, "": function (c, f, g) {
                    var l, e = b++, h = a;
                    typeof f == "string" && !o.test(f) && (f = f.toLowerCase(), l = f, h = d);
                    h("parentNode", f, e, c, l, g)
                }, "~": function (c, f, g) {
                    var l, e = b++, h = a;
                    typeof f == "string" && !o.test(f) && (f = f.toLowerCase(), l = f, h = d);
                    h("previousSibling", f, e, c, l, g)
                }
            },
            find: {
                ID: function (a, d, c) {
                    if (typeof d.getElementById != "undefined" && !c)return (a = d.getElementById(a[1])) && a.parentNode ? [a] : []
                }, NAME: function (a, d) {
                    if (typeof d.getElementsByName != "undefined") {
                        for (var c =
                            [], f = d.getElementsByName(a[1]), g = 0, b = f.length; g < b; g++)f[g].getAttribute("name") === a[1] && c.push(f[g]);
                        return c.length === 0 ? null : c
                    }
                }, TAG: function (a, d) {
                    if (typeof d.getElementsByTagName != "undefined")return d.getElementsByTagName(a[1])
                }
            },
            preFilter: {
                CLASS: function (a, d, c, f, g, b) {
                    a = " " + a[1].replace(j, "") + " ";
                    if (b)return a;
                    for (var b = 0, n; (n = d[b]) != null; b++)n && (g ^ (n.className && (" " + n.className + " ").replace(/[\t\n\r]/g, " ").indexOf(a) >= 0) ? c || f.push(n) : c && (d[b] = !1));
                    return !1
                }, ID: function (a) {
                    return a[1].replace(j,
                        "")
                }, TAG: function (a) {
                    return a[1].replace(j, "").toLowerCase()
                }, CHILD: function (a) {
                    if (a[1] === "nth") {
                        a[2] || p.error(a[0]);
                        a[2] = a[2].replace(/^\+|\s*/g, "");
                        var d = /(-?)(\d*)(?:n([+\-]?\d*))?/.exec(a[2] === "even" && "2n" || a[2] === "odd" && "2n+1" || !/\D/.test(a[2]) && "0n+" + a[2] || a[2]);
                        a[2] = d[1] + (d[2] || 1) - 0;
                        a[3] = d[3] - 0
                    } else a[2] && p.error(a[0]);
                    a[0] = b++;
                    return a
                }, ATTR: function (a, d, c, f, g, b) {
                    d = a[1] = a[1].replace(j, "");
                    !b && w.attrMap[d] && (a[1] = w.attrMap[d]);
                    a[4] = (a[4] || a[5] || "").replace(j, "");
                    a[2] === "~=" && (a[4] = " " + a[4] +
                        " ");
                    return a
                }, PSEUDO: function (a, d, c, f, b) {
                    if (a[1] === "not")if ((g.exec(a[3]) || "").length > 1 || /^\w/.test(a[3])) a[3] = p(a[3], null, null, d); else return a = p.filter(a[3], d, c, 1 ^ b), c || f.push.apply(f, a), !1; else if (w.match.POS.test(a[0]) || w.match.CHILD.test(a[0]))return !0;
                    return a
                }, POS: function (a) {
                    a.unshift(!0);
                    return a
                }
            },
            filters: {
                enabled: function (a) {
                    return a.disabled === !1 && a.type !== "hidden"
                }, disabled: function (a) {
                    return a.disabled === !0
                }, checked: function (a) {
                    return a.checked === !0
                }, selected: function (a) {
                    return a.selected ===
                        !0
                }, parent: function (a) {
                    return !!a.firstChild
                }, empty: function (a) {
                    return !a.firstChild
                }, has: function (a, d, c) {
                    return !!p(c[3], a).length
                }, header: function (a) {
                    return /h\d/i.test(a.nodeName)
                }, text: function (a) {
                    var d = a.getAttribute("type"), c = a.type;
                    return a.nodeName.toLowerCase() === "input" && "text" === c && (d === c || d === null)
                }, radio: function (a) {
                    return a.nodeName.toLowerCase() === "input" && "radio" === a.type
                }, checkbox: function (a) {
                    return a.nodeName.toLowerCase() === "input" && "checkbox" === a.type
                }, file: function (a) {
                    return a.nodeName.toLowerCase() ===
                        "input" && "file" === a.type
                }, password: function (a) {
                    return a.nodeName.toLowerCase() === "input" && "password" === a.type
                }, submit: function (a) {
                    var d = a.nodeName.toLowerCase();
                    return (d === "input" || d === "button") && "submit" === a.type
                }, image: function (a) {
                    return a.nodeName.toLowerCase() === "input" && "image" === a.type
                }, reset: function (a) {
                    var d = a.nodeName.toLowerCase();
                    return (d === "input" || d === "button") && "reset" === a.type
                }, button: function (a) {
                    var d = a.nodeName.toLowerCase();
                    return d === "input" && "button" === a.type || d === "button"
                }, input: function (a) {
                    return /input|select|textarea|button/i.test(a.nodeName)
                },
                focus: function (a) {
                    return a === a.ownerDocument.activeElement
                }
            },
            setFilters: {
                first: function (a, d) {
                    return d === 0
                }, last: function (a, d, c, f) {
                    return d === f.length - 1
                }, even: function (a, d) {
                    return d % 2 === 0
                }, odd: function (a, d) {
                    return d % 2 === 1
                }, lt: function (a, d, c) {
                    return d < c[3] - 0
                }, gt: function (a, d, c) {
                    return d > c[3] - 0
                }, nth: function (a, d, c) {
                    return c[3] - 0 === d
                }, eq: function (a, d, c) {
                    return c[3] - 0 === d
                }
            },
            filter: {
                PSEUDO: function (a, d, c, f) {
                    var g = d[1], b = w.filters[g];
                    if (b)return b(a, c, d, f);
                    if (g === "contains")return (a.textContent || a.innerText ||
                        A([a]) || "").indexOf(d[3]) >= 0;
                    if (g === "not") {
                        d = d[3];
                        c = 0;
                        for (f = d.length; c < f; c++)if (d[c] === a)return !1;
                        return !0
                    }
                    p.error(g)
                }, CHILD: function (a, d) {
                    var c, g, b, n, l, e;
                    c = d[1];
                    e = a;
                    switch (c) {
                        case "only":
                        case "first":
                            for (; e = e.previousSibling;)if (e.nodeType === 1)return !1;
                            if (c === "first")return !0;
                            e = a;
                        case "last":
                            for (; e = e.nextSibling;)if (e.nodeType === 1)return !1;
                            return !0;
                        case "nth":
                            c = d[2];
                            g = d[3];
                            if (c === 1 && g === 0)return !0;
                            b = d[0];
                            n = a.parentNode;
                            if (n && (n[f] !== b || !a.nodeIndex)) {
                                l = 0;
                                for (e = n.firstChild; e; e = e.nextSibling)e.nodeType ===
                                1 && (e.nodeIndex = ++l);
                                n[f] = b
                            }
                            e = a.nodeIndex - g;
                            return c === 0 ? e === 0 : e % c === 0 && e / c >= 0
                    }
                }, ID: function (a, d) {
                    return a.nodeType === 1 && a.getAttribute("id") === d
                }, TAG: function (a, d) {
                    return d === "*" && a.nodeType === 1 || !!a.nodeName && a.nodeName.toLowerCase() === d
                }, CLASS: function (a, d) {
                    return (" " + (a.className || a.getAttribute("class")) + " ").indexOf(d) > -1
                }, ATTR: function (a, d) {
                    var c = d[1],
                        c = p.attr ? p.attr(a, c) : w.attrHandle[c] ? w.attrHandle[c](a) : a[c] != null ? a[c] : a.getAttribute(c),
                        f = c + "", g = d[2], b = d[4];
                    return c == null ? g === "!=" : !g && p.attr ?
                        c != null : g === "=" ? f === b : g === "*=" ? f.indexOf(b) >= 0 : g === "~=" ? (" " + f + " ").indexOf(b) >= 0 : b ? g === "!=" ? f !== b : g === "^=" ? f.indexOf(b) === 0 : g === "$=" ? f.substr(f.length - b.length) === b : g === "|=" ? f === b || f.substr(0, b.length + 1) === b + "-" : !1 : f && c !== !1
                }, POS: function (a, d, c, f) {
                    var g = w.setFilters[d[2]];
                    if (g)return g(a, c, d, f)
                }
            }
        }, r = w.match.POS, s = function (a, d) {
            return "\\" + (d - 0 + 1)
        }, q;
        for (q in w.match)w.match[q] = RegExp(w.match[q].source + /(?![^\[]*\])(?![^\(]*\))/.source), w.leftMatch[q] = RegExp(/(^(?:.|\r|\n)*?)/.source + w.match[q].source.replace(/\\(\d+)/g,
                s));
        var u = function (a, d) {
            a = Array.prototype.slice.call(a, 0);
            return d ? (d.push.apply(d, a), d) : a
        };
        try {
            Array.prototype.slice.call(v.documentElement.childNodes, 0)
        } catch (x) {
            u = function (a, d) {
                var c = 0, f = d || [];
                if (l.call(a) === "[object Array]") Array.prototype.push.apply(f, a); else if (typeof a.length == "number")for (var g = a.length; c < g; c++)f.push(a[c]); else for (; a[c]; c++)f.push(a[c]);
                return f
            }
        }
        var t, G;
        v.documentElement.compareDocumentPosition ? t = function (a, d) {
            return a === d ? (h = !0, 0) : !a.compareDocumentPosition || !d.compareDocumentPosition ?
                a.compareDocumentPosition ? -1 : 1 : a.compareDocumentPosition(d) & 4 ? -1 : 1
        } : (t = function (a, d) {
            if (a === d)return h = !0, 0;
            if (a.sourceIndex && d.sourceIndex)return a.sourceIndex - d.sourceIndex;
            var c, f, g = [], b = [];
            c = a.parentNode;
            f = d.parentNode;
            var n = c;
            if (c === f)return G(a, d);
            if (!c)return -1;
            if (!f)return 1;
            for (; n;)g.unshift(n), n = n.parentNode;
            for (n = f; n;)b.unshift(n), n = n.parentNode;
            c = g.length;
            f = b.length;
            for (n = 0; n < c && n < f; n++)if (g[n] !== b[n])return G(g[n], b[n]);
            return n === c ? G(a, b[n], -1) : G(g[n], d, 1)
        }, G = function (a, d, c) {
            if (a ===
                d)return c;
            for (a = a.nextSibling; a;) {
                if (a === d)return -1;
                a = a.nextSibling
            }
            return 1
        });
        (function () {
            var a = v.createElement("div"), d = "script" + (new Date).getTime(),
                c = v.documentElement;
            a.innerHTML = "<a name='" + d + "'/>";
            c.insertBefore(a, c.firstChild);
            v.getElementById(d) && (w.find.ID = function (a, d, c) {
                if (typeof d.getElementById != "undefined" && !c)return (d = d.getElementById(a[1])) ? d.id === a[1] || typeof d.getAttributeNode != "undefined" && d.getAttributeNode("id").nodeValue === a[1] ? [d] : e : []
            }, w.filter.ID = function (a, d) {
                var c = typeof a.getAttributeNode !=
                    "undefined" && a.getAttributeNode("id");
                return a.nodeType === 1 && c && c.nodeValue === d
            });
            c.removeChild(a);
            c = a = null
        })();
        (function () {
            var a = v.createElement("div");
            a.appendChild(v.createComment(""));
            a.getElementsByTagName("*").length > 0 && (w.find.TAG = function (a, d) {
                var c = d.getElementsByTagName(a[1]);
                if (a[1] === "*") {
                    for (var f = [], g = 0; c[g]; g++)c[g].nodeType === 1 && f.push(c[g]);
                    c = f
                }
                return c
            });
            a.innerHTML = "<a href='#'></a>";
            a.firstChild && typeof a.firstChild.getAttribute != "undefined" && a.firstChild.getAttribute("href") !==
            "#" && (w.attrHandle.href = function (a) {
                return a.getAttribute("href", 2)
            });
            a = null
        })();
        v.querySelectorAll && function () {
            var a = p, d = v.createElement("div");
            d.innerHTML = "<p class='TEST'></p>";
            if (!d.querySelectorAll || d.querySelectorAll(".TEST").length !== 0) {
                p = function (d, c, f, g) {
                    c = c || v;
                    if (!g && !p.isXML(c)) {
                        var b = /^(\w+$)|^\.([\w\-]+$)|^#([\w\-]+$)/.exec(d);
                        if (b && (c.nodeType === 1 || c.nodeType === 9)) {
                            if (b[1])return u(c.getElementsByTagName(d), f);
                            if (b[2] && w.find.CLASS && c.getElementsByClassName)return u(c.getElementsByClassName(b[2]),
                                f)
                        }
                        if (c.nodeType === 9) {
                            if (d === "body" && c.body)return u([c.body], f);
                            if (b && b[3]) {
                                var n = c.getElementById(b[3]);
                                if (!n || !n.parentNode)return u([], f);
                                if (n.id === b[3])return u([n], f)
                            }
                            try {
                                return u(c.querySelectorAll(d), f)
                            } catch (e) {
                            }
                        } else if (c.nodeType === 1 && c.nodeName.toLowerCase() !== "object") {
                            var b = c,
                                l = (n = c.getAttribute("id")) || "__sizzle__",
                                h = c.parentNode,
                                j = /^\s*[+~]/.test(d);
                            n ? l = l.replace(/'/g, "\\$&") : c.setAttribute("id", l);
                            j && h && (c = c.parentNode);
                            try {
                                if (!j || h)return u(c.querySelectorAll("[id='" + l + "'] " + d),
                                    f)
                            } catch (k) {
                            } finally {
                                n || b.removeAttribute("id")
                            }
                        }
                    }
                    return a(d, c, f, g)
                };
                for (var c in a)p[c] = a[c];
                d = null
            }
        }();
        (function () {
            var a = v.documentElement,
                d = a.matchesSelector || a.mozMatchesSelector || a.webkitMatchesSelector || a.msMatchesSelector;
            if (d) {
                var c = !d.call(v.createElement("div"), "div"), f = !1;
                try {
                    d.call(v.documentElement, "[test!='']:sizzle")
                } catch (g) {
                    f = !0
                }
                p.matchesSelector = function (a, g) {
                    g = g.replace(/\=\s*([^'"\]]*)\s*\]/g, "='$1']");
                    if (!p.isXML(a))try {
                        if (f || !w.match.PSEUDO.test(g) && !/!=/.test(g)) {
                            var b = d.call(a,
                                g);
                            if (b || !c || a.document && a.document.nodeType !== 11)return b
                        }
                    } catch (n) {
                    }
                    return p(g, null, null, [a]).length > 0
                }
            }
        })();
        (function () {
            var a = v.createElement("div");
            a.innerHTML = "<div class='test e'></div><div class='test'></div>";
            if (a.getElementsByClassName && a.getElementsByClassName("e").length !== 0 && (a.lastChild.className = "e", a.getElementsByClassName("e").length !== 1)) w.order.splice(1, 0, "CLASS"), w.find.CLASS = function (a, d, c) {
                if (typeof d.getElementsByClassName != "undefined" && !c)return d.getElementsByClassName(a[1])
            },
                a = null
        })();
        v.documentElement.contains ? p.contains = function (a, d) {
            return a !== d && (a.contains ? a.contains(d) : !0)
        } : v.documentElement.compareDocumentPosition ? p.contains = function (a, d) {
            return !!(a.compareDocumentPosition(d) & 16)
        } : p.contains = function () {
            return !1
        };
        p.isXML = function (a) {
            return (a = (a ? a.ownerDocument || a : 0).documentElement) ? a.nodeName !== "HTML" : !1
        };
        var B = function (a, d, c) {
            for (var f, g = [], b = "", d = d.nodeType ? [d] : d; f = w.match.PSEUDO.exec(a);)b += f[0], a = a.replace(w.match.PSEUDO, "");
            a = w.relative[a] ? a + "*" : a;
            f = 0;
            for (var n =
                d.length; f < n; f++)p(a, d[f], g, c);
            return p.filter(b, g)
        };
        p.attr = c.attr;
        p.selectors.attrMap = {};
        c.find = p;
        c.expr = p.selectors;
        c.expr[":"] = c.expr.filters;
        c.unique = p.uniqueSort;
        c.text = p.getText;
        c.isXMLDoc = p.isXML;
        c.contains = p.contains
    })();
    var ib = /Until$/, jb = /^(?:parents|prevUntil|prevAll)/, kb = /,/,
        Za = /^.[^:#\[\.,]*$/,
        lb = Array.prototype.slice, Fa = c.expr.match.POS,
        mb = {children: !0, contents: !0, next: !0, prev: !0};
    c.fn.extend({
        find: function (a) {
            var d = this, g, f;
            if (typeof a != "string")return c(a).filter(function () {
                for (g =
                         0, f = d.length; g < f; g++)if (c.contains(d[g], this))return !0
            });
            var b = this.pushStack("", "find", a), e, l, h;
            for (g = 0, f = this.length; g < f; g++)if (e = b.length, c.find(a, this[g], b), g > 0)for (l = e; l < b.length; l++)for (h = 0; h < e; h++)if (b[h] === b[l]) {
                b.splice(l--, 1);
                break
            }
            return b
        }, has: function (a) {
            var d = c(a);
            return this.filter(function () {
                for (var a = 0, f = d.length; a < f; a++)if (c.contains(this, d[a]))return !0
            })
        }, not: function (a) {
            return this.pushStack(K(this, a, !1), "not", a)
        }, filter: function (a) {
            return this.pushStack(K(this, a, !0), "filter",
                a)
        }, is: function (a) {
            return !!a && (typeof a == "string" ? Fa.test(a) ? c(a, this.context).index(this[0]) >= 0 : c.filter(a, this).length > 0 : this.filter(a).length > 0)
        }, closest: function (a, d) {
            var g = [], f, b, e = this[0];
            if (c.isArray(a)) {
                for (b = 1; e && e.ownerDocument && e !== d;) {
                    for (f = 0; f < a.length; f++)c(e).is(a[f]) && g.push({
                        selector: a[f],
                        elem: e,
                        level: b
                    });
                    e = e.parentNode;
                    b++
                }
                return g
            }
            var l = Fa.test(a) || typeof a != "string" ? c(a, d || this.context) : 0;
            for (f = 0, b = this.length; f < b; f++)for (e = this[f]; e;) {
                if (l ? l.index(e) > -1 : c.find.matchesSelector(e,
                        a)) {
                    g.push(e);
                    break
                }
                e = e.parentNode;
                if (!e || !e.ownerDocument || e === d || e.nodeType === 11)break
            }
            g = g.length > 1 ? c.unique(g) : g;
            return this.pushStack(g, "closest", a)
        }, index: function (a) {
            return !a ? this[0] && this[0].parentNode ? this.prevAll().length : -1 : typeof a == "string" ? c.inArray(this[0], c(a)) : c.inArray(a.jquery ? a[0] : a, this)
        }, add: function (a, d) {
            var g = typeof a == "string" ? c(a, d) : c.makeArray(a && a.nodeType ? [a] : a),
                f = c.merge(this.get(), g);
            return this.pushStack(!g[0] || !g[0].parentNode || g[0].parentNode.nodeType === 11 || !f[0] ||
            !f[0].parentNode || f[0].parentNode.nodeType === 11 ? f : c.unique(f))
        }, andSelf: function () {
            return this.add(this.prevObject)
        }
    });
    c.each({
        parent: function (a) {
            return (a = a.parentNode) && a.nodeType !== 11 ? a : null
        }, parents: function (a) {
            return c.dir(a, "parentNode")
        }, parentsUntil: function (a, d, g) {
            return c.dir(a, "parentNode", g)
        }, next: function (a) {
            return c.nth(a, 2, "nextSibling")
        }, prev: function (a) {
            return c.nth(a, 2, "previousSibling")
        }, nextAll: function (a) {
            return c.dir(a, "nextSibling")
        }, prevAll: function (a) {
            return c.dir(a, "previousSibling")
        },
        nextUntil: function (a, d, g) {
            return c.dir(a, "nextSibling", g)
        }, prevUntil: function (a, d, g) {
            return c.dir(a, "previousSibling", g)
        }, siblings: function (a) {
            return c.sibling(a.parentNode.firstChild, a)
        }, children: function (a) {
            return c.sibling(a.firstChild)
        }, contents: function (a) {
            return c.nodeName(a, "iframe") ? a.contentDocument || a.contentWindow.document : c.makeArray(a.childNodes)
        }
    }, function (a, d) {
        c.fn[a] = function (g, f) {
            var b = c.map(this, d, g);
            ib.test(a) || (f = g);
            f && typeof f == "string" && (b = c.filter(f, b));
            b = this.length > 1 && !mb[a] ?
                c.unique(b) : b;
            (this.length > 1 || kb.test(f)) && jb.test(a) && (b = b.reverse());
            return this.pushStack(b, a, lb.call(arguments).join(","))
        }
    });
    c.extend({
        filter: function (a, d, g) {
            g && (a = ":not(" + a + ")");
            return d.length === 1 ? c.find.matchesSelector(d[0], a) ? [d[0]] : [] : c.find.matches(a, d)
        }, dir: function (a, d, g) {
            for (var f = [], a = a[d]; a && a.nodeType !== 9 && (g === e || a.nodeType !== 1 || !c(a).is(g));)a.nodeType === 1 && f.push(a), a = a[d];
            return f
        }, nth: function (a, d, c) {
            for (var d = d || 1, f = 0; a; a = a[c])if (a.nodeType === 1 && ++f === d)break;
            return a
        }, sibling: function (a,
                              d) {
            for (var c = []; a; a = a.nextSibling)a.nodeType === 1 && a !== d && c.push(a);
            return c
        }
    });
    var wa = "abbr|article|aside|audio|canvas|datalist|details|figcaption|figure|footer|header|hgroup|mark|meter|nav|output|progress|section|summary|time|video",
        nb = / jQuery\d+="(?:\d+|null)"/g, pa = /^\s+/,
        Ga = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/ig,
        Ha = /<([\w:]+)/,
        ob = /<tbody/i, pb = /<|&#?\w+;/, qb = /<(?:script|style)/i,
        rb = /<(?:script|object|embed|option|style)/i,
        Ia = RegExp("<(?:" + wa + ")", "i"),
        Ja = /checked\s*(?:[^=]|=\s*.checked.)/i,
        sb = /\/(java|ecma)script/i, Ya = /^\s*<!(?:\[CDATA\[|\-\-)/, L = {
            option: [1, "<select multiple='multiple'>", "</select>"],
            legend: [1, "<fieldset>", "</fieldset>"],
            thead: [1, "<table>", "</table>"],
            tr: [2, "<table><tbody>", "</tbody></table>"],
            td: [3, "<table><tbody><tr>", "</tr></tbody></table>"],
            col: [2, "<table><tbody></tbody><colgroup>", "</colgroup></table>"],
            area: [1, "<map>", "</map>"],
            _default: [0, "", ""]
        }, Ka = x(v);
    L.optgroup = L.option;
    L.tbody = L.tfoot = L.colgroup = L.caption = L.thead;
    L.th = L.td;
    c.support.htmlSerialize || (L._default =
        [1, "div<div>", "</div>"]);
    c.fn.extend({
        text: function (a) {
            return c.isFunction(a) ? this.each(function (d) {
                var g = c(this);
                g.text(a.call(this, d, g.text()))
            }) : typeof a != "object" && a !== e ? this.empty().append((this[0] && this[0].ownerDocument || v).createTextNode(a)) : c.text(this)
        }, wrapAll: function (a) {
            if (c.isFunction(a))return this.each(function (d) {
                c(this).wrapAll(a.call(this, d))
            });
            if (this[0]) {
                var d = c(a, this[0].ownerDocument).eq(0).clone(!0);
                this[0].parentNode && d.insertBefore(this[0]);
                d.map(function () {
                    for (var a = this; a.firstChild &&
                    a.firstChild.nodeType === 1;)a = a.firstChild;
                    return a
                }).append(this)
            }
            return this
        }, wrapInner: function (a) {
            return c.isFunction(a) ? this.each(function (d) {
                c(this).wrapInner(a.call(this, d))
            }) : this.each(function () {
                var d = c(this), g = d.contents();
                g.length ? g.wrapAll(a) : d.append(a)
            })
        }, wrap: function (a) {
            var d = c.isFunction(a);
            return this.each(function (g) {
                c(this).wrapAll(d ? a.call(this, g) : a)
            })
        }, unwrap: function () {
            return this.parent().each(function () {
                c.nodeName(this, "body") || c(this).replaceWith(this.childNodes)
            }).end()
        },
        append: function () {
            return this.domManip(arguments, !0, function (a) {
                this.nodeType === 1 && this.appendChild(a)
            })
        }, prepend: function () {
            return this.domManip(arguments, !0, function (a) {
                this.nodeType === 1 && this.insertBefore(a, this.firstChild)
            })
        }, before: function () {
            if (this[0] && this[0].parentNode)return this.domManip(arguments, !1, function (a) {
                this.parentNode.insertBefore(a, this)
            });
            if (arguments.length) {
                var a = c.clean(arguments);
                a.push.apply(a, this.toArray());
                return this.pushStack(a, "before", arguments)
            }
        }, after: function () {
            if (this[0] &&
                this[0].parentNode)return this.domManip(arguments, !1, function (a) {
                this.parentNode.insertBefore(a, this.nextSibling)
            });
            if (arguments.length) {
                var a = this.pushStack(this, "after", arguments);
                a.push.apply(a, c.clean(arguments));
                return a
            }
        }, remove: function (a, d) {
            for (var g = 0, f; (f = this[g]) != null; g++)if (!a || c.filter(a, [f]).length) !d && f.nodeType === 1 && (c.cleanData(f.getElementsByTagName("*")), c.cleanData([f])), f.parentNode && f.parentNode.removeChild(f);
            return this
        }, empty: function () {
            for (var a = 0, d; (d = this[a]) != null; a++)for (d.nodeType ===
                                                               1 && c.cleanData(d.getElementsByTagName("*")); d.firstChild;)d.removeChild(d.firstChild);
            return this
        }, clone: function (a, d) {
            a = a == null ? !1 : a;
            d = d == null ? a : d;
            return this.map(function () {
                return c.clone(this, a, d)
            })
        }, html: function (a) {
            if (a === e)return this[0] && this[0].nodeType === 1 ? this[0].innerHTML.replace(nb, "") : null;
            if (typeof a == "string" && !qb.test(a) && (c.support.leadingWhitespace || !pa.test(a)) && !L[(Ha.exec(a) || ["", ""])[1].toLowerCase()]) {
                a = a.replace(Ga, "<$1></$2>");
                try {
                    for (var d = 0, g = this.length; d < g; d++)this[d].nodeType ===
                    1 && (c.cleanData(this[d].getElementsByTagName("*")), this[d].innerHTML = a)
                } catch (f) {
                    this.empty().append(a)
                }
            } else c.isFunction(a) ? this.each(function (d) {
                var f = c(this);
                f.html(a.call(this, d, f.html()))
            }) : this.empty().append(a);
            return this
        }, replaceWith: function (a) {
            if (this[0] && this[0].parentNode) {
                if (c.isFunction(a))return this.each(function (d) {
                    var g = c(this), f = g.html();
                    g.replaceWith(a.call(this, d, f))
                });
                typeof a != "string" && (a = c(a).detach());
                return this.each(function () {
                    var d = this.nextSibling, g = this.parentNode;
                    c(this).remove();
                    d ? c(d).before(a) : c(g).append(a)
                })
            }
            return this.length ? this.pushStack(c(c.isFunction(a) ? a() : a), "replaceWith", a) : this
        }, detach: function (a) {
            return this.remove(a, !0)
        }, domManip: function (a, d, g) {
            var f, b, l, h, j = a[0], k = [];
            if (!c.support.checkClone && arguments.length === 3 && typeof j == "string" && Ja.test(j))return this.each(function () {
                c(this).domManip(a, d, g, !0)
            });
            if (c.isFunction(j))return this.each(function (f) {
                var b = c(this);
                a[0] = j.call(this, f, d ? b.html() : e);
                b.domManip(a, d, g)
            });
            if (this[0]) {
                h = j && j.parentNode;
                c.support.parentNode && h && h.nodeType === 11 && h.childNodes.length === this.length ? f = {fragment: h} : f = c.buildFragment(a, this, k);
                l = f.fragment;
                l.childNodes.length === 1 ? b = l = l.firstChild : b = l.firstChild;
                if (b) {
                    d = d && c.nodeName(b, "tr");
                    b = 0;
                    h = this.length;
                    for (var m = h - 1; b < h; b++)g.call(d ? c.nodeName(this[b], "table") ? this[b].getElementsByTagName("tbody")[0] || this[b].appendChild(this[b].ownerDocument.createElement("tbody")) : this[b] : this[b], f.cacheable || h > 1 && b < m ? c.clone(l, !0, !0) : l)
                }
                k.length && c.each(k, t)
            }
            return this
        }
    });
    c.buildFragment =
        function (a, d, g) {
            var f, b, e, l, h = a[0];
            d && d[0] && (l = d[0].ownerDocument || d[0]);
            l.createDocumentFragment || (l = v);
            a.length === 1 && typeof h == "string" && h.length < 512 && l === v && h.charAt(0) === "<" && !rb.test(h) && (c.support.checkClone || !Ja.test(h)) && (c.support.html5Clone || !Ia.test(h)) && (b = !0, e = c.fragments[h], e && e !== 1 && (f = e));
            f || (f = l.createDocumentFragment(), c.clean(a, l, f, g));
            b && (c.fragments[h] = e ? f : 1);
            return {fragment: f, cacheable: b}
        };
    c.fragments = {};
    c.each({
        appendTo: "append",
        prependTo: "prepend",
        insertBefore: "before",
        insertAfter: "after",
        replaceAll: "replaceWith"
    }, function (a, d) {
        c.fn[a] = function (g) {
            var f = [], g = c(g), b = this.length === 1 && this[0].parentNode;
            if (b && b.nodeType === 11 && b.childNodes.length === 1 && g.length === 1)return g[d](this[0]), this;
            for (var b = 0, e = g.length; b < e; b++) {
                var l = (b > 0 ? this.clone(!0) : this).get();
                c(g[b])[d](l);
                f = f.concat(l)
            }
            return this.pushStack(f, a, g.selector)
        }
    });
    c.extend({
        clone: function (a, d, g) {
            var f, b, e;
            c.support.html5Clone || !Ia.test("<" + a.nodeName) ? f = a.cloneNode(!0) : (f = v.createElement("div"), Ka.appendChild(f), f.innerHTML =
                a.outerHTML, f = f.firstChild);
            var l = f;
            if ((!c.support.noCloneEvent || !c.support.noCloneChecked) && (a.nodeType === 1 || a.nodeType === 11) && !c.isXMLDoc(a)) {
                O(a, l);
                f = h(a);
                b = h(l);
                for (e = 0; f[e]; ++e)b[e] && O(f[e], b[e])
            }
            if (d && (G(a, l), g)) {
                f = h(a);
                b = h(l);
                for (e = 0; f[e]; ++e)G(f[e], b[e])
            }
            return l
        }, clean: function (a, d, g, f) {
            d = d || v;
            typeof d.createElement == "undefined" && (d = d.ownerDocument || d[0] && d[0].ownerDocument || v);
            for (var b = [], e, l = 0, h; (h = a[l]) != null; l++)if (typeof h == "number" && (h += ""), h) {
                if (typeof h == "string")if (pb.test(h)) {
                    h =
                        h.replace(Ga, "<$1></$2>");
                    e = (Ha.exec(h) || ["", ""])[1].toLowerCase();
                    var j = L[e] || L._default, k = j[0],
                        m = d.createElement("div");
                    for (d === v ? Ka.appendChild(m) : x(d).appendChild(m), m.innerHTML = j[1] + h + j[2]; k--;)m = m.lastChild;
                    if (!c.support.tbody) {
                        k = ob.test(h);
                        j = e === "table" && !k ? m.firstChild && m.firstChild.childNodes : j[1] === "<table>" && !k ? m.childNodes : [];
                        for (e = j.length - 1; e >= 0; --e)c.nodeName(j[e], "tbody") && !j[e].childNodes.length && j[e].parentNode.removeChild(j[e])
                    }
                    !c.support.leadingWhitespace && pa.test(h) && m.insertBefore(d.createTextNode(pa.exec(h)[0]),
                        m.firstChild);
                    h = m.childNodes
                } else h = d.createTextNode(h);
                var p;
                if (!c.support.appendChecked)if (h[0] && typeof(p = h.length) == "number")for (e = 0; e < p; e++)s(h[e]); else s(h);
                h.nodeType ? b.push(h) : b = c.merge(b, h)
            }
            if (g) {
                a = function (a) {
                    return !a.type || sb.test(a.type)
                };
                for (l = 0; b[l]; l++)f && c.nodeName(b[l], "script") && (!b[l].type || b[l].type.toLowerCase() === "text/javascript") ? f.push(b[l].parentNode ? b[l].parentNode.removeChild(b[l]) : b[l]) : (b[l].nodeType === 1 && (d = c.grep(b[l].getElementsByTagName("script"), a), b.splice.apply(b,
                    [l + 1, 0].concat(d))), g.appendChild(b[l]))
            }
            return b
        }, cleanData: function (a) {
            for (var d, g, f = c.cache, b = c.event.special, e = c.support.deleteExpando, l = 0, h; (h = a[l]) != null; l++)if (!h.nodeName || !c.noData[h.nodeName.toLowerCase()])if (g = h[c.expando]) {
                if ((d = f[g]) && d.events) {
                    for (var j in d.events)b[j] ? c.event.remove(h, j) : c.removeEvent(h, j, d.handle);
                    d.handle && (d.handle.elem = null)
                }
                e ? delete h[c.expando] : h.removeAttribute && h.removeAttribute(c.expando);
                delete f[g]
            }
        }
    });
    var qa = /alpha\([^)]*\)/i, tb = /opacity=([^)]*)/, ub = /([A-Z]|^ms)/g,
        La = /^-?\d+(?:px)?$/i, vb = /^-?\d/, wb = /^([\-+])=([\-+.\de]+)/,
        xb = {position: "absolute", visibility: "hidden", display: "block"},
        Wa = ["Left", "Right"],
        Xa = ["Top", "Bottom"], Z, Ma, Na;
    c.fn.css = function (a, d) {
        return arguments.length === 2 && d === e ? this : c.access(this, a, d, !0, function (a, d, b) {
            return b !== e ? c.style(a, d, b) : c.css(a, d)
        })
    };
    c.extend({
        cssHooks: {
            opacity: {
                get: function (a, d) {
                    if (d) {
                        var c = Z(a, "opacity", "opacity");
                        return c === "" ? "1" : c
                    }
                    return a.style.opacity
                }
            }
        },
        cssNumber: {
            fillOpacity: !0, fontWeight: !0, lineHeight: !0, opacity: !0,
            orphans: !0, widows: !0, zIndex: !0, zoom: !0
        },
        cssProps: {"float": c.support.cssFloat ? "cssFloat" : "styleFloat"},
        style: function (a, d, b, f) {
            if (a && a.nodeType !== 3 && a.nodeType !== 8 && a.style) {
                var l, h, j = c.camelCase(d), k = a.style, m = c.cssHooks[j],
                    d = c.cssProps[j] || j;
                if (b === e)return m && "get" in m && (l = m.get(a, !1, f)) !== e ? l : k[d];
                h = typeof b;
                h === "string" && (l = wb.exec(b)) && (b = +(l[1] + 1) * +l[2] + parseFloat(c.css(a, d)), h = "number");
                if (!(b == null || h === "number" && isNaN(b)))if (h === "number" && !c.cssNumber[j] && (b += "px"), !m || !("set" in m) || (b = m.set(a,
                        b)) !== e)try {
                    k[d] = b
                } catch (p) {
                }
            }
        },
        css: function (a, d, b) {
            var f, l;
            d = c.camelCase(d);
            l = c.cssHooks[d];
            d = c.cssProps[d] || d;
            d === "cssFloat" && (d = "float");
            if (l && "get" in l && (f = l.get(a, !0, b)) !== e)return f;
            if (Z)return Z(a, d)
        },
        swap: function (a, d, c) {
            var f = {}, b;
            for (b in d)f[b] = a.style[b], a.style[b] = d[b];
            c.call(a);
            for (b in d)a.style[b] = f[b]
        }
    });
    c.curCSS = c.css;
    c.each(["height", "width"], function (a, d) {
        c.cssHooks[d] = {
            get: function (a, f, b) {
                var e;
                if (f) {
                    if (a.offsetWidth !== 0)return D(a, d, b);
                    c.swap(a, xb, function () {
                        e = D(a, d, b)
                    });
                    return e
                }
            },
            set: function (a, d) {
                if (!La.test(d))return d;
                d = parseFloat(d);
                if (d >= 0)return d + "px"
            }
        }
    });
    c.support.opacity || (c.cssHooks.opacity = {
        get: function (a, d) {
            return tb.test((d && a.currentStyle ? a.currentStyle.filter : a.style.filter) || "") ? parseFloat(RegExp.$1) / 100 + "" : d ? "1" : ""
        }, set: function (a, d) {
            var b = a.style, f = a.currentStyle,
                e = c.isNumeric(d) ? "alpha(opacity=" + d * 100 + ")" : "",
                l = f && f.filter || b.filter || "";
            b.zoom = 1;
            if (d >= 1 && c.trim(l.replace(qa, "")) === "" && (b.removeAttribute("filter"), f && !f.filter))return;
            b.filter = qa.test(l) ?
                l.replace(qa, e) : l + " " + e
        }
    });
    c(function () {
        c.support.reliableMarginRight || (c.cssHooks.marginRight = {
            get: function (a, d) {
                var b;
                c.swap(a, {display: "inline-block"}, function () {
                    d ? b = Z(a, "margin-right", "marginRight") : b = a.style.marginRight
                });
                return b
            }
        })
    });
    v.defaultView && v.defaultView.getComputedStyle && (Ma = function (a, d) {
        var b, f, e;
        d = d.replace(ub, "-$1").toLowerCase();
        (f = a.ownerDocument.defaultView) && (e = f.getComputedStyle(a, null)) && (b = e.getPropertyValue(d), b === "" && !c.contains(a.ownerDocument.documentElement, a) && (b =
            c.style(a, d)));
        return b
    });
    v.documentElement.currentStyle && (Na = function (a, d) {
        var c, b, e, l = a.currentStyle && a.currentStyle[d], h = a.style;
        l === null && h && (e = h[d]) && (l = e);
        !La.test(l) && vb.test(l) && (c = h.left, b = a.runtimeStyle && a.runtimeStyle.left, b && (a.runtimeStyle.left = a.currentStyle.left), h.left = d === "fontSize" ? "1em" : l || 0, l = h.pixelLeft + "px", h.left = c, b && (a.runtimeStyle.left = b));
        return l === "" ? "auto" : l
    });
    Z = Ma || Na;
    c.expr && c.expr.filters && (c.expr.filters.hidden = function (a) {
        var d = a.offsetHeight;
        return a.offsetWidth ===
            0 && d === 0 || !c.support.reliableHiddenOffsets && (a.style && a.style.display || c.css(a, "display")) === "none"
    }, c.expr.filters.visible = function (a) {
        return !c.expr.filters.hidden(a)
    });
    var yb = /%20/g, Va = /\[\]$/, Oa = /\r?\n/g, zb = /#.*$/,
        Ab = /^(.*?):[ \t]*([^\r\n]*)\r?$/mg,
        Bb = /^(?:color|date|datetime|datetime-local|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,
        Cb = /^(?:GET|HEAD)$/, Db = /^\/\//, Pa = /\?/,
        Eb = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi,
        Fb = /^(?:select|textarea)/i, va = /\s+/,
        Gb = /([?&])_=[^&]*/,
        Qa = /^([\w\+\.\-]+:)(?:\/\/([^\/?#:]*)(?::(\d+))?)?/, Ra = c.fn.load,
        la = {}, Sa = {},
        W, X, Ta = ["*/"] + ["*"];
    try {
        W = ca.href
    } catch (Mb) {
        W = v.createElement("a"), W.href = "", W = W.href
    }
    X = Qa.exec(W.toLowerCase()) || [];
    c.fn.extend({
        load: function (a, d, b) {
            if (typeof a != "string" && Ra)return Ra.apply(this, arguments);
            if (!this.length)return this;
            var f = a.indexOf(" ");
            if (f >= 0)var l = a.slice(f, a.length), a = a.slice(0, f);
            f = "GET";
            d && (c.isFunction(d) ? (b = d, d = e) : typeof d == "object" && (d = c.param(d, c.ajaxSettings.traditional),
                    f = "POST"));
            var h = this;
            c.ajax({
                url: a,
                type: f,
                dataType: "html",
                data: d,
                complete: function (a, d, f) {
                    f = a.responseText;
                    a.isResolved() && (a.done(function (a) {
                        f = a
                    }), h.html(l ? c("<div>").append(f.replace(Eb, "")).find(l) : f));
                    b && h.each(b, [f, d, a])
                }
            });
            return this
        }, serialize: function () {
            return c.param(this.serializeArray())
        }, serializeArray: function () {
            return this.map(function () {
                return this.elements ? c.makeArray(this.elements) : this
            }).filter(function () {
                return this.name && !this.disabled && (this.checked || Fb.test(this.nodeName) ||
                    Bb.test(this.type))
            }).map(function (a, d) {
                var b = c(this).val();
                return b == null ? null : c.isArray(b) ? c.map(b, function (a) {
                    return {name: d.name, value: a.replace(Oa, "\r\n")}
                }) : {name: d.name, value: b.replace(Oa, "\r\n")}
            }).get()
        }
    });
    c.each("ajaxStart ajaxStop ajaxComplete ajaxError ajaxSuccess ajaxSend".split(" "), function (a, d) {
        c.fn[d] = function (a) {
            return this.on(d, a)
        }
    });
    c.each(["get", "post"], function (a, d) {
        c[d] = function (a, b, l, h) {
            c.isFunction(b) && (h = h || l, l = b, b = e);
            return c.ajax({type: d, url: a, data: b, success: l, dataType: h})
        }
    });
    c.extend({
        getScript: function (a, d) {
            return c.get(a, e, d, "script")
        }, getJSON: function (a, d, b) {
            return c.get(a, d, b, "json")
        }, ajaxSetup: function (a, d) {
            d ? z(a, c.ajaxSettings) : (d = a, a = c.ajaxSettings);
            z(a, d);
            return a
        }, ajaxSettings: {
            url: W,
            isLocal: /^(?:about|app|app\-storage|.+\-extension|file|res|widget):$/.test(X[1]),
            global: !0,
            type: "GET",
            contentType: "application/x-www-form-urlencoded",
            processData: !0,
            async: !0,
            accepts: {
                xml: "application/xml, text/xml",
                html: "text/html",
                text: "text/plain",
                json: "application/json, text/javascript",
                "*": Ta
            },
            contents: {xml: /xml/, html: /html/, json: /json/},
            responseFields: {xml: "responseXML", text: "responseText"},
            converters: {
                "* text": b.String,
                "text html": !0,
                "text json": c.parseJSON,
                "text xml": c.parseXML
            },
            flatOptions: {context: !0, url: !0}
        }, ajaxPrefilter: C(la), ajaxTransport: C(Sa), ajax: function (a, d) {
            function b(a, d, g, o) {
                if (x !== 2) {
                    x = 2;
                    r && clearTimeout(r);
                    q = e;
                    A = o || "";
                    B.readyState = a > 0 ? 4 : 0;
                    var w, s, u, o = d;
                    if (g) {
                        var G = f, z = B, O = G.contents, v = G.dataTypes,
                            y = G.responseFields, M, H, C, D;
                        for (H in y)H in g && (z[y[H]] = g[H]);
                        for (; v[0] ===
                               "*";)v.shift(), M === e && (M = G.mimeType || z.getResponseHeader("content-type"));
                        if (M)for (H in O)if (O[H] && O[H].test(M)) {
                            v.unshift(H);
                            break
                        }
                        if (v[0] in g) C = v[0]; else {
                            for (H in g) {
                                if (!v[0] || G.converters[H + " " + v[0]]) {
                                    C = H;
                                    break
                                }
                                D || (D = H)
                            }
                            C = C || D
                        }
                        C ? (C !== v[0] && v.unshift(C), g = g[C]) : g = void 0
                    } else g = e;
                    if (a >= 200 && a < 300 || a === 304) {
                        if (f.ifModified) {
                            if (M = B.getResponseHeader("Last-Modified")) c.lastModified[p] = M;
                            if (M = B.getResponseHeader("Etag")) c.etag[p] = M
                        }
                        if (a === 304) o = "notmodified", w = !0; else try {
                            M = f;
                            M.dataFilter && (g = M.dataFilter(g,
                                M.dataType));
                            var da = M.dataTypes;
                            H = {};
                            var K, ga, F = da.length, I, E = da[0], P, J, U, V,
                                L;
                            for (K = 1; K < F; K++) {
                                if (K === 1)for (ga in M.converters)typeof ga == "string" && (H[ga.toLowerCase()] = M.converters[ga]);
                                P = E;
                                E = da[K];
                                if (E === "*") E = P; else if (P !== "*" && P !== E) {
                                    J = P + " " + E;
                                    U = H[J] || H["* " + E];
                                    if (!U)for (V in L = e, H)if (I = V.split(" "), I[0] === P || I[0] === "*")if (L = H[I[1] + " " + E]) {
                                        V = H[V];
                                        V === !0 ? U = L : L === !0 && (U = V);
                                        break
                                    }
                                    !U && !L && c.error("No conversion from " + J.replace(" ", " to "));
                                    U !== !0 && (g = U ? U(g) : L(V(g)))
                                }
                            }
                            s = g;
                            o = "success";
                            w = !0
                        } catch (fa) {
                            o =
                                "parsererror", u = fa
                        }
                    } else if (u = o, !o || a) o = "error", a < 0 && (a = 0);
                    B.status = a;
                    B.statusText = "" + (d || o);
                    w ? j.resolveWith(l, [s, o, B]) : j.rejectWith(l, [B, o, u]);
                    B.statusCode(m);
                    m = e;
                    t && h.trigger("ajax" + (w ? "Success" : "Error"), [B, f, w ? s : u]);
                    k.fireWith(l, [B, o]);
                    t && (h.trigger("ajaxComplete", [B, f]), --c.active || c.event.trigger("ajaxStop"))
                }
            }

            typeof a == "object" && (d = a, a = e);
            d = d || {};
            var f = c.ajaxSetup({}, d), l = f.context || f,
                h = l !== f && (l.nodeType || l instanceof c) ? c(l) : c.event,
                j = c.Deferred(),
                k = c.Callbacks("once memory"), m = f.statusCode ||
                    {}, p, o = {}, w = {}, A, s, q, r, u, x = 0, t, G, B = {
                    readyState: 0, setRequestHeader: function (a, d) {
                        if (!x) {
                            var c = a.toLowerCase();
                            a = w[c] = w[c] || a;
                            o[a] = d
                        }
                        return this
                    }, getAllResponseHeaders: function () {
                        return x === 2 ? A : null
                    }, getResponseHeader: function (a) {
                        var d;
                        if (x === 2) {
                            if (!s)for (s = {}; d = Ab.exec(A);)s[d[1].toLowerCase()] = d[2];
                            d = s[a.toLowerCase()]
                        }
                        return d === e ? null : d
                    }, overrideMimeType: function (a) {
                        x || (f.mimeType = a);
                        return this
                    }, abort: function (a) {
                        a = a || "abort";
                        q && q.abort(a);
                        b(0, a);
                        return this
                    }
                };
            j.promise(B);
            B.success = B.done;
            B.error =
                B.fail;
            B.complete = k.add;
            B.statusCode = function (a) {
                if (a) {
                    var d;
                    if (x < 2)for (d in a)m[d] = [m[d], a[d]]; else d = a[B.status], B.then(d, d)
                }
                return this
            };
            f.url = ((a || f.url) + "").replace(zb, "").replace(Db, X[1] + "//");
            f.dataTypes = c.trim(f.dataType || "*").toLowerCase().split(va);
            f.crossDomain == null && (u = Qa.exec(f.url.toLowerCase()), f.crossDomain = !(!u || u[1] == X[1] && u[2] == X[2] && (u[3] || (u[1] === "http:" ? 80 : 443)) == (X[3] || (X[1] === "http:" ? 80 : 443))));
            f.data && f.processData && typeof f.data != "string" && (f.data = c.param(f.data, f.traditional));
            y(la, f, d, B);
            if (x === 2)return !1;
            t = f.global;
            f.type = f.type.toUpperCase();
            f.hasContent = !Cb.test(f.type);
            t && c.active++ === 0 && c.event.trigger("ajaxStart");
            if (!f.hasContent && (f.data && (f.url += (Pa.test(f.url) ? "&" : "?") + f.data, delete f.data), p = f.url, f.cache === !1)) {
                u = c.now();
                var z = f.url.replace(Gb, "$1_=" + u);
                f.url = z + (z === f.url ? (Pa.test(f.url) ? "&" : "?") + "_=" + u : "")
            }
            (f.data && f.hasContent && f.contentType !== !1 || d.contentType) && B.setRequestHeader("Content-Type", f.contentType);
            f.ifModified && (p = p || f.url, c.lastModified[p] &&
            B.setRequestHeader("If-Modified-Since", c.lastModified[p]), c.etag[p] && B.setRequestHeader("If-None-Match", c.etag[p]));
            B.setRequestHeader("Accept", f.dataTypes[0] && f.accepts[f.dataTypes[0]] ? f.accepts[f.dataTypes[0]] + (f.dataTypes[0] !== "*" ? ", " + Ta + "; q=0.01" : "") : f.accepts["*"]);
            for (G in f.headers)B.setRequestHeader(G, f.headers[G]);
            if (f.beforeSend && (f.beforeSend.call(l, B, f) === !1 || x === 2))return B.abort(), !1;
            for (G in{success: 1, error: 1, complete: 1})B[G](f[G]);
            if (q = y(Sa, f, d, B)) {
                B.readyState = 1;
                t && h.trigger("ajaxSend",
                    [B, f]);
                f.async && f.timeout > 0 && (r = setTimeout(function () {
                    B.abort("timeout")
                }, f.timeout));
                try {
                    x = 1, q.send(o, b)
                } catch (O) {
                    if (x < 2) b(-1, O); else throw O;
                }
            } else b(-1, "No Transport");
            return B
        }, param: function (a, d) {
            var b = [], f = function (a, d) {
                d = c.isFunction(d) ? d() : d;
                b[b.length] = encodeURIComponent(a) + "=" + encodeURIComponent(d)
            };
            d === e && (d = c.ajaxSettings.traditional);
            if (c.isArray(a) || a.jquery && !c.isPlainObject(a)) c.each(a, function () {
                f(this.name, this.value)
            }); else for (var l in a)m(l, a[l], d, f);
            return b.join("&").replace(yb,
                "+")
        }
    });
    c.extend({active: 0, lastModified: {}, etag: {}});
    var Hb = c.now(), ia = /(\=)\?(&|$)|\?\?/i;
    c.ajaxSetup({
        jsonp: "callback", jsonpCallback: function () {
            return c.expando + "_" + Hb++
        }
    });
    c.ajaxPrefilter("json jsonp", function (a, d, g) {
        d = a.contentType === "application/x-www-form-urlencoded" && typeof a.data == "string";
        if (a.dataTypes[0] === "jsonp" || a.jsonp !== !1 && (ia.test(a.url) || d && ia.test(a.data))) {
            var f,
                l = a.jsonpCallback = c.isFunction(a.jsonpCallback) ? a.jsonpCallback() : a.jsonpCallback,
                e = b[l],
                h = a.url, j = a.data, k = "$1" + l +
                    "$2";
            a.jsonp !== !1 && (h = h.replace(ia, k), a.url === h && (d && (j = j.replace(ia, k)), a.data === j && (h += (/\?/.test(h) ? "&" : "?") + a.jsonp + "=" + l)));
            a.url = h;
            a.data = j;
            b[l] = function (a) {
                f = [a]
            };
            g.always(function () {
                b[l] = e;
                f && c.isFunction(e) && b[l](f[0])
            });
            a.converters["script json"] = function () {
                f || c.error(l + " was not called");
                return f[0]
            };
            a.dataTypes[0] = "json";
            return "script"
        }
    });
    c.ajaxSetup({
        accepts: {script: "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"},
        contents: {script: /javascript|ecmascript/},
        converters: {
            "text script": function (a) {
                c.globalEval(a);
                return a
            }
        }
    });
    c.ajaxPrefilter("script", function (a) {
        a.cache === e && (a.cache = !1);
        a.crossDomain && (a.type = "GET", a.global = !1)
    });
    c.ajaxTransport("script", function (a) {
        if (a.crossDomain) {
            var d,
                c = v.head || v.getElementsByTagName("head")[0] || v.documentElement;
            return {
                send: function (b, l) {
                    d = v.createElement("script");
                    d.async = "async";
                    a.scriptCharset && (d.charset = a.scriptCharset);
                    d.src = a.url;
                    d.onload = d.onreadystatechange = function (a, b) {
                        if (b || !d.readyState || /loaded|complete/.test(d.readyState)) d.onload =
                            d.onreadystatechange = null, c && d.parentNode && c.removeChild(d), d = e, b || l(200, "success")
                    };
                    c.insertBefore(d, c.firstChild)
                }, abort: function () {
                    d && d.onload(0, 1)
                }
            }
        }
    });
    var ra = b.ActiveXObject ? function () {
        for (var a in ba)ba[a](0, 1)
    } : !1, Ib = 0, ba;
    c.ajaxSettings.xhr = b.ActiveXObject ? function () {
        var a;
        if (!(a = !this.isLocal && k()))a:{
            try {
                a = new b.ActiveXObject("Microsoft.XMLHTTP");
                break a
            } catch (d) {
            }
            a = void 0
        }
        return a
    } : k;
    (function (a) {
        c.extend(c.support, {ajax: !!a, cors: !!a && "withCredentials" in a})
    })(c.ajaxSettings.xhr());
    c.support.ajax &&
    c.ajaxTransport(function (a) {
        if (!a.crossDomain || c.support.cors) {
            var d;
            return {
                send: function (g, f) {
                    var l = a.xhr(), h, j;
                    a.username ? l.open(a.type, a.url, a.async, a.username, a.password) : l.open(a.type, a.url, a.async);
                    if (a.xhrFields)for (j in a.xhrFields)l[j] = a.xhrFields[j];
                    a.mimeType && l.overrideMimeType && l.overrideMimeType(a.mimeType);
                    !a.crossDomain && !g["X-Requested-With"] && (g["X-Requested-With"] = "XMLHttpRequest");
                    try {
                        for (j in g)l.setRequestHeader(j, g[j])
                    } catch (k) {
                    }
                    l.send(a.hasContent && a.data || null);
                    d = function (b,
                                  g) {
                        var j, k, m, p, o;
                        try {
                            if (d && (g || l.readyState === 4))if (d = e, h && (l.onreadystatechange = c.noop, ra && delete ba[h]), g) l.readyState !== 4 && l.abort(); else {
                                j = l.status;
                                m = l.getAllResponseHeaders();
                                p = {};
                                o = l.responseXML;
                                o && o.documentElement && (p.xml = o);
                                p.text = l.responseText;
                                try {
                                    k = l.statusText
                                } catch (w) {
                                    k = ""
                                }
                                !j && a.isLocal && !a.crossDomain ? j = p.text ? 200 : 404 : j === 1223 && (j = 204)
                            }
                        } catch (s) {
                            g || f(-1, s)
                        }
                        p && f(j, k, p, m)
                    };
                    !a.async || l.readyState === 4 ? d() : (h = ++Ib, ra && (ba || (ba = {}, c(b).unload(ra)), ba[h] = d), l.onreadystatechange = d)
                }, abort: function () {
                    d &&
                    d(0, 1)
                }
            }
        }
    });
    var ka = {}, Q, Y, Jb = /^(?:toggle|show|hide)$/,
        Kb = /^([+\-]=)?([\d+.\-]+)([a-z%]*)$/i, ja,
        sa = [["height", "marginTop", "marginBottom", "paddingTop", "paddingBottom"], ["width", "marginLeft", "marginRight", "paddingLeft", "paddingRight"], ["opacity"]],
        ea;
    c.fn.extend({
        show: function (a, d, b) {
            var f, l;
            if (a || a === 0)return this.animate(o("show", 3), a, d, b);
            a = 0;
            for (d = this.length; a < d; a++)f = this[a], f.style && (l = f.style.display, !c._data(f, "olddisplay") && l === "none" && (l = f.style.display = ""), l === "" && c.css(f, "display") === "none" &&
            c._data(f, "olddisplay", r(f.nodeName)));
            for (a = 0; a < d; a++)if (f = this[a], f.style && (l = f.style.display, l === "" || l === "none")) f.style.display = c._data(f, "olddisplay") || "";
            return this
        }, hide: function (a, d, b) {
            if (a || a === 0)return this.animate(o("hide", 3), a, d, b);
            for (var f, l, a = 0, d = this.length; a < d; a++)f = this[a], f.style && (l = c.css(f, "display"), l !== "none" && !c._data(f, "olddisplay") && c._data(f, "olddisplay", l));
            for (a = 0; a < d; a++)this[a].style && (this[a].style.display = "none");
            return this
        }, _toggle: c.fn.toggle, toggle: function (a,
                                                   d, b) {
            var f = typeof a == "boolean";
            c.isFunction(a) && c.isFunction(d) ? this._toggle.apply(this, arguments) : a == null || f ? this.each(function () {
                var d = f ? a : c(this).is(":hidden");
                c(this)[d ? "show" : "hide"]()
            }) : this.animate(o("toggle", 3), a, d, b);
            return this
        }, fadeTo: function (a, d, c, b) {
            return this.filter(":hidden").css("opacity", 0).show().end().animate({opacity: d}, a, c, b)
        }, animate: function (a, d, b, f) {
            function l() {
                var F;
                e.queue === !1 && c._mark(this);
                var d = c.extend({}, e), b = this.nodeType === 1,
                    f = b && c(this).is(":hidden"), g, h, n, j,
                    k, m, p, o, w;
                d.animatedProperties = {};
                for (n in a) {
                    g = c.camelCase(n);
                    n !== g && (a[g] = a[n], delete a[n]);
                    h = a[g];
                    c.isArray(h) ? (d.animatedProperties[g] = h[1], F = a[g] = h[0], h = F) : d.animatedProperties[g] = d.specialEasing && d.specialEasing[g] || d.easing || "swing";
                    if (h === "hide" && f || h === "show" && !f)return d.complete.call(this);
                    b && (g === "height" || g === "width") && (d.overflow = [this.style.overflow, this.style.overflowX, this.style.overflowY], c.css(this, "display") === "inline" && c.css(this, "float") === "none" && (!c.support.inlineBlockNeedsLayout ||
                    r(this.nodeName) === "inline" ? this.style.display = "inline-block" : this.style.zoom = 1))
                }
                d.overflow != null && (this.style.overflow = "hidden");
                for (n in a)j = new c.fx(this, d, n), h = a[n], Jb.test(h) ? (w = c._data(this, "toggle" + n) || (h === "toggle" ? f ? "show" : "hide" : 0), w ? (c._data(this, "toggle" + n, w === "show" ? "hide" : "show"), j[w]()) : j[h]()) : (k = Kb.exec(h), m = j.cur(), k ? (p = parseFloat(k[2]), o = k[3] || (c.cssNumber[n] ? "" : "px"), o !== "px" && (c.style(this, n, (p || 1) + o), m *= (p || 1) / j.cur(), c.style(this, n, m + o)), k[1] && (p = (k[1] === "-=" ? -1 : 1) * p + m), j.custom(m,
                    p, o)) : j.custom(m, h, ""));
                return !0
            }

            var e = c.speed(d, b, f);
            if (c.isEmptyObject(a))return this.each(e.complete, [!1]);
            a = c.extend({}, a);
            return e.queue === !1 ? this.each(l) : this.queue(e.queue, l)
        }, stop: function (a, d, b) {
            typeof a != "string" && (b = d, d = a, a = e);
            d && a !== !1 && this.queue(a || "fx", []);
            return this.each(function () {
                function d(a, f, l) {
                    f = f[l];
                    c.removeData(a, l, !0);
                    f.stop(b)
                }

                var l, e = !1, h = c.timers, j = c._data(this);
                b || c._unmark(!0, this);
                if (a == null)for (l in j)j[l] && j[l].stop && l.indexOf(".run") === l.length - 4 && d(this, j, l); else j[l =
                    a + ".run"] && j[l].stop && d(this, j, l);
                for (l = h.length; l--;)h[l].elem === this && (a == null || h[l].queue === a) && (b ? h[l](!0) : h[l].saveState(), e = !0, h.splice(l, 1));
                (!b || !e) && c.dequeue(this, a)
            })
        }
    });
    c.each({
        slideDown: o("show", 1),
        slideUp: o("hide", 1),
        slideToggle: o("toggle", 1),
        fadeIn: {opacity: "show"},
        fadeOut: {opacity: "hide"},
        fadeToggle: {opacity: "toggle"}
    }, function (a, d) {
        c.fn[a] = function (a, c, b) {
            return this.animate(d, a, c, b)
        }
    });
    c.extend({
        speed: function (a, d, b) {
            var f = a && typeof a == "object" ? c.extend({}, a) : {
                complete: b || !b && d ||
                c.isFunction(a) && a,
                duration: a,
                easing: b && d || d && !c.isFunction(d) && d
            };
            f.duration = c.fx.off ? 0 : typeof f.duration == "number" ? f.duration : f.duration in c.fx.speeds ? c.fx.speeds[f.duration] : c.fx.speeds._default;
            if (f.queue == null || f.queue === !0) f.queue = "fx";
            f.old = f.complete;
            f.complete = function (a) {
                c.isFunction(f.old) && f.old.call(this);
                f.queue ? c.dequeue(this, f.queue) : a !== !1 && c._unmark(this)
            };
            return f
        }, easing: {
            linear: function (a, d, c, b) {
                return c + b * a
            }, swing: function (a, d, c, b) {
                return (-Math.cos(a * Math.PI) / 2 + 0.5) * b + c
            }
        }, timers: [],
        fx: function (a, d, c) {
            this.options = d;
            this.elem = a;
            this.prop = c;
            d.orig = d.orig || {}
        }
    });
    c.fx.prototype = {
        update: function () {
            this.options.step && this.options.step.call(this.elem, this.now, this);
            (c.fx.step[this.prop] || c.fx.step._default)(this)
        }, cur: function () {
            if (this.elem[this.prop] != null && (!this.elem.style || this.elem.style[this.prop] == null))return this.elem[this.prop];
            var a, d = c.css(this.elem, this.prop);
            return isNaN(a = parseFloat(d)) ? !d || d === "auto" ? 0 : d : a
        }, custom: function (a, d, b) {
            function f(a) {
                return l.step(a)
            }

            var l =
                this, h = c.fx;
            this.startTime = ea || j();
            this.end = d;
            this.now = this.start = a;
            this.pos = this.state = 0;
            this.unit = b || this.unit || (c.cssNumber[this.prop] ? "" : "px");
            f.queue = this.options.queue;
            f.elem = this.elem;
            f.saveState = function () {
                l.options.hide && c._data(l.elem, "fxshow" + l.prop) === e && c._data(l.elem, "fxshow" + l.prop, l.start)
            };
            f() && c.timers.push(f) && !ja && (ja = setInterval(h.tick, h.interval))
        }, show: function () {
            var a = c._data(this.elem, "fxshow" + this.prop);
            this.options.orig[this.prop] = a || c.style(this.elem, this.prop);
            this.options.show =
                !0;
            a !== e ? this.custom(this.cur(), a) : this.custom(this.prop === "width" || this.prop === "height" ? 1 : 0, this.cur());
            c(this.elem).show()
        }, hide: function () {
            this.options.orig[this.prop] = c._data(this.elem, "fxshow" + this.prop) || c.style(this.elem, this.prop);
            this.options.hide = !0;
            this.custom(this.cur(), 0)
        }, step: function (a) {
            var d, b, f, l = ea || j(), e = !0, h = this.elem, k = this.options;
            if (a || l >= k.duration + this.startTime) {
                this.now = this.end;
                this.pos = this.state = 1;
                this.update();
                k.animatedProperties[this.prop] = !0;
                for (d in k.animatedProperties)k.animatedProperties[d] !==
                !0 && (e = !1);
                if (e) {
                    k.overflow != null && !c.support.shrinkWrapBlocks && c.each(["", "X", "Y"], function (a, d) {
                        h.style["overflow" + d] = k.overflow[a]
                    });
                    k.hide && c(h).hide();
                    if (k.hide || k.show)for (d in k.animatedProperties)c.style(h, d, k.orig[d]), c.removeData(h, "fxshow" + d, !0), c.removeData(h, "toggle" + d, !0);
                    f = k.complete;
                    f && (k.complete = !1, f.call(h))
                }
                return !1
            }
            k.duration == Infinity ? this.now = l : (b = l - this.startTime, this.state = b / k.duration, this.pos = c.easing[k.animatedProperties[this.prop]](this.state, b, 0, 1, k.duration), this.now =
                this.start + (this.end - this.start) * this.pos);
            this.update();
            return !0
        }
    };
    c.extend(c.fx, {
        tick: function () {
            for (var a, d = c.timers, b = 0; b < d.length; b++)a = d[b], !a() && d[b] === a && d.splice(b--, 1);
            d.length || c.fx.stop()
        }, interval: 13, stop: function () {
            clearInterval(ja);
            ja = null
        }, speeds: {slow: 600, fast: 200, _default: 400}, step: {
            opacity: function (a) {
                c.style(a.elem, "opacity", a.now)
            }, _default: function (a) {
                a.elem.style && a.elem.style[a.prop] != null ? a.elem.style[a.prop] = a.now + a.unit : a.elem[a.prop] = a.now
            }
        }
    });
    c.each(["width", "height"],
        function (a, d) {
            c.fx.step[d] = function (a) {
                c.style(a.elem, d, Math.max(0, a.now) + a.unit)
            }
        });
    c.expr && c.expr.filters && (c.expr.filters.animated = function (a) {
        return c.grep(c.timers, function (d) {
            return a === d.elem
        }).length
    });
    var Lb = /^t(?:able|d|h)$/i, Ua = /^(?:body|html)$/i;
    "getBoundingClientRect" in v.documentElement ? c.fn.offset = function (a) {
        var d = this[0], b;
        if (a)return this.each(function (d) {
            c.offset.setOffset(this, a, d)
        });
        if (!d || !d.ownerDocument)return null;
        if (d === d.ownerDocument.body)return c.offset.bodyOffset(d);
        try {
            b = d.getBoundingClientRect()
        } catch (f) {
        }
        var l = d.ownerDocument, e = l.documentElement;
        if (!b || !c.contains(e, d))return b ? {
            top: b.top,
            left: b.left
        } : {top: 0, left: 0};
        d = l.body;
        l = q(l);
        return {
            top: b.top + (l.pageYOffset || c.support.boxModel && e.scrollTop || d.scrollTop) - (e.clientTop || d.clientTop || 0),
            left: b.left + (l.pageXOffset || c.support.boxModel && e.scrollLeft || d.scrollLeft) - (e.clientLeft || d.clientLeft || 0)
        }
    } : c.fn.offset = function (a) {
        var d = this[0];
        if (a)return this.each(function (d) {
            c.offset.setOffset(this, a, d)
        });
        if (!d ||
            !d.ownerDocument)return null;
        if (d === d.ownerDocument.body)return c.offset.bodyOffset(d);
        for (var b, f = d.offsetParent, l = d.ownerDocument, e = l.documentElement, h = l.body, j = (l = l.defaultView) ? l.getComputedStyle(d, null) : d.currentStyle, k = d.offsetTop, m = d.offsetLeft; (d = d.parentNode) && d !== h && d !== e;) {
            if (c.support.fixedPosition && j.position === "fixed")break;
            b = l ? l.getComputedStyle(d, null) : d.currentStyle;
            k -= d.scrollTop;
            m -= d.scrollLeft;
            d === f && (k += d.offsetTop, m += d.offsetLeft, c.support.doesNotAddBorder && (!c.support.doesAddBorderForTableAndCells ||
            !Lb.test(d.nodeName)) && (k += parseFloat(b.borderTopWidth) || 0, m += parseFloat(b.borderLeftWidth) || 0), f = d.offsetParent);
            c.support.subtractsBorderForOverflowNotVisible && b.overflow !== "visible" && (k += parseFloat(b.borderTopWidth) || 0, m += parseFloat(b.borderLeftWidth) || 0);
            j = b
        }
        if (j.position === "relative" || j.position === "static") k += h.offsetTop, m += h.offsetLeft;
        c.support.fixedPosition && j.position === "fixed" && (k += Math.max(e.scrollTop, h.scrollTop), m += Math.max(e.scrollLeft, h.scrollLeft));
        return {top: k, left: m}
    };
    c.offset = {
        bodyOffset: function (a) {
            var d =
                a.offsetTop, b = a.offsetLeft;
            c.support.doesNotIncludeMarginInBodyOffset && (d += parseFloat(c.css(a, "marginTop")) || 0, b += parseFloat(c.css(a, "marginLeft")) || 0);
            return {top: d, left: b}
        }, setOffset: function (a, d, b) {
            var f = c.css(a, "position");
            f === "static" && (a.style.position = "relative");
            var l = c(a), e = l.offset(), h = c.css(a, "top"),
                j = c.css(a, "left"), k = {}, m = {}, p, o;
            (f === "absolute" || f === "fixed") && c.inArray("auto", [h, j]) > -1 ? (m = l.position(), p = m.top, o = m.left) : (p = parseFloat(h) || 0, o = parseFloat(j) || 0);
            c.isFunction(d) && (d = d.call(a,
                b, e));
            d.top != null && (k.top = d.top - e.top + p);
            d.left != null && (k.left = d.left - e.left + o);
            "using" in d ? d.using.call(a, k) : l.css(k)
        }
    };
    c.fn.extend({
        position: function () {
            if (!this[0])return null;
            var a = this[0], d = this.offsetParent(), b = this.offset(),
                f = Ua.test(d[0].nodeName) ? {top: 0, left: 0} : d.offset();
            b.top -= parseFloat(c.css(a, "marginTop")) || 0;
            b.left -= parseFloat(c.css(a, "marginLeft")) || 0;
            f.top += parseFloat(c.css(d[0], "borderTopWidth")) || 0;
            f.left += parseFloat(c.css(d[0], "borderLeftWidth")) || 0;
            return {
                top: b.top - f.top, left: b.left -
                f.left
            }
        }, offsetParent: function () {
            return this.map(function () {
                for (var a = this.offsetParent || v.body; a && !Ua.test(a.nodeName) && c.css(a, "position") === "static";)a = a.offsetParent;
                return a
            })
        }
    });
    c.each(["Left", "Top"], function (a, d) {
        var b = "scroll" + d;
        c.fn[b] = function (d) {
            var l, h;
            if (d === e) {
                l = this[0];
                return !l ? null : (h = q(l)) ? "pageXOffset" in h ? h[a ? "pageYOffset" : "pageXOffset"] : c.support.boxModel && h.document.documentElement[b] || h.document.body[b] : l[b]
            }
            return this.each(function () {
                h = q(this);
                h ? h.scrollTo(a ? c(h).scrollLeft() :
                    d, a ? d : c(h).scrollTop()) : this[b] = d
            })
        }
    });
    c.each(["Height", "Width"], function (a, d) {
        var b = d.toLowerCase();
        c.fn["inner" + d] = function () {
            var a = this[0];
            return a ? a.style ? parseFloat(c.css(a, b, "padding")) : this[b]() : null
        };
        c.fn["outer" + d] = function (a) {
            var d = this[0];
            return d ? d.style ? parseFloat(c.css(d, b, a ? "margin" : "border")) : this[b]() : null
        };
        c.fn[b] = function (a) {
            var l = this[0];
            if (!l)return a == null ? null : this;
            if (c.isFunction(a))return this.each(function (d) {
                var l = c(this);
                l[b](a.call(this, d, l[b]()))
            });
            if (c.isWindow(l)) {
                var h =
                        l.document.documentElement["client" + d],
                    j = l.document.body;
                return l.document.compatMode === "CSS1Compat" && h || j && j["client" + d] || h
            }
            if (l.nodeType === 9)return Math.max(l.documentElement["client" + d], l.body["scroll" + d], l.documentElement["scroll" + d], l.body["offset" + d], l.documentElement["offset" + d]);
            return a === e ? (l = c.css(l, b), h = parseFloat(l), c.isNumeric(h) ? h : l) : this.css(b, typeof a == "string" ? a : a + "px")
        }
    });
    b.jQuery = b.$ = c;
    typeof define == "function" && define.amd && define.amd.jQuery && define("jquery", [], function () {
        return c
    })
})(window);
var is = {}, isapp = {};
(function () {
    isapp.global = this;
    isapp.version = "0.0.1.a"
})();
function Empty() {
}
function log() {
}
(function () {
    var b = {}, b = navigator, e = b.appVersion, q = b.userAgent, r, o,
        p = b.language || b.systemLanguage, j = !0,
        k = !1, b = {};
    this.ActiveXObject || this.msIndexedDB || e.match(/MSIE/) || e.match(/Edge/) ? (r = "ie", o = e.match(/MSIE (\d+)\./) || e.match(/rv:(\d+)\./) || e.match(/Edge\/(\d+)\./)) : this.mozIndexedDB || this.mozRequestAnimationFrame ? (r = "ff", o = q.match(/Firefox\/(\d+)\./)) : this.chrome || e.match(/Chrome/) ? (r = "ch", o = e.match(/Chrome\/(\d+)\./), k = !0) : this.opera || /Opera/.test(e) ? (r = "op", o = q.match(/Version\/(\d+)\./)) : /Safari/.test(e) ?
        (/iPad/.test(e) ? (r = "ipad", j = !1, k = !0) : /iPhone/.test(e) ? (r = "iphone", j = !1, k = !0) : /Android/.test(e) ? (r = "android", j = !1) : (r = "sa", k = !0), o = e.match(/Version\/(\d+)\./)) : /mobile/i.test(e) ? (j = !1, k = /webkit/i.test(e), o = e.match(/(\d+)/), /IEMobile|Windows Phone/.test(q) ? r = "wp" : /iPad/i.test(e) ? r = "ipad" : /iPhone/.test(e) ? r = "iphone" : /Android/.test(e) && (r = "android")) : r = "unknow";
    b.name = r;
    b.version = o && o[1] ? parseInt(o[1], 10) : 0;
    b.web = j;
    b.webkit = k;
    b.lang = p.toLowerCase();
    b[r] = b[r + b.version] = !0;
    b.wap = !/ipad/i.test(q) && /android|iphone|blackberry|mobile/i.test(q);
    b.os = function () {
        var b = navigator, e = b.userAgent, b = b.platform, j = /^Win/.test(b),
            k = /^Mac/.test(b);
        if (k)return "Mac";
        if (b == "X11" && !j && !k)return "Unix";
        if (b.indexOf("Linux") > -1)return "Linux";
        if (j) {
            if (e.indexOf("Windows NT 5.0") > -1 || e.indexOf("Windows 2000") > -1)return "Win2000";
            if (e.indexOf("Windows NT 5.1") > -1 || e.indexOf("Windows XP") > -1)return "WinXP";
            if (e.indexOf("Windows NT 5.2") > -1 || e.indexOf("Windows 2003") > -1)return "Win2003";
            if (e.indexOf("Windows NT 6.0") > -1 || e.indexOf("Windows Vista") > -1)return "WinVista";
            if (e.indexOf("Windows NT 6.1") > -1 || e.indexOf("Windows 7") > -1)return "Win7"
        }
        return b
    }();
    this.ua = b
})();
(function () {
    function b(b) {
        if (isapp.config("debug")) {
            var b = isapp.makeArray(b), e = b.length, k = b[e - 1];
            typeof k == "number" && e > 1 && r[k] ? (b.pop(), e = r[k]) : e = r[0];
            b.v = e;
            b.unshift("[" + e + "] " + (new Date).toLocaleTimeString() + " -> ");
            return b
        }
        return !1
    }

    var e = isapp.global.console || {
                clear: Empty,
                log: Empty,
                warn: Empty,
                error: Empty
            }, q = [],
        r = ["log", "warn", "error"], o;
    log = ua.ie ? function () {
        if (o = b(arguments)) e[o.v](o.shift()), e[o.v](o)
    } : function () {
        (o = b(arguments)) && e[o.v].apply(e, o)
    };
    log.push = function (b) {
        var e = new Date, k, m;
        k =
            e.getMinutes();
        m = e.getSeconds();
        e = e.getMilliseconds();
        k > 9 || (k = "0" + k);
        m > 9 || (m = "0" + m);
        q.push([k + ":" + m + "." + e, [b]])
    };
    log.output = function () {
        return q
    }
})();
(function () {
    var b, e, q;

    function r(c, b) {
        return R.call(c, b)
    }

    function o(c) {
        var b = typeof c;
        if (b.charAt(0) == "o") {
            if (null === c)return "null";
            b = c.nodeName || S.call(c).slice(8, -1);
            b = b.toLowerCase()
        }
        return b
    }

    function p(c) {
        return "array" == o(c)
    }

    function j(c) {
        return "function" == typeof c
    }

    function k(c) {
        return "object" == o(c)
    }

    function m(c, b) {
        return "string" == typeof c || "string" == o(c) ? b ? !!u(c) : !0 : !1
    }

    function z(c) {
        return c == void 0
    }

    function y(c, b, e) {
        if (e || b instanceof Array)for (var h = 0, e = b.length; h < e; h++)c[h] = b[h]; else for (h in b)r(b,
            h) && (c[h] = b[h]);
        return c
    }

    function C(c) {
        function b(k) {
            h = c[k];
            j = p[o(h)];
            1 == j ? e[k] = h : 4 == j ? (e[k] = new Date, e[k].setTime(h.getTime())) : e[k] = C(h)
        }

        var e, h, j, k, m,
            p = {
                undefined: 1,
                "null": 1,
                number: 1,
                "boolean": 1,
                string: 1,
                array: 2,
                "function": 3,
                date: 4
            };
        if (e = c)if (j = p[o(c)])if (2 == j) {
            e = [];
            for (k = 0, m = c.length; k < m; k++)b(k)
        } else 3 == j ? (e = c + "", m = e.substring(e.indexOf("(") + 1, e.indexOf(")")), k = e.substring(e.indexOf("{") + 1, e.lastIndexOf("}")), "[native code]" == k && log("copy() can't copy native method : " + e, 2), m.length ? (m = m.split(","),
            m[m.length] = k, e = v.apply(x.global, m)) : e = new Function(k)) : 4 == j && (e = new Date, e.setTime(c.getTime())); else for (k in e = new c.constructor, c)b(k);
        return e
    }

    function D(c, b, e) {
        if (c == null || b == null) c = -1; else if (e)a:if (c.lastIndexOf) c = c.lastIndexOf(b); else {
            for (e = c.length - 1; e > -1; e--)if (b === c[e]) {
                c = e;
                break a
            }
            c = -1
        } else a:if (c.indexOf) c = c.indexOf(b); else {
            for (var e = 0, h = c.length; e < h; e++)if (b === c[e]) {
                c = e;
                break a
            }
            c = -1
        }
        return c
    }

    function t(c, b, e) {
        var h;
        if (b > 0) {
            if (c += "", h = c.indexOf("."), h > -1 && (c = c.substr(0, h + b + 1)), (h == -1 ||
                h + b + 1 > c.length) && e) {
                h == -1 ? c += "." : b = b - c.length + h + 1;
                h = 0;
                for (e = []; h++ < b;)e.push(0);
                c += e.join("")
            }
        } else b == 0 && (c = parseInt(c) + "");
        return c
    }

    function s(c, b) {
        var e = "",
            h = b ? "0123456789" : "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM",
            k = c || 10,
            j = h.length;
        for (i = 0; i < k; i++)e += h[Math.floor(Math.random() * j)];
        return e
    }

    function u(c) {
        return null == c ? "" : (c + "").replace(/^\s+/, "").replace(/\s+$/, "")
    }

    function h(c, b) {
        var e, h = localStorage;
        if (m(c))if (null === b) h.removeItem(c); else if (1 == arguments.length)return c = h.getItem(c),
            c = void 0 === c ? void 0 : JSON.parse(c), c == null && (c = void 0), c; else e = JSON.stringify(b), h.setItem(c, e)
    }

    function O(c, b, e) {
        if (j(c))return c.apply(e || x.global, p(b) ? b : [b])
    }

    function G(l) {
        ca = K.body;
        c = K.documentElement;
        G = typeof pageXOffset == "number" ? b : typeof K.compatMode != "undefined" && K.compatMode != "BackCompat" ? e : q;
        return (x.getMousePosition = G)(l)
    }

    var x = isapp, K, P, I;
    K = document;
    P = K.head || K.getElementsByTagName("head")[0];
    var E = Object.prototype, J = {}.constructor, R = E.hasOwnProperty,
        S = E.toString, v = Empty.constructor;
    x.prototype &&
    (x.prototype.author = "web team");
    (function () {
        var c, b, e, h, k, j, m;
        c = location.href;
        e = {};
        j = K.getElementsByTagName("script");
        j = j[j.length - 1] || "";
        j = j.src || "";
        k = c.indexOf("?") + 1 || c.indexOf("#");
        k = k > 0 ? c.substr(0, k).lastIndexOf("/") : c.lastIndexOf("/");
        h = c.substr(0, ++k);
        k = j.indexOf("?");
        if (-1 < k) {
            m = j.substr(k + 1).split("&");
            for (c = 0, b = m.length; c < b; c++)m[c] = m[c].split("="), e[m[c][0]] = m[c][1];
            j = j.substr(0, k);
            /:\//.test(j) || (j = h + j);
            "root" in e || (h = j.substr(0, j.lastIndexOf("/") + 1))
        } else h = j.substr(0, j.lastIndexOf("/") +
            1);
        e.debug = "debug" in e;
        e.css_root = e.css_root ? /\/$/.test(e.css_root) ? e.css_root : e.css_root + "/" : "";
        I = {
            root: h,
            charset: (e.charset || K.charset || K.characterSet).toLowerCase(),
            media: e.media || "all",
            css_root: e.css_root,
            debug: e.debug
        }
    })();
    try {
        if (!window.localStorage || !window.JSON) h = function () {
        }
    } catch (fa) {
    }
    var ca, c;
    b = function (c) {
        return {x: c.clientX + pageXOffset, y: c.clientY + pageYOffset}
    };
    q = function (c) {
        return {x: c.clientX + ca.scrollLeft, y: c.clientY + ca.scrollTop}
    };
    e = function (b) {
        return {
            x: b.clientX + c.scrollLeft, y: b.clientY +
            c.scrollTop
        }
    };
    x.config = function (c) {
        return k(c) ? y(I, c) && void 0 : c ? I[c] : I.debug && I
    };
    x.hasOwn = r;
    x.type = o;
    x.isArr = p;
    x.isFn = j;
    x.isObj = k;
    x.isStr = m;
    x.isNU = z;
    x.isNaN = function (c) {
        return c !== void 0 && isNaN(c)
    };
    x.isRef = function (c) {
        if (c) {
            var b = !1, e = s(4);
            c[e] = 1;
            b = 1 == c[e];
            delete c[e]
        }
        return b
    };
    x.addMember = y;
    x.addProto = function (c, b) {
        var e, h = c && (c.prototype || c.constructor.prototype);
        for (e in b)r(b, e) && (h[e] = b[e])
    };
    x.copy = C;
    x.each = function (c, b) {
        var e, h;
        if (c && j(b))if (c.constructor == J)for (e in c) {
            if (!1 === b.call(c[e],
                    c[e], e))break
        } else for (e = 0, h = c.length; e < h; e++)if (!1 === b.call(c[e], c[e], e))break
    };
    x.walk = function (c, b) {
        var e, h;
        if (c.constructor == J)for (e in c)b.call(c[e], c[e], e); else for (e = 0, h = c.length; e < h; e++)b.call(c[e], c[e], e)
    };
    x.map = function (c, b) {
        var e, h, j;
        if (c.constructor == J)for (e in j = {}, c)j[e] = b.call(c[e], c[e], e); else {
            j = [];
            for (e = 0, h = c.length; e < h; e++)j[e] = b.call(c[e], c[e], e)
        }
        return j
    };
    x.equal = function (c, b, e) {
        return e ? c === b : c && b ? JSON.stringify(c) == JSON.stringify(b) : c == b
    };
    x.functions = function (c, b) {
        var e, h, k = [],
            m = 0;
        if (b)for (e in c)h = c[e], j(h) && (k[m++] = e + "(" + h.length); else for (e in c)j(c[e]) && (k[m++] = e);
        return k
    };
    x.run = O;
    x.has = function (c, b) {
        return D(c, b) > -1
    };
    x.index = D;
    x.intersect = function (c, b) {
        var e = [], h, j, k;
        for (j = 0, k = c.length; j < k; j++)h = c[j], D(b, h) > -1 && e.push(h);
        return e
    };
    x.unique = function (c) {
        if (p(c)) {
            var b, e, h = {}, j, k = [], m = 0;
            for (b = 0, e = c.length; b < e; b++)j = c[b], h[j] || (h[j] = 1, k[m++] = j);
            return k
        }
    };
    x.rand = function (c, b) {
        c = c != void 0 ? c + 0 === c ? c : parseInt(c, 10) : 0;
        b = b != void 0 ? b + 0 === b ? b : parseInt(b, 10) : 0;
        isNaN(c) || isNaN(b) ?
            c = NaN : c == b ? arguments.length == 0 && (c = Math.random()) : (b < c && (c = b), c = Math.ceil(c + (b - c) * Math.random()));
        return c
    };
    x.getAccuracy = t;
    x.parseFloat = function (c, b, e) {
        c = parseFloat(c);
        return isNaN(c) ? c : parseFloat(t(c, b, e))
    };
    x.getRequestUri = function (c) {
        var b = location, e = b.pathname.substr(1);
        return c ? b.protocol + "//" + b.hostname + e : e
    };
    x.getUrlParam = function (c, b) {
        var e, h, j, k, m;
        b || (b = location.href);
        b = b.replace(/#.+$/, "");
        e = b.indexOf("?");
        e > -1 && (b = b.substring(e, b.length));
        if (b.substr(0, 1) == "?" && b.length > 1) {
            e = b.substring(1,
                b.length);
            k = e.split("&");
            for (e = 0, h = k.length; e < h; e++)if (m = k[e].split("="), m[0] == c && m.length == 2) {
                j = m[1];
                break
            }
        }
        return j
    };
    x.escapeHTML = function (c) {
        return c && (c + "").replace(/&/g, "&amp;").replace(/ /g, "&nbsp;").replace(/"/g, "&quot;").replace(/</g, "&lt;").replace(/>/g, "&gt;")
    };
    x.descapseHTML = function (c) {
        return c && (c + "").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, '"').replace(/&nbsp;/g, " ").replace(/&amp;/g, "&")
    };
    x.randString = s;
    x.trim = u;
    x.trimA = function (c) {
        return null == c ? "" : (c + "").replace(/\s/g,
            "")
    };
    x.toUnicode = function (c) {
        var b, e, h, j = "";
        if (m(c))for (b = 0, e = c.length; b < e; b++)h = c[b], j = j + "\\u" + h.charCodeAt(0).toString(16);
        return j
    };
    x.toDBC = function (c) {
        var b = "", e, h, j;
        if (m(c))for (h = 0, j = c.length; h < j; h++)e = c.charCodeAt(h), b += e == 12288 ? String.fromCharCode(32) : e > 65280 && e < 65375 ? String.fromCharCode(e - 65248) : String.fromCharCode(e);
        return b
    };
    x.mergeArray = function (c, b) {
        for (var e = arguments, h = c.length, j, k, m = 1; b = e[m++];)for (j = 0, k = b.length; j < k; j++)c[h++] = b[j];
        c.length = h;
        return c
    };
    x.deleteArrayElement = function (c,
                                     b, e) {
        e && e > 0 || (e = 1);
        return b > -1 ? p(c) ? c.slice(0, b).concat(c.slice(b + e)) : c : c
    };
    x.makeDate = function (c) {
        var b = new Date;
        m(c) && (c = u(c).match(/^(\d+)[\-|\/](\d+)[\-|\/](\d+)$/)) && !c[3] && c[2] && b.getYear()
    };
    x.makeArray = function (c) {
        var b, e, h = [];
        if (z(c))return h;
        try {
            if (h = [].slice.call(c, 0), !h.length && k(c))for (b in c)r(c, b) && h.push(c[b])
        } catch (j) {
            if (c.length == null)for (b in c)r(c, b) && h.push(c[b]); else for (b = 0, e = c.length; b < e; b++)h.push(c[b])
        }
        return h
    };
    x.makeObject = function (c, b) {
        var e, h, j, k, p;
        p = {};
        if (m(c)) {
            c = u(c).replace(/^\.+/,
                "").replace(/\.+$/, "");
            k = c.split(".");
            for (e = k.shift(); j = k.pop();)h = b, b = {}, b[j] = h;
            p[e] = b
        }
        return p
    };
    x.storage = h;
    x.poBegin = function (c) {
        c && console.profile(c)
    };
    x.poEnd = function (c) {
        c && console.profileEnd(c)
    };
    x.run = O;
    x.getEventOffset = function (c) {
        if (c) {
            if (c.offsetX == null) {
                var b, e = c.target;
                e.offsetLeft == void 0 && (e = e.parentNode);
                b = e;
                for (var h = e = 0; b;)e += b.offsetLeft, h += b.offsetTop, b = b.offsetParent;
                b = window.pageXOffset + c.clientX;
                c = window.pageYOffset + c.clientY;
                c = {x: b - e, y: c - h}
            } else c = {x: c.offsetX, y: c.offsetY};
            return c
        }
    };
    x.getVisibleOffset = function (c) {
        var b, e, h, j;
        b = K.body;
        e = K.documentElement;
        h = e.clientWidth || b.clientWidth || 0;
        j = e.clientHeight || b.clientHeight || 0;
        c = void 0 == c.pageX ? {left: c.clientX, top: c.clientY} : {
            left: c.pageX - (e.scrollLeft || b.scrollLeft),
            top: c.pageY - (e.scrollTop || b.scrollTop)
        };
        c.right = h - c.left;
        c.bottom = j - c.top;
        return c
    };
    x.getMousePosition = G;
    x.returnFalse = function () {
        return !1
    };
    x.stopBubble = function (c) {
        if (c) c.stopPropgation && c.stopPropgation(), c.cancelBubble = !0; else if (x.global.event) x.global.event.cancelBubble =
            !0
    };
    x.test = function (c, b, e, h) {
        var k, m, p, o, s;
        if (j(c)) {
            k = new Date;
            m = k.getMinutes();
            p = k.getSeconds();
            m < 10 && (m = "0" + m);
            p < 10 && (p = "0" + p);
            k = m + ":" + p + "." + k.getMilliseconds() + ">>test() start";
            console.log(k);
            try {
                1 < arguments.length ? (h == void 0 && (h = global), (s = 2 < arguments.length) ? (o = c.apply(h, b), e === o ? log(["test(fn) ok"], 0) : log(["test(fn) error", "expect : ", e, "actual : ", o], 2)) : c.apply(h, b)) : c()
            } catch (q) {
                log(q, 2)
            } finally {
                k = new Date, m = k.getMinutes(), p = k.getSeconds(), m < 10 && (m = "0" + m), p < 10 && (p = "0" + p), o = m + ":" + p + "." + k.getMilliseconds() +
                    ">>test() end", console.log(o)
            }
        } else log("test(fn, extra) param fn should be a function", 2)
    };
    x.Flag = function (c) {
        var b = !!c;
        return {
            is: function (c) {
                return c == null ? b : b == !!c
            }, on: function () {
                b = !0
            }, off: function () {
                b = !1
            }
        }
    };
    x.Timer = function () {
        var c = (new Date).getTime(), b = 0;
        return {
            start: function () {
                c = (new Date).getTime();
                b = 0
            }, end: function () {
                b = (new Date).getTime();
                return b - c
            }, differ: function () {
                return (b || (new Date).getTime()) - c
            }
        }
    };
    this.head = P;
    this.hasOwn = r
})();
String.prototype.trim = function () {
    return isapp.trim(this.valueOf())
};
String.prototype.trimA = function () {
    return isapp.trimA(this.valueOf())
};
String.prototype.toDBC = function () {
    return isapp.toDBC(this.valueOf())
};
isapp.hasChinese = function (b) {
    return /[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/.test(b)
};
isapp.isChinese = function (b) {
    return /^[\u4E00-\u9FA5]|^[\uFE30-\uFFA0]/.test(b)
};
isapp.hasJapanese = function (b) {
    return /[\u0800-\u4e00]/.test(b)
};
isapp.isJapanese = function (b) {
    return /^[\u0800-\u4e00]/.test(b)
};
isapp.hasKorean = function (b) {
    return /[\u3130-\u318F]|[\uAC00-\uD7A3]/.test(b)
};
isapp.isKorean = function (b) {
    return /^[\u3130-\u318F]|^[\uAC00-\uD7A3]/.test(b)
};
isapp.isDoubleByte = function (b) {
    return b && b.charCodeAt(0) > 255
};
(function () {
    function b(e, j) {
        function h(b) {
            var e, h, c, j = m.getElementsByTagName("script");
            e = b.getRequires();
            c = {};
            for (h in e)c[e[h]] = h;
            for (b = 0, e = j.length; b < e; b++)c[j[b].src] && delete c[j[b].src];
            e = [];
            b = 0;
            for (h in c)e[b++] = h;
            return e
        }

        var q, r, x, t, z, I, E, J, R, S;
        z = [];
        I = [];
        E = [];
        J = [];
        x = y(e) ? e : D(e) ? o.trimA(e).split(",") : "";
        if ("" == x)return log("use(name, fn) name is should be an array or string", 2);
        for (q = 0, r = x.length; q < r; q++)if (t = k[x[q]], t instanceof p) {
            z[q] = t;
            E[q] = t.exports;
            if (!t.css_loaded) o.css(t.css), t.css_loaded =
                !0;
            if (!t.loaded) S = h(t), S[0] ? (I[I.length] = t, J = J.concat(S)) : t.loaded = !0
        } else return log("undefined Module : <" + x[q] + ">", 2);
        J = o.unique(J);
        if (J[0]) o.require(J, function () {
            for (q = 0, r = I.length; q < r; q++)I[q].loaded = !0;
            b(x, j)
        }); else {
            for (q = 0, r = z.length; q < r; q++)z[q].executed || (E[q] = z[q].exec());
            j && (C(j) ? R = j.apply(o.global, E) : log("use(name, fn) <fn> should be a function"));
            return R
        }
    }

    function e(e) {
        var j = k[e];
        if (j) j.executed || b(e), j = j.exports;
        return j
    }

    function q(b) {
        return o.index(r(), b) > -1
    }

    function r() {
        var b, e = [],
            h = 0;
        for (b in k)e[h++] = k[b].name;
        return e
    }

    var o, p, j, k, m, z, y, C, D, t;
    o = {};
    this.isapp && (o = isapp);
    if (o.prototype) o.prototype.author = "web team";
    m = document;
    z = m.head || m.getElementsByTagName("head")[0];
    y = o.isArr;
    C = o.isFn;
    D = o.isStr;
    t = o.isRef;
    p = function (b, e) {
        var h, j, k;
        e ? C(e) && (e = {exports: e, file: !1}) : e = {};
        h = e.path ? o.trimA(e.path) : "";
        this.name = b;
        this.exports = e.exports || {};
        this.is_fn = C(this.exports);
        this.executed = !this.is_fn;
        this.requires = !e.requires ? [] : y(e.requires) ? e.requires : [e.requires];
        this.css = !e.css ? [] : y(e.css) ?
            e.css : [e.css];
        this.css_loaded = !this.css.length;
        this.file = h ? !0 : !0 == e.file;
        this.loaded = !this.file && !this.requires.length;
        this.path = h ? /:\//.test(h) ? h : o.config("root") + h : !0 == this.loaded ? "" : o.config("root") + b.replace(".", "/");
        for (h = 0, j = this.requires.length; h < j; h++)k = this.requires[h], /\?/.test(k) && (this.requires[h] = k.substr(0, k.indexOf("?")));
        this.path && (/\.js$/.test(this.path) || /\.js\?*$/.test(this.path) || (this.path += ".js"));
        if (e.v && this.path) e.v.indexOf("=") > 0 || (e.v = "v=" + e.v), this.path = this.path + (/\?/.test(this.path) ?
                "&" : "?") + e.v, this.path = this.path.replace(/\?&/, "?")
    };
    p.prototype.getRequires = function () {
        var b = {}, e, h, j, m = this.requires;
        if (m[0])for (e = 0, h = m.length; e < h; e++)j = k[m[e]], j instanceof p ? o.addMember(b, j.getRequires()) : log("undefined Module : <" + m[e] + "> required by <" + this.name + ">", 2);
        this.file && (b[this.name] = this.path);
        return b
    };
    p.prototype.exec = function () {
        var b, e, h = [], j;
        if (!this.executed && this.is_fn) {
            j = this.requires;
            if (j[0]) {
                for (b = 0, e = j.length; b < e; b++)h[b] = k[j[b]].exec();
                this.exports = this.exports.apply(o.global,
                    h)
            } else this.exports = this.exports();
            this.executed = !0
        }
        return this.exports
    };
    k = {};
    j = {
        scripts: {}, loading: !1, list: [], index: 0, stack: function () {
            j.loading = !1;
            j.list = [];
            j.index = 0
        }, inner: null
    };
    j.inner = function (b, e, h) {
        var k;
        j.list = j.list.concat(b);
        if (!j.loading || e) j.loading = !0, (b = j.list[j.index++]) ? j.scripts[b] ? j.inner(0, !0) : (k = m.createElement("script"), k.charset = o.config("charset"), k.type = "text/javascript", k.src = b, k.readyState ? k.onreadystatechange = function () {
            if ("loaded" == k.readyState || "complete" == k.readyState) k.onreadystatechange =
                null, j.scripts[b] = 1, j.inner(0, !0); else if ("error" == k.readyState) k.onreadystatechange = null, j.scripts[b] = -1, j.inner(0, !0, !1)
        } : (k.addEventListener("load", function () {
            j.scripts[b] = 1;
            j.inner(0, !0)
        }, !1), k.addEventListener("error", function () {
            j.scripts[b] = -1;
            j.inner(0, !0, !1)
        }, !1)), z.appendChild(k)) : (j.index--, e = j.stack, j.stack = function () {
            j.loading = !1;
            j.list = [];
            j.index = 0
        }, e(h))
    };
    o.add = function (b, e) {
        var h, j, m, q;
        if (y(b))for (h = 0, j = b.length; h < j; h++)D(b[h].name) ? o.add(b[h].name, b[h].config) : o.add(b[h], {file: 1});
        else if (D(b) && o.trim(b)) {
            e && e.requires ? q = e.requires : q = [];
            k[b] ? (delete k[b], k[b] = new p(b, e), log("Module : module <" + b + "> redefined")) : k[b] = new p(b, e);
            y(q) || (q = [q]);
            for (h = 0, j = q.length; h < j; h++)if (m = o.trim(q[h]), ("" == m || /\s/.test(m)) && log("Module \uff1a required module's name can't cantain any blanks(<" + m + ">)", 2), !k[m]) {
                e = {file: 1};
                if (/\?/.test(m)) m = m.split("?"), e.v = m[1], m = m[0];
                k[m] = new p(m, e)
            }
        } else log("Module constructor : module's name is required and can't cantain any blanks(<" + b + ">)", 2);
        return o
    };
    o.use = b;
    o.require = function (b, e) {
        var h, k = o.type(b);
        if ("string" != k && "array" != k)return log("require(src, fn) <src> should be a string or an array");
        h = j.stack;
        C(e) ? j.stack = function (b) {
            h(b);
            b === !1 || e()
        } : e && log("require(src, fn) <fn> should be a function");
        j.inner(b);
        return o
    };
    o.getModule = e;
    o.hasModule = q;
    o.exports = function (b, j, h) {
        if (b && D(j) && q(b)) t(h) || (h = o.global), h[j] && log("exports(name, alias, context) alias of " + h + " exists"), h[j] = e(b)
    };
    o.list = r;
    o.remove = function (b) {
        return k[b] && delete k[b]
    };
    o.css = function (b,
                      e) {
        if (!b || !(b + "").length)return o;
        var h, j, k, p, q = o.config("css_root");
        b + "" === b && (b = [b]);
        e || (e = o.config("media"));
        for (h = 0, j = b.length; h < j; h++)if (p = b[h], /:\//.test(p) || (p = q + p), p) k = m.createElement("link"), k.charset = o.config("charset"), k.media = e, k.rel = "stylesheet", k.href = p.match(/\.css/) ? p : p + ".css", z.appendChild(k);
        return o
    };
    o.preImage = function (b, e) {
        e = e ? o.trim(e) : "";
        y(b) || (b = [b]);
        for (var h = 0, j = b.length; h < j; h++)b[h] && ((new Image).src = e + b[h]);
        return base
    }
})();
isapp.ajax = function (b, e, q) {
    var r = b || {}, o = r.errnoMap;
    delete r.success;
    delete r.error;
    r.dataType = "text";
    r.type = "post";
    r.contentType = "application/x-www-form-urlencoded";
    r.beforeSend = function (b) {
        var e = isapp.ajax.list.count++;
        b.__count = e;
        isapp.ajax.list[e] = b
    };
    a70712c3563d006f43e992485335da1f
    a70712c3563d006f43e992485335da1f
    r.complete = function (b, j) {
        delete isapp.ajax.list[b.__count];
        if (j != "abort") {
            var k, m = isapp.trim(b.responseText), z = b.status, y = 0;
            if (z != 0) {
                if ("" == m && (log("isapp.ajax() none data received. http status: " + z), z > 200 && z < 300 || z == 304))return;
                try {
                    /\*\/$|--\>$/.test(m) &&
                    (m = isapp.trim(m.replace(/\n|\r/g, "##\\n##").replace(/\/\*.*?\*\/$|<\!--.*?--\>$/, "").replace(/##\\n##/g, "\n"))), k = jQuery.parseJSON(m), y = k.errno
                } catch (C) {
                    k = {}, y = j == "timeout" ? -408 : -500, k.errno = y, z in {
                        400: 1,
                        404: 1,
                        500: 1
                    } ? log([z + " happended. url : " + r.url, JSON.stringify(r.data)], 2) : log(["isapp.ajax() responseText parse Error : ", C, m, r.url, JSON.stringify(r.data)], 2)
                }
                if (y) {
                    k.msg && log("isapp.ajax() error occured, url<" + r.url + ">, msg<" + k.msg + ">");
                    if (o && o[y]) q = o[y]; else for (var D in o)if (D += "", D[0] == ">" && y >
                        D.substr(1) || D[0] == "<" && y < D.substr(1)) {
                        q = o[D];
                        break
                    }
                    isapp.isFn(q) && q(k.errno, k.msg)
                } else isapp.isFn(e) && e(k.data == null ? "" : k.data)
            }
        }
    };
    r.error = function (b, e) {
        if (e == "abort")return !1;
        navigator.onLine || (e = "offline");
        isapp.isFn(q) && q(e)
    };
    return jQuery.ajax(r)
};
isapp.ajax.list = {count: 0};
(function () {
    function b(p, j, k, m, r) {
        var y;
        if (e.isStr(p))if (void 0 === j) {
            j = null;
            if (p = q.cookie.match(RegExp("(^| )" + p + "=([^;]*)(;|$)"))) j = unescape(p[2]);
            return j
        } else if (null === j) b(p, "", -1); else if (y = new Date, y.setTime(y.getTime() + (k || 24) * 36E5), m = void 0 == m ? o ? "." + o : null : m, null != m) q.cookie = p + "=" + escape(j) + ";expires=" + y.toGMTString() + (";domain=" + m) + (void 0 == r ? ";path=/" : ";path=" + r)
    }

    var e = {}, q = document, r = q.domain, o;
    this.isapp && (e = isapp);
    e.prototype && (e.prototype.author = "web team");
    e.domain = {};
    e.domain.config =
        function (b) {
            var j, k;
            for (k in b)hasOwn(b, k) && (e.ent[k] = k, j = b[k], e.domain[k] = {
                m: j.m || r,
                s: j.s,
                top: j.top,
                reg: j.reg || RegExp(j.top.replace(/(\.|\-)/g, "\\$1"))
            });
            return e.detectEnt()
        };
    e.detectEnt = function () {
        var b = e.domain, j, k, m = location.protocol,
            q = location.origin || m + "//" + location.host;
        for (k in b)if (hasOwn(b, k) && b[k].reg && b[k].reg.test(q)) {
            j = k;
            break
        }
        j ? (k = b[j].m, m = b[j].s.https && e.has(m, "https") ? b[j].s.https : b[j].s.http || b[j].s, o = b[j].top) : (k = r, m = null, o = (o = r.match(/\w+\.\w+$/)) && o[0] ? o[0] : null);
        e.ent.current =
            j;
        e.main_domain = k;
        e.static_domain = m;
        e.top_domain = o;
        return j
    };
    b.duplicated = function (e) {
        return e && b(e) ? (e = q.cookie.match(RegExp(e + "=", "g"))) ? e.length > 1 : !1 : !1
    };
    b.unique = function (e, j, k, m) {
        e && (void 0 == j && (j = b(e)), null != j && (b(e, "", -1, r), b(e, "", -1, o), b(e, j, k, m)))
    };
    e.main_domain = null;
    e.static_domain = null;
    e.top_domain = null;
    e.ent = {};
    e.ent.current = null;
    e.cookie = b;
    e.deleteCookie = function (e) {
        b(e, null)
    };
    e.getCookie = function (e) {
        return b(e)
    };
    e.setCookie = function (e, j, k, m) {
        b(e, j, k, m)
    };
    e.getEnt = function () {
        return e.ent.current
    }
})();
(function () {
    function b(b, e) {
        var j, k, m, o, p, q, r;
        r = jQuery("#" + b);
        if (0 == r.length)return log("validator(form_id, validates) form is not found");
        q = !0;
        k = e ? e.length : 0;
        for (j = 0; j < k; j++)if (o = e[j], m = o.rule ? o.rule.trim() : "")if (p = r.find("[name=" + o.name + "]"), m = C(m, p), y.isFn(o.fn) && o.fn.call(p[0], m), 0 !== m) {
            q = !1;
            break
        }
        return q
    }

    function e(b) {
        if (!b)return 2;
        var e = b.indexOf("/") > -1 ? b.split("/") : b.split("-");
        3 == e.length ? (b = new Date, b.setFullYear(e[0], e[1], e[2]), b = e[0] == b.getFullYear() && e[1] == b.getMonth() && e[2] == b.getDate() ?
            0 : 1) : b = 1;
        return b
    }

    function q(b) {
        return !b ? 2 : D.test(b) ? 0 : 1
    }

    function r(b) {
        return !b ? 2 : t.test(b) ? 0 : 1
    }

    function o(b) {
        return !b ? 2 : s.test(b) ? 0 : 1
    }

    function p(b) {
        if (!b)return 2;
        b = y.trim(b).replace(/^(\w+)?:\/\//, "").replace(/\/.*$/, "");
        return u.test(b) ? /-\./.test(b) ? 1 : 0 : 1
    }

    function j(b) {
        if (y.isStr(b)) {
            var e = b[0].type.toLowerCase();
            if ("checkbox" == e)return b[0].checked ? 0 : 1;
            if ("radio" == e)return b.each(function () {
                if (this.checked)return b = 0, !1
            }), b == 0 ? 0 : 1;
            b = b[0].value
        }
        return "" === b ? 1 : !b ? 2 : 0
    }

    function k(b) {
        return 0 === b ?
            0 : !b ? 2 : parseInt(b, 10) + "" === b + "" ? 0 : 1
    }

    function m(b, e) {
        if (!b)return 2;
        var j = b.length, k;
        k = e.trimA();
        k.indexOf(",") > -1 ? (k = k.split(","), k[0] ? k[0] = parseInt(k[0], 10) : k[0] = 0, k[1] ? k[1] = parseInt(k[1], 10) : k[1] = 0, k[0] == k[1] ? j = j == k[0] : (k[0] > k[1] && (k[0] += k[1], k[1] = k[0] - k[1], k[0] -= k[1]), j = k[0] <= j && j <= k[1])) : (k = parseInt(k, 10), j = j == k);
        return j ? 0 : 1
    }

    function z(b, e) {
        if (!b && 0 !== b)return 2;
        var k;
        k = e;
        b = parseFloat(b);
        k.indexOf(",") > -1 ? (k = k.split(","), k[0] ? k[0] = parseFloat(k[0]) : k[0] = 0, k[1] ? k[1] = parseFloat(k[1]) : k[1] = 0, k[0] ==
        k[1] ? k = b == k[0] : (k[0] > k[1] && (k[0] += k[1], k[1] = k[0] - k[1], k[0] -= k[1]), k = k[0] <= b && b <= k[1])) : k = b == parseFloat(k);
        return k ? 0 : 1
    }

    var y = isapp, C = function (b, s) {
        var t, u, y, C, D, E;
        C = {
            date: e,
            email: q,
            mobile: r,
            phone: o,
            url: p,
            empty: j,
            number: k
        };
        E = s.val();
        s.prop("type") == "text" && isapp.trim(E) != E && (E = isapp.trim(E), s.val(E));
        D = b.split("|");
        for (u = 0, y = D.length; u < y; u++) {
            b = D[u];
            if (C[b]) t = C[b]("empty" == b ? s : E); else if (b.indexOf("=") > -1)if (b = b.split("="), b[0] = b[0].trim(), "length" == b[0]) t = m(E, b[1]); else if ("range" == b[0]) t = z(E, b[1]);
            else if ("match" == b[0]) {
                t = E;
                var J = b[1], R = 2, S = void 0;
                void 0 != t && void 0 != J && (S = document.getElementById(J)) && (R = t === S.value ? 0 : 1);
                t = R
            } else t = 2; else t = 2;
            if (0 === t)break
        }
        return t
    };
    b.single = function (b, e, k) {
        b = jQuery("#" + b);
        y.isFn(k) && k.call(b[0], C(e, b))
    };
    var D = /^[A-Za-z0-9!#$%&\'*+\/=?^_`{|}~-]+(\.[A-Za-z0-9!#$%&\'*+\/=?^_`{|}~-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*\.([A-Za-z]{2,})$/,
        t = /^[+]{0,1}\d+$/, s = /^1[3578]{1}[0-9]{9}$/,
        u = /^(?:\w[a-zA-Z0-9-]*?\.)+[a-z]{2,}$|^(?:\d{1,3}\.){3}\d{1,3}$/;
    b.isDate = e;
    b.isEmail =
        q;
    b.isMobile = r;
    b.isUrl = p;
    b.isNumber = k;
    b.isLength = m;
    b.isSeriesNumber = function (b) {
        return /^[0-9]{0,8}$/.test(b) ? !1 : !0
    };
    b.isRang = z;
    b.isPhone = o;
    this.isappFormValidator = b
})();
(function () {
    var b = window.isapp || {};
    b.add([{
        name: "jquery", config: {
            file: 0, v: "1.7.1", exports: function () {
                return jQuery
            }
        }
    }, {
        name: "translator", config: {
            version: 1, file: 0, exports: function () {
                var e = {
                    each: function (e) {
                        var j, o;
                        e.each(function (e, k) {
                            var p = k.getAttribute("langid");
                            if (p && !/\$/.test(p))if (o = k.tagName.toLowerCase(), j = b.getLangText(p), k = jQuery(k), "input" == o)switch (k.attr("type")) {
                                case "button":
                                case "submit":
                                    k.data("deftext", j).val(j);
                                    break;
                                case "text":
                                    k.data("deftext", j).attr("placeholder", j)
                            } else"img" ==
                            o ? k.attr("title", j) : "textarea" == o ? k.attr("placeholder", j) : "title" == o ? document.title = j : "option" == o ? k.html(j) : k.data("deftext", j).html(j)
                        })
                    }
                };
                e.replace = function (b) {
                    return b ? (b = jQuery("<div>" + b + "</div>"), e.each(b.find("[langid]")), b.html()) : ""
                };
                return e
            }
        }
    }, {
        name: "template", config: {
            file: 0, v: "1.0", exports: function () {
                return TrimPath
            }
        }
    }, {
        name: "email_suggest", config: {
            file: 0, v: "1.0", exports: function () {
                return function (b, e, m, o) {
                    var p = new EmailSuggest(b, e, m, o);
                    p.create();
                    p.addListener();
                    p.attach();
                    document.body.onresize =
                        function () {
                            p.attach()
                        }
                }
            }
        }
    }, {name: "scroll", config: {file: 0, requires: "jquery"}}]);
    var e, q = "_ccl", r = {
        en: "en-us",
        zh: "zh-cn",
        "zh-cn": "zh-cn",
        "zh-tw": "zh-tw",
        "zh-Hans": "zh-cn",
        "zh-Hant": "zh-tw",
        "zh-hans": "zh-cn",
        "zh-hant": "zh-tw",
        "de-de": "de-de",
        "en-us": "en-us",
        "fr-fr": "fr-fr",
        "ja-jp": "ja-jp",
        "ko-kr": "ko-kr"
    };
    b.getLangId = function () {
        var j;
        e ? j = b.cookie(q) : (e = 1, j = b.getUrlParam("language") || b.getUrlParam("l") || b.cookie(q));
        j && j.indexOf("_") && (j = j.replace("_", "-"));
        if (r[j])return r[j];
        b.cookie(q, "en-us");
        return "en-us"
    };
    b.setLangId = function (e) {
        b.cookie(q, e, 8760)
    };
    b.setLangCookieId = function (b) {
        q = b
    };
    b.setLangSupports = function (b) {
        return b ? (r = b, !0) : !1
    };
    b.getLangText = function (e) {
        if (e) {
            var k = $("#lang_" + e).html();
            return k == null ? e : b.descapseHTML(k)
        }
    };
    var o = b.translate = function (e) {
        b.use("translator", function (k) {
            b.setLangId(e);
            k.each(jQuery("[langid]"));
            typeof o.fn == "function" && o.fn();
            k = b.getLangId();
            e = {
                    "en-us": "en",
                    "zh-cn": "zh_cn",
                    "zh-tw": "zh_tw",
                    "de-de": "de",
                    "fr-fr": "fr",
                    "ja-jp": "ja",
                    "ko-kr": "ko"
                }[k] || "en";
            jQuery("body #a_bb_download").attr("href",
                "http://appworld.blackberry.com/webstore/content/13707/?lang=" + e);
            for (var m = 0, q = p.length; m < q; m++)p[m](k)
        })
    }, p = [];
    o.addCallback = function (b) {
        typeof b == "function" && p.push(b)
    };
    o.format = function (b) {
        for (var e = /(%\w+)/, m = 1, o; e.test(b) && void 0 != arguments[m];)(o = b.match(e)[1].match(/\d+/)) && o.length ? (o = parseInt(o[0], 10), isNaN(o) && (o = m)) : o = m, m++, b = b.replace(e, arguments[o]);
        return b
    };
    b.translateReplace = function (e) {
        return b.use("translator", function (b) {
            return b.replace(e)
        })
    }
})();
function isappAjax(b, e, q) {
    b && b.hasOwnProperty || (b = {});
    b.errnoMap = {
        ">0": function (b) {
            var e = isappAjaxError[-2], p = isapp.getLangText(e);
            isui.toastCommon(e, 1);
            isapp.isFn(q) && q(b, p, e)
        }, "-105": function () {
            log("token is invalid");
            isappAjax({url: "/user/token"}, function () {
                isappAjax(b, e, q)
            }, function (b, e, p) {
                isui.toastCommon(p, 1)
            })
        }, "-3": function () {
            isui.dialogLogin()
        }, "-4": function () {
            location.reload(!0)
        }
    };
    isapp.ajax(b, e, function (b, e) {
        var p = isappAjaxError[b];
        e || (e = isapp.getLangText(p) || "server busy");
        -2 == b && isui.toastCommon(p,
            1);
        isapp.isFn(q) && q(b, e, p)
    })
}
var isappAjaxError = function () {
    return {
        "-1": "cc_msg_network_busy",
        "-2": "cc_msg_network_busy",
        "-3": "cc_msg_network_busy",
        "-100": "cc_msg_network_busy",
        "-101": "cc_msg_network_busy",
        "-102": "cc_usr_comm_emailnotacceptable",
        "-103": "cc_comm_confirmpassword",
        "-104": "cc_msg_network_busy",
        "-105": "cc_msg_network_busy",
        "-106": "cc_comm_invalidemail",
        "-107": "cc_msg_network_busy",
        "-201": "cc_sys_email_notregistered",
        "-202": "cc_usr_comm_emailreged",
        "-203": "cc_sys_account_notactivated",
        "-204": "cc_comm_acount_activated",
        "-205": "cc_sys_account_disabled",
        "-206": "cc_comm_loginerror",
        "-207": "cc_msg_network_busy",
        "-208": "cc_comm_oldpwdwrong",
        "-211": "cc_comm_temp_unavail_info",
        "-221": "cc_msg_network_busy",
        "-231": "cc_msg_network_busy",
        "-232": "cc_msg_network_busy",
        "-233": "cc_msg_network_busy",
        "-301": "cc_msg_network_busy",
        "-302": "cc_msg_network_busy",
        "-303": "cc_msg_network_busy",
        "-304": "cc_msg_network_busy",
        "-305": "cc_msg_network_busy",
        "-306": "cc_msg_network_busy",
        "-307": "cc_msg_network_busy",
        "-310": "cc_msg_network_busy",
        "-311": "cc_msg_network_busy",
        "-312": "cc_msg_network_busy",
        "-313": "cc_msg_network_busy",
        "-350": "cc_msg_network_busy",
        "-351": "cc_msg_network_busy",
        "-401": "cc_msg_network_busy",
        "-402": "cc_msg_network_busy",
        "-403": "cc_msg_network_busy",
        "-500": "cc_msg_network_busy",
        "-501": "cc_msg_network_busy",
        "-502": "cc_msg_network_busy",
        "-503": "cc_msg_network_busy",
        "-504": "cc_msg_network_busy",
        "-505": "cc_msg_network_busy"
    }
}();
function isappFormAjax(b, e, q) {
    b = $("#" + b);
    isappAjax({url: b.attr("action"), data: b.serialize()}, e, q)
}
var iu = function () {
    var b = [];
    $(function () {
        for (var e = 0, q = b.length; e < q; e++)iu.widget.apply(window, b[e]);
        b = null
    });
    return {
        widget: function () {
            b.push(arguments)
        }
    }
}();
(function () {
    function b(b, e, p, j) {
        window.sessionStorage[p] = JSON.stringify({state: b, title: e});
        q = p;
        b = location.href.replace(location.search, "");
        if (location.search) location.href = b + "#" + p;
        j ? location.replace("#" + p) : location.hash = "#" + p
    }

    function e() {
        var b = location.hash[0] === "#" ? location.hash.substring(1) : location.hash;
        b !== q && ((q = b) ? (b = window.sessionStorage[q], b = JSON.parse(b ? b : "{}")) : b = {}, "onpopstate" in window && typeof window.onpopstate === "function" && window.onpopstate.apply(window, [{state: b ? b.state : null}]))
    }

    if (!("pushState" in
        window.history)) {
        var q;
        window.history.pushState = function (e, o, p) {
            b(e, o, p, !1)
        };
        window.history.replaceState = function (e, o, p) {
            b(e, o, p, !0)
        };
        if ("onhashchange" in window) window.onhashchange = e
    }
})();
function l10n(b) {
    var e = null;
    if (b) {
        var e = (e = document.getElementById("lang_" + b)) ? e.innerHTML : xss(b),
            q, r = arguments.length, o, p, j, k;
        !1 === arguments[r - 1] && (o = !1, r--);
        if (r > 1 && /%[@ds]/.test(e)) {
            p = /%[@ds]\d\b/.test(e);
            for (q = 1; q < r; q++)k = arguments[q], k != null && (j = p ? RegExp("%[@ds]{1}" + q + "?\\b") : /%[@ds]\d?\b/, e = e.replace(j, xss(k, o)));
            k != null && (e = e.replace(/%[@ds](\d\b)?/g, xss(k, o)))
        }
        return e
    }
    return null
}
var getText = l10n;
var xss = xss_html;
function xss_html(b, e) {
    return e == !1 || typeof b !== "string" ? b : b = (b = (b = (b = (b = b && (b + "").replace(/&/g, "&amp;")) && (b + "").replace(/'/g, "&#39;")) && (b + "").replace(/"/g, "&quot;")) && (b + "").replace(/</g, "&lt;")) && (b + "").replace(/>/g, "&gt;")
}
function xss_href(b, e) {
    b = b === void 0 || b === null ? "" : b + "";
    e == !0 && (b = xss(b));
    var q = b.split("&amp;"), b = q[0];
    delete q[0];
    for (var r in q) {
        var o = q[r];
        b += /^\w*=.*/.test(o) ? "&" + o : "&amp;" + o
    }
    b = b.replace(/^[^\w]*j\s*a\s*v\s*a\s*s\s*c\s*r\s*i\s*p\s*t\s*:/i, "javascript :");
    b = b.replace(/^[^\w]*v\s*b\s*s\s*c\s*r\s*i\s*p\s*t\s*:/i, "vbscript :");
    return /^[^\w]*d\s*a\s*t\s*a\s*:(?!\s*image\/(jpeg|jpg|png|gif);)([^;]*;)*base64,(.*)/i.test(b) ? b.replace(/^\s*data:/i, "data :") : b
};isapp.domain.config({
    dev: {s: "www.devstatic.com", top: "devcamcard.com"},
    test: {
        s: {
            http: "static-sandbox.intsig.net",
            https: "static-sandbox-c.intsig.net"
        }, top: "camcard.me"
    },
    pre: {
        reg: /wb\d+.camcard.com|w12013.camcard.com|b12013.camcard.com/,
        s: {http: "static12013.intsig.net", https: "static12013-c.intsig.net"},
        top: "camcard.com"
    },
    online: {
        reg: /w\d{1,3}.camcard.com|www.camcard.com|b.camcard.com|b\d{1,3}.camcard.com/,
        s: {http: "static.intsig.net", https: "static-c.intsig.net"},
        top: "camcard.com"
    }
});
isapp.config({debug: isapp.ent.current == isapp.ent.dev || isapp.ent.current == isapp.ent.test});
is.lang = {
    zh_cn: {},
    zh_tw: {},
    de_de: {},
    en_us: {},
    ja_jp: {},
    ko_kr: {},
    fr_fr: {}
};
isapp.setLangCookieId("_cpl");
isapp.input_type = navigator.userAgent.indexOf("MSIE 9.0") > 0 || navigator.userAgent.indexOf("MSIE 8.0") > 0 ? "keyup" : "input";
(function () {
    var b = location, e = b.href.toLowerCase();
    if (ua.ie && ua.version < 8) {
        if (e.indexOf("/site/upgrade") == -1) b.href = "/site/upgrade"
    } else if (e.indexOf("/site/upgrade") > 0) b.href = "/"
})();
function addZero(b, e) {
    b += "";
    return Array(e - b.length + 1).join("0") + b
}
function showLogoutDialog() {
    iu.dialogSmall("body", {
        text: getText("cc_sys_account_logout"),
        positiveText: getText("cc_sys_relogin"),
        positive: function () {
            location.href = "/user/logout"
        }
    })
}
function isappAjax(b, e, q, r, o) {
    if (!b || !b.hasOwnProperty) b = {};
    b.errnoMap = {
        ">0": function (b, e) {
            var k = isappAjaxError[-2];
            "404" == b && isapp.isFn(o) ? e = getText(k) : isNaN(parseInt(b)) ? e || (e = getText(k)) : (e = getText(k), iu.notice("body", e));
            isapp.isFn(q) && q(b, e, k)
        }, "-3": function () {
            showLogoutDialog()
        }, "-4": function () {
            location.reload(!0)
        }, "-105": function () {
            showLogoutDialog()
        }, "-500": function (b, e) {
            $.isFunction(o) ? o() : $.isFunction(q) && q(b, e)
        }
    };
    if (r !== "false") {
        if (!b.hasOwnProperty("data")) b.data = {};
        r = $("#_csrf_token").attr("name");
        b.data.constructor == String ? b.data += "&" + r + "=" + $("#_csrf_token").val() : b.data[r] = $("#_csrf_token").val()
    }
    return isapp.ajax(b, e, function (b, e) {
        var k = isappAjaxError[b];
        e || (e = getText(k) || "server busy");
        -2 == b && iu.notice("body", e);
        isapp.isFn(q) && q(b, e, k);
        b == -8 && iu.dialogSmall("body", {
            text: getText("csrf_expired_msg"),
            persist: !0,
            positiveText: getText("refresh"),
            positive: function () {
                location.reload()
            }
        })
    })
}
function rowToPage(b, e) {
    return parseInt(b % e == 0 ? b / e : b / e + 1)
}
function updatePages(b, e, q, r, o) {
    var e = rowToPage(e, q), q = "", p;
    o ? p = o - 1 : (o = 10, p = 7);
    if (e > 1) {
        if (e <= o)for (p = 0; p < e; p++)q += '<a class="btn_page" page_id="' + p + '">' + (p + 1) + "</a>"; else if (r >= p && r + o <= e) {
            for (p = r - parseInt(o / 2); p < r - parseInt(o / 2) + o; p++)q += '<a class="btn_page" page_id="' + p + '">' + (p + 1) + "</a>";
            q = '<a class="pre_page_soon" id="pre_page_soon" page_id="0">1</a><span class="dots"></span>' + q + '<span class="dots"></span><a class="next_page_soon" id="next_page_soon" page_id="' + (e - 1) + '">' + e + "</a>"
        } else if (r >= p &&
            r + o > e) {
            for (p = e - o; p < e; p++)q += '<a class="btn_page" page_id="' + p + '">' + (p + 1) + "</a>";
            p = '<a class="pre_page_soon" id="pre_page_soon" page_id="0">1</a>';
            e - o != 1 && (p += '<span class="dots"></span>');
            q = p + q
        } else {
            for (p = 0; p < o; p++)q += '<a class="btn_page" page_id="' + p + '">' + (p + 1) + "</a>";
            o + 1 != e && (q += '<span class="dots"></span>');
            q = q + '<a class="next_page_soon" id="next_page_soon" page_id="' + (e - 1) + '">' + e + "</a>"
        }
        q = r == 0 ? '<a class="pre_page disable" id="pre_page"><span class="icon"></span></a>' + q : '<a class="pre_page" id="pre_page"><span class="icon"></span></a>' +
            q;
        q += r == e - 1 ? '<a class="next_page disable" id="next_page"><span class="icon"></span></a>' : '<a class="next_page" id="next_page"><span class="icon"></span></a>';
        b.html(q).show().find(".btn_page[page_id=" + r + "]").addClass("select")
    } else b.hide()
}
function updateCardPages(b, e, q, r) {
    var e = rowToPage(e, q), o = "";
    e > 0 ? (o = '<a class="card_page_show">' + getText("per_page_show_cards", q) + "</a>", r == 0 ? (o += '<a class="first_page disable" id="first_page" title="' + getText("go_to_first_page") + '"><span class="icon"></span></a>', o += '<a class="pre_page disable" id="pre_page" title=' + getText("pre_page") + '><span class="icon"></span></a>') : (o = o + '<a class="first_page " id="first_page" title="' + getText("go_to_first_page") + '"><span class="icon"></span></a>', o = o + '<a class="pre_page" id="pre_page" title=' +
        getText("pre_page") + '><span class="icon"></span></a>'), o += '<span class="page_tips1">' + getText("di") + "</span>", q = parseInt(r) + 1, o = o + "<input type='text' class='widget_input_search input_jump' id='jump_page' value=" + q + " size=5>", o += '<span class="page_tips2">/&nbsp;&nbsp;' + e + "<span class='page_number'></span>" + getText("page") + "</span>", r == e - 1 ? (o = o + '<a class="next_page disable" id="next_page"  title=' + getText("next_page") + '><span class="icon"></span></a>', o = o + '<a class="last_page disable" id="last_page"  title="' +
        getText("go_to_last_page") + '"><span class="icon"></span></a>') : (o = o + '<a class="next_page" id="next_page"  title=' + getText("next_page") + '><span class="icon"></span></a>', o = o + '<a class="last_page " id="last_page"  title="' + getText("go_to_last_page") + '"><span class="icon"></span></a>'), o += "<input type='hidden' value=" + e + " id='total_page_num' />", o += '<a class="return_top">' + getText("return_top") + "</a>", b.html(o).show().find(".btn_page[page_id=" + r + "]").addClass("select")) : b.hide()
}
function getMapUrl(b) {
    var e = "";
    isapp.isObj(b) ? (b.country && (e += b.country + " "), b.state && (e += b.state + " "), b.city && (e += b.city + " "), b.street1 && (e += b.street1 + " "), b.street2 && (e += b.street2)) : e = b;
    return e = isapp.getLangId() == "zh-cn" && $("#__ENV_ADDRESS").val() == 1 ? "http://api.map.baidu.com/geocoder?address=" + encodeURIComponent(isapp.trim(e)) + "&output=html&src=IntSig|CamCard" : "https://maps.google.com/maps?ie=UTF8&source=CamCard&q=" + encodeURIComponent(isapp.trim(e))
}
function dialogConfirm(b, e, q) {
    iu.dialogConfirm("body", {
        title: b,
        text: e,
        buttonsAlign: "right",
        positiveText: getText("cancel"),
        positive: function () {
        },
        negativeText: getText("confirm"),
        css: "iu_dialog_danger",
        negative: function () {
            isapp.isFn(q) && q()
        }
    })
}
function isWest(b) {
    var e = !0;
    isapp.isStr(b) && b.charCodeAt(0) > 255 && (e = !1);
    return e
}
function get_str_name(b) {
    b = $.trim(b);
    /[\uAC00-\uD7A3]/g.test(b) ? b = b.length / 3 <= 2 ? b.substr(-1, 1) : b.substr(-2, 2) : /[\u0800-\u4e00]/g.test(b) ? b = b.substr(0, 1) : /[\u4e00-\u9fa5]/g.test(b) ? b = b.substr(0, 1) : b.indexOf(" ") != -1 ? (b = b.split(" "), b = b[0].substr(0, 1) + b[b.length - 1].substr(0, 1)) : b = b.substr(0, 2);
    return b
}
function getAvatar(b, e, q, r, o, p, j, k, m, z) {
    var y = "";
    typeof r == "undefined" && (r = " user_photo font_color ");
    typeof o == "undefined" && (o = " ");
    typeof e == "undefined" && (e = "");
    typeof p == "undefined" && (p = !0);
    var b = $.md5(b ? "" + b : "" + e).substr(0, 1), C = "";
    p && (C += " bg_color_" + b, r += C);
    p = get_str_name("" + xss(e));
    p.indexOf('<div class="colleague_jp_pic"></div>') == -1 && (p = xss(p));
    e = " ";
    j && (e += ' avatar_bg="' + b + '"', e += ' avatar_name="' + p + '"');
    if (typeof k == "object")for (var D in k)e += " " + xss(D) + '="' + xss(k[D]) + '"';
    q ? (r = /^[a-zA-Z]+$/,
    !q.match(".png") && !q.match(".jpg") && (q += "&jpg=1"), m && r.test(m) && m !== "script" ? (z = z ? '"' + xss(z) + '"' : '""', y += "<" + m + " class=" + z + ">", y += '<img class="' + xss(o) + '" src="' + xss_href(q, !0) + '" ' + e + ">", y += "</" + m + ">") : y = '<img class="' + xss(o) + '" src="' + xss_href(q, !0) + '" ' + e + "/>") : y = '<div class="' + xss(r) + '" ' + e + ">" + p + "</div>";
    return y
}
$.fn.setCursorPosition = function (b) {
    return this.length == 0 ? this : $(this).setSelection(b, b)
};
$.fn.setSelection = function (b, e) {
    if (this.length == 0)return this;
    var q = this[0];
    q.createTextRange ? (q = q.createTextRange(), q.collapse(!0), q.moveEnd("character", e), q.moveStart("character", b), q.select()) : q.setSelectionRange && (q.focus(), q.setSelectionRange(b, e));
    return this
};
$.fn.focusEnd = function () {
    this.setCursorPosition(this.val().length)
};
var dialog_loading;
$(function () {
    $("body").append($('<div class="loading_video_ff" style="display:none;" id="dialog_ajax_loading"><div class="loading_gif"></div></div>'));
    dialog_loading = iu.dialog($("#dialog_ajax_loading"), {
        close: !1,
        persist: !0,
        show: !1,
        clone: !1,
        duration: 1
    });
    dialog_loading.dialog.addClass("no_dialog_min_limit")
});
function ajax_loading() {
    dialog_loading.show()
}
function ajax_loading_stop() {
    dialog_loading.hide()
}
function parseImTime(b) {
    if (!b)return "";
    b = parseInt(b);
    (b + "").length < 13 && (b = parseInt(b) * Math.pow(10, 13 - (b + "").length));
    var e = isapp.copy(new Date), q = new Date(b), r = "";
    e.setHours(24);
    e.setMinutes(0);
    e.setSeconds(0);
    var o = e.getTime() - 6048E5, e = e.getTime() - 864E5;
    if (b > e) r = addZero(q.getHours(), 2) + ":" + addZero(q.getMinutes(), 2); else if (b > o)switch (q.getDay()) {
        case 0:
            r = "\u661f\u671f\u5929";
            break;
        case 1:
            r = "\u661f\u671f\u4e00";
            break;
        case 2:
            r = "\u661f\u671f\u4e8c";
            break;
        case 3:
            r = "\u661f\u671f\u4e09";
            break;
        case 4:
            r =
                "\u661f\u671f\u56db";
            break;
        case 5:
            r = "\u661f\u671f\u4e94";
            break;
        case 6:
            r = "\u661f\u671f\u516d"
    } else r = q.getFullYear() + "-" + addZero(q.getMonth() + 1, 2) + "-" + addZero(q.getDate(), 2);
    return r
}
function isToday(b) {
    return isSameDay(b, (new Date).getTime())
}
function isSameDay(b, e) {
    if (!b || !e)return !1;
    b = parseInt(b);
    e = parseInt(e);
    (b + "").length < 13 && (b = parseInt(b) * Math.pow(10, 13 - (b + "").length));
    (e + "").length < 13 && (e = parseInt(e) * Math.pow(10, 13 - (e + "").length));
    var q = new Date(b), r = new Date(e);
    return q.getFullYear() == r.getFullYear() && q.getMonth() == r.getMonth() && q.getDate() == r.getDate()
}
Date.prototype.Format = function (b) {
    var e = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        S: this.getMilliseconds()
    };
    /(y+)/.test(b) && (b = b.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length)));
    for (var q in e)RegExp("(" + q + ")").test(b) && (b = b.replace(RegExp.$1, RegExp.$1.length == 1 ? e[q] : ("00" + e[q]).substr(("" + e[q]).length)));
    return b
};
function parseContact(b) {
    var e = {}, q = -1, r = -1, o = -1;
    if (isapp.isArr(b)) {
        for (var p in b) {
            var j = b[p];
            if (j)if (j.key == "contact_id" || j.key == "name" || j.key == "nickname" || j.key == "template_id" || j.key == "front_pic" || j.key == "avatar" || j.key == "back_pic" || j.key == "note" || j.key == "note2") e[j.key] = j, j.key == "template_id" && j.value ? r = p : j.key == "front_pic" && j.value && (o = p); else {
                if (j.key == "address") {
                    if (j.address = j.street1 + " " + j.street2 + " " + j.city + " " + j.state + " " + j.zip + " " + j.country, isapp.hasChinese(j.address) || isapp.hasJapanese(j.address) ||
                        isapp.hasKorean(j.address)) j.address = j.country + " " + j.zip + " " + j.state + " " + j.city + " " + j.street1 + " " + j.street2
                } else if (j.key == "im" || j.key == "sns") {
                    var k = j.key + "_" + j.type.replace(/;.*/ig, "").toLowerCase(),
                        m = getText(k);
                    j.type_text = k == m ? j.type.replace(/;.*/ig, "") : m
                } else if (j.key == "date") {
                    if (j.value) j.type == "EXCHANGEDATE" && (q = p), j.value_format = j.value, k = j.key + "_" + j.type.replace(/;.*/ig, "").toLowerCase(), m = getText(k), j.type_text = "date_exchangedate" == k ? getText("exchange_date") : "date_wedding" == k ? getText("date_other") :
                        k == m ? j.type.replace(/;.*/ig, "") : m
                } else j.key == "website" && (j.href = j.value.indexOf("http://") == -1 && j.value.indexOf("https://") == -1 ? "http://" + j.value : j.value);
                e[j.key] || (e[j.key] = []);
                e[j.key].push(j)
            }
        }
        if (q == -1) e.date || (e.date = []), e.date.push({
            key: "date",
            type: "EXCHANGEDATE",
            value: e.contact_id.timecreate,
            type_text: getText("exchange_date"),
            value_format: (new Date(parseInt(e.contact_id.timecreate * 1E3))).Format("yyyy-MM-dd")
        });
        e.is_template = o == -1 || r > -1 ? 1 : 0
    }
    return e
}
function getHash(b) {
    var e = location.hash.replace("#", ""), q = [], r = {};
    ua.ff && (q = location.href.indexOf("#"), e = q > -1 ? location.href.substr(q + 1) : "");
    if (e) {
        for (var q = e.split("&"), e = 0, o = q.length; e < o; e++) {
            var p = q[e].split("=");
            p.length == 2 && (r[p[0]] = decodeURIComponent(p[1]))
        }
        if (b)return r[b] ? r[b] : ""
    }
    return b ? "" : r
}
function setHash(b) {
    var e = "", q = !0;
    if (isapp.isObj(b)) {
        b.tm = (new Date).getTime();
        for (var r in b)b.hasOwnProperty(r) && (q ? q = !1 : e += "&", e += r + "=" + encodeURIComponent(b[r]))
    } else isapp.isStr(b) && (e = b);
    location.hash = e
}
function updateHash(b) {
    var e = "", q = !0, r = getHash() || {}, o;
    for (o in r)r.hasOwnProperty(o) && b[o] == void 0 && (b[o] = r[o]);
    if (isapp.isObj(b)) {
        b.tm = (new Date).getTime();
        for (var p in b)b.hasOwnProperty(p) && b[p] && (q ? q = !1 : e += "&", e += p + "=" + encodeURIComponent(b[p]))
    } else isapp.isStr(b) && (e = b);
    location.hash = e
}
function checkPersonInfo(b) {
    if (b) {
        var e = 1, q = 1, r = 0, o = 0;
        if (!b.basic_info || !b.basic_info.name || b.basic_info.name.length == 0 || !isapp.trim(b.basic_info.name[0].VALUE[0] + b.basic_info.name[0].VALUE[1])) e = 0;
        if (!b.contact_info || !b.contact_info.telephone || b.contact_info.telephone.length == 0) q = 0;
        if (b.work_info && b.work_info.length > 0)for (var p in b.work_info)if (r == 0 && p.company !== "" && (r = 1), o == 0 && p.title !== "" && (o = 1), r == 1 && o == 1)break;
        return {
            complete: e && q && r && o,
            name: e,
            phone: q,
            company: r,
            title: o
        }
    } else return {
        complete: 0,
        name: 0, phone: 0, company: 0, title: 0
    }
};var CPConfError = {
    PARAM_ILLEGAL: -1,
    SERVER_BUSY: -2,
    LOGIN_INVALID: -3,
    DOMAIN_CHANGE: -4,
    ERROR_PASSWORD: -6,
    ERROR_SYNC: -7,
    ERROR_CSRF_TOKEN: -8,
    PARAMETER_NOT_ACCEPTABLE: -101,
    EMAIL_NOT_ACCEPTABLE: -102,
    TAG_ITEM_NOT_EXISTS: -102,
    TAG_NAME_EXISTS: -103,
    TOKEN_NOT_ACCEPTABLE: -105,
    VERIFICATION_CODE_ERROR: -107,
    AREA_CODE_ERROR: -112,
    TASK_HAS_BEEN_DELETED: -113,
    ACCOUNT_NOT_EXISTS: -201,
    ACCOUNT_REGISTERED: -202,
    ACCOUNT_NOT_ACTIVATED: -203,
    LOGIN_INFO_ERROR: -206,
    MOBILE_NOT_REGISTER: -207,
    IP_ERROR_NORMAL_LOGIN: -208,
    IP_ERROR_ADMIN: -209,
    IP_ERROR_NORMAL: -210,
    TEMPORARILY_UNAVAILABLE: -211,
    COLLEAGUE_NOT_EXISTS: -221,
    CARD_5D_ID_NOT_EXISTS: -232,
    ACCOUNT_UNREGISTERED_BUT_BOUND: -261,
    ACCOUNT_ONLY_FOR_INDIVIDUAL: -262,
    ACCOUNT_IS_CORP: -263,
    ACCOUNT_IS_NOT_CORP: -264,
    INVITE_TOKEN_EXPIRED: -268,
    ACCOUNT_LAST_ADMINISTRATOR: -269,
    ERROR_USER_LIMITATION: -271,
    ACCOUNT_EXPIRED: -275,
    DEPARTMENT_NAME_EXISTS: -277,
    DEPARTMENT_CREATE_SET_ERROR: -278,
    DEPARTMENT_RELATIONSHIP_ERROR: -280,
    DEPARTMENT_SUPERIOR_IS_SELF: -281,
    DEPARTMENT_SUPERIOR_IS_SUBORDINATE: -282,
    DEPARTMENT_SUPERIOR_NOT_EXISTS: -283,
    INVALID_REGISTER_NAME: -289,
    INVALID_REGISTER_CORPORATION: -288,
    FOLDER_NAME_NOT_EXISTS: -302,
    FILE_NAME_NOT_EXISTS: -304,
    NOTE_NOT_FOUND: -304,
    HTTP_TIMEOUT: -408,
    HTTP_INTERNAL_SERVER_ERROR: -500,
    SERVER_PARAM_ILLEGAL: 1001,
    SERVER_JSON_ILLEGAL: 1002,
    SERVER_WEB_ERROR: 2001,
    ITEM_EXISTS: -1050,
    CRM_SERVER_URL_NOT_WORK: -1051,
    CRM_NOT_BIND_CORP: -1052,
    CRM_NOT_BIND_USER: -1053,
    CRM_NOT_ANAME: -1057,
    CRM_USERNAME_PASSWORD_NOT_MATCH: -1058,
    ERROR_CRM_DISABLED: 13,
    ACCOUNT_REMOVE_LAST_ADMINISTRATOR: -1054,
    ACCOUNT_REMOVE_COLLEAGUE_FAIL: -1055,
    FILE_TYPE_ERROR: -1060,
    FILE_SIZE_ERROR: -1061,
    FILE_SERVER_ERROR: -1062,
    FILE_SIZE_TOO_SMALL: -1063,
    USER_NOTREG: -1070,
    USER_BIND: -1071,
    USER_CAMCARD: -1072,
    USER_CORPORATION: -1073,
    USER_REMOVE: -1074,
    CARD_DELETED: 404,
    IM_NOT_IN_GROUP: 111,
    ERROR_IM_NOT_IN_GROUP: -1080
};
/*
 http://www.gnu.org/licenses/gpl.html [GNU General Public License]
 @param {jQuery} {md5:function(string))
 @return string
 */
(function (b) {
    var e = function (b, e) {
        var j, o, p, q, r;
        p = b & 2147483648;
        q = e & 2147483648;
        j = b & 1073741824;
        o = e & 1073741824;
        r = (b & 1073741823) + (e & 1073741823);
        return j & o ? r ^ 2147483648 ^ p ^ q : j | o ? r & 1073741824 ? r ^ 3221225472 ^ p ^ q : r ^ 1073741824 ^ p ^ q : r ^ p ^ q
    }, q = function (b, j, o, p, q, r, t) {
        b = e(b, e(e(j & o | ~j & p, q), t));
        return e(b << r | b >>> 32 - r, j)
    }, r = function (b, j, o, p, q, r, t) {
        b = e(b, e(e(j & p | o & ~p, q), t));
        return e(b << r | b >>> 32 - r, j)
    }, o = function (b, j, o, p, q, r, t) {
        b = e(b, e(e(j ^ o ^ p, q), t));
        return e(b << r | b >>> 32 - r, j)
    }, p = function (b, j, o, p, q, r, t) {
        b = e(b, e(e(o ^ (j | ~p),
            q), t));
        return e(b << r | b >>> 32 - r, j)
    }, j = function (b) {
        var e = "", j = "", o;
        for (o = 0; o <= 3; o++)j = b >>> o * 8 & 255, j = "0" + j.toString(16), e += j.substr(j.length - 2, 2);
        return e
    };
    b.extend({
        md5: function (b) {
            var m = [], z, y, C, D, t, s, u, h,
                m = b.replace(/\x0d\x0a/g, "\n"), b = "";
            for (z = 0; z < m.length; z++)y = m.charCodeAt(z), y < 128 ? b += String.fromCharCode(y) : (y > 127 && y < 2048 ? b += String.fromCharCode(y >> 6 | 192) : (b += String.fromCharCode(y >> 12 | 224), b += String.fromCharCode(y >> 6 & 63 | 128)), b += String.fromCharCode(y & 63 | 128));
            m = b;
            b = m.length;
            z = b + 8;
            y = ((z - z % 64) / 64 +
                1) * 16;
            C = Array(y - 1);
            for (t = D = 0; t < b;)z = (t - t % 4) / 4, D = t % 4 * 8, C[z] |= m.charCodeAt(t) << D, t++;
            C[(t - t % 4) / 4] |= 128 << t % 4 * 8;
            C[y - 2] = b << 3;
            C[y - 1] = b >>> 29;
            m = C;
            t = 1732584193;
            s = 4023233417;
            u = 2562383102;
            h = 271733878;
            for (b = 0; b < m.length; b += 16)z = t, y = s, C = u, D = h, t = q(t, s, u, h, m[b + 0], 7, 3614090360), h = q(h, t, s, u, m[b + 1], 12, 3905402710), u = q(u, h, t, s, m[b + 2], 17, 606105819), s = q(s, u, h, t, m[b + 3], 22, 3250441966), t = q(t, s, u, h, m[b + 4], 7, 4118548399), h = q(h, t, s, u, m[b + 5], 12, 1200080426), u = q(u, h, t, s, m[b + 6], 17, 2821735955), s = q(s, u, h, t, m[b + 7], 22, 4249261313), t =
                q(t, s, u, h, m[b + 8], 7, 1770035416), h = q(h, t, s, u, m[b + 9], 12, 2336552879), u = q(u, h, t, s, m[b + 10], 17, 4294925233), s = q(s, u, h, t, m[b + 11], 22, 2304563134), t = q(t, s, u, h, m[b + 12], 7, 1804603682), h = q(h, t, s, u, m[b + 13], 12, 4254626195), u = q(u, h, t, s, m[b + 14], 17, 2792965006), s = q(s, u, h, t, m[b + 15], 22, 1236535329), t = r(t, s, u, h, m[b + 1], 5, 4129170786), h = r(h, t, s, u, m[b + 6], 9, 3225465664), u = r(u, h, t, s, m[b + 11], 14, 643717713), s = r(s, u, h, t, m[b + 0], 20, 3921069994), t = r(t, s, u, h, m[b + 5], 5, 3593408605), h = r(h, t, s, u, m[b + 10], 9, 38016083), u = r(u, h, t, s, m[b + 15], 14, 3634488961),
                s = r(s, u, h, t, m[b + 4], 20, 3889429448), t = r(t, s, u, h, m[b + 9], 5, 568446438), h = r(h, t, s, u, m[b + 14], 9, 3275163606), u = r(u, h, t, s, m[b + 3], 14, 4107603335), s = r(s, u, h, t, m[b + 8], 20, 1163531501), t = r(t, s, u, h, m[b + 13], 5, 2850285829), h = r(h, t, s, u, m[b + 2], 9, 4243563512), u = r(u, h, t, s, m[b + 7], 14, 1735328473), s = r(s, u, h, t, m[b + 12], 20, 2368359562), t = o(t, s, u, h, m[b + 5], 4, 4294588738), h = o(h, t, s, u, m[b + 8], 11, 2272392833), u = o(u, h, t, s, m[b + 11], 16, 1839030562), s = o(s, u, h, t, m[b + 14], 23, 4259657740), t = o(t, s, u, h, m[b + 1], 4, 2763975236), h = o(h, t, s, u, m[b + 4], 11, 1272893353),
                u = o(u, h, t, s, m[b + 7], 16, 4139469664), s = o(s, u, h, t, m[b + 10], 23, 3200236656), t = o(t, s, u, h, m[b + 13], 4, 681279174), h = o(h, t, s, u, m[b + 0], 11, 3936430074), u = o(u, h, t, s, m[b + 3], 16, 3572445317), s = o(s, u, h, t, m[b + 6], 23, 76029189), t = o(t, s, u, h, m[b + 9], 4, 3654602809), h = o(h, t, s, u, m[b + 12], 11, 3873151461), u = o(u, h, t, s, m[b + 15], 16, 530742520), s = o(s, u, h, t, m[b + 2], 23, 3299628645), t = p(t, s, u, h, m[b + 0], 6, 4096336452), h = p(h, t, s, u, m[b + 7], 10, 1126891415), u = p(u, h, t, s, m[b + 14], 15, 2878612391), s = p(s, u, h, t, m[b + 5], 21, 4237533241), t = p(t, s, u, h, m[b + 12], 6, 1700485571),
                h = p(h, t, s, u, m[b + 3], 10, 2399980690), u = p(u, h, t, s, m[b + 10], 15, 4293915773), s = p(s, u, h, t, m[b + 1], 21, 2240044497), t = p(t, s, u, h, m[b + 8], 6, 1873313359), h = p(h, t, s, u, m[b + 15], 10, 4264355552), u = p(u, h, t, s, m[b + 6], 15, 2734768916), s = p(s, u, h, t, m[b + 13], 21, 1309151649), t = p(t, s, u, h, m[b + 4], 6, 4149444226), h = p(h, t, s, u, m[b + 11], 10, 3174756917), u = p(u, h, t, s, m[b + 2], 15, 718787259), s = p(s, u, h, t, m[b + 9], 21, 3951481745), t = e(t, z), s = e(s, y), u = e(u, C), h = e(h, D);
            return (j(t) + j(s) + j(u) + j(h)).toLowerCase()
        }
    })
})(jQuery);
