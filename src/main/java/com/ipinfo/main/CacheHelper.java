package com.ipinfo.main;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class CacheHelper {
   
	private static CacheHelper self;
	private CacheManager cacheManager;
    private Cache<String, Country> countryCache;
    private Cache<String, Country> ipCache;
    private Cache<String, Double> currencieCache;
 
    public CacheHelper() {
        cacheManager = CacheManagerBuilder
          .newCacheManagerBuilder().build();
        cacheManager.init();
        CacheConfigurationBuilder<String,Country> cacheCountryConfig = 
        		CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Country.class, ResourcePoolsBuilder.heap(194)); //194 paises
        
        countryCache = cacheManager.createCache("countryCache", cacheCountryConfig);
        ipCache = cacheManager.createCache("ipCache", cacheCountryConfig);
        
        CacheConfigurationBuilder<String, Double> cacheCurrencyConfig = 
        		CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Double.class, ResourcePoolsBuilder.heap(194)); //considero 1 moneda por pais
        
        currencieCache = cacheManager.createCache("currencieCache", cacheCurrencyConfig);
        
    }
    
    public static CacheHelper getInstance(){
    	return self;
    }
    
    public Cache<String,Country> getCountryCache(){
    	return countryCache;
    }
    public Cache<String,Country> getIPCache(){
    	return ipCache;
    }

	public synchronized Country getCountryByISOCode(String key){
		return countryCache.get(key);
	}
	
	public synchronized void putCountry(String key, Country value){
		countryCache.put(key, value);
	}
	
	public synchronized boolean hasCountryISOCode(String key){
		return countryCache.containsKey(key);
	}

	public synchronized Country getCountryByIp(String key){
		return ipCache.get(key);
	}
	
	public synchronized void putIP(String key, Country value){
		ipCache.put(key, value);
	}
	
	public synchronized boolean hasIP(String key){
		return ipCache.containsKey(key);
	}
	
	public synchronized Double getCourrency(String key){
		return currencieCache.get(key);
	}
	
	public synchronized void putCurrency(String key, Double value){
		currencieCache.put(key, value);
	}
	
	public synchronized boolean hasCurrency(String key){
		return currencieCache.containsKey(key);
	}

	public Cache<String, Double> getCurrencieCache() {
		return currencieCache;
	}

	public static void initCache() {
		CacheHelper.self = new CacheHelper();
	}

	

}
