package com.hy.otw.service.customer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.dao.customer.OrderOtherAmtDao;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.OrderOtherAmtPo;
import com.hy.otw.po.OrderPo;
import com.hy.otw.service.order.OrderService;
import com.hy.otw.vo.OrderOtherAmtVo;
import com.hy.otw.vo.OrderVo;
import com.hy.otw.vo.UserInfoVo;
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
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		if(orderOtherAmtVo.getPropertyType() == 1){//归属类型 1-司机 2-客户 3-自己
			orderOtherAmtVo.setTargetName(orderVo.getPlateNumber());
		}else if(orderOtherAmtVo.getPropertyType() == 2){
			orderOtherAmtVo.setTargetId(orderVo.getCustomerId());
			orderOtherAmtVo.setTargetName(orderVo.getCompanyName());
		}else if(orderOtherAmtVo.getPropertyType() == 3){
			orderOtherAmtVo.setTargetId(loginUser.getId());
			orderOtherAmtVo.setTargetName(loginUser.getUserName());
		}
		
		Date date = new Date();
		OrderOtherAmtPo orderOtherAmtPo = new OrderOtherAmtPo();
		orderOtherAmtVo.setCreateBy(loginUser.getId());
		orderOtherAmtVo.setCreateDate(date);
		orderOtherAmtVo.setUpdateBy(loginUser.getId());
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
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		if(orderOtherAmtVo.getPropertyType() == 1){//归属类型 1-司机 2-客户 3-自己
			orderOtherAmtVo.setTargetName(orderVo.getPlateNumber());
		}else if(orderOtherAmtVo.getPropertyType() == 2){
			orderOtherAmtVo.setTargetId(orderVo.getCustomerId());
			orderOtherAmtVo.setTargetName(orderVo.getCompanyName());
		}else if(orderOtherAmtVo.getPropertyType() == 3){
			orderOtherAmtVo.setTargetId(loginUser.getId());
			orderOtherAmtVo.setTargetName(loginUser.getUserName());
		}
		OrderOtherAmtPo orderOtherAmtPo = orderOtherAmtDao.getOrderOtherAmt(orderOtherAmtVo.getId());
		if(orderOtherAmtPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		if(orderOtherAmtVo.getIsSettle() == 0 && orderOtherAmtPo.getSettleDate() == null) {
			orderOtherAmtPo.setSettleDate(date);
		}
		orderOtherAmtVo.setSettleDate(orderOtherAmtPo.getSettleDate());
		orderOtherAmtVo.setCreateBy(orderOtherAmtPo.getCreateBy());
		orderOtherAmtVo.setCreateDate(orderOtherAmtPo.getCreateDate());
		orderOtherAmtVo.setUpdateBy(loginUser.getId());
		orderOtherAmtVo.setUpdateDate(date);
		orderOtherAmtVo.setDelStatus(orderOtherAmtPo.getDelStatus());
		PropertyUtils.copyProperties(orderOtherAmtPo, orderOtherAmtVo);
		this.orderOtherAmtDao.editOrderOtherAmt(orderOtherAmtPo);
	}

	public Long deleteOrderOtherAmt(Long orderOtherAmtId) throws Exception {
		Long orderId = null;
		OrderOtherAmtPo orderOtherAmtPo = orderOtherAmtDao.getOrderOtherAmt(orderOtherAmtId);
		if(orderOtherAmtPo == null){
			throw new Exception("未找到该条信息");
		}
		orderId = orderOtherAmtPo.getOrderId();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		orderOtherAmtPo.setDelStatus(DelStatusEnum.HIDE.getValue());
		orderOtherAmtPo.setUpdateBy(loginUser.getId());
		orderOtherAmtPo.setUpdateDate(date);
		this.orderOtherAmtDao.editOrderOtherAmt(orderOtherAmtPo);
		return orderId;
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

	public void batchSettles(List<Long> orderOtherAmtIdList) {
		this.orderOtherAmtDao.batchSettles(orderOtherAmtIdList);
		List<OrderOtherAmtPo> orderOtherAmtPoList = this.orderOtherAmtDao.findOrderOtherAmtListByIds(orderOtherAmtIdList);
		if(CollectionUtils.isNotEmpty(orderOtherAmtPoList)){
			for (OrderOtherAmtPo orderOtherAmtPo : orderOtherAmtPoList) {
				this.statisticalAmount(orderOtherAmtPo.getOrderId());
			}
		}
	}

	public List<OrderOtherAmtVo> findOrderOtherAmtList(List<Long> orderIdlist, Integer propertyType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<OrderOtherAmtPo> orderOtherAmtPoList = this.orderOtherAmtDao.findOrderOtherAmtList(orderIdlist, propertyType);
		List<OrderOtherAmtVo> orderOtherAmtVoList = new ArrayList<OrderOtherAmtVo>();
		if(CollectionUtils.isNotEmpty(orderOtherAmtPoList)){
			for (OrderOtherAmtPo orderOtherAmtPo : orderOtherAmtPoList) {
				OrderOtherAmtVo orderOtherAmtVo = new OrderOtherAmtVo();
				PropertyUtils.copyProperties(orderOtherAmtVo, orderOtherAmtPo);
				orderOtherAmtVoList.add(orderOtherAmtVo);
			}
		}
		return orderOtherAmtVoList;
	}

	public String getItemsText(List<OrderOtherAmtVo> orderOtherAmtVoList, Long orderId, int settleStatus) {
		StringBuffer itemsText = new StringBuffer();
		for (OrderOtherAmtVo orderOtherAmtVo : orderOtherAmtVoList) {
			if(orderOtherAmtVo.getOrderId().equals(orderId) && orderOtherAmtVo.getIsSettle() == settleStatus){
				itemsText.append(orderOtherAmtVo.getItemName()).append(":").append(orderOtherAmtVo.getPrice()).append("元").append("\r\n");
			}
		}
		return itemsText.toString();
	}

}
