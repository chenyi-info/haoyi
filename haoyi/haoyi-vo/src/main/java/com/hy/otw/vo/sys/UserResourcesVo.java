package com.hy.otw.vo.sys;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.hy.otw.vo.BaseVo;

/**
 * 用户菜单管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class UserResourcesVo extends BaseVo{
	private Long id;				//主键
	
	private Long userId;		//用户id
	
	private Long menuId;		//菜单id
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
