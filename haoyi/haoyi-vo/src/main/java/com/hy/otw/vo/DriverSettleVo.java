package com.hy.otw.vo;


import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 司机结算管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class DriverSettleVo extends BaseVo {

	private Long id;				//主键
	
	private String plateNumber;		//车牌号
	
	private String ownerName;		//司机姓名
	
	private String contactNumber;	//联系电话
	
	private String itemName;		//结账类型
	
	private BigDecimal shouldPrice;		//应结账金额
	
	private BigDecimal actualPrice;		//实际结账金额
	
	private Date settleDate;			//结算日期
	
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
