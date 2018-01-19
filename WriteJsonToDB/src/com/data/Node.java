package com.data;

import java.util.Arrays;


public class Node {
	
	private String taxid;
	private String[] children;
	private String lineage;
	private String name;
	private String taxlevel;
	
	public Node(String taxid, String[] children, String lineage, String name, String taxlevel) {
		super();
		this.taxid = taxid;
		this.children = children;
		this.lineage = lineage;
		this.name = name;
		this.taxlevel = taxlevel;
	}

	public String getTaxid() {
		return taxid;
	}
	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}
	public String[] getChildren() {
		return children;
	}
	public void setChildren(String[] children) {
		this.children = children;
	}
	public String getLineage() {
		return lineage;
	}
	public void setLineage(String lineage) {
		this.lineage = lineage;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTaxlevel() {
		return taxlevel;
	}
	public void setTaxlevel(String taxlevel) {
		this.taxlevel = taxlevel;
	}


	@Override
	public String toString() {
		return "nodes [taxid=" + taxid + ", children=" + Arrays.toString(children) + ", lineage=" + lineage + ", name="
				+ name + ", taxlevel=" + taxlevel + "]";
	}
	
}

