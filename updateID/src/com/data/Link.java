package com.data;


public class Link {
	private int id;             
	private String bio_entity_1;      
	private String entity_1_name;     
	private String bio_entity_2;    
	private String entity_2_name;        
	private String entity_1_type;      
	
	private String entity_2_type;    
	private String contextid;       
	private String interaction_type;  
	private String habitat_1;        
	private String habitat_2;
	private double pvalue;
	private double weight; 
	private String annotation;
	
	public Link(int id, String bio_entity_1, String entity_1_name, String bio_entity_2, String entity_2_name,
			String entity_1_type, String entity_2_type, String contextid, String interaction_type, String habitat_1,
			String habitat_2, double pvalue, double weight, String annotation) {
		super();
		this.id = id;
		this.bio_entity_1 = bio_entity_1;
		this.entity_1_name = entity_1_name;
		this.bio_entity_2 = bio_entity_2;
		this.entity_2_name = entity_2_name;
		this.entity_1_type = entity_1_type;
		this.entity_2_type = entity_2_type;
		this.contextid = contextid;
		this.interaction_type = interaction_type;
		this.habitat_1 = habitat_1;
		this.habitat_2 = habitat_2;
		this.pvalue = pvalue;
		this.weight = weight;
		this.annotation = annotation;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPvalue() {
		return pvalue;
	}
	public void setPvalue(double pvalue) {
		this.pvalue = pvalue;
	}
	public String getBio_entity_1() {
		return bio_entity_1;
	}
	public void setBio_entity_1(String bio_entity_1) {
		this.bio_entity_1 = bio_entity_1;
	}
	public String getEntity_1_name() {
		return entity_1_name;
	}
	public void setEntity_1_name(String entity_1_name) {
		this.entity_1_name = entity_1_name;
	}
	public String getBio_entity_2() {
		return bio_entity_2;
	}
	public void setBio_entity_2(String bio_entity_2) {
		this.bio_entity_2 = bio_entity_2;
	}
	public String getEntity_2_name() {
		return entity_2_name;
	}
	public void setEntity_2_name(String entity_2_name) {
		this.entity_2_name = entity_2_name;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getEntity_1_type() {
		return entity_1_type;
	}
	public void setEntity_1_type(String entity_1_type) {
		this.entity_1_type = entity_1_type;
	}
	public String getEntity_2_type() {
		return entity_2_type;
	}
	public void setEntity_2_type(String entity_2_type) {
		this.entity_2_type = entity_2_type;
	}
	public String getContextid() {
		return contextid;
	}
	public void setContextid(String contextid) {
		this.contextid = contextid;
	}
	public String getInteraction_type() {
		return interaction_type;
	}
	public void setInteraction_type(String interaction_type) {
		this.interaction_type = interaction_type;
	}
	public String getHabitat_1() {
		return habitat_1;
	}
	public void setHabitat_1(String habitat_1) {
		this.habitat_1 = habitat_1;
	}
	public String getHabitat_2() {
		return habitat_2;
	}
	public void setHabitat_2(String habitat_2) {
		this.habitat_2 = habitat_2;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
}
