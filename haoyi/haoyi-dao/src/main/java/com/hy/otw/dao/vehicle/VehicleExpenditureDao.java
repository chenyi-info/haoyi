package com.hy.otw.dao.vehicle;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.VehicleExpenditurePo;
import com.hy.otw.vo.query.VehicleExpenditureQueryVo;

@Repository
public class VehicleExpenditureDao extends HibernateDao<VehicleExpenditurePo, Long>{

	public void addVehicleExpenditure(VehicleExpenditurePo vehicleExpenditurePo) {
		this.save(vehicleExpenditurePo);
	}

	public Pagination findVehicleExpenditureList(VehicleExpenditureQueryVo vehicleExpenditureQueryVo) {
		StringBuffer hql = new StringBuffer("from VehicleExpenditurePo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<VehicleExpenditurePo> page = new Page<VehicleExpenditurePo>();
		page.setPageNo(vehicleExpenditureQueryVo.getPage());
		page.setPageSize(vehicleExpenditureQueryVo.getRows());
		if(StringUtils.isNotBlank(vehicleExpenditureQueryVo.getContactNumber())){
			hql.append(" and contactNumber like '%").append(vehicleExpenditureQueryVo.getContactNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(vehicleExpenditureQueryVo.getOwnerName())){
			hql.append(" and ownerName like '%").append(vehicleExpenditureQueryVo.getOwnerName()).append("%'");
		}
		if(StringUtils.isNotBlank(vehicleExpenditureQueryVo.getPlateNumber())){
			hql.append(" and plateNumber like '%").append(vehicleExpenditureQueryVo.getPlateNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(vehicleExpenditureQueryVo.getTransactorName())){
			hql.append(" and transactorName like '%").append(vehicleExpenditureQueryVo.getTransactorName()).append("%'");
		}
		if(vehicleExpenditureQueryVo.getExpenditureDateBegin() != null){
			hql.append(" and expenditureDate > ?");
			param.add(vehicleExpenditureQueryVo.getExpenditureDateBegin());
		}
		if(vehicleExpenditureQueryVo.getExpenditureDateEnd() != null){
			hql.append(" and expenditureDate < ?");
			param.add(vehicleExpenditureQueryVo.getExpenditureDateEnd());
		}
		hql.append(" order by expenditureDate desc");
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public VehicleExpenditurePo getVehicleExpenditure(Long vehicleExpenditureId) {
		String hql = "from VehicleExpenditurePo where delStatus=? and id=?";
		VehicleExpenditurePo vehicleExpenditurePo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), vehicleExpenditureId);
		return vehicleExpenditurePo;
	}

	public void editVehicleExpenditure(VehicleExpenditurePo vehicleExpenditurePo) {
		this.update(vehicleExpenditurePo);
	}
}
