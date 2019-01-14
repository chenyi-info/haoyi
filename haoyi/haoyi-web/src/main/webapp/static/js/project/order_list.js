﻿$(function(){
	//订单状态：0-正常；1-已取消
	var orderStatus = [{'text':'全部','value':' '},{'text':'正常','value':'0'},{'text':'已取消','value':'1'}];
	var columns = [[
	             {field:'orderDate',title:'订单日期',width:'5%',align:'center',formatter:function(value,row,index){
	        		 return getYMDHMS(row.orderDate);
	        	 }}, 
	           	 
	        	 {field:'orderNO',title:'订单编号',width:'10%',align:'center'},
	        	 {field:'demand',title:'订单要求',width:'5%',align:'center'},
	        	 {field:'cabinetModel',title:'柜型',width:'5%',align:'center'},
	        	 {field:'cabinetNumber',title:'柜号',width:'5%',align:'center'},
	        	 {field:'sealNumber',title:'封号',width:'5%',align:'center'},
	        	 {field:'address',title:'订单简址',width:'5%',align:'center'},
	        	 {field:'weighed',title:'重量(T)',width:'5%',align:'center'},
                 {field:'plateNumber',title:'车牌号',width:'5%',align:'center',editor: {
                     type: 'combogrid', // 指明控件类型
                     options:{
                 		mode : 'remote',//远程连接方式  
                        striped: true,
                        method:'GET',//请求方式  
                        dataType:'json',
                        panelWidth: 600,
                        url: '/vehicle/list',
                        idField: 'plateNumber',
                        textField: 'plateNumber',
                        editable: true,
                        pagination: true,//是否分页
                        columns: [[
                            {field:'plateNumber',title:'车牌号',width:'30%',align:'center'},    
                	    	{field:'ownerName',title:'司机姓名',width:'20%',align:'center'}, 
                	    	{field:'contactNumber',title:'联系电话',width:'20%',align:'center'},
                	    	{field:'remarks',title:'备注',width:'30%',align:'center'}
                        ]],
                        keyHandler:{
                            up: function() {},
                            down: function() {},
                            enter: function() {},
                            query: function(q) {
                                //动态搜索
                               $(this).combogrid("grid").datagrid("reload", {'plateNumber': q});
                               $(this).combogrid("setValue", q);
                            }
                        },onChange : function(newValue, oldValue){
                        	var g = $(this).combogrid('grid');	// get datagrid object
                        	var r = g.datagrid('getSelected');
                        	if(r != null){
                        		$('.main-dataTable-content  .datagrid-btable tr.datagrid-row-editing td[field=ownerName] .datagrid-editable-input').textbox("setValue", r.ownerName);
                        		$('.main-dataTable-content  .datagrid-btable tr.datagrid-row-editing td[field=contactNumber] .datagrid-editable-input').textbox("setValue", r.contactNumber);
                        	}else{
                        		$('.main-dataTable-content  .datagrid-btable tr.datagrid-row-editing td[field=ownerName] .datagrid-editable-input').textbox("setValue", '');
                        		$('.main-dataTable-content  .datagrid-btable tr.datagrid-row-editing td[field=contactNumber] .datagrid-editable-input').textbox("setValue", '');
                        	}
                        }}
                 }},    
	        	 {field:'ownerName',title:'司机姓名',width:'5%',align:'center',editor:{
	        		 type:'textbox',
	        		 options:{
	        			 editable:false
	        		 }
	        	 }}, 
	        	 {field:'contactNumber',title:'联系电话',width:'10%',align:'center',editor:{
	        		 type:'textbox',
	        		 options:{
	        			 editable:false
	        		 }
	        	 }},
	        	 {field:'otherAmt',title:'杂费金额',width:'5%',align:'center'},
	        	 {field:'companyName',title:'客户公司名称',width:'5%',align:'center'},
	        	 {field:'operatorName',title:'操作人',width:'5%',align:'center'},
	        	 {field:'orderStatus',title:'订单状态',width:'5%',align:'center', formatter:function(value,row,index){
                 	return value == 0 ? '正常' : '已取消';
                 },editor: {
                     type: 'combobox', // 指明控件类型
                     options: {
                         textField: 'text',
                         valueField: 'value',
                         editable:false,
                         data: [
                             {
                            	 text: '正常',
                                 value: 0
                             },
                             {
                            	 text: '取消',
                                 value: 1
                             }
                         ],
                         required: true, // 是否必填
                         missingMessage: '请选择订单类型'
                     }
                 }},
	        	 
	        	 {field:'opt',title:'操作',width:'200px',align:'center', formatter:function(value,row,index){
                 	return "<button class='btn btn-add-oa'>添加杂费</button><button class='btn btn-sel-oa'>查看杂费</button><button class='btn btn-edit'>修改</button><button class='btn btn-del'>删除</button>";
                 }}
	            ]];
	
	var editIndex = undefined;  
	var endEditing = function(){//该方法用于关闭上一个焦点的editing状态  
	    if (editIndex == undefined) {  
	        return true  
	    }
	    if ($('#dataGrid').datagrid('validateRow', editIndex)) {
	        $('#dataGrid').datagrid('endEdit', editIndex);  
	        var row = $('#dataGrid').datagrid('getChanges');
	        if(row.length == 1){
	        	$.messager.confirm("修改提示", "是否修改:"+row[0].orderNO+"的信息?",function(e){
		   		     if(e){
		   		    	eidtOrder(row[0]);
		   		     }
	   		 	});
	        	
	        }
	        editIndex = undefined;  
	        return true;  
	    } else {  
	        return false;  
	    }
	}
	
	var initDataGrid = function(){
		$("#dataGrid").datagrid({
			url : '/order/list',
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
            columns:columns,
            onDblClickCell: function(index,field,value){
            	var fields = ['plateNumber','orderStatus'];
            	if(fields.indexOf(field) > -1){
            		if (editIndex != index && endEditing()) {  
            			$(this).datagrid('beginEdit', index);       
            	        editIndex = index;  
            	    }
            	}
        	},
        	onClickRow:function(index,row){
        		if (editIndex != index) {  
        			endEditing();
        	    }
        	},onBeginEdit:function(index,row){
        		if(row.orderStatus ==1){
        			var editor = $(this).datagrid("getEditor", {'index':index,'field':'plateNumber'});
        			var plateNumber = editor.target;
        			plateNumber.combobox({  
        				disabled: true   //是否可编辑
        			});
        		}
			}
        });
	}
	
	var eidtOrder = function(dataModel){
		dataModel.orderDate = dateFormat(dataModel.orderDate,"yyyy-MM-dd HH:mm:ss");
		dataModel.createDate = dateFormat(dataModel.createDate,"yyyy-MM-dd HH:mm:ss");
		dataModel.updateDate = dateFormat(dataModel.updateDate,"yyyy-MM-dd HH:mm:ss");
		dataModel.hasConfirm =true;
		dataModel = removeNullParams(dataModel);
		var maskObj = new mask();
		$.ajax({
			url:'/order/edit',
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
			$("#dataGrid").datagrid('reload');
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
			if(data.status == 200 && data.statusCode != 'hasOrder'){
				$('#main_dlg').dialog('destroy');
				$("#dataGrid").datagrid('reload');
			}else if(data.status == 200 && data.statusCode == 'hasOrder'){
				$.messager.confirm("重复提示", data.msg,function(e){
				     if(e){
				    	 dataModel.hasConfirm =true;
				    	 operateVehicle(url,dataModel);
				     }
				 });
				return;
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
	 	    title: '订单管理',    
	 	    width: 400,    
	 	    height: 760,    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#main_dlg');
	 	    	loadVehicleGrid();
	 	    	loadCompanyGrid();
	 	    	initDataDic();
	 	    },
	 	    onClose:function(){
	 	    	$('#main_dlg').dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'保存',
				handler:function(){
					if($(".main-form-content").form ("validate")){
						operateVehicle('/order/add', $(".main-form-content").serializeObject());
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
	 	    title: '订单管理('+row.orderNO+')',    
	 	    width: 400,    
	 	    height: 760,    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$('.main-form-content input[name=orderStatus][value='+row.orderStatus+']').attr("checked",true);
	 	    	$.parser.parse('#main_dlg');
	 	    	loadVehicleGrid();
	 	    	loadCompanyGrid();
	 	    	initDataDic();
	 	    },
	 	    onClose:function(){
	 	    	$('#main_dlg').dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'修改',
				handler:function(){
					if($(".main-form-content").form ("validate")){
						operateVehicle('/order/edit', $(".main-form-content").serializeObject());
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
		    	 operateVehicle('/order/delete', {orderId:row.id});
		     }
		 });
	}
	var initOrderStatus = function(){
		$('#orderStatus_query').combobox({
			data:orderStatus,
			editable:false
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
		        	 {field:'cabinetNumber',title:'柜号',width:'10%',align:'center'},
		        	 {field:'address',title:'订单简址',width:'10%',align:'center'},
		        	 {field:'itemName',title:'支付项目类型',width:'10%',align:'center'},
		        	 {field:'price',title:'支付金额',width:'10%',align:'center'},
		        	 {field:'targetName',title:'支出对象',width:'10%',align:'center'},
		        	 {field:'propertyType',title:'归属类型',width:'10%',align:'center', formatter:function(value,row,index){
		        		 return value == 1 ? '司机' : value == 2 ? '客户' : '自己';
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
	 	    	initDialogDataGrid({'orderId':row.id});
	 	    	$('.main-dialog-view-content').delegate('button.btn-edit','click',updateItemDialog);
	 			$('.main-dialog-view-content').delegate('button.btn-del','click',showDeleteItemDialog);
	 	    },
	 	    onClose:function(){
	 	    	$('#main_dlg').dialog('destroy');
	 	   }
	     }); 
	}
	
	var downExcel = function(){
		var criteria = $(".main-query-content form").serializeObject();
		buildExportFormSubmit("/order/loadExcel.do", criteria);
	}
	
	var initializeUI = function(){
		var nowDate = new Date();
		var toDay = nowDate.getFullYear() + '-'+ (nowDate.getMonth()+1) + '-' + nowDate.getDate();
		$('#orderDateBegin_query').datetimebox('setValue', toDay+' 00:00:00')
		$('#orderDateEnd_query').datetimebox('setValue', toDay+' 23:59:59')
		initOrderStatus();
		initDataGrid();
		$('.dataTable-toolbar').delegate('button.btn-add','click',showAddDialog);
		$('.dataTable-toolbar').delegate('button.btn-excel','click',downExcel);
		$('.main-query-content').delegate('button.btn-search','click',initDataGrid);
		$('.main-dataTable-content').delegate('button.btn-edit','click',showUpdateDialog);
		$('.main-dataTable-content').delegate('button.btn-del','click',showDeleteDialog);
		$('.main-dataTable-content').delegate('button.btn-add-oa','click',showOtherAmtDialog);
		$('.main-dataTable-content').delegate('button.btn-sel-oa','click',showOtherAmtItemDialog);
		$('.main-dataTable-content').delegate('.datagrid-view','click',endEditing);
	}
	initializeUI();
	
})