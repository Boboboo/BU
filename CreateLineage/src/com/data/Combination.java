package com.data;

public class Combination {
	private String taxidCom;
    private String name;
	private String type;
	 
	public Combination(String taxidCom, String name, String type) {
		super();
		this.taxidCom = taxidCom;
		this.name = name;
		this.type = type;
	}
	
	public String getTaxidCom() {
		return taxidCom;
	}
	public void setTaxidCom(String taxidCom) {
		this.taxidCom = taxidCom;
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

}
