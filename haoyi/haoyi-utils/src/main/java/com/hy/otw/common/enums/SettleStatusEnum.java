package com.hy.otw.common.enums;

/**
 * 
 * 结算状态：0-未结算；1-已结算
 * @author chenyi_info@126.com
 *
 */
public enum SettleStatusEnum {
	
	/**
	 * 未结算
	 */
	UNDONE(0, "未结算"),
	
	/**
	 * 已结算
	 */
	DONE(1, "已结算");

	// 成员变量
	private int value;
	private String text;

	// 构造方法
	private SettleStatusEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	// 覆盖方法
	@Override
	public String toString() {
		return this.value + "_" + this.text;
	}
	
	public static String getText(int value) {
		for (SettleStatusEnum obj : SettleStatusEnum.values()) {
			if (value == obj.getValue()) {
				return obj.getText();
			}
		}
		return "";
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
