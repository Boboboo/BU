package com.kcc.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.kcc.db.DBConnection;

public class PreProcess {
	
	public void WriteIntoDB(String path)  throws IOException, SQLException {  
		 DBConnection DBconn=new DBConnection();
		 Connection conn=DBconn.getDBConnection();
		 Statement statement=null;
		 
		 int C_id=0,H_id=0,H_max_speed=0,H_min_pressure=0;
		 String H_number=null, C_name=null;
		 String H_day=null, H_time=null,H_identifier=null,H_status=null,H_latitude=null,H_longitude=null,C_number=null;
			 
		 FileInputStream fstream = new FileInputStream(path);
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		 String strLine;
		 
		 try {
			 while ((strLine = br.readLine()) != null)   {
					if(strLine.startsWith("AL")) {
						String[] headerArray=strLine.split(",");
						C_id++;
						H_number=headerArray[0].trim();
						C_name=headerArray[1].trim();

						String sql = "INSERT INTO cyclone VALUES (?, ?, ?)";
						PreparedStatement pstmt = conn.prepareStatement(sql);
			        	 
				        	pstmt.setInt(1, C_id);
				        	pstmt.setString(2, H_number);
				        	pstmt.setString(3, C_name);
				        	pstmt.executeUpdate();	
					}else {
						String[] detailsArray=strLine.split(",");
						H_id++;
						H_day=detailsArray[0].trim();
						H_time=detailsArray[1].trim();
						H_identifier=detailsArray[2].trim();
						H_status=detailsArray[3].trim();
						H_latitude=detailsArray[4].trim();
						H_longitude=detailsArray[5].trim();
						H_max_speed=Integer.valueOf(detailsArray[6].trim());
						H_min_pressure=Integer.valueOf(detailsArray[7].trim());
						C_number=H_number;
						
						String sql = "INSERT INTO hurricane VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, H_id);
			        	    pstmt.setString(2, H_day);
			        	    pstmt.setString(3, H_time);
			        	    pstmt.setString(4, H_identifier);
			        	    pstmt.setString(5, H_status);
			        	    pstmt.setString(6, H_latitude);
			        	    pstmt.setString(7, H_longitude);
			        	    pstmt.setInt(8, H_max_speed);
			        	    pstmt.setInt(9, H_min_pressure);
			        	    pstmt.setString(10, C_number);
			          	pstmt.executeUpdate();
	
					}
	        }
			 DBconn.cleanConnection(null,statement,conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    br.close();
	    //System.out.println("WriteIntoDB() done!!!");	 
	}

}
