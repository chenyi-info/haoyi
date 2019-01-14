package com.hy.otw.po.sys;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.hy.otw.po.BasePo;

/**
 * 用户菜单管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
@Entity
@Table(name="user_resources")
public class UserResourcesPo extends BasePo{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9192341493835705513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;				//主键
	
	@Column(name = "user_id")
	private Long userId;		//用户id
	
	@Column(name = "menu_id")
	private Long menuId;		//菜单id
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
