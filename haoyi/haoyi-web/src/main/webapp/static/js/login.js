$(function(){

	var loginAuth = function(){
		var maskObj = new mask();
		var dataModel = {};
		dataModel.userAccount = $('input[name=userAccount]').val();
		dataModel.userPassWord = $('input[name=userPassWord]').val();
		if(dataModel.userAccount == ''){
			$('input[name=userAccount]').css('border-bottom','1px solid red');
			return false;
		}
		if(dataModel.userPassWord == ''){
			$('input[name=userPassWord]').css('border-bottom','1px solid red');
			return false;
		}
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
	
	var setInfo = function(){
		$(this).css('border-bottom','1px solid #484856');
	}
	var chekLoginAuth = function(event){
		if(event.keyCode == "13") {
			loginAuth();
	    }
	}
	var initializeUi = function(){
		$('div.login-form').delegate('input[name=userAccount]', 'focus', setInfo);
		$('div.login-form').delegate('input[name=userPassWord]', 'focus', setInfo);
		$('input[name=userPassWord]')
		$('div.login-form').delegate('input[name=userAccount]', 'keydown', chekLoginAuth);
		$('div.login-form').delegate('input[name=userPassWord]', 'keydown', chekLoginAuth);
		$('div.signin').delegate('#login','click', loginAuth);
	}
	initializeUi();
})