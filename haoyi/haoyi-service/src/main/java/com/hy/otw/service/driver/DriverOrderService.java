package com.hy.otw.service.driver;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.common.enums.SettleStatusEnum;
import com.hy.otw.dao.driver.DriverOrderDao;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.DriverOrderPo;
import com.hy.otw.po.OrderPo;
import com.hy.otw.vo.DriverOrderVo;
import com.hy.otw.vo.query.DriverOrderQueryVo;

@Service
public class DriverOrderService {
	
	@Resource private DriverOrderDao driverOrderDao;

	public void addDriverOrder(OrderPo orderPo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 if(StringUtils.isNotBlank(orderPo.getContactNumber())){
			DriverOrderVo driverOrderVo = new DriverOrderVo();
			driverOrderVo.setAddress(orderPo.getAddress());
			driverOrderVo.setCabinetNumber(orderPo.getCabinetNumber());
			driverOrderVo.setCabinetModel(orderPo.getCabinetModel());
			driverOrderVo.setContactNumber(orderPo.getContactNumber());
			driverOrderVo.setOrderDate(orderPo.getOrderDate());
			driverOrderVo.setOrderId(orderPo.getId());
			driverOrderVo.setOrderNO(orderPo.getOrderNO());
			driverOrderVo.setOwnerName(orderPo.getOwnerName());
			driverOrderVo.setPlateNumber(orderPo.getPlateNumber());
			driverOrderVo.setSealNumber(orderPo.getSealNumber());
			driverOrderVo.setDriverPrice(orderPo.getDriverPrice());
			driverOrderVo.setSettleStatus(SettleStatusEnum.UNDONE.getValue());
			driverOrderVo.setCreateBy(orderPo.getCreateBy());
			driverOrderVo.setCreateDate(orderPo.getCreateDate());
			driverOrderVo.setUpdateBy(orderPo.getUpdateBy());
			driverOrderVo.setUpdateDate(orderPo.getUpdateDate());
			driverOrderVo.setDelStatus(DelStatusEnum.NORMAL.getValue());
			DriverOrderPo driverOrderPo = new DriverOrderPo();
			PropertyUtils.copyProperties(driverOrderPo, driverOrderVo);
			this.driverOrderDao.addDriverOrder(driverOrderPo);
		 }
	}

	public Pagination findDriverOrderList(DriverOrderQueryVo driverOrderQueryVo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Pagination pagination = this.driverOrderDao.findDriverOrderList(driverOrderQueryVo);
		List<DriverOrderPo> driverOrderPoList = (List<DriverOrderPo>) pagination.getRows();
		List<DriverOrderVo> driverOrderVoList = new ArrayList<DriverOrderVo>();
		if(CollectionUtils.isNotEmpty(driverOrderPoList)){
			for (DriverOrderPo driverOrderPo : driverOrderPoList) {
				DriverOrderVo driverOrderVo = new DriverOrderVo();
				PropertyUtils.copyProperties(driverOrderVo, driverOrderPo);
				driverOrderVoList.add(driverOrderVo);
			}
		}
		pagination.setRows(driverOrderVoList);
		return pagination;
	}

	public void editOrderInfo(OrderPo orderPo) throws Exception {
		DriverOrderPo driverOrderPo = this.driverOrderDao.getDriverOrderByOrderId(orderPo.getId());
		if(driverOrderPo == null){//如果没有查到司机订单信息
			this.addDriverOrder(orderPo);
		}else if(StringUtils.isNotBlank(orderPo.getContactNumber())){//如果车牌不为空则为修改(调度)
			driverOrderPo.setOwnerName(orderPo.getOwnerName());
			driverOrderPo.setPlateNumber(orderPo.getPlateNumber());
			driverOrderPo.setContactNumber(orderPo.getContactNumber());
			driverOrderPo.setOrderId(orderPo.getId());
			driverOrderPo.setAddress(orderPo.getAddress());
			driverOrderPo.setCabinetModel(orderPo.getCabinetModel());
			driverOrderPo.setCabinetNumber(orderPo.getCabinetNumber());
			driverOrderPo.setDriverPrice(orderPo.getDriverPrice());
			driverOrderPo.setDelStatus(orderPo.getDelStatus());
			driverOrderPo.setOrderDate(orderPo.getOrderDate());
			driverOrderPo.setOrderNO(orderPo.getOrderNO());
			driverOrderPo.setSealNumber(orderPo.getSealNumber());
			driverOrderPo.setUpdateBy(orderPo.getUpdateBy());
			driverOrderPo.setUpdateDate(orderPo.getUpdateDate());
			this.driverOrderDao.editDriverOrder(driverOrderPo);
		}else if(StringUtils.isBlank(orderPo.getContactNumber())){//删除
			deleteDriverOrder(orderPo.getId());
		}
		
	}
	
	public void editDriverOrder(DriverOrderVo driverOrderVo) throws Exception {
		DriverOrderPo driverOrderPo = driverOrderDao.getDriverOrder(driverOrderVo.getId());
		if(driverOrderPo == null){
			throw new Exception("未找到该条信息");
		}
		Date date = new Date();
		driverOrderPo.setRemarks(driverOrderVo.getRemarks());
		driverOrderPo.setSettleStatus(driverOrderVo.getSettleStatus());
		driverOrderPo.setUpdateBy(1l);
		driverOrderPo.setUpdateDate(date);
		this.driverOrderDao.editDriverOrder(driverOrderPo);
	}

	public void deleteDriverOrder(Long driverOrderId) throws Exception {
		DriverOrderPo driverOrderPo = driverOrderDao.getDriverOrder(driverOrderId);
		if(driverOrderPo != null){
			Date date = new Date();
			driverOrderPo.setDelStatus(DelStatusEnum.HIDE.getValue());
			driverOrderPo.setUpdateBy(1l);
			driverOrderPo.setUpdateDate(date);
			this.driverOrderDao.editDriverOrder(driverOrderPo);
		}
	}

	public BigDecimal findDriverOrderTotalAmt(DriverOrderQueryVo driverOrderQueryVo) {
		return this.driverOrderDao.findDriverOrderTotalAmt(driverOrderQueryVo);
	}

}
