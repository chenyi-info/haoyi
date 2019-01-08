package com.hy.otw.dao.driver;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.DriverSettlePo;
import com.hy.otw.vo.query.DriverSettleQueryVo;

@Repository
public class DriverSettleDao extends HibernateDao<DriverSettlePo, Long>{

	public void addDriverSettle(DriverSettlePo driverSettlePo) {
		this.save(driverSettlePo);
	}

	public Pagination findDriverSettleList(DriverSettleQueryVo driverSettleQueryVo) {
		StringBuffer hql = new StringBuffer("from DriverSettlePo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<DriverSettlePo> page = new Page<DriverSettlePo>();
		page.setPageNo(driverSettleQueryVo.getPage());
		page.setPageSize(driverSettleQueryVo.getRows());
		if(StringUtils.isNotBlank(driverSettleQueryVo.getContactNumber())){
			hql.append(" and contactNumber like '%").append(driverSettleQueryVo.getContactNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(driverSettleQueryVo.getOwnerName())){
			hql.append(" and ownerName like '%").append(driverSettleQueryVo.getOwnerName()).append("%'");
		}
		if(StringUtils.isNotBlank(driverSettleQueryVo.getPlateNumber())){
			hql.append(" and plateNumber like '%").append(driverSettleQueryVo.getPlateNumber()).append("%'");
		}
		if(driverSettleQueryVo.getSettleDateBegin() != null){
			hql.append(" and settleDate > ?");
			param.add(driverSettleQueryVo.getSettleDateBegin());
		}
		if(driverSettleQueryVo.getSettleDateEnd() != null){
			hql.append(" and settleDate < ?");
			param.add(driverSettleQueryVo.getSettleDateEnd());
		}
		hql.append(" order by settleDate desc");
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public DriverSettlePo getDriverSettle(Long driverSettleId) {
		String hql = "from DriverSettlePo where delStatus=? and id=?";
		DriverSettlePo driverSettlePo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), driverSettleId);
		return driverSettlePo;
	}

	public void editDriverSettle(DriverSettlePo driverSettlePo) {
		this.update(driverSettlePo);
	}
}
