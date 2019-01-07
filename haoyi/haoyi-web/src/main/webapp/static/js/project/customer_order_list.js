$(function(){
	//结算状态：0-未结算；1-已结算
	var settleStatus = [{'text':'全部','value':' '},{'text':'未结算','value':'0'},{'text':'已结算','value':'1'}];
	var columns = [[
	             {field:'orderDate',title:'订单日期',width:'10%',align:'center',formatter:function(value,row,index){
	        		 return getYMDHMS(row.orderDate);
	        	 }}, 
	           	 {field:'companyName',title:'公司名称',width:'10%',align:'center'},  
	           	 {field:'orderNO',title:'订单编号',width:'10%',align:'center'},
	           	 {field:'address',title:'订单简址',width:'10%',align:'center'},
	           	 {field:'cabinetModel',title:'柜型',width:'10%',align:'center'},
	        	 {field:'cabinetNumber',title:'柜号',width:'5%',align:'center'},
	        	 {field:'sealNumber',title:'封号',width:'5%',align:'center'},
	        	 {field:'orderPrice',title:'订单金额',width:'10%',align:'center'},
	        	 {field:'otherAmt',title:'杂费金额',width:'5%',align:'center'},
	        	 {field:'settleStatus',title:'结算状态',width:'5%',align:'center', formatter:function(value,row,index){
                 	return value == 1 ? '已结算' : '未结算';
                 }},
	        	 {field:'remarks',title:'备注',width:'10%',align:'center'},
	        	 {field:'opt',title:'操作',width:'10%',align:'center', formatter:function(value,row,index){
                 	return "<button class='btn btn-edit'>修改</button>";
                 }}
	            ]];
	var initDataGrid = function(){
		loadCustomerTotalAmt();
		$("#dataGrid").datagrid({
			url : '/customerOrder/list',
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
		$.ajax({
			url:'/customerOrder/totalAmt',
			type:"get",
			data:$(".main-form-content").serializeObject(),
			dataType:'json'
		}).done(function(data){
			$('.dataTable-toolbar .totalAmt').html(data);
		}).fail(function(data){
			$.messager.alert('操作提示','获取合计总金额失败');
		});
	} 
	
	var initializeUI = function(){
		initSettleStatus();
		initDataGrid();
		$('.dataTable-toolbar').delegate('button.btn-add','click',showAddDialog);
		$('.main-query-content').delegate('button.btn-search','click',initDataGrid);
		$('.main-dataTable-content').delegate('button.btn-edit','click',showUpdateDialog);
		$('.main-dataTable-content').delegate('button.btn-del','click',showDeleteDialog);
	}
	initializeUI();
	
})