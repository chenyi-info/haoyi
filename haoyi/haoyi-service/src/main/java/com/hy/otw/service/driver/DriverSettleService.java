package com.hy.otw.service.driver;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.dao.driver.DriverSettleDao;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.DriverSettlePo;
import com.hy.otw.vo.DriverSettleVo;
import com.hy.otw.vo.query.DriverSettleQueryVo;

@Service
public class DriverSettleService {
	
	@Resource private DriverSettleDao driverSettleDao;

	public void addDriverSettle(DriverSettleVo driverSettleVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Date date = new Date();
		DriverSettlePo driverSettlePo = new DriverSettlePo();
		driverSettleVo.setCreateBy(1l);
		driverSettleVo.setCreateDate(date);
		driverSettleVo.setUpdateBy(1l);
		driverSettleVo.setUpdateDate(date);
		driverSettleVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
		PropertyUtils.copyProperties(driverSettlePo, driverSettleVo);
		this.driverSettleDao.addDriverSettle(driverSettlePo);
	}

	public Pagination findDriverSettleList(DriverSettleQueryVo driverSettleQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.driverSettleDao.findDriverSettleList(driverSettleQueryVo);
		List<DriverSettlePo> driverSettlePoList = (List<DriverSettlePo>) pagination.getRows();
		List<DriverSettleVo> driverSettleVoList = new ArrayList<DriverSettleVo>();
		if(CollectionUtils.isNotEmpty(driverSettlePoList)){
			for (DriverSettlePo driverSettlePo : driverSettlePoList) {
				DriverSettleVo driverSettleVo = new DriverSettleVo();
				PropertyUtils.copyProperties(driverSettleVo, driverSettlePo);
				driverSettleVoList.add(driverSettleVo);
			}
		}
		pagination.setRows(driverSettleVoList);
		return pagination;
	}

	public void editDriverSettle(DriverSettleVo driverSettleVo) throws Exception {
		DriverSettlePo driverSettlePo = driverSettleDao.getDriverSettle(driverSettleVo.getId());
		if(driverSettlePo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		driverSettleVo.setCreateBy(driverSettlePo.getCreateBy());
		driverSettleVo.setCreateDate(driverSettlePo.getCreateDate());
		driverSettleVo.setUpdateBy(1l);
		driverSettleVo.setUpdateDate(date);
		driverSettleVo.setDelStatus(driverSettlePo.getDelStatus());
		PropertyUtils.copyProperties(driverSettlePo, driverSettleVo);
		this.driverSettleDao.editDriverSettle(driverSettlePo);
	}

	public void deleteDriverSettle(Long driverSettleId) throws Exception {
		DriverSettlePo driverSettlePo = driverSettleDao.getDriverSettle(driverSettleId);
		if(driverSettlePo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		driverSettlePo.setDelStatus(DelStatusEnum.HIDE.getValue());
		driverSettlePo.setUpdateBy(1l);
		driverSettlePo.setUpdateDate(date);
		this.driverSettleDao.editDriverSettle(driverSettlePo);
	}

}
