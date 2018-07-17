package com.kcc.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kcc.entity.Cyclone;
import com.kcc.entity.Hurricane;
import com.kcc.entity.OutputReport;

public class ProcessManager {
	
	/**
     * Define the global mLandFallsMap to contain all the cyclone and their related hurricanes
     * Define the global mOutputList to contain all the outputReport
     */
	Map<Cyclone,ArrayList<Hurricane>> mLandFallsMap=new HashMap<>();
	ArrayList<OutputReport> mOutputList=new ArrayList<>();	
	
	
	/**
     * The make report progress for each OutputReport in the mOutputList
     */
	public void progress() throws Exception {
		String filePath=getFilePath();
		
		if(filePath!=null) {
			readAllData(filePath);	
		}
		
		filterAllFloridas();
		
		writeReport();
		
		System.out.println("Output report.txt file has been created successfully.");	
	}
	
	
	/**
     * Get path for the file to be parsed
     * Return the file path string
     */
	private String getFilePath() {
		System.out.println("Please input the path of directory which contains data.txt. Example /Users/air/Desktop");
		
		//get the input data.txt file path in the keyboard 
		Scanner scanner =new Scanner(System.in);      
		String inputPath="";
		String filename="data.txt";
		File file;
		
		//get the data.txt file path
		while(true){
			inputPath=scanner.nextLine();
			if(inputPath.charAt(inputPath.length()-1)!='/') {
				inputPath=inputPath+'/';
			}
			inputPath=inputPath+filename;
			
			file=new File(inputPath);
			
			//check if the file in the input path exists, if not, continue to input
			if(file.exists()) break;
			else {
				System.out.println("Cannot find file data.txt. Please try again.");
			}	
		}
		return file.getPath();	
	}
	
	
	/**
     * Read data set from file and write data in a mLandFallsMap
     * @param path file path
     */
	private void readAllData(String path)  throws Exception {  
		
		 System.out.println("readData path: "+path);
		 
		 //define the arrayList for adding all the  hurricanes related with the cyclone
		 ArrayList<Hurricane> hurricaneList=null;
		 
		 //define the variables for cyclone class
		 int C_number=0;
		 String C_id=null, C_name=null;
		
		 //define the variables for hurricane class
		 int H_max_speed=0;
		 String H_day=null, H_time=null,H_identifier=null,H_latitude=null,H_longitude=null;
			 
		 //obtaining input bytes from the data set file, and buffering for efficient reading 
		 FileInputStream fstream = new FileInputStream(path);
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		 String strLine;
		 
		 //read data line by line, and put the corresponding cyclone and hurricane in the map
		 try {
			 while ((strLine = br.readLine()) != null)   {
				 
				//for each key of mLandFallsMap
				Cyclone cyclone=null;
				
				//check if the line starts with 'AL'
				if(strLine.startsWith("AL")) {
					
					//create a new hurricane arrayList to be added hurricanes related with this cyclone
					hurricaneList=new ArrayList<>();
					
					//split the line use comma delimiter to create the new cyclone
					String[] headerArray=strLine.split(",");
					C_id=headerArray[0].trim();
					C_name=headerArray[1].trim();
					C_number=Integer.valueOf(headerArray[2].trim());
					cyclone=new Cyclone(C_id, C_name, C_number);		
				
				//if the line doesn't start with 'AL', it should be hurricane line
				}else {
					
					//split the line use comma delimiter to create the new hurricane
					Hurricane hurricane=null;
					String[] detailsArray=strLine.split(",");
					H_day=detailsArray[0].trim();
					H_time=detailsArray[1].trim();
					H_identifier=detailsArray[2].trim();
					H_latitude=detailsArray[4].trim();
					H_longitude=detailsArray[5].trim();
					H_max_speed=Integer.valueOf(detailsArray[6].trim());	
					
					//just put all those lines has the identifier 'L' into the hurricane List
					if(H_identifier!=null && H_identifier.length()!=0 && H_identifier.equals("L")) {
						hurricane=new Hurricane(H_day, H_time, H_identifier,H_latitude, H_longitude, H_max_speed);			
						hurricaneList.add(hurricane);
					}				
				}
				
				//put all the clone and the related identifier 'L' hurricane list in the map
				if(cyclone!=null) {
					mLandFallsMap.put(cyclone,hurricaneList);	
				}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		//close buffered reader
	    br.close(); 
	}
	
	
	 /**
     * Filter all Florida from all identifier 'L' for each hurricane list in the map
     */
	private void filterAllFloridas () {
		
		int progress=0;
		System.out.print("Processing--------------------- ");
		
		//traverse each key for the mLandFallsMap
		for(Cyclone cyclone: mLandFallsMap.keySet()) {
			
			//get each hurricane list mapping each cyclone
			ArrayList<Hurricane> eachList=mLandFallsMap.get(cyclone);
			
			//calculate current progress, and show the filter progress, EX '56%'
			progress++;
			int readbleProgress= (int) (100 * (progress*1.0f/mLandFallsMap.size()));
			processing(readbleProgress);
			
			//check to only deal with the hurricane lists they have hurricanes 
			if(eachList!=null && eachList.size()!=0) {
				
				//traverse hurricanes in each hurricane list
				for(Hurricane h:eachList) {
					
					//define the variable outputReport
					OutputReport outputReport;
					
					//get the attributes for each hurricane
					String lat=h.getH_latitude().substring(0, h.getH_latitude().length()-1);
					String log="-"+h.getH_longitude().substring(0, h.getH_longitude().length()-1);
					String day=h.getH_day();
					String time=h.getH_time();
					int max_speed=h.getH_max_speed();
				
					//call google maps API to check if the latitude and longitude in the range of Florida
					boolean isFlorida=stateLookUp(lat,log);
					
					//if the latitude and longitude belongs to Florida, write it in the final report result file
					if(isFlorida) {
					
						//get the name 
						String name=cyclone.getC_name();
						
						//format the date 
						Date date=formatDate(day,time);
						
						//create the new outputReport result
						outputReport=new OutputReport(name, date, max_speed);
						
						//put all the outputReport in the mOutputList
						mOutputList.add(outputReport);
						
					}
				}
			}		
		}
		System.out.println("There are " + mOutputList.size() +" results in total.");
	}
	
	
	 /**
     * Use google maps API for each latitude and longitude pair
     * Return true if the latitude and longitude pair belongs to 'Florida, USA'
     */
	private boolean stateLookUp(String lat,String log) {	
			String outResult="";
			String output;
			String resultString=null;
			
			//Reverse geocoding request and response (state lookup)
			try {
		        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+log+"&result_type=administrative_area_level_1&key=AIzaSyB5-DgA2_xkrmy56id9LnHyOZzxZTJHQqY");
		        
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
		        resultString=parseApiResults(outResult,lat,log);
		   
		        //get all 'Florida, USA' and return true
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
	

	
	 /**
     *  Parse Json formatted results and get all formatted_address equals 'Florida, USA'
     *  Return each formatted_address value
     */
	private String parseApiResults(String outResult,String lat,String log) throws JSONException {
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
     * Format date 
     * Return each formatted date
     */
	private Date formatDate(String day, String time) {
		
		//get the year,month,day,hour and minute from each day+time string
		String year=(day+time).substring(0, 4);
		String month=(day+time).substring(4, 6);
		String theDay=(day+time).substring(6, 8);
		String hour=(day+time).substring(8, 10);
		String minute=(day+time).substring(10, 12);
		
		Date date=null;
		
		try {
			//parse each day+time string
			date = new SimpleDateFormat("yyyy/MM/dd hh:mm").parse(year + "/"+ month +"/"+ theDay +" "+ hour +":" + minute);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	
	 /**
     * Write report result on path 
     */
	private void writeReport() {
		System.out.println("Please input the path of directory in which you would like to save report.txt.");
		
		//get the output report.txt file path in the keyboard 
		Scanner scanner = new Scanner(System.in);
		String filename="report.txt";
		
		while(true) {
			String outputPath = scanner.nextLine();
		
			if(outputPath.charAt(outputPath.length()-1)!='/') {
				outputPath = outputPath + '/';
			}
			outputPath = outputPath+filename;
		
			
			File file = new File(outputPath);
			if(file.exists()) {
				try {
					FileWriter fileWriter = new FileWriter(outputPath);
		    			
		    		    for(OutputReport outputReport:mOutputList) {
			    			String name=outputReport.getName();
			    			Date date=outputReport.getDate();
			    			int max_speed=outputReport.getMax_speed();
			    			String string=name+", "+date+", "+max_speed;
			    		
			    			//write into file 
			    			fileWriter.write(string +"\r\n");
		    		    }
		            fileWriter.close();
				} catch (Exception var12) {
					var12.printStackTrace();
				}
				return;
			}
			System.out.println("Cannot find the file report.txt. Please try again.");
		}
    }
	
	
	/**
	 * Print progress in the terminal
	 * @param i current progress
	 */
	private void processing(int i) {
			if(i < 10){
				System.out.print(i + "%");
				System.out.print("\b\b");
			}else if(i >= 10 && i <= 99){
				System.out.print(i + "%");
				System.out.print("\b\b\b");
			}else{
				System.out.println(i + "%");
				System.out.print("Done!! ");
			}
	}
}
