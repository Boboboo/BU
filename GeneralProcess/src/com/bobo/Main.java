package com.bobo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.ScatteringByteChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes.Name;

import javax.naming.InitialContext;


public class Main {
	public static void main(String[] args) throws IOException, SQLException {
		FilterData("/Users/air/Desktop/4.txt");
		writeName("/Users/air/Desktop/name.txt");
		finishLinkTable("C0309","correlation","Gut","Gut");
		cleanData("C0309","correlation","Gut","Gut");
		mergeAll("C0309");
	}
	
	
	public static void FilterData(String path)  throws IOException, SQLException {  
		 Connection conn=DBConnection.getDBConnection();
		 Statement statement=null;
		 
		 Double weight=null;
		 String name1=null;
		 String name2=null;
		
		 FileInputStream fstream = new FileInputStream(path);
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		 String strLine;
		 int id=0;
		 try {
			 while ((strLine = br.readLine()) != null)   {
					String[] array=strLine.split("\t");
					if(Double.valueOf(array[2])<=0.7 && Double.valueOf(array[2])>=-0.5) {

						id++;
						name1=array[0];
						name2=array[1];
						weight=Double.valueOf(array[2]);
						
						String sql = "INSERT INTO name VALUES (?, ?, ? ,?)";
						PreparedStatement pstmt = conn.prepareStatement(sql);
			        	 
				        	pstmt.setInt(1, id);
				        	pstmt.setString(2, name1);
				        	pstmt.setString(3, name2);
				        	pstmt.setDouble(4, weight);
				        	pstmt.executeUpdate();	
					}						
	         }
			 DBConnection.cleanConnection(null,statement,conn);
		} catch (Exception e) {
			System.out.println(e);
		}
         br.close();
         System.out.println("FilterData() done!!!");	 
	}
	
	
	public static void writeName (String path) throws IOException, SQLException {
		 //FilterData();
		 Connection conn=DBConnection.getDBConnection();
		 Statement statement=null;
		 String sql="";
		 
//		 statement = conn.createStatement();
//		 sql="DROP TABLE IF EXISTS name";
//		 statement.executeUpdate(sql);
//		
//		 sql="CREATE TABLE name" + 
//				"(" + 
//				"    id integer NOT NULL," + 
//				"    n1 character varying(255)," +
//				"    n2 character varying(255)," +
//				"    weight double precision," +
//				"    entity_1_name character varying(255)," + 
//				"    entity_2_name character varying(255)," + 
//				"    entity_1_type character varying(255)," + 
//				"    entity_2_type character varying(255)," +
//				"    bio_entity_1 integer," + 
//				"    bio_entity_2 integer" + 
//				")";
//		 statement.executeUpdate(sql);
		 
		 String n=null;
		 String entity_name=null;
		
		 FileInputStream fstream = new FileInputStream(path);
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		 String strLine;
		 
		 try {
			 while ((strLine = br.readLine()) != null)   {
					String[] array=strLine.split("\t");
					
					n=array[0];
					entity_name=array[1];
					
					statement = conn.createStatement();
					sql = "update name set entity_1_name='"+entity_name+"' where n1='"+n+"'"; 
					statement.executeUpdate(sql);
					
					sql = "update name set entity_2_name='"+entity_name+"' where n2='"+n+"'"; 
					statement.executeUpdate(sql);	
	         }
			 DBConnection.cleanConnection(null,statement,conn);
		} catch (Exception e) {
			System.out.println(e);
		}
        br.close();
        System.out.println("writeName() done!!!");	 
	}
	
	//has update name all columns
	public static void UpdateDB() throws SQLException, IOException {
		//writeName();
		try {
			Connection conn=DBConnection.getDBConnection();
			Statement statement=null;
			String sql="";
			statement = conn.createStatement();
		
			sql="update name set bio_entity_1=original_names.taxid from original_names where original_names.name=name.entity_1_name";
			statement.executeUpdate(sql);
			
			sql="update name set bio_entity_2=original_names.taxid from original_names where original_names.name=name.entity_2_name";
			statement.executeUpdate(sql);
			
			sql="update name set entity_1_type=original_nodes.rank from original_nodes where original_nodes.taxid=name.bio_entity_1";
			statement.executeUpdate(sql);
			
			sql="update name set entity_2_type=original_nodes.rank from original_nodes where original_nodes.taxid=name.bio_entity_2";
			statement.executeUpdate(sql);
			
			sql="delete from name where entity_1_type is null";
			statement.executeUpdate(sql);
			
			sql="delete from name where entity_2_type is null";
			statement.executeUpdate(sql);
	
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("UpdateDB() done!!!");
	}
	
	
	public static void finishLinkTable(String contextid,String interaction_type,String habitat_1,String habitat_2) throws SQLException, IOException {
		UpdateDB();
		try {
			Connection conn=DBConnection.getDBConnection();
			Statement statement=null;
			String sql="";
			statement = conn.createStatement();
			sql="DROP TABLE IF EXISTS links_"+contextid+"";
			System.out.println(sql);
			statement.executeUpdate(sql);
			
			sql="CREATE TABLE links_"+contextid+"" + 
					"(" + 
					"    id integer NOT NULL," + 
					"    bio_entity_1 integer," + 
					"    entity_1_name character varying(255)," + 
					"    bio_entity_2 integer," + 
					"    entity_2_name character varying(255)," + 
					"    entity_1_type character varying(255)," + 
					"    entity_2_type character varying(255)," + 
					"    contextid character varying(255)," + 
					"    interaction_type character varying(255)," + 
					"    habitat_1 character varying(255)," + 
					"    habitat_2 character varying(255)," + 
					"    pvalue double precision," + 
					"    weight double precision," + 
					"    annotation character varying(255)" + 
					")";
			statement.executeUpdate(sql);	
			
			sql="insert into links_"+contextid+"" 
					+ " (id,bio_entity_1,entity_1_name,bio_entity_2,entity_2_name,entity_1_type,entity_2_type,weight) "
					+ "select id,bio_entity_1,entity_1_name,bio_entity_2,entity_2_name,entity_1_type,entity_2_type,weight from name";
			statement.executeUpdate(sql);	
			
			sql="update links_"+contextid+""
					+ " set contextid='"+contextid+"',"
					+ " interaction_type='"+interaction_type+"',"
					+ " habitat_1='"+habitat_1+"',"
					+ " habitat_2='"+habitat_2+"'";
			statement.executeUpdate(sql);
			
//			sql="delete from name";
//			statement.executeUpdate(sql);
		
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("finishAll() done!!!");
	}
	
	public static boolean cleanData(String contextid,String interaction_type,String habitat_1,String habitat_2) throws SQLException, IOException {
		Set<String> set=new HashSet<>();
		try {
		    Connection con=DBConnection.getDBConnection();
	  	    Statement statement=null,state=null;
	  	    ResultSet resultSet=null,rSet=null;
		    String sql="";
		    String name1=null,name2=null;
		    Double weight=null;
		    int id=0;
		    	  	    
	        statement = con.createStatement();
//	        sql="select * from mind_links"
//	        		 + " where contextid='"+contextid+"'";
	        sql="select * from links_"+contextid+"";
	        resultSet = statement.executeQuery(sql);
	        
	        while (resultSet.next()) {
	        	 	  name1=resultSet.getString("entity_1_name"); 
	      	      name2=resultSet.getString("entity_2_name");
	      	      weight=resultSet.getDouble("weight"); 	        	      
	        		  if(!name1.equals(name2)) {
	        			  if(!set.contains(name1+"*"+name2+"*"+weight)) {
		      	    	  		set.add(name1+"*"+name2+"*"+weight);
		      	      } 
	        		  }	      	       	      
	 	    }
	        
	        statement = con.createStatement();
			sql="delete from name";
			statement.executeUpdate(sql);
	        
	        Iterator<String> iterator=set.iterator();
	        while(iterator.hasNext()) {
	        		String[] array=iterator.next().split("\\*");
	        		id++;
				String sqlInsert = "INSERT INTO name VALUES (?, ?, ? ,?)";
				PreparedStatement pstmt = con.prepareStatement(sqlInsert);
	        	 
		        	pstmt.setInt(1, id);
		        	pstmt.setString(2, array[0]);
		        	pstmt.setString(3, array[1]);
		        	pstmt.setDouble(4, Double.valueOf(array[2]));
		        	pstmt.executeUpdate();	        		
	        }
	       
		} catch (Exception e) {
			System.out.println(e);
		}		
		System.out.println("cleanData() done!!!");
		UpdateDBAgain();
		finishLinkTable(contextid,interaction_type,habitat_1,habitat_2);
		return true;
	}
	
	public static void UpdateDBAgain() throws SQLException, IOException {
		try {
			Connection conn=DBConnection.getDBConnection();
			Statement statement=null;
			String sql="";
			statement = conn.createStatement();
		
			sql="update name set entity_1_name=n1";
			statement.executeUpdate(sql);
			
			sql="update name set entity_2_name=n2";
			statement.executeUpdate(sql);
	
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("UpdateDBAgain() done!!!");	
	}	
	
	public static void mergeAll(String contextid) {
		try {
			Connection conn=DBConnection.getDBConnection();
			Statement statement=null;
			String sql="";
			statement = conn.createStatement();
			sql="DROP TABLE IF EXISTS tempAll";
			statement.executeUpdate(sql);
		
			sql="create table tempAll as (select * from mind_links"
					+ " union all"
					+ " select * from links_"+contextid+")";
			
			statement.executeUpdate(sql);
			
			sql="delete from mind_links";
			statement.executeUpdate(sql);	
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("mergeAll() done!!!");
	}
}
