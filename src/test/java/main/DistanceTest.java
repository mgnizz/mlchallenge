package main;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ipinfo.main.Country;



public class DistanceTest {

	@Test
	public void un_pais_calcula_bien_su_distancia_con_buenos_aires(){
		Country ctr = new Country();
		Double[] point = new Double[2];
		point[0] = 40.0;
		point[1] = -4.0;
		
		ctr.setCooridinates(point);
		//info tomada de https://gps-coordinates.org/distance-between-coordinates.php
		assertEquals(10275,ctr.getDistanceFromBA(),0);
	}
	
	@Test
	public void si_el_pais_tiene_las_mismas_coordinadas_que_buenos_aires_la_disancia_es_cero(){
		Country ctr = new Country();
		Double[] point = new Double[2];
		point[0] = -34.0;
		point[1] = -64.0;
		ctr.setCooridinates(point);
		
		assertEquals(0,ctr.getDistanceFromBA(),0);
	}
	
}
