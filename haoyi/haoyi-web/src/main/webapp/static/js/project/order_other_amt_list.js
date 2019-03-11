$(function(){
	//结算状态：0-未结算；1-已结算
	var settleStatus = [{'text':'全部','value':' '},{'text':'已结算','value':'0'},{'text':'未结算','value':'1'}];
	var columns = [[
	             {field : 'id',width : '3%',align : 'center',checkbox:'true'},
	             {field:'expenditureDate',title:'支出日期',width:'17%',align:'center',formatter:function(value,row,index){
	        		 return getYMDHMS(row.expenditureDate);
	        	 }}, 
	        	 {field:'orderNO',title:'订单编号',width:'10%',align:'center'},
	        	 {field:'price',title:'支付金额',width:'10%',align:'center'},
	        	 {field:'targetName',title:'支出对象',width:'10%',align:'center'},
	        	 {field:'cabinetNumber',title:'柜号',width:'10%',align:'center'},
	        	 {field:'address',title:'订单简址',width:'5%',align:'center'},
	        	 {field:'itemName',title:'支付项目类型',width:'10%',align:'center'},
	        	 {field:'propertyType',title:'归属类型',width:'5%',align:'center', formatter:function(value,row,index){
                 	return value == 1 ? '司机' : value == 2 ? '客户' : '自己';
                 }},
                 {field:'isSettle',title:'结算状态',width:'5%',align:'center', formatter:function(value,row,index){
                 	return value == 0 ? '已结算' :'未结算';
                 }},
	        	 {field:'remarks',title:'备注',width:'5%',align:'center'},
	        	 {field:'opt',title:'操作',width:'10%',align:'center', formatter:function(value,row,index){
                 	return "<button class='btn btn-edit'>修改</button><button class='btn btn-del'>删除</button>";
                 }}
	            ]];
	var initDataGrid = function(){
		$("#dataGrid").datagrid({
			url : '/orderOtherAmt/list',
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
			checkOnSelect: true,
			selectOnCheck: false,
			method: "GET", //请求数据的方法
			loadMsg: '数据加载中,请稍候......',
			idField: 'id',
			pagePosition: "bottom",
			view:dataTableView,
			emptyMsg:'未查询到内容',
            columns:columns,
            onCheck : function (index, row) {
            	var total = parseInt($('.dataTable-toolbar .totalAmt').html());
            	$('.dataTable-toolbar .totalAmt').html(total + row.price);
            },onUncheck : function (index, row) {
            	var total = parseInt($('.dataTable-toolbar .totalAmt').html());
            	$('.dataTable-toolbar .totalAmt').html(total - row.price);
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
	
	var showAddDialog = function(){
		var html = Mustache.render($('#dialog_content_template').html(),{});
		var diaHtml = "<div id='main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '订单杂费管理',    
	 	    width: 400,    
	 	    height: 500,    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#main_dlg');
	 	    	initDataDic();
	 	    	loadOrderGrid();
	 	    	$('.main-form-content').delegate('input[name=propertyType]','click',chooseAddViewRadio);
	 	    	$('#itemName_view').combobox({
	 	    		onChange:function(n,o){
	 	    			chooseAddViewRadio();
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
						operateVehicle('/orderOtherAmt/add', $(".main-form-content").serializeObject());
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
	 	    title: '订单杂费管理('+row.orderNO+')',    
	 	    width: 400,    
	 	    height: 500,    
	 	    closed: false,    
	 	    modal: true,
	 	    onOpen:function(){
	 	    	$.parser.parse('#main_dlg');
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
	 	    	$('#main_dlg').dialog('destroy');
	 	   },
	 	    buttons:[{
				text:'修改',
				handler:function(){
					if($(".main-form-content").form ("validate")){
						operateVehicle('/orderOtherAmt/edit', $(".main-form-content").serializeObject());
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
	
	var showDeleteDialog = function(){
		var row = $('#dataGrid').datagrid('getSelected');
		$.messager.confirm("删除提示", "是否删除:"+row.orderNO+"的信息?",function(e){
		     if(e){
		    	 operateVehicle('/orderOtherAmt/delete', {orderOtherAmtId:row.id});
		     }
		 });
	}
	
	var showSettledDialog = function(){
		var row = $('#dataGrid').datagrid('getChecked');
		var ids = [];
		$.each(row,function(i,v){
			if(v.isSettle == 1){
				ids[ids.length] = v.id;
			}
			
		});
		if(ids.length == 0){
			$.messager.alert('操作提示','请选择未结算的信息');
			return false;
		}
		$.messager.confirm("结算提示", "是否批量结算该批信息?",function(e){
		     if(e){
		    	 batchSettled('/orderOtherAmt/batchSettles', {orderOtherAmtIds:ids});
		     }
		 });
	}
	
	var downExcel = function(){
		var criteria = $(".main-query-content form").serializeObject();
		buildExportFormSubmit("/orderOtherAmt/loadExcel.do", criteria);
	}
	
	var initSettleStatus = function(){
		$('#settleStatus_query').combobox({
			data:settleStatus,
			editable:false
		});
	}
	
	var initializeUI = function(){
		initSettleStatus();
		initDataGrid();
		initQueryDataDic();
		$('.dataTable-toolbar').delegate('button.btn-batch-settled','click',showSettledDialog);
		$('.dataTable-toolbar').delegate('button.btn-add','click',showAddDialog);
		$('.dataTable-toolbar').delegate('button.btn-excel','click',downExcel);
		$('.main-query-content').delegate('button.btn-search','click',initDataGrid);
		$('.main-dataTable-content').delegate('button.btn-edit','click',showUpdateDialog);
		$('.main-dataTable-content').delegate('button.btn-del','click',showDeleteDialog);
		var propertyTypeItems = [{'text':'全部','value':' '},{'text':'司机','value':'1'},{'text':'客户','value':'2'},{'text':'自己','value':'3'}];
		$('#propertyType_query_view').combobox({
			data:propertyTypeItems,
			editable:false
		});
	}
	initializeUI();
	
})