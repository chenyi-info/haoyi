package com.hy.otw.service.customer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.dao.customer.OrderDao;
import com.hy.otw.dao.customer.OrderOtherAmtDao;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.OrderOtherAmtPo;
import com.hy.otw.po.OrderPo;
import com.hy.otw.service.order.OrderService;
import com.hy.otw.vo.OrderOtherAmtVo;
import com.hy.otw.vo.OrderVo;
import com.hy.otw.vo.query.OrderOtherAmtQueryVo;

@Service
public class OrderOtherAmtService {
	
	@Resource private OrderOtherAmtDao orderOtherAmtDao;
	@Resource private OrderService orderService;

	public void addOrderOtherAmt(OrderOtherAmtVo orderOtherAmtVo) throws Exception {
		OrderVo orderVo = this.orderService.getOrderInfoById(orderOtherAmtVo.getOrderId());
		if(orderVo == null){
			throw new Exception("未找到订单信息orderId:"+orderOtherAmtVo.getOrderId());
		}
		if(orderOtherAmtVo.getPropertyType() == 1){//归属类型 1-司机 2-客户 3-自己
			orderOtherAmtVo.setTargetName(orderVo.getPlateNumber());
		}else if(orderOtherAmtVo.getPropertyType() == 2){
			orderOtherAmtVo.setTargetId(orderVo.getCustomerId());
			orderOtherAmtVo.setTargetName(orderVo.getCompanyName());
		}else if(orderOtherAmtVo.getPropertyType() == 3){
			orderOtherAmtVo.setTargetId(1l);
			orderOtherAmtVo.setTargetName("郝意");
		}
		Date date = new Date();
		OrderOtherAmtPo orderOtherAmtPo = new OrderOtherAmtPo();
		orderOtherAmtVo.setCreateBy(1l);
		orderOtherAmtVo.setCreateDate(date);
		orderOtherAmtVo.setUpdateBy(1l);
		orderOtherAmtVo.setUpdateDate(date);
		orderOtherAmtVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		PropertyUtils.copyProperties(orderOtherAmtPo, orderOtherAmtVo);
		this.orderOtherAmtDao.addOrderOtherAmt(orderOtherAmtPo);
		this.orderOtherAmtDao.statisticalAmount(orderOtherAmtPo.getOrderId());
	}

	public Pagination findOrderOtherAmtList(OrderOtherAmtQueryVo orderOtherAmtQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.orderOtherAmtDao.findOrderOtherAmtList(orderOtherAmtQueryVo);
		List<OrderOtherAmtPo> orderOtherAmtPoList = (List<OrderOtherAmtPo>) pagination.getRows();
		List<OrderOtherAmtVo> orderOtherAmtVoList = new ArrayList<OrderOtherAmtVo>();
		if(CollectionUtils.isNotEmpty(orderOtherAmtPoList)){
			for (OrderOtherAmtPo orderOtherAmtPo : orderOtherAmtPoList) {
				OrderOtherAmtVo orderOtherAmtVo = new OrderOtherAmtVo();
				PropertyUtils.copyProperties(orderOtherAmtVo, orderOtherAmtPo);
				orderOtherAmtVoList.add(orderOtherAmtVo);
			}
		}
		pagination.setRows(orderOtherAmtVoList);
		return pagination;
	}

	public void editOrderOtherAmt(OrderOtherAmtVo orderOtherAmtVo) throws Exception {
		OrderVo orderVo = this.orderService.getOrderInfoById(orderOtherAmtVo.getOrderId());
		if(orderVo == null){
			throw new Exception("未找到订单信息orderId:"+orderOtherAmtVo.getOrderId());
		}
		if(orderOtherAmtVo.getPropertyType() == 1){//归属类型 1-司机 2-客户 3-自己
			orderOtherAmtVo.setTargetName(orderVo.getPlateNumber());
		}else if(orderOtherAmtVo.getPropertyType() == 2){
			orderOtherAmtVo.setTargetId(orderVo.getCustomerId());
			orderOtherAmtVo.setTargetName(orderVo.getCompanyName());
		}else if(orderOtherAmtVo.getPropertyType() == 3){
			orderOtherAmtVo.setTargetId(1l);
			orderOtherAmtVo.setTargetName("郝意");
		}
		OrderOtherAmtPo orderOtherAmtPo = orderOtherAmtDao.getOrderOtherAmt(orderOtherAmtVo.getId());
		if(orderOtherAmtPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		orderOtherAmtVo.setCreateBy(orderOtherAmtPo.getCreateBy());
		orderOtherAmtVo.setCreateDate(orderOtherAmtPo.getCreateDate());
		orderOtherAmtVo.setUpdateBy(1l);
		orderOtherAmtVo.setUpdateDate(date);
		orderOtherAmtVo.setDelStatus(orderOtherAmtPo.getDelStatus());
		PropertyUtils.copyProperties(orderOtherAmtPo, orderOtherAmtVo);
		this.orderOtherAmtDao.editOrderOtherAmt(orderOtherAmtPo);
	}

	public void deleteOrderOtherAmt(Long orderOtherAmtId) throws Exception {
		OrderOtherAmtPo orderOtherAmtPo = orderOtherAmtDao.getOrderOtherAmt(orderOtherAmtId);
		if(orderOtherAmtPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		orderOtherAmtPo.setDelStatus(DelStatusEnum.HIDE.getValue());
		orderOtherAmtPo.setUpdateBy(1l);
		orderOtherAmtPo.setUpdateDate(date);
		this.orderOtherAmtDao.editOrderOtherAmt(orderOtherAmtPo);
	}

	public void deleteOrder(Long orderId) {
		this.orderOtherAmtDao.deleteOrder(orderId);
	}

	public void editOrderInfo(OrderPo orderPo) {
		this.orderOtherAmtDao.editOrderInfo(orderPo);
	}

	public void statisticalAmount(Long orderId) {
		this.orderOtherAmtDao.statisticalAmount(orderId);
	}

}