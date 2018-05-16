package com.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class findNewNode {
	   public static void main(String[] args) {
		   List<String> result=getNewNames();
		   for(String i:result) {
			   System.out.println(i);
			   writeUsingFileWriter(i+"\r\n");
			   
		   }
		   
//		   Integer id;
//		   String name;
//		   String type;
//		   Integer taxid;
//		   String rank;
//		   Integer hit_total;
//		   
//		   try {
//		          Class.forName("org.postgresql.Driver");
//		      } catch(ClassNotFoundException e) {
//		          System.out.println("Class not found "+ e);
//		      }
//		      
//		   try {
//		          Connection con = DriverManager.getConnection(
//		             "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
//		          Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//		          
//		          ResultSet rs = stmt.executeQuery("SELECT * FROM mind_nodes");  
//			      rs.last();
//			      int rowCount=rs.getRow();
//			      System.out.println(rowCount);
//			      
//			      while (rs.next()) {
//			    	  		Statement statement = con.createStatement();
//			    	  		ResultSet resultSet = statement.executeQuery("SELECT * FROM original_names where name=");  
//			        	    id=rowCount++;
//			        		name=rs.getString("name"); 
//			        		type=rs.getString("type");     
//			        		taxid=rs.getInt("taxid");   
//			        		rank=rs.getString("rank");     
//			        		hit_total=rs.getInt("hit_total");             
//			       	
//			        		String sql = "INSERT INTO mind_nodes VALUES (?, ?, ?, ?, ?, ?)";
//				        	 
//			        		PreparedStatement pstmt = con.prepareStatement(sql);
//				        	 
//			        		pstmt.setInt(1, id);
//			        		pstmt.setString(2, name);
//			        		pstmt.setString(3, type);
//			        		pstmt.setInt(4, taxid);
//			        		pstmt.setString(5, rank);
//			        		pstmt.setInt(6, hit_total);
//			        	
//				        	pstmt.executeUpdate();
//			      }
//		      } catch(SQLException e) {
//		          System.out.println("SQL exception occured" + e);
//		      }
		     
	   }
		
       
	   public static List<String> getNewNames(){
		  Set<String> nodeCahce=getNodeNameCache();
		  //System.out.println(nodeCahce.get(0).getName()+"**************");
		  String name;
		  List<String> newNames=new ArrayList<>();		
	  
	      try {
	          Class.forName("org.postgresql.Driver");
	      } catch(ClassNotFoundException e) {
	          System.out.println("Class not found "+ e);
	      }
	      
	      try {
	          Connection con = DriverManager.getConnection(
	             "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
	         
	          Statement stmt = con.createStatement();
	          ResultSet rs = stmt.executeQuery("SELECT distinct entity_1_name FROM mind_links"
	          		 + " where contextid='C1800'"
	         		 + " union"
	         		 + " SELECT distinct entity_2_name FROM mind_links"
	         		 + " where contextid='C1800'");
	          
	          
	          while (rs.next()) {
	        	      name=rs.getString("entity_1_name"); 
	        	      
	        	      if(!nodeCahce.contains(name)) {
	        	    	  	 newNames.add(name);
	        	      }  
	          }   
	      } catch(SQLException e) {
	          System.out.println("SQL exception occured" + e);
	      }
	      return newNames;
	  }
	
	  
	  public static Set<String> getNodeNameCache(){
	   		  String name;
	   	      Set<String> nodesSet=new HashSet<>();
	   		
	   	      try {
	   	         Class.forName("org.postgresql.Driver");
	   	      } catch(ClassNotFoundException e) {
	   	         System.out.println("Class not found "+ e);
	   	      }
	   	      
	   	      try {
	   	         Connection con = DriverManager.getConnection(
	   	            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
	   	        
	   	         Statement stmt = con.createStatement();
	   	         ResultSet rs = stmt.executeQuery("SELECT * FROM mind_nodes");
	   	         
	   	         while (rs.next()) {   
	   	        		name=rs.getString("name"); 
	   	        		nodesSet.add(name);        
	   	         }
	   	         System.out.println(nodesSet.size()+"**************set size after filter duplicates");
	   		         
	   	      } catch(SQLException e) {
	   	         System.out.println("SQL exception occured" + e);
	   	      }  
	   	      return nodesSet;
	   	}
	  
	  private static void writeUsingFileWriter(String data) {
	        File file = new File("/Users/air/Desktop/res.txt");
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
