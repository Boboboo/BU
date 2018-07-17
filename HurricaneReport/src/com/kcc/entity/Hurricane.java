package com.kcc.entity;


/**
 * Hurricane class
 */
public class Hurricane {
	
	String H_day;
	String H_time;
	String H_identifier;
	String H_latitude;
	String H_longitude;
	int H_max_speed;
	
	public Hurricane() {
		
	}
	
	public Hurricane(String h_day, String h_time, String h_identifier, String h_latitude,
			String h_longitude, int h_max_speed) {
		
		super();
		H_day = h_day;
		H_time = h_time;
		H_identifier = h_identifier;
		H_latitude = h_latitude;
		H_longitude = h_longitude;
		H_max_speed = h_max_speed;
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
}
