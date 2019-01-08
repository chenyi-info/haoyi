package com.hy.otw.controller.customer;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.customer.CustomerService;
import com.hy.otw.vo.CustomerOperatorVo;
import com.hy.otw.vo.CustomerVo;
import com.hy.otw.vo.http.ResponseMsgVo;
import com.hy.otw.vo.query.CustomerQueryVo;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
@Resource private CustomerService customerService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseMsgVo add(HttpServletRequest request,HttpServletResponse response, CustomerVo customerVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.customerService.addCustomer(customerVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseMsgVo edit(HttpServletRequest request,HttpServletResponse response, CustomerVo customerVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.customerService.editCustomer(customerVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseMsgVo delete(HttpServletRequest request,HttpServletResponse response, Long customerId) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.customerService.deleteCustomer(customerId);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, CustomerQueryVo customerQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.customerService.findCustomerList(customerQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/operatorList", method = RequestMethod.GET)
	public Pagination operatorList(HttpServletRequest request,HttpServletResponse response, CustomerQueryVo customerQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.customerService.findCustomerOperatorList(customerQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/addOperator", method = RequestMethod.POST)
	public ResponseMsgVo addOperator(HttpServletRequest request,HttpServletResponse response, CustomerOperatorVo customerOperatorVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.customerService.addCustomerOperator(customerOperatorVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/editOperator", method = RequestMethod.POST)
	public ResponseMsgVo editOperator(HttpServletRequest request,HttpServletResponse response, CustomerOperatorVo customerOperatorVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.customerService.editCustomerOperator(customerOperatorVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/deleteOperator", method = RequestMethod.POST)
	public ResponseMsgVo deleteOperator(HttpServletRequest request,HttpServletResponse response, Long customerOperatorId) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.customerService.deleteCustomerOperator(customerOperatorId);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}

}
