package com.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="nodes")
public class Node {
	
	
	@Id 
	private String taxid;
	
	@Column(length=2500)
	private String children;

	private String lineage;
	
	private String name;
	
	private String taxlevel;
	
	private String id;

	public Node() {
		super();
	}


	public Node(String taxid, String lineage, String name, String taxlevel, String id) {
		super();
		this.taxid = taxid;
		this.lineage = lineage;
		this.name = name;
		this.taxlevel = taxlevel;
		this.id = id;
	}

	public Node(String taxid,  String children, String lineage, String name, String taxlevel, String id) {
		super();
		this.taxid = taxid;
		this.children = children;
		this.lineage = lineage;
		this.name = name;
		this.taxlevel = taxlevel;
		this.id = id;
	}
	
	public String getTaxid() {
		return taxid;
	}
	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}
	public String getChildren() {
		return children;
	}
	public void setChildren(String children) {
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Node [taxid=" + taxid + ", children=" + children + ", lineage=" + lineage + ", name=" + name
				+ ", taxlevel=" + taxlevel + ", id=" + id + "]";
	}
	
}

