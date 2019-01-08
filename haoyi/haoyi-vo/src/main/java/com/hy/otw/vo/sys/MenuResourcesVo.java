package com.hy.otw.vo.sys;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.hy.otw.vo.BaseVo;

/**
 * 系统菜单管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class MenuResourcesVo extends BaseVo{


	private Long id;				//主键
	
	private String menuName;		//菜单名称
	
	private String menuPath;		//菜单路径
	
	private Integer level;			//菜单级别1:一级，2：二级，3：三级（功能菜单）

	private String funcode;			//菜单编码
	
	private Long parentId;			//父级菜单id
	
	private Integer status;			//状态1：正常 0：关闭
	
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
