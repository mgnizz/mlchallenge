package com.ipinfo.main;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.annotations.SerializedName;

public class Country {

	private String name;
	private String alpha2Code;
	@SerializedName("latlng")
	private Double[] coordinates;
	private String[] timezones;
	private List<Language> languages;
	private List<String> currencies;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAlpha2Code() {
		return alpha2Code;
	}
	
	public void setAlpha2Code(String alpha2Code) {
		this.alpha2Code = alpha2Code;
	}
	
	public Double[] getCooridinates() {
		return coordinates;
	}
	
	public void setCooridinates(Double[] cooridinates) {
		this.coordinates = cooridinates;
	}
	
	public String[] getTimezones() {
		return timezones;
	}
	
	public void setTimezones(String[] timezones) {
		this.timezones = timezones;
	}
	
	public List<String> getCurrencies() {
		return currencies;
	}
	
	public void setCurrencies(List<String> currencies2) {
		this.currencies = currencies2;
	}

	public List<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}
	
	public double getDistanceFromBA(){
		//formula de haversine
		double earthRadius = 6371.0;
		double baLat = -34.0;
		double baLng = -64.0;
		double thisLat = coordinates[0];
		double thisLng = coordinates[1];
		
	    double dLat = Math.toRadians(thisLat-baLat);
	    double dLng = Math.toRadians(thisLng-baLng);
	   	    
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	            Math.cos(Math.toRadians(baLat)) * Math.cos(Math.toRadians(thisLat)) * 
	            Math.sin(dLng/2) * Math.sin(dLng/2); 
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
	    return Math.round(earthRadius * c);   
	
	}
	
	public List<OffsetDateTime> getTimes(){
		List<OffsetDateTime> dates = new ArrayList<OffsetDateTime>();
		for(String timeZone : this.timezones){
			Instant instant = Instant.now();
			String str = timeZone.replace("UTC", "");
			if(str.length() == 0) str = "+00:00";
			ZoneOffset zoneOffset = ZoneOffset.of(str);
			OffsetDateTime odt = OffsetDateTime.ofInstant( instant , zoneOffset );
			
			dates.add(odt);
		}
		return dates;
	}
	
	public String getTimesForPrint(){
		
		Function<OffsetDateTime, String> fromatter =  t ->t.format(DateTimeFormatter.ISO_OFFSET_TIME) ;
		return joinStringList(getTimes()," o ",fromatter);
	}

	public String getCurrenciesRate(Map<String, Double> rates) {
		List<String> values = new ArrayList<String>();
		for(String currencie: currencies){
			if(rates.containsKey(currencie)) {
				values.add(currencie + " (1 "+currencie+" = "+rates.get(currencie)+"USD)");
			}else{
				values.add(currencie);
			}
		}
		return String.join(", ", values);
	}
	
	public String getLanguagesForPrint(){
		Function<Language,String> formatter = l->l.getName() +" ("+l.getIsoName()+")";
		return joinStringList(getLanguages(),", ",formatter);
	}
	
	public <T> String joinStringList(List<T> list, String separator, Function<T,String> formatter){
		List<String> str = list.stream().map(formatter).collect(Collectors.toList());		
		return String.join(separator, str);
	}
}
