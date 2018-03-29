package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class C5 {
	
	public static void main(String[] args) throws IOException {
		getMapping();
	}
	
	private static void getMapping() throws IOException {

	      FileInputStream fstream = new FileInputStream("/Users/air/Desktop/data.txt");
		  BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		  
		  Map<String,Integer> map=new HashMap<>();
		  String strLine;
	
		  while ((strLine = br.readLine()) != null)   {
				String[] lineArray=strLine.split("\t");
				if(!lineArray[2].equals("0000")) {
					//find if there is duplicates
					if(!map.containsKey(lineArray[0]+"*"+lineArray[1]+"*"+lineArray[2]) && !map.containsKey(lineArray[1]+"*"+lineArray[0]+"*"+lineArray[2])) {
						map.put(lineArray[0]+"*"+lineArray[1]+"*"+lineArray[2],1);
					}else {
						System.out.println(lineArray[0]+"*"+lineArray[1]+"*"+lineArray[2]+"********************");
					}	
				}
		   }
		  
		  int count=0;
		  for(String s:map.keySet()) {
			  System.out.println(s);
			  count++;
		  }
		  System.out.println(count);
	       
		  PreparedStatement ps = null;
	      String sql = null;
		   
		  try {
		         Class.forName("org.postgresql.Driver");
		  } catch(ClassNotFoundException e) {
		         System.out.println("Class not found "+ e);
		  }
		 
	      try {
		         Connection con = DriverManager.getConnection(
		            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
		         
		         int id=0;
		         for(String s:map.keySet()) {
			    		String[] array=s.split("\\*");
			 
    	        		id++;
			    		sql="INSERT INTO links_c0301 (id,entity_1_name,entity_2_name,weight) values ('"+id+"','"+array[0]+"','"+array[1]+"','"+Double.valueOf(array[2])+ "')";
			    		ps = con.prepareStatement(sql);	
					ps.executeUpdate();
		         }            
	  	   } catch(SQLException e) {
	  	         System.out.println("SQL exception occured" + e);
	  	   }  
	       
	       System.out.println("DONE!");
		 
		   br.close();	
	}
}
