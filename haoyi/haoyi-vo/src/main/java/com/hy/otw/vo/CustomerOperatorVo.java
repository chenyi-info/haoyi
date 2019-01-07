package com.hy.otw.vo;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 客户操作人信息管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class CustomerOperatorVo extends BaseVo{

	private Long id;				//主键
	
	private Long customerId;		//公司Id
	
	private String contactName;		//联系人姓名
	
	private String contactNumber;	//联系电话
	
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
