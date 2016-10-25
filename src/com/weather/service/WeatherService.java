package com.weather.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.XML;

import com.github.dvdme.ForecastIOLib.FIOAlerts;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.FIOMinutely;
import com.github.dvdme.ForecastIOLib.ForecastIO;

public class WeatherService implements WeatherServiceInit {
	
	private final static String USER_AGENT = "Mozilla/5.0";

	@Override
	public String getCurrentWeatherForcastByLocation(String latitude, String longitude) 
	{
		
		ForecastIO fio = Service.getMainService();
		fio.setUnits(ForecastIO.UNITS_US);
		StringBuilder sb = new StringBuilder();
		
		try 
		{
			sb.append("<Weather>");
			String response = sendGetResponse(fio.getUrl(latitude,longitude));
			JSONObject json = new JSONObject(response);
			sb.append(XML.toString(json));
			sb.append("</Weather>");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return sb.toString(); 
		
	}

	@Override
	public String getMinutelyWeatherReportByLocation(String latitude, String longitude)
	{
		
		ForecastIO fio = Service.getMainService();
		fio.setUnits(ForecastIO.UNITS_US);
		fio.getForecast(latitude,longitude);
		fio.setExcludeURL("hourly,daily,flags,alerts");
		FIOMinutely minutely = new FIOMinutely(fio);
		
		StringBuilder sb = new StringBuilder();
		
	    if(minutely.minutes() < 0)
	    {
	    	sb.append("<Weather><minutely>No minutely data</minutely></Weather>");
	    }
	    else
	    {
	    	//sb.append("<Weather>"+getEscapedXml(fio.getMinutely().toString())+"</Weather>");
	    	sb.append("<Weather>");
			String response = "";
			
			try 
			{
				response = sendGetResponse(fio.getUrl(latitude,longitude));
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			JSONObject json = new JSONObject(response);
			sb.append(XML.toString(json));
			sb.append("</Weather>");
	    }
	        
	    return sb.toString();
	    
	}

	@Override
	public String getHourlyWeatherReportByLocation(String latitude, String longitude)
	{
		
		ForecastIO fio = Service.getMainService();
		fio.setUnits(ForecastIO.UNITS_US);
		fio.setExcludeURL("minutely,daily,flags,alerts");
		fio.getForecast(latitude,longitude);
		
		FIOHourly hourly = new FIOHourly(fio);
		StringBuilder sb = new StringBuilder();
		
	    //In case there is no hourly data available
	    if(hourly.hours() < 0)
	    {
	    	sb.append("<Weather><hourly>No hourly data</hourly></Weather>");
	    }
	    else
	    {
    		// sb.append("<Weather>"+getEscapedXml(fio.getHourly().toString())+"</Weather>");
	    	sb.append("<Weather>");
			String response = "";
			
			try 
			{
				response = sendGetResponse(fio.getUrl(latitude,longitude));
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			JSONObject json = new JSONObject(response);
			sb.append(XML.toString(json));
			sb.append("</Weather>");
	    }
		
		return sb.toString();
		
	}
	
	@Override
	public String getDailyWeatherReportByLocation(String latitude, String longitude)
	{
		
		ForecastIO fio = Service.getMainService();
		fio.setUnits(ForecastIO.UNITS_US);
		fio.setExcludeURL("minutely,hourly,flags,alerts");
		fio.getForecast(latitude,longitude);
		
		FIODaily daily = new FIODaily(fio);
		StringBuilder sb = new StringBuilder();
		
	    //In case there is no hourly data available
	    if(daily.days() < 0)
	    {
	    	sb.append("<Weather><daily>No daily data</daily></Weather>");
	    }
	    else
	    {
    		// sb.append("<Weather>"+getEscapedXml(fio.getDaily().toString())+"</Weather>");
	    	sb.append("<Weather>");
	    	String response = "";
			try 
			{
				response = sendGetResponse(fio.getUrl(latitude,longitude));
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			JSONObject json = new JSONObject(response);
			sb.append(XML.toString(json));
			sb.append("</Weather>");
	    }
		
		return sb.toString();
		
	}
	
	@Override
	public String getFlagWeatherReportByLocation(String latitude, String longitude)
	{
		
		ForecastIO fio = Service.getMainService();
		fio.setUnits(ForecastIO.UNITS_US);
		fio.setExcludeURL("minutely,hourly,daily,alerts");
		fio.getForecast(latitude,longitude);
		
		StringBuilder sb = new StringBuilder();
		
		// sb.append("<Weather>"+getEscapedXml(fio.getFlags().toString())+"</Weather>");
		sb.append("<Weather>");
		String response = "";
		try 
		{
			response = sendGetResponse(fio.getUrl(latitude,longitude));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		JSONObject json = new JSONObject(response);
		sb.append(XML.toString(json));
		sb.append("</Weather>");
		
		return sb.toString();
		
	}

	@Override
	public String getAlertWeatherReportByLocation(String latitude, String longitude)
	{
		
		ForecastIO fio = Service.getMainService();
		fio.setUnits(ForecastIO.UNITS_US);
		fio.setExcludeURL("minutely,hourly,daily,flags");
		fio.getForecast(latitude,longitude);
		
		FIOAlerts alerts = new FIOAlerts(fio);
		StringBuilder sb = new StringBuilder();
		
	    if(alerts.NumberOfAlerts() <= 0)
	    {
	    	sb.append("<Weather><Alerts>No alert data</Alerts></Weather>");
	    }
	    else 
	    {
	        // sb.append("<Weather>"+getEscapedXml(fio.getAlerts().toString())+"</Weather>");
	    	sb.append("<Weather>");
			String response = "";
			try 
			{
				response = sendGetResponse(fio.getUrl(latitude,longitude));
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			JSONObject json = new JSONObject(response);
			sb.append(XML.toString(json));
			sb.append("</Weather>");
	        
	    }
	    
	    return sb.toString();
		
	}

	private String sendGetResponse(String url) throws Exception 
	{

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		
		StringBuffer response = new StringBuffer();
		
		if(responseCode == 200)
		{
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) 
			{
				response.append(inputLine);
			}
			
			in.close();
		}

		return response.toString();
		
	}
	/*
	 * Escaping the generated XML
	 * http://stackoverflow.com/questions/35743480/convert-json-which-escaped-quotes-to-xml
	 */
	private static String getEscapedXml(String jsonStr)
	{
		JSONObject o = new JSONObject(
				jsonStr.replaceFirst("result", "\"result\"")
				.replaceAll("\"\\{", "{")
				.replaceAll("\\}\"", "}")
				.replaceAll("\\\"", "\"")
				.replaceAll("\\\\\"", "\"") );
		return XML.toString(o);
	}
	
}
