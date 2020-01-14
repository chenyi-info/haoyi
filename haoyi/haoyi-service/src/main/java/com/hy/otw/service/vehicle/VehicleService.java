package com.hy.otw.service.vehicle;

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
import com.hy.otw.dao.vehicle.VehicleDao;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.VehiclePo;
import com.hy.otw.vo.UserInfoVo;
import com.hy.otw.vo.VehicleVo;
import com.hy.otw.vo.query.VehicleQueryVo;

@Service
public class VehicleService {
	
	@Resource private VehicleDao vehicleDao;

	public void addVehicle(VehicleVo vehicleVo) throws Exception {
		VehiclePo vehiclePo = vehicleDao.getVehicleByPlateNumber(vehicleVo.getPlateNumber());
		if(vehiclePo != null){
			throw new Exception("车牌号不能重复");
		}
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		vehiclePo = new VehiclePo();
		vehicleVo.setCreateBy(loginUser.getId());
		vehicleVo.setCreateDate(date);
		vehicleVo.setUpdateBy(loginUser.getId());
		vehicleVo.setUpdateDate(date);
		vehicleVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		PropertyUtils.copyProperties(vehiclePo, vehicleVo);
		this.vehicleDao.addVehicle(vehiclePo);
	}

	public Pagination findVehicleList(VehicleQueryVo vehicleQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.vehicleDao.findVehicleList(vehicleQueryVo);
		List<VehiclePo> vehicelPoList = (List<VehiclePo>) pagination.getRows();
		List<VehicleVo> vehicelVoList = new ArrayList<VehicleVo>();
		if(CollectionUtils.isNotEmpty(vehicelPoList)){
			for (VehiclePo vehiclePo : vehicelPoList) {
				VehicleVo vehicleVo = new VehicleVo();
				PropertyUtils.copyProperties(vehicleVo, vehiclePo);
				vehicelVoList.add(vehicleVo);
			}
		}
		pagination.setRows(vehicelVoList);
		return pagination;
	}

	public void editVehicle(VehicleVo vehicleVo) throws Exception {
		VehiclePo vehiclePo = vehicleDao.getVehicleByPlateNumber(vehicleVo.getPlateNumber());
		if(vehiclePo != null && !vehiclePo.getId().equals(vehicleVo.getId())){
			throw new Exception("车牌号不能重复");
		}
		if(vehiclePo == null){
			vehiclePo = vehicleDao.getVehicle(vehicleVo.getId());
		}
		if(vehiclePo == null){
			throw new Exception("未找到司机信息");
		}
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		vehicleVo.setCreateBy(vehiclePo.getCreateBy());
		vehicleVo.setCreateDate(vehiclePo.getCreateDate());
		vehicleVo.setUpdateBy(loginUser.getId());
		vehicleVo.setUpdateDate(date);
		vehicleVo.setDelStatus(vehiclePo.getDelStatus());
		PropertyUtils.copyProperties(vehiclePo, vehicleVo);
		this.vehicleDao.editVehicle(vehiclePo);
	}

	public void deleteVehicle(Long vehicleId) throws Exception {
		VehiclePo vehiclePo = vehicleDao.getVehicle(vehicleId);
		if(vehiclePo == null){
			throw new Exception("未找到司机信息");
		}
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		vehiclePo.setDelStatus(DelStatusEnum.HIDE.getValue());
		vehiclePo.setUpdateBy(loginUser.getId());
		vehiclePo.setUpdateDate(date);
		this.vehicleDao.editVehicle(vehiclePo);
	}

}
