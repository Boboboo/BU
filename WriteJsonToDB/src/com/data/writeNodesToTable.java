package com.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class writeNodesToTable {
	public static void main(String[] args) {
		 SessionFactory factory=new Configuration()
				 .configure("hibernate.cfg.xml")
		         .addAnnotatedClass(Link.class)
		         .buildSessionFactory();

		 Session session=factory.getCurrentSession();

		 session.beginTransaction();
		  
		 JSONParser parser = new JSONParser();
		
         try {     
        	
             JSONArray data = (JSONArray) parser.parse(new FileReader("/Users/air/Desktop/nodes.json"));
 
             for (Object o : data){
            	
				  JSONObject node = (JSONObject) o;
				  
				  String taxid = (String) node.get("taxid");
   			      String lineage = (String) node.get("lineage");
   			      String name = (String) node.get("name");
   			      String taxlevel = (String) node.get("taxlevel");
   			      String id = (String) node.get("id");
   			     // String[] children=(String[]) node.get("children");
		         
			      //System.out.println(children);
			    
			     // Node theNode = new Node(taxid, children,lineage, name, taxlevel,id);
			      Node theNode = new Node(taxid, lineage, name, taxlevel,id);
				  session.save(theNode);
             }
            
             session.getTransaction().commit();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }  catch (Exception e) {
			factory.close();
			//session.close();
		}  
	}
}
