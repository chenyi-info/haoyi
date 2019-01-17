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
import com.hy.otw.dao.customer.CustomerDao;
import com.hy.otw.dao.customer.CustomerOperatorDao;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.CustomerOperatorPo;
import com.hy.otw.po.CustomerPo;
import com.hy.otw.vo.CustomerOperatorVo;
import com.hy.otw.vo.CustomerVo;
import com.hy.otw.vo.UserInfoVo;
import com.hy.otw.vo.query.CustomerQueryVo;

@Service
public class CustomerService {
	
	@Resource private CustomerDao customerDao;
	@Resource private CustomerOperatorDao customerOperatorDao;
	
	public void addCustomer(CustomerVo customerVo) throws Exception {
		CustomerPo customerPo = this.customerDao.getCustomerByCompanyName(customerVo.getCompanyName());
		if(customerPo != null){
			throw new Exception("公司不能重复");
		}
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		customerPo = new CustomerPo();
		customerVo.setCreateBy(loginUser.getId());
		customerVo.setCreateDate(date);
		customerVo.setUpdateBy(loginUser.getId());
		customerVo.setUpdateDate(date);
		customerVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		PropertyUtils.copyProperties(customerPo, customerVo);
		this.customerDao.addCustomer(customerPo);
	}

	public Pagination findCustomerList(CustomerQueryVo customerQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.customerDao.findCustomerList(customerQueryVo);
		List<CustomerPo> customerPoList = (List<CustomerPo>) pagination.getRows();
		List<CustomerVo> customerVoList = new ArrayList<CustomerVo>();
		if(CollectionUtils.isNotEmpty(customerPoList)){
			for (CustomerPo customerPo : customerPoList) {
				CustomerVo customerVo = new CustomerVo();
				PropertyUtils.copyProperties(customerVo, customerPo);
				customerVoList.add(customerVo);
			}
		}
		pagination.setRows(customerVoList);
		return pagination;
	}

	public void editCustomer(CustomerVo customerVo) throws Exception {
		
		CustomerPo customerPo = this.customerDao.getCustomerByCompanyName(customerVo.getCompanyName());
		if(customerPo != null && customerPo.getId() != customerVo.getId()){
			throw new Exception("公司名称不能重复");
		}
		if(customerPo == null){
		 customerPo = customerDao.getCustomer(customerVo.getId());
		}
		if(customerPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		customerVo.setCreateBy(customerPo.getCreateBy());
		customerVo.setCreateDate(customerPo.getCreateDate());
		customerVo.setUpdateBy(loginUser.getId());
		customerVo.setUpdateDate(date);
		customerVo.setDelStatus(customerPo.getDelStatus());
		PropertyUtils.copyProperties(customerPo, customerVo);
		this.customerDao.editCustomer(customerPo);
	}

	public void deleteCustomer(Long customerId) throws Exception {
		CustomerPo customerPo = customerDao.getCustomer(customerId);
		if(customerPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		customerPo.setDelStatus(DelStatusEnum.HIDE.getValue());
		customerPo.setUpdateBy(loginUser.getId());
		customerPo.setUpdateDate(date);
		this.customerDao.editCustomer(customerPo);
	}

	public Pagination findCustomerOperatorList(CustomerQueryVo customerQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.customerOperatorDao.findCustomerOperatorList(customerQueryVo);
		List<CustomerOperatorPo> customerOperatorPoList = (List<CustomerOperatorPo>) pagination.getRows();
		List<CustomerOperatorVo> customerOperatorVoList = new ArrayList<CustomerOperatorVo>();
		if(CollectionUtils.isNotEmpty(customerOperatorPoList)){
			for (CustomerOperatorPo customerOperatorPo : customerOperatorPoList) {
				CustomerOperatorVo customerOperatorVo = new CustomerOperatorVo();
				PropertyUtils.copyProperties(customerOperatorVo, customerOperatorPo);
				customerOperatorVoList.add(customerOperatorVo);
			}
		}
		pagination.setRows(customerOperatorVoList);
		return pagination;
	}

	public void addCustomerOperator(CustomerOperatorVo customerOperatorVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		CustomerOperatorPo customerOperatorPo = new CustomerOperatorPo();
		customerOperatorVo.setCreateBy(loginUser.getId());
		customerOperatorVo.setCreateDate(date);
		customerOperatorVo.setUpdateBy(loginUser.getId());
		customerOperatorVo.setUpdateDate(date);
		customerOperatorVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		PropertyUtils.copyProperties(customerOperatorPo, customerOperatorVo);
		this.customerOperatorDao.addCustomerOperator(customerOperatorPo);
	}
	
	public void editCustomerOperator(CustomerOperatorVo customerOperatorVo) throws Exception {
		CustomerOperatorPo customerOperatorPo = customerOperatorDao.getCustomerOperator(customerOperatorVo.getId());
		if(customerOperatorPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		customerOperatorVo.setCreateBy(customerOperatorPo.getCreateBy());
		customerOperatorVo.setCreateDate(customerOperatorPo.getCreateDate());
		customerOperatorVo.setUpdateBy(loginUser.getId());
		customerOperatorVo.setUpdateDate(date);
		customerOperatorVo.setDelStatus(customerOperatorPo.getDelStatus());
		PropertyUtils.copyProperties(customerOperatorPo, customerOperatorVo);
		this.customerOperatorDao.editCustomerOperator(customerOperatorPo);
	}

	public void deleteCustomerOperator(Long customerOperatorId) throws Exception {
		CustomerOperatorPo customerOperatorPo = customerOperatorDao.getCustomerOperator(customerOperatorId);
		if(customerOperatorPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		customerOperatorPo.setDelStatus(DelStatusEnum.HIDE.getValue());
		customerOperatorPo.setUpdateBy(loginUser.getId());
		customerOperatorPo.setUpdateDate(date);
		this.customerOperatorDao.editCustomerOperator(customerOperatorPo);
	}
}
