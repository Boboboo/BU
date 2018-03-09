package com.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class updateLinkId {
	public static void main(String[] args) {
		           
		Integer bio_entity_1;      
		String entity_1_name;     
		Integer bio_entity_2;    
		String entity_2_name;            
		String entity_1_type;      
		String entity_2_type;     
		String contextid;        
		String interaction_type;  
		String habitat_1;       
		String habitat_2;
		Double pvalue; 
		Double weight; 
		String annotation;
		
	      try {
	         Class.forName("org.postgresql.Driver");
	      } catch(ClassNotFoundException e) {
	         System.out.println("Class not found "+ e);
	      }
	      
	      try {
	         Connection con = DriverManager.getConnection(
	            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
	         
	         //get all names collection for each taxid
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery("SELECT * FROM l");
	         int id=0;
	         while (rs.next()) {
	        	    id++;          
	        		bio_entity_1=rs.getInt("bio_entity_1"); 
	        		entity_1_name=rs.getString("entity_1_name");     
	        		bio_entity_2=rs.getInt("bio_entity_2");   
	        		entity_2_name=rs.getString("entity_2_name");             
	        		entity_1_type=rs.getString("entity_1_type");     
	        		entity_2_type=rs.getString("entity_2_type");     
	        		contextid=rs.getString("contextid");        
	        		interaction_type=rs.getString("interaction_type");  
	        		habitat_1=rs.getString("habitat_1");        
	        		habitat_2=rs.getString("habitat_2");
	        		pvalue=rs.getDouble("pvalue"); 
	        		weight=rs.getDouble("weight"); 
	        		annotation=rs.getString("annotation");
	        		
	        		String sql = "INSERT INTO mind_links VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		        	 
	        		PreparedStatement pstmt = con.prepareStatement(sql);
		        	 
	        		pstmt.setInt(1, id);
	        		pstmt.setInt(2, bio_entity_1);
	        		pstmt.setString(3, entity_1_name);
	        		pstmt.setInt(4, bio_entity_2);
	        		pstmt.setString(5, entity_2_name);
	        		pstmt.setString(6, entity_1_type);
	        		pstmt.setString(7, entity_2_type);
	        		pstmt.setString(8, contextid);
	        		pstmt.setString(9, interaction_type);
	        		pstmt.setString(10, habitat_1);
	        		pstmt.setString(11, habitat_2);
	        		pstmt.setDouble(12, pvalue);
	        		pstmt.setDouble(13, weight);
	        		pstmt.setString(14, annotation);
		        	pstmt.executeUpdate();
	         }
		     
		      
	      } catch(SQLException e) {
	         System.out.println("SQL exception occured" + e);
	      }
	      
	   }
}
