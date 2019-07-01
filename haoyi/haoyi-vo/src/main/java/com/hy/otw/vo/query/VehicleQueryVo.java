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
	
	private String vehicleSource;	//车辆来源: 1-合作车辆 2-固定车辆 3-散找车辆

	private int page = 1;
	private int rows = 10;
	
	private String sort = "createDate";
	
	private String order = "desc";
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
