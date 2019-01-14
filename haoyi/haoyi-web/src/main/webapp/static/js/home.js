var gVersion="1.0.0";
$(function() {
  init();
  menuInit();//菜单数据初始化
});

function init() {
	$.ajax({
		url:'/auth/userInfo.do',
		type:"post"
	}).done(function(data){
		$("#userName").text(data.userName);
	}).fail(function(data){
		$.messager.alert('操作提示','获取登录信息失败');
	});
}

function tabOpenEven(){
  $('.addTab').on("click","a",function() {
      $('.addTab li').removeClass("click");
      $(this).closest("li").toggleClass("click");
			var $this = $(this);
			var href = $this.attr('src');
			var title = $this.text();
      if(href){
        addTab(title, href);
      }
  });
}

function menuInit() {
	 var mainURL = 'http://chenyi.tsh365.cn';
	  $("#menu").html('<div class="loading"><p class="bg-warning " style="padding:10px;color:#fff ;text-align:center">菜单加载中...</p></div>');
	$.ajax({
		url:'/userResources/findUserAuthority.do',
		type:"post"
	}).done(function(data){
		menuRender(data);
	}).fail(function(data){
		$.messager.alert('操作提示','获取菜单权限失败');
	});
}

function menuRender(data) {
  var content = '';
  var m=1;
  content += '<div class="sysMenu"><ul>';
	  $.each(data,function(i,v){
		  if(v.level && v.level == 1){
		    var icon='<i class="i-menu i-menu-'+m+'"></i>';
		    m++;
		    content += '<li><div class="link">'+icon+v.menuName+'<i class="i-arrow-down"></i></div>';
		    content+='<ul>';
		    $.each(data,function(j,k){
				if(k.parentId==v.id){
					content+= '<li id="'+k.funCode+'"><a href="javascript:void(0);"  src="'+k.menuPath+'" >'+k.menuName+'</a></li>';
				}
		      });
		      content+='</ul>';
		    
		    content+='</li>';
		  }
	  });
	  content+= '</ul></div>';
	  $("#menu").html(content);
	  menuAnimate();//菜单动画
	  tabOpenEven();//打开菜单事件
	  tabCloseEven();//关闭菜单事件
}


function menuAnimate(){
	 var Accordion = function(el, multiple) {
			this.el = el || {};
			this.multiple = multiple || false;
			// Variables privadas
			var links = this.el.find('.link');
			// Evento
			links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown);
		};

		Accordion.prototype.dropdown = function(e) {
			var $el = e.data.el;
				$this = $(this),
				$next = $this.next();

			$next.slideToggle();
			$this.parent().toggleClass('open');

			if (!e.data.multiple) {
				$el.find('.submenu').not($next).slideUp().parent().removeClass('open');
			};
		};
		var accordion = new Accordion($('#menu'), false);
}

function addTab(title, url){
	if ($('#tabs').tabs('exists', title)){
		$('#tabs').tabs('select', title);//选中并刷新
		var currTab = $('#tabs').tabs('getSelected');
		//var url = $(currTab.panel('options').content).attr('src');
		if(url != undefined && currTab.panel('options').title != 'Home') {
			$('#tabs').tabs('update',{
				tab:currTab,
				options:{
					content:createFrame(url)
				}
			});
		}
	} else {
		var content = createFrame(url);
		$('#tabs').tabs('add',{
			title:title,
			content:content,
			closable:true
		});
	}
	tabClose();
}
function createFrame(url) {
	var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;overflow-y : hidden;   "></iframe>';
	return s;
}


/**
	 * 刷新tab
	 * @cfg
	 *example: {tabTitle:'tabTitle',url:'refreshUrl'}
	 *如果tabTitle为空，则默认刷新当前选中的tab
	 *如果url为空，则默认以原来的url进行reload
	 */
	function refreshTab(cfg){
	    var refresh_tab = cfg.tabTitle?$('#tabs').tabs('getTab',cfg.tabTitle):$('#tabs').tabs('getSelected');
	    if(refresh_tab && refresh_tab.find('iframe').length > 0){
	    var _refresh_ifram = refresh_tab.find('iframe')[0];
	    var refresh_url = cfg.url?cfg.url:_refresh_ifram.src;
	    //_refresh_ifram.src = refresh_url;
	    _refresh_ifram.contentWindow.location.href=refresh_url;
	    }
	}
function tabClose() {
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close',subtitle);
	});
	/*为选项卡绑定右键*/
	$(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});

		var subtitle =$(this).children(".tabs-closable").text();

		$('#mm').data("currtab",subtitle);
		$('#tabs').tabs('select',subtitle);
		return false;
	});
}
//绑定右键菜单事件
function tabCloseEven() {
	//刷新
	$('#mm-tabupdate').click(function(){
		var currTab = $('#tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		if(url != undefined && currTab.panel('options').title != '首页') {
			$('#tabs').tabs('update',{
				tab:currTab,
				options:{
					content:createFrame(url)
				}
			});
		}
	});
	//关闭当前
	$('#mm-tabclose').click(function(){
		var currtab_title = $('#mm').data("currtab");
		$('#tabs').tabs('close',currtab_title);
	});
	//全部关闭
	$('#mm-tabcloseall').click(function(){
		$('.tabs-inner span').each(function(i,n){
			var t = $(n).text();
			if(t != '首页') {
				$('#tabs').tabs('close',t);
			}
		});
	});
	//关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		var nextall = $('.tabs-selected').nextAll();
		if(prevall.length>0){
			prevall.each(function(i,n){
				var t=$('a:eq(0) span',$(n)).text();
				if(t != '首页') {
					$('#tabs').tabs('close',t);
				}
			});
		}
		if(nextall.length>0) {
			nextall.each(function(i,n){
				var t=$('a:eq(0) span',$(n)).text();
				if(t != '首页') {
					$('#tabs').tabs('close',t);
				}
			});
		}
		return false;
	});
	//关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function(){
		var nextall = $('.tabs-selected').nextAll();
		if(nextall.length==0){
			//msgShow('系统提示','后边没有啦~~','error');
			alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});
	//关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		if(prevall.length==0){
			alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});

	//退出
	$("#mm-exit").click(function(){
		$('#mm').menu('hide');
	});
	

}
