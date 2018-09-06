package com.shalou.generator.model;


import com.power.poi.annotation.Excel;

/**
 * excel 读取数据模型
 *
 * Created by yu on 2016/11/10.
 */
public class DataModel {

	/**
	 * 参数名
	 */
	@Excel(name = "参数名", columnNum = 0)
	private String paramName;

	/**
	 * 参数类型
	 */
	@Excel(name = "类型", columnNum = 1,width = 7000)
	private String type;

	/**
	 * 描述
	 */
	@Excel(name = "描述", columnNum = 2,width = 9000)
	private String desc;//数据类型描述

	/**
	 * 是否必须
	 */
	@Excel(name = "是否必须", columnNum = 3)
	private String required;


	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}
}
