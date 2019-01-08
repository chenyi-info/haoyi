package com.hy.otw.vo.query;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 用户信息管理
 * @author chenyi_info@126.com
 */
@Getter
@Setter
public class UserInfoQueryVo {
	
	private String userName;		//用户姓名
	
	private String userAccount;		//用户账号
	
	private String contactNumber;	//联系电话
	
	private int page = 1;
	
	private int rows = 10;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
