package com.eneiascs.orchard.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Comment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8577332454602198719L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, nullable = false)
	private Long id;
	private String username;
	
	@Column(columnDefinition = "text")
	private String content;
	private Date postedDate;
	
	public Comment() {
		
	}
	public Comment(Long id, String username, String content, Date postedDate) {
		this.id = id;
		this.username = username;
		this.content = content;
		this.postedDate = postedDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}
	
	
}
