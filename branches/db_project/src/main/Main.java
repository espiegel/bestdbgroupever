package main;

import gui.*;

import java.sql.*;

public class Main
{
	static Connection conn; // DB Connection
	
	final static String DB   = "DbMysql02";
	final static String USER = "DbMysql02";
	final static String PASS = "DbMysql02"; // Is there a better way of storing these two? Maybe in a file?

	private static String currentUser = ""; // The current user thats logged in.
	
	public static void main(String[] args)
	{
		openConnection();
		Login.main(null); // Call the main GUI. In this case Login is the first GUI window.
		closeConnection();
	}
	
	/**
	 * 
	 * @param user Username
	 * @param password Password
	 * @return Returns true if successful and false otherwise. Also sets the currentUser appropriately
	 */
	public static boolean login(String user, String password)
	{
		Statement stmt;
		ResultSet rs;
		
		try
		{
			stmt	=	conn.createStatement();
			rs		=	stmt.executeQuery("SELECT * FROM Users WHERE name = '" + user + "' AND password = '" + password +"'");

			// This means there is no user of this name
			if(!rs.next())
				return false;
			
			// Otherwise we successfully logged in.
			currentUser = user;
						
			// closing
			rs.close();
			stmt.close();
			
			return true;

		}
		catch (SQLException e)
		{
			System.out.println("ERROR executeQuery - " + e.toString());
			java.lang.System.exit(0); 
			return false;
		}
	}

	/**
	 * 
	 * @param user Username
	 * @param password Password
	 * @return Returns true if successful and false otherwise.
	 */
	public static boolean register(String user, String password)
	{
		Statement stmt;
		ResultSet rs;
		
		try
		{
			stmt	=	conn.createStatement();
			rs		=	stmt.executeQuery("SELECT * FROM Users WHERE name = '" + user + "'");

			// This means there is already a user of this name
			if(rs.next())
				return false;
			
			// Otherwise we successfully registered.
			stmt.executeUpdate("INSERT INTO Users (name, password, upvotes, downvotes, badges, is_admin) "+
			                   "VALUES ('"+user+"', '"+password+"', 0, 0, 0, 0)");
						
			// closing
			rs.close();
			stmt.close();
			
			return true;

		}
		catch (SQLException e)
		{
			System.out.println("ERROR executeQuery - " + e.toString());
			java.lang.System.exit(0); 
			return false;
		}
	}
	
	/**
	 * 
	 * @return the connection (null on error)
	 */
	private static void openConnection()
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
					"jdbc:mysql://localhost:3305/"+DB,USER,PASS);
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
	private static void closeConnection()
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

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
}
