package com.hy.otw.service.order;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.dao.customer.OrderDao;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.OrderPo;
import com.hy.otw.service.customer.CustomerOrderService;
import com.hy.otw.service.customer.OrderOtherAmtService;
import com.hy.otw.service.driver.DriverOrderService;
import com.hy.otw.vo.OrderVo;
import com.hy.otw.vo.UserInfoVo;
import com.hy.otw.vo.http.ResponseMsgVo;
import com.hy.otw.vo.query.OrderQueryVo;

@Service
public class OrderService {
	
	@Resource private OrderDao orderDao;
	@Resource private CustomerOrderService customerOrderService;
	@Resource private DriverOrderService driverOrderService;
	@Resource private OrderOtherAmtService orderOtherAmtService;
	
	public ResponseMsgVo addOrder(OrderVo orderVo, Boolean hasConfirm) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ResponseMsgVo msgVo = new ResponseMsgVo();
		Boolean hasOrder = BooleanUtils.isNotTrue(hasConfirm);
		if(BooleanUtils.isNotTrue(hasConfirm)){
			hasOrder = orderDao.checkHasOrder(orderVo);
		}
		if(hasOrder){//如果存在重复订单信息
			msgVo.setMsg("订单信息已存在,是否重复添加该信息?");
			msgVo.setStatusCode("hasOrder");
			return msgVo;
		}
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		OrderPo orderPo = new OrderPo();
		orderVo.setCreateBy(loginUser.getId());
		orderVo.setCreateDate(date);
		orderVo.setUpdateBy(loginUser.getId());
		orderVo.setUpdateDate(date);
		orderVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		PropertyUtils.copyProperties(orderPo, orderVo);
		this.orderDao.addOrder(orderPo);
		this.customerOrderService.addCustomerOrder(orderPo);
		this.driverOrderService.addDriverOrder(orderPo);
		return msgVo;
	}

	public Pagination findOrderList(OrderQueryVo orderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.orderDao.findOrderList(orderQueryVo);
		List<OrderPo> orderPoList = (List<OrderPo>) pagination.getRows();
		List<OrderVo> orderVoList = new ArrayList<OrderVo>();
		if(CollectionUtils.isNotEmpty(orderPoList)){
			for (OrderPo orderPo : orderPoList) {
				OrderVo orderVo = new OrderVo();
				PropertyUtils.copyProperties(orderVo, orderPo);
				orderVoList.add(orderVo);
			}
		}
		pagination.setRows(orderVoList);
		return pagination;
	}

	public ResponseMsgVo editOrder(OrderVo orderVo, Boolean hasConfirm) throws Exception {
		ResponseMsgVo msgVo = new ResponseMsgVo();
		msgVo.setStatus(200);
		Boolean hasOrder = BooleanUtils.isFalse(hasConfirm);
		if(BooleanUtils.isFalse(hasConfirm)){
			hasOrder = orderDao.checkHasOrder(orderVo);
		}
		if(hasOrder){//如果存在重复订单信息
			msgVo.setMsg("订单信息已存在,是否重复添加该信息?");
			msgVo.setStatusCode("hasOrder");
			return msgVo;
		}
		OrderPo orderPo = orderDao.getOrder(orderVo.getId());
		if(orderPo == null){
			throw new Exception("未找到该条信息");
		}
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		orderVo.setOtherAmt(orderPo.getOtherAmt());
		orderVo.setCreateBy(orderPo.getCreateBy());
		orderVo.setCreateDate(orderPo.getCreateDate());
		orderVo.setUpdateBy(loginUser.getId());
		orderVo.setUpdateDate(date);
		orderVo.setDelStatus(orderPo.getDelStatus());
		PropertyUtils.copyProperties(orderPo, orderVo);
		this.orderDao.editOrder(orderPo);
		this.customerOrderService.editOrderInfo(orderPo);
		this.driverOrderService.editOrderInfo(orderPo);
		this.orderOtherAmtService.editOrderInfo(orderPo);
		return msgVo;
	}

	public void deleteOrder(Long orderId) throws Exception {
		OrderPo orderPo = orderDao.getOrder(orderId);
		if(orderPo == null){
			throw new Exception("未找到该条信息");
		}
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		orderPo.setDelStatus(DelStatusEnum.HIDE.getValue());
		orderPo.setUpdateBy(loginUser.getId());
		orderPo.setUpdateDate(date);
		this.orderDao.editOrder(orderPo);
		this.customerOrderService.deleteCustomerOrder(orderId);
		this.driverOrderService.deleteDriverOrder(orderId);
		this.orderOtherAmtService.deleteOrder(orderId);
	}

	public OrderVo getOrderInfoById(Long orderId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		OrderPo orderPo = orderDao.getOrder(orderId);
		OrderVo orderVo = new OrderVo();
		PropertyUtils.copyProperties(orderVo, orderPo);
		return orderVo;
	}

}
