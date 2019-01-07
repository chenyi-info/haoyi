package com.hy.otw.vo;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 数据字典项管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class DictionaryItemVo extends BaseVo{

	private Long id;				//主键
	
	private String dictionaryCode;	//字典编码
	
	private String itemName;		//字典项名称
	
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
