package com.hy.otw.controller.customer;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.customer.CustomerOrderService;
import com.hy.otw.vo.CustomerOrderVo;
import com.hy.otw.vo.query.CustomerOrderQueryVo;

@RestController
@RequestMapping("/customerOrder")
public class CustomerOrderController {
	
	@Resource private CustomerOrderService customerOrderService;
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request,HttpServletResponse response, CustomerOrderVo customerOrderVo) throws Exception {
		this.customerOrderService.editCustomerOrder(customerOrderVo);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response, Long customerOrderId) throws Exception {
		this.customerOrderService.deleteCustomerOrder(customerOrderId);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, CustomerOrderQueryVo customerOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.customerOrderService.findCustomerOrderList(customerOrderQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/totalAmt", method = RequestMethod.GET)
	public BigDecimal totalAmt(HttpServletRequest request,HttpServletResponse response, CustomerOrderQueryVo customerOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BigDecimal totalAmt = this.customerOrderService.findCustomerOrderTotalAmt(customerOrderQueryVo);
		return totalAmt == null ? new BigDecimal(0.00) : totalAmt;
	}

}
