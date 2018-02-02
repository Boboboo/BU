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
//		         .addAnnotatedClass(Context.class)
		         .buildSessionFactory();

		 //write nodes data to DB
		 Session session=factory.getCurrentSession();
		 session.beginTransaction();
		  
		 JSONParser parser = new JSONParser();
		
         try {    
             JSONArray data = (JSONArray) parser.parse(new FileReader("/Users/air/Desktop/1/updatedNodes.json"));
 
             for (Object o : data){
				  JSONObject node = (JSONObject) o;
				  
				  String taxid = (String) node.get("taxid");
   			      String lineage = (String) node.get("lineage");
   			      String name = (String) node.get("name");
   			      String taxlevel = (String) node.get("taxlevel");
   			      String id= (String) node.get("id");
//   			  JSONArray list=(JSONArray) node.get("children");
//		          String children=JSONArraytoArray(list);
			      //System.out.println(children);

			      Node theNode = new Node(taxid,lineage, name, taxlevel,id);
			      
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
            JSONArray data = (JSONArray) parser.parse(new FileReader("/Users/air/Desktop/1/links.json"));
            for (Object o : data){
				  JSONObject link = (JSONObject) o;
				  Double pvalue = (Double) link.get("pvalue");
				  String source = (String) link.get("source");
				  String source_cond = (String) link.get("source_cond");
				  String target = (String) link.get("target");
				  String target_cond = (String) link.get("target_cond"); 
				  Double weight = (Double) link.get("weight");
			    
			      Link theLink = new Link(pvalue, source, source_cond,target,target_cond,weight);
				
				  session.save(theLink);
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
         
         
         //write contexts data to DB
//       session=factory.getCurrentSession();
//		 session.beginTransaction();
//		 parser = new JSONParser();
//         try {     
//            JSONArray data = (JSONArray) parser.parse(new FileReader("/Users/air/Desktop/contexts.json"));
//            for (Object o : data){
//				  JSONObject context = (JSONObject) o;
//            		  String id=(String) context.get("id");
//            	      String body_site=(String) context.get("body_site");
//            	      String condition=(String) context.get("condition");
//            	      String disease_state=(String) context.get("disease_state");
//            	      String experiment_info=(String) context.get("experiment_info");
//            		  String host=(String) context.get("host");
//            		  String interaction_type=(String) context.get("interaction_type");
//            		  Integer pubmed_id=(Integer) context.get("pubmed_id");
//                   Double pval_cutoff=(Double) context.get("pval_cutoff");
//            	      String pvalue_method=(String) context.get("pvalue_method");
//            	      Context theContext=new Context(id, body_site, condition, disease_state, experiment_info, host, interaction_type, pubmed_id, pval_cutoff, pvalue_method);
//				  session.save(theContext);
//             }
//            
//             session.getTransaction().commit();
//             
//         } catch (FileNotFoundException e) {
//             e.printStackTrace();
//         } catch (IOException e) {
//             e.printStackTrace();
//         } catch (ParseException e) {
//             e.printStackTrace();
//         }  catch (Exception e) {
//			factory.close();
//			//session.close();
//		}              
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
