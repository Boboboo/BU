package com.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	         
	         Map<String,Double> map=new HashMap<>();
	         ResultSet rs = stmt.executeQuery("SELECT * FROM l");
	         while(rs.next()) {
	        	 	String bio_entity_1=rs.getString("bio_entity_1");
	        	 	String bio_entity_2=rs.getString("bio_entity_2");
	        	 	Double weight=rs.getDouble("weight");
	        	 	
	        	 	String key=bio_entity_1+"\t"+bio_entity_2;
	        	 	String keyOther=bio_entity_2+"\t"+bio_entity_1;
	        	 	if(!map.containsKey(key) && !map.containsKey(keyOther)) {
	        	 		map.put(key, weight);
	        	 	}else {
	        	 		
	        	 	}
	         }
	         
	         Set<Map.Entry<String, Double>> Entries=map.entrySet();
		     Iterator<Map.Entry<String, Double>> iterator=Entries.iterator();
		     while(iterator.hasNext()){
		     	Map.Entry<String, Double> mEntry=iterator.next();
		     	String key=mEntry.getKey();
		     	Double value=mEntry.getValue();
		     	writeUsingFileWriter(key+"\t"+value +"\r\n");
		     	System.out.println(key+"\t"+value);			    	
		     }
		     System.out.println(Entries.size()+"*********************************************************");
		     
		     
//	         Map<String,Integer> map1=new HashMap<>();
//	         Map<String,String> map2=new HashMap<>();
//	         
//	         ResultSet rs = stmt.executeQuery("SELECT * FROM l");
//	         
//	         int count=0;
	         
//	         while(rs.next()) {
//	        	 	String bio_entity_1=rs.getString("bio_entity_1");
//	        	 	String bio_entity_2=rs.getString("bio_entity_2");
//	        	 	Integer v1=Integer.valueOf(bio_entity_1);
//	        	 	Integer v2=Integer.valueOf(bio_entity_2);
//	        	 	String key=v1<v2?bio_entity_1+" * "+bio_entity_2:bio_entity_2+" * "+bio_entity_1;
//	        	 	if(!map1.containsKey(key)) {
//	        	 		map1.put(key, 1);
//	        	 		map2.put(key, "unique");
//	        	 	}else {
//	        	 		map1.put(key, map1.get(key)+1);
//	        	 		map2.put(key, "dup"+" "+bio_entity_1+" "+bio_entity_2);
//	        	 	}
//	        	 	count++;
//	        	 
//	         }
		     
		     
//	         Set<Map.Entry<String, Integer>> Entries1=map1.entrySet();
//		     Iterator<Map.Entry<String, Integer>> iterator1=Entries1.iterator();
//		     while(iterator1.hasNext()){
//		     	Map.Entry<String, Integer> mEntry=iterator1.next();
//		     	String key=mEntry.getKey();
//		     	Integer value=mEntry.getValue();
//		     	System.out.println(key+" ____________ "+value);			    	
//		     }  
//		     
//		     Set<Map.Entry<String, String>> Entries2=map2.entrySet();
//		     Iterator<Map.Entry<String, String>> iterator2=Entries2.iterator();
//		     while(iterator2.hasNext()){
//		     	Map.Entry<String, String> mEntry=iterator2.next();
//		     	String key=mEntry.getKey();
//		     	String value=mEntry.getValue();
//		     	//System.out.println(key+" ____________ "+value);			    	
//		     }  
		     
	           
	      } catch(SQLException e) {
	         System.out.println("SQL exception occured" + e);
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
