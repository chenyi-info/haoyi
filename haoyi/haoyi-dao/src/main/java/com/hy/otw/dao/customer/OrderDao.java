package com.hy.otw.dao.customer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.OrderPo;
import com.hy.otw.vo.OrderVo;
import com.hy.otw.vo.query.OrderQueryVo;

@Repository
public class OrderDao extends HibernateDao<OrderPo, Long>{

	public void addOrder(OrderPo orderPo) {
		this.save(orderPo);
	}

	public Pagination findOrderList(OrderQueryVo orderQueryVo) {
		StringBuffer hql = new StringBuffer("from OrderPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<OrderPo> page = new Page<OrderPo>();
		page.setPageNo(orderQueryVo.getPage());
		page.setPageSize(orderQueryVo.getRows());
		if(StringUtils.isNotBlank(orderQueryVo.getContactNumber())){
			hql.append(" and contactNumber like '%").append(orderQueryVo.getContactNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(orderQueryVo.getOwnerName())){
			hql.append(" and ownerName like '%").append(orderQueryVo.getOwnerName()).append("%'");
		}
		if(StringUtils.isNotBlank(orderQueryVo.getPlateNumber())){
			hql.append(" and plateNumber like '%").append(orderQueryVo.getPlateNumber()).append("%'");
		}
		
		if(StringUtils.isNotBlank(orderQueryVo.getOperatorName())){
			hql.append(" and operatorName like '%").append(orderQueryVo.getOperatorName()).append("%'");
		}
		
		if(StringUtils.isNotBlank(orderQueryVo.getOrderNO())){
			hql.append(" and orderNO like '%").append(orderQueryVo.getOrderNO()).append("%'");
		}
		
		if(StringUtils.isNotBlank(orderQueryVo.getAddress())){
			hql.append(" and address like '%").append(orderQueryVo.getAddress()).append("%'");
		}
		
		if(StringUtils.isNotBlank(orderQueryVo.getCabinetNumber())){
			hql.append(" and cabinetNumber like '%").append(orderQueryVo.getCabinetNumber()).append("%'");
		}
		
		if(orderQueryVo.getOrderStatus() != null){
			hql.append(" and orderStatus = ? ");
			param.add(orderQueryVo.getOrderStatus());
		}
		
		if(StringUtils.isNotBlank(orderQueryVo.getCabinetRecipientAddr())){
			hql.append(" and cabinetRecipientAddr = ? ");
			param.add(orderQueryVo.getCabinetRecipientAddr());
		}
		
		if(StringUtils.isNotBlank(orderQueryVo.getCabinetReturnAddr())){
			hql.append(" and cabinetReturnAddr = ? ");
			param.add(orderQueryVo.getCabinetReturnAddr());
		}
		
		if(orderQueryVo.getOrderDateBegin() != null){
			hql.append(" and orderDate > ?");
			param.add(orderQueryVo.getOrderDateBegin());
		}
		if(orderQueryVo.getOrderDateEnd() != null){
			hql.append(" and orderDate < ?");
			param.add(orderQueryVo.getOrderDateEnd());
		}
		hql.append(" order by orderDate asc");
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public OrderPo getOrder(Long orderId) {
		String hql = "from OrderPo where delStatus=? and id=?";
		OrderPo orderPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), orderId);
		return orderPo;
	}

	public void editOrder(OrderPo orderPo) {
		this.update(orderPo);
	}

	public Boolean checkHasOrder(OrderVo orderVo) {
		StringBuffer hqlBuffer = new  StringBuffer("select count(id) from OrderPo where delStatus=? and orderNO=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		param.add(orderVo.getOrderNO());
		if(orderVo.getId() != null){
			hqlBuffer.append(" and id != ?");
			param.add(orderVo.getId());
		}
		if(StringUtils.isNotBlank(orderVo.getCabinetNumber())){
			hqlBuffer.append(" and cabinetNumber != ?");
			param.add(orderVo.getCabinetNumber());
		}
		if(StringUtils.isNotBlank(orderVo.getSealNumber())){
			hqlBuffer.append(" and sealNumber != ?");
			param.add(orderVo.getSealNumber());
		}
		Query query = this.createQuery(hqlBuffer.toString(), param.toArray());
		Long total = (Long) query.uniqueResult();
		return total > 0l;
	}
}
