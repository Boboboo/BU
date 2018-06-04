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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes.Name;


public class Main {

	public static void main(String[] args) throws IOException, SQLException {
//		FilterData();
//		writeName();
//		UpdateDB (); 
		finishLinkTable();
	}
	
	
	public static void FilterData()  throws IOException, SQLException {  
		 Connection conn=DBConnection.getDBConnection();
		 Statement statement=null;
		 
		 Double weight=null;
		 String name1=null;
		 String name2=null;
		
		 FileInputStream fstream = new FileInputStream("/Users/air/Desktop/1.txt");
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		 String strLine;
		 int id=0;
		 try {
			 while ((strLine = br.readLine()) != null)   {
					String[] array=strLine.split("\t");
					if(Double.valueOf(array[2])<=0.7 && Double.valueOf(array[2])>=-0.5) {
						//list.add(array[0]+"*"+array[1]+"*"+array[2]);
						
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
	
	
	public static void writeName () throws IOException {
		 Connection conn=DBConnection.getDBConnection();
		 Statement statement=null;
		 String sql="";
		 
		 String n=null;
		 String entity_name=null;
		 String species=null;
		
		 FileInputStream fstream = new FileInputStream("/Users/air/Desktop/name.txt");
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		 String strLine;
		 
		 try {
			 while ((strLine = br.readLine()) != null)   {
					String[] array=strLine.split("\t");
					
					n=array[0];
					entity_name=array[1];
					species=array[2];
					
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
	
	
	public static void UpdateDB() throws SQLException {
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
	
	public static void finishLinkTable() {
		try {
			Connection conn=DBConnection.getDBConnection();
			Statement statement=null;
			String sql="";
			statement = conn.createStatement();
			sql="DROP TABLE IF EXISTS links_c0306";
			statement.executeUpdate(sql);
			
			sql="CREATE TABLE links_c0306" + 
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
			
			sql="insert into links_c0306 (id,bio_entity_1,entity_1_name,bio_entity_2,entity_2_name,entity_1_type,entity_2_type,weight) "
					+ "select id,bio_entity_1,entity_1_name,bio_entity_2,entity_2_name,entity_1_type,entity_2_type,weight from name";
			statement.executeUpdate(sql);	
			
			sql="update links_c0306 set contextid='C0306',interaction_type='correlation',habitat_1='Gut',habitat_2='Gut'";
			statement.executeUpdate(sql);
		
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("finishAll() done!!!");
	}
	
}
