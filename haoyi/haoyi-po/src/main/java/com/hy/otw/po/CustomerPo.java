package com.hy.otw.po;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 客户信息管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
@Entity
@Table(name="customer")
public class CustomerPo extends BasePo{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9192341493835705513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;				//主键
	
	@Column(name = "company_name")
	private String companyName;		//公司名称
	
	@Column(name = "address")
	private String address;			//公司地址
	
	@Column(name = "contact_name")
	private String contactName;		//联系人姓名
	
	@Column(name = "contact_number")
	private String contactNumber;	//联系电话
	
	@Column(name = "settle_interval")
	private Long settleInterval;	//结算周期
	
	@Column(name = "remarks")
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
