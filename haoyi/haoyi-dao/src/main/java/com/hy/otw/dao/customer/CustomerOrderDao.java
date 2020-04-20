package com.hy.otw.dao.customer;

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
import com.hy.otw.po.CustomerOrderPo;
import com.hy.otw.vo.query.CustomerOrderQueryVo;

@Repository
public class CustomerOrderDao extends HibernateDao<CustomerOrderPo, Long>{

	public void addCustomerOrder(CustomerOrderPo customerOrderPo) {
		this.save(customerOrderPo);
	}

	public Pagination findCustomerOrderList(CustomerOrderQueryVo customerOrderQueryVo) {
		StringBuffer hql = new StringBuffer("from CustomerOrderPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<CustomerOrderPo> page = new Page<CustomerOrderPo>();
		page.setPageNo(customerOrderQueryVo.getPage());
		page.setPageSize(customerOrderQueryVo.getRows());
		if(StringUtils.isNotBlank(customerOrderQueryVo.getCompanyName())){
			hql.append(" and companyName like '%").append(customerOrderQueryVo.getCompanyName()).append("%'");
		}
		
		if(StringUtils.isNotBlank(customerOrderQueryVo.getOrderNO())){
			hql.append(" and orderNO like '%").append(customerOrderQueryVo.getOrderNO()).append("%'");
		}
		if(StringUtils.isNotBlank(customerOrderQueryVo.getAddress())){
			hql.append(" and address like '%").append(customerOrderQueryVo.getAddress()).append("%'");
		}
		
		if(StringUtils.isNotBlank(customerOrderQueryVo.getCabinetNumber())){
			hql.append(" and cabinetNumber like '%").append(customerOrderQueryVo.getCabinetNumber()).append("%'");
		}
		
		if(customerOrderQueryVo.getSettleStatus() != null){
			if(customerOrderQueryVo.getSettleStatus() == 0 ) {
				hql.append(" and settleStatus in (0,2) ");
			}else {
				hql.append(" and settleStatus = ? ");
				param.add(customerOrderQueryVo.getSettleStatus());
			}
		}
		
		if(customerOrderQueryVo.getOrderDateBegin() != null){
			hql.append(" and orderDate > ?");
			param.add(customerOrderQueryVo.getOrderDateBegin());
		}
		if(customerOrderQueryVo.getOrderDateEnd() != null){
			hql.append(" and orderDate < ?");
			param.add(customerOrderQueryVo.getOrderDateEnd());
		}
		//如果要查询司机信息
		if(StringUtils.isNotBlank(customerOrderQueryVo.getContactNumber()) || StringUtils.isNotBlank(customerOrderQueryVo.getOwnerName()) || StringUtils.isNotBlank(customerOrderQueryVo.getPlateNumber())) {
			hql.append(" and orderId in (select id from OrderPo where delStatus=? ");
			param.add(DelStatusEnum.NORMAL.getValue());
			if(StringUtils.isNotBlank(customerOrderQueryVo.getContactNumber())){
				hql.append(" and contactNumber like '%").append(customerOrderQueryVo.getContactNumber()).append("%'");
			}
			if(StringUtils.isNotBlank(customerOrderQueryVo.getOwnerName())){
				hql.append(" and ownerName like '%").append(customerOrderQueryVo.getOwnerName()).append("%'");
			}
			if(StringUtils.isNotBlank(customerOrderQueryVo.getPlateNumber())){
				hql.append(" and plateNumber like '%").append(customerOrderQueryVo.getPlateNumber()).append("%'");
			}
			hql.append(")");
		}
		
		hql.append(" order by ").append(customerOrderQueryVo.getSort()).append(" ").append(customerOrderQueryVo.getOrder());
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public CustomerOrderPo getCustomerOrder(Long customerOrderId) {
		String hql = "from CustomerOrderPo where delStatus=? and id=?";
		CustomerOrderPo customerOrderPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), customerOrderId);
		return customerOrderPo;
	}

	public void editCustomerOrder(CustomerOrderPo customerOrderPo) {
		this.update(customerOrderPo);
	}

	public BigDecimal findCustomerOrderTotalAmt(CustomerOrderQueryVo customerOrderQueryVo) {
		StringBuffer hql = new StringBuffer("select sum(IFNULL(customerPrice,0) + IFNULL(otherAmt,0) - IFNULL(settlePrice,0)) from CustomerOrderPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		if(StringUtils.isNotBlank(customerOrderQueryVo.getCompanyName())){
			hql.append(" and companyName like '%").append(customerOrderQueryVo.getCompanyName()).append("%'");
		}
		
		if(StringUtils.isNotBlank(customerOrderQueryVo.getOrderNO())){
			hql.append(" and orderNO like '%").append(customerOrderQueryVo.getOrderNO()).append("%'");
		}
		if(StringUtils.isNotBlank(customerOrderQueryVo.getAddress())){
			hql.append(" and address like '%").append(customerOrderQueryVo.getAddress()).append("%'");
		}
		
		if(StringUtils.isNotBlank(customerOrderQueryVo.getCabinetNumber())){
			hql.append(" and cabinetNumber like '%").append(customerOrderQueryVo.getCabinetNumber()).append("%'");
		}
		if(customerOrderQueryVo.getSettleStatus() != null){
			hql.append(" and settleStatus = ? ");
			param.add(customerOrderQueryVo.getSettleStatus());
		}
		
		if(customerOrderQueryVo.getOrderDateBegin() != null){
			hql.append(" and orderDate > ?");
			param.add(customerOrderQueryVo.getOrderDateBegin());
		}
		if(customerOrderQueryVo.getOrderDateEnd() != null){
			hql.append(" and orderDate < ?");
			param.add(customerOrderQueryVo.getOrderDateEnd());
		}
		
		//如果要查询司机信息
		if(StringUtils.isNotBlank(customerOrderQueryVo.getContactNumber()) || StringUtils.isNotBlank(customerOrderQueryVo.getOwnerName()) || StringUtils.isNotBlank(customerOrderQueryVo.getPlateNumber())) {
			hql.append(" and orderId in (select id from OrderPo where delStatus=? ");
			param.add(DelStatusEnum.NORMAL.getValue());
			if(StringUtils.isNotBlank(customerOrderQueryVo.getContactNumber())){
				hql.append(" and contactNumber like '%").append(customerOrderQueryVo.getContactNumber()).append("%'");
			}
			if(StringUtils.isNotBlank(customerOrderQueryVo.getOwnerName())){
				hql.append(" and ownerName like '%").append(customerOrderQueryVo.getOwnerName()).append("%'");
			}
			if(StringUtils.isNotBlank(customerOrderQueryVo.getPlateNumber())){
				hql.append(" and plateNumber like '%").append(customerOrderQueryVo.getPlateNumber()).append("%'");
			}
			hql.append(")");
		}
				
		hql.append(" order by ").append(customerOrderQueryVo.getSort()).append(" ").append(customerOrderQueryVo.getOrder());
		
		Query query = this.createQuery(hql.toString(), param.toArray());
		Object amt = query.uniqueResult();
		BigDecimal totalAmt = new BigDecimal( amt == null ? "0" : amt.toString());
		return totalAmt;
	}

	public CustomerOrderPo getCustomerOrderByOrderId(Long orderId) {
		String hql = "from CustomerOrderPo where delStatus=? and orderId=?";
		CustomerOrderPo customerOrderPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), orderId);
		return customerOrderPo;
	}

	public void batchSettles(List<Long> customerOrderIdList) {
		String hql = "update CustomerOrderPo set settleStatus = 1, settle_date = now() where id in (:idList)";
		Query query = this.createQuery(hql);
		query.setParameterList("idList", customerOrderIdList);
		query.executeUpdate();
		
		hql = "update OrderOtherAmtPo set is_settle = 0, settle_date = now() where order_id in (select orderId from CustomerOrderPo where id in (:idList)) and property_type = 2 and del_status = 0";
		query = this.createQuery(hql);
		query.setParameterList("idList", customerOrderIdList);
		query.executeUpdate();
	}

	public void batchLockOrUnLock(List<Long> customerOrderIdList, int status) {
		String hql = "update CustomerOrderPo set settleStatus =:status, update_date = now() where id in (:idList)";
		Query query = this.createQuery(hql);
		query.setParameter("status", status);
		query.setParameterList("idList", customerOrderIdList);
		query.executeUpdate();
	}
}
