package main;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.junit.Test;

import com.ipinfo.main.Country;



public class TimesTest {
	
	@Test
	public void se_obtienen_bien_las_horas_de_acuerdo_a_las_zonas_horarias_del_pais(){
		Country ctr = new Country();
		String[] timezones = new String[2];
		timezones[0] = "UTC-03:00";
		timezones[1] = "UTC-04:00";
		ctr.setTimezones(timezones);
		
		List<Integer> list = ctr.getTimes().stream().map(x->x.getHour()).collect(Collectors.toList());
		int difference = list.get(0) - list.get(1);
		assertEquals(1,difference);
		
	}

	
	@Test
	public void si_el_timezone_es_UTC_sin_offset_devuelve_hora_UTC_correcta(){
		Country ctr = new Country();
		String[] timezones = new String[1];
		timezones[0] = "UTC";
		ctr.setTimezones(timezones);
		List<Integer> list = ctr.getTimes().stream().map(x->x.getHour()).collect(Collectors.toList());

		assertEquals(Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(Calendar.HOUR_OF_DAY),(int)list.get(0));
		
	}

}
