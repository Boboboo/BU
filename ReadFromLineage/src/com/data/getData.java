package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;


public class getData {
	
	public static void main(String[] args) throws SQLException {
		try {

            File f = new File("/Users/air/Desktop/finalResult.txt");

            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine = "";

            System.out.println("Reading Start...");
            
            while ((readLine = b.readLine()) != null) {
                String [] array=readLine.split("\t");
                
                String[] subArray3=array[3].split(";");
                String[] subArray4=array[4].split(";");
                String entity1=null;
                String entity2=null;
                for(int i=0;i<subArray3.length;i++) {
                		entity1=subArray3[subArray3.length-1];	
                }
                for(int i=0;i<subArray4.length;i++) {
            			entity2=subArray4[subArray4.length-1];
                }
                if(entity1.equals("Unclassified") || entity2.equals("Unclassified")) {
                		//System.out.println(entity1+"*"+entity2);
                } else {
                		writeUsingFileWriter(array[0]+"\t"+array[1]+"\t"+array[2]+"\t"+entity1+"\t"+entity2+"\r\n");
                		writeUsingFileWriter2(array[2]+"\t"+entity1+"\t"+entity2+"\r\n");
                }  
            }
		  
		    
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private static void writeUsingFileWriter(String data) {
        File file = new File("/Users/air/Desktop/dataSetAll.txt");
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
	private static void writeUsingFileWriter2(String data) {
        File file = new File("/Users/air/Desktop/dataSet.txt");
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
