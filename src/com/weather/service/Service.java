package com.weather.service;

import com.github.dvdme.ForecastIOLib.ForecastIO;

public class Service {
	
	public static ForecastIO getMainService(){
		
		// https://darksky.net/dev/
		// add your key here
		ForecastIO fio = new ForecastIO("YOUR_KEY");
		fio.setUnits(ForecastIO.UNITS_US);
		return fio;
		
	}
	
}
