package com.data;

public class Lineage {
	
	private String taxid;
	private StringBuilder lineage;
	
	public Lineage(String taxid, StringBuilder lineage) {
		super();
		this.taxid = taxid;
		this.lineage = lineage;
	}
	
	public String getTaxid() {
		return taxid;
	}
	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}
	public StringBuilder getLineage() {
		return lineage;
	}
	public void setLineage(StringBuilder lineage) {
		this.lineage = lineage;
	}
	
}
