package com.hy.otw.vo.query;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 车辆日常费用管理
 * @author chenyi_info@126.com
 */
@Getter
@Setter
public class VehicleExpenditureQueryVo {
	
	private String plateNumber;		//车牌号
	
	private String ownerName;		//司机姓名
	
	private String contactNumber;	//联系电话
	
	private Date expenditureDateBegin;	//支出日期
	
	private Date expenditureDateEnd;	//支出日期
	
	private String transactorName;	//经手人
	
	private int page = 1;
	
	private int rows = 10;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
