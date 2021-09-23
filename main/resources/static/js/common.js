// Get data from cookie
function getCookie(cname) {
    var ss = document.cookie;
    var name = cname + "=";
    var ca = ss.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i].trim();
        if (c.indexOf(name) == 0)
            return c.substring(name.length, c.length);
    }
    return "";
}

// Get parameter from URL
Url = {
    get get() {
        var vars = {};
        if (window.location.search.length !== 0)
            window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m, key, value) {
                key = decodeURIComponent(key);
                if (typeof vars[key] === "undefined") { vars[key] = decodeURIComponent(value); } else { vars[key] = [].concat(vars[key], decodeURIComponent(value)); }
            });
        return vars;
    }
};

// 對Date的擴展，將 Date 轉化為指定格式的String
// 月(M)、日(d)、小時(h)、分(m)、秒(s)、季度(q) 可以用 1-2 個預留位置，
// 年(y)可以用 1-4 個預留位置，毫秒(S)只能用 1 個預留位置(是 1-3 位元的數字)
// 例子： 
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
// var time1 = new Date().Format("yyyy-MM-dd");
// var time2 = new Date().Format("yyyy-MM-dd HH:mm:ss");  
Date.prototype.Format = function(fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

function InvalidURLException() {  
    this.message = "An attempt was made to open a webpage of foreign domain. No allowed.";  
    this.toString = this.message;
}

function redirect (url) {
    try {
        if ( validateURL(url) ) window.open(url)
        else throw new InvalidURLException()
    } catch (e) { if (e instanceof InvalidURLException) alert(e.message) }
}

function validateURL(surl) {
    var url = parseURL(surl);  
    var urlHostname = url.hostname.trim();  
    if (urlHostname == '') return true; 
    else return urlHostname.toUpperCase() == location.hostname.trim().toUpperCase() ? true : false  
}

function parseURL(url) {
    var a = document.createElement('a');  
    a.href = url;  
    return {  
        source: url,  
        protocol: a.protocol.replace(':', ''),  
        hostname: a.hostname,  
        host: a.host,  
        port: a.port,  
        query: a.search,  
        params: (function () {  
            var ret = {},  
                seg = a.search.replace(/^\?/, '').split('&'),  
                len = seg.length, i = 0, s;  
            for (; i < len; i++) {  
                if (!seg[i]) { continue; }  
                s = seg[i].split('=');  
                ret[s[0]] = s[1];  
            }  
            return ret;  
        })(),  
        file: (a.pathname.match(/\/([^\/?#]+)$/i) || [, ''])[1],  
        hash: a.hash.replace('#', ''),  
        path: a.pathname.replace(/^([^\/])/, '/$1'),  
        relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [, ''])[1],  
        segments: a.pathname.replace(/^\//, '').split('/')  
    };  
}