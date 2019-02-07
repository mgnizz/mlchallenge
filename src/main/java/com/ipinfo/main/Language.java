package com.ipinfo.main;

import com.google.gson.annotations.SerializedName;

public class Language {

	private String name;
	@SerializedName("iso639_1")
	private String isoName;
	
	public String getIsoName() {
		return isoName;
	}
	public void setIsoName(String isoName) {
		this.isoName = isoName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
