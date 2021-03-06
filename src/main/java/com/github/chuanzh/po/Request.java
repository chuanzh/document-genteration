package com.github.chuanzh.po;

/**
 * @author zhangchuan
 */
public class Request {

	private String title;
	private String name;
	private String type;
	private int isNotNull;
	private String desc;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getIsNotNull() {
		return isNotNull;
	}
	public void setIsNotNull(int isNotNull) {
		this.isNotNull = isNotNull;
	}
}
