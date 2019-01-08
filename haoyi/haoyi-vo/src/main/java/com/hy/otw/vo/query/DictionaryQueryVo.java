package com.hy.otw.vo.query;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 数据字典查询
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class DictionaryQueryVo {
	
	
	private String dictionaryName;		//字典名称
	private String dictionaryCode;		//字典编码
	
	private int page = 1;
	private int rows = 10;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
