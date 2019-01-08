package com.hy.otw.dao.customer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.CustomerOperatorPo;
import com.hy.otw.vo.query.CustomerQueryVo;

@Repository
public class CustomerOperatorDao extends HibernateDao<CustomerOperatorPo, Long>{

	public void addCustomerOperator(CustomerOperatorPo customerOperatorPo) {
		this.save(customerOperatorPo);
	}

	public Pagination findCustomerOperatorList(CustomerQueryVo customerQueryVo) {
		StringBuffer hql = new StringBuffer("from CustomerOperatorPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<CustomerOperatorPo> page = new Page<CustomerOperatorPo>();
		page.setPageNo(customerQueryVo.getPage());
		page.setPageSize(customerQueryVo.getRows());
		if(customerQueryVo.getCompanyId() != null){
			hql.append(" and customerId = ?");
			param.add(customerQueryVo.getCompanyId());
		}
		if(StringUtils.isNotBlank(customerQueryVo.getContactName())){
			hql.append(" and contactName like '%").append(customerQueryVo.getContactName()).append("%'");
		}
		hql.append(" order by updateDate desc");
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public CustomerOperatorPo getCustomerOperator(Long customerOperatorId) {
		String hql = "from CustomerOperatorPo where delStatus=? and id=?";
		CustomerOperatorPo customerOperatorPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), customerOperatorId);
		return customerOperatorPo;
	}

	public void editCustomerOperator(CustomerOperatorPo customerOperatorPo) {
		this.update(customerOperatorPo);
	}
}
