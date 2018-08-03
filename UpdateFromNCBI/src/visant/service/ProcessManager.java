package visant.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import visant.db.DBConnection;

public class ProcessManager {
	public void progress() throws Exception {
		String filePath=getFilePath();
		
		if(filePath!=null) {
			readAllData(filePath);	
		}
		
		System.out.println("Update original_nodes successfully.");	
	}
	
	
	private String getFilePath() {
		System.out.println("Please input the path of directory which contains nodes.dmp. Example /Users/air/Desktop");
		
		//get the input data.txt file path in the keyboard 
		Scanner scanner =new Scanner(System.in);      
		String inputPath="";
		String filename="nodes.txt";
		File file;
		
		while(true){
			inputPath=scanner.nextLine();
			if(inputPath.charAt(inputPath.length()-1)!='/') {
				inputPath=inputPath+'/';
			}
			inputPath=inputPath+filename;
			
			file=new File(inputPath);
			
			//check if the file in the input path exists, if not, continue to input
			if(file.exists()) break;
			else {
				System.out.println("Cannot find file data.txt. Please try again.");
			}	
		}
		return file.getPath();	
	}
	
	
	private void readAllData(String path)  throws Exception {  
		 System.out.println("readData path: "+path);
		 
		 DBConnection DBconn=new DBConnection();
		 Connection conn=DBconn.getDBConnection();
		 String sql="";
	
		 FileInputStream fstream = new FileInputStream(path);
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		 String strLine;
		 
		 int taxid=0, parent_taxid=0;
		 String rank=null;
		 
		 try {
			 initailTable("original_nodes");
			 
			 while ((strLine = br.readLine()) != null)   {
				 String[] array=strLine.split("\t\\|\t");
				 taxid=Integer.valueOf(array[0].trim());
				 parent_taxid=Integer.valueOf(array[1].trim());
				 rank=array[2].trim();
				 
				 insertEachRow(DBconn,conn,taxid, parent_taxid, rank);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	    br.close(); 
	}
	
	private void insertEachRow(DBConnection DBconn,Connection conn,int taxid,int parent_taxid,String rank) {
		String sql="";
		
		try {		
			sql = "INSERT INTO original_nodes VALUES (?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, taxid);
	        	pstmt.setInt(2, parent_taxid);
	        	pstmt.setString(3, rank);
	        pstmt.executeUpdate();    		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private void initailTable(String tableName) {	
		DBConnection DBconn=new DBConnection();
		Connection conn=DBconn.getDBConnection();
		Statement statement=null;
		String sql="";
		
		try {
			statement = conn.createStatement();
			sql = "DELETE FROM "+tableName; 
			statement.executeUpdate(sql);
			System.out.println(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("initailTable finished");
	}
	
	
	private  void writeUsingFileWriter(String data,String path) {
        File file = new File(path+"result.txt");
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
