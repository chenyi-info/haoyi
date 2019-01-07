package com.hy.otw.vo;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 客户信息管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class CustomerVo extends BaseVo{

	private Long id;				//主键
	
	private String companyName;		//公司名称
	
	private String address;			//公司地址
	
	private String contactName;		//联系人姓名
	
	private String contactNumber;	//联系电话
	
	private Long settleInterval;	//结算周期
	
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
