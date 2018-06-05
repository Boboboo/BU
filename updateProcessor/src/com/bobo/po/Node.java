package com.bobo.po;



public class Node {
	private Integer id;
	private String name;
    private String type;
    private String taxid;
    private String rank;
    private Integer hit_total;
   
    
	public Node() {
		super();
	}

	public Node(Integer id, String name, String type, String taxid, String rank, Integer hit_total) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.taxid = taxid;
		this.rank = rank;
		this.hit_total = hit_total;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getTaxid() {
		return taxid;
	}
	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public Integer getHit_total() {
		return hit_total;
	}
	public void setHit_total(Integer hit_total) {
		this.hit_total = hit_total;
	}

}
