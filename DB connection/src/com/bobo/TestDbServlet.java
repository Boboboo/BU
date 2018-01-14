package com.bobo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/TestDbServlet")
public class TestDbServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//setup connection variables
		String dbName = "jdbc:postgresql://localhost/postgres";
		String user="postgres";
		String pass="722722";
		String driver="org.postgresql.Driver";
		String jdbcUrl="jdbc:postgresql://127.0.0.1:5432/postgres";
		
		//get connection to database
		try {
			PrintWriter out=response.getWriter();
			out.println("Connecting to database:"+jdbcUrl);
			
			
			Class.forName(driver);
			
			Connection myConnection=DriverManager.getConnection(dbName,user,pass);
			out.println("success!!!");
			
			Statement statement = myConnection.createStatement();
	        String sql = "select * from company";
	        ResultSet rs = statement.executeQuery(sql);
	        while (rs.next()) {
	            out.println(rs.getString("name"));
	        }
			myConnection.close();	
		}catch(Exception e){
				System.out.println("Connection URL or username or password errors!");
				e.printStackTrace();
			}
		
	}
	
}
