$(function(){
	//结算状态：0-未结算；1-已结算
	var settleStatus = [{'text':'全部','value':' '},{'text':'未结算','value':'0'},{'text':'已结算','value':'1'},{'text':'锁定','value':'2'}];
	var columns = [[
	             {field : 'id',width : '3%',align : 'center',checkbox:'true'},
	             {field:'orderDate',title:'订单日期',width:'6%',align:'center',sortable :true,formatter:function(value,row,index){
	        		 return getYMDHMS(row.orderDate);
	        	 }}, 
	           	 {field:'companyName',title:'公司名称',width:'8%',sortable :true,align:'center'},  
	           	 {field:'orderNO',title:'订单编号',width:'10%',sortable :true,align:'center'},
	           	 {field:'address',title:'订单简址',width:'5%',sortable :true,align:'center'},
	           	 {field:'cabinetModel',title:'柜型',width:'5%',sortable :true,align:'center'},
	        	 {field:'cabinetNumber',title:'柜号',width:'10%',sortable :true,align:'center'},
	        	 {field:'sealNumber',title:'封号',width:'5%',sortable :true,align:'center'},
	        	 {field:'customerPrice',title:'订单金额',width:'5%',sortable :true,align:'center'},
	        	 {field:'settlePrice',title:'实结金额',width:'5%',sortable :true,align:'center'},
	        	 {field:'otherAmt',title:'应结金额',width:'5%',sortable :true,align:'center'},
	        	 {field:'settleStatus',title:'结算状态',width:'5%',align:'center', sortable :true,formatter:function(value,row,index){
                 	return value == 1 ? '已结算' : value == 0 ? '未结算' : '<label style="color:red;">锁定</label>';
                 }},
                 {field:'settleDate',title:'结算日期',width:'5%',align:'center', sortable :true,formatter:function(value,row,index){
                	 return getYMDHMS(row.settleDate);
                 }},
	        	 {field:'remarks',title:'备注',width:'5%',align:'center'},
	        	 {field:'updateDate',title:'修改日期',width:'5%',align:'center', sortable :true,formatter:function(value,row,index){
                	 return getYMDHMS(row.updateDate);
                 }},
	        	 {field:'opt',title:'操作',width:'15%',align:'center', formatter:function(value,row,index){
                 	return "<button class='btn btn-add-oa'>添加杂费</button><button class='btn btn-sel-oa'>查看杂费</button><button class='btn btn-edit'>修改</button>";
                 }}
	            ]];
	var initDataGrid = function(){
		loadCustomerTotalAmt();
		var dataModel = $(".main-query-content form").serializeObject();
		if(dataModel.orderDateEnd){
			dataModel.orderDateEnd = dataModel.orderDateEnd+" 23:59:59";
		}
		$("#dataGrid").datagrid({
			url : '/customerOrder/list',
			queryParams : dataModel,
			singleSelect: true, //是否单选
			striped:true,//各行变色
			pagination: true, //分页控件
			rownumbers:true,
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
            columns:columns,
            sortName:'orderDate',
            sortOrder:'desc',
            onLoadSuccess:function(data){
	         	var total = 0;
	         	$('.main-dataTable-content input[name=id]:checked').each(function(i,v){
	         		$(v).attr('add','add'); 
	         		var price = $(v).parents('tr').find('td[field=customerPrice]').text();
	         		var otherAmt = $(v).parents('tr').find('td[field=otherAmt]').text();
	         		var settlePrice = $(v).parents('tr').find('td[field=settlePrice]').text();
	         		if(isNaN(price) || price == ''){
	         			price = 0;
	         		}
	         		if(isNaN(otherAmt) || otherAmt == ''){
	         			otherAmt = 0;
	         		}
	         		if(isNaN(settlePrice) || settlePrice == ''){
	         			settlePrice = 0;
	         		}
	         		total += (parseInt(price)+parseInt(otherAmt)-parseInt(settlePrice));
	         	});
	         	$('.dataTable-toolbar .selTotalAmt').html(total);
	         },onCheckAll:function(rows){
	         	var total = 0;
	         	$.each(rows,function(i,v){
	         		total += (v.customerPrice + v.otherAmt - v.settlePrice);
	         		$('.main-dataTable-content input[name=id]').eq(i).attr('add','add'); 
	         	});
	         	$('.dataTable-toolbar .selTotalAmt').html(total);
	         },onUncheckAll:function(rows){
	        	 $('.dataTable-toolbar .selTotalAmt').html('0');
	        	 $('.main-dataTable-content input[name=id]').removeAttr('add');
	         },onCheck : function (index, row) {
	         	if($('.main-dataTable-content input[name=id]').eq(index).attr('add') == 'add'){
	         		return false;
	         	}
	         	var total = parseInt($('.dataTable-toolbar .selTotalAmt').html());
	         	$('.dataTable-toolbar .selTotalAmt').html(total + (row.customerPrice + row.otherAmt - row.settlePrice));
	         	$('.main-dataTable-content input[name=id]').eq(index).attr('add','add'); 
	         },onUncheck : function (index, row) {
	         	if($('.main-dataTable-content input[name=id]').eq(index).attr('add') != 'add'){
	         		return false;
	         	}
	         	var total = parseInt($('.dataTable-toolbar .selTotalAmt').html());
	         	$('.dataTable-toolbar .selTotalAmt').html(total - (row.customerPrice + row.otherAmt - row.settlePrice));
	         	$('.main-dataTable-content input[name=id]').eq(index).removeAttr('add');
	         }
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
	 	    title: '客户账单管理',    
	 	    width: 400,    
	 	    height: 500,    
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
						operateVehicle('/customerOrder/add', $(".main-form-content").serializeObject());
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
	 	    title: '客户账单管理('+row.orderNO+')',    
	 	    width: 400,    
	 	    height: 550,    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$('.main-form-content input[name=settleStatus][value='+row.settleStatus+']').attr("checked",true);
	 	    	$.parser.parse('#main_dlg');
	 	    },
	 	    onClose:function(){
	 	    	$('#main_dlg').dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'修改',
				handler:function(){
					if(row.settleStatus == 2){
						$.messager.alert('操作提示','锁定订单无法操作');
						return;
					}
					if($(".main-form-content").form ("validate")){
						operateVehicle('/customerOrder/edit', $(".main-form-content").serializeObject());
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
		$.messager.confirm("删除提示", "是否删除:"+row.orderNO+"的信息?",function(e){
		     if(e){
		    	 operateVehicle('/customerOrder/delete', {customerOrderId:row.id});
		     }
		 });
	}
	var initSettleStatus = function(){
		$('#settleStatus_query').combobox({
			data:settleStatus,
			editable:false
		});
	}
	
	var loadCustomerTotalAmt = function(){
		var dataModel = $(".main-query-content form").serializeObject();
		if(dataModel.orderDateEnd){
			dataModel.orderDateEnd = dataModel.orderDateEnd+" 23:59:59";
		}
		$.ajax({
			url:'/customerOrder/totalAmt',
			type:"get",
			data: dataModel,
			dataType:'json'
		}).done(function(data){
			$('.dataTable-toolbar .totalAmt').html(data);
		}).fail(function(data){
			$.messager.alert('操作提示','获取合计总金额失败');
		});
	} 
	

	var addOtherAmt = function(url, dataModel){
		var maskObj = new mask();
		dataModel.propertyTypes=dataModel.propertyType;
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
		if(row.settleStatus == 2){
			$.messager.alert('操作提示','锁定订单无法操作');
			return;
		}
		var dataModel = {};
		dataModel.orderId = row.id;
		dataModel.orderNO = row.orderNO;
		dataModel.address = row.address;
		dataModel.cabinetModel = row.cabinetModel;
		dataModel.cabinetNumber = row.cabinetNumber;
		dataModel.sealNumber = row.sealNumber;
		dataModel.expenditureDate = dateFormat(row.orderDate,"yyyy-MM-dd HH:mm:ss");
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
	 	    	initDialogDataGrid({'orderId':row.orderId,'propertyType':2});
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
			if(v.settleStatus == 0 || v.settleStatus == 2){
				ids[ids.length] = v.id;
			}
			
		});
		if(ids.length == 0){
			$.messager.alert('操作提示','请选择未结算或锁单的信息');
			return false;
		}
		$.messager.confirm("结算提示", "是否批量结算该批信息?",function(e){
		     if(e){
		    	 batchSettled('/customerOrder/batchSettles', {customerOrderIds:ids});
		     }
		 });
	}
	
	var showLockDialog = function(){
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
		$.messager.confirm("结算提示", "是否批量锁定该批信息?",function(e){
		     if(e){
		    	 batchSettled('/customerOrder/batchLock', {customerOrderIds:ids});
		     }
		 });
	}
	
	var showUNLockDialog = function(){
		var row = $('#dataGrid').datagrid('getChecked');
		var ids = [];
		$.each(row,function(i,v){
			if(v.settleStatus == 2){
				ids[ids.length] = v.id;
			}
			
		});
		if(ids.length == 0){
			$.messager.alert('操作提示','请选择锁单的信息');
			return false;
		}
		$.messager.confirm("结算提示", "是否批量解锁该批信息?",function(e){
		     if(e){
		    	 batchSettled('/customerOrder/batchUNLock', {customerOrderIds:ids});
		     }
		 });
	}
	
	var downExcel = function(){
		var dataModel = $(".main-query-content form").serializeObject();
		if(dataModel.orderDateEnd){
			dataModel.orderDateEnd = dataModel.orderDateEnd+" 23:59:59";
		}
		buildExportFormSubmit("/customerOrder/loadExcel.do", dataModel);
	}
	
	var initializeUI = function(){
		initSettleStatus();
		initDataGrid();
		$('.dataTable-toolbar').delegate('button.btn-batch-lock','click',showLockDialog);
		$('.dataTable-toolbar').delegate('button.btn-batch-unlock','click',showUNLockDialog);
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