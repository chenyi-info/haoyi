package com.hy.otw.controller.vehicle;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.vehicle.VehicleCareService;
import com.hy.otw.vo.VehicleCareVo;
import com.hy.otw.vo.query.VehicleCareQueryVo;

@RestController
@RequestMapping("/vehicleCare")
public class VehicleCareController {
	
	@Resource private VehicleCareService vehicleCareService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(HttpServletRequest request,HttpServletResponse response, VehicleCareVo vehicleCareVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		this.vehicleCareService.addVehicleCare(vehicleCareVo);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request,HttpServletResponse response, VehicleCareVo vehicleCareVo) throws Exception {
		this.vehicleCareService.editVehicleCare(vehicleCareVo);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response, Long vehicleCareId) throws Exception {
		this.vehicleCareService.deleteVehicleCare(vehicleCareId);
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, VehicleCareQueryVo vehicleCareQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.vehicleCareService.findVehicleCareList(vehicleCareQueryVo);
		return pagination;
	}
	
	@RequestMapping(value = "/totalAmt", method = RequestMethod.GET)
	public BigDecimal totalAmt(HttpServletRequest request,HttpServletResponse response, VehicleCareQueryVo vehicleCareQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BigDecimal totalAmt = this.vehicleCareService.findVehicleCareTotalAmt(vehicleCareQueryVo);
		return totalAmt;
	}

}
