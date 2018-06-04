package com.bobo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBConnection {
	public static Connection getDBConnection() {
		
		String databaseURL = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "722722";
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(databaseURL, user, password);
            if (conn != null) {
                System.out.println("Connected to the database successfully!");
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Could not find database driver class.");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid.");
            ex.printStackTrace();

        }
        return conn;
		
	}
	
	public static void cleanConnection(ResultSet rs, Statement stmt, Connection conn){
	    if (rs != null) {
	        try { rs.close(); } catch (SQLException e) { ; }
	        rs = null;
	    }
	    if (stmt != null) {
	        try { stmt.close(); } catch (SQLException e) { ; }
	        stmt = null;
	    }
	    if (conn != null) {
	        try { conn.close(); } catch (SQLException e) { ; }
	        conn = null;
	    }
	}
}
