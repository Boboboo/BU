package com.kcc;

import com.kcc.data.ProcessManager;

public class Report {
	public static void main(String[] args) throws Exception {	
		
//		PreProcess pre=new PreProcess();
//		pre.WriteIntoDB("/Users/air/Desktop/data.txt");
			
		ProcessManager manager=new ProcessManager();
		manager.progress();
	}	
}
