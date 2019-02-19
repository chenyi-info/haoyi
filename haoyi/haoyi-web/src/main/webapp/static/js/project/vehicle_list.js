$(function(){
	var columns = [[
	           	 {field:'plateNumber',title:'车牌号',width:'20%',align:'center'},    
	        	 {field:'ownerName',title:'司机姓名',width:'10%',align:'center'}, 
	        	 {field:'contactNumber',title:'联系电话',width:'15%',align:'center'},
	        	 {field:'vehicleType',title:'车辆类型',width:'10%',align:'center'},
	        	 {field:'vehicleSource',title:'车辆来源',width:'10%',align:'center'},
	        	 {field:'selfWeight',title:'自重(T)',width:'5%',align:'center'},
	        	 {field:'remarks',title:'备注',width:'10%',align:'center'},
	        	 {field:'createDate',title:'创建时间',width:'10%',align:'center',formatter:function(value,row,index){
	        		 return getYMDHMS(row.createDate);
	        	 }},
	        	 {field:'opt',title:'操作',width:'10%',align:'center', formatter:function(value,row,index){
                 	return "<button class='btn btn-edit'>修改</button><button class='btn btn-del'>删除</button>";
                 }}
	            ]];
	var initDataGrid = function(){
		$("#dataGrid").datagrid({
			url : '/vehicle/list',
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
			if(data.status == 200){
				$('#main_dlg').dialog('destroy');
				$("#dataGrid").datagrid('reload');
			}
			$.messager.alert('操作提示',data.msg);
		}).fail(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			$.messager.alert('操作提示','操作失败');
		});
	} 
	
	var showAddDialog = function(){
		var html = Mustache.render($('#dialog_content_template').html(),{});
		var diaHtml = "<div id='main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '新增司机',    
	 	    width: 400,    
	 	    height: 400,    
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
						operateVehicle('/vehicle/add', $(".main-form-content").serializeObject());
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
	 	    title: '修改司机('+row.ownerName+')',    
	 	    width: 400,    
	 	    height: 400,    
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
				text:'修改',
				handler:function(){
					if($(".main-form-content").form ("validate")){
						operateVehicle('/vehicle/edit', $(".main-form-content").serializeObject());
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
		$.messager.confirm("删除提示", "是否删除司机:"+row.ownerName+"的信息?",function(e){
		     if(e){
		    	 operateVehicle('/vehicle/delete', {vehicleId:row.id});
		     }
		 });
	}
	var downExcel = function(){
		var criteria = $(".main-query-content form").serializeObject();
		buildExportFormSubmit("/vehicle/loadExcel.do", criteria);
	}
	var initializeUI = function(){
		initQueryDataDic();
		initDataGrid();
		$('.dataTable-toolbar').delegate('button.btn-add','click',showAddDialog);
		$('.dataTable-toolbar').delegate('button.btn-excel','click',downExcel);
		$('.main-query-content').delegate('button.btn-search','click',initDataGrid);
		$('.main-dataTable-content').delegate('button.btn-edit','click',showUpdateDialog);
		$('.main-dataTable-content').delegate('button.btn-del','click',showDeleteDialog);
	}
	initializeUI();
	
})