package com.bobo.nodeProcessor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.bobo.DB.DBConnection;



public class findNewNode {
	   public static void main(String[] args) {
		   writeNewNodesToDB();
//		   Set<String> result=getNewNames();
//		   for(String i:result) {
//			   System.out.println(i);
//			   //writeUsingFileWriter(i+"\r\n");			   
//		   }
//		   System.out.println(result.size());
		   System.out.println("done!");
     
	   }
	   
	   
	   public static void writeNewNodesToDB() {
		  Set<String> result=getNewNames();
		  String name=null,rank=null,type=null,sql="";   
   		  int taxid=0;
   		  Statement stmt=null;
   		  
   	      try {
   	    	  	 Connection con=DBConnection.getDBConnection();
   	    	  	 
   	    	     stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	         ResultSet rs = stmt.executeQuery("SELECT * FROM mind_nodes");  
		     rs.last();
		     int rowCount=rs.getRow();
		     //System.out.println(rowCount);
		     
             stmt = con.createStatement();   
   	         for(String i:result) {
   	        	     rowCount++;
   	        	 	 String[] array=i.split("\\*");
   	        	 	 sql="insert into mind_nodes (id,name,type,taxid,rank,hit_total) values "
   	        	 	 		+ "('"+rowCount+"','"+array[0]+"','"+array[2]+"','"+array[1]+"','"+array[3]+"','0')";
   	   	         stmt.executeUpdate(sql);
   	   	         System.out.println(array[0]);
   	         }    
   	      } catch(SQLException e) {
   	         System.out.println("SQL exception occured" + e);
   	      }  		   
	   }
	
	   public static Set<String> getNewNames(){
		  Set<String> nodeCahce=getNodeNameCache();
		  Set<String> newNames=new HashSet<>();	
		  String name=null,rank=null,type=null;
   		  int taxid=0;
	      
	      try {
	    	      Connection con=DBConnection.getDBConnection();
	    	      Statement statement=null,state=null;
	    	      ResultSet rs=null,resultSet=null;
	  		  String sql="";
	    	    
	          statement = con.createStatement();
	          sql="select entity_1_name,entity_1_type,bio_entity_1 FROM mind_links"
	          		 + " where contextid='C0306'"
	         		 + " union"
	         		 + " select entity_2_name,entity_2_type,bio_entity_2 FROM mind_links"
	         		 + " where contextid='C0306'";
	          rs = statement.executeQuery(sql);
	          
	          while (rs.next()) {
	        	      name=rs.getString("entity_1_name"); 
	        	      rank=rs.getString("entity_1_type");
	        	      taxid=rs.getInt("bio_entity_1"); 	        	     
	        	      
	        	      state = con.createStatement();
	       	      sql="select type from original_names where name='"+name+"';";
	       	      resultSet = state.executeQuery(sql);	       	      
	   	    	      while (resultSet.next()){
	   	    	    	  		type=resultSet.getString("type");
		   	    	    	  	if(!nodeCahce.contains(name+"*"+taxid+"*"+type+"*"+rank)) {
		   		        	    	 newNames.add(name+"*"+taxid+"*"+type+"*"+rank);
		   	    	    	  	}		   	    	    	  			
	   	        	  }
	          }  
	      } catch(SQLException e) {
	          System.out.println("SQL exception occured" + e);
	      }
	      return newNames;
	  }
	
	  
	  public static Set<String> getNodeNameCache(){
		      Set<String> nodesSet=new HashSet<>();
		      String name=null,rank=null,type=null;
	   		  int taxid=0;
	   	      
	   	      try {
	   	    	  	 Connection con=DBConnection.getDBConnection();
	   	         Statement stmt = con.createStatement();
	   	         ResultSet rs = stmt.executeQuery("SELECT * FROM mind_nodes");
	   	         
	   	         while (rs.next()) {   
	   	        		name=rs.getString("name");
	   	        		type=rs.getString("type");
	   	        		taxid=rs.getInt("taxid");
	   	        		rank=rs.getString("rank");
	   	        		nodesSet.add(name+"*"+taxid+"*"+type+"*"+rank);        
	   	         }
	   	         System.out.println(nodesSet.size()+"**************");
	   		         
	   	      } catch(SQLException e) {
	   	         System.out.println("SQL exception occured" + e);
	   	      }  
	   	      return nodesSet;
	   }

}
