package com.ipinfo.web;

import java.io.InputStream;
import java.util.Properties;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ipinfo.main.CacheHelper;

/**
 * Application Lifecycle Listener implementation class initConfigFiles
 *
 */
@WebListener
public class Initializer implements ServletContextListener {

	Properties nizzperProp = new Properties();
	InputStream nizzperInput = null;
	
	Properties smtpProp = new Properties();
	InputStream smtpInput = null;
	
	Properties langProp = new Properties();
	InputStream langInput = null;
	
    /**
     * Default constructor. 
     */
    public Initializer() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
    	
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	
    	CacheHelper.initCache();
 	
    }
	
}
