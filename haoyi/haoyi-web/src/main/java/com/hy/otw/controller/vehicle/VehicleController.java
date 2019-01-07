package com.hy.otw.controller.vehicle;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.service.vehicle.VehicleService;
import com.hy.otw.vo.VehicleVo;
import com.hy.otw.vo.http.ResponseMsgVo;
import com.hy.otw.vo.query.VehicleQueryVo;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
	
	@Resource private VehicleService vehicleService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseMsgVo add(HttpServletRequest request,HttpServletResponse response, VehicleVo vehicleVo) {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.vehicleService.addVehicle(vehicleVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseMsgVo edit(HttpServletRequest request,HttpServletResponse response, VehicleVo vehicleVo) throws Exception {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.vehicleService.editVehicle(vehicleVo);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseMsgVo delete(HttpServletRequest request,HttpServletResponse response, Long vehicleId) throws Exception {
		ResponseMsgVo msg = new ResponseMsgVo();
		try{
			this.vehicleService.deleteVehicle(vehicleId);
		}catch(Exception e){
			msg.setStatus(500);
			msg.setMsg(e.getMessage());
		}
		return msg;
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Pagination list(HttpServletRequest request,HttpServletResponse response, VehicleQueryVo vehicleQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.vehicleService.findVehicleList(vehicleQueryVo);
		return pagination;
	}

}
