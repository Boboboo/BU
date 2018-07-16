package com.kcc.entity;

import java.util.Date;

public class FloridaReport {
	
	String name;
	Date date;
	int max_speed;
	
	public FloridaReport(String name, Date date, int max_speed) {
		super();
		this.name = name;
		this.date = date;
		this.max_speed = max_speed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getMax_speed() {
		return max_speed;
	}

	public void setMax_speed(int max_speed) {
		this.max_speed = max_speed;
	}
	
}
