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
		if(StringUtils.isNotBlank(driverOrderQueryVo.getContactNumber())){
			hql.append(" and contactNumber like '%").append(driverOrderQueryVo.getContactNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(driverOrderQueryVo.getOwnerName())){
			hql.append(" and ownerName like '%").append(driverOrderQueryVo.getOwnerName()).append("%'");
		}
		if(StringUtils.isNotBlank(driverOrderQueryVo.getPlateNumber())){
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
		hql.append(" order by orderDate desc");
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public DriverOrderPo getDriverOrder(Long driverOrderId) {
		String hql = "from DriverOrderPo where delStatus=? and id=?";
		DriverOrderPo driverOrderPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), driverOrderId);
		return driverOrderPo;
	}

	public void editDriverOrder(DriverOrderPo driverOrderPo) {
		this.update(driverOrderPo);
	}

	public BigDecimal findDriverOrderTotalAmt(DriverOrderQueryVo driverOrderQueryVo) {
		StringBuffer hql = new StringBuffer("select sum(IFNULL(settlePrice,0) + IFNULL(otherAmt,0))  from DriverOrderPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		if(StringUtils.isNotBlank(driverOrderQueryVo.getContactNumber())){
			hql.append(" and contactNumber like '%").append(driverOrderQueryVo.getContactNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(driverOrderQueryVo.getOwnerName())){
			hql.append(" and ownerName like '%").append(driverOrderQueryVo.getOwnerName()).append("%'");
		}
		if(StringUtils.isNotBlank(driverOrderQueryVo.getPlateNumber())){
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
		hql.append(" order by orderDate desc");
		
		Query query = this.createQuery(hql.toString(), param.toArray());
		BigDecimal totalAmt = new BigDecimal(query.uniqueResult().toString()) ;
		return totalAmt;
	}

	public DriverOrderPo getDriverOrderByOrderId(Long orderId) {
		String hql = "from DriverOrderPo where delStatus=? and orderId=?";
		DriverOrderPo driverOrderPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), orderId);
		return driverOrderPo;
	}
}
