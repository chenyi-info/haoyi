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
 * 订单管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
@Entity
@Table(name="order_info")
public class OrderPo extends BasePo{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9192341493835705513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;				//主键
	
	@Column(name = "plate_number")
	private String plateNumber;		//车牌号
	
	@Column(name = "owner_name")
	private String ownerName;		//司机姓名
	
	@Column(name = "contact_number")
	private String contactNumber;	//联系电话
	
	@Column(name = "order_no")
	private String orderNO;			//订单编号
	
	@Column(name = "order_date")
	private Date orderDate;			//订单日期
	
	@Column(name = "customer_id")
	private Long customerId;		//订单客户公司
	
	@Column(name = "company_name")
	private String companyName;	//订单客户公司名称
	
	@Column(name = "operator_id")
	private Long operatorId;		//操作人Id
	
	@Column(name = "operator_name")
	private String operatorName;	//操作人姓名
	
	@Column(name = "detail_address")
	private String detailAddress;	//订单详址
	
	@Column(name = "address")
	private String address;			//订单简址
	
	@Column(name = "weighed")
	private BigDecimal weighed;		//重量(T)
	
	@Column(name = "cabinet_model")
	private String cabinetModel;	//柜型
	
	@Column(name = "cabinet_number")
	private String cabinetNumber;	//柜号
	
	@Column(name = "seal_number")
	private String sealNumber;		//封号
	
	@Column(name = "customer_price")
	private BigDecimal customerPrice;	//订单金额(客单价)
	
	@Column(name = "driver_price")
	private BigDecimal driverPrice;	//划价(司机结算价)
	
	@Column(name = "other_amt")
	private BigDecimal otherAmt;	//其它金额(杂费)
	
	@Column(name = "order_status")
	private Integer orderStatus;	//订单状态：0-正常；1-已取消
	
	@Column(name = "demand")
	private String demand;			//订单要求
	
	@Column(name = "remarks")
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
