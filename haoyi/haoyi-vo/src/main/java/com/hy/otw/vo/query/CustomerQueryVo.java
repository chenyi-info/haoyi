package com.hy.otw.vo.query;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 客户信息管理
 * @author chenyi_info@126.com
 */
@Getter
@Setter
public class CustomerQueryVo {
	
	private Long companyId;		//公司Id
	
	private String companyName;		//公司名称
	
	private String contactName;		//联系人姓名
	
	private String contactNumber;	//联系电话
	
	private int page = 1;
	
	private int rows = 10;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
