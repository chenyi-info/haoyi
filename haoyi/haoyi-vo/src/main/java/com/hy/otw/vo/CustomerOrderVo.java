package com.hy.otw.vo;


import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 客户账单管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class CustomerOrderVo extends BaseVo{
	
	private Long id;				//主键
	
	private Long customerId;		//客户id
	
	private String companyName;		//客户公司名称
	
	private Long orderId;			//订单Id
	
	private Date orderDate;			//订单日期
	
	private String orderNO;			//订单编号
	
	private String cabinetModel;	//柜型
	
	private String cabinetNumber;	//柜号
	
	private String sealNumber;		//封号
	
	private String address;			//订单地址
	
	private BigDecimal customerPrice;	//订单金额(客单价)
	
	private BigDecimal settlePrice;	//结算金额
	
	private BigDecimal otherAmt;	//其它金额(运费)
	
	private Integer settleStatus;	//结算状态：0-未结算；1-已结算
	
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
