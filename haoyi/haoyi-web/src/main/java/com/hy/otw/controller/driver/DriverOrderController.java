package com.hy.otw.controller.driver;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.driver.DriverOrderService;
import com.hy.otw.vo.DriverOrderVo;
import com.hy.otw.vo.query.DriverOrderQueryVo;

@RestController
@RequestMapping("/driverOrder")
public class DriverOrderController {
	
	@Resource private DriverOrderService driverOrderService;
	
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request,HttpServletResponse response, DriverOrderVo driverOrderVo) throws Exception {
		this.driverOrderService.editDriverOrder(driverOrderVo);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response, Long driverOrderId) throws Exception {
		this.driverOrderService.deleteDriverOrder(driverOrderId);
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, DriverOrderQueryVo driverOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.driverOrderService.findDriverOrderList(driverOrderQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/totalAmt", method = RequestMethod.GET)
	public BigDecimal totalAmt(HttpServletRequest request,HttpServletResponse response, DriverOrderQueryVo driverOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BigDecimal totalAmt = this.driverOrderService.findDriverOrderTotalAmt(driverOrderQueryVo);
		return totalAmt == null ? new BigDecimal(0.00) : totalAmt;
	}

}
