package com.kcc.entity;

public class Hurricane {
	int H_id;
	String H_day;
	String H_time;
	String H_identifier;
	String H_status;
	String H_latitude;
	String H_longitude;
	int H_max_speed;
	int H_min_pressure;
	String C_number;
	
	public Hurricane() {
		
	}
	
	public int getH_id() {
		return H_id;
	}

	public void setH_id(int h_id) {
		H_id = h_id;
	}

	public String getH_day() {
		return H_day;
	}

	public void setH_day(String h_day) {
		H_day = h_day;
	}

	public String getH_time() {
		return H_time;
	}

	public void setH_time(String h_time) {
		H_time = h_time;
	}

	public String getH_identifier() {
		return H_identifier;
	}

	public void setH_identifier(String h_identifier) {
		H_identifier = h_identifier;
	}

	public String getH_status() {
		return H_status;
	}

	public void setH_status(String h_status) {
		H_status = h_status;
	}

	public String getH_latitude() {
		return H_latitude;
	}

	public void setH_latitude(String h_latitude) {
		H_latitude = h_latitude;
	}

	public String getH_longitude() {
		return H_longitude;
	}

	public void setH_longitude(String h_longitude) {
		H_longitude = h_longitude;
	}

	public int getH_max_speed() {
		return H_max_speed;
	}

	public void setH_max_speed(int h_max_speed) {
		H_max_speed = h_max_speed;
	}

	public int getH_min_pressure() {
		return H_min_pressure;
	}

	public void setH_min_pressure(int h_min_pressure) {
		H_min_pressure = h_min_pressure;
	}

	public String getC_number() {
		return C_number;
	}

	public void setC_number(String c_number) {
		C_number = c_number;
	}
}
