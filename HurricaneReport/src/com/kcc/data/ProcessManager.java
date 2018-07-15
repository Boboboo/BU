package com.kcc.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kcc.db.DBConnection;
import com.kcc.entity.Hurricane;

public class ProcessManager {
	
	public void progress() {
		ArrayList<Hurricane> allLandFalls = getAllLandFalls();
		//System.out.println(allLandFalls.size()+"  ---list");
		Map<String, String> floridaMap = getAllFlorida(allLandFalls);
		//System.out.println(floridaMap.size()+"  ---map");
		getReport(floridaMap);
	}
	
	private ArrayList<Hurricane> getAllLandFalls(){
		ArrayList<Hurricane> landFallsList=new ArrayList<>();
		Hurricane hurricane=null;

		try {
			DBConnection DBconn=new DBConnection();
			Connection conn=DBconn.getDBConnection();
	  	    Statement statement=null;
	  	    ResultSet resultSet=null;
	  		statement = conn.createStatement();
		    String sql="SELECT * FROM hurricane where h_identifier='L';";
			resultSet = statement.executeQuery(sql);
			
			while (resultSet.next()) {
					hurricane=new Hurricane();
	    	      		hurricane.setH_day(resultSet.getString("H_day"));
	    	      		hurricane.setH_time(resultSet.getString("H_time"));
	    	      		hurricane.setH_identifier(resultSet.getString("H_identifier"));
	    	      		hurricane.setH_latitude(resultSet.getString("H_latitude"));
	    	      		hurricane.setH_longitude(resultSet.getString("H_longitude"));
	    	      		hurricane.setH_max_speed(resultSet.getInt("H_max_speed"));
	    	      		hurricane.setC_number(resultSet.getString("C_number"));
	    	      		landFallsList.add(hurricane);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return landFallsList;
	}
	
	private Map<String, String> getAllFlorida(ArrayList<Hurricane> landFallsList) {	
		Map<String, String> floridaMap=new HashMap<>();
		String output;
		
		if(landFallsList==null || landFallsList.size()==0) {
			return null;
		}
		
		for(Hurricane h:landFallsList) {
			String outResult="";
			String day=h.getH_day();
			String time=h.getH_time();
			String lat=h.getH_latitude().substring(0, h.getH_latitude().length()-1);
			String log="-"+h.getH_longitude().substring(0, h.getH_longitude().length()-1);
			String C_number=h.getC_number();
			int max_speed=h.getH_max_speed();
			String resultString=null;
			
			// Reverse geocoding request and response (address lookup)
			try {
		        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+log+"&result_type=administrative_area_level_1&key=");
		        
		        // making connection
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Accept", "application/json");
		        if (conn.getResponseCode() != 200) {
		            throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
		            
		        }
		
		        // Reading data from url
		        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		        while ((output = br.readLine()) != null) {
		        		outResult+=output;
		        }
		        
		        System.out.println(outResult);	        
		        // parse the json format results after calling google maps api
		        resultString=parseApiResults(outResult,day,time,lat,log,C_number,max_speed);
		        
		        if(resultString!=null &&  resultString.length()!=0) {
		        		String[] array=resultString.split("\t");
		        		floridaMap.put(array[0],array[1]);  		
		        }
		        
		        // disconnecting HttpURLConnection
		        conn.disconnect();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}

		return floridaMap;
	}
	
	
	// Converting Json formatted string into JSON object 
    // and all results contained 'Florida, USA' in a HashMap 
	private String parseApiResults(String outResult,String day,String time,String lat,String log,String C_number,int max_speed) throws JSONException {
		String resultString=null;
		String formatted_address = null;	
		JSONObject json;
		JSONArray results;
		String status;
		
        json = new JSONObject(outResult);
        results=json.getJSONArray("results");
        status=json.getString("status");
        
        
        if(status.equals("ZERO_RESULTS")) {
        		return null;
        }
        	   
	    if(status.equals("OK")) {
    			formatted_address=results.getJSONObject(0).getString("formatted_address");
    			//TODO: Replace with StringBuilder
    			if(formatted_address!=null && formatted_address.equals("Florida, USA")) {
    				resultString=day+","+time+","+lat+","+log+","+String.valueOf(max_speed)+"\t"+C_number;	
     		}
	    	}
		return resultString;
	}
	
	private void getReport(Map<String, String> floridaMap) {
		String reportRow=null;
		String name=null;
		
		if(floridaMap==null || floridaMap.size()==0) {
			return;
		}
			
		for(String key:floridaMap.keySet()) {
			String[] array=key.split(",");
			String day=array[0];
			String time=array[1];
			int max_speed=Integer.valueOf(array[4]);
			String C_number=floridaMap.get(key);

			//format date 
			Date date=formatDate(day,time);
			
			//query cyclone number from table cycloone according to the cyclone number, ex.'AL031994'
			name=queryName(C_number.toUpperCase()); 
			
			//write all report record into report.txt file
			reportRow=name+","+date+","+max_speed;
			writeUsingFileWriter(reportRow+"\r\n") ;				
		}	
	}
	
	
	private Date formatDate(String day, String time) {
		String y=(day+time).substring(0, 4);
		String mo=(day+time).substring(4, 6);
		String d=(day+time).substring(6, 8);
		String h=(day+time).substring(8, 10);
		String mm=(day+time).substring(10, 11);
		Date date=null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(y + "-"+ mo +"-"+ d +" "+ h +":" + mm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	
	private String queryName(String C_number) {
		 DBConnection DBconn=new DBConnection();
		 Connection conn=DBconn.getDBConnection();
		 Statement statement=null;
		 ResultSet resultSet=null;
		 String sql="";
		 String name=null;
	
		 try {
		        statement = conn.createStatement();
		        sql="SELECT * FROM cyclone WHERE c_number='"+C_number+"';";
		        resultSet = statement.executeQuery(sql);
		        while (resultSet.next()) {
	        	 	   name=resultSet.getString("c_name"); 
		        }     	       	             
		        DBconn.cleanConnection(null,statement,conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;    
	}


	private void writeUsingFileWriter(String data) {
        File file = new File("/Users/air/Desktop/report.txt");
        FileWriter fr = null;
        try{
            fr = new FileWriter(file,true);
            fr.write(data);
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                fr.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
