package com.kcc;

import com.kcc.data.InitProcess;
import com.kcc.data.ProcessManager;

public class Report {

	public static void main(String[] args) throws Exception {

		if (args.length == 2) {
			if ("init".equals(args[0])) {
				InitProcess initProcess = new InitProcess();
				initProcess.init(args[1]);
			} else if ("create".equals(args[0])) {
				ProcessManager manager = new ProcessManager();
				manager.progress(args[1]);
			}
		} else {
			System.out.println("Invalid parameter, try with init <path> or create <path>");
		}

	}
}
