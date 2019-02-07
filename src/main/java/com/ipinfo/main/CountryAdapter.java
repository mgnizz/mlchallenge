package com.ipinfo.main;

import java.util.ArrayList;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CountryAdapter {
	
	private Gson gson = new Gson();
	private JsonObject countryInfo;

	public CountryAdapter(JsonObject countryInfo) {		
		this.countryInfo = countryInfo;	
	}

	public Country adapt() {
		JsonArray ja = countryInfo.remove("currencies").getAsJsonArray();
		List<String> currencies = new ArrayList<String>();
		ja.forEach(c->currencies.add(c.getAsJsonObject().get("code").getAsString()));
		Country country = gson.fromJson(countryInfo, Country.class);
		country.setCurrencies(currencies);
		
		return country;
	}

}
