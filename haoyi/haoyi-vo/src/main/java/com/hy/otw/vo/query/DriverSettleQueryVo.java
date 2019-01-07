package com.hy.otw.vo.query;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 司机结算管理
 * @author chenyi_info@126.com
 */
@Getter
@Setter
public class DriverSettleQueryVo {
	
	private Integer id;				//主键
	
	private String plateNumber;		//车牌号
	
	private String ownerName;		//司机姓名
	
	private String contactNumber;	//联系电话
	
	private Date settleDateBegin;	//结算日期
	
	private Date settleDateEnd;		//结算日期
	
	private int page = 1;
	
	private int rows = 10;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
