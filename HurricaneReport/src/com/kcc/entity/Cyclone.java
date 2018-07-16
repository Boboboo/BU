package com.kcc.entity;

public class Cyclone {
	 
	String C_id=null;
	String C_name=null;
	int C_number=0;
	
	public Cyclone(String c_id, String c_name, int c_number) {
		super();
		C_id = c_id;
		C_name = c_name;
		C_number = c_number;
	}

	public String getC_id() {
		return C_id;
	}
	public void setC_id(String c_id) {
		C_id = c_id;
	}
	public String getC_name() {
		return C_name;
	}
	public void setC_name(String c_name) {
		C_name = c_name;
	}
	public int getC_number() {
		return C_number;
	}
	public void setC_number(int c_number) {
		C_number = c_number;
	}
}
