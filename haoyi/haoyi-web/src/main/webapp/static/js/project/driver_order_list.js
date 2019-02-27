$(function(){
	//结算状态：0-未结算；1-已结算
	var settleStatus = [{'text':'全部','value':' '},{'text':'未结算','value':'0'},{'text':'已结算','value':'1'}];
	var columns = [[
	             {field : 'id',width : '3%',align : 'center',checkbox:'true'},
	             {field:'orderDate',title:'订单日期',width:'10%',align:'center',formatter:function(value,row,index){
	        		 return getYMDHMS(row.orderDate);
	        	 }}, 
	           	 {field:'plateNumber',title:'车牌号',width:'5%',align:'center'},    
	        	 {field:'ownerName',title:'司机姓名',width:'5%',align:'center'}, 
	        	 {field:'contactNumber',title:'联系电话',width:'5%',align:'center'},
	        	 {field:'orderNO',title:'订单编号',width:'10%',align:'center'},
	           	 {field:'address',title:'订单简址',width:'5%',align:'center'},
	           	 {field:'cabinetModel',title:'柜型',width:'5%',align:'center'},
	        	 {field:'cabinetNumber',title:'柜号',width:'5%',align:'center'},
	        	 {field:'sealNumber',title:'封号',width:'5%',align:'center'},
	        	 {field:'driverPrice',title:'划价',width:'5%',align:'center'},
	        	 {field:'settlePrice',title:'实结金额',width:'5%',align:'center'},
	        	 {field:'otherAmt',title:'应结金额',width:'5%',align:'center'},
	        	 {field:'settleStatus',title:'结算状态',width:'5%',align:'center', formatter:function(value,row,index){
                 	return value == 1 ? '已结算' : '未结算';
                 }},
	        	 {field:'remarks',title:'备注',width:'5%',align:'center'},
	        	 {field:'opt',title:'操作',width:'20%',align:'center', formatter:function(value,row,index){
                 	return "<button class='btn btn-add-oa'>添加杂费</button><button class='btn btn-sel-oa'>查看杂费</button><button class='btn btn-edit'>修改</button>";
                 }}
	            ]];
	var initDataGrid = function(){
		loadDriverTotalAmt();
		$("#dataGrid").datagrid({
			url : '/driverOrder/list',
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
	 	    title: '司机接单管理',    
	 	    width: 400,    
	 	    height: 550,    
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
						operateVehicle('/driverOrder/add', $(".main-form-content").serializeObject());
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
		row.orderDate = getYMDHMS(row.orderDate);
		var html = Mustache.render($('#dialog_content_template').html(),row);
		var diaHtml = "<div id='main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '司机接单管理('+row.ownerName+')',    
	 	    width: 400,    
	 	    height: 550,    
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
						operateVehicle('/driverOrder/edit', $(".main-form-content").serializeObject());
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
		    	 operateVehicle('/driverOrder/delete', {driverOrderId:row.id});
		     }
		 });
	}
	
	var initSettleStatus = function(){
		$('#settleStatus_query').combobox({
			data:settleStatus,
			editable:false
		});
	}
	
	var loadDriverTotalAmt = function(){
		$.ajax({
			url:'/driverOrder/totalAmt',
			type:"get",
			data:$(".main-query-content form").serializeObject(),
			dataType:'json'
		}).done(function(data){
			$('.dataTable-toolbar .totalAmt').html(data);
		}).fail(function(data){
			$.messager.alert('操作提示','获取合计总金额失败');
		});
	} 
	
	var addOtherAmt = function(url, dataModel){
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
	
	var showOtherAmtDialog = function(){
		var row = $('#dataGrid').datagrid('getSelected');
		var dataModel = {};
		dataModel.orderId = row.id;
		dataModel.orderNO = row.orderNO;
		dataModel.address = row.address;
		dataModel.cabinetModel = row.cabinetModel;
		dataModel.cabinetNumber = row.cabinetNumber;
		dataModel.sealNumber = row.sealNumber;
		var html = Mustache.render($('#otherAmt_dialog_content_template').html(),dataModel);
		var diaHtml = "<div id='main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '订单杂费管理('+row.orderNO+')',    
	 	    width: 400,    
	 	    height: 500,    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#main_dlg');
	 	    	initDataDic();
	 	    },
	 	    onClose:function(){
	 	    	$('#main_dlg').dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'保存',
				handler:function(){
					if($(".main-form-content").form ("validate")){
						addOtherAmt('/orderOtherAmt/add', $(".main-form-content").serializeObject());
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
	
	
	var itemColumns = [[
		   	             {field:'expenditureDate',title:'支出日期',width:'20%',align:'center',formatter:function(value,row,index){
		   	            	 return getYMDHMS(row.expenditureDate);
			        	 }}, 
			        	 {field:'orderNO',title:'订单编号',width:'15%',align:'center'},
			        	 {field:'cabinetNumber',title:'柜号',width:'15%',align:'center'},
			        	 {field:'address',title:'订单简址',width:'5%',align:'center'},
			        	 {field:'itemName',title:'支付项目类型',width:'10%',align:'center'},
			        	 {field:'price',title:'支付金额',width:'10%',align:'center'},
			        	 {field:'targetName',title:'支出对象',width:'10%',align:'center'},
			        	 {field:'propertyType',title:'归属类型',width:'5%',align:'center', formatter:function(value,row,index){
			        		 return value == 1 ? '司机' : value == 2 ? '客户' : '自己';
		                 }},
		                 {field:'isSettle',title:'结算状态',width:'5%',align:'center', formatter:function(value,row,index){
		                  	return value == 0 ? '已结算' :'未结算';
		                  }},
			        	 {field:'remarks',title:'备注',width:'5%',align:'center'}
			            ]];
		var initDialogDataGrid = function(dataModel){
			$("#dialogDataGrid").datagrid({
				url : '/orderOtherAmt/list',
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
		
	var showOtherAmtItemDialog = function(){
		var row = $('#dataGrid').datagrid('getSelected');
		var diaHtml = "<div id='main_dlg' class='main-dialog-view-content'><table id='dialogDataGrid' class='easyui-datagrid'></table></div>";
		$(diaHtml).dialog({    
	 	    title: '杂费项('+row.orderNO+')',    
	 	    width: 850,    
	 	    height: 500,    
	 	    closed: false,   
	 	    resizable:true,
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#main_dlg');
	 	    	initDialogDataGrid({'orderId':row.orderId,'propertyType':1});
	 	    },
	 	    onClose:function(){
	 	    	$('#main_dlg').dialog('destroy');
	 	   }
	     }); 
	}
	
	var batchSettled = function(url, dataModel){
		var maskObj = new mask();
		$.ajax({
			url:url,
			type:"post",
			data:dataModel,
			dataType:'json',
			traditional: true,
			beforeSend : function (){
			    maskObj.showMask();// 显示遮蔽罩
		    }
		}).done(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			$.messager.alert('操作提示','操作成功');
			$("#dataGrid").datagrid('reload');
		}).fail(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			$.messager.alert('操作提示','操作失败');
		});
	} 
	
	var showSettledDialog = function(){
		var row = $('#dataGrid').datagrid('getChecked');
		var ids = [];
		$.each(row,function(i,v){
			if(v.settleStatus == 0){
				ids[ids.length] = v.id;
			}
			
		});
		if(ids.length == 0){
			$.messager.alert('操作提示','请选择未结算的信息');
			return false;
		}
		$.messager.confirm("结算提示", "是否批量结算该批信息?",function(e){
		     if(e){
		    	 batchSettled('/driverOrder/batchSettles', {driverOrderIds:ids});
		     }
		 });
	}
	
	var downExcel = function(){
		var criteria = $(".main-query-content form").serializeObject();
		buildExportFormSubmit("/driverOrder/loadExcel.do", criteria);
	}
	
	var initializeUI = function(){
		initDataGrid();
		initSettleStatus();
		$('.dataTable-toolbar').delegate('button.btn-batch-settled','click',showSettledDialog);
		$('.dataTable-toolbar').delegate('button.btn-add','click',showAddDialog);
		$('.dataTable-toolbar').delegate('button.btn-excel','click',downExcel);
		$('.main-query-content').delegate('button.btn-search','click',initDataGrid);
		$('.main-dataTable-content').delegate('button.btn-edit','click',showUpdateDialog);
		$('.main-dataTable-content').delegate('button.btn-del','click',showDeleteDialog);
		$('.main-dataTable-content').delegate('button.btn-add-oa','click',showOtherAmtDialog);
		$('.main-dataTable-content').delegate('button.btn-sel-oa','click',showOtherAmtItemDialog);
	}
	initializeUI();
	
})