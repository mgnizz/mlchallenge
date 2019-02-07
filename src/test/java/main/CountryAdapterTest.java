package main;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipinfo.main.Country;
import com.ipinfo.main.CountryAdapter;

public class CountryAdapterTest {
	
	private final String json = "{\"name\":\"Argentina\",\"topLevelDomain\":[\".ar\"],\"alpha2Code\":\"AR\",\"alpha3Code\":\"ARG\",\"callingCodes\":[\"54\"],\"capital\":\"Buenos Aires\",\"altSpellings\":[\"AR\",\"Argentine Republic\",\"República Argentina\"],\"region\":\"Americas\",\"subregion\":\"South America\",\"population\":43590400,\"latlng\":[-34.0,-64.0],\"demonym\":\"Argentinean\",\"area\":2780400.0,\"gini\":44.5,\"timezones\":[\"UTC-03:00\"],\"borders\":[\"BOL\",\"BRA\",\"CHL\",\"PRY\",\"URY\"],\"nativeName\":\"Argentina\",\"numericCode\":\"032\",\"currencies\":[{\"code\":\"ARS\",\"name\":\"Argentine peso\",\"symbol\":\"$\"},{\"code\":\"USD\",\"name\":\"Dolar\",\"symbol\":\"$\"}],\"languages\":[{\"iso639_1\":\"es\",\"iso639_2\":\"spa\",\"name\":\"Spanish\",\"nativeName\":\"Español\"},{\"iso639_1\":\"gn\",\"iso639_2\":\"grn\",\"name\":\"Guaraní\",\"nativeName\":\"Avañe'ẽ\"}],\"translations\":{\"de\":\"Argentinien\",\"es\":\"Argentina\",\"fr\":\"Argentine\",\"ja\":\"アルゼンチン\",\"it\":\"Argentina\",\"br\":\"Argentina\",\"pt\":\"Argentina\",\"nl\":\"Argentinië\",\"hr\":\"Argentina\",\"fa\":\"آرژانتین\"},\"flag\":\"https://restcountries.eu/data/arg.svg\",\"regionalBlocs\":[{\"acronym\":\"USAN\",\"name\":\"Union of South American Nations\",\"otherAcronyms\":[\"UNASUR\",\"UNASUL\",\"UZAN\"],\"otherNames\":[\"Unión de Naciones Suramericanas\",\"União de Nações Sul-Americanas\",\"Unie van Zuid-Amerikaanse Naties\",\"South American Union\"]}],\"cioc\":\"ARG\"}";
			
	
	@Test
	public void cuando_recibo_info_del_pais_se_parsea_bien_a_la_clase_country(){
		JsonParser parser = new JsonParser();
		JsonObject jo = parser.parse(json).getAsJsonObject();
		JsonArray latlng = jo.get("latlng").getAsJsonArray();
		JsonArray currencies = jo.get("currencies").getAsJsonArray();
		
		Country ctr = new CountryAdapter(jo).adapt();
		assertEquals(jo.get("alpha2Code").getAsString(),ctr.getAlpha2Code());

		assertEquals(latlng.get(0).getAsDouble(),ctr.getCooridinates()[0],0);
		assertEquals(latlng.get(1).getAsDouble(),ctr.getCooridinates()[1],0);
		
		assertEquals(currencies.get(0).getAsJsonObject().get("code").getAsString(),ctr.getCurrencies().get(0));
		assertEquals(currencies.get(1).getAsJsonObject().get("code").getAsString(),ctr.getCurrencies().get(1));
	}

}
