package com.hy.otw.po;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 客户订单其它费用管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
@Entity
@Table(name="order_other_amt")
public class OrderOtherAmtPo extends BasePo{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9192341493835705513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;				//主键
	
	@Column(name = "order_id")
	private Long orderId;			//订单号主键
	
	@Column(name = "property_type")
	private Integer propertyType; 	//归属类型 1-司机 2-客户 3-自己
	
	@Column(name = "target_id")
	private Long targetId;			//归属方Id property_type=1则id为vehicle表id,=2则为customer表id,=3则为user表id
	
	@Column(name = "target_name")
	private String targetName;		//归属方名称 property_type=1则id为vehicle表车牌号,=2则为customer表客户名称,=3则为user表用户名称
	
	@Column(name = "cabinet_model")
	private String cabinetModel;	//柜型
	
	@Column(name = "cabinet_number")
	private String cabinetNumber;	//柜号
	
	@Column(name = "seal_number")
	private String sealNumber;		//封号
	
	@Column(name = "address")
	private String address;			//订单简址
	
	@Column(name = "expenditure_date")
	private Date expenditureDate;	//支出日期
	
	@Column(name = "order_no")
	private String orderNO;			//订单编号
	
	@Column(name = "item_name")
	private String itemName;		//支付项目类型
	
	@Column(name = "price")
	private BigDecimal price;		//支付金额
	
	@Column(name = "is_settle")
	private Integer isSettle;		//是否结算：0-已结算,1-未结算
	
	@Column(name = "remarks")
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
