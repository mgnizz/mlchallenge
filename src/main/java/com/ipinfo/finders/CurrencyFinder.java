package com.ipinfo.finders;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ipinfo.exception.GettingInfoException;
import com.ipinfo.main.CacheHelper;
import com.ipinfo.main.DataRequester;

public class CurrencyFinder{

	private final static Logger log = Logger.getLogger(CurrencyFinder.class);
	private Map<String,Double> rates = new HashMap<String,Double>();
	
	
	public Map<String,Double> findCurrencies(List<String> currencies) throws GettingInfoException, URISyntaxException{
		List<String> currenciesToFind = new ArrayList<String>();
		CacheHelper cache = CacheHelper.getInstance();
		for(String currencie : currencies){
			if(cache.hasCurrency(currencie)){
				log.debug("currency "+currencie+" encontrada en la cache");
				Double value = cache.getCourrency(currencie);
				rates.put(currencie, value);
			}else{
				currenciesToFind.add(currencie);
			}
		}
		
		if(!currenciesToFind.isEmpty()){
			String searchFor = String.join(",", currenciesToFind);
			log.info("consultando por las monedas "+searchFor);
			this.getCurrencyInfo(searchFor, "USD");
		}
		
		return rates;
	}

	private void getCurrencyInfo(String symbol, String base) throws GettingInfoException, URISyntaxException{
		DataRequester datareq = new DataRequester();
		datareq.addParameter("base", base);
		datareq.addParameter("symbols", symbol);
		JsonObject jo;
		try{
			jo = datareq.doRequest("https://api.exchangeratesapi.io/latest");
		}catch(GettingInfoException e){
			return;
		}
		if(jo.get("error") != null){
			log.warn("error al pedir los rates "+symbol+": "+jo.get("error"));
			return;
		}
		Set<Map.Entry<String, JsonElement>> entries = jo.get("rates").getAsJsonObject().entrySet();
		CacheHelper cache = CacheHelper.getInstance();
		for (Map.Entry<String, JsonElement> entry: entries) {
			String key = entry.getKey();
			Double value = entry.getValue().getAsDouble();
		    cache.putCurrency(key,value);
		    rates.put(key,value);
		}
		
	}
	
}
