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
 * 司机接单管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
@Entity
@Table(name="driver_order")
public class DriverOrderPo extends BasePo{
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
	
	@Column(name = "order_id")
	private Long orderId;			//订单id
	
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
	
	@Column(name = "driver_price")
	private BigDecimal driverPrice;	//划价(司机结算价)
	
	@Column(name = "settle_price")
	private BigDecimal settlePrice;	//实际结算金额
	
	@Column(name = "other_amt")
	private BigDecimal otherAmt;	//其它金额(运费)
	
	@Column(name = "settle_status")
	private Integer settleStatus;	//结算状态：0-未结算；1-已结算
	
	@Column(name = "settle_date")
	private Date settleDate;	//结算时间
	
	@Column(name = "remarks")
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
