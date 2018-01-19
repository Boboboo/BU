package com.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="links")
public class Link {
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private Double pvalue;
	private String source;
	private String source_cond;
	private String target;
	private String target_cond;
	private Double weight;
	
	public Link(int id, Double pvalue, String source, String source_cond, String target, String target_cond,
			Double weight) {
		super();
		this.id = id;
		this.pvalue = pvalue;
		this.source = source;
		this.source_cond = source_cond;
		this.target = target;
		this.target_cond = target_cond;
		this.weight = weight;
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
	public void setPvalue(Double pvalue) {
		this.pvalue = pvalue;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSource_cond() {
		return source_cond;
	}
	public void setSource_cond(String source_cond) {
		this.source_cond = source_cond;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getTarget_cond() {
		return target_cond;
	}
	public void setTarget_cond(String target_cond) {
		this.target_cond = target_cond;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Link [pvalue=" + pvalue + ", source=" + source + ", source_cond=" + source_cond + ", target=" + target
				+ ", target_cond=" + target_cond + ", weight=" + weight + "]";
	}
	
}
