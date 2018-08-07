package visant;

import visant.service.MainManager;

public class Gate {
	
	public static void main(String[] args) throws Exception {

		MainManager manager = new MainManager();
		manager.progress();
		System.out.println("All related tables have been successfully updated!!!");
	}
	
	
}
