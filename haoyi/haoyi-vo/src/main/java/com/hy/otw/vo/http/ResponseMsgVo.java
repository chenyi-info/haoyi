package com.hy.otw.vo.http;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * httpResponse返回信息
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class ResponseMsgVo {
	
	private Integer status = 200;		//状态
	
	private String statusCode;	//状态码
	
	private String msg = "操作成功";		//信息

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
