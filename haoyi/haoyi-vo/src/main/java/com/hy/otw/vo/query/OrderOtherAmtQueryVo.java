package com.hy.otw.vo.query;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 客户订单其它费用管理
 * @author chenyi_info@126.com
 */
@Getter
@Setter
public class OrderOtherAmtQueryVo {
	
	private Integer id;				//主键
	
	private Long orderId;			//订单id
	
	private Date expenditureDateBegin;	//支出日期
	
	private Date expenditureDateEnd;	//支出日期
	
	private String orderNO;			//订单编号
	
	private String itemName;		//杂费项目
	
	private String address;			//订单简址
	
	private String cabinetNumber;	//柜号
	
	private String targetName;		//支付方名称
	
	private Integer propertyType;	//归属类型 1-司机 2-客户 3-自己
	
	private Integer isSettle;		//是否结算：0-已结算,1-未结算
	
	private int page = 1;
	
	private int rows = 10;
	
	private String sort = "expenditureDate";
	
	private String order = "desc";
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
