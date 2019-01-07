//得到url地址，拿出指定参数的值
function acceptParam(paras){
	var url = location.href;
	var paraString = url.substring(url.indexOf("?")+1,url.length).split("&");
	var paraObj = {}
	for (i=0; j=paraString[i]; i++){
		paraObj[j.substring(0,j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=")+1,j.length);
	}
	var returnValue = paraObj[paras.toLowerCase()];
	if(typeof(returnValue)=="undefined"){
		return "";
	}else{
		return returnValue;
	}
}
function setCookie(name,value) {//两个参数，一个是cookie的名子，一个是值
    var Days = 30; //此 cookie 将被保存 30 天
    var exp = new Date();    //new Date("December 31, 9998");
    exp.setTime(exp.getTime() + Days*24*60*60*1000);
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}

function getCookie(name) {//取cookies函数        
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
     if(arr != null) return unescape(arr[2]); return null;
}
//EasyUI DataGrid根据字段动态合并单元格
// param tableID 要合并table的id
//@param fldList 要合并的列,用逗号分隔(例如："name,department,office");
function MergeCells(tableID, fldList) {
    var Arr = fldList.split(",");
    var dg = $('#' + tableID);
    var fldName;
    var RowCount = dg.datagrid("getRows").length;
    var span;
    var PerValue = "";
    var CurValue = "";
    var length = Arr.length - 1;
    for (i = length; i >= 0; i--) {
        fldName = Arr[i];
        PerValue = "";
        span = 1;
        for (row = 0; row <= RowCount; row++) {
            if (row == RowCount) {
                CurValue = "";
            }
            else {
                CurValue = dg.datagrid("getRows")[row][fldName];
            }
            if (PerValue == CurValue) {
                span += 1;
            }
            else {
                var index = row - span;
                dg.datagrid('mergeCells', {
                    index: index,
                    field: fldName,
                    rowspan: span,
                    colspan: null
                });
                span = 1;
                PerValue = CurValue;
            }
        }
    }
}

 $.getFormData = function(form) {
        var o = {};
        $.each(form.serializeArray(), function(index) {
            if (o[this['name']]) {
                o[this['name']] = o[this['name']] + "," + this['value'];
            } else {
                o[this['name']] = this['value'];
            }
        });
        return o;
    };

/*
*js Unicode编码转换
*/ 
var decToHex = function(str) {
    var res=[];
    for(var i=0;i < str.length;i++)
        res[i]=("00"+str.charCodeAt(i).toString(16)).slice(-4);
    return "\\u"+res.join("\\u");
};
var hexToDec = function(str) {
    str=str.replace(/\\/g,"%");
    return unescape(str);
};
function getSessionId() {
    var c_name = 'JSESSIONID';
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(c_name + "=");
        if (c_start != -1) {
            c_start = c_start + c_name.length + 1;
            c_end = document.cookie.indexOf(";", c_start);
            if (c_end == -1)
                c_end = document.cookie.length;
            return unescape(document.cookie.substring(c_start, c_end));
        }
    }
}

function uploadImages(selector,url,view,imgUrl){
    var sessionId = getSessionId();
    $(selector).uploadify({
        'auto': true, // 是否自动上传   
        'multi': false, // 是否支持多文件上传    
        'buttonText':'上传图片', // 按钮上的文字  
        'fileSizeLimit':'2MB',
        'width':80,
        'height':25,
        'fileObjName':'fileUpload', // 传到后台的对象名    
        'fileTypeExts':'*.jpg;*.jpeg',
        'fileTypeDesc':'请选择图片文件,格式jpg、jpeg',
       // 'fileTypeExts':'*.jpg;*.jpeg;*.gif;*.png;*.bmp',
       // 'fileTypeDesc':'请选择图片文件,格式png、gif、jpg、jpeg、bmp',
        'swf': imgUrl,
        'uploader': url+';jsessionid='+sessionId, // 上传到后台的处理类    
        
//        formData    附带值，需要通过get or post传递的额外数据，需要结合onUploadStart事件一起使用
//        'formData'      : {'someKey' : 'someValue', 'someOtherKey' : 1},
//        'onUploadStart' : function(file) {
//            $("#fileUpload").uploadify("settings", "someOtherKey", 2);
//        }
        
        'onUploadSuccess' : function(file, data, response) {
            //alert(data);
//           $('#imgUrl').val(data);
            $(view).attr("src",data);
        },
        'overrideEvents': ['onSelectError','onDialogClose'],  
        'onSelectError': function (file, errorCode, errorMsg) {  
              switch (errorCode) {  
                  case -100:  
                      //$.messager.alert('操作提示', '上传的文件数量已经超出系统限制的'+$('#fileUpload').uploadify('settings', 'queueSizeLimit')+'个文件！', 'error');
                	  alert("'上传的文件数量已经超出系统限制的'+$('#fileUpload').uploadify('settings', 'queueSizeLimit')+'个文件！'")
                      break;  
                  case -110:  
                      //$.messager.alert('操作提示', '文件大小超出系统限制的'+$('#fileUpload').uploadify('settings', 'fileSizeLimit')+'大小！', 'error');
                	  alert('文件大小超出系统限制的2M大小！');
                      break;  
                  case -120:  
                      //$.messager.alert('操作提示', '文件大小异常！', 'error');
                	  alert("文件大小异常");
                      break;  
                  case -130:  
                     // $.messager.alert('操作提示', '文件类型不正确！', 'error');
                	  alert("文件类型不正确");
                      break;  
              }  
              return false;  
         }, 
         //检测FLASH失败调用   
         'onFallback': function () {  
//           $("#resultMessage").html('<span style="color:red">您未安装FLASH控件，无法上传！请安装FLASH控件后再试。<span>');
             $.messager.alert('操作提示', '您未安装FLASH控件，无法上传！请安装FLASH控件后再试。', 'error');
         } 
    
    });
}

/**
 *将豪秒转抱成指定的格式 
 *@millisecond: 豪秒
 *@format：格式化字符
 *  用法示意：dateFormat(1433232328000,"yyyy-MM-dd HH:mm:ss") 
 */
function dateFormat(millisecond, format){
      var t = new Date(millisecond);
      var tf = function(i){return (i < 10 ? '0' : '') + i;};
  return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
      switch(a){
          case 'yyyy':
              return tf(t.getFullYear());
              break;
          case 'MM':
              return tf(t.getMonth() + 1);
              break;
          case 'mm':
              return tf(t.getMinutes());
              break;
          case 'dd':
              return tf(t.getDate());
              break;
          case 'HH':
              return tf(t.getHours());
              break;
          case 'ss':
                return tf(t.getSeconds());
            break;
          }
      });
  };
  /** 
   * 使用方法: 
   * 开启:MaskUtil.mask(); 
   * 关闭:MaskUtil.unmask(); 
   *  
   * MaskUtil.mask('其它提示文字...'); 
   */
  var MaskUtil = (function () {

      var $mask, $maskMsg;

      var defMsg = '正在处理，请稍待。。。';

      function init() {
          if (!$mask) {
              $mask = $("<div class=\"datagrid-mask mymask\"></div>").appendTo("html");
          }
          if (!$maskMsg) {
              $maskMsg = $("<div class=\"datagrid-mask-msg mymask\">" + defMsg + "</div>")
                  .appendTo("body").css({ 'font-size': '12px' });
          }

          $mask.css({ width: "100%", height: $(document).height() });

          $maskMsg.css({
              left: ($(document.body).outerWidth(true) - 190) / 2, top: ($(window).height() - 45) / 2
          });

      }

      return {
          mask: function (msg) {
              if (msg) {
                  defMsg = msg;
              }
              init();
              $mask.show();
              $maskMsg.html(msg || defMsg).show();
          }
          , unmask: function () {
              $mask.hide();
              $maskMsg.hide();
          }
      }
  })
  
  //js 加法计算  
//调用：accAdd(arg1,arg2)  
//返回值：arg1加arg2的精确结果   
function accAdd(arg1,arg2){   
  var r1,r2,m;   
  try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}   
  try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}   
  m=Math.pow(10,Math.max(r1,r2))   
  return ((arg1*m+arg2*m)/m).toFixed(2);   
}   
//js 减法计算  
//调用：Subtr(arg1,arg2)  
//返回值：arg1减arg2的精确结果   
function Subtr(arg1,arg2){  
     var r1,r2,m,n;  
     try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}  
     try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}  
     m=Math.pow(10,Math.max(r1,r2));  
     //last modify by deeka  
     //动态控制精度长度  
     n=(r1>=r2)?r1:r2;  
     return ((arg1*m-arg2*m)/m).toFixed(2);  
}   
//js 除法函数  
//调用：accDiv(arg1,arg2)  
//返回值：arg1除以arg2的精确结果   
function accDiv(arg1,arg2){   
  var t1=0,t2=0,r1,r2;   
  try{t1=arg1.toString().split(".")[1].length}catch(e){}   
  try{t2=arg2.toString().split(".")[1].length}catch(e){}   
  with(Math){   
    r1=Number(arg1.toString().replace(".",""))   
    r2=Number(arg2.toString().replace(".",""))   
    return (r1/r2)*pow(10,t2-t1);   
  }   
}   
  
//js 乘法函数  
//调用：accMul(arg1,arg2)   
//返回值：arg1乘以arg2的精确结果   
function accMul(arg1,arg2)   
{   
  var m=0,s1=arg1.toString(),s2=arg2.toString();   
  try{m+=s1.split(".")[1].length}catch(e){}   
  try{m+=s2.split(".")[1].length}catch(e){}   
  return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)   
}   

//type==1 电话 
//type==2 身份证号
function enlarge(type,arg1,arg2){
	if(type==1){
		$(arg1).textbox('textbox').focus(function(e) {
			$(arg2).show();
			var telPhone='';
			var tel=$(arg1).textbox('getValue');
			for(var i=0; i<tel.length;i++) {
				if (i==3) {
					telPhone = telPhone + ' ' + tel.charAt(i);
				}
				else if(i==7){
					telPhone = telPhone + ' ' + tel.charAt(i);
				}
				else {
					telPhone = telPhone + tel.charAt(i);
				}
			}
			$(arg2).val(telPhone);
			
		});
		$(arg1).textbox('textbox').blur(function(e) {
			$(arg2).hide();
		});
		
		$(arg1).textbox('textbox').keyup(function(e) {
	        var txt = $(this).val();
			var len = txt.length;
			var content = '';
			for(var i=0; i<len;i++) {
				if (i==3) {
					content = content + ' ' + txt.charAt(i);
				}
				else if(i==7){
					content = content + ' ' + txt.charAt(i);
				}
				else {
					content = content + txt.charAt(i);
				}
			}
			$(arg2).val(content);
			
	    });
	}
	if(type==2){
		$(arg1).textbox('textbox').focus(function(e) {
			$(arg2).show();
			var card='';
			var cardlen=$(arg1).textbox('getValue');
			for(var i=0; i<cardlen.length;i++) {
				if (i==3) {
					card = card + ' ' + cardlen.charAt(i);
				}
				else if(i==6){
					card = card + ' ' + cardlen.charAt(i);
				}
				else if(i==14){
					card = card + ' ' + cardlen.charAt(i);
				}
				else {
					card = card + cardlen.charAt(i);
				}
			}
			$(arg2).val(card);
		});
		$(arg1).textbox('textbox').blur(function(e) {
			$(arg2).hide();
		});
		$(arg1).textbox('textbox').keyup(function(e) {
	        var txt = $(this).val();
			var len = txt.length;
			var content = '';
			for(var i=0; i<len;i++) {
				if (i==3) {
					content = content + ' ' + txt.charAt(i);
				}
				else if(i==6){
					content = content + ' ' + txt.charAt(i);
				}
				else if(i==14){
					content = content + ' ' + txt.charAt(i);
				}
				else {
					content = content + txt.charAt(i);
				}
			}
			$(arg2).val(content);
	    });
	}
	
}

/**
 * 用于处理js浮点类型的精度问题
 * @param f     浮点数
 * @param digit 精确到小数点第几位
 * @returns {number}
 */
function formatFloat(f, digit) {
    var m = Math.pow(10, digit);
    return parseInt(f * m, 10) / m;
}

var dataTableView = $.extend({},$.fn.datagrid.defaults.view,{
    onAfterRender:function(target){
        $.fn.datagrid.defaults.view.onAfterRender.call(this,target);
        var opts = $(target).datagrid('options');
        var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
        vc.children('div.datagrid-empty').remove();
        if (!$(target).datagrid('getRows').length){
            var d = $('<div class="datagrid-empty"></div>').html(opts.emptyMsg || 'no records').appendTo(vc);
            d.css({
                position:'absolute',
                left:0,
                top:50,
                width:'100%',
                textAlign:'center'
            });
        }
    }
});
	
function mask (){
	this.showMask = function (msg){
		 var defMsg = '正在处理，请稍待...';
		 if (msg) {
             defMsg = msg;
         }
		$ ("<div class=\"datagrid-mask\"></div>").css ({
		    display : "block",
		    width : "100%",
		    height : $ (document).height ()
		}).appendTo ("body");
		$ ("<div class=\"datagrid-mask-msg\"></div>").html (defMsg).appendTo ("body").css ({
		    display : "block",
		    left : ($ (document.body).outerWidth (true) - 190) / 2,
		    top : ($ (window).height () - 45) / 2
		});
	};
	this.hideMask = function (){
		$ ("body").find ("div.datagrid-mask").remove ();
		$ ("body").find ("div.datagrid-mask-msg").remove ();
	}
};

/**
 * 自动将form表单封装成json对象
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [ o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/**
 * 时间戳转日期
 */
function getYMDHMS(time){
	var date = new Date(time);
    var y = date.getFullYear();  
    var m = date.getMonth() + 1;  
    m = m < 10 ? ('0' + m) : m;  
    var d = date.getDate();  
    d = d < 10 ? ('0' + d) : d;  
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    var second = date.getSeconds();
    minute = minute < 10 ? ('0' + minute) : minute;  
    second = second < 10 ? ('0' + second) : second; 
    return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second; 
}
  
/**
 * 时间戳转日期
 */
function removeNullParams(dataModel){
	for (var key in dataModel) {
		if(dataModel[key] == null || dataModel[key] == ''){
			delete dataModel[key];
		}
	}
	return dataModel;
}