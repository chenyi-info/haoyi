var loadVehicleGrid = function(){
	$('#plateNumber_view').combogrid({
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
               $('#plateNumber_view').combogrid("grid").datagrid("reload", {'plateNumber': q});
               $('#plateNumber_view').combogrid("setValue", q);
            }
        },onChange : function(newValue, oldValue){
        	var g = $('#plateNumber_view').combogrid('grid');	// get datagrid object
        	var r = g.datagrid('getSelected');
        	if(r != null){
        		$('#ownerName_view').textbox('setValue', r.ownerName);
            	$('#contactNumber_view').textbox('setValue', r.contactNumber);
        	}else{
        		$('#ownerName_view').textbox('setValue', '');
            	$('#contactNumber_view').textbox('setValue', '');
        	}
        }

	});
}

var loadCompanyGrid = function(){
	$('#companyName_view').combogrid({
		mode : 'remote',//远程连接方式  
        striped: true,
        method:'GET',//请求方式  
        dataType:'json',
        panelWidth: 600,
        url: '/customer/list',
        idField: 'companyName',
        textField: 'companyName',
        editable: true,
        pagination: true,//是否分页
        columns: [[
            {field:'companyName',title:'公司名称',width:'30%',align:'center'},    
	        {field:'contactName',title:'联系人姓名',width:'20%',align:'center'}, 
	        {field:'contactNumber',title:'联系人电话',width:'20%',align:'center'}, 
	    	{field:'remarks',title:'备注',width:'30%',align:'center'}
        ]],
        keyHandler:{
            up: function() {},
            down: function() {},
            enter: function() {},
            query: function(q) {
                //动态搜索
               $('#companyName_view').combogrid("grid").datagrid("reload", {'companyName': q});
               $('#companyName_view').combogrid("setValue", q);
            }
        },onChange : function(newValue, oldValue){
        	var g = $('#companyName_view').combogrid('grid');	// get datagrid object
        	var r = g.datagrid('getSelected');
        	if(r != null){
        		$('#customerId_form').val(r.id);
        		loadOperatorGrid(r.id);
        	}else{
        		$('#customerId_form').val('');
        	}
        	$('#operatorName_view').combogrid('setValue', '');
        }
	});
}

var loadOperatorGrid = function(companyId){
	var dataModel = {};
	dataModel.companyId = companyId;
	$('#operatorName_view').combogrid({
		mode : 'remote',//远程连接方式  
        striped: true,
        method:'GET',//请求方式  
        dataType:'json',
        panelWidth: 600,
        url: '/customer/operatorList',
        idField: 'contactName',
        textField: 'contactName',
        editable: true,
        pagination: true,//是否分页
        queryParams: dataModel,
        columns: [[
            {field:'contactName',title:'操作人姓名',width:'30%',align:'center'},   
		    {field:'contactNumber',title:'操作电话',width:'30%',align:'center'}, 
	    	{field:'remarks',title:'备注',width:'40%',align:'center'}
        ]],
        keyHandler:{
            up: function() {},
            down: function() {},
            enter: function() {},
            query: function(q) {
                //动态搜索
               $('#operatorName_view').combogrid("grid").datagrid("reload", {'contactName': q, 'companyId': companyId});
               $('#operatorName_view').combogrid("setValue", q);
            }
        },onChange : function(newValue, oldValue){
        	var g = $('#operatorName_view').combogrid('grid');	// get datagrid object
        	var r = g.datagrid('getSelected');
        	if(r != null){
        		$('#operatorId_form').val(r.id);
        	}else{
        		$('#operatorId_form').val('');
        	}
        	
        }

	});
}

var loadCustomerOrderGrid = function(){
	$('#orderNO_view').combogrid({
		mode : 'remote',//远程连接方式  
        striped: true,
        method:'GET',//请求方式  
        dataType:'json',
        panelWidth: 600,
        url: '/customerOrder/list',
        idField: 'orderNO',
        textField: 'orderNO',
        editable: true,
        pagination: true,//是否分页
        columns: [[
             {field:'orderDate',title:'订单日期',width:'10%',align:'center',formatter:function(value,row,index){
        		 return getYMDHMS(row.orderDate);
        	 }}, 
           	 {field:'companyName',title:'公司名称',width:'20%',align:'center'},  
           	 {field:'orderNO',title:'订单编号',width:'10%',align:'center'}, 
           	 {field:'cabinetModel',title:'柜型',width:'10%',align:'center'},
           	 {field:'settleStatus',title:'结算状态',width:'10%',align:'center', formatter:function(value,row,index){
             	return value == 1 ? '已结算' : '未结算';
             }},
           	 {field:'remarks',title:'备注',width:'30%',align:'center'}
        ]],
        keyHandler:{
            up: function() {},
            down: function() {},
            enter: function() {},
            query: function(q) {
                //动态搜索
               $('#orderNO_view').combogrid("grid").datagrid("reload", {'orderNO': q});
               $('#orderNO_view').combogrid("setValue", q);
            }
        },onChange : function(newValue, oldValue){
        	var g = $('#orderNO_view').combogrid('grid');	// get datagrid object
        	var r = g.datagrid('getSelected');
        	if(r != null){
        		$('#orderDate_view').textbox('setValue', r.orderDate);
            	$('#address_view').textbox('setValue', r.address);
            	$('#customerId_form').val(r.customerId);
            	$('#companyName_view').textbox('setValue', r.companyName);
        		loadOperatorGrid(r.customerId);
        	}else{
        		$('#orderDate_view').textbox('setValue', '');
            	$('#address_view').textbox('setValue', '');
            	$('#customerId_form').val('');
            	$('#companyName_view').textbox('setValue', '');
        	}
        }
	});
}

var loadOrderGrid = function(){
	$('#orderNO_view').combogrid({
		mode : 'remote',//远程连接方式  
        striped: true,
        method:'GET',//请求方式  
        dataType:'json',
        panelWidth: 650,
        url: '/order/list',
        idField: 'orderNO',
        textField: 'orderNO',
        editable: true,
        pagination: true,//是否分页
        columns: [[
                   {field:'orderDate',title:'订单日期',width:'10%',align:'center',formatter:function(value,row,index){
	        		 return getYMDHMS(row.orderDate);
	        	 }}, 
	        	 {field:'orderNO',title:'订单编号',width:'10%',align:'center'},
	        	 {field:'remarks',title:'备注',width:'10%',align:'center'},
	        	 {field:'cabinetModel',title:'柜型',width:'10%',align:'center'},
	        	 {field:'cabinetNumber',title:'柜号',width:'5%',align:'center'},
	        	 {field:'sealNumber',title:'封号',width:'5%',align:'center'},
	        	 {field:'address',title:'订单地址',width:'10%',align:'center'},
                 {field:'plateNumber',title:'车牌号',width:'10%',align:'center'},    
	        	 {field:'ownerName',title:'司机姓名',width:'5%',align:'center'}, 
	        	 {field:'companyName',title:'客户公司名称',width:'10%',align:'center'},
	        	 {field:'operatorName',title:'操作人',width:'10%',align:'center'},
	        	 {field:'orderStatus',title:'订单状态',width:'5%',align:'center', formatter:function(value,row,index){
                 	return value == 0 ? '正常' : '已取消';
                 }}
        ]],
        keyHandler:{
            up: function() {},
            down: function() {},
            enter: function() {},
            query: function(q) {
                //动态搜索
               $('#orderNO_view').combogrid("grid").datagrid("reload", {'orderNO': q});
               $('#orderNO_view').combogrid("setValue", q);
            }
        },onChange : function(newValue, oldValue){
        	var g = $('#orderNO_view').combogrid('grid');	// get datagrid object
        	var r = g.datagrid('getSelected');
        	if(r != null){
            	$('#orderId_form').val(r.id);
            	if($('#cabinetModel_view').length == 1){
            		$('#cabinetModel_view').textbox('setValue', r.cabinetModel);
            	}
            	if($('#cabinetNumber_view').length == 1){
            		$('#cabinetNumber_view').textbox('setValue', r.cabinetNumber);
            	}
            	if($('#sealNumber_view').length == 1){
            		$('#sealNumber_view').textbox('setValue', r.sealNumber);
            	}
            	if($('#address_view').length == 1){
            		$('#address_view').textbox('setValue', r.address);
            	}
        	}else{
            	$('#orderId_form').val('');
            	if($('#cabinetModel_view').length == 1){
            		$('#cabinetModel_view').textbox('setValue', '');
            	}
            	if($('#cabinetNumber_view').length == 1){
            		$('#cabinetNumber_view').textbox('setValue', '');
            	}
            	if($('#sealNumber_view').length == 1){
            		$('#sealNumber_view').textbox('setValue', '');
            	}
            	if($('#address_view').length == 1){
            		$('#address_view').textbox('setValue', '');
            	}
        	}
        }
	});
}

var loadDataDic = function(eId,dictionaryCode){
	var dataModel = {};
	dataModel.page = 1;
	dataModel.rows = 200;
	dataModel.dictionaryCode = dictionaryCode;
	$.ajax({
		url:'/dictionary/itemList',
		type:"GET",
		data:dataModel,
		dataType:'json'
	}).done(function(data){
		var dataList = [];
		if(eId.indexOf('_query') !=-1 ){
			dataList[dataList.length] = {'text':'全部','value':''};
		}
		$(data.rows).each(function(i, v){
			dataList[dataList.length] = {'text':v.itemName,'value':v.itemName};
		});
		$('#'+eId).combobox({
			data:dataList,
			editable:false
		});
		var val = $('#'+eId).combobox('getValue');
		var selectedVal = $('#'+eId).attr('defaultSel');
		if((val == '' || val == null) && selectedVal){
			$('#'+eId).combobox('select', selectedVal);
		}
	}).fail(function(data){
		$.messager.alert('操作提示','获取数据字典失败');
	});
}


var initDataDic = function(){
	$('.main-form-content input[datadic]').each(function(i,v){
		loadDataDic($(v).attr('id'),$(v).attr('datadic'));
	});
}

var initQueryDataDic = function(){
	$('.main-query-content input[datadic]').each(function(i,v){
		loadDataDic($(v).attr('id'),$(v).attr('datadic'));
	});
}

$(function(){
	var initializeUI = function(){
		$('.main-view-content').height($(window).height());
		var mainH = $('.main-view-content').height();
		var otherH = 0;
		$('.main-view-content>div').each(function(i,v){
			if($(v).attr('class') == 'main-dataTable-content') {
				return true;
			}
			otherH += $(v).height();
		});
		$(".main-dataTable-content").css("height", mainH-otherH-20 + "px");
	}
	initializeUI();
})