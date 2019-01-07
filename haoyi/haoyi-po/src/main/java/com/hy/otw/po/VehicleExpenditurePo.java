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
 * 车辆日常费用管理
 * @author chenyi_info@126.com
 */
@Setter
@Getter
@Entity
@Table(name="vehicle_expenditure")
public class VehicleExpenditurePo extends BasePo{
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
	
	@Column(name = "expenditure_date")
	private Date expenditureDate;	//支出日期
	
	@Column(name = "price")
	private BigDecimal price;		//支出金额
	  
	@Column(name = "item_name")
	private String itemName;		//支出项目'
	
	@Column(name = "transactor_name")
	private String transactorName;	//经手人
	
	@Column(name = "remarks")
	private String remarks;			//备注
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
