package com.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class WriteLinksToTable {
	public static void main(String[] args) {
		
		 SessionFactory factory=new Configuration()
				 .configure("hibernate.cfg.xml")
		         .addAnnotatedClass(Link.class)
		         .buildSessionFactory();

		 Session session=factory.getCurrentSession();
		 session.beginTransaction();
		  
		 JSONParser parser = new JSONParser();
		
         try {     
        	
            JSONArray data = (JSONArray) parser.parse(new FileReader("/Users/air/Desktop/links.json"));

            for (Object o : data){
				  JSONObject link = (JSONObject) o;
				  Double pvalue = (Double) link.get("pvalue");
				  String source = (String) link.get("source");
				  String source_cond = (String) link.get("source_cond");
				  String target = (String) link.get("target");
				  String target_cond = (String) link.get("target_cond"); 
				  Double weight = (Double) link.get("weight");
		          //System.out.println(pvalue+","+source+","+source_cond+","+target+","+target_cond+","+weight+",");
				  
			      System.out.println("Creating a new link object");
			    
			      Link theLink = new Link(pvalue, source, source_cond,target,target_cond,weight);
				
				  session.save(theLink);
             }
            
             session.getTransaction().commit();
             session.close();
             
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
         
         
         
        try {   
        	 		session=factory.getCurrentSession();
				session.beginTransaction();
				
				//from Link, should write the name of class, not the name of table
				String hql="from Link";
				List<Link> links = session.createQuery(hql).getResultList();
				
				System.out.println("#001");
				
			
//				for(Link theOne:links) {
//					int id=theOne.getId();
//					Double pvalue=theOne.getPvalue();
//					String source=theOne.getSource();
//					String source_cond=theOne.getSource_cond();
//					String target=theOne.getSource_cond();
//					String target_cond=theOne.getTarget_cond();
//					Double weight=theOne.getWeight();
//					Link link=new Link(id, pvalue, source, source_cond, target, target_cond, weight);
//				
//					System.out.println(link);
//				}
				
				
				session.getTransaction().commit();
				 
				System.out.println("Done!");
            
          }  catch (Exception e) {
        	  		factory.close();
        	  		//session.close();
 		 }  
	}
}
