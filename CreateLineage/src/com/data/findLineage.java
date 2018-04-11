package com.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class FindLineage {
	public static void main(String[] args) {
		  String taxid=null;
		  String parent_taxid=null;
		  String rank;
		 
		  String taxidCom;
		  String name;
		  String type;
		 
	      try {
	         Class.forName("org.postgresql.Driver");
	      } catch(ClassNotFoundException e) {
	         System.out.println("Class not found "+ e);
	      }
	      
	      try {
	         Connection con = DriverManager.getConnection(
	            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
	         
	         
		     //taxid -- parent_taxid
		     Statement statement = con.createStatement();
	         ResultSet res = statement.executeQuery("SELECT * FROM original_nodes");
	         
	         Map<String,String> mapl= new HashMap<>(); 
	     
	         while (res.next()) {
        	        taxid=res.getString("taxid");
        	        parent_taxid=res.getString("parent_taxid");
        	        rank=res.getString("rank");
        	        
        	        if(!mapl.containsKey(taxid)) {
        	        		mapl.put(taxid, parent_taxid); 
        	        }
	         }
	         
	         Map<String,String> newMap=new HashMap<>();
	         
	         for(String key : mapl.keySet()) {
	        	 	String orig_lineage="";
	        	 	String keyCopy=key;
	        	 	
	        	 	if(mapl.get(key).equals("1")) {
	        	 		newMap.put(keyCopy, "1");
	        	 		continue;
	        	 	}
	        	 		
	        	 	while(!mapl.get(key).equals("1")) {
	        	 		orig_lineage+=mapl.get(key)+"||";
	        	 		key=mapl.get(key);
	        	 	}
	        	 	orig_lineage+="1";
	        	 	newMap.put(keyCopy, orig_lineage);
	         }

	         for(String key : newMap.keySet()) {
		         System.out.println(key+"   "+newMap.get(key));
		        	 String sql = "INSERT INTO mind_lineage_number VALUES (?, ?)";
		        	 PreparedStatement pstmt = con.prepareStatement(sql);
		        	 
		        	 pstmt.setString(1, key);
		        	 pstmt.setString(2, newMap.get(key));
		        	 pstmt.executeUpdate();
	        	 	
	         }

	         System.out.println("done!");   
	        
	      } catch(SQLException e) {
	         System.out.println("SQL exception occured" + e);
	      }
	      
	   }
}
