package com.hy.otw.dao.customer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.hy.otw.common.enums.DelStatusEnum;
import com.hy.otw.hibernate.utils.HibernateDao;
import com.hy.otw.hibernate.utils.Page;
import com.hy.otw.hibernate.utils.Pagination;
import com.hy.otw.po.CustomerPo;
import com.hy.otw.vo.query.CustomerQueryVo;

@Repository
public class CustomerDao extends HibernateDao<CustomerPo, Long>{

	public void addCustomer(CustomerPo CustomerPo) {
		this.save(CustomerPo);
	}

	public Pagination findCustomerList(CustomerQueryVo customerQueryVo) {
		StringBuffer hql = new StringBuffer("from CustomerPo where delStatus=?");
		List<Object> param = new ArrayList<Object>();
		param.add(DelStatusEnum.NORMAL.getValue());
		Page<CustomerPo> page = new Page<CustomerPo>();
		page.setPageNo(customerQueryVo.getPage());
		page.setPageSize(customerQueryVo.getRows());
		if(StringUtils.isNotBlank(customerQueryVo.getCompanyName())){
			hql.append(" and companyName like '%").append(customerQueryVo.getCompanyName()).append("%'");
		}
		if(StringUtils.isNotBlank(customerQueryVo.getContactNumber())){
			hql.append(" and contactNumber like '%").append(customerQueryVo.getContactNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(customerQueryVo.getContactName())){
			hql.append(" and contactName like '%").append(customerQueryVo.getContactName()).append("%'");
		}
		hql.append(" order by ").append(customerQueryVo.getSort()).append(" ").append(customerQueryVo.getOrder());
		
		Pagination pagination = this.findPagination(page, hql.toString(), param.toArray());
		return pagination;
	}

	public CustomerPo getCustomer(Long customerId) {
		String hql = "from CustomerPo where delStatus=? and id=?";
		CustomerPo customerPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), customerId);
		return customerPo;
	}

	public void editCustomer(CustomerPo customerPo) {
		this.update(customerPo);
	}

	public CustomerPo getCustomerByCompanyName(String companyName) {
		String hql = "from CustomerPo where delStatus=? and companyName=?";
		CustomerPo customerPo = this.findUnique(hql, DelStatusEnum.NORMAL.getValue(), companyName);
		return customerPo;
	}
}
