package com.hy.otw.service.vehicle;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.dao.vehicle.VehicleExpenditureDao;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.VehicleExpenditurePo;
import com.hy.otw.vo.VehicleExpenditureVo;
import com.hy.otw.vo.query.VehicleExpenditureQueryVo;

@Service
public class VehicleExpenditureService {
	
	@Resource private VehicleExpenditureDao vehicleExpenditureDao;

	public void addVehicleExpenditure(VehicleExpenditureVo vehicleExpenditureVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Date date = new Date();
		VehicleExpenditurePo vehicleExpenditurePo = new VehicleExpenditurePo();
		vehicleExpenditureVo.setCreateBy(1l);
		vehicleExpenditureVo.setCreateDate(date);
		vehicleExpenditureVo.setUpdateBy(1l);
		vehicleExpenditureVo.setUpdateDate(date);
		vehicleExpenditureVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		PropertyUtils.copyProperties(vehicleExpenditurePo, vehicleExpenditureVo);
		this.vehicleExpenditureDao.addVehicleExpenditure(vehicleExpenditurePo);
	}

	public Pagination findVehicleExpenditureList(VehicleExpenditureQueryVo vehicleExpenditureQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.vehicleExpenditureDao.findVehicleExpenditureList(vehicleExpenditureQueryVo);
		List<VehicleExpenditurePo> vehicelExpenditurePoList = (List<VehicleExpenditurePo>) pagination.getRows();
		List<VehicleExpenditureVo> vehicelExpenditureVoList = new ArrayList<VehicleExpenditureVo>();
		if(CollectionUtils.isNotEmpty(vehicelExpenditurePoList)){
			for (VehicleExpenditurePo vehicleExpenditurePo : vehicelExpenditurePoList) {
				VehicleExpenditureVo vehicleExpenditureVo = new VehicleExpenditureVo();
				PropertyUtils.copyProperties(vehicleExpenditureVo, vehicleExpenditurePo);
				vehicelExpenditureVoList.add(vehicleExpenditureVo);
			}
		}
		pagination.setRows(vehicelExpenditureVoList);
		return pagination;
	}

	public void editVehicleExpenditure(VehicleExpenditureVo vehicleExpenditureVo) throws Exception {
		VehicleExpenditurePo vehicleExpenditurePo = vehicleExpenditureDao.getVehicleExpenditure(vehicleExpenditureVo.getId());
		if(vehicleExpenditurePo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		vehicleExpenditureVo.setCreateBy(vehicleExpenditurePo.getCreateBy());
		vehicleExpenditureVo.setCreateDate(vehicleExpenditurePo.getCreateDate());
		vehicleExpenditureVo.setUpdateBy(1l);
		vehicleExpenditureVo.setUpdateDate(date);
		vehicleExpenditureVo.setDelStatus(vehicleExpenditurePo.getDelStatus());
		PropertyUtils.copyProperties(vehicleExpenditurePo, vehicleExpenditureVo);
		this.vehicleExpenditureDao.editVehicleExpenditure(vehicleExpenditurePo);
	}

	public void deleteVehicleExpenditure(Long vehicleExpenditureId) throws Exception {
		VehicleExpenditurePo vehicleExpenditurePo = vehicleExpenditureDao.getVehicleExpenditure(vehicleExpenditureId);
		if(vehicleExpenditurePo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		vehicleExpenditurePo.setDelStatus(DelStatusEnum.HIDE.getValue());
		vehicleExpenditurePo.setUpdateBy(1l);
		vehicleExpenditurePo.setUpdateDate(date);
		this.vehicleExpenditureDao.editVehicleExpenditure(vehicleExpenditurePo);
	}

}
