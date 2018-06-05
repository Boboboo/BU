package com.bobo.update;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class updateNodeId {
	
public static void main(String[] args) {
		
		Integer id;
		String name;
	    String type;
	    Integer taxid;
	    String rank;
	    Integer hit_total;
		
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
	         ResultSet rs = stmt.executeQuery("SELECT * FROM n");
	         
	         id=0;
	         while (rs.next()) {
	        	    id++;    
	        		name=rs.getString("name"); 
	        		type=rs.getString("type");     
	        		taxid=rs.getInt("taxid");   
	        		rank=rs.getString("rank");     
	        		hit_total=rs.getInt("hit_total");             
	        		
	        		String sql = "INSERT INTO mind_nodes VALUES (?, ?, ?, ?, ?, ?)";
		        	 
	        		PreparedStatement pstmt = con.prepareStatement(sql);
		        	 
	        		pstmt.setInt(1, id);
	        		pstmt.setString(2, name);
	        		pstmt.setString(3, type);
	        		pstmt.setInt(4, taxid);
	        		pstmt.setString(5, rank);
	        		pstmt.setInt(6, hit_total);
	        	
		        	pstmt.executeUpdate();
	         }
		     
		      
	      } catch(SQLException e) {
	         System.out.println("SQL exception occured" + e);
	      }
	      
	   }

}
