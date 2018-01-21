package com.data;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;

public class formatData {
	
	public static String[] JSONArraytoArray(JSONArray jsonArray) {
		String[] array = new String[jsonArray.size()];
		if (jsonArray != null) { 
		   int len = jsonArray.size();
		   for (int i=0;i<len;i++){ 
			   array[i]=jsonArray.get(i).toString();
		   } 
		}
		return array; 
	}
	
	public static List<String> toList(JSONArray jsonArray) {
		ArrayList<String> list = new ArrayList<String>();     
		if (jsonArray != null) { 
		   int len = jsonArray.size();
		   for (int i=0;i<len;i++){ 
			   list.add(jsonArray.get(i).toString());
		   } 
		}
		return list; 
	}

}
