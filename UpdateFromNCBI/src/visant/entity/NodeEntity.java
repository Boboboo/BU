package visant.entity;

public class NodeEntity {
	
	private String name;
	private String type;
	private Integer taxid;
	private String rank;
	private String unique_name;
	
	public NodeEntity() {
		super();
	}
	
	public NodeEntity(int taxid,String name, String type, String rank) {
		super();
		this.taxid = taxid;
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

	public Integer getTaxid() {
		return taxid;
	}

	public void setTaxid(Integer taxid) {
		this.taxid = taxid;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getUnique_name() {
		return unique_name;
	}

	public void setUnique_name(String unique_name) {
		this.unique_name = unique_name;
	}	
}

