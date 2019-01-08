$(function(){
	var hiddenLabel = function(){
		$(this).parents('.form_item').find('label').css('display','none');
	}
	var showLabel = function(){
		if($.trim($(this).val()) === '')
			$(this).parents('.form_item').find('label').css('display','');
	}
	var resizeBG = function(){
		$('div.otw_background').css({'width':$(window).width(),'height':$(window).height()});
		$('.otw_background_img').css({'width':$(window).width(),'height':$(window).height()});
	}
	var loginAuth = function(){
		var maskObj = new mask();
		var dataModel = {};
		dataModel.userAccount = $('input[name=userAccount]').val();
		dataModel.userPassWord = $('input[name=userPassWord]').val();
		$.ajax({
			url:'/auth/login',
			type:"post",
			data:dataModel,
			dataType:'json',
			beforeSend : function (){
			    maskObj.showMask();// 显示遮蔽罩
		    }
		}).done(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			if(data.status == 200){
				window.location.href = '/view/home.html'
			}else{
				$.messager.alert('操作提示',data.msg);
			}
		}).fail(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			$.messager.alert('操作提示','操作失败');
		});
	}
	var initializeUi = function(){
		$('div.main_container').delegate('input.ui_textinput','focus',hiddenLabel);
		$('div.main_container').delegate('input.ui_textinput','blur',showLabel);
		$('div.main_container').delegate('button.ui_login','click',loginAuth);
	}
	resizeBG();
	initializeUi();
	$(window).resize(function() {
		resizeBG();
	});
})