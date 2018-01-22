package com.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadDataFromDB {
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
	         ResultSet rs = stmt.executeQuery("SELECT * FROM links");
	         System.out.println("read data from links...");
	         
	         while (rs.next()) {
	            int id = rs.getInt("id");
	            String pvalue = rs.getString("pvalue");
	            String target = rs.getString("target");
	            String target_cond=rs.getString("target_cond");
	            String source=rs.getString("source");
	            String source_cond=rs.getString("source_cond");
	            Double weight=rs.getDouble("weight");
	            System.out.println(id+", "+pvalue+", "+target+", "+target_cond+", "+source+", "+source_cond+", "+weight);
	         }
	         
	         stmt = con.createStatement();
	         rs = stmt.executeQuery("SELECT * FROM nodes");
	         while (rs.next()) {
		            int id = rs.getInt("dummyid");
		            String taxid = rs.getString("taxid");
		            String taxlevel = rs.getString("taxlevel");
		            String name=rs.getString("name");
		            String lineage=rs.getString("lineage");
		            String children=rs.getString("children");
		            System.out.println(id+" ,"+taxid+" ,"+taxlevel+" ,"+name+" ,"+lineage+" ,"+children);
		         
	         }
	      } catch(SQLException e) {
	         System.out.println("SQL exception occured" + e);
	      }
	   }
}
