package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.ls.LSInput;

public class readDataFromMatrix {
	
	public static void main(String[] args) throws IOException  {		
//		writeWeightFromMatrixToDB();
//		writeNamesToDB();
//		FilterDuplicates();
		writeFinalIntoDB();
	}
	
	
	public static void writeFinalIntoDB() throws IOException{
		  try {
	           Class.forName("org.postgresql.Driver");
	      } catch(ClassNotFoundException e) {
	           System.out.println("Class not found "+ e);
	      }
	      
		  FileInputStream fstream = new FileInputStream("/Users/air/Desktop/d1final.txt");
		  BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		  String strLine;
			      
	      try {
		         Connection con = DriverManager.getConnection(
		            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
		         
				 int id=0;
		         while ((strLine = br.readLine()) != null)   {
						String[] array=strLine.split("\\*");
						
						id++;
						String sql = "INSERT INTO links_c1003 VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			        		PreparedStatement pstmt = con.prepareStatement(sql);

			        		pstmt.setInt(1, id);
			        		pstmt.setInt(2, 0);
			        		pstmt.setString(3, array[0].trim());
			        		pstmt.setInt(4, 0);
			        		pstmt.setString(5, array[1].trim());
			        		pstmt.setString(6, "");
			        		pstmt.setString(7, "");
			        		pstmt.setString(8, "C1003");
			        		pstmt.setString(9, "correlation");
			        		pstmt.setString(10, "Gut, stool sample");
			        		pstmt.setString(11, "Gut, stool sample");
			        		pstmt.setDouble(12, -1000.0);
			        		pstmt.setDouble(13, Double.valueOf(array[2]));
			        		pstmt.setString(14, "");
				        	pstmt.executeUpdate();	
							
				  }
				  br.close(); 
		          System.out.println("done!!!"); 
	      } catch(SQLException e) {
	         System.out.println("SQL exception occured" + e);
	      }      
	}
	
	//filter duplicates and those weight=0.0
	public static void FilterDuplicates()  throws IOException { 
		Double weight;
		String name1;
		String name2;
		Map<String, Double> map=new HashMap<>();
		
		try {
	           Class.forName("org.postgresql.Driver");
	    } catch(ClassNotFoundException e) {
	           System.out.println("Class not found "+ e);
	    }
		
		try {
	         Connection con = DriverManager.getConnection(
	            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
	         Statement statement = con.createStatement();
	         ResultSet res = statement.executeQuery("SELECT * FROM c1003");
	         
	         
	         while (res.next()) {
	        	 	name1=res.getString("name1");
	        	 	name2=res.getString("name2");
	        	 	weight=res.getDouble("weight");
	        	 	
	        	 	if(!map.containsKey(name1+"*"+name2) && !map.containsKey(name2+"*"+name1)) {
	        	 		map.put(name1+"*"+name2,weight);
	        	 	} 
	         }
	         
	         Set<Map.Entry<String,Double>> Entries1=map.entrySet();
		     Iterator<Map.Entry<String,Double>> iterator1=Entries1.iterator();
		     int count=0;
		     while(iterator1.hasNext()){
		     	Map.Entry<String,Double> mEntry=iterator1.next();
		     	String key=mEntry.getKey();
		     	Double value=mEntry.getValue();
		     	
		     	if(!value.equals(0.0)) {          //filter out weight=0.0
		     		writeUsingFileWriter(key+"*"+value+"\r\n");
			     	System.out.println(key+"*"+value);
			     	count++;
		     	}
	
		     }
		     System.out.println(count+"*********");
	         System.out.println("done!!!"); 
	     } catch(SQLException e) {
	        System.out.println("SQL exception occured" + e);
	     }      
	}
	
	public static void writeNamesToDB () throws IOException{
			try {
		           Class.forName("org.postgresql.Driver");
		    } catch(ClassNotFoundException e) {
		           System.out.println("Class not found "+ e);
		    }
			
			ArrayList<String> arrayList=getNamePairs();
			
			try {
		         Connection con = DriverManager.getConnection(
		            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
		         
				 int id=0;
		         for(int i=0;i<arrayList.size();i++) {
		        	 		id++;
					  	String[] namePair=arrayList.get(i).split("\t");
						
						String sql = "INSERT INTO name VALUES (?,?,?)";
						PreparedStatement pstmt = con.prepareStatement(sql);
						pstmt.setInt(1, id);
						pstmt.setString(2, namePair[0]);
						pstmt.setString(3, namePair[1]);
						
						
						pstmt.executeUpdate();	
				 }	
				
		         System.out.println("done!!!"); 
		     } catch(SQLException e) {
		        System.out.println("SQL exception occured" + e);
		     }      
	}
	
	public static ArrayList<String> getNamePairs() throws IOException {
		ArrayList<String> arrayList=getNameList();
		ArrayList<String> newList=new ArrayList<>();
		int count=0;
		
		for(int i=1;i<arrayList.size();i++) {     //i start from to get filter the first one OTUs
			 for(int j=1;j<arrayList.size();j++) { 
				 count++;
				 newList.add(arrayList.get(i)+"\t"+arrayList.get(j));
			 }
		}
		
//		for(int i=0;i<newList.size();i++) {  
//			 System.out.println(newList.get(i));
//		}
//		System.out.println(count);
		return newList;	
	}
	
	public static ArrayList<String> getNameList() throws IOException{
	      FileInputStream fstream = new FileInputStream("/Users/air/Desktop/d1.txt");
		  BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		  String strLine;

		  ArrayList<String> list=new ArrayList<>();
		  while ((strLine = br.readLine()) != null)   {
				String[] array=strLine.split("\t");
				list.add(array[0]);	
		  }
		  
		  br.close();
		  return list;
	}
	
	
	public static void writeWeightFromMatrixToDB() throws IOException{
		  try {
	           Class.forName("org.postgresql.Driver");
	      } catch(ClassNotFoundException e) {
	           System.out.println("Class not found "+ e);
	      }
	      
		  FileInputStream fstream = new FileInputStream("/Users/air/Desktop/d1matrix.txt");
		  BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		  String strLine;
			      
	      try {
		         Connection con = DriverManager.getConnection(
		            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
		         
				 int id=0;
		         while ((strLine = br.readLine()) != null)   {
						String[] array=strLine.split("\t");
						ArrayList<String> list=new ArrayList<>();

						for(int i=0;i<array.length;i++) {
							id++;
							list.add(array[i]);
							String sql = "INSERT INTO l VALUES (?,?)";
				        		PreparedStatement pstmt = con.prepareStatement(sql);
	
				        		pstmt.setDouble(1, Double.valueOf(list.get(i)));
				        		pstmt.setInt(2, id);
					        	pstmt.executeUpdate();	
						}	
				  }
				  br.close(); 
		          System.out.println("done!!!"); 
	      } catch(SQLException e) {
	         System.out.println("SQL exception occured" + e);
	      }      
	}
	
	private static void writeUsingFileWriter(String data) {
        File file = new File("/Users/air/Desktop/d1final.txt");
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
