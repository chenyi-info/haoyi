package com.hy.otw.vo;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 用户信息管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class UserInfoVo extends BaseVo{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9192341493835705516L;

	private Long id;				//主键
	
	private String userName;		//用户名字
	
	private String userAccount;		//账号
	
	private String password;		//密码
	
	private String contactNumber;	//联系电话
	
	private String salt;			//密码盐
	
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
