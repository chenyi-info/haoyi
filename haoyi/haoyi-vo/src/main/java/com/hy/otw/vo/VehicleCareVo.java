package com.hy.otw.vo;


import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 车辆保养管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class VehicleCareVo extends BaseVo{

	private Long id;				//主键
	
	private String plateNumber;		//车牌号
	
	private String ownerName;		//司机姓名
	
	private String contactNumber;	//联系电话
	
	private Date careDate;			//保养日期
	
	private BigDecimal price;		//保养金额
	  
	private String itemName;		//保养项目
	
	private Long careInterval;		//保养间距(天)
	
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
