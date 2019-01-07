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
 * 司机结算管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
@Entity
@Table(name="driver_settle")
public class DriverSettlePo extends BasePo{
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
	
	@Column(name = "item_name")
	private String itemName;		//结账类型
	
	@Column(name = "should_price")
	private BigDecimal shouldPrice;		//应结账金额
	
	@Column(name = "actual_price")
	private BigDecimal actualPrice;		//实际结账金额
	
	@Column(name = "settle_date")
	private Date settleDate;			//结算日期
	
	@Column(name = "remarks")
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
