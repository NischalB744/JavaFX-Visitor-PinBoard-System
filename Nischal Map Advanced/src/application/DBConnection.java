package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static Connection conn;  
	private static String url = "jdbc:mysql://localhost:3306/maptest";  
	private static String user = "root";//Username of database  
	private static String pass = "Tetrahedronist9";//Password of database  
	public static Connection connect() throws SQLException{  
		try{  
			Class.forName("com.mysql.jdbc.Driver").newInstance();  
		}catch(ClassNotFoundException cnfe){  
			System.err.println("Error: "+cnfe.getMessage());  
		}catch(InstantiationException ie){  
			System.err.println("Error: "+ie.getMessage());  
		}catch(IllegalAccessException iae){  
			System.err.println("Error: "+iae.getMessage());  
		}  
		conn = DriverManager.getConnection(url,user,pass);  
		return conn;  
	}  
	public static Connection getConnection() throws SQLException, ClassNotFoundException{  
		if(conn !=null && !conn.isClosed())  
			return conn;  
		connect();  
		return conn;  
	}  
}   







/*
 * package Nischal;
 

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection
{
	static private Connection conn;
	
	public static Connection getConnection() throws Exception
	{
		if(conn == null)
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql//127.0.0.1:3306/visitorinfo", "root", "Tetrahedronist9");
		}
		return conn;
	}
}
*/
