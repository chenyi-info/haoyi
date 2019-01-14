$(function(){
	var columns = [[
	           	 {field:'menuName',title:'菜单名称',width:'20%'},    
	        	 {field:'menuPath',title:'菜单路径',width:'20%'}, 
	        	 {field:'funCode',title:'菜单编码',width:'10%'},
	        	 {field:'status',title:'菜单状态',width:'10%',align:'center',formatter:function(value,row,index){
	        		 return value == 1 ? '正常' : '关闭';
	        	 }},
	        	 {field:'sort',title:'菜单排序',width:'5%',align:'center'},
	        	 {field:'remarks',title:'备注',width:'10%'},
	        	 {field:'createDate',title:'创建时间',width:'15%',align:'center',formatter:function(value,row,index){
	        		 return getYMDHMS(row.createDate);
	        	 }},
	        	 {field:'opt',title:'操作',width:'10%',align:'center', formatter:function(value,row,index){
                 	return "<button class='btn btn-add-child'>添加子菜单</button><button class='btn btn-edit'>修改</button><button class='btn btn-del'>删除</button>";
                 }}
	            ]];
	function myLoadFilter(data,parentId){
		function setData(){
			var todo = [];
			for(var i=0; i<data.length; i++){
				todo.push(data[i]);
			}
			while(todo.length){
				var node = todo.shift();
				if (node.children){
					node.state = 'closed';
					node.children1 = node.children;
					node.children = undefined;
					todo = todo.concat(node.children1);
				}
			}
		}
		
		setData(data);
		var tg = $(this);
		var opts = tg.treegrid('options');
		opts.onBeforeExpand = function(row){
			if (row.children1){
				tg.treegrid('append',{
					parent: row[opts.idField],
					data: row.children1
				});
				row.children1 = undefined;
				tg.treegrid('expand', row[opts.idField]);
			}
			return row.children1 == undefined;
		};
		return data;
	}
	
	var initDataGrid = function(){
		$("#dataGrid").treegrid({
			url : '/menuResources/list',
			rownumbers: true, //行号
			singleSelect: true, //是否单�			
			pagination: false, //分页控件
			autoRowHeight: false,
			fit: true,
			striped: true, //设置为true将交替显示行背景
			state:closed,
			fitColumns: true,
			nowrap: false,
			remotesort: false,
			checkOnSelect: false,
			method: "get", //请求数据的方�			
			loadMsg: '数据加载中请稍后',
			idField:'id',
			parentField:'parentId',
			treeField:'menuName',
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
				$("#dataGrid").treegrid('reload');
			}
			$.messager.alert('操作提示',data.msg);
		}).fail(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			$.messager.alert('操作提示','操作失败');
		});
	} 
	
	var showAddDialog = function(){
		var h = 300;
		var hasParent = $(this).hasClass('btn-add-child');
		var dataModel = {}
		dataModel.parentId = -1;
		dataModel.level = 1;
		if(hasParent){
			var row = $('#dataGrid').treegrid('getSelected');
			dataModel.parentId = row.id;
			dataModel.level = row.level+1;
			dataModel.parentMenuName = row.menuName;
			h = 400;
		}
		var html = Mustache.render($('#dialog_content_template').html(),dataModel);
		var diaHtml = "<div id='main_dlg'>"+html+"</div>";
		
		$(diaHtml).dialog({
	 	    title: '新增菜单',    
	 	    width: 400,    
	 	    height: h,    
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
						operateVehicle('/menuResources/add', $(".main-form-content").serializeObject());
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
		var row = $('#dataGrid').treegrid('getSelected');
		var html = Mustache.render($('#dialog_content_template').html(),row);
		var diaHtml = "<div id='main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '修改菜单('+row.menuName+')',    
	 	    width: 400,    
	 	    height: 350,    
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
						operateVehicle('/menuResources/edit', $(".main-form-content").serializeObject());
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
		var row = $('#dataGrid').treegrid('getSelected');
		$.messager.confirm("删除提示", "是否删除菜单:"+row.menuName+"的信息?",function(e){
		     if(e){
		    	 operateVehicle('/menuResources/delete', {menuId:row.id});
		     }
		 });
	}
	
	var initializeUI = function(){
		initDataGrid();
		$('.dataTable-toolbar').delegate('button.btn-add','click',showAddDialog);
		$('.main-dataTable-content').delegate('button.btn-edit','click',showUpdateDialog);
		$('.main-dataTable-content').delegate('button.btn-del','click',showDeleteDialog);
		$('.main-dataTable-content').delegate('button.btn-add-child','click',showAddDialog);
	}
	initializeUI();
	
})