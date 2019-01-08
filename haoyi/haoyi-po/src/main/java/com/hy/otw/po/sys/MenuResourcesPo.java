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
 * 系统菜单管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
@Entity
@Table(name="menu_resources")
public class MenuResourcesPo extends BasePo{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9192341493835705513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;				//主键
	
	@Column(name = "menu_name")
	private String menuName;		//菜单名称
	
	@Column(name = "menu_path")
	private String menuPath;		//菜单路径
	
	@Column(name = "level")
	private Integer level;			//菜单级别1:一级，2：二级，3：三级（功能菜单）

	@Column(name = "fun_code")
	private String funcode;			//菜单编码
	
	@Column(name = "parent_id")
	private Long parentId;			//父级菜单id
	
	@Column(name = "status")
	private Integer status;			//状态1：正常 0：关闭
	
	@Column(name = "remarks")
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
