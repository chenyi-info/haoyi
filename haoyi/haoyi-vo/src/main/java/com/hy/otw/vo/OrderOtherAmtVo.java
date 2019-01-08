package com.hy.otw.vo;


import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 订单其它费用管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
public class OrderOtherAmtVo extends BaseVo{

	private Long id;				//主键
	
	private Long orderId;			//订单号主键
	
	private Integer propertyType; 	//归属类型 1-司机 2-客户 3-自己
	
	private Long targetId;			//归属方Id property_type=1则id为vehicle表id,=2则为customer表id,=3则为user表id
	
	private String targetName;		//归属方名称 property_type=1则id为vehicle表车牌号,=2则为customer表客户名称,=3则为user表用户名称
	
	private String cabinetModel;	//柜型
	
	private String cabinetNumber;	//柜号
	
	private String sealNumber;		//封号
	
	private String address;			//订单简址
	
	private Date expenditureDate;	//支出日期
	
	private String orderNO;			//订单编号
	
	private String itemName;		//支付项目类型
	
	private BigDecimal price;		//支付金额
	
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
