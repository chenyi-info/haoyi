package com.hy.otw.dao.vehicle;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.VehiclePo;
import com.hy.otw.vo.query.VehicleQueryVo;

@Repository
public class VehicleDao extends HibernateDao<VehiclePo, Long>{

	public void addVehicle(VehiclePo vehiclePo) {
		this.save(vehiclePo);
	}

	public Pagination findVehicleList(VehicleQueryVo vehicleQueryVo) {
		StringBuffer hql = new StringBuffer("from VehiclePo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<VehiclePo> page = new Page<VehiclePo>();
		page.setPageNo(vehicleQueryVo.getPage());
		page.setPageSize(vehicleQueryVo.getRows());
		if(StringUtils.isNotBlank(vehicleQueryVo.getContactNumber())){
			hql.append(" and contactNumber like '%").append(vehicleQueryVo.getContactNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(vehicleQueryVo.getOwnerName())){
			hql.append(" and ownerName like '%").append(vehicleQueryVo.getOwnerName()).append("%'");
		}
		if(StringUtils.isNotBlank(vehicleQueryVo.getPlateNumber())){
			hql.append(" and plateNumber like '%").append(vehicleQueryVo.getPlateNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(vehicleQueryVo.getVehicleType())){
			hql.append(" and vehicleType = ?");
			param.add(vehicleQueryVo.getVehicleType());
		}
		if(StringUtils.isNotBlank(vehicleQueryVo.getVehicleSource())){
			hql.append(" and vehicleSource = ?");
			param.add(vehicleQueryVo.getVehicleSource());
		}
		hql.append(" order by createDate desc");
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public VehiclePo getVehicle(Long vehicleId) {
		String hql = "from VehiclePo where delStatus=? and id=?";
		VehiclePo vehiclePo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), vehicleId);
		return vehiclePo;
	}

	public void editVehicle(VehiclePo vehiclePo) {
		this.update(vehiclePo);
	}

	public VehiclePo getVehicleByPlateNumber(String plateNumber) {
		String hql = "from VehiclePo where delStatus=? and plateNumber=?";
		VehiclePo vehiclePo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), plateNumber);
		return vehiclePo;
	}
}
