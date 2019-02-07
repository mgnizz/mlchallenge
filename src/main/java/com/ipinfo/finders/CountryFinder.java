package com.ipinfo.finders;

import java.net.URISyntaxException;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.ipinfo.exception.GettingInfoException;
import com.ipinfo.exception.InvalidIPException;
import com.ipinfo.main.CacheHelper;
import com.ipinfo.main.Country;
import com.ipinfo.main.CountryAdapter;
import com.ipinfo.main.DataRequester;

public class CountryFinder{
	
	private final static Logger log = Logger.getLogger(CountryFinder.class);	
	public Country findByIP(String ip) throws InvalidIPException, GettingInfoException, URISyntaxException {
		
		if(!isValidIP(ip)){
			throw new InvalidIPException();
		}
		CacheHelper cache = CacheHelper.getInstance();
		if(cache.hasIP(ip)){
			log.debug("ip "+ip+" encontrada en cache");
			return cache.getCountryByIp(ip);
		}
		log.debug("solicitando info de la ip "+ip);
		JsonObject countryJson = this.getCountry(ip);
		String code = countryJson.get("countryCode").getAsString();
		
		if(cache.hasCountryISOCode(code)){
			log.debug("codigo "+code+" encontrado en cache");
			return cache.getCountryByISOCode(code);
		}
		
		log.debug("solicitando info del codigo "+code);
		JsonObject countryInfo = this.getCountryInfo(code);
				
		CountryAdapter adapter = new CountryAdapter(countryInfo);	
		Country country = adapter.adapt();
		cache.putIP(ip, country);
		cache.putCountry(code, country);
		return country;
	}
	

	private JsonObject getCountryInfo(String isoCode) throws GettingInfoException, URISyntaxException {
		DataRequester datareq = new DataRequester();
		return datareq.doRequest("https://restcountries.eu/rest/v2/alpha/"+isoCode.toUpperCase());
	}

	private JsonObject getCountry(String ip) throws GettingInfoException, URISyntaxException {
		DataRequester datareq = new DataRequester();		
		return datareq.doRequest("https://api.ip2country.info/ip?"+ip);
	}
	
	

	private boolean isValidIP(String ip) {
		String[] splitted = ip.split("\\.");
		int length = splitted.length;
		if(length!=4) return false;
		for(int i = 0;i<length;i++){
			try{
				int block = Integer.parseInt(splitted[i]);
				boolean firstIsValidNumber = true;
				if(i==0) firstIsValidNumber = (block > 0 && block < 127) || (block > 127 && block < 191) || (block > 192 && block < 223);
			    
				boolean isLowerThanZero = block < 0;
			    boolean isGreaterThan255 = block > 255;
			    if(!firstIsValidNumber || isLowerThanZero || isGreaterThan255){
			      return false;
			    }
			}catch(NumberFormatException e){
				return false;
			}
			
		}
		return true;
		
	}
	
	

}
