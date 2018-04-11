package com.data;


//For those node entity in database mind_nodes_all
public class MindNode {
	private String name;
    private String type;
    private int taxid;
    private String rank;
    
	public MindNode(String name, String type, int taxid, String rank) {
		super();
		this.name = name;
		this.type = type;
		this.taxid = taxid;
		this.rank = rank;
	}
	
	public MindNode(String type, int taxid, String rank) {
		super();
		this.type = type;
		this.taxid = taxid;
		this.rank = rank;
	}

	public MindNode(String name, String type, String rank) {
		super();
		this.name = name;
		this.type = type;
		this.rank = rank;
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

	public int getTaxid() {
		return taxid;
	}

	public void setTaxid(int taxid) {
		this.taxid = taxid;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}  
}
