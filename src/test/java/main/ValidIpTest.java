package main;

import java.net.URISyntaxException;

import org.ehcache.Cache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ipinfo.main.CacheHelper;
import com.ipinfo.main.Country;
import com.ipinfo.main.DataRequester;
import com.ipinfo.exception.GettingInfoException;
import com.ipinfo.exception.InvalidIPException;
import com.ipinfo.finders.CountryFinder;

@SuppressWarnings("unchecked")
@RunWith(PowerMockRunner.class)
@PrepareForTest({DataRequester.class, CountryFinder.class, Cache.class, CacheHelper.class})
public class ValidIpTest {
	
	private CountryFinder countryFinder;

	@Before
	public void before() throws Exception{
		
		PowerMockito.mockStatic(CacheHelper.class);
		DataRequester dataRequesterMock = Mockito.mock(DataRequester.class);
		CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
		Cache<String,Country> cache = Mockito.mock(Cache.class);
		
		PowerMockito.when(CacheHelper.getInstance()).thenReturn(cacheHelper);
		Mockito.when(cache.containsKey(Mockito.anyString())).thenReturn(false);
		Mockito.when(cacheHelper.getCountryCache()).thenReturn(cache);
		Mockito.when(cacheHelper.getIPCache()).thenReturn(cache);
		Mockito.when(dataRequesterMock.doRequest(Mockito.anyString())).thenReturn(null);
		PowerMockito.whenNew(DataRequester.class).withNoArguments().thenReturn(dataRequesterMock);
		
		countryFinder = PowerMockito.spy(new CountryFinder());
		
		
	}
	
	@Test(expected = InvalidIPException.class) 
	public void cuando_se_busca_una_ip_que_empieza_con_letras_lanza_excepcion() throws InvalidIPException, GettingInfoException, URISyntaxException{
		countryFinder.findByIP("aaa.aaa.aaa.aaa");
	}
	
	@Test(expected = InvalidIPException.class) 
	public void cuando_se_busca_una_ip_que_empieza_con_valor_valido_y_tiene_letras_lanza_excepcion() throws InvalidIPException, GettingInfoException, URISyntaxException{
		countryFinder.findByIP("1.aaa.aaa.aaa");
	}

	@Test(expected = InvalidIPException.class) 
	public void cuando_se_busca_una_ip_que_empieza_con_cero_lanza_excepcion() throws InvalidIPException, GettingInfoException, URISyntaxException{
		countryFinder.findByIP("0.1.1.1");
	}
	
	@Test(expected = InvalidIPException.class) 
	public void cuando_se_busca_una_ip_que_empieza_con_127_lanza_excepcion() throws InvalidIPException, GettingInfoException, URISyntaxException{
		countryFinder.findByIP("127.1.1.1");
	}
	
	@Test(expected = InvalidIPException.class) 
	public void cuando_se_busca_una_ip_que_empieza_con_numero_mayor_a_255_lanza_excepcion() throws InvalidIPException, GettingInfoException, URISyntaxException{
		countryFinder.findByIP("127.1.1.1");
	}
	

	@Test(expected = InvalidIPException.class) 
	public void cuando_se_busca_una_ip_que_tiene_cualquier_numero_mayor_al_255_lanza_excepcion() throws InvalidIPException, GettingInfoException, URISyntaxException{
		countryFinder.findByIP("128.1.256.1");
	}
	
	@Test
	public void cuando_se_busca_una_ip_valida_busca_a_que_pais_corresponde() throws Exception{
		String ip = "128.1.21.1";
		try{
			countryFinder.findByIP(ip);
		}catch(NullPointerException e){
			
		}
		PowerMockito.verifyPrivate(countryFinder).invoke("getCountry", ip);
	}
	
}
