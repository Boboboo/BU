package com.kcc.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kcc.db.DBConnection;
import com.kcc.entity.Hurricane;


public class InitProcess {
	
	/**
     * Define the global mLandFallsList to contain all the hurricanes with identifier 'L'
     */
	private ArrayList<Hurricane> mLandFallsList=new ArrayList<>();
	
	
	/**
     * init process to update all records with 'L' identifier
     */
	public void init(String filePath) {

		try {
			System.out.println("(1/3)Loading data into DB...");	
			if(filePath!=null) {
				WriteIntoDB(filePath);
			}
			System.out.println("Load DB success.");	
			
			getAllLandFalls();
			System.out.println("(2/3)Updating DB with L identifier...");
			
			updateAllLandFalls();
			System.out.println("(3/3)Done");	
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("init failed");	
		}	
	}
	
	
	/**
	 * Write all data set line from given path into DB hurricane table
	 * @param path read file path
	 * @throws Exception
	 */
	public void WriteIntoDB(String path)  throws Exception {  
		 
		//make connection with DB
		 DBConnection DBconn=new DBConnection();
		 Connection conn=DBconn.getDBConnection();
		 Statement statement=null;
		 
		 //define the variables 
		 int H_id=0,H_max_speed=0;
		 String C_number=null, C_name=null;
		 String H_day=null, H_time=null,H_identifier=null,H_state=null,H_latitude=null,H_longitude=null,H_number=null,H_name;
			 
		 //obtaining input bytes from the data set file, and buffering for efficient reading 
		 FileInputStream fstream = new FileInputStream(path);
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		 String strLine;
		 
		//read data line by line, and insert the corresponding values in the hurricane table
		 try {
			 while ((strLine = br.readLine()) != null)   {
				 
				   //check if the line starts with 'AL', if so, get the value of C_number and C_name
					if(strLine.startsWith("AL")) {
						String[] headerArray=strLine.split(",");
						C_number=headerArray[0].trim();
						C_name=headerArray[1].trim();	
					}else {
						
						//parse the string line the corresponding values for each variable
						String[] detailsArray=strLine.split(",");
						H_id++;
						H_day=detailsArray[0].trim();
						H_time=detailsArray[1].trim();
						H_identifier=detailsArray[2].trim();
						H_latitude=detailsArray[4].trim();
						H_longitude=detailsArray[5].trim();
						H_max_speed=Integer.valueOf(detailsArray[6].trim());
						H_number=C_number;
						H_name=C_name;
						
						//insert one record in hurricane table
						String sql = "INSERT INTO hurricane VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, H_id);
			        	    pstmt.setString(2, H_day);
			        	    pstmt.setString(3, H_time);
			        	    pstmt.setString(4, H_identifier);
			        	    pstmt.setString(5, H_state);
			        	    pstmt.setString(6, H_latitude);
			        	    pstmt.setString(7, H_longitude);
			        	    pstmt.setInt(8, H_max_speed);
			        	    pstmt.setString(9, H_number);
			        	    pstmt.setString(10, H_name);
			          	pstmt.executeUpdate();
					}
	        }
			DBconn.cleanConnection(null,statement,conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		//close buffered readers
	    br.close(); 
	}
	
	
	/**
	 * Retrieve data from DB to get records with h_identifier 'L' in a hurricane list
	 */
	private void  getAllLandFalls(){
		
		//declare a Hurricane object
		Hurricane hurricane=null;

		try {
			//make the DB connection
			DBConnection DBconn=new DBConnection();
			Connection conn=DBconn.getDBConnection();
	  	    Statement statement=null;
	  	    ResultSet resultSet=null;
	  	    
	  	    //retrieve records with h_identifier 'L'
	  		statement = conn.createStatement();
		    String sql="SELECT * FROM hurricane where h_identifier='L'";
			resultSet = statement.executeQuery(sql);
		
			//loop the query resultSet to create hurricane
			while (resultSet.next()) {
					hurricane=new Hurricane();
	    	      		hurricane.setH_day(resultSet.getString("H_day"));
	    	      		hurricane.setH_time(resultSet.getString("H_time"));
	    	      		hurricane.setH_identifier(resultSet.getString("H_identifier"));
	    	      		hurricane.setH_latitude(resultSet.getString("H_latitude"));
	    	      		hurricane.setH_longitude(resultSet.getString("H_longitude"));
	    	      		hurricane.setH_max_speed(resultSet.getInt("H_max_speed"));
	    	      		hurricane.setH_number(resultSet.getString("H_number"));
	    	      		
	    	      		//add each hurricane in a hurricane landfall list
	    	      		mLandFallsList.add(hurricane);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Update the h_state schema in hurricane table after calling google maps API
	 * @throws Exception 
	 */
	private void updateAllLandFalls() throws Exception {	
		
		if(mLandFallsList==null || mLandFallsList.size()==0) {
			return;
		}
		
		//traverse hurricanes in each hurricane landfall list
		for(Hurricane h:mLandFallsList) {
			String outResult="";
			String output;
			
			//get related values for each landfall hurricane
			String day=h.getH_day();
			String time=h.getH_time();
			String lat=h.getH_latitude().substring(0, h.getH_latitude().length()-1);
			
			//'-' to represent west longitude 
			String log="-"+h.getH_longitude().substring(0, h.getH_longitude().length()-1);
			String H_number=h.getH_number();
			int max_speed=h.getH_max_speed();
			String state=null;
			
			//call google maps API to get each state for each the latitude and longitude pair
	        state=stateLookUp(lat,log);
	       
	        updateStateInDB(day,time,lat,log,state);  	
		}
	}

	
	 /**
     * Use google maps API for each latitude and longitude pair
     * Return true if the latitude and longitude pair belongs to 'Florida, USA'
     */
	private String stateLookUp(String lat,String log) {	
			String outResult="";
			String output;
			String state=null;
			
			//Reverse geocoding request and response (state lookup)
			try {
		        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+log+"&result_type=administrative_area_level_1&key=");
		        
		        // making connection
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Accept", "application/json");
		        if (conn.getResponseCode() != 200) {
		            throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());    
		        }
		
		        // Reading data from each geocoding response result
		        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		        while ((output = br.readLine()) != null) {
		        		outResult+=output;
		        }
		       	        
		        // parse the geocoding response results 
		        state=parseStateResults(outResult,lat,log);
		        
		        // disconnecting HttpURLConnection
		        conn.disconnect();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			return state;
	}

	
	 /**
     * Use google maps API for each latitude and longitude pair
     * Return the state according to the latitude and longitude pair 
	 * @throws Exception 
     */
	private String parseStateResults(String outResult,String lat,String log) throws JSONException {
		String resultString=null;	
		JSONObject json;
		JSONArray results;
		String status;
		
        //get results and status from JSON format data
		json = new JSONObject(outResult);
        results=json.getJSONArray("results");
        status=json.getString("status");
        
        //check if the status equals 'ZERO_RESULTS', meaning results:[]
        if(status.equals("ZERO_RESULTS")) {
        		return null;
        }
        	 
        //if the status equals 'OK', meaning there are results 
	    if(status.equals("OK")) {
	 
	    		//get formatted_address string as resultString 
	    		resultString=results.getJSONObject(0).getString("formatted_address");
	    	}
		return resultString;
	}
	
	
	/**
	 * Update h_state value for each row in DB
	 * @param day
	 * @param time
	 * @param lat
	 * @param log
	 * @param state
	 */
	private void updateStateInDB(String day,String time,String lat,String log,String state) {
		//to remove the '_' in the first index in each longitude string
		String actualLog=log.substring(1, log.length());
		try {
			DBConnection DBconn=new DBConnection();
			Connection conn=DBconn.getDBConnection();
			Statement statement=null;
			String sql="";
			statement = conn.createStatement();
		
			sql="UPDATE hurricane SET h_state='"+state+"'"
					+ " WHERE h_day='"+ day +"'"
					+ " AND h_time='" + time +"'"
					+ " AND h_latitude='" + lat +"N'"
					+ " AND h_longitude='" + actualLog +"W'";
			statement.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println(e);
		}		
	}	

}
