package com.bobo.update;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateWeight {
	public static void main(String[] args) {
	  Integer id;
	  Double weight;
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
         ResultSet rs = stmt.executeQuery("SELECT * FROM weight");
         
         id=0;
         while (rs.next()) {
        	    id++;    
        		weight=rs.getDouble("weight");            
        		
        		String sql = "INSERT INTO ll VALUES (?, ?)";
	        	 
        		PreparedStatement pstmt = con.prepareStatement(sql);
	        	 
        		pstmt.setInt(1, id);
        		pstmt.setDouble(2, weight);
        	
	        	pstmt.executeUpdate();
         }
	     
	      
      } catch(SQLException e) {
         System.out.println("SQL exception occured" + e);
      }
      
   }

}
