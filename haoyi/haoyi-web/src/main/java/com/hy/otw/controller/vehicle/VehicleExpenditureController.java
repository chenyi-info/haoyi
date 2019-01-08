package com.hy.otw.controller.vehicle;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.vehicle.VehicleExpenditureService;
import com.hy.otw.vo.VehicleExpenditureVo;
import com.hy.otw.vo.query.VehicleExpenditureQueryVo;

@RestController
@RequestMapping("/vehicleExpenditure")
public class VehicleExpenditureController {
	
	@Resource private VehicleExpenditureService vehicleExpenditureService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(HttpServletRequest request,HttpServletResponse response, VehicleExpenditureVo vehicleExpenditureVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		this.vehicleExpenditureService.addVehicleExpenditure(vehicleExpenditureVo);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request,HttpServletResponse response, VehicleExpenditureVo vehicleExpenditureVo) throws Exception {
		this.vehicleExpenditureService.editVehicleExpenditure(vehicleExpenditureVo);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response, Long vehicleExpenditureId) throws Exception {
		this.vehicleExpenditureService.deleteVehicleExpenditure(vehicleExpenditureId);
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, VehicleExpenditureQueryVo vehicleExpenditureQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.vehicleExpenditureService.findVehicleExpenditureList(vehicleExpenditureQueryVo);
		return pagination;
	}

}
