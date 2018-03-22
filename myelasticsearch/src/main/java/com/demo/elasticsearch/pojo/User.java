package com.demo.elasticsearch.pojo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

@Document(indexName="autozi_passcar_b2r_party",replicas=1,shards=2)
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7855702045597219816L;
	@Id
	@Field(index=true)
	Long id;
	@Field(index=true)
	String name;
	@Field(index=true)
	String password;
	@Field(index=true)
	String tel;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
}
