package com.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

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
	         
	         Gson gson=new Gson();
	         
//	         ResultSet rs = stmt.executeQuery("SELECT * FROM temp_links");
	         
///	         List<Link> resultList=new ArrayList<>();
	         
//	         while (rs.next()) {
//	            int id = rs.getInt("id");
//	            Double pvalue = rs.getDouble("pvalue");
//	            String target = rs.getString("target");
//	            String target_cond=rs.getString("target_cond");
//	            String source=rs.getString("source");
//	            String source_cond=rs.getString("source_cond");
//	            Double weight=rs.getDouble("weight");
//	            Link theLink=new Link(pvalue, source, source_cond, target, target_cond, weight);
//	            resultList.add(theLink);
//	            //System.out.println(id+", "+pvalue+", "+target+", "+target_cond+", "+source+", "+source_cond+", "+weight);
//	         }
//	         String resultJson=gson.toJson(resultList);
//	         System.out.println(resultJson);
	         
	         stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery("SELECT * FROM nodes1");
	         while (rs.next()) {
		            String taxid = rs.getString("taxid");
		            String rank = rs.getString("rank");
		            String name=rs.getString("name");
		            String lineage=rs.getString("lineage");
		            String id=rs.getString("id");
		            Node theNode=new Node(taxid,lineage, name, rank,id);
		            String jsonObject=gson.toJson(theNode);
		            System.out.println(jsonObject);
	         }         
	           
	      } catch(SQLException e) {
	         System.out.println("SQL exception occured" + e);
	      }
	   }
}
