$(function(){
	var columns = [[
	             {field:'expenditureDate',title:'支出日期',width:'10%',align:'center',formatter:function(value,row,index){
	        		 return getYMDHMS(row.expenditureDate);
	        	 }}, 
	           	 {field:'plateNumber',title:'车牌号',width:'10%',align:'center'},    
	        	 {field:'ownerName',title:'司机姓名',width:'10%',align:'center'}, 
	        	 {field:'contactNumber',title:'联系电话',width:'10%',align:'center'},
	        	 {field:'itemName',title:'支出项目',width:'20%',align:'center'},
	        	 {field:'price',title:'支出金额',width:'10%',align:'center'},
	        	 {field:'transactorName',title:'经手人',width:'10%',align:'center'},
	        	 {field:'remarks',title:'备注',width:'10%',align:'center'},
	        	 {field:'opt',title:'操作',width:'10%',align:'center', formatter:function(value,row,index){
                 	return "<button class='btn btn-edit'>修改</button><button class='btn btn-del'>删除</button>";
                 }}
	            ]];
	var initDataGrid = function(){
		$("#dataGrid").datagrid({
			url : '/vehicleExpenditure/list',
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
	
	var operateVehicle = function(url, dataModel){
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
			$('#main_dlg').dialog('destroy');
			$.messager.alert('操作提示','操作成功');
			$("#dataGrid").datagrid('reload');
		}).fail(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			$.messager.alert('操作提示','操作失败');
		});
	} 
	
	var showAddDialog = function(){
		var html = Mustache.render($('#dialog_content_template').html(),{});
		var diaHtml = "<div id='main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '车辆日常费用管理',    
	 	    width: 400,    
	 	    height: 420,    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#main_dlg');
	 	    	loadVehicleGrid();
	 	    },
	 	    onClose:function(){
	 	    	$('#main_dlg').dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'保存',
				handler:function(){
					if($(".main-form-content").form ("validate")){
						operateVehicle('/vehicleExpenditure/add', $(".main-form-content").serializeObject());
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
		row.expenditureDate = getYMDHMS(row.expenditureDate);
		var html = Mustache.render($('#dialog_content_template').html(),row);
		var diaHtml = "<div id='main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '车辆日常费用管理('+row.ownerName+')',    
	 	    width: 400,    
	 	    height: 420,    
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
						operateVehicle('/vehicleExpenditure/edit', $(".main-form-content").serializeObject());
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
		$.messager.confirm("删除提示", "是否删除:"+row.ownerName+"的信息?",function(e){
		     if(e){
		    	 operateVehicle('/vehicleExpenditure/delete', {vehicleExpenditureId:row.id});
		     }
		 });
	}
	
	var initializeUI = function(){
		initDataGrid();
		$('.dataTable-toolbar').delegate('button.btn-add','click',showAddDialog);
		$('.main-query-content').delegate('button.btn-search','click',initDataGrid);
		$('.main-dataTable-content').delegate('button.btn-edit','click',showUpdateDialog);
		$('.main-dataTable-content').delegate('button.btn-del','click',showDeleteDialog);
	}
	initializeUI();
	
})