package com.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.ScatteringByteChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	   public static void main(String[] args) throws SQLException {
		   //getAllNodesNoDepulicatesTaxid();
		   getResult();
	   }
	
	   public static void getResult() throws SQLException{
		    Connection con = DriverManager.getConnection(
		            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
		    
		    
			 Map<String,String> newMap=loadALlLineageNumber();
			 
	         //get mind_lineage_number from newMap memory
	         String taxid1=null;
			 String lineage1=null;
			  
			 String taxidCom1=null;
			 String name1=null;
			 String type1 = null;
			 String rank1=null;
	         
	         for(String key : newMap.keySet()) {
		        //System.out.println(key+"   "+newMap.get(key));
		        taxid1=key;
	        	    String temp[]=newMap.get(key).split("\\|\\|");
      	        String nameList="";
      	        String nameResult=null;
      	       
      	        for(int i=0;i<temp.length;i++) {
      	         	Statement s  = con.createStatement();
	    	        		String sql="SELECT * FROM mind_unique where taxid='"+temp[i]+"' limit 5";
	    	        		ResultSet result = s.executeQuery(sql);
    	        			
    	        			Map<String, String> resultMap=new HashMap<>();
	    	        		
    	        			while (result.next()) {
	    	 	      	      taxidCom1=result.getString("taxid");
	    	 	      	      name1=result.getString("name");
	    	 	      	      type1=result.getString("type");
	    	 	      	      rank1=result.getString("rank");  
	    	        		}

    	        			//nameList+=resultName+"["+resultTaxid+"]"+"||";
    	        			nameList+=taxidCom1+"*"+name1+"*"+type1+"*"+rank1+"||";
    	        			nameResult=nameList.substring(0, nameList.length()-2);
      	        }
      	        System.out.println(taxid1+"\t"+nameResult);
      	        writeUsingFileWriter(taxid1+"\t"+nameResult+"\r\n");
	         }
	         System.out.println("done!");   
	   }   
	   
	   
	   
	   public static Map<String, String> loadALlLineageNumber(){
		      String taxid=null;
			  String parent_taxid=null;
			  String rank;
			 
			  String taxidCom;
			  String name;
			  String type;
			 
			  Map<String,String> newMap=new HashMap<>();
			 
		      try {
		         Class.forName("org.postgresql.Driver");
		      } catch(ClassNotFoundException e) {
		         System.out.println("Class not found "+ e);
		      }
		      
		      try {
		         Connection con = DriverManager.getConnection(
		            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
		         
			     //taxid -- parent_taxid
			     Statement statement = con.createStatement();
		         ResultSet res = statement.executeQuery("SELECT * FROM original_nodes");
		         
		         Map<String,String> mapl= new HashMap<>(); 
		     
		         while (res.next()) {
	        	        taxid=res.getString("taxid");
	        	        parent_taxid=res.getString("parent_taxid");
	        	        rank=res.getString("rank");
	        	        
	        	        if(!mapl.containsKey(taxid)) {
	        	        		mapl.put(taxid, parent_taxid); 
	        	        }
		         }

		         for(String key : mapl.keySet()) {
		        	 	String orig_lineage="";
		        	 	String keyCopy=key;
		        	 	
		        	 	if(mapl.get(key).equals("1")) {
		        	 		newMap.put(keyCopy, "1");
		        	 		continue;
		        	 	}
		        	 		
		        	 	while(!mapl.get(key).equals("1")) {
		        	 		orig_lineage+=mapl.get(key)+"||";
		        	 		key=mapl.get(key);
		        	 	}
		        	 	orig_lineage+="1";
		        	 	
		        	 	//write all the mind_lineage_number to newMap memory
		        	 	newMap.put(keyCopy, orig_lineage);
		          }
		       }catch(SQLException e) {
			         System.out.println("SQL exception occured" + e);
			   }
		       return newMap; 
	   }
	   
	   
	   //create only one table from table mind_nodes_all according to the priority
	   public static Map<Integer,String> getAllNodesNoDepulicatesTaxid() {
		      PreparedStatement ps = null;
		    
			  int taxid=0;
			  String name=null;
			  String type = null;
			  String rank=null;
			  
			  Map<Integer, List<MindNode>> map=new HashMap<>();   //the first classify map
			  Map<String,String> oneMap=null;                     //to get the only one MindNode map
			  Map<Integer,String> resultMap=null; 
			  
			  List<MindNode> list;
			  String nameList=null;
			  String nameResult=null;
			 
		      try {
		         Class.forName("org.postgresql.Driver");
		      } catch(ClassNotFoundException e) {
		         System.out.println("Class not found "+ e);
		      }
		      
		      try {
			         Connection con = DriverManager.getConnection(
			            "jdbc:postgresql://localhost:5432/postgres","postgres", "722722");
				     
	    	        		Statement s = con.createStatement();
	    	        		String sql="SELECT * FROM mind_nodes_all limit 50";
	    	        		ResultSet result = s.executeQuery(sql);
	    	        		
	    	        		Integer resultTaxid=0;
	    	        		String resultEntity=null;

 	        			while (result.next()) {
	    	 	      	      name=result.getString("name");
	    	 	      	      type=result.getString("type");
	    	 	      	      taxid=result.getInt("taxid");
	    	 	      	      rank=result.getString("rank");
	    	 	      	      
	    	 	      	      MindNode mindNode=new MindNode(name, type, rank);
	    	 	      	      
	    	 	      	      if(!map.containsKey(taxid)) {
	    	 	      	    	  		list=new ArrayList<>();
	    	 	      	    	  		list.add(mindNode);
	    	 	      	    	  		map.put(taxid, list);
	    	 	      	   	  }else {
	    	 	      	   		    list=map.get(taxid);
	    	 	      	   		    list.add(mindNode);
	    	 	      	   		    map.put(taxid, list);    
	    	 	      	   	  } 	     
 	        			}
 	        			
 	        			
 	        			for(Integer id:map.keySet()) {
 	        				oneMap=new HashMap<>();
 	        				String nType=null;
 	        				String nName=null;
 	        				String nRank=null;
 	        				
 	        				//filter to make sure each type has one record
 	        				for(int i=0;i<map.get(id).size();i++) {
 	        					nType=map.get(id).get(i).getType();
 	        					nName=map.get(id).get(i).getName();
 	        					nRank=map.get(id).get(i).getRank();
 	        					if(!oneMap.containsKey(nType)) {    
 	        						oneMap.put(nType, id+"\t"+nName+"\t"+nRank+"\t"+nType);
 	 	        				}
 	        				}
 	        					
 	        				//get the specific only one record according to priority
        					if(oneMap.get("unique name")!=null) {
	 	    	        			resultEntity=oneMap.get("unique name");
 	    	        			}
 	    	        			else if(oneMap.get("scientific name")!=null) {
 	    	        				resultEntity=oneMap.get("scientific name");
 	    	        			
 	    	        			}
 	    	        			else if(oneMap.get("genbank common name")!=null) {
 	    	        				resultEntity=oneMap.get("genbank common name");
 	    	        				
 	    	        			}
 	    	        			else if(oneMap.get("common name")!=null) {
 	    	        				resultEntity=oneMap.get("common name");
 	    	        				
 	    	        			}
 	    	        			else if(oneMap.get("equivalent name")!=null) {
 	    	        				resultEntity=oneMap.get("equivalent name");
 	    	        				
 	    	        			}
 	    	        			else if(oneMap.get("synonym")!=null) {
 	    	        				resultEntity=oneMap.get("synonym");
 	    	        			}		        					
        					
        					
        					System.out.println(resultEntity);
 	        			}
 	        			
		      } catch(SQLException e) {
		         System.out.println("SQL exception occured" + e);
		      }	 
		      return resultMap;
	   }
	     
	   
	   private static void writeUsingFileWriter(String data) {
	        File file = new File("/Users/air/Desktop/result.txt");
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
