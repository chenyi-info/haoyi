package com.hy.otw.common.enums;

/**
 * 
 * 数据逻辑删除状态（0：正常；1：删除即隐藏）
 * @author chenyi_info@126.com
 *
 */
public enum DelStatusEnum {
	
	/**
	 * 正常
	 */
	NORMAL(0, "正常"),
	
	/**
	 * 删除即隐藏
	 */
	HIDE(1, "删除即隐藏");

	// 成员变量
	private int value;
	private String text;

	// 构造方法
	private DelStatusEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	// 覆盖方法
	@Override
	public String toString() {
		return this.value + "_" + this.text;
	}
	
	public static String getText(int value) {
		for (DelStatusEnum obj : DelStatusEnum.values()) {
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
