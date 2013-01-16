package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import main.ConfigurationManager;

public class ConnectionManager {
	public static Connection conn; // DB Connection
	
	/**
	 * 
	 * @return the connection (null on error)
	 */
	public static void openConnection()
	{
		
		// loading the driver
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Unable to load the MySQL JDBC driver..");
			java.lang.System.exit(0); 
		}
		System.out.println("Driver loaded successfully");
		
		
		
		// creating the connection
		System.out.print("Trying to connect... ");
		try
		{
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3305/"+ConfigurationManager.DB_SCHEMA,
					ConfigurationManager.DB_USER,
					ConfigurationManager.DB_PASS);
		}
		catch (SQLException e)
		{
			System.out.println("Unable to connect - " + e.toString());
			java.lang.System.exit(0); 
		}
		System.out.println("Connected!");
		
	}
	
	/**
	 * close the connection
	 */
	public static void closeConnection()
	{
		// closing the connection
		try
		{
			System.out.print("Closing the connection... ");
			conn.close();
		}
		catch (SQLException e)
		{
			System.out.println("Unable to close the connection - " + e.toString());
			java.lang.System.exit(0); 
		}
		System.out.println("Closed.");
	}
}
