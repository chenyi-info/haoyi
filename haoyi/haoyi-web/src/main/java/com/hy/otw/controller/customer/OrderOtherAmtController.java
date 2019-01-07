package com.hy.otw.controller.customer;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.customer.OrderOtherAmtService;
import com.hy.otw.vo.OrderOtherAmtVo;
import com.hy.otw.vo.query.OrderOtherAmtQueryVo;

@RestController
@RequestMapping("/orderOtherAmt")
public class OrderOtherAmtController {
	
	@Resource private OrderOtherAmtService orderOtherAmtService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(HttpServletRequest request,HttpServletResponse response, OrderOtherAmtVo orderOtherAmtVo) throws Exception {
		this.orderOtherAmtService.addOrderOtherAmt(orderOtherAmtVo);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request,HttpServletResponse response, OrderOtherAmtVo orderOtherAmtVo) throws Exception {
		this.orderOtherAmtService.editOrderOtherAmt(orderOtherAmtVo);
		this.orderOtherAmtService.statisticalAmount(orderOtherAmtVo.getOrderId());
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response, Long orderOtherAmtId) throws Exception {
		this.orderOtherAmtService.deleteOrderOtherAmt(orderOtherAmtId);
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, OrderOtherAmtQueryVo orderOtherAmtQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.orderOtherAmtService.findOrderOtherAmtList(orderOtherAmtQueryVo);
		return pagination;
	}

}
