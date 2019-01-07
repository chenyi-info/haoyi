package com.hy.otw.po;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础PO信息
 * @author chenyi_info@126.com
 */
@Setter
@Getter
@MappedSuperclass
public class BasePo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3609284531961521862L;

	@Column(name = "del_status")
	private Integer delStatus; 	//逻辑删除状态：0-正常；1-删除即隐藏
	
	@Column(name = "create_by")
	private Long createBy;			//创建者ID
	
	@Column(name = "create_date")
	private Date createDate;		//创建时间
	
	@Column(name = "update_by")
	private Long updateBy;			//更新者ID
	
	@Column(name = "update_date")
	private Date updateDate;		//更新时间
	
}
