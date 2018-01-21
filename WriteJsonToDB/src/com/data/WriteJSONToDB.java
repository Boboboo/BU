package com.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class WriteJSONToDB {
	public static void main(String[] args) {
		 SessionFactory factory=new Configuration()
				 .configure("hibernate.cfg.xml")
				 .addAnnotatedClass(Node.class)
		         .addAnnotatedClass(Link.class)
		         .buildSessionFactory();

		 //write nodes data to DB
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
   			      JSONArray list=(JSONArray) node.get("children");
		          String children=JSONArraytoArray(list);
			      //System.out.println(children);

			      Node theNode = new Node(taxid,children,lineage, name, taxlevel,id);
			      
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
         
         
         //write links data to DB
         session=factory.getCurrentSession();
		 session.beginTransaction();
		 parser = new JSONParser();
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
				  
			      //System.out.println("Creating a new link object");
			    
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
         
	}

	
	private static String JSONArraytoArray(JSONArray jsonArray) {
		String[] array = new String[jsonArray.size()];
		if (jsonArray != null) { 
		   int len = jsonArray.size();
		   for (int i=0;i<len;i++){ 
			   array[i]=jsonArray.get(i).toString();
		   } 
		}
		return Arrays.toString(array);
	}
}
