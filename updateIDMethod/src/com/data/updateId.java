package com.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class updateId {
	public static void main(String[] args) {
		
		Double pvalue;             
		String bio_entity_1;      
		String entity_1_name;     
		String bio_entity_2;    
		String entity_2_name;     
		Double weight;             
		String entity_1_type;      
		String entity_2_type;     
		String contextid;        
		String interaction_type;  
		String habitat_1;       
		String habitat_2;
		
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
	         ResultSet rs = stmt.executeQuery("SELECT * FROM links3");
	         int id=0;
	         while (rs.next()) {
	        	    id++;
	        	 	pvalue=rs.getDouble("pvalue");            
	        		bio_entity_1=rs.getString("bio_entity_1"); 
	        		entity_1_name=rs.getString("entity_1_name");     
	        		bio_entity_2=rs.getString("bio_entity_2");   
	        		entity_2_name=rs.getString("entity_2_name");     
	        		weight=rs.getDouble("weight");             
	        		entity_1_type=rs.getString("entity_1_type");     
	        		entity_2_type=rs.getString("entity_2_type");     
	        		contextid=rs.getString("contextid");        
	        		interaction_type=rs.getString("interaction_type");  
	        		habitat_1=rs.getString("habitat_1");        
	        		habitat_2=rs.getString("habitat_2");
		        	 
	        		String sql = "INSERT INTO links VALUES (?, ?,?, ?,?, ?,?, ?,?, ?,?, ?,?)";
		        	 
	        		PreparedStatement pstmt = con.prepareStatement(sql);
		        	 
	        		pstmt.setInt(1, id);
	        		pstmt.setDouble(2, pvalue);
	        		pstmt.setString(3, bio_entity_1);
	        		pstmt.setString(4, entity_1_name);
	        		pstmt.setString(5, bio_entity_2);
	        		pstmt.setString(6, entity_2_name);
	        		pstmt.setDouble(7, weight);
	        		pstmt.setString(8, entity_1_type);
	        		pstmt.setString(9, entity_2_type);
	        		pstmt.setString(10, contextid);
	        		pstmt.setString(11, interaction_type);
	        		pstmt.setString(12, habitat_1);
	        		pstmt.setString(13, habitat_2);
		        	pstmt.executeUpdate();
	         }
		     
		      
	      } catch(SQLException e) {
	         System.out.println("SQL exception occured" + e);
	      }
	      
	   }
}
