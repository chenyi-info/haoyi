package com.hy.otw.dao.customer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.engine.query.spi.HQLQueryPlan;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.OrderOtherAmtPo;
import com.hy.otw.po.OrderPo;
import com.hy.otw.vo.query.OrderOtherAmtQueryVo;

@Repository
public class OrderOtherAmtDao extends HibernateDao<OrderOtherAmtPo, Long>{

	public void addOrderOtherAmt(OrderOtherAmtPo orderOtherAmtPo) {
		this.save(orderOtherAmtPo);
	}

	public Pagination findOrderOtherAmtList(OrderOtherAmtQueryVo orderOtherAmtQueryVo) {
		StringBuffer hql = new StringBuffer("from OrderOtherAmtPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<OrderOtherAmtPo> page = new Page<OrderOtherAmtPo>();
		page.setPageNo(orderOtherAmtQueryVo.getPage());
		page.setPageSize(orderOtherAmtQueryVo.getRows());
		
		if(orderOtherAmtQueryVo.getOrderId() != null){
			hql.append(" and orderId = ?");
			param.add(orderOtherAmtQueryVo.getOrderId());
		}
		
		if(StringUtils.isNotBlank(orderOtherAmtQueryVo.getOrderNO())){
			hql.append(" and orderNO like '%").append(orderOtherAmtQueryVo.getOrderNO()).append("%'");
		}
		
		if(StringUtils.isNotBlank(orderOtherAmtQueryVo.getItemName())){
			hql.append(" and itemName like '%").append(orderOtherAmtQueryVo.getItemName()).append("%'");
		}
		
		if(StringUtils.isNotBlank(orderOtherAmtQueryVo.getAddress())){
			hql.append(" and address like '%").append(orderOtherAmtQueryVo.getAddress()).append("%'");
		}
		
		if(StringUtils.isNotBlank(orderOtherAmtQueryVo.getCabinetNumber())){
			hql.append(" and cabinetNumber like '%").append(orderOtherAmtQueryVo.getCabinetNumber()).append("%'");
		}
		
		if(StringUtils.isNotBlank(orderOtherAmtQueryVo.getTargetName())){
			hql.append(" and targetName like '%").append(orderOtherAmtQueryVo.getTargetName()).append("%'");
		}
		
		if(orderOtherAmtQueryVo.getIsSettle() != null){
			hql.append(" and isSettle = ?");
			param.add(orderOtherAmtQueryVo.getIsSettle());
		}
		
		if(orderOtherAmtQueryVo.getPropertyType() != null){
			hql.append(" and propertyType = ?");
			param.add(orderOtherAmtQueryVo.getPropertyType());
		}
		if(orderOtherAmtQueryVo.getExpenditureDateBegin() != null){
			hql.append(" and expenditureDate > ?");
			param.add(orderOtherAmtQueryVo.getExpenditureDateBegin());
		}
		if(orderOtherAmtQueryVo.getExpenditureDateEnd() != null){
			hql.append(" and expenditureDate < ?");
			param.add(orderOtherAmtQueryVo.getExpenditureDateEnd());
		}
		hql.append(" order by expenditureDate desc");
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public OrderOtherAmtPo getOrderOtherAmt(Long orderOtherAmtId) {
		String hql = "from OrderOtherAmtPo where delStatus=? and id=?";
		OrderOtherAmtPo orderOtherAmtPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), orderOtherAmtId);
		return orderOtherAmtPo;
	}

	public void editOrderOtherAmt(OrderOtherAmtPo orderOtherAmtPo) {
		this.update(orderOtherAmtPo);
	}

	public void statisticalAmount(Long orderId) {
		String sql = "update order_info as oi set oi.other_amt = (SELECT sum(oa.price) from order_other_amt as oa where oa.del_status = ? and oa.order_id=?) where  oi.del_status = ? and oi.id = ?";
		this.updateSql(sql, DelStatusEnum.NORMAL.getValue(), orderId, DelStatusEnum.NORMAL.getValue(), orderId);
		sql = "update customer_order as oi set oi.other_amt = IFNULL((SELECT sum(oa.price) from order_other_amt as oa where oa.del_status = ? and oa.order_id=? and property_type = 2),0) where  oi.del_status = ? and oi.order_id = ?";
		this.updateSql(sql, DelStatusEnum.NORMAL.getValue(), orderId, DelStatusEnum.NORMAL.getValue(), orderId);
		sql = "update driver_order as oi set oi.other_amt = IFNULL((SELECT sum(oa.price) from order_other_amt as oa where oa.del_status = ? and oa.order_id=? and property_type = 1),0) where  oi.del_status = ? and oi.order_id = ?";
		this.updateSql(sql, DelStatusEnum.NORMAL.getValue(), orderId, DelStatusEnum.NORMAL.getValue(), orderId);
		//实际结算金额
		sql = "update customer_order as oi set oi.settle_price = IFNULL((SELECT sum(oa.price) from order_other_amt as oa where oa.del_status = ? and is_settle = 0 and oa.order_id=? and property_type = 2),0) where  oi.del_status = ? and oi.order_id = ?";
		this.updateSql(sql, DelStatusEnum.NORMAL.getValue(), orderId, DelStatusEnum.NORMAL.getValue(), orderId);
		sql = "update driver_order as oi set oi.settle_price = IFNULL((SELECT sum(oa.price) from order_other_amt as oa where oa.del_status = ? and is_settle = 0 and oa.order_id=? and property_type = 1),0) where  oi.del_status = ? and oi.order_id = ?";
		this.updateSql(sql, DelStatusEnum.NORMAL.getValue(), orderId, DelStatusEnum.NORMAL.getValue(), orderId);
	}

	public void deleteOrder(Long orderId) {
		String sql = "update order_other_amt set del_status=? where order_id=?";
		this.updateSql(sql, DelStatusEnum.HIDE.getValue(), orderId);
	}

	public void editOrderInfo(OrderPo orderPo) {
		String sql = "update order_other_amt set order_no=?,cabinet_model=?,cabinet_number=?,seal_number=?,address=? where order_id=?";
		this.updateSql(sql, orderPo.getOrderNO(), orderPo.getCabinetModel(),orderPo.getCabinetNumber(),orderPo.getSealNumber(),orderPo.getAddress(), orderPo.getId());
		sql = "update order_other_amt as ooa set ooa.target_name = ? where ooa.property_type = 1 and ooa.order_id = ?";
		this.updateSql(sql, orderPo.getPlateNumber(), orderPo.getId());
		
	}

	public void batchSettles(List<Long> orderOtherAmtIdList) {
		String hql = "update OrderOtherAmtPo set isSettle = 0 where id in (:idList)";
		Query query = this.createQuery(hql);
		query.setParameterList("idList", orderOtherAmtIdList);
		query.executeUpdate();
	}

	public List<OrderOtherAmtPo> findOrderOtherAmtList(List<Long> orderIdlist, Integer propertyType) {
		String hql = "from OrderOtherAmtPo where orderId in (:orderIdlist) and propertyType =:propertyType";
		Query query = this.createQuery(hql);
		query.setParameterList("orderIdlist", orderIdlist);
		query.setParameter("propertyType", propertyType);
		return query.list();
	}
	
	public List<OrderOtherAmtPo> findOrderOtherAmtListByIds(List<Long> idlist) {
		String hql = "from OrderOtherAmtPo where id in (:idlist)";
		Query query = this.createQuery(hql);
		query.setParameterList("idlist", idlist);
		return query.list();
	}
}
