package com.hy.otw.vo;


import java.math.BigDecimal;
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
public class VehicleExpenditureVo extends BaseVo{
	
	private Long id;				//主键
	
	private String plateNumber;		//车牌号
	
	private String ownerName;		//司机姓名
	
	private String contactNumber;	//联系电话
	
	private Date expenditureDate;	//支出日期
	
	private BigDecimal price;		//支出金额
	
	private String itemName;		//支出项目
	
	private String transactorName;	//经手人
	
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
