package com.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="contexts")
public class Context {
	
	@Id
	private String id;
	
	private String body_site;
	
	private String condition;
	
	private String disease_state;
	
	private String experiment_info;
	
	private String host;
	
	private String interaction_type;
	
	private Integer pubmed_id;
	
	private Double pval_cutoff;
	
	private String pvalue_method;

	public Context(String id, String body_site, String condition, String disease_state, String experiment_info,
			String host, String interaction_type, Integer pubmed_id, Double pval_cutoff, String pvalue_method) {
		super();
		this.id = id;
		this.body_site = body_site;
		this.condition = condition;
		this.disease_state = disease_state;
		this.experiment_info = experiment_info;
		this.host = host;
		this.interaction_type = interaction_type;
		this.pubmed_id = pubmed_id;
		this.pval_cutoff = pval_cutoff;
		this.pvalue_method = pvalue_method;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBody_site() {
		return body_site;
	}

	public void setBody_site(String body_site) {
		this.body_site = body_site;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDisease_state() {
		return disease_state;
	}

	public void setDisease_state(String disease_state) {
		this.disease_state = disease_state;
	}

	public String getExperiment_info() {
		return experiment_info;
	}

	public void setExperiment_info(String experiment_info) {
		this.experiment_info = experiment_info;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getInteraction_type() {
		return interaction_type;
	}

	public void setInteraction_type(String interaction_type) {
		this.interaction_type = interaction_type;
	}

	public Integer getPubmed_id() {
		return pubmed_id;
	}

	public void setPubmed_id(Integer pubmed_id) {
		this.pubmed_id = pubmed_id;
	}

	public Double getPval_cutoff() {
		return pval_cutoff;
	}

	public void setPval_cutoff(Double pval_cutoff) {
		this.pval_cutoff = pval_cutoff;
	}

	public String getPvalue_method() {
		return pvalue_method;
	}

	public void setPvalue_method(String pvalue_method) {
		this.pvalue_method = pvalue_method;
	}

	@Override
	public String toString() {
		return "Context [id=" + id + ", body_site=" + body_site + ", condition=" + condition + ", disease_state="
				+ disease_state + ", experiment_info=" + experiment_info + ", host=" + host + ", interaction_type="
				+ interaction_type + ", pubmed_id=" + pubmed_id + ", pval_cutoff=" + pval_cutoff + ", pvalue_method="
				+ pvalue_method + "]";
	}
	
}
