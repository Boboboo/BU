package com.bobo;

public class Link {

	private int id;
	private String entity_1_name;
	private String entity_2_name;
	private Double weight;
	
	public Link(int id, String entity_1_name, String entity_2_name, Double weight) {
		super();
		this.id = id;
		this.entity_1_name = entity_1_name;
		this.entity_2_name = entity_2_name;
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEntity_1_name() {
		return entity_1_name;
	}

	public void setEntity_1_name(String entity_1_name) {
		this.entity_1_name = entity_1_name;
	}

	public String getEntity_2_name() {
		return entity_2_name;
	}

	public void setEntity_2_name(String entity_2_name) {
		this.entity_2_name = entity_2_name;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Link [id=" + id + ", entity_1_name=" + entity_1_name + ", entity_2_name=" + entity_2_name + ", weight="
				+ weight + "]";
	}
}