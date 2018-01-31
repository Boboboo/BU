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

public class NodeSampleToDB {
	public static void main(String[] args) {
		 SessionFactory factory=new Configuration()
				 .configure("hibernate.cfg.xml")
				 .addAnnotatedClass(Node.class)
		         .buildSessionFactory();

		 //write name data to DB
		 Session session=factory.getCurrentSession();
		 session.beginTransaction();
		  
		 JSONParser parser = new JSONParser();
		
        try {    
            JSONArray data = (JSONArray) parser.parse(new FileReader("/Users/air/Desktop/node.json"));

            for (Object o : data){
				  JSONObject node = (JSONObject) o;
				  
				  String taxid = (String) node.get("taxid");
  			      String parent_taxid = (String) node.get("parent_taxid");
  			      String rank = (String) node.get("rank");

			      Node theNode=new Node(taxid, parent_taxid, rank);
			      
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
