package com.hy.otw.dao.driver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.DriverOrderPo;
import com.hy.otw.vo.query.DriverOrderQueryVo;

@Repository
public class DriverOrderDao extends HibernateDao<DriverOrderPo, Long>{

	public void addDriverOrder(DriverOrderPo driverOrderPo) {
		this.save(driverOrderPo);
	}

	public Pagination findDriverOrderList(DriverOrderQueryVo driverOrderQueryVo) {
		StringBuffer hql = new StringBuffer("from DriverOrderPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<DriverOrderPo> page = new Page<DriverOrderPo>();
		page.setPageNo(driverOrderQueryVo.getPage());
		page.setPageSize(driverOrderQueryVo.getRows());
		if(StringUtils.isBlank(driverOrderQueryVo.getVehicleSource()) && StringUtils.isNotBlank(driverOrderQueryVo.getContactNumber())){
			hql.append(" and contactNumber like '%").append(driverOrderQueryVo.getContactNumber()).append("%'");
		}
		if(StringUtils.isBlank(driverOrderQueryVo.getVehicleSource()) && StringUtils.isNotBlank(driverOrderQueryVo.getOwnerName())){
			hql.append(" and ownerName like '%").append(driverOrderQueryVo.getOwnerName()).append("%'");
		}
		if(StringUtils.isBlank(driverOrderQueryVo.getVehicleSource()) &&  StringUtils.isNotBlank(driverOrderQueryVo.getPlateNumber())){
			hql.append(" and plateNumber like '%").append(driverOrderQueryVo.getPlateNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(driverOrderQueryVo.getOrderNO())){
			hql.append(" and orderNO like '%").append(driverOrderQueryVo.getOrderNO()).append("%'");
		}
		if(StringUtils.isNotBlank(driverOrderQueryVo.getAddress())){
			hql.append(" and address like '%").append(driverOrderQueryVo.getAddress()).append("%'");
		}
		if(StringUtils.isNotBlank(driverOrderQueryVo.getCabinetNumber())){
			hql.append(" and cabinetNumber like '%").append(driverOrderQueryVo.getCabinetNumber()).append("%'");
		}
		if(driverOrderQueryVo.getSettleStatus() != null){
			if(driverOrderQueryVo.getSettleStatus() == 0 ) {
				hql.append(" and settleStatus in (0,2) ");
			}else {
				hql.append(" and settleStatus = ? ");
				param.add(driverOrderQueryVo.getSettleStatus());
			}
		}
		if(driverOrderQueryVo.getOrderDateBegin() != null){
			hql.append(" and orderDate > ?");
			param.add(driverOrderQueryVo.getOrderDateBegin());
		}
		if(driverOrderQueryVo.getOrderDateEnd() != null){
			hql.append(" and orderDate < ?");
			param.add(driverOrderQueryVo.getOrderDateEnd());
		}
		
		//如果要查询公司信息
		if(StringUtils.isNotBlank(driverOrderQueryVo.getCompanyName())) {
			hql.append(" and orderId in (select id from OrderPo where delStatus=? ");
			param.add(DelStatusEnum.NORMAL.getValue());
			hql.append(" and companyName like '%").append(driverOrderQueryVo.getCompanyName()).append("%'");
			hql.append(")");
		}
		
		//如果要按车辆来源查询
		if(StringUtils.isNotBlank(driverOrderQueryVo.getVehicleSource())) {
			hql.append(" and plateNumber in (select plateNumber from VehiclePo where delStatus=? ");
			param.add(DelStatusEnum.NORMAL.getValue());
			hql.append(" and vehicleSource = ? ");
			param.add(driverOrderQueryVo.getVehicleSource());
			
			if(StringUtils.isNotBlank(driverOrderQueryVo.getContactNumber())){
				hql.append(" and contactNumber like '%").append(driverOrderQueryVo.getContactNumber()).append("%'");
			}
			if(StringUtils.isNotBlank(driverOrderQueryVo.getOwnerName())){
				hql.append(" and ownerName like '%").append(driverOrderQueryVo.getOwnerName()).append("%'");
			}
			if(StringUtils.isNotBlank(driverOrderQueryVo.getPlateNumber())){
				hql.append(" and plateNumber like '%").append(driverOrderQueryVo.getPlateNumber()).append("%'");
			}
			
			
			hql.append(")");
		}
		
		hql.append(" order by ").append(driverOrderQueryVo.getSort()).append(" ").append(driverOrderQueryVo.getOrder());
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public DriverOrderPo getDriverOrder(Long driverOrderId) {
		String hql = "from DriverOrderPo where delStatus=? and orderId=?";
		DriverOrderPo driverOrderPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), driverOrderId);
		return driverOrderPo;
	}

	public void editDriverOrder(DriverOrderPo driverOrderPo) {
		this.update(driverOrderPo);
	}

	public BigDecimal findDriverOrderTotalAmt(DriverOrderQueryVo driverOrderQueryVo) {
		StringBuffer hql = new StringBuffer("select sum(IFNULL(driverPrice,0) + IFNULL(otherAmt,0) - IFNULL(settlePrice,0))  from DriverOrderPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		if(StringUtils.isBlank(driverOrderQueryVo.getVehicleSource()) && StringUtils.isNotBlank(driverOrderQueryVo.getContactNumber())){
			hql.append(" and contactNumber like '%").append(driverOrderQueryVo.getContactNumber()).append("%'");
		}
		if(StringUtils.isBlank(driverOrderQueryVo.getVehicleSource()) && StringUtils.isNotBlank(driverOrderQueryVo.getOwnerName())){
			hql.append(" and ownerName like '%").append(driverOrderQueryVo.getOwnerName()).append("%'");
		}
		if(StringUtils.isBlank(driverOrderQueryVo.getVehicleSource()) && StringUtils.isNotBlank(driverOrderQueryVo.getPlateNumber())){
			hql.append(" and plateNumber like '%").append(driverOrderQueryVo.getPlateNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(driverOrderQueryVo.getOrderNO())){
			hql.append(" and orderNO like '%").append(driverOrderQueryVo.getOrderNO()).append("%'");
		}
		if(StringUtils.isNotBlank(driverOrderQueryVo.getAddress())){
			hql.append(" and address like '%").append(driverOrderQueryVo.getAddress()).append("%'");
		}
		if(StringUtils.isNotBlank(driverOrderQueryVo.getCabinetNumber())){
			hql.append(" and cabinetNumber like '%").append(driverOrderQueryVo.getCabinetNumber()).append("%'");
		}
		if(driverOrderQueryVo.getSettleStatus() != null){
			hql.append(" and settleStatus = ? ");
			param.add(driverOrderQueryVo.getSettleStatus());
		}
		if(driverOrderQueryVo.getOrderDateBegin() != null){
			hql.append(" and orderDate > ?");
			param.add(driverOrderQueryVo.getOrderDateBegin());
		}
		if(driverOrderQueryVo.getOrderDateEnd() != null){
			hql.append(" and orderDate < ?");
			param.add(driverOrderQueryVo.getOrderDateEnd());
		}
		
		
		//如果要查询公司信息
		if(StringUtils.isNotBlank(driverOrderQueryVo.getCompanyName())) {
			hql.append(" and orderId in (select id from OrderPo where delStatus=? ");
			param.add(DelStatusEnum.NORMAL.getValue());
			hql.append(" and companyName like '%").append(driverOrderQueryVo.getCompanyName()).append("%'");
			hql.append(")");
		}
		
		
		//如果要按车辆来源查询
		if(StringUtils.isNotBlank(driverOrderQueryVo.getVehicleSource())) {
			hql.append(" and plateNumber in (select plateNumber from VehiclePo where delStatus=? ");
			param.add(DelStatusEnum.NORMAL.getValue());
			hql.append(" and vehicleSource = ? ");
			param.add(driverOrderQueryVo.getVehicleSource());
			
			if(StringUtils.isNotBlank(driverOrderQueryVo.getContactNumber())){
				hql.append(" and contactNumber like '%").append(driverOrderQueryVo.getContactNumber()).append("%'");
			}
			if(StringUtils.isNotBlank(driverOrderQueryVo.getOwnerName())){
				hql.append(" and ownerName like '%").append(driverOrderQueryVo.getOwnerName()).append("%'");
			}
			if(StringUtils.isNotBlank(driverOrderQueryVo.getPlateNumber())){
				hql.append(" and plateNumber like '%").append(driverOrderQueryVo.getPlateNumber()).append("%'");
			}
			
			
			hql.append(")");
		}
		hql.append(" order by ").append(driverOrderQueryVo.getSort()).append(" ").append(driverOrderQueryVo.getOrder());
		
		Query query = this.createQuery(hql.toString(), param.toArray());
		Object amt = query.uniqueResult();
		BigDecimal totalAmt = new BigDecimal( amt == null ? "0" : amt.toString());
		return totalAmt;
	}

	public DriverOrderPo getDriverOrderByOrderId(Long orderId) {
		String hql = "from DriverOrderPo where delStatus=? and orderId=?";
		DriverOrderPo driverOrderPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), orderId);
		return driverOrderPo;
	}
	
	public void batchSettles(List<Long> driverOrderIdList) {
		String hql = "update DriverOrderPo set settleStatus = 1,settleDate = now() where id in (:idList)";
		Query query = this.createQuery(hql);
		query.setParameterList("idList", driverOrderIdList);
		query.executeUpdate();
		hql = "update OrderOtherAmtPo set is_settle = 0, settle_date = now() where order_id in (select orderId from DriverOrderPo where id in (:idList)) and property_type = 1 and del_status = 0";
		query = this.createQuery(hql);
		query.setParameterList("idList", driverOrderIdList);
		query.executeUpdate();
	}

	public DriverOrderPo getDriverOrderId(Long id) {
		String hql = "from DriverOrderPo where delStatus=? and id=?";
		DriverOrderPo driverOrderPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), id);
		return driverOrderPo;
	}

	public void batchLockOrUnLock(List<Long> driverOrderIdList, int status) {
		String hql = "update DriverOrderPo set settleStatus =:status, update_date = now() where id in (:idList)";
		Query query = this.createQuery(hql);
		query.setParameter("status", status);
		query.setParameterList("idList", driverOrderIdList);
		query.executeUpdate();
	}
}
