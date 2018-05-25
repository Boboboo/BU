package com.bobo;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
//http://localhost:8080/FileUploadToDB/Upload.jsp
@WebServlet("/uploadServlet")
public class FileUploadController extends HttpServlet {
		
	    public void doPost(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
	        
	    	    String entity_1_name = null;
	        String entity_2_name =null;
	        String weight=null;
	        int id=0;
	        List<Link> listInfo=new ArrayList<>();
	        
	        
	        Connection conn = null; 
	        String message = null;  // message will be sent back to client

	        try {
	            File f = new File("/Users/air/Desktop/c1800.txt");
	            BufferedReader b = new BufferedReader(new FileReader(f));
	            String readLine = "";
	            System.out.println("Reading Start...");

	            try {
	            		Class.forName("org.postgresql.Driver");
	            } catch (ClassNotFoundException e) {
	            		try {
							throw new SQLException(e);
					} catch (SQLException e1) {
							e1.printStackTrace();
					}
	            }
	            
	            int row=0;
			    try {
	  		         Connection con = DriverManager.getConnection(
	  		            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
	  		    
	  		         while ((readLine = b.readLine()) != null) {
		  	                String[] array=readLine.split("\t");
				   	      	      
		  	                String sql="INSERT INTO temp (id,entity_1_name,entity_2_name,weight) VALUES (?,?,?,?)";
			   	      	    PreparedStatement pstmt = con.prepareStatement(sql);
			   	      	    
			   	      	    id++; 
			   	      	    entity_1_name=array[0].trim();
			   	      	    	entity_2_name=array[1].trim();
			   	      	    	weight=array[2];
			   	      	    	
				        		pstmt.setInt(1, id);
				        		pstmt.setString(2, entity_1_name);
				        		pstmt.setString(3, entity_2_name);
				        		pstmt.setDouble(4, Double.valueOf(weight));
				
					        	row = pstmt.executeUpdate();
//					        	Link link=new Link(id, array[0], array[1], Double.valueOf(array[2]));     
//					        	listInfo.add(link);  
	  	             }
	  		         System.out.println("done!");
	  		        
		             if (row > 0) {
		                 message = "File uploaded and saved into database";
		             }  
			    } catch(SQLException ex) {
		  	    	 	message = "ERROR: " + ex.getMessage();
			        ex.printStackTrace();
		  	    }  finally {
		            if (conn != null) {
		                try {
		                    conn.close();
		                } catch (SQLException ex) {
		                    ex.printStackTrace();
		                }
		            }
	
		            request.setAttribute("Message", message);
		            getServletContext().getRequestDispatcher("/Message.jsp").forward(request, response);
		        }
	        }catch (IOException e) {
	            e.printStackTrace();
	        }
	    } 
}
