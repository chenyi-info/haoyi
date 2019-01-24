package com.hy.otw.vo;


import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 车辆信息
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class VehicleVo extends BaseVo{
	
	private Long id;				//主键
	
	private String plateNumber;		//车牌号
	
	private String ownerName;		//司机姓名
	
	private String contactNumber;	//联系电话
	
	private String vehicleType;		//车辆类型
	
	private String vehicleSource;	//车辆来源: 1-合作车辆 2-固定车辆 3-散找车辆
	
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
