package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//read from Lineage to get the one last and find out if there are something match for the other data about nodes
public class readLineage {
	
	public static void main(String[] args) throws SQLException {
		try {

            File f = new File("/Users/air/Desktop/data.txt");

            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine = "";

            System.out.println("Reading Start...");
            Map<String, String> map=new HashMap<>();
            
            while ((readLine = b.readLine()) != null) {
                String [] array=readLine.split("\t");
                if(!map.containsKey(array[0])) {
            			map.put(array[0], array[array.length-1]);
            			
                }
            }
		    System.out.println("Total in map: "+map.size());
		    
		    
		    String otu1 = null;
	     	try {
		         Class.forName("org.postgresql.Driver");
		    } catch(ClassNotFoundException e) {
		         System.out.println("Class not found "+ e);
		    }
		      
		    try {
		         Connection con = DriverManager.getConnection(
		            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
		         
			     Statement statement = con.createStatement();
		         ResultSet res = statement.executeQuery("SELECT * FROM lf12");
		         
		         int count=0;
		         while (res.next()) {
	        	        otu1=res.getString("otu1");
	        	        
	        		    Set<Map.Entry<String,String >> Entries1=map.entrySet();
	        		    Iterator<Map.Entry<String, String >> iterator1=Entries1.iterator();
	        		     
	        		    while(iterator1.hasNext()){
	        		     	Map.Entry<String, String> mEntry=iterator1.next();
	        		     	String key=mEntry.getKey();
	        		     	String value=mEntry.getValue();
	        		     	if(otu1.equals(key)) {
	        		     		writeUsingFileWriter(otu1+"\t"+key+"\r\n");
	        		     		System.out.println(otu1+"\t"+key);
	        		     	}
	        		     	count++;
	        		    }	
	        		
		         }
		         System.out.println(count);
		        
		    } catch(SQLException e) {
		         System.out.println("SQL exception occured" + e);
		    }
		    System.out.println("done!");   

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
