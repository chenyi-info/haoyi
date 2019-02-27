$(function(){
	var columns = [[
	           	 {field:'fileName',title:'文件名',width:'30%',align:'center'},    
	        	 {field:'filePath',title:'文件路径',width:'30%',align:'center'}, 
	        	 {field:'fileSize',title:'文件大小(kb)',width:'10%',align:'center'},
	        	 {field:'fileTime',title:'创建时间',width:'20%',align:'center',formatter:function(value,row,index){
	        		 return getYMDHMS(row.fileTime);
	        	 }},
	        	 {field:'opt',title:'操作',width:'10%',align:'center', formatter:function(value,row,index){
                 	return "<button class='btn btn-down'>下载</button>";
                 }}
	            ]];
	var initDataGrid = function(){
		$("#dataGrid").datagrid({
			url : '/mysql/list',
			queryParams : $(".main-query-content form").serializeObject(),
			singleSelect: true, //是否单选
			striped:true,//各行变色
			pagination: true, //分页控件
			pageNumber: 1,
			pageSize: 50,
			pageList: [50, 100, 150],
			autoRowHeight: true,
			fit: true,
			fitColumns: true, //设置是否滚动条
			nowrap: false,
			remotesort: true,
			checkOnSelect: false,
			selectOnCheck: false,
			method: "GET", //请求数据的方法
			loadMsg: '数据加载中,请稍候......',
			idField: 'id',
			pagePosition: "bottom",
			view:dataTableView,
			emptyMsg:'未查询到内容',
            columns:columns
        });
	}
	
	var backUp = function(){
		$.messager.confirm("备份提示", "是否备份数据库信息?",function(e){
		     if(e){
		    	var maskObj = new mask();
		 		$.ajax({
		 			url:'/mysql/backup',
		 			type:"post",
		 			dataType:'json',
		 			beforeSend : function (){
		 			    maskObj.showMask();// 显示遮蔽罩
		 		    }
		 		}).done(function(data){
		 			maskObj.hideMask ();// 隐藏遮蔽罩
		 			$.messager.alert('操作提示','操作成功');
		 		}).fail(function(data){
		 			maskObj.hideMask ();// 隐藏遮蔽罩
		 			$.messager.alert('操作提示','操作失败');
		 		});
		     }
		 });
	}
	
	var downLoad = function(){
		var row = $('#dataGrid').datagrid('getSelected');
		var fileName = row.fileName;
		buildExportFormSubmit("/mysql/download", {"fileName":fileName});
	}
	var initializeUI = function(){
		initDataGrid();
		$('.dataTable-toolbar').delegate('button.btn-backup','click', backUp);
		$('.main-dataTable-content').delegate('button.btn-down','click', downLoad);
	}
	initializeUI();
	
})