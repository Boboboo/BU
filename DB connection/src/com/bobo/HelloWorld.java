package com.bobo;

import java.io.*;
import javax.servlet.*;


public class HelloWorld implements Servlet {
 
	public void init(ServletConfig arg0) throws ServletException {
		
		
	}
  
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getServletInfo() {
		return null;
	}
	

	//这个函数用于处理业务逻辑
	//res：用于向客户端返回信息
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		System.out.println("Service...");
		PrintWriter pWriter=res.getWriter();
		pWriter.println("Hello World.");
		
	}
	
	//销毁servlet实例
	public void destroy(){
		System.out.println("Destroy...");
	}
}
