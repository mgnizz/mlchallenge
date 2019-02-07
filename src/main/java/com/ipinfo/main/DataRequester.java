package com.ipinfo.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipinfo.exception.GettingInfoException;

public class DataRequester {
	
	
	private JsonParser parser = new JsonParser();
	private List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
	private HttpClient client = HttpClientBuilder.create().build();
	private final static Logger log = Logger.getLogger(DataRequester.class);
	
	public JsonObject doRequest(String url) throws GettingInfoException, URISyntaxException{
		
		try{
			URIBuilder builder = new URIBuilder(url);
			if(urlParameters.size() > 0) builder.setParameters(urlParameters);
			log.debug("realizando consulta a "+url+ " con parametros "+urlParameters.toString());
			HttpGet get = new HttpGet(builder.build());
			
			HttpResponse res = client.execute(get);	
			
			int statusCode = res.getStatusLine().getStatusCode();
			if( statusCode < 200 || statusCode > 299){
				log.warn("no se obtuvo respuesta del sitio "+url+" Status Code: "+statusCode);
				throw new GettingInfoException();
			};
			log.info("se obtuvo exitosamente respuesta del sitio "+url+" Status Code: "+statusCode);
			BufferedReader rd = new BufferedReader(
			    new InputStreamReader(res.getEntity().getContent()));
			
			JsonObject jo = parser.parse(rd).getAsJsonObject();
			log.debug("respuesta recibida del sitio "+url+": "+jo.toString());
			return jo;
			
		}catch(IOException e){
			
			throw new GettingInfoException();
		} 
	}
	
	public void addParameter(String name, String value){
		urlParameters.add(new BasicNameValuePair(name,value));
	}
}
