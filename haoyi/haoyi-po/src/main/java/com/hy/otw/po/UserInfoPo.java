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
 * 用户信息管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
@Entity
@Table(name="user_info")
public class UserInfoPo extends BasePo{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9192341493835705513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;				//主键
	
	@Column(name = "user_name")
	private String userName;		//用户名字
	
	@Column(name = "user_account")
	private String userAccount;		//账号
	
	@Column(name = "password")
	private String password;		//密码
	
	@Column(name = "contact_number")
	private String contactNumber;	//联系电话
	
	@Column(name = "salt")
	private String salt;			//密码盐
	
	@Column(name = "remarks")
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
