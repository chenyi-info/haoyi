package com.hy.otw.service.vehicle;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.dao.vehicle.VehicleCareDao;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.VehicleCarePo;
import com.hy.otw.vo.UserInfoVo;
import com.hy.otw.vo.VehicleCareVo;
import com.hy.otw.vo.query.VehicleCareQueryVo;

@Service
public class VehicleCareService {
	
	@Resource private VehicleCareDao vehicleCareDao;

	public void addVehicleCare(VehicleCareVo vehicleCareVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		VehicleCarePo vehicleCarePo = new VehicleCarePo();
		vehicleCareVo.setCreateBy(loginUser.getId());
		vehicleCareVo.setCreateDate(date);
		vehicleCareVo.setUpdateBy(loginUser.getId());
		vehicleCareVo.setUpdateDate(date);
		vehicleCareVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		PropertyUtils.copyProperties(vehicleCarePo, vehicleCareVo);
		VehicleCarePo prevVehicleCarePo = vehicleCareDao.findLastPrevVehicleCarePo(vehicleCareVo.getPlateNumber(), vehicleCareVo.getCareDate());
		this.vehicleCareDao.addVehicleCare(vehicleCarePo);
		if(prevVehicleCarePo != null){
			Long days = (Long) ((vehicleCarePo.getCareDate().getTime() - prevVehicleCarePo.getCareDate().getTime()) / (1000*3600*24));
			prevVehicleCarePo.setCareInterval(days);
			this.vehicleCareDao.editVehicleCare(prevVehicleCarePo);	
		}
	}

	public Pagination findVehicleCareList(VehicleCareQueryVo vehicleCareQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.vehicleCareDao.findVehicleCareList(vehicleCareQueryVo);
		List<VehicleCarePo> vehicelCarePoList = (List<VehicleCarePo>) pagination.getRows();
		List<VehicleCareVo> vehicelCareVoList = new ArrayList<VehicleCareVo>();
		if(CollectionUtils.isNotEmpty(vehicelCarePoList)){
			for (VehicleCarePo vehicleCarePo : vehicelCarePoList) {
				VehicleCareVo vehicleCareVo = new VehicleCareVo();
				PropertyUtils.copyProperties(vehicleCareVo, vehicleCarePo);
				vehicelCareVoList.add(vehicleCareVo);
			}
		}
		pagination.setRows(vehicelCareVoList);
		return pagination;
	}

	public void editVehicleCare(VehicleCareVo vehicleCareVo) throws Exception {
		VehicleCarePo vehicleCarePo = vehicleCareDao.getVehicleCare(vehicleCareVo.getId());
		if(vehicleCarePo == null){
			throw new Exception("未找到该条信息");
		}
		
		VehicleCarePo prevVehicleCarePo = vehicleCareDao.findLastPrevVehicleCarePo(vehicleCareVo.getPlateNumber(), vehicleCareVo.getCareDate());
		VehicleCarePo nextVehicleCarePo = vehicleCareDao.findLastNextVehicleCarePo(vehicleCareVo.getPlateNumber(), vehicleCareVo.getCareDate());
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		vehicleCareVo.setCreateBy(vehicleCarePo.getCreateBy());
		vehicleCareVo.setCreateDate(vehicleCarePo.getCreateDate());
		vehicleCareVo.setUpdateBy(loginUser.getId());
		vehicleCareVo.setUpdateDate(date);
		vehicleCareVo.setDelStatus(vehicleCarePo.getDelStatus());
		PropertyUtils.copyProperties(vehicleCarePo, vehicleCareVo);
		if(prevVehicleCarePo != null){//如果上一个不为空,那么计算当前的保养间距
			Long days = (Long) ((vehicleCarePo.getCareDate().getTime() - prevVehicleCarePo.getCareDate().getTime()) / (1000*3600*24));
			vehicleCarePo.setCareInterval(days);
		}
		
		this.vehicleCareDao.editVehicleCare(vehicleCarePo);
		if(prevVehicleCarePo != null && prevVehicleCarePo.getCareInterval() == null){//如果上一个是第一次保养，那么保养间距设置为0
			prevVehicleCarePo.setCareInterval(0l);
			this.vehicleCareDao.editVehicleCare(prevVehicleCarePo);
		}
		if(nextVehicleCarePo != null && prevVehicleCarePo.getCareInterval() != null){//如果下一个不为空,并且不为最后一次保养，那么计算下一个的保养间距
			Long days = (Long) ((nextVehicleCarePo.getCareDate().getTime() - vehicleCarePo.getCareDate().getTime()) / (1000*3600*24));
			nextVehicleCarePo.setCareInterval(days);
			this.vehicleCareDao.editVehicleCare(nextVehicleCarePo);
		}
		
	}

	public void deleteVehicleCare(Long vehicleCareId) throws Exception {
		VehicleCarePo vehicleCarePo = vehicleCareDao.getVehicleCare(vehicleCareId);
		if(vehicleCarePo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		UserInfoVo loginUser = (UserInfoVo) SecurityUtils.getSubject().getPrincipal();
		vehicleCarePo.setDelStatus(DelStatusEnum.HIDE.getValue());
		vehicleCarePo.setUpdateBy(loginUser.getId());
		vehicleCarePo.setUpdateDate(date);
		this.vehicleCareDao.editVehicleCare(vehicleCarePo);
	}

	public BigDecimal findVehicleCareTotalAmt(VehicleCareQueryVo vehicleCareQueryVo) {
		BigDecimal totalAmt =this.vehicleCareDao.findVehicleCareTotalAmt(vehicleCareQueryVo);
		return totalAmt;
	}

}
