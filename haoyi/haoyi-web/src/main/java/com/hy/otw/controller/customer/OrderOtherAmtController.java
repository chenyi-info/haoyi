package com.hy.otw.controller.customer;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.customer.OrderOtherAmtService;
import com.hy.otw.utils.DownloadUtils;
import com.hy.otw.utils.ExcelUtil;
import com.hy.otw.vo.OrderOtherAmtVo;
import com.hy.otw.vo.query.OrderOtherAmtQueryVo;

@RestController
@RequestMapping("/orderOtherAmt")
public class OrderOtherAmtController {
	
	@Resource private OrderOtherAmtService orderOtherAmtService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(HttpServletRequest request,HttpServletResponse response, OrderOtherAmtVo orderOtherAmtVo) throws Exception {
		if(orderOtherAmtVo.getPropertyTypes().length > 0){
			for (String propertyType : orderOtherAmtVo.getPropertyTypes()) {
				orderOtherAmtVo.setPropertyType(Integer.valueOf(propertyType));
				this.orderOtherAmtService.addOrderOtherAmt(orderOtherAmtVo);
			}
		}else{
			this.orderOtherAmtService.addOrderOtherAmt(orderOtherAmtVo);
		}
		
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request,HttpServletResponse response, OrderOtherAmtVo orderOtherAmtVo) throws Exception {
		this.orderOtherAmtService.editOrderOtherAmt(orderOtherAmtVo);
		this.orderOtherAmtService.statisticalAmount(orderOtherAmtVo.getOrderId());
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response, Long orderOtherAmtId) throws Exception {
		Long orderId = this.orderOtherAmtService.deleteOrderOtherAmt(orderOtherAmtId);
		this.orderOtherAmtService.statisticalAmount(orderId);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, OrderOtherAmtQueryVo orderOtherAmtQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.orderOtherAmtService.findOrderOtherAmtList(orderOtherAmtQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/batchSettles", method = RequestMethod.POST)
	public void batchSettles(HttpServletRequest request,HttpServletResponse response, Long[] orderOtherAmtIds) throws Exception {
		List<Long> orderOtherAmtIdList = Arrays.asList(orderOtherAmtIds);
		this.orderOtherAmtService.batchSettles(orderOtherAmtIdList);
	}
	
	@RequestMapping(value = "/loadExcel", method = RequestMethod.POST)
	public void loadExcel(HttpServletRequest request,HttpServletResponse response, OrderOtherAmtQueryVo orderOtherAmtQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		orderOtherAmtQueryVo.setPage(1);
		orderOtherAmtQueryVo.setRows(Integer.MAX_VALUE);
		Pagination pagination = this.orderOtherAmtService.findOrderOtherAmtList(orderOtherAmtQueryVo);
		List<OrderOtherAmtVo> orderOtherAmtVoList =(List<OrderOtherAmtVo>) pagination.getRows();
		List<JSONObject> orderOtherAmtList = new ArrayList<JSONObject>();
		if(CollectionUtils.isNotEmpty(orderOtherAmtVoList)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (OrderOtherAmtVo orderOtherAmtVo : orderOtherAmtVoList) {
				JSONObject obj = JSONObject.parseObject(JSONObject.toJSON(orderOtherAmtVo).toString());
				obj.put("expenditureDateStr", sdf.format(orderOtherAmtVo.getExpenditureDate()));
				obj.put("createDateStr", sdf.format(orderOtherAmtVo.getCreateDate()));
				obj.put("propertyTypeStr", orderOtherAmtVo.getPropertyType() == 1 ? "司机" : orderOtherAmtVo.getPropertyType() == 2 ? "客户" : "自己");
				obj.put("isSettleStr", orderOtherAmtVo.getIsSettle() == null ? "--" : orderOtherAmtVo.getIsSettle() == 0 ? "未结算" : "已结算");
				orderOtherAmtList.add(obj);
			}
		}
		OutputStream outputStream = null;
        try {
			String fileName = "订单杂费管理";
			String[] fields = { "expenditureDateStr", "orderNO", "price", "targetName", "cabinetNumber", "address", "itemName", "propertyTypeStr", "isSettleStr", "remarks", "createDateStr" };
			String[] titles = { "支出日期", "订单编号", "支付金额", "支出对象", "柜号", "订单简址", "支付项目类型", "归属类型", "结算状态", "备注", "创建时间" };
			File file = ExcelUtil.export(null, fileName, fields, titles, orderOtherAmtList, null);
			DownloadUtils.downloadExcel(request, response, file, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
	}

}
