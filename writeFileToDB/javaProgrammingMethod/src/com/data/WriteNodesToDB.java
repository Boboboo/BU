package com.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.SQLException;

public class WriteNodesToDB {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		 String taxid=null;;
	     String parent_taxid=null;
	     String rank=null;
	     
	     PreparedStatement ps = null;
	     Connection con = null;
	     
	     String sql = null;

		 try {
	
			BufferedReader br = new BufferedReader(new FileReader("/Users/air/Desktop/testNodes.dmp"));
			String username = "postgres";
	        String pwd = "722722";
	        String connurl = "jdbc:postgresql://localhost:5432/postgres";
	
	
	        con = DriverManager.getConnection(connurl, username, pwd);
	        Class.forName("org.postgresql.Driver");
			String line = null;
			
			while((line=br.readLine()) != null){
				line.replaceAll("|", "");
				line.replaceAll("\t\t", "\t");
		    		String tmp[]=line.split("\t");
				taxid=tmp[0];
				parent_taxid=tmp[2].replaceAll("'", "`");
				rank=tmp[4];
				
				
				System.out.println(tmp[0] + "\t" + tmp[2]  + "\t" +tmp[4] );	

			    sql = "INSERT INTO morenodes (taxid,parent_taxid,rank) values ('" + taxid + "','" + parent_taxid + "','" + rank + "')";
		
			    ps = con.prepareStatement(sql);
		
			    ps.executeUpdate();
			}
	
		    br.close();
		    con.close();
		    ps.close();
	
		    } catch(IOException e){
		        e.printStackTrace();
		    }
	  }
}
