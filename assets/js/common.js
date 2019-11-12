/*全局配置*/
var config={
  root:'http://47.74.65.170/azjy/',
  rootjc:'http://47.74.65.170/azjy/',//接口根路径url
  ossroot:'http://ali-sgoss.oss-ap-southeast-1.aliyuncs.com/',//oss根路径
  pagesize: 10,
  msgTime: 2000,
  isTest:false,
  ip: 'http://au.kaifaapp.cn/azjyUserShareHtml/',
  //ip: 'http://47.74.65.170/',
  downroot: 'http://119.23.38.168:8080/',
  shareroot: 'http://47.75.193.38:8080/ynHtmlShare/',
  debug: true,
  ifGoLogin: 0
};

// user = {"head":null,"recommendsn":"VPZAXI","phone":"13699486932","dhhareaid":1,"openid":null,"sex":null,"appid":"602282017112700000075","customerid":50,"nickname":"晨曦","token":"391520a703ef05a50a25980d26cf8008","timestamp":1511960460470};

var AJAX2={
  url:function(){return config.root},
  //url:function(){return "http://192.168.8.108:8080/ygsh/"},
  get:function(api, obj, cb, err, co){
    co = co || 0;
    //var user = CommonFunction.get("user");
    //if (user) {
    //  obj.appid = user.appid;
    //  obj.timestamp = new Date().getTime();
    //  obj = this.objKeySort(obj);
    //  obj.token = user.token;
    //  var arr1 = [];
    //  for (var i in obj) {
    //    arr1.push(i + "=" + obj[i]);
    //  }
    //  obj.sign = md5(arr1.join("&")).toUpperCase();
    //  delete obj.token;
    //}
    //
    //if (co == 1) {
    //  obj.timestamp = new Date().getTime();
    //  obj = this.objKeySort(obj);
    //  obj.token = "eb73spb5c9wakiy0e3stbzjnuts8950e";
    //  var arr1 = [];
    //  for (var i in obj) {
    //    arr1.push(i + "=" + obj[i]);
    //  }
    //  obj.sign = md5(arr1.join("&")).toUpperCase();
    //  delete obj.token;
    //}
    //
    obj = CommonFunction.deleteEmpty(obj);

    if (user && user.token) {
      obj._token = user.token;
    }

    $.ajax({
      type: "get",
      url:AJAX2.url()+api,
      data: obj,
      timeout: 1000*10,
      success: function (a) {
        a = JSON.parse(a);
        !a.data && (a.data = []);
        console.log(a);
        if(a.code == 1) {
          if (api == "api/collect/collectList" || api == "api/business/list" || api == "api/course/list" || api == "api/post/list" || api == "api/post/detaillist" || api == "api/article/articleList" || api == "api/post/myPostList" || api == "api/evaluate/apilist" || api == "api/class/listClasses" || api == "api/mess/list" || api == "api/order/list") {
            cb&&cb(a);
          } else {
            cb&&cb(a.data, a.msg);
          }
        } else if (a.code == 110) {
          //err&&err("未登录", 110);
          config.ifGoLogin++;
          if (config.ifGoLogin == 1) {
            Comm.alert('Logon is invalid, Please log in again.', function () {
              Comm.db("user", null);
              Comm.db("_token", null);
              Comm.gotop('login.html');
            });
          }
        } else if (a.code == 140) {
          err&&err("绑定手机号参数缺失", 140);
        } else {
          err&&err(a.msg);
        }
      },
      error: function () {
        //err&&err("Network error");
      },
    });
  },
  post:function(api, obj, cb, err, co){
    co = co || 0;
    //var user = CommonFunction.get("user");
    //if (user) {
    //  obj.appid = user.appid;
    //  obj.timestamp = new Date().getTime();
    //  obj = this.objKeySort(obj);
    //  obj.token = user.token;
    //  var arr1 = [];
    //  for (var i in obj) {
    //    arr1.push(i + "=" + obj[i]);
    //  }
    //  obj.sign = md5(arr1.join("&")).toUpperCase();
    //  delete obj.token;
    //}
    //
    //if (co == 1) {
    //  obj.timestamp = new Date().getTime();
    //  obj = this.objKeySort(obj);
    //  obj.token = "eb73spb5c9wakiy0e3stbzjnuts8950e";
    //  var arr1 = [];
    //  for (var i in obj) {
    //    arr1.push(i + "=" + obj[i]);
    //  }
    //
    //  console.log(arr1);
    //
    //  obj.sign = md5(arr1.join("&")).toUpperCase();
    //
    //  delete obj.token;
    //}
    //
    obj = CommonFunction.deleteEmpty(obj);

    if (user && user.token) {
      obj._token = user.token;
    }
    console.log(obj);

    $.ajax({
      type: "post",
      url:AJAX2.url()+api,
      data: obj,
      timeout: 1000*10,
      success: function (a) {
        a = JSON.parse(a);
        !a.data && (a.data = []);
        console.log(a);
        if(a.code == 1) {
          if (api == "api/collect/collectList") {
            cb&&cb(a);
          } else {
            cb&&cb(a.data, a.msg);
          }
        } else if (a.code == 110) {
          //err&&err("未登录", 110);
          config.ifGoLogin++;
          if (config.ifGoLogin == 1) {
            Comm.alert('Logon is invalid, Please log in again.', function () {
              Comm.db("user", null);
              Comm.db("_token", null);
              Comm.gotop('login.html');
            });
          }
        } else if (a.code == 140) {
          err&&err("绑定手机号参数缺失", 140);
        } else {
          err&&err(a.msg);
        }
      },
      error: function () {
        //err&&err("Network error");
      },
    });
  },
  objKeySort: function(obj) {//排序的函数
    var newkey = Object.keys(obj).sort();
    //先用Object内置类的keys方法获取要排序对象的属性名，再利用Array原型上的sort方法对获取的属性名进行排序，newkey是一个数组
    var newObj = {};//创建一个新的对象，用于存放排好序的键值对
    for (var i = 0; i < newkey.length; i++) {//遍历newkey数组
      newObj[newkey[i]] = obj[newkey[i]];//向新创建的对象中按照排好的顺序依次增加键值对
    }
    return newObj;//返回排好序的新对象
  },
};

var UTFTranslate = {
  Change:function(pValue){
    return pValue.replace(/[^\u0000-\u00FF]/g,function($0){return escape($0).replace(/(%u)(\w{4})/gi,"&#x$2;")});
  },
  //ReChange:function(pValue){
  //  return unescape(pValue.replace(/&#x/g,'%u').replace(/\\u/g,'%u').replace(/;/g,''));
  //}
};

var CommonFunction = {
  //短信重发限制
  noteLimit:function(classN){
    if(pageData.registerFlag==true){
      pageData.registerFlag=false;
      setTimeout(function(){
        pageData.registerFlag=true;
      },1000*60+1000);
    }
    var d1=document.getElementsByClassName(classN)[0];
    $("." + classN).addClass("codeColor2");
    var x=1*60;
    var time1=setInterval(function(){
      x=x-1;
      d1.innerHTML=x+"秒后重发";
      if(x==0){
        clearInterval(time1);
      }
    },1000*1);
    var time2=setTimeout(function(){
      console.log(d1,classN);
      $("." + classN).removeClass("codeColor2");
      d1.innerHTML="获取验证码";
    },1000*60+2000);
  },
  //请求处理
  requireData: function (data, success, error) {
    if(data.code == 1) {
      success&&success(data.data)
    } else if (data.code == 110) {
      Comm.db('user', null);
      Comm.db('__utoken', null);
      Comm.gotop("login.html");
      Comm.message("该账号已在其他地方登录");
    } else if (data.code == 88) {
      Comm.db('user', null);
      Comm.db('__utoken', null);
      Comm.gotop("login.html");
      Comm.message("出错了！账户已被禁用，请联系客服");
    } else {
      error&&error(data.msg);
      Comm.message(data.msg);
    }
  },
  //Page请求处理
  requirePageData: function (data, success, error) {
    if(data.code == 1) {
      success&&success(data.data)
    } else if (data.code == 110) {
      Comm.db('user', null);
      Comm.db('__utoken', null);
      Comm.gotop("login.html");
      Comm.message("该账号已在其他地方登录");
    } else if (data.code == 88) {
      Comm.db('user', null);
      Comm.db('__utoken', null);
      Comm.gotop("login.html");
      Comm.message("出错了！账户已被禁用，请联系客服");
    } else {
      error&&error(data.msg);
      Comm.message(data.msg);
    }
  },
  //打印
  print: function (a, b, c) {
    console.log(a, b, c);
  },
  save: function (key, obj) {
    key = 'yn_' + key;
    localStorage.setItem(key, JSON.stringify(obj));
  },
  get: function (key) {
    key = 'yn_' + key;
    var obj = JSON.parse(localStorage.getItem(key));
    return obj;
  },
  remove: function (key) {
    key = 'yn_' + key;
    localStorage.removeItem(key);
  },
  //获取主价格
  getMainPrice: function(price){
    return (price+"").split(".")[0];
  },
  //获取副价格
  getSubPrice: function(price){
    return (price+"").split(".")[1] ? ((price+"").split(".")[1].length > 1 ? (price+"").split(".")[1] : (price+"").split(".")[1] + "0") : "00";
  },
  //获取总价格
  getSumPrice: function(p){
    return this.getMainPrice(p) + "." + this.getSubPrice(p);
  },
  //获取url参数
  getUrlQueryString: function(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return (r[2]); return null;
  },
  //储存历史搜索
  savesearch: function(o){
    var u = Comm.db("user");
    console.log(u);
    var addFlag = true;
    if(Comm.db("search_" + u.recommendsn) == null){
      var arr1 = [];
      arr1.unshift(o);
      Comm.db("search_" + u.recommendsn, arr1);
    }else{
      var arr1 = Comm.db("search_" + u.recommendsn);
      for(var index in arr1){
        if(arr1[index].keyword == o.keyword){
          addFlag = false;
          break;
        }
      }
      if(arr1.length < 12 && addFlag == true){
        arr1.unshift(o);
        Comm.db("search_" + u.recommendsn, arr1);
      }else if(arr1.length >= 12 && addFlag == true){
        arr1.unshift(o);
        arr1.pop();
        Comm.db("search_" + u.recommendsn, arr1);
      }
    }
  },
  //储存歌曲历史搜索
  savesong: function(o){
    var u = Comm.db("user");
    var addFlag = true;
    if(Comm.db("song_h_" + u.recommendsn) == null){
      var arr1 = [];
      arr1.unshift(o);
      Comm.db("song_h_" + u.recommendsn, arr1);
    }else{
      var arr1 = Comm.db("song_h_" + u.recommendsn);
      for(var index in arr1){
        if(arr1[index].babysongid == o.babysongid){
          addFlag = false;
          break;
        }
      }
      if(arr1.length < 12 && addFlag == true){
        arr1.unshift(o);
        Comm.db("song_h_" + u.recommendsn, arr1);
      }else if(arr1.length >= 12 && addFlag == true){
        arr1.unshift(o);
        arr1.pop();
        Comm.db("song_h_" + u.recommendsn, arr1);
      }
    }
  },
  //储存历史商品
  saveGoods: function(o){
    var u = Comm.db("user");
    console.log(u);
    var addFlag = true;
    if(Comm.db("saveGoods_" + u.recommendsn) == null){
      var arr1 = [];
      arr1.unshift(o);
      Comm.db("saveGoods_" + u.recommendsn, arr1);
    }else{
      var arr1 = Comm.db("saveGoods_" + u.recommendsn);
      for(var index in arr1){
        if(arr1[index].goodsid == o.goodsid){
          addFlag = false;
          break;
        }
      }
      if(arr1.length < 20 && addFlag == true){
        arr1.unshift(o);
        Comm.db("saveGoods_" + u.recommendsn, arr1);
      }else if(arr1.length >= 20 && addFlag == true){
        arr1.unshift(o);
        arr1.pop();
        Comm.db("saveGoods_" + u.recommendsn, arr1);
      }
    }
  },
  //时间转换 t:时间戳, f: 时间格式(yy-MM-dd hh:mm:ss)
  timeFormat:function(t, f){
    var d1 = new Date(t);
    var year = d1.getFullYear();
    var month = d1.getMonth() + 1;
    month < 10 && (month = "0" + month);
    var data = d1.getDate();
    data < 10 && (data = "0" + data);
    var hours = d1.getHours();
    hours < 10 && (hours = "0" + hours);
    var minutes = d1.getMinutes();
    minutes < 10 && (minutes = "0" + minutes);
    var seconds = d1.getSeconds();
    seconds < 10 && (seconds = "0" + seconds);

    f = f.replace(/yy/g, year);
    f = f.replace(/MM/g, month);
    f = f.replace(/dd/g, data);
    f = f.replace(/hh/g, hours);
    f = f.replace(/mm/g, minutes);
    f = f.replace(/ss/g, seconds);

    return f;
  },
  //时间转换2 t:时间戳, f: 时间格式(yy-MM-dd hh:mm:ss)
  timeFormat2:function(t){
    var f = new Date(t).getTime();
    return f;
  },
  //时间转换3 t:时间戳, f: 时间格式(yy-MM-dd hh:mm:ss)
  timeFormat3:function(t){
    var d1 = new Date().getTime();
    var d2 = new Date(t).getTime();
    var d3 = Math.floor((d1 - d2) / 1000);
    var f;
    if (d3 < 60) {
      f = "just now";
    } else if (d3 < 3600) {
      f = Math.floor(d3 / 60) + " minutes ago";
    } else if (d3 < 3600 * 24) {
      f = Math.floor(d3 / 3600) + " hours ago";
    } else if (d3 < 3600 * 24 *2) {
      f = "yesterday";
    } else {
      f = CommonFunction.timeFormat(d2, "yy-MM-dd hh:mm:ss");
    }
    return f;
  },
  //时间转换4 t:时间戳, f: 时间格式(yy-MM-dd hh:mm:ss)
  timeFormat4:function(t){
    var num = (new Date(t.replace(/-/g, '/')).getTime() - new Date(CommonFunction.timeFormat(new Date().getTime(), "yy/MM/dd")).getTime()) / (1000*60*60*24);
    return num;
  },
  //重新计算倒计时间
  timeSurplus:function(finishTime){
    var num1=finishTime.length;
    var returndata=[];
    for(var i=0;i<num1;i++){
      var timeSurplus=finishTime[i];
      var d1=new Object();
      d1.h=parseInt(timeSurplus/(60*60));
      d1.m=parseInt(timeSurplus/60%60);
      d1.s=parseInt(timeSurplus%60);
      if(d1.h<10){
        d1.h='0'+d1.h;
      }
      if(d1.m<10){
        d1.m='0'+d1.m;
      }
      if(d1.s<10){
        d1.s='0'+d1.s;
      }
      returndata.push(d1);
    }
    return returndata;
  },
  expandImgFc:function (imgurl) {
    console.log(imgurl);
    if(!document.getElementById("expandImgId")){
      var d1 = document.createElement("div");
      d1.id="expandImgId";
      d1.className = "commonImgExpand1";
      document.body.appendChild(d1);
      var d2 = document.createElement("div");
      d2.className = "commonImgExpand2";
      d1.appendChild(d2);
      var d3 = document.createElement("div");
      d3.className = "commonImgExpand4";
      d3.style.cssText = "background-image:url(" + imgurl +")";
      d2.appendChild(d3);
      //setTimeout(function(){
      //  d3.className = "commonImgExpand4";
      //},100);
      d1.onclick = function(){
        d1.remove();
      }
    }
  },
  showIW_time1: null,
  showIW_time2: null,
  showIW:function(msg, t) {
    t = t || 2000;
    layer.msg(msg, {time: t});
    
    // t = t / 1000;
    //
    // if ($("#informationWindowId")) {
    //   $("#informationWindowId").remove();
    // }
    //
    // if (this.showIW_time1) {
    //   clearTimeout(this.showIW_time1);
    //   this.showIW_time1 = null;
    // }
    //
    // if (this.showIW_time2) {
    //   clearTimeout(this.showIW_time2);
    //   this.showIW_time2 = null;
    // }
    //
    // var d1 = document.createElement("div");
    // d1.id = "informationWindowId";
    // d1.style.cssText = "width: 20%;background: #4E4848;color: white;border-radius: 5px;position: fixed;z-index: 10;top: 45%;padding: 8px;text-align: center;left: 40%;opacity:1;transition:" + t + "s;";
    // d1.innerHTML = msg;
    // document.body.appendChild(d1);
    // this.showIW_time1 = setTimeout(function(){
    //   d1.style.cssText="width: 20%;background: #4E4848;color: white;border-radius: 5px;position: fixed;z-index: 10;top: 45%;padding: 8px;text-align: center;left: 40%;opacity:0;transition:" + t + "s;";
    //   // this.showIW_time2 = setTimeout(function(){
    //   //   $("#informationWindowId").remove();
    //   // }, t*1000);
    // },1000);
  },
  //按对象字段排序
  //goodsArr = goodsArr.sort(CommonFunction.compare('enable'));
  compare: function(p) {
  return function (a, b) {
      var value1 = a[p];
      var value2 = b[p];
      return value1 - value2;
    }
  },
  chooseimg:function (cb, max) {
    var ei =layer.open({
      type: 2,
      title: '上传图片',
      area: ['600px', '400px'],
      content: 'uploadImg/upImg.html'
    });
    max&&($(".layui-layer").find("iframe")[0].contentWindow.max_number = max);
    window.imgComplete = function (imglist) {
      if(imglist.length>0){
        layer.close(ei);
        cb&&(cb(imglist));
      } else {
        layer.msg("请先上传图片");
      }
    };
  },
  ifIE: function () {
    if (navigator.appName == "Microsoft Internet Explorer") {
      return true;
    }else{
      return false;
    }
  },
  checkPhone: function (e) {
    var e = e || window.event;
    var targetObj = e.target || e.srcElement;
    if (targetObj.value.length>11) {
      targetObj.value = targetObj.value.slice(0,11);
    }
    targetObj.value = targetObj.value.replace(/[\D]/g,'');
  },
  checkNumber: function (e) {
    var e = e || window.event;
    var targetObj = e.target || e.srcElement;
    targetObj.value = targetObj.value.replace(/[\D]/g,'');
  },
  checkPassword: function (e) {
    var e = e || window.event;
    var targetObj = e.target || e.srcElement;
    targetObj.value = targetObj.value.replace(/[\W]/g,'');
  },
  checkNoNegative: function (e) {
    var e = e || window.event;
    var targetObj = e.target || e.srcElement;
    targetObj.value = targetObj.value.replace(/[^\d.]/g,'');
  },
  checkPositiveInteger: function (e) {
    var e = e || window.event;
    var targetObj = e.target || e.srcElement;
    targetObj.value = targetObj.value.replace(/[^\d.]/g,'').replace(/\./g, '');
  },
  checkTelePhone: function (e) {
    var e = e || window.event;
    var targetObj = e.target || e.srcElement;
    targetObj.value = targetObj.value.replace(/[^\d\-]/ig,'');
  },
  checkTSCode: function (e) {
    var e = e || window.event;
    var targetObj = e.target || e.srcElement;
    targetObj.value = targetObj.value.replace(/\//g, '').replace(/\\/g, '');
  },
  checkOnlyEn: function (e) {
    var e = e || window.event;
    var targetObj = e.target || e.srcElement;
    targetObj.value = targetObj.value.replace(/[^ A-Za-z]/g, "");
  },
  removeEmoji: function(e) {
    var e = e || window.event;
    var targetObj = e.target || e.srcElement;
    var regStr = /[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF][\u200D|\uFE0F]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF]|[0-9|*|#]\uFE0F\u20E3|[0-9|#]\u20E3|[\u203C-\u3299]\uFE0F\u200D|[\u203C-\u3299]\uFE0F|[\u2122-\u2B55]|\u303D|[\A9|\AE]\u3030|\uA9|\uAE|\u3030/ig;
    targetObj.value = targetObj.value.replace(regStr, '');
  },
  deleteEmpty: function (o) {
    for (var i in o) {
      if (o[i] === "" || o[i] === null) {
        delete o[i];
      }
    }
    return o;
  },
  enterKey: function (t) {
    return t.replace(/\n/g, '<br>');
  },
  getImgUrl:function(url,type){
    //if(uri==null)
    //  return "-----------error";
    //if(uri.length>=4 && uri.indexOf("http")>-1)
    //  return uri;
    //var url=z.OSS.host()+uri;
    if(type){
      url +="/";
      switch(type){
        case 'l':url+='800';break;
        case 'f':url+='480';break;
        case 'm':url+='250';break;
        case 's':url+='120';break;
      }
    }
    return url;
  }
};

var SubmitEvent = {
    regDict: {
        m: {t: '手机号码格式错误', e: /^1[34578]\d{9}$/},
        p: {t: '密码格式错误（6-16位）', e: /^[a-zA-Z0-9]{6,16}$/},
        c: {t: '验证码格式错误', e: /^.{6,6}$/},
        length4: {t: '位数错误（4-20位）', e: /^.{4,20}$/},
        required: {t: '必填项', e: /^.{1,999}$/},
        money: {t: '价格格式错误', e: /(^[1-9]\d*(\.\d{1,2})?$)|(^0(\.\d{1,8})?$)/},
        amount: {t: '数量格式错误', e: /(^[1-9]\d*(\.\d{1,2})?$)|(^0(\.\d{1,8})?$)/},
        bankac: {t: '位数错误（10-20位）', e: /^.{10,20}$/},
        email: {t: '邮箱格式错误', e: /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/},
        nz: {t: '请输入正整数', e: /^\d+$/},
        rn: {t: '请输入正确姓名', e: /[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*/},
        cd: {t: '请输入正确身份证号', e: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/
        }
    },
    getParams:function (n) {
        !n&&(n="param");
        var valok=true;
        var obj = {};
        $("["+n+"]").each(function () {
            var p = this.getAttribute(n);
            if(p){
                obj[p]="";
                if(this.tagName=="INPUT" || this.tagName=="TEXTAREA" ){
                    obj[p] = this.value;
                } else {
                    var d = this.getAttribute("data");
                    if(d) {
                        obj[p] = d;
                    }
                }
            }
            var va = this.getAttribute("va");
            var reg = SubmitEvent.regDict[va];

            if(va && reg) {
                if(!reg.e.test(obj[p]) && valok) {
                    var vat = this.getAttribute("vat");
                    if(obj[p].toString().length>0)vat=null;
                    mui.toast(vat?vat:reg.t);
                    valok=false;
                }
            }
        });
        return valok?obj:null;
    },
    params:function (obj, n) {
        !n&&(n="param");
        $("["+n+"]").each(function () {
            var p = this.getAttribute(n);
            if(obj[p]){
                if(this.tagName=="INPUT" || this.tagName=="TEXTAREA" ){
                    this.value = obj[p];
                } else {
                    this.setAttribute("data",obj[p]);
                }
            }
        });
    }
};


//事件格式化
Date.prototype.format = function(format) {
    !format&&(format = "yyyy-MM-dd HH:mm:ss");
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "H+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
};


var Tool = {
    getDict:function (list, key) {
        var dict = {};
        for(var i=0;i<list.length;i++){
            if(list[i][key]) {
                dict[list[i][key]] = list[i];
            }
        }
        return dict;
    },
    getIds:function (list, key) {
        var ids = [];
        for(var i=0;i<list.length;i++){
            if(list[i][key]) {
                ids.push(list[i][key]);
            }
        }
        return ids.length>0?ids.join(","):null;
    }

};
var Cart = {
    save:function (goods, count) {
        var cart = Comm.db("cart");
        !cart&&(cart=[]);
        var exist = -1;
        for(var i=0;i<cart.length;i++){
            if(cart[i].goodsid==goods.goodsid){
                exist = i;
                break;
            }
        }
        if(exist<0){
            cart.unshift({goodsid:goods.goodsid, count:parseInt(count)});
        } else {
            cart[i].count = parseInt(cart[i].count) + parseInt(count);
        }
        Comm.db("cart", cart);
    },
    get:function () {
        var cart = Comm.db("cart");
        !cart&&(cart=[]);
        return cart;
    },
    change:function (g, c) {
        var cart = Comm.db("cart");
        !cart&&(cart=[]);
        for(var i=0;i<cart.length;i++) {
            if(cart[i].goodsid==g){
                if(c) {
                    cart[i].count = c;
                } else {
                    cart.splice(i, 1);
                }
                break;
            }
        }
        Comm.db("cart", cart);
    },
    delete_c: function(a){
      if (a.length <= 0) {
        Comm.message("请选择要删除的商品");
      } else {
        var cart = Comm.db("cart");
        !cart&&(cart=[]);
        for (var i=0;i<a.length;i++) {
          for (var j=0;j<cart.length;j++) {
            if (a[i] == cart[j].goodsid) {
              cart.splice(j, 1);
              break;
            }
          }
        }
        Comm.db("cart", cart);
        Comm.message("已删除选中商品");
      }
    }
};


var Cart2 = {
  save: function (goodsid, sku, count, goodsDetail) {
    var u = Comm.db("user");
    var c = Comm.db("cart_" + u.recommendsn);

    console.log(goodsid, sku, count, goodsDetail);

    console.log(c);

    !c && (c = {});
    if (c[goodsid + "|" + sku.propertyid]) {
      c[goodsid + "|" + sku.propertyid].count = c[goodsid + "|" + sku.propertyid].count + count;
    } else {
      c[goodsid + "|" + sku.propertyid] = {goodsid: goodsid, sku: sku, count: count, goodsDetail: goodsDetail};
    }
    Comm.db("cart_" + u.recommendsn, c);
    console.log(c);
  },
  change: function (goodsid_propertyid, count) {
    var u = Comm.db("user");
    var c = Comm.db("cart_" + u.recommendsn);
    if (!c) {
      return;
    }
    if (c[goodsid_propertyid]) {
      c[goodsid_propertyid].count = count;
      Comm.db("cart_" + u.recommendsn, c);
    } else {
      return;
    }
  },
  delete: function (goodsid_propertyid) {
    var u = Comm.db("user");
    var c = Comm.db("cart_" + u.recommendsn);
    if (!c) {
      return;
    }
    if (c[goodsid_propertyid]) {
      delete c[goodsid_propertyid];
      Comm.db("cart_" + u.recommendsn, c);
    } else {
      return;
    }
  }
}

//P1:type(1:主页，2：非主页)
//P2:页面地址
function checkIsLogin(t, h) {
  var user = Comm.db('user');
  var string = "If you are not logged in, do you want to jump to the login screen?";
  if (!user) {
    Comm.confirm(string,function(a){
      if (a==1) {
        Comm.gotop('login.html');
      } else {

      }
    });
    return false;
  } else {
    if (t == 1 && h) {
      window.location.href = h;
    } else if (t == 2 && h) {
      Comm.go(h);
    }
    return true;
  }
}

function checkIsLogin2() {
  var user = Comm.db('user');
  var string = "If you are not logged in, do you want to jump to the login screen?";
  if (!user) {
    Comm.confirm(string, function(a){
      if (a==1) {
        Comm.gotop('login.html');
      } else {

      }
    });
    return false;
  } else {
    return true;
  }
}

//P1:type(1:主页，2：非主页)
//P2:页面地址
function checkIsLogin3(t, h) {
  var user = Comm.db('user');
  var string;
  if (localStorage.language == "cn") {
    string = "您还未登录，是否跳转到登录界面？";
  } else if (localStorage.language == "en") {
    string = "If you are not logged in, do you want to jump to the login screen?";
  }
  if (!user) {
    Comm.confirm(string,function(a){
      if (a==1) {
        Comm.go('login.html');
      } else {

      }
    });
    return false;
  } else {
    if (t == 1 && h) {
      window.location.href = h;
    } else if (t == 2 && h) {
      Comm.go(h);
    }
    return true;
  }
}

//P1:type(1:主页，2:商品详情)
function cart_sum_num(n) {
  var u = Comm.db("user");

  if (!u) {
    return;
  }

  var cart = Comm.db("cart_" + u.recommendsn);
  var num1 = 0;
  for (var i in cart) {
    num1 += cart[i].count;
  }

  if (n == 1) {
    if (num1 > 0 && num1 <= 99) {
      $(".cart-sum-num").html(num1).removeClass("hide");
    } else if (num1 > 99) {
      $(".cart-sum-num").html("99+").removeClass("hide");
    } else {
      $(".cart-sum-num").html("0").addClass("hide");
    }
  } else if (n == 2) {
    if (num1 > 0 && num1 <= 99) {
      $(".cart-sum-num2").html(num1).removeClass("hide");
    } else if (num1 > 99) {
      $(".cart-sum-num2").html("99+").removeClass("hide");
    } else {
      $(".cart-sum-num2").html("0").addClass("hide");
    }
  }
}

function getMessage(data) {
  console.log(data);
  data = JSON.parse(data);

  if (!data || !data.appid){return;}

  var getAppid = data.appid;
  var arr = Comm.db('chatIdArr') || [];
//  if (arr.indexOf(getAppid)>-1){
//    return;
//  } else {
      var parms = {appid:getAppid};
      AJAX.POST('/api/partner/modifyHaveConsultation',parms,function (d) {
        if (d.code == 1){
          arr.push(getAppid);
          Comm.db('chatIdArr',arr);
          getNew && getNew();
        }
      });
//  }

  // alert(JSON.stringify(data));
}

function reactOut() {
  Comm.db("user", null);
  Comm.db("_token", null);
  Comm.gotop("login.html");
}

//生成弹成筛选
var ComBox = function (v, data, cb) {
    Comm.loading(1);
    document.querySelector("#loadingbox").style.display = "none";
    var cssz = ".combox{z-index:999;width:50%;height:auto;background:#fff;";
    cssz += "position:fixed;left:25%;top:25%;border-radius:0.5rem;}";
    cssz += ".combox>div{padding:1.3rem;font-size:16px;border-bottom:1px solid #f1f1f1;}";
    cssz += ".combox :last-child{padding:1.3rem;font-size:16px;border-bottom:1px solid #fff;}";
    cssz += ".combox>.check{background-image:url(data:image/png;base64,iVBORw0KGgoAAA" +
        "ANSUhEUgAAAEAAAABACAYAAACqaXHeAAAExElEQVR4Xu2aXWgcVRTH/2dm527alJbU5mO3WmusS" +
        "acBEaUQSyOFFgrtS3ypoBbsgxXBh/rgkwp+gO/FFz9QSn2ognmwVIsVNI1ttNaXPqQ7G4ktFGc2" +
        "MYXQQpvc2cyRIS5mZzPJ7szsyuzOPu7ee875/+ace3f/LKHFX9Ti+pEASDqgxQkkI9DiDZAcgsk" +
        "IJCPQ4gSSEWjxBkhugWQEkhFoYgKbeceDaZn6DAQB0OeWyH3hldu0I9DDOzpJpn4jou0l0Qxnty" +
        "Xyvy+H0JQAOrh3U5tM/0IEfblYhvOqJfIfNTWALGfXs71xjEBPlYtHsahh19+U+6N5AfCAyNjOD" +
        "wQ8Uy6emRgvmGnjTPOeAYxURurfEOGQV+RKrV9a0xxnAEPJSH2ECMMV4onftDTjA7/LrikAZKR+" +
        "moCjlU8eJy2RO7HaTR97AFm580OAXqsUyadMYRxb62tOrAFkbP0tYrxf8eSZRyxhHAHBaVoAGbn" +
        "zZQJ9UiGQ+ZwpjGdBKK4l3v08lh3QvaA/pxCfIVBZ/QwetTT1IGhCViM+lgB6ivohcvgsgVTPXX" +
        "+VtDv7TDLvVSs+dgCytn4AjG8B98fNfy8GJu5r9/fO0c25WsTHCkC31AdV8E8AtZWJZ77hCHtwm" +
        "qZmahUfGwBdC/2Pq6RcJmBDmUhmE8IeNGnqVhDxvgCyUn8SjK8YkKzijUIq913QBGH3dbL+mGbj" +
        "VwCby2PxbalhzywZk2FyVNwCnTywIWUvThGoyw3M4EVietFM574MkyjI3iw/+hBL7QoRZTwH3t1" +
        "FOEMzYvJakLjL91QAyNh9Q8TqmCchE9PzjYSwkqGxVBPPFwn7ZzRjPKz4FUfATazYWh5Ax/8Fwd" +
        "/QQBHEhy3NuBCFeN8zoGdh1wCRM0qgLY2G4G9osAMFR6yUMRKV+FVvgSz397NNlxoKgbe3Zex13" +
        "3sNDbdQB3ysIIxTUYpf8xpsKARGKmvr5wEc8IpkOCcskT8Ztfg1AbgLliAoFwnorts4rGJogPld" +
        "M228Uw/xVQFwF3VzX69iq+MrQWDQSwWROx2mwDCGRpi8VQMoQVClMgaird5OCAMhrKHRMABuogz" +
        "rD5Pky1FB6LH1txXGexUzX4Oh0VAAUULwMzSY+YIljMPVGhoNB1CCAIkxImwLMg5+hgbA46Y2vx" +
        "90cz6ssGr3B3aEHuC+rUKq47VCyBb7htlRvvYaGgBfkxoPzVL+brXFR7EuMAA3+RIEZYyIer2dA" +
        "OAVSxifLn/fNTSYcZ6AVFnxzJMksOcvMm5HIaqWGKEAuIm6+JFuVabHvRDczxh8vATBz9AA45YU" +
        "zuAs5c1aCo9qbWgAJQgpmb4Iov6KEx18fJH5ykqGBoNnHM15epom/4xKUK1xIgHgJs1y3xZI5ZI" +
        "PhDsE2ugpbs5h2ltIX5+otego10cGYC0I5WcE7jEW9xXE5NUoxQSJFSmAEgS21VECBnwKkg7xwY" +
        "JmjAYpOOo9kQNwC9zE2zrW2+0/eyG49pqjYHg6ZZyLWkjQeHUBUILQbrf/COCJf28E3z8pBC0+i" +
        "n11A+AW51pb66R4nUG7SXU+NlP5s1EUHWWMugKIstB6xUoA1ItsXOImHRCXJ1WvOpMOqBfZuMRN" +
        "OiAuT6pedf4DkugMXwC605YAAAAASUVORK5CYII=);";
    cssz += "background-repeat:no-repeat;background-position:90% 8px;background-size:15%;}";
    var html = "";
    for (var i = 0; i < data.length; i++) {
        if (data[i].value == v) {
            html += "<div class='check' data-value='" + data[i].value + "'>" + data[i].text + "</div>";
        } else {
            html += "<div data-value='" + data[i].value + "'>" + data[i].text + "</div>";
        }
    }
    var cz = document.createElement("style");
    cz.innerHTML = cssz;
    cz.setAttribute("id", "ComBoxStyle");
    var dv = document.createElement("div");
    dv.setAttribute("class", "combox");
    dv.setAttribute("id", "ComBoxDom");
    dv.innerHTML = html;
    document.body.appendChild(cz);
    document.body.appendChild(dv);
    var is = dv.querySelectorAll("div");
    for (var i = 0; i < is.length; i++) {
        is[i].onclick = function () {
            var items = dv.querySelectorAll("div");
            for (var j = 0; j < items.length; j++) {
                if (items[j].getAttribute("class")) {
                    items[j].setAttribute("class", items[j].getAttribute("class").replace("check", ""));
                }
            }
            this.setAttribute("class", "check");
            cb && cb(this.getAttribute("data-value"));
            document.body.removeChild(document.querySelector("#ComBoxStyle"));
            document.body.removeChild(document.querySelector("#ComBoxDom"));
            Comm.loading(0);
        }
    }
}

/* 语言切换begin */
var LngObj = {
  Cookie: {
    set: function(name, value) {
      document.cookie = name + "=" + escape(value);
    },
    get: function(name) {
      var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
      if(arr = document.cookie.match(reg))
        return unescape(arr[2]);
      else
        return null;
    }
  },
  tag: function() {
    return "__lang";
  },
  init: function() {
    $("[data-lang]").each(function(index,item){
      var me = $(this);
      var a = me.attr('data-lang').split(':');
      var p = a[0]; //文字放置位置
      var m = a[1]; //文字的标识

      //用户选择语言后保存在cookie中，这里读取cookie中的语言版本
      //var lan = LngObj.Cookie.get('__lang');

      var lan = LngObj.getLng();
      //选取语言文字
      switch(lan) {
        case 'cn':
          var t = cn[m]; //这里cn[m]中的cn是上面定义的json字符串的变量名，m是json中的键，用此方式读取到json中的值
          break;
        case 'en':
          var t = en[m];
          break;
        default:
          var t = cn[m];
      }
      //如果所选语言的json中没有此内容就选取其他语言显示
      if(t == undefined) t = cn[m];
      if(t == undefined) t = en[m];

      if(t == undefined) return true; //如果还是没有就跳出

      //文字放置位置有（html,val等，可以自己添加）
      switch(p) {
        case 'html':
          me.html(t);
          break;
        case 'place':
          me.attr("placeholder",t);
          break;
        case 'val':
        case 'value':
          me.val(t);
          break;
        default:
          me.html(t);
      }
    });
  },
  set: function(value) {
    LngObj.Cookie.set(LngObj.tag(), value);
    LngObj.init();
  },
  setLng: function(value){
    localStorage.language = value;
    LngObj.init();
  },
  getLng: function(){
    return localStorage.language;
  },
  get_js_lan:function(m) {
    //获取文字
    var lan = LngObj.getLng(); //语言版本
    //选取语言文字
    switch(lan) {
      case 'cn':
        var t = cn[m];
        break;
      case 'en':
        var t = en[m];
        break;
      default:
        var t = cn[m];
    }
    //如果所选语言的json中没有此内容就选取其他语言显示
    if(t == undefined) t = cn[m];
    if(t == undefined) t = en[m];

    if(t == undefined) t = m; //如果还是没有就返回他的标识
    return t;
  }
};

function langTransform(o) {
  var d1 = localStorage.language;
  var d2;

  if (d1 == "cn") {
    d2 = cn[o];
  } else if (d1 == "en") {
    d2 = en[o];
  }

  return d2;
}

//解决弹出键盘遮挡输入框
window.addEventListener("resize", function () {
  if (document.activeElement.tagName == "INPUT" || document.activeElement.tagName == "TEXTAREA") {
    window.setTimeout(function () {
      document.activeElement.scrollIntoView(true);
      document.activeElement.scrollIntoViewIfNeeded(true);
    }, 100);
  }
  Comm.resizeSection();
})

//function goClientDetail(appid) {
//  Comm.go("clientDetail.html?appid=" + appid);
//}

//**** layer start ****//
// layer.open({
//   type: 0,
//   title: "test",
//   content: '传入任意的文本或html', //这里content是一个普通的String
//   skin: "",
//   offset: 'auto',
//   area: 'auto',
//   shadeClose: true,
//   anim: 0,
//   btn: ['按钮一', '按钮二', '按钮三'],
//   yes: function(index, layero){
//     //按钮【按钮一】的回调
//
//     console.log(1);
//     layer.close(index);
//   },
//   btn2: function(index, layero){
//     //按钮【按钮二】的回调
//
//     //return false 开启该代码可禁止点击该按钮关闭
//
//     console.log(2);
//   },
//   btn3: function(index, layero){
//     //按钮【按钮三】的回调
//
//     //return false 开启该代码可禁止点击该按钮关闭
//
//     console.log(3);
//   },
//   cancel: function(){
//     //右上角关闭回调
//
//
//     //return false 开启该代码可禁止点击该按钮关闭
//
//     console.log(4);
//   },
//   success: function(layero, index){
//     console.log(layero, index);
//   }
// });

// layer.alert('加了个图标', {title: "提示", shadeClose: true});

// layer.msg('同上', {time: Config.msgTime});

// layer.confirm('is not?', {icon: 3, title:'提示', shadeClose: true}, function(index){
//   //do something
//
//   layer.close(index);
// }, function() {
//
// });

// layer.load(2, {shade: [0.1, '#000']});

// var laypage = null;
// layui.use('laypage', function(){
//   laypage = layui.laypage;
// });
//
// laypage.render({
//   elem: 'page',
//   count: a.totalCount,
//   limit: Config.pagesize,
//   theme: "#fb5933",
//   layout: ['prev', 'page', 'next', 'skip'],
//   jump: function(obj, first){
//     console.log(obj);
//     pageData.pageno = obj.curr;
//
//     if (!first) {
//       goodsList();
//     }
//   }
// });

// var laydate = null;
// layui.use('laydate', function(){
//   laydate = layui.laydate;
// });
//
// laydate.render({
//   elem: '#a-arrivaltime', //指定元素
//   type: "datetime",
//   format: "yyyy-MM-dd HH:mm",
//   min: new Date().getTime(),
//   done: function(value, date, endDate){
//     console.log(value); //得到日期生成的值，如：2017-08-18
//     console.log(date); //得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
//     console.log(endDate); //得结束的日期时间对象，开启范围选择（range: true）才会返回。对象成员同上。
//   }
// });
//**** layer end ****//
