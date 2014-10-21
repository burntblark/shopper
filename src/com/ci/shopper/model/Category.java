package com.ci.shopper.model;

public class Category
{
	private long id;
	private String name;
	private String description;
	
	public long getId(){
		return id;
	}
	
	public Category setId(long id){
		this.id = id;
		return this;
	}
	
	public String getName(){
		return name;
	}
	
	public Category setName(String name){
		this.name = name;
		return this;
	}

	public String getDescription(){
		return description;
	}

	public Category setDescription(String description){
		this.description = description;
		return this;
	}
}
