package com.hy.otw.controller.order;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.order.OrderService;
import com.hy.otw.utils.DownloadUtils;
import com.hy.otw.utils.ExcelUtil;
import com.hy.otw.vo.OrderVo;
import com.hy.otw.vo.http.ResponseMsgVo;
import com.hy.otw.vo.query.OrderQueryVo;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Resource private OrderService orderService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseMsgVo add(HttpServletRequest request,HttpServletResponse response, OrderVo orderVo, Boolean hasConfirm) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			msg = this.orderService.addOrder(orderVo, hasConfirm);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMsgVo edit(HttpServletRequest request,HttpServletResponse response, OrderVo orderVo, Boolean hasConfirm){
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			msg = this.orderService.editOrder(orderVo, hasConfirm);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseMsgVo delete(HttpServletRequest request,HttpServletResponse response, Long orderId){
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.orderService.deleteOrder(orderId);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, OrderQueryVo orderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.orderService.findOrderList(orderQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/loadExcel", method = RequestMethod.POST)
	public void loadExcel(HttpServletRequest request,HttpServletResponse response, OrderQueryVo orderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		orderQueryVo.setPage(1);
		orderQueryVo.setRows(Integer.MAX_VALUE);
		Pagination pagination = this.orderService.findOrderList(orderQueryVo);
		List<OrderVo> orderVoList = (List<OrderVo>) pagination.getRows();
		List<JSONObject> orderList = new ArrayList<JSONObject>();
		if(CollectionUtils.isNotEmpty(orderVoList)){
			for (OrderVo orderVo : orderVoList) {
				JSONObject obj = JSONObject.parseObject(JSONObject.toJSON(orderVo).toString());
				obj.put("orderStatusStr", orderVo.getOrderStatus() == 0 ? "正常" : "已取消");
				orderList.add(obj);
			}
		}
		OutputStream outputStream = null;
        try {
			String fileName = "订单管理";
			String[] fields = { "orderDate", "orderNO",	"demand", "cabinetModel", "cabinetNumber", "sealNumber", "address", "weighed", "orderPrice", "plateNumber", "ownerName", "contactNumber", "otherAmt", "companyName", "operatorName", "orderStatusStr", "remarks", "createDate" };
			String[] titles = { "订单日期", "订单编号", "订单要求", "柜型", "柜号", "封号", "订单简址", "重量(T)", "订单金额", "车牌号", "司机姓名", "联系电话", "杂费金额", "客户公司名称", "操作人", "订单状态", "备注", "创建时间" };
			File file = ExcelUtil.export(null, fileName, fields, titles, orderList, null);
			DownloadUtils.downloadExcel(request, response, file, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
	}

}