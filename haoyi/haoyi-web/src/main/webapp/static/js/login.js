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
	$(window).resize(function() {
		resizeBG();
	});
	var initializeUi = function(){
		$('div.main_container').delegate('input.ui_textinput','focus',hiddenLabel);
		$('div.main_container').delegate('input.ui_textinput','blur',showLabel);
	}
	resizeBG();
	initializeUi();
	
})