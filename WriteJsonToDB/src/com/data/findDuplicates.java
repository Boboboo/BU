package com.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class findDuplicates {
	public static void main(String[] args) {
	      try {
	         Class.forName("org.postgresql.Driver");
	      } catch(ClassNotFoundException e) {
	         System.out.println("Class not found "+ e);
	      }
	      
	      try {
	         Connection con = DriverManager.getConnection(
	            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
	         
	         Statement stmt = con.createStatement();
	         
	         Map<String,Integer> map1=new HashMap<>();
	         Map<String,String> map2=new HashMap<>();
	         
	         ResultSet rs = stmt.executeQuery("SELECT * FROM links");
	         
	         int count=0;
	         
	         while(rs.next()) {
	        	 	String bio_entity_1=rs.getString("bio_entity_1");
	        	 	String bio_entity_2=rs.getString("bio_entity_2");
	        	 	Integer v1=Integer.valueOf(bio_entity_1);
	        	 	Integer v2=Integer.valueOf(bio_entity_2);
	        	 	String key=v1<v2?bio_entity_1+" * "+bio_entity_2:bio_entity_2+" * "+bio_entity_1;
	        	 	if(!map1.containsKey(key)) {
	        	 		map1.put(key, 1);
	        	 		map2.put(key, "unique");
	        	 	}else {
	        	 		map1.put(key, map1.get(key)+1);
	        	 		map2.put(key, "dup"+" "+bio_entity_1+" "+bio_entity_2);
	        	 	}
	        	 	count++;
	        	 
	         }
	         System.out.println(count+"*****************************************************");
	         
	         Set<Map.Entry<String, Integer>> Entries1=map1.entrySet();
		     Iterator<Map.Entry<String, Integer>> iterator1=Entries1.iterator();
		     while(iterator1.hasNext()){
		     	Map.Entry<String, Integer> mEntry=iterator1.next();
		     	String key=mEntry.getKey();
		     	Integer value=mEntry.getValue();
		     	System.out.println(key+" ____________ "+value);			    	
		     }  
		     
		     Set<Map.Entry<String, String>> Entries2=map2.entrySet();
		     Iterator<Map.Entry<String, String>> iterator2=Entries2.iterator();
		     while(iterator2.hasNext()){
		     	Map.Entry<String, String> mEntry=iterator2.next();
		     	String key=mEntry.getKey();
		     	String value=mEntry.getValue();
		     	System.out.println(key+" ____________ "+value);			    	
		     }        

	           
	      } catch(SQLException e) {
	         System.out.println("SQL exception occured" + e);
	      }
	   }

}
