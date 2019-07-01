package com.hy.otw.dao.vehicle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.VehicleCarePo;
import com.hy.otw.vo.query.VehicleCareQueryVo;

@Repository
public class VehicleCareDao extends HibernateDao<VehicleCarePo, Long>{

	public void addVehicleCare(VehicleCarePo vehicleCarePo) {
		this.save(vehicleCarePo);
	}

	public Pagination findVehicleCareList(VehicleCareQueryVo vehicleCareQueryVo) {
		StringBuffer hql = new StringBuffer("from VehicleCarePo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<VehicleCarePo> page = new Page<VehicleCarePo>();
		page.setPageNo(vehicleCareQueryVo.getPage());
		page.setPageSize(vehicleCareQueryVo.getRows());
		if(StringUtils.isNotBlank(vehicleCareQueryVo.getContactNumber())){
			hql.append(" and contactNumber like '%").append(vehicleCareQueryVo.getContactNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(vehicleCareQueryVo.getOwnerName())){
			hql.append(" and ownerName like '%").append(vehicleCareQueryVo.getOwnerName()).append("%'");
		}
		if(StringUtils.isNotBlank(vehicleCareQueryVo.getPlateNumber())){
			hql.append(" and plateNumber like '%").append(vehicleCareQueryVo.getPlateNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(vehicleCareQueryVo.getItemName())){
			hql.append(" and itemName like '%").append(vehicleCareQueryVo.getItemName()).append("%'");
		}
		if(vehicleCareQueryVo.getCareDateBegin() != null){
			hql.append(" and careDate > ?");
			param.add(vehicleCareQueryVo.getCareDateBegin());
		}
		if(vehicleCareQueryVo.getCareDateEnd() != null){
			hql.append(" and careDate < ?");
			param.add(vehicleCareQueryVo.getCareDateEnd());
		}
		hql.append(" order by ").append(vehicleCareQueryVo.getSort()).append(" ").append(vehicleCareQueryVo.getOrder());
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public VehicleCarePo getVehicleCare(Long vehicleCareId) {
		String hql = "from VehicleCarePo where delStatus=? and id=?";
		VehicleCarePo vehicleCarePo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), vehicleCareId);
		return vehicleCarePo;
	}

	public void editVehicleCare(VehicleCarePo vehicleCarePo) {
		this.update(vehicleCarePo);
	}

	public BigDecimal findVehicleCareTotalAmt(VehicleCareQueryVo vehicleCareQueryVo) {
		StringBuffer hql = new StringBuffer("select sum(price) from VehicleCarePo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		if(StringUtils.isNotBlank(vehicleCareQueryVo.getContactNumber())){
			hql.append(" and contactNumber like '%").append(vehicleCareQueryVo.getContactNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(vehicleCareQueryVo.getOwnerName())){
			hql.append(" and ownerName like '%").append(vehicleCareQueryVo.getOwnerName()).append("%'");
		}
		if(StringUtils.isNotBlank(vehicleCareQueryVo.getPlateNumber())){
			hql.append(" and plateNumber like '%").append(vehicleCareQueryVo.getPlateNumber()).append("%'");
		}
		if(vehicleCareQueryVo.getCareDateBegin() != null){
			hql.append(" and careDate > ?");
			param.add(vehicleCareQueryVo.getCareDateBegin());
		}
		if(vehicleCareQueryVo.getCareDateEnd() != null){
			hql.append(" and careDate < ?");
			param.add(vehicleCareQueryVo.getCareDateEnd());
		}
		hql.append(" order by ").append(vehicleCareQueryVo.getSort()).append(" ").append(vehicleCareQueryVo.getOrder());
		Query query = this.createQuery(hql.toString(), param.toArray());
		BigDecimal totalAmt = (BigDecimal) query.uniqueResult();
		return totalAmt;
	}

	public VehicleCarePo findLastPrevVehicleCarePo(String plateNumber, Date careDate) {
		String hql = "from VehicleCarePo where delStatus=? and plateNumber=? and careDate < ? order by careDate desc ";
		Query query = this.createQuery(hql, DelStatusEnum.NORMAL.getValue(), plateNumber, careDate);
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<VehicleCarePo> vehicleCarePoList = query.list();
		return CollectionUtils.isNotEmpty(vehicleCarePoList) ? vehicleCarePoList.get(0) : null;
	}

	public VehicleCarePo findLastNextVehicleCarePo(String plateNumber, Date careDate) {
		String hql = "from VehicleCarePo where delStatus=? and plateNumber=? and careDate > ? order by careDate asc ";
		Query query = this.createQuery(hql, DelStatusEnum.NORMAL.getValue(), plateNumber, careDate);
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<VehicleCarePo> vehicleCarePoList = query.list();
		return CollectionUtils.isNotEmpty(vehicleCarePoList) ? vehicleCarePoList.get(0) : null;
	}
}
