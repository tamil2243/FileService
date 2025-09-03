package org.fileservice.repository;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL="jdbc:mysql://localhost:3306/temp";
    private static final String USER_NAME="user";
    private static final String PASSWORD="123456";

	public static Connection getConnection() {
   	 try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    	} catch (Exception e) {
        	e.printStackTrace();
        	return null;
    	}
	}
}
