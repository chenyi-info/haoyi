package com.hy.otw.vo.query;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 客户账单管理
 * @author chenyi_info@126.com
 */
@Getter
@Setter
public class CustomerOrderQueryVo {
	
	private String companyName;		//客户公司姓名
	
	private String orderNO;			//订单编号
	
	private String address;			//订单简址
	
	private String cabinetNumber;	//柜号
	
	private Integer settleStatus;	//结算状态：0-未结算；1-已结算
	
	private Date orderDateBegin;	//订单日期
	
	private Date orderDateEnd;		//订单日期
	
	private String plateNumber;		//车牌号
	
	private String ownerName;		//司机姓名
	
	private String contactNumber;	//司机联系电话
	
	private int page = 1;
	
	private int rows = 10;
	
	private String sort = "orderDate";
	
	private String order = "desc";
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
