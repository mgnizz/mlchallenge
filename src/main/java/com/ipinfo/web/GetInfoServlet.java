package com.ipinfo.web;

import java.io.IOException;
import java.net.URISyntaxException;

import java.util.Map;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ipinfo.main.Country;
import com.ipinfo.exception.GettingInfoException;
import com.ipinfo.exception.InvalidIPException;
import com.ipinfo.finders.CountryFinder;
import com.ipinfo.finders.CurrencyFinder;

/**
 * Servlet implementation class IPGetterServlet
 */
@WebServlet("/ipInfo")
public class GetInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger log = Logger.getLogger(GetInfoServlet.class);
    /**
     * Default constructor. 
     */
    public GetInfoServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String ip = request.getParameter("ip").trim();
		log.info("se recibio consulta por la ip "+ip);
		CurrencyFinder currencyFinder = new CurrencyFinder();
		CountryFinder cf = new CountryFinder();
    	    	
    	try{
    		log.debug("buscando pais");
	    	Country ctr = cf.findByIP(ip);
	    	
	    	Map<String,Double> rates;
	    	log.debug("buscando monedas");
	    	rates = currencyFinder.findCurrencies(ctr.getCurrencies());
	    	String currenciesRate = ctr.getCurrenciesRate(rates);
	    	
	    	request.setAttribute("country", ctr);
	    	request.setAttribute("rates", currenciesRate);
	    	request.getRequestDispatcher("info.jsp").forward(request, response);
	    	
    	}catch(InvalidIPException e){
    		log.error(e,e);
    		response.getWriter().write("Formato de IP invalido");
    	} catch (GettingInfoException | URISyntaxException e) {
    		log.error(e,e);
    		response.getWriter().write("Hubo un problema al obtener los datos");
		} 
    	
	    	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
}
