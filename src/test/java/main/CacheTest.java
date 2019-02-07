package main;

import java.util.ArrayList;
import java.util.List;

import org.ehcache.Cache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ipinfo.finders.CountryFinder;
import com.ipinfo.finders.CurrencyFinder;
import com.ipinfo.main.CacheHelper;
import com.ipinfo.main.DataRequester;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataRequester.class, CountryFinder.class, CurrencyFinder.class, Cache.class, CacheHelper.class})
public class CacheTest {
	
	
	private String ip = "128.1.21.1";
	private CountryFinder countryFinder;
	private String code = "AR";
	private CurrencyFinder currencyFinder;
	private CacheHelper cache;
	private String currency1 = "GBP";
	private String currency2 = "CAD";
	
	@Before()
	public void before() throws Exception{
		CacheHelper.initCache();
		JsonObject jo = new JsonObject();
		jo.addProperty("countryCode", code);
		jo.addProperty("name", "Argentina");
		jo.add("currencies", new JsonArray());
		jo.addProperty("success", true);
		JsonObject rates = new JsonObject();
	    rates.addProperty(currency2, 1.560132);   
	    rates.addProperty(currency1, 0.88204);
	    jo.add("rates", rates);
		
		DataRequester dataRequesterMock = Mockito.mock(DataRequester.class);
		Mockito.when(dataRequesterMock.doRequest(Mockito.anyString())).thenReturn(jo);
		PowerMockito.whenNew(DataRequester.class).withNoArguments().thenReturn(dataRequesterMock);
		
		cache = CacheHelper.getInstance();
		countryFinder = PowerMockito.spy(new CountryFinder());
		currencyFinder = PowerMockito.spy(new CurrencyFinder());
		        
	}
	
	@Test
	public void cuando_una_ip_se_consulta_y_esta_en_cache_no_realiza_ninguna_conexion_a_las_apis() throws Exception{
		
		countryFinder.findByIP(ip);

		countryFinder.findByIP(ip);
		
		PowerMockito.verifyPrivate(countryFinder,Mockito.times(1)).invoke("getCountry", ip);
	}
	
	@Test
	public void cuando_una_ip_se_consulta_y_es_de_un_pais_que_el_codigo_esta_en_cache_no_realiza_ninguna_conexion_para_obtener_mas_info() throws Exception{
		
		countryFinder.findByIP(ip);

		countryFinder.findByIP("128.1.21.2");
		
		PowerMockito.verifyPrivate(countryFinder,Mockito.times(1)).invoke("getCountryInfo", code);
	}

	@Test
	public void cuando_unas_monedas_se_consultan_y_ya_estan_en_cache_no_realiza_ninguna_conexion_para_el_valor() throws Exception{
		
		List<String> currencies = new ArrayList<String>();
		currencies.add(currency1);
		currencies.add(currency2);
		cache.getCurrencieCache().put(currency1, 1.01);
		cache.getCurrencieCache().put(currency2, 2.01);
		currencyFinder.findCurrencies(currencies);
		PowerMockito.verifyPrivate(currencyFinder,Mockito.never()).invoke("getCurrencyInfo", String.join(",", currencies), "USD");
	}
	
	@Test
	public void cuando_unas_monedas_se_consultan_y_no_estan_en_cache_invoca_el_metodo_para_obtener_info_una_sola_vez() throws Exception{
		
		List<String> currencies = new ArrayList<String>();
		currencies.add(currency1);
		currencies.add(currency2);
		currencyFinder.findCurrencies(currencies);
		PowerMockito.verifyPrivate(currencyFinder).invoke("getCurrencyInfo", String.join(",", currencies), "USD");
	}
	
}
