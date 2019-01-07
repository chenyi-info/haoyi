package com.hy.otw.vo;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 基础VO信息
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class BaseVo {
	 private Integer delStatus; 	//逻辑删除状态：0-正常；1-删除即隐藏
	 private Long createBy;			//创建者ID
	 private Date createDate;		//创建时间
	 private Long updateBy;			//更新者ID
	 private Date updateDate;		//更新时间
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
