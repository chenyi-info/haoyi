package com.hy.otw.vo;


import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 订单管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class OrderVo extends BaseVo{
	
	private Long id;				//主键
	
	private String plateNumber;		//车牌号
	
	private String ownerName;		//司机姓名
	
	private String contactNumber;	//联系电话
	
	private String orderNO;			//订单编号
	
	private Date orderDate;			//订单日期
	
	private Long customerId;		//订单客户公司
	
	private String companyName;		//订单客户公司名称
	
	private Long operatorId;		//操作人Id
	
	private String operatorName;	//操作人姓名
	
	private String detailAddress;	//订单详址
	
	private String address;			//订单简址
	
	private BigDecimal weighed;		//重量(T)
	
	private String cabinetModel;	//柜型
	
	private String cabinetNumber;	//柜号
	
	private String sealNumber;		//封号
	
	private BigDecimal orderPrice;	//订单金额
	
	private BigDecimal otherAmt;	//其它金额(杂费)
	
	private Integer orderStatus;	//订单状态：0-正常；1-已取消
	
	private String demand;			//订单要求
	
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
