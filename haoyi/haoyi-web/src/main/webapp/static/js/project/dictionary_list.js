$(function(){
	var columns = [[
	           	 {field:'name',title:'字典名称',width:'20%',align:'center'},    
	        	 {field:'code',title:'字典编码',width:'20%',align:'center'}, 
	        	 {field:'remarks',title:'备注',width:'15%',align:'center'},
	        	 {field:'createDate',title:'创建时间',width:'15%',align:'center',formatter:function(value,row,index){
	        		 return getYMDHMS(row.createDate);
	        	 }},
	        	 {field:'opt',title:'操作',width:'30%',align:'center', formatter:function(value,row,index){
                 	return "<button class='btn btn-dictionaryItem'>字典项</button><button class='btn btn-addItem'>新增项</button><button class='btn btn-edit'>修改</button><button class='btn btn-del'>删除</button>";
                 }}
	            ]];
	var itemColumns = [[
		           	 {field:'itemName',title:'字典项名称',width:'30%',align:'center'},    
		           	 {field:'remarks',title:'备注',width:'20%',align:'center'},
		        	 {field:'createDate',title:'创建时间',width:'20%',align:'center',formatter:function(value,row,index){
		        		 return getYMDHMS(row.createDate);
		        	 }},
		        	 {field:'opt',title:'操作',width:'30%',align:'center', formatter:function(value,row,index){
	                 	return "<button class='btn btn-edit'>修改</button><button class='btn btn-del'>删除</button>";
	                 }}
		            ]];
	var initDataGrid = function(){
		$("#dataGrid").datagrid({
			url : '/dictionary/list',
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
	
	var operateInfo = function(gridId,dialogId, url, dataModel){
		var maskObj = new mask();
		$.ajax({
			url:url,
			type:"post",
			data:dataModel,
			dataType:'json',
			beforeSend : function (){
			    maskObj.showMask();// 显示遮蔽罩
		    }
		}).done(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			if(data.status == 200){
				$('#'+dialogId).dialog('destroy');
				$("#"+gridId).datagrid('reload');
			}
			$.messager.alert('操作提示', data.msg);
			
		}).fail(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			$.messager.alert('操作提示','操作失败');
		});
	} 
	
	var showAddDialog = function(){
		var html = Mustache.render($('#dialog_content_template').html(),{});
		var diaHtml = "<div id='main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '新增字典',    
	 	    width: 400,    
	 	    height: 300,    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#main_dlg');
	 	    },
	 	    onClose:function(){
	 	    	$('#main_dlg').dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'保存',
				handler:function(){
					if($(".main-form-content").form ("validate")){
						operateInfo('dataGrid','main_dlg','/dictionary/add', $(".main-form-content").serializeObject());
					}
				}
			},{
				text:'关闭',
				handler:function(){
					$('#main_dlg').dialog('destroy');
				}
			}]
	     }); 
	}
	var showUpdateDialog = function(){
		var row = $('#dataGrid').datagrid('getSelected');
		var html = Mustache.render($('#dialog_content_template').html(),row);
		var diaHtml = "<div id='main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '修改字典('+row.name+')',    
	 	    width: 400,    
	 	    height: 300,    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#main_dlg');
	 	    },
	 	    onClose:function(){
	 	    	$('#main_dlg').dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'修改',
				handler:function(){
					if($(".main-form-content").form ("validate")){
						operateInfo('dataGrid','main_dlg','/dictionary/edit', $(".main-form-content").serializeObject());
					}
				}
			},{
				text:'关闭',
				handler:function(){
					$('#main_dlg').dialog('destroy');
				}
			}]
	     }); 
	}
	
	var showDeleteDialog = function(){
		var row = $('#dataGrid').datagrid('getSelected');
		$.messager.confirm("删除提示", "是否删除字典:"+row.name+"的信息?",function(e){
		     if(e){
		    	 operateInfo('dataGrid','','/dictionary/delete', {dictionaryId:row.id});
		     }
		 });
	}
	
	
	var initDialogDataGrid = function(dataModel){
		$("#dialogDataGrid").datagrid({
			url : '/dictionary/itemList',
			queryParams : dataModel,
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
            columns:itemColumns
        });
	}
	
	var showItemDialog = function(){
		var row = $('#dataGrid').datagrid('getSelected');
		var diaHtml = "<div id='main_dlg' class='main-dialog-view-content'><table id='dialogDataGrid' class='easyui-datagrid'></table></div>";
		$(diaHtml).dialog({    
	 	    title: '字典项('+row.name+')',    
	 	    width: 800,    
	 	    height: 500,    
	 	    closed: false,   
	 	    resizable:true,
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#main_dlg');
	 	    	initDialogDataGrid({'dictionaryCode':row.code});
	 	    	$('.main-dialog-view-content').delegate('button.btn-edit','click',updateItemDialog);
	 			$('.main-dialog-view-content').delegate('button.btn-del','click',showDeleteItemDialog);
	 	    },
	 	    onClose:function(){
	 	    	$('#main_dlg').dialog('destroy');
	 	   }
	     }); 
	}
	
	var showDeleteItemDialog = function(){
		var row = $('#dialogDataGrid').datagrid('getSelected');
		$.messager.confirm("删除提示", "是否删除字典项:"+row.itemName+"的信息?",function(e){
		     if(e){
		    	 operateInfo('dialogDataGrid','','/dictionary/deleteItem', {dictionaryItemId:row.id});
		     }
		 });
	}
	
	var updateItemDialog = function(){
		var dicRow = $('#dataGrid').datagrid('getSelected');
		var row = $('#dialogDataGrid').datagrid('getSelected');
		row.name = dicRow.name;
		var html = Mustache.render($('#item_dialog_content_template').html(), row);
		var diaHtml = "<div id='item_main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '修改字典项('+row.itemName+')',    
	 	    width: 400,    
	 	    height: 300,    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#item_main_dlg');
	 	    },
	 	    onClose:function(){
	 	    	$('#item_main_dlg').dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'保存',
				handler:function(){
					if($(".main-form-content").form ("validate")){
						operateInfo('dialogDataGrid','item_main_dlg','/dictionary/editItem', $(".main-form-content").serializeObject());
					}
				}
			},{
				text:'关闭',
				handler:function(){
					$('#item_main_dlg').dialog('destroy');
				}
			}]
	     }); 
	}
	
	var addItemDialog = function(){
		var row = $('#dataGrid').datagrid('getSelected');
		var dataModel = {};
		dataModel.dictionaryCode = row.code;
		dataModel.name = row.name;
		var html = Mustache.render($('#item_dialog_content_template').html(), dataModel);
		var diaHtml = "<div id='item_main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '新增字典项('+row.name+')',    
	 	    width: 400,    
	 	    height: 300,    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#item_main_dlg');
	 	    },
	 	    onClose:function(){
	 	    	$('#item_main_dlg').dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'保存',
				handler:function(){
					if($(".main-form-content").form ("validate")){
						operateInfo('dataGrid','item_main_dlg','/dictionary/addItem', $(".main-form-content").serializeObject());
					}
				}
			},{
				text:'关闭',
				handler:function(){
					$('#item_main_dlg').dialog('destroy');
				}
			}]
	     }); 
	}
	var initializeUI = function(){
		initDataGrid();
		$('.dataTable-toolbar').delegate('button.btn-add','click',showAddDialog);
		$('.main-query-content').delegate('button.btn-search','click',initDataGrid);
		$('.main-dataTable-content').delegate('button.btn-edit','click',showUpdateDialog);
		$('.main-dataTable-content').delegate('button.btn-del','click',showDeleteDialog);
		$('.main-dataTable-content').delegate('button.btn-dictionaryItem','click',showItemDialog);
		$('.main-dataTable-content').delegate('button.btn-addItem','click',addItemDialog);
	}
	initializeUI();
	
})