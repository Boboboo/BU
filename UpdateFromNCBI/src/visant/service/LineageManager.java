package visant.service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import visant.db.DBConnection;
import visant.entity.NodeEntity;

public class LineageManager {
	
	private Map<Integer,NodeEntity> mResultMap=new HashMap<>();
	private Map<Integer,String> mLineageMap=new HashMap<>();
	
	public void lineageProgress() {	
		MainManager manager = new MainManager();
		
		//update mind_nodes_all table
		manager.initailTable("mind_nodes_all");
		System.out.println("Loading the third process...");
		updateMind_Nodes_All();	
		updateRank();
		System.out.println("(3/4)Update table mind_nodes_all successfully.");
		
		//update mind_lineage table
		manager.initailTable("mind_lineage");
		System.out.println("Loading the forth process...");
		mResultMap=getAllNodesNoDepulicateTaxid();
		mLineageMap=getAllLineageNumber();
		getLineageResult();
		System.out.println("(4/4)Update table mind_lineage successfully.");
	}
	
	
	private void updateMind_Nodes_All() {
		DBConnection DBconn=new DBConnection();
		Connection conn=DBconn.getDBConnection();
		Statement statement=null;
		ResultSet resultSet=null;
		String sql="";
		
		NodeEntity nodeEntity=null;
		int id=0;
		
		try {
			statement = conn.createStatement();
			sql = "SELECT * FROM original_names "
				+ "WHERE type='scientific name' "
				+ "OR type='common name'"
				+ "OR type='genbank common name'" 
				+ "OR type='equivalent name'"
				+ "OR type='synonomy'"
				+ "OR unique_name!=''";
			
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				id++;
				nodeEntity=new NodeEntity();
				nodeEntity.setName(resultSet.getString("name"));
				nodeEntity.setType(resultSet.getString("type"));
				nodeEntity.setTaxid(resultSet.getInt("taxid"));
				nodeEntity.setUnique_name(resultSet.getString("unique_name"));
				String theName=nodeEntity.getName();
				String theType=nodeEntity.getType();
				int theTaxid=nodeEntity.getTaxid();
				String theUnique_name=nodeEntity.getUnique_name();
				
				if(theUnique_name=="" || theUnique_name==null || theUnique_name.length()==0 ) {
					insertEachRow(DBconn,conn,id,theName,theType,theTaxid);
				}
				
				if(theUnique_name.length()!=0 ) {
					theName=theUnique_name;
					theType="unique name";	
					insertEachRow(DBconn,conn,id,theName,theType,theTaxid);
				}	
			}	
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void insertEachRow(DBConnection DBconn,Connection conn,int id,String name,String type,int taxid) {
		String sql="";
		
		try {		
			sql = "INSERT INTO mind_nodes_all VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.setString(2, name);
			pstmt.setString(3, type);
	        	pstmt.setInt(4, taxid);
	        	pstmt.setString(5, "");
	        pstmt.setInt(6, 0);
	        pstmt.executeUpdate();    		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updateRank(){
		DBConnection DBconn=new DBConnection();
		Connection conn=DBconn.getDBConnection();
		Statement statement=null;
		String sql="";
		
		try {
			statement = conn.createStatement();
			sql="update mind_nodes_all set rank=original_nodes.rank from original_nodes "
			   +"where original_nodes.taxid=mind_nodes_all.taxid";
			statement.executeUpdate(sql);
		} catch(Exception e) {
	         e.printStackTrace();
	    }
	}
	
	
	//create only one table from table mind_nodes_all according to the priority
	private Map<Integer,NodeEntity> getAllNodesNoDepulicateTaxid() {
			  DBConnection DBconn=new DBConnection();
			  Connection conn=DBconn.getDBConnection();
			  String sql="";
		    
			  int taxid=0;
			  String name=null;
			  String type = null;
			  String rank=null;
			  
			  //put all nodes in allMap, key is taxid, value is list of related nodeEntity
			  Map<Integer, List<NodeEntity>> allMap=new HashMap<>();   
			  List<NodeEntity> list;
			 
		      try {
	    	        		Statement s = conn.createStatement();
	    	        		sql="SELECT * FROM mind_nodes_all";
	    	        		ResultSet result = s.executeQuery(sql);

	        			while (result.next()) {
	    	 	      	      name=result.getString("name");
	    	 	      	      type=result.getString("type");
	    	 	      	      taxid=result.getInt("taxid");
	    	 	      	      rank=result.getString("rank");
	    	 	      	      
	    	 	      	      NodeEntity nodeEntity=new NodeEntity(taxid, name, type, rank);
	    	 	      	      if(!allMap.containsKey(taxid)) {
	    	 	      	    	  		list=new ArrayList<>();
	    	 	      	    	  		list.add(nodeEntity);
	    	 	      	    	  		allMap.put(taxid, list);
	    	 	      	   	  }else {
	    	 	      	   		    list=allMap.get(taxid);
	    	 	      	   		    list.add(nodeEntity);
	    	 	      	   		    allMap.put(taxid, list);    
	    	 	      	   	  } 	     
	        			}
	        			
	        			for(Integer nTaxid:allMap.keySet()) {
 	        				Map<String, NodeEntity>oneMap=new HashMap<>();
 	        				String nType=null;
 	        				NodeEntity nEntity=null, resultEntity=null;
 	        				
 	        				//filter to make sure each type has one record
 	        				for(int i=0;i<allMap.get(nTaxid).size();i++) {
 	        					nEntity=allMap.get(nTaxid).get(i);
 	        					nType=allMap.get(nTaxid).get(i).getType();
 	        					if(!oneMap.containsKey(nType)) {    
 	        						oneMap.put(nType,nEntity);
 	 	        				}
 	        				}
	        					
	        				//get the specific only one record according to priority
     					if(oneMap.get("unique name")!=null) {
     						resultEntity=oneMap.get("unique name");
	    	        			}
	    	        			else if(oneMap.get("scientific name")!=null) {
	    	        				resultEntity=oneMap.get("scientific name");
	    	        			}
	    	        			else if(oneMap.get("common name")!=null) {
	    	        				resultEntity=oneMap.get("common name");	
	    	        			}
	    	        			else if(oneMap.get("genbank common name")!=null) {
	    	        				resultEntity=oneMap.get("genbank common name");	
	    	        			}
	    	        			else if(oneMap.get("equivalent name")!=null) {
	    	        				resultEntity=oneMap.get("equivalent name");	
	    	        			}
	    	        			else if(oneMap.get("synonym")!=null) {
	    	        				resultEntity=oneMap.get("synonym");
	    	        			}
     					
     					if(!mResultMap.containsKey(resultEntity.getTaxid())) {
     						mResultMap.put(resultEntity.getTaxid(),resultEntity);
     					}	
	        			}	
		      } catch(Exception e) {
		         e.printStackTrace();
		      }
		     // System.out.println(mResultMap.size()); 
		      return mResultMap;	     
	   }
	
	
	private Map<Integer, String> getAllLineageNumber(){
		 DBConnection DBconn=new DBConnection();
		 Connection conn=DBconn.getDBConnection();
		 String sql="";
		 
	     int taxid=0,parent_taxid=0;
	     String rank;

	     try {
			 Statement statement = conn.createStatement();
			 ResultSet res = statement.executeQuery("SELECT * FROM original_nodes");

			 Map<Integer,Integer> tempMap= new HashMap<>(); 

			 while (res.next()) {
				taxid=res.getInt("taxid");
				parent_taxid=res.getInt("parent_taxid");
				rank=res.getString("rank");

				if(!tempMap.containsKey(taxid)) {
					tempMap.put(taxid, parent_taxid); 
				}
			 }

			 for(Integer key : tempMap.keySet()) {
				String orig_lineage="";
				Integer keyCopy=key;

				if(tempMap.get(key)==1) {
					mLineageMap.put(keyCopy, "1");
					continue;
				}
				
				while(tempMap.get(key)!=1) {
					orig_lineage+=String.valueOf(tempMap.get(key))+"||";
					key=tempMap.get(key);
				}
				orig_lineage+="1";
				
				if(!mLineageMap.containsKey(keyCopy)) {
					mLineageMap.put(keyCopy, orig_lineage);
				}	
			  }
	       }catch(Exception e) {
		         e.printStackTrace();
		   }
//	    System.out.println(mLineageMap.size());
	    return mLineageMap; 
	 }

	
	 private void getLineageResult() {
		     DBConnection DBconn=new DBConnection();
			 Connection conn=DBconn.getDBConnection();
			 Statement statement=null;
			 
	         //get mind lineage number format from mResultMap memory
	         for(Integer key : mLineageMap.keySet()) {
				String temp[]=mLineageMap.get(key).split("\\|\\|");
				
				String resultList="";
				String oneList="";
				for(int i=0;i<temp.length;i++) {
					NodeEntity nodeEntity=mResultMap.get(Integer.valueOf(temp[i]));

					if(nodeEntity!=null) {
						oneList=String.valueOf(nodeEntity.getTaxid())+"*"+nodeEntity.getName()+"*"+nodeEntity.getType()+"*"+nodeEntity.getRank()+"||";
					}	
					resultList+=oneList; 
	        	    }
				resultList=resultList.substring(0, resultList.length()-2);
				System.out.println(key+"---"+resultList);
				insertLineageResult(DBconn,conn,key,resultList) ;
	         }        
	  }
	 
	 private void insertLineageResult(DBConnection DBconn,Connection conn,int taxid,String lineage) {
			String sql="";
			
			try {		
				sql = "INSERT INTO mind_lineage VALUES (?, ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, taxid);
		        	pstmt.setString(2, lineage);
		        pstmt.executeUpdate();    		
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	    
}
