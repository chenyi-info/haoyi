$(function(){
	
	//初始化渲染
	var renderInitAuthority = function (initAuthority){
		var tpl ="";
		var length1= initAuthority.length;
		tpl+='<div class="content">';
		 for(var i =0;i<length1;i++){
			 tpl +='<div class="row1">'+
		 	'<div class="th">'+
	        '<label for="'+initAuthority[i].id+'">'+
	        '<input id="'+initAuthority[i].id+'" pid="-1" type="checkbox" value="checkbox"> '+initAuthority[i].menuName+'</label>'+
	        '</div>';
		 	if("childNode" in initAuthority[i]){
		 		var length2 = initAuthority[i].childNode.length;
			 	for(var j =0;j<length2;j++){
			 		if(initAuthority[i].childNode[j].sysName){
			 			tpl +='<div class="row2" ><div class="th" style=" width: 560px;">';
			 			tpl +='<div class="sysrow" style=" width:160px;"><label for="'+initAuthority[i].childNode[j].id+'">'+initAuthority[i].childNode[j].sysName+'</label></div>';
			 		}else{
			 			tpl +='<div class="row2" ><div class="th" style=" width: 220px;"><label for="'+initAuthority[i].childNode[j].id+'">';
			 		}
			 			
			        if(initAuthority[i].childNode[j].aliases){
			        	tpl+='<label  for="'+initAuthority[i].childNode[j].id+'">';
			        	tpl +='<input id="'+initAuthority[i].childNode[j].id+'" pid="'+initAuthority[i].id+'" type="checkbox" value="checkbox"> '
			        	+initAuthority[i].childNode[j].menuName+"("+initAuthority[i].childNode[j].aliases+')</label>';
			        }else{
			        	tpl+='<label for="'+initAuthority[i].childNode[j].id+'">';
			        	tpl +='<input id="'+initAuthority[i].childNode[j].id+'" pid="'+initAuthority[i].id+'" type="checkbox" value="checkbox"> '
			        	+initAuthority[i].childNode[j].menuName+'</label>';
			        }
			 		tpl +='</div>';
			 		
			 		
			 		tpl +='<div class="row3">';
			 		if("childNode" in initAuthority[i].childNode[j]){
				 		var length3 = initAuthority[i].childNode[j].childNode.length;
				 		for(var k =0;k<length3;k++){
					       tpl +='<label for="'+initAuthority[i].childNode[j].childNode[k].id+'">'+
					        '<input id="'+initAuthority[i].childNode[j].childNode[k].id+'" pid="'+initAuthority[i].childNode[j].id+'" type="checkbox" value="checkbox"> '
					        +initAuthority[i].childNode[j].childNode[k].menuName+'</label>';
					 	}
			 		}else{
			 			tpl+="&nbsp";
			 		}
			 		tpl +='</div>';
			 	
			 		tpl +='</div>';
			 	}
		 	}
		 	
		 	tpl+='</div>';
		 }
		 tpl+='</div>';
		return tpl;
	}
	
	var columns = [[
	           	 {field:'userName',title:'用户姓名',width:'20%',align:'center'},    
	        	 {field:'userAccount',title:'用户账号',width:'15%',align:'center'}, 
	        	 {field:'contactNumber',title:'联系电话',width:'20%',align:'center'},
	        	 {field:'remarks',title:'备注',width:'15%',align:'center'},
	        	 {field:'createDate',title:'创建时间',width:'15%',align:'center',formatter:function(value,row,index){
	        		 return getYMDHMS(row.createDate);
	        	 }},
	        	 {field:'opt',title:'操作',width:'15%',align:'center', formatter:function(value,row,index){
                 	return "<button class='btn btn-edit'>修改</button><button class='btn btn-del'>删除</button><button class='btn btn-auth'>授权</button>";
                 }}
	            ]];
	var initDataGrid = function(){
		$("#dataGrid").datagrid({
			url : '/userInfo/list',
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
	 	    title: '新增用户',    
	 	    width: 400,    
	 	    height: 350,    
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
						operateVehicle('/userInfo/add', $(".main-form-content").serializeObject());
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
		var html = Mustache.render($('#edit_dialog_content_template').html(),row);
		var diaHtml = "<div id='main_dlg'>"+html+"</div>";
		$(diaHtml).dialog({    
	 	    title: '修改用户('+row.userName+')',    
	 	    width: 400,    
	 	    height: 300,    
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
						operateVehicle('/userInfo/edit', $(".main-form-content").serializeObject());
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
		$.messager.confirm("删除提示", "是否删除用户:"+row.userName+"的信息?",function(e){
		     if(e){
		    	 operateVehicle('/userInfo/delete', {userInfoId:row.id});
		     }
		 });
	}
	
	//获取已有权限
	var getAuthorityMenuByUserId = function(userId){
		$.ajax({
				url:"/userResources/getUserResources.do",
				traditional:true,
				type:'post',
				async:false,
				cache:false,
				data:{'userId':userId},
				success:function(data){
					if(null != data && data.length>0){
						for (var i = 0; i < data.length; i++) {
							$("#"+data[i]).attr("checked",true);
						}
					}
				},error:function(data){
					$.messager.alert({
	            	      title:'提示',
	            	      msg:'<div class="content">操作失败!</div>',
	            	      ok:'<i class="i-ok"></i> 确定',
	            	      icon:'error'
	            	    });
				}
			});
	}
	
	var loadMenu = function(){
		var maskObj = new mask();
		var row = $('#dataGrid').datagrid('getSelected');
		$.ajax({
			url:'/menuResources/findMenuList',
			type:"GET",
			dataType:'json',
			beforeSend : function (){
			    maskObj.showMask();// 显示遮蔽罩
		    }
		}).done(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			var tplInitAuthority = renderInitAuthority(data);
			$("#editUserAuthority").dialog("open");
			$("#editUserAuthority .content").html(tplInitAuthority);
			getAuthorityMenuByUserId(row.id);
		}).fail(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			$.messager.alert('操作提示','操作失败');
		});
	}
	//控制一级全选
	$(".content").on("change",".row1 > .th input",function(){
		if($(this).is(':checked')){
	        $(this).closest(".row1").find("input").prop("checked",true);
	    }else{
	    		$("#checkAll").prop("checked",false);
	         $(this).closest(".row1").find("input").prop("checked",false);
	    }
	})
	//控制二级全选
	$(".content").on("change",".row2 .th input",function(){
		if($(this).is(':checked')){
	        $(this).closest(".row2").find("input").prop("checked",true);
	        $(this).closest(".row1").find("> .th  input").prop("checked",true);
	    }else{
	    	$("#checkAll").prop("checked",false);
	         $(this).closest(".row2").find("input").prop("checked",false);
	    }
	})
	//控制三级选择
	$(".content").on("change",".row3 input",function(){
		if($(this).is(':checked')){
			$(this).closest(".row1").find("> .th  input").prop("checked",true);
	        $(this).closest(".row2").find(".th input").prop("checked",true);
	    }else{
	    	$("#checkAll").prop("checked",false);
	    }
	})
	
	//获取权限值
	var getAuthority = function(selector){
		var row = $('#dataGrid').datagrid('getSelected');
		var authority=[];
		$(selector).find(":input:checked").each(function(i,n){
			if($(n).attr("disabled")!="disabled"){
				var id = $(n).attr("id");
				var pid = $(n).attr("pid");
				//authority.push({"menuId":id,"userId":row.id});
				authority[authority.length] = {"menuId":id,"userId":row.id};
			}
		});
		return authority;
	}

	
	var saveUserAuthority = function(){
		var data = {};
		var row = $('#dataGrid').datagrid('getSelected');
		var userId = row.id;
		var authority = getAuthority("#editUserAuthority .content");//获取权限
		var maskObj = new mask();
		$.ajax({
			url:'/userResources/userAuthority.do',
			type:"post",
			data:{'userId':userId,'userResourcesStrList':JSON.stringify(authority)},
			beforeSend : function (){
			    maskObj.showMask();// 显示遮蔽罩
		    }
		}).done(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			if(data.status == 200){
				$('#editUserAuthority').dialog('close');
				$("#dataGrid").datagrid('reload');
				data.msg = '操作成功';
			}
			$.messager.alert('操作提示',data.msg);
		}).fail(function(data){
			maskObj.hideMask ();// 隐藏遮蔽罩
			$.messager.alert('操作提示','操作失败');
		});
		
	}
	var initializeUI = function(){
		initDataGrid();
		$('.dataTable-toolbar').delegate('button.btn-add','click',showAddDialog);
		$('.main-query-content').delegate('button.btn-search','click',initDataGrid);
		$('.main-dataTable-content').delegate('button.btn-edit','click',showUpdateDialog);
		$('.main-dataTable-content').delegate('button.btn-del','click',showDeleteDialog);
		$('.main-dataTable-content').delegate('button.btn-auth','click',loadMenu);
		$('#editUserAuthority').delegate('#submit','click', saveUserAuthority);
		
	}
	initializeUI();
	
})