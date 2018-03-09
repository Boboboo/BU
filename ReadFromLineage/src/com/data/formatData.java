package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;


public class formatData {
	
	public static void main(String[] args) throws SQLException {
		try {

            File f = new File("/Users/air/Desktop/result.txt");

            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine = "";

            System.out.println("Reading Start...");
            
            while ((readLine = b.readLine()) != null) {
                String [] array=readLine.split("\t");
                if(array[3].endsWith(";")) {
                		array[3]=array[3].substring(0, array[3].length()-1);
                		System.out.println("array[3] "+array[3]);
                }
                if(array[4].endsWith(";")) {
            			array[4]=array[4].substring(0, array[4].length()-1);
            			System.out.println("array[4] "+array[4]);
                }
                
                writeUsingFileWriter(array[0]+"\t"+array[1]+"\t"+array[2]+"\t"+array[3]+"\t"+array[4]+"\r\n");
                
            }
		  
		    
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private static void writeUsingFileWriter(String data) {
        File file = new File("/Users/air/Desktop/finalResult.txt");
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
