﻿$(function(){
	//订单状态：0-正常；1-已取消
	var orderStatus = [{'text':'全部','value':' '},{'text':'正常','value':'0'},{'text':'已取消','value':'1'}];
	var columns = [[
	             {field:'orderDate',title:'订单日期',width:'100px',align:'center',sortable :true,formatter:function(value,row,index){
	        		 return getYMDHMS(row.orderDate);
	        	 }}, 
	        	 {field:'orderNO',title:'订单编号',width:'12%',sortable :true,align:'center',formatter:function(value,row,index){
	        		 var img = '';
	        		 if(row.filePath != null  && row.filePath != ''){
	        			 img = '<img src="'+row.filePath+'" style="float:left;width:30px;height:30px;" name="orderImg"/>'
	        		 }else{
	        			 img = '<img src="" style="float:left;width:30px;height:30px;display:none;" name="orderImg"/>'
	        		 }
	        		
	        		 var btn = '<div style="float:right;width:120px;height:60px;" title="'+value+'" class="easyui-tooltip">'+value+' <br/><a class="uploadOrderFile" name="filePicker" data-id="'+row.id+'">上传条码</a></div>';
                 	return  img+btn;
                 }},
	        	 {field:'cabinetModel',title:'柜型',width:'5%',sortable :true,align:'center'},
	        	 {field:'cabinetRecipientAddr',title:'提还柜',width:'45px',align:'center',sortable :true,formatter:function(value,row,index){
	        		 var addr = '';
	        		 if(row.cabinetRecipientAddr != null && row.cabinetRecipientAddr != ''){
	        			 addr = row.cabinetRecipientAddr+"/";
	        		 }
	        		 if(row.cabinetReturnAddr != null && row.cabinetReturnAddr != ''){
	        			 addr += row.cabinetReturnAddr;
	        		 }
                 	return  addr;
                 },styler: function(value, row, index){
                	 	if (row.cabinetReturnAddr !=  row.cabinetRecipientAddr){  
                	        return 'background-color:yellow;';
                	    }
                	}},
                 {field:'address',title:'订单简址',width:'6%',sortable :true,align:'center'},
                 {field:'weighed',title:'重量(T)',width:'40px',sortable :true,align:'center'},
                 {field:'demand',title:'订单要求',width:'5%',sortable :true,align:'center', formatter:function(value,row,index){
                 	return "<div style='height:60px;' title='"+value+"'  class='easyui-tooltip'>"+value+"</div>";
                 }},
	        	 {field:'cabinetNumber',title:'柜号',width:'11%',align:'center',sortable :true,editor:{
	        		 type:'textbox',
	        		 options:{
	        			 editable:true
	        		 }
	        	 }},
	        	 {field:'sealNumber',title:'封号',width:'5%',align:'center',sortable :true,editor:{
	        		 type:'textbox',
	        		 options:{
	        			 editable:true
	        		 }
	        	 }},
                 {field:'plateNumber',title:'车牌号',width:'7%',align:'center',sortable :true,editor: {
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
	        	 {field:'ownerName',title:'司机姓名',width:'4%',align:'center',sortable :true,editor:{
	        		 type:'textbox',
	        		 options:{
	        			 editable:false
	        		 }
	        	 }}, 
	        	 {field:'contactNumber',title:'联系电话',width:'8%',align:'center',sortable :true,editor:{
	        		 type:'textbox',
	        		 options:{
	        			 editable:false
	        		 }
	        	 }},
	        	 {field:'driverPrice',title:'划价',width:'4%',align:'center',sortable :true,editor:{
	        		 type:'numberbox',
	        		 options:{
	        			 editable:true,
	        			 precision:2
	        		 }
	        	 }},
	        	 /**
	        	 {field:'otherAmt',title:'杂费金额',width:'5%',align:'center'},*/
	        	 {field:'companyName',title:'客户公司名称',width:'5%',align:'center'},
	        	 {field:'operatorName',title:'操作人',width:'4%',sortable :true,align:'center'},
	        	 {field:'orderStatus',title:'订单状态',width:'4%',align:'center', sortable :true,formatter:function(value,row,index){
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
                 {field:'updateDate',title:'修改日期',width:'5%',align:'center', sortable :true,formatter:function(value,row,index){
                	 return getYMDHMS(row.updateDate);
                 }},
	        	 {field:'opt',title:'操作',width:'320px',align:'center', formatter:function(value,row,index){
                 	return "<button class='btn btn-add-oa'>添加杂费</button><button class='btn btn-sel-oa'>查看杂费</button><button class='btn btn-edit'>修改</button><button class='btn btn-del'>删除</button><button class='btn btn-print'>派车单</button><button class='btn btn-text'>文本</button><button class='btn btn-copy'>复制</button>";
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
		var dataModel = $(".main-query-content form").serializeObject();
		if(dataModel.orderDateEnd){
			dataModel.orderDateEnd = dataModel.orderDateEnd+" 23:59:59";
		}
		$("#dataGrid").datagrid({
			url : '/order/list',
			queryParams : dataModel,
			rownumbers:true,
			singleSelect: true, //是否单选
			striped:true,//各行变色
			pagination: true, //分页控件
			pageNumber: 1,
			pageSize: 50,
			pageList: [50, 100, 150],
			autoRowHeight: false,
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
            sortOrder:'asc',
            onDblClickCell: function(index,field,value){
            	var fields = ['cabinetNumber','sealNumber','plateNumber','orderStatus'];
            	if(fields.indexOf(field) > -1){
            		if (editIndex != index && endEditing()) {  
            			$(this).datagrid('beginEdit', index);       
            	        editIndex = index;  
            	    }
            	}
        	},
        	onClickCell: function(index,field,value){
        		var rows = $('#dataGrid').datagrid('getRows');
        		var row = rows[index];
        		if(field == 'address' && row.detailAddress && row.detailAddress.length > 0){
        			$("<div id='detail_addr' style='padding:10px;' >"+row.detailAddress+"</div>").dialog({    
        		 	    title: '详细地址',    
        		 	    width: '450px',    
        		 	    height: '360px',    
        		 	    closed: false,    
        		 	    modal: true,
        		 	    onClose:function(){
        		 	    	$('#detail_addr').dialog('destroy');
        		 	   },
        		 	    buttons:[{
        					text:'关闭',
        					handler:function(){
        						$('#detail_addr').dialog('destroy');
        					}
        				}]
        		     });
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
			},onLoadSuccess: function(){
				                    $(".easyui-tooltip").tooltip({
					                        onShow: function () {
					                            $(this).tooltip('tip').css({
					                                borderColor: '#000',
													width:'300px'
					                            });
					                        }
					                    });
				fileUpload();
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
	
	var showCopyDialog = function(){
		var row = $('#dataGrid').datagrid('getSelected');
		row.id=null;
		row.orderDate = getYMDHMS(row.orderDate);
		row.ownerName = null;
		row.plateNumber = null;
		row.contactNumber = null;
		row.cabinetNumber = null;
		row.sealNumber = null;
		showAddDialog(row);
	}
	
	var showAddDialog = function(orderModel){
		if(orderModel){
			orderModel.orderDate = dateFormat(orderModel.orderDate,"yyyy-MM-dd HH:mm:ss");
	    }
		var html = Mustache.render($('#dialog_content_template').html(),orderModel);
		var diaHtml = "<div id='main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '订单管理',    
	 	    width: 400,    
	 	    height: 800,    
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
	 	    height: 800,    
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
			traditional:true,
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
	 	    	$('#itemName_view').combobox({
	 	    		onChange: function (n,o) {
	 	    			if(n == '办单费'){
	 	    				$('input[name=propertyTypes]').removeAttr("checked");
	 	    				$('#propertyTypes_myself_view').attr("checked",'checked');
	 	    			}
	 	    		}
	 	    	});
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
	   	             {field:'expenditureDate',title:'支出日期',width:'10%',align:'center',formatter:function(value,row,index){
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
		        	 {field:'remarks',title:'备注',width:'5%',align:'center'},
		        	 {field:'opt',title:'操作',width:'10%',align:'center', formatter:function(value,row,index){
		                 	return "<button class='btn btn-edit edit-other-amt' otherId='"+row.id+"' orderNO='"+row.orderNO+"'>修改</button><button class='btn btn-del del-other-amt' otherId='"+row.id+"' orderNO='"+row.orderNO+"'>删除</button>";
		                 }}
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
			autoRowHeight: false,
			fixRowHeight:'40',
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
		var diaHtml = "<div id='main_oth_dlg' class='main-dialog-view-content'><table id='dialogDataGrid' class='easyui-datagrid'></table></div>";
		$(diaHtml).dialog({    
	 	    title: '杂费项('+row.orderNO+')',    
	 	    width: 850,    
	 	    height: 500,    
	 	    closed: false,   
	 	    resizable:true,
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#main_oth_dlg');
	 	    	initDialogDataGrid({'orderId':row.id});
	 	    	$('#main_oth_dlg').delegate('button.del-other-amt','click',showDeleteOtherAmtDialog);
	 	    	$('#main_oth_dlg').delegate('button.edit-other-amt','click',showUpdateOtherAmtDialog);
	 	    	
	 	    },
	 	    onClose:function(){
	 	    	$('#main_oth_dlg').dialog('destroy');
	 	    	$('#main_oth_dlg').undelegate('button.del-other-amt','click',showDeleteOtherAmtDialog);
	 	    	$('#main_oth_dlg').undelegate('button.edit-other-amt','click',showUpdateOtherAmtDialog);
	 	   }
	     }); 
	}
	
	var downExcel = function(){
		var criteria = $(".main-query-content form").serializeObject();
		if(criteria.orderDateEnd){
			criteria.orderDateEnd = criteria.orderDateEnd+" 23:59:59";
		}
		buildExportFormSubmit("/order/loadExcel.do", criteria);
	}
	
	var copyText = function(){
		var text = $(this).val();
		var name = $(this).attr('name');
		$('.bottom_area input[name='+name+']').val(text);
	}
	
	var showPrintDialog = function(){
		var row = $('#dataGrid').datagrid('getSelected');
		row.orderDate = getYMDHMS(row.orderDate);
		var html = Mustache.render($('#print_dialog_content_template').html(),row);
		var diaHtml = "<div id='main_print_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '派车单('+row.orderNO+')',    
	 	    width: '90%',    
	 	    height: '90%',    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$('#print_area').delegate('.top_area input','change', copyText);
	 	    },
	 	    onClose:function(){
	 	    	$('#main_dlg').dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'打印',
				handler:function(){
					$('#print_area').print();
				}
			},{
				text:'关闭',
				handler:function(){
					$('#main_print_dlg').dialog('destroy');
				}
			}]
	     }); 
	}
	var showTextDialog = function(){
		var row = $('#dataGrid').datagrid('getSelected');
		var maskObj = new mask();
		var orderText = "单号："+row.orderNO+"，<br/>柜号："+row.cabinetNumber+"，<br/>封号："+row.sealNumber+"，<br/>车牌："+row.plateNumber+"，<br/>地址简称："+row.address+"，<br/>柜型："+row.cabinetModel+" <br/>司机电话："+row.contactNumber+"，<br/>";
		$.ajax({
			url:'/orderOtherAmt/list',
			type:"get",
			data:{'orderId':row.id,'propertyType':2},
			dataType:'json',
			async: false,
			traditional:true,
			beforeSend : function (){
			    maskObj.showMask();// 显示遮蔽罩
		    }
		}).done(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			$('#main_dlg').dialog('destroy');
			$(data.rows).each(function(i,v){
				orderText += (v.itemName+":"+v.price+"元，<br/>");
			});
		}).fail(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			$.messager.alert('操作提示','提柜费查询失败');
		});
		
		$("<div id='main_text_dlg'>"+orderText+"</div>").dialog({    
	 	    title: '订单信息',    
	 	    width: '450px',    
	 	    height: '300px',    
	 	    closed: false,    
	 	    modal: true,
	 	    onClose:function(){
	 	    	$(this).dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'关闭',
				handler:function(){
					$('#main_text_dlg').dialog('destroy');
				}
			}]
	     });
	}
	
	var upload = function(){
        var form = new FormData(document.getElementById("uploadWord"));
        $.ajax({
            url:"/order/uploadWord",
            type:"post",
            data:form,
            processData:false,
            contentType:false,
            success:function(data){
            	showAddDialog(data);
            },
            error:function(e){
                alert("错误！！");
            }
        });        
    }
	
	
	var operateOtherAmt = function(url, dataModel){
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
			$('#edit_main_ot_dlg').dialog('destroy');
			$.messager.alert('操作提示','操作成功');
			$("#dialogDataGrid").datagrid('reload');
		}).fail(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			$.messager.alert('操作提示','操作失败');
		});
	}
	
	var showDeleteOtherAmtDialog = function(){
		var otherId = $(this).attr('otherId');
		var orderNO = $(this).attr('orderNO');
		$.messager.confirm("删除提示", "是否删除:"+orderNO+"的杂费信息?",function(e){
		     if(e){
		    	 operateOtherAmt('/orderOtherAmt/delete', {orderOtherAmtId:otherId});
		     }
		 });
	}
	
	var chooseAddViewRadio = function(){
		var itemName = $('#itemName_view').combobox('getValue');//项目类型
		var propertyType = $('.main-form-content input[name=propertyType]:checked').val();//归属类型 1-司机 2-客户
		var isProfit = $('.main-form-content input[name=isProfit]:checked').val();//是否盈利 0-盈利 1-亏损
		if(isProfit == 0 && itemName != '办单费'){
			 //是否计入杂费 0-计入 1-不计入
			 $(".main-form-content input[name=isAdd][value='0']").attr('checked','true');
		}else{
			 $(".main-form-content input[name=isAdd][value='1']").attr('checked','true');
		}
	}
	var showUpdateOtherAmtDialog = function(){
		var row = $('#dialogDataGrid').datagrid('getSelected');
		row.expenditureDate = getYMDHMS(row.expenditureDate);
		var html = Mustache.render($('#otherAmt_edit_dialog_content_template').html(),row);
		var diaHtml = "<div id='edit_main_ot_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '订单杂费管理('+row.orderNO+')',    
	 	    width: 400,    
	 	    height: 500,    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#edit_main_ot_dlg');
	 	    	initDataDic();
	 	    	loadOrderGrid();
	 	    	$('.main-form-content input[name=propertyType][value='+row.propertyType+']').attr("checked",true);
	 	    	$('.main-form-content input[name=isSettle][value='+row.isSettle+']').attr("checked",true);
	 	    	$('.main-form-content').delegate('input[name=propertyType]','click',chooseAddViewRadio);
	 	    	$('#itemName_view').combobox({
	 	    		onChange:function(n,o){
	 	    			chooseAddViewRadio();
	 	    		}
	 			});
	 	    },
	 	    onClose:function(){
	 	    	$('#edit_main_ot_dlg').dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'修改',
				handler:function(){
					if($(".main-form-content").form ("validate")){
						operateOtherAmt('/orderOtherAmt/edit', $(".main-form-content").serializeObject());
					}
				}
			},{
				text:'关闭',
				handler:function(){
					$('#edit_main_ot_dlg').dialog('destroy');
				}
			}]
	     }); 
	}
	
	var fileUpload = function() {
		var maxLength = 1;
		var setHeader = function(object, data, headers) {
			if (document.all) {
				headers['Access-Control-Allow-Origin'] = '*';
				headers['Access-Control-Request-Headers'] = 'content-type';
				headers['Access-Control-Request-Method'] = 'POST';
			}
		}
		WebUploader.create({
			auto: true,
			duplicate:true, // 图片可以重复
			compress:null,
			swf: '../static/js/webuploader-0.1.5/Uploader.swf',
			server: '/upload/uploadImg.do',
			threads:1,
			pick: {
				id: 'a[name=filePicker]',
				multiple: false		//是否可以选择多张图片
			},
			accept: {
				title: 'Images',
				extensions: 'jpg,jpeg,bmp,png',
				mimeTypes: 'image/jpg,image/jpeg,image/bmp,image/png'			//弹窗会加快
			},
			fileSingleSizeLimit:10242880,
			onUploadBeforeSend: setHeader,
			onFilesQueued:function(files){
			  if(files.length > maxLength){
				$.messager.alert({
					title: '图片上传提示',
					msg: '<div class="content">最多同时上传'+maxLength+'张图</div>',
					ok: '<i class="i-ok"></i> 确定',
					icon: 'warning'
				});
				this.reset();
			  }
			},
			onUploadError: function(file, reason) {
				if (reason == "http") {
					$.messager.alert({
						title: '图片上传提示',
						msg: '<div class="content">网络异常!</div>',
						ok: '<i class="i-ok"></i> 确定',
						icon: 'warning'
					});
				} else {
					$.messager.alert({
						title: '图片上传提示',
						msg: '<div class="content">不支持的图片,换张图片试试!</div>',
						ok: '<i class="i-ok"></i> 确定',
						icon: 'warning'
					});
				}
				this.removeFile(file);
			},
			onUploadProgress: function(file, percentage) {},
			onUploadSuccess: function(file, response) {
				updateOrderFile('#rt_' + file.source.ruid,response.path);
				var ossImage = response.path;
				$('.friend-head').css({'background-image':'url('+ossImage+')','background-repeat':'no-repeat','background-size':'100% 100%','-moz-background-size':'100% 100%'});
				$('input[name=showImg]').val(ossImage);
				this.removeFile(file);
			},
			onError: function(file, response) {
				switch (file) {
					case "Q_EXCEED_NUM_LIMIT":
						$.messager.alert({
							title: '图片上传提示',
							msg: '<div class="content">最多同时上30张图片</div>',
							ok: '<i class="i-ok"></i> 确定',
							icon: 'warning'
						});
						break;
					case "F_DUPLICATE":
						$.messager.alert({
							title: '图片上传提示',
							msg: '<div class="content">文件重复!</div>',
							ok: '<i class="i-ok"></i> 确定',
							icon: 'warning'
						});
						break;
					case "F_EXCEED_SIZE":
						$.messager.alert({
							title: '图片上传提示',
							msg: '<div class="content">图片大小不超10M!</div>',
							ok: '<i class="i-ok"></i> 确定',
							icon: 'warning'
						});

						break;
					case "Q_TYPE_DENIED":
						$.messager.alert({
							title: '图片上传提示',
							msg: '<div class="content">请上传jpg,jpeg,bmp,png格式图片!</div>',
							ok: '<i class="i-ok"></i> 确定',
							icon: 'warning'
						});
						break;
					default:
						$.messager.alert({
							title: '图片上传提示',
							msg: '<div class="content">上传失败!</div>',
							ok: '<i class="i-ok"></i> 确定',
							icon: 'warning'
						});
						break;
				}
			}

		});
		$('input.webuploader-element-invisible').hide();//
		$('a.uploadOrderFile div.webuploader-pick').addClass('btn');
	};
	
	var updateOrderFile = function(uploaderId,ossImage){
		var orderId = $(uploaderId).parents('a.webuploader-container').attr('data-id');
		var url = '/order/updateFile.do';
		var dataModel = {};
		dataModel.orderId = orderId;
		dataModel.filePath = ossImage;
		$.ajax({
			url:url,
			type:"post",
			data:dataModel,
			dataType:'json'
		}).done(function(data){
			$("#dataGrid").datagrid('reload');
			//$(uploaderId).parents('td').find('img').attr('src',ossImage);
			//$(uploaderId).parents('td').find('img').css('display','');
			$.messager.alert('操作提示','上传成功');
		}).fail(function(data){
			$.messager.alert('操作提示','上传失败');
		});
	}
	
	  var x = 50;
	  var y = 90;
	var showOrderImg = function(e){
	    var tooltip = "<div id='tooltip'><img src='"+ $(this)[0].src +"' alt='预览图'/><\/div>";
	    //创建 div 元素
	    $("body").append(tooltip);
	    //把它追加到文档中             
	    $("#tooltip")
	      .css({
	        "top": (e.pageY+y) + "px",
	        "left": (e.pageX+x) + "px"
	      }).show("fast"); //设置x坐标和y坐标，并且显示
	} 
	var removeOrderImg = function(){
		$("#tooltip").remove(); //移除 
	}
	var showInOrderImg = function(e){
		$("#tooltip")
	      .css({
	        "top": (e.pageY+y) + "px",
	        "left": (e.pageX+x) + "px"
	      });
	}
	var initializeUI = function(){
		var nowDate = new Date();
		var toDay = nowDate.getFullYear() + '-'+ (nowDate.getMonth()+1) + '-' + nowDate.getDate();
		$('#orderDateBegin_query').datebox('setValue', toDay);
		$('#orderDateEnd_query').datebox('setValue', toDay);
		initQueryDataDic();
		initOrderStatus();
		initDataGrid();
		$('.dataTable-toolbar').delegate('button.btn-add','click',showAddDialog);
		$('.dataTable-toolbar').delegate('button.btn-excel','click',downExcel);
		$('.dataTable-toolbar').delegate('button.btn-upload','click',upload);
		$('.main-query-content').delegate('button.btn-search','click',initDataGrid);
		$('.main-dataTable-content').delegate('button.btn-edit','click',showUpdateDialog);
		$('.main-dataTable-content').delegate('button.btn-del','click',showDeleteDialog);
		$('.main-dataTable-content').delegate('button.btn-add-oa','click',showOtherAmtDialog);
		$('.main-dataTable-content').delegate('button.btn-sel-oa','click',showOtherAmtItemDialog);
		$('.main-dataTable-content').delegate('button.btn-print','click',showPrintDialog);
		$('.main-dataTable-content').delegate('button.btn-text','click',showTextDialog);
		$('.main-dataTable-content').delegate('button.btn-copy','click',showCopyDialog);
		$('.main-dataTable-content').delegate('.datagrid-view','click',endEditing);
		//$('.main-dataTable-content').delegate('img[name=orderImg]','mouseover',showOrderImg);
		//$('.main-dataTable-content').delegate('img[name=orderImg]','mouseout',removeOrderImg);
		//$('.main-dataTable-content').delegate('img[name=orderImg]','mousemove',showInOrderImg);
	}
	initializeUI();
})