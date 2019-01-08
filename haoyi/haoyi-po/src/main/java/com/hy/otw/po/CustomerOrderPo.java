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
 * 客户账单管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
@Entity
@Table(name="customer_order")
public class CustomerOrderPo extends BasePo{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9192341493835705513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;				//主键
	
	@Column(name = "customer_id")
	private Long customerId;		//客户id
	
	@Column(name = "order_id")
	private Long orderId;			//订单id
	
	@Column(name = "company_name")
	private String companyName;		//客户公司名称
	
	@Column(name = "order_date")
	private Date orderDate;			//订单日期
	
	@Column(name = "order_no")
	private String orderNO;			//订单编号
	
	@Column(name = "cabinet_model")
	private String cabinetModel;	//柜型
	
	@Column(name = "cabinet_number")
	private String cabinetNumber;	//柜号
	
	@Column(name = "seal_number")
	private String sealNumber;		//封号
	
	@Column(name = "address")
	private String address;			//订单地址
	
	@Column(name = "settle_price")
	private BigDecimal settlePrice;	//结算金额
	
	@Column(name = "other_amt")
	private BigDecimal otherAmt;	//其它金额(运费)
	
	@Column(name = "settle_status")
	private Integer settleStatus;	//结算状态：0-未结算；1-已结算
	
	@Column(name = "remarks")
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
