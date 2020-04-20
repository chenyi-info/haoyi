package com.hy.otw.service.customer;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.common.enums.SettleStatusEnum;
import com.hy.otw.dao.customer.CustomerOrderDao;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.CustomerOrderPo;
import com.hy.otw.po.OrderPo;
import com.hy.otw.vo.CustomerOrderVo;
import com.hy.otw.vo.UserInfoVo;
import com.hy.otw.vo.query.CustomerOrderQueryVo;

@Service
public class CustomerOrderService {
	
	@Resource private CustomerOrderDao customerOrderDao;

	public void addCustomerOrder(OrderPo orderPo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		CustomerOrderVo customerOrderVo = new CustomerOrderVo();
		customerOrderVo.setOrderId(orderPo.getId());
		customerOrderVo.setAddress(orderPo.getAddress());
		customerOrderVo.setCabinetModel(orderPo.getCabinetModel());
		customerOrderVo.setCabinetNumber(orderPo.getCabinetNumber());
		customerOrderVo.setCompanyName(orderPo.getCompanyName());
		customerOrderVo.setCustomerId(orderPo.getCustomerId());
		customerOrderVo.setDelStatus(orderPo.getDelStatus());
		customerOrderVo.setOrderDate(orderPo.getOrderDate());
		customerOrderVo.setOrderNO(orderPo.getOrderNO());
		customerOrderVo.setSealNumber(orderPo.getSealNumber());
		customerOrderVo.setCustomerPrice(orderPo.getCustomerPrice());
		customerOrderVo.setSettleStatus(SettleStatusEnum.UNDONE.getValue());
		customerOrderVo.setCreateBy(orderPo.getCreateBy());
		customerOrderVo.setCreateDate(orderPo.getCreateDate());
		customerOrderVo.setUpdateBy(orderPo.getUpdateBy());
		customerOrderVo.setUpdateDate(orderPo.getUpdateDate());
		customerOrderVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		CustomerOrderPo customerOrderPo = new CustomerOrderPo();
		PropertyUtils.copyProperties(customerOrderPo, customerOrderVo);
		this.customerOrderDao.addCustomerOrder(customerOrderPo);
	}

	public Pagination findCustomerOrderList(CustomerOrderQueryVo customerOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.customerOrderDao.findCustomerOrderList(customerOrderQueryVo);
		List<CustomerOrderPo> customerOrderPoList = (List<CustomerOrderPo>) pagination.getRows();
		List<CustomerOrderVo> customerOrderVoList = new ArrayList<CustomerOrderVo>();
		if(CollectionUtils.isNotEmpty(customerOrderPoList)){
			for (CustomerOrderPo customerOrderPo : customerOrderPoList) {
				CustomerOrderVo customerOrderVo = new CustomerOrderVo();
				PropertyUtils.copyProperties(customerOrderVo, customerOrderPo);
				customerOrderVoList.add(customerOrderVo);
			}
		}
		pagination.setRows(customerOrderVoList);
		return pagination;
	}

	public void editOrderInfo(OrderPo orderPo) throws Exception {
		CustomerOrderPo customerOrderPo = customerOrderDao.getCustomerOrderByOrderId(orderPo.getId());
		if(customerOrderPo == null){
			throw new Exception("未找到该条信息");
		}
		if(customerOrderPo.getSettleStatus() == 2) {
			throw new Exception("锁定客户订单不允许修改");
		}
		customerOrderPo.setOrderId(orderPo.getId());
		customerOrderPo.setAddress(orderPo.getAddress());
		customerOrderPo.setCustomerPrice(orderPo.getCustomerPrice());
		customerOrderPo.setCabinetModel(orderPo.getCabinetModel());
		customerOrderPo.setCabinetNumber(orderPo.getCabinetNumber());
		customerOrderPo.setCompanyName(orderPo.getCompanyName());
		customerOrderPo.setCustomerId(orderPo.getCustomerId());
		customerOrderPo.setDelStatus(orderPo.getDelStatus());
		customerOrderPo.setOrderDate(orderPo.getOrderDate());
		customerOrderPo.setOrderNO(orderPo.getOrderNO());
		customerOrderPo.setSealNumber(orderPo.getSealNumber());
		customerOrderPo.setUpdateBy(orderPo.getUpdateBy());
		customerOrderPo.setUpdateDate(orderPo.getUpdateDate());
		this.customerOrderDao.editCustomerOrder(customerOrderPo);
	}

	public void editCustomerOrder(CustomerOrderVo customerOrderVo) throws Exception {
		CustomerOrderPo customerOrderPo = customerOrderDao.getCustomerOrder(customerOrderVo.getId());
		if(customerOrderPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		if(customerOrderPo.getSettleStatus() == 2) {
			throw new Exception("锁定客户订单不允许修改");
		}
		if(customerOrderVo.getSettleStatus() == 1 && customerOrderPo.getSettleDate() == null) {
			customerOrderPo.setSettleDate(date);
		}
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		customerOrderPo.setRemarks(customerOrderVo.getRemarks());
		customerOrderPo.setSettleStatus(customerOrderVo.getSettleStatus());
		customerOrderPo.setUpdateBy(loginUser.getId());
		customerOrderPo.setUpdateDate(date);
		this.customerOrderDao.editCustomerOrder(customerOrderPo);
	}
	
	public void deleteCustomerOrder(Long orderId) throws Exception {
		CustomerOrderPo customerOrderPo = customerOrderDao.getCustomerOrderByOrderId(orderId);
		if(customerOrderPo != null){
			UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
			Date date = new Date();
			customerOrderPo.setDelStatus(DelStatusEnum.HIDE.getValue());
			customerOrderPo.setUpdateBy(loginUser.getId());
			customerOrderPo.setUpdateDate(date);
			this.customerOrderDao.editCustomerOrder(customerOrderPo);
		}
	}

	public BigDecimal findCustomerOrderTotalAmt(CustomerOrderQueryVo customerOrderQueryVo) {
		return this.customerOrderDao.findCustomerOrderTotalAmt(customerOrderQueryVo);
	}

	public void batchSettles(List<Long> customerOrderIdList) {
		this.customerOrderDao.batchSettles(customerOrderIdList);
	}

	public void batchLockOrUnLock(List<Long> customerOrderIdList, int status) {
		this.customerOrderDao.batchLockOrUnLock(customerOrderIdList, status);
	}

	public CustomerOrderPo getCustomerOrderByOrderId(Long orderId) {
		return customerOrderDao.getCustomerOrderByOrderId(orderId);
	}

}
