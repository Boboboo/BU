package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class filterData {

		public static void main(String[] args) throws SQLException {
			try {

	            File f = new File("/Users/air/Desktop/data.txt");

	            BufferedReader b = new BufferedReader(new FileReader(f));

	            String readLine = "";

	            System.out.println("Reading Start...");
	            
	            while ((readLine = b.readLine()) != null) {
	                String [] array=readLine.split("\t");
	                if(array[3].equals(array[4])) {
	                		System.out.println(array[3]+"\t"+array[4]);
	                }else {
	                	    writeUsingFileWriter(array[0]+"\t"+array[1]+"\t"+array[2]+"\t"+array[3]+"\t"+array[4]+"\r\n");
	                }
	                
	            }
			    //System.out.println("Total in map: "+map.size());
			    
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		
		private static void writeUsingFileWriter(String data) {
	        File file = new File("/Users/air/Desktop/result.txt");
	        FileWriter fr = null;
	        try {
	            fr = new FileWriter(file,true);
	            fr.write(data);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally{
	            try {
	                fr.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }	

}
