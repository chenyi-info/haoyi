package com.hy.otw.vo.query.sys;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 系统菜单管理
 * @author chenyi_info@126.com
 */
@Getter
@Setter
public class MenuResourcesQueryVo {
	
	private String menuName;		//菜单名称
	
	private int page = 1;
	
	private int rows = 10;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
