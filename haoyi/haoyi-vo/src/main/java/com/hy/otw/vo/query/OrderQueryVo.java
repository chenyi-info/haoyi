package com.hy.otw.vo.query;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 订单管理
 * @author chenyi_info@126.com
 */
@Getter
@Setter
public class OrderQueryVo {
	
	private String plateNumber;		//车牌号
	
	private String ownerName;		//司机姓名
	
	private String contactNumber;	//联系电话
	
	private String orderNO;			//订单编号
	
	private String address;			//订单简址
	
	private String cabinetNumber;	//柜号
	
	private String cabinetRecipientAddr; //提柜地
	
	private String cabinetReturnAddr;	//还柜地
	
	private String operatorName;	//操作人姓名
	
	private Integer orderStatus;	//订单状态：0-正常；1-已取消
	
	private Date orderDateBegin;	//结算日期
	
	private Date orderDateEnd;		//结算日期
	
	private int page = 1;
	
	private int rows = 10;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
