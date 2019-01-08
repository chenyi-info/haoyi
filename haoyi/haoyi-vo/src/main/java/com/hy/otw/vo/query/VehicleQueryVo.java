package com.hy.otw.vo.query;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 车辆信息
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class VehicleQueryVo {
	
	private String plateNumber;		//车牌号
	
	private String ownerName;		//司机姓名
	
	private String contactNumber;	//联系电话
	
	private String vehicleType;		//车辆类型

	private int page = 1;
	private int rows = 10;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
