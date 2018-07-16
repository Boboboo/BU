package com.kcc.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kcc.entity.Cyclone;
import com.kcc.entity.Hurricane;
import com.kcc.entity.FloridaReport;

public class ProcessManager {
	
	Map<Cyclone,ArrayList<Hurricane>> mLandFallsMap=new HashMap<>();
	
	public void progress() throws Exception {
		readAllData("/Users/air/Desktop/data.txt");
		getAllFloridas();
		
	}
	
	public void readAllData(String path)  throws Exception {  
		
		 System.out.println("readData path:"+path);
		 ArrayList<Hurricane> hurricaneList=null;
		 int C_number=0;
		 String C_id=null, C_name=null;
		
		 int H_max_speed=0;
		 String H_day=null, H_time=null,H_identifier=null,H_latitude=null,H_longitude=null;
			 
		 FileInputStream fstream = new FileInputStream(path);
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		 String strLine;
		 
		 try {
			 while ((strLine = br.readLine()) != null)   {
				Cyclone cyclone=null;
				
				if(strLine.startsWith("AL")) {
					hurricaneList=new ArrayList<>();
					
					String[] headerArray=strLine.split(",");
					C_id=headerArray[0].trim();
					C_name=headerArray[1].trim();
					C_number=Integer.valueOf(headerArray[2].trim());
					cyclone=new Cyclone(C_id, C_name, C_number);		
				}else {
					Hurricane hurricane=null;
					String[] detailsArray=strLine.split(",");
					H_day=detailsArray[0].trim();
					H_time=detailsArray[1].trim();
					H_identifier=detailsArray[2].trim();
					H_latitude=detailsArray[4].trim();
					H_longitude=detailsArray[5].trim();
					H_max_speed=Integer.valueOf(detailsArray[6].trim());	
					
					if(H_identifier!=null && H_identifier.length()!=0 && H_identifier.equals("L")) {
						hurricane=new Hurricane(H_day, H_time, H_identifier,H_latitude, H_longitude, H_max_speed);			
						hurricaneList.add(hurricane);
					}				
				}
				if(cyclone!=null) {
					mLandFallsMap.put(cyclone,hurricaneList);	
				}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
//		 for(Cyclone c:mLandFallsMap.keySet()) {
//			 System.out.println(mLandFallsMap.get(c).size()    +"\r\n");
//		 }
//		System.out.println(mLandFallsMap.size()+"***");
	    br.close(); 
//	    return totalMap;
	}
	
	
	//get all Florida from all identifier equal 'L'
	private void getAllFloridas () {
		for(Cyclone cyclone: mLandFallsMap.keySet()) {
			ArrayList<Hurricane> eachList=mLandFallsMap.get(cyclone);
			
			if(eachList!=null && eachList.size()!=0) {
				for(Hurricane h:eachList) {
					//FloridaReport floridaReport;
					String lat=h.getH_latitude().substring(0, h.getH_latitude().length()-1);
					String log="-"+h.getH_longitude().substring(0, h.getH_longitude().length()-1);
					String day=h.getH_day();
					String time=h.getH_time();
					int max_speed=h.getH_max_speed();
				
					boolean isFlorida=callAPI(lat,log);
					if(isFlorida) {
						String name=cyclone.getC_name();
						Date date=formatDate(day,time);
						//floridaReport=new FloridaReport(name, date, max_speed);
						writeUsingFileWriter(name+","+date+","+max_speed+"\r\n") ;
					}
				}
			}		
		}		
	}
		
		
	private boolean callAPI(String lat,String log) {	
			String outResult="";
			String output;
			String resultString=null;
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
		        resultString=parseApiResults(outResult,lat,log);
		        
		        if(resultString!=null &&  resultString.equals("Florida, USA")) {
		        		 return true;
		        }
		        
		        // disconnecting HttpURLConnection
		        conn.disconnect();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			return false;
	}
	

	// Converting Json formatted string into JSON object 
    // and all results contained 'Florida, USA' in a HashMap 
	private String parseApiResults(String outResult,String lat,String log) throws JSONException {
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
    			if(formatted_address!=null && formatted_address.equals("Florida, USA")) {
    				resultString=formatted_address;
     		}
	    	}
		return resultString;
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
