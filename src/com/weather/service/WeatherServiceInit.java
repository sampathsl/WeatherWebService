package com.weather.service;

public interface WeatherServiceInit {
	
	public String getHourlyWeatherReportByLocation(String latitude, String longitude);
    public String getFlagWeatherReportByLocation(String latitude, String longitude);
    public String getAlertWeatherReportByLocation(String latitude, String longitude);
    public String getMinutelyWeatherReportByLocation(String latitude, String longitude);
    public String getCurrentWeatherForcastByLocation(String latitude, String longitude);
    public String getDailyWeatherReportByLocation(String latitude, String longitude);
    
}
