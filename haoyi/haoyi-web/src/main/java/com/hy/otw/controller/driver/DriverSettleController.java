package com.hy.otw.controller.driver;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.driver.DriverSettleService;
import com.hy.otw.vo.DriverSettleVo;
import com.hy.otw.vo.query.DriverSettleQueryVo;

@RestController
@RequestMapping("/driverSettle")
public class DriverSettleController {
	
	@Resource private DriverSettleService driverSettleService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(HttpServletRequest request,HttpServletResponse response, DriverSettleVo driverSettleVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		this.driverSettleService.addDriverSettle(driverSettleVo);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request,HttpServletResponse response, DriverSettleVo driverSettleVo) throws Exception {
		this.driverSettleService.editDriverSettle(driverSettleVo);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response, Long driverSettleId) throws Exception {
		this.driverSettleService.deleteDriverSettle(driverSettleId);
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, DriverSettleQueryVo driverSettleQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.driverSettleService.findDriverSettleList(driverSettleQueryVo);
		return pagination;
	}

}
