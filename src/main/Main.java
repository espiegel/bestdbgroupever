package main;

import gui.Login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import objects.User;

public class Main
{
	static Connection conn; // DB Connection
	
	private static User currentUser; // The current user thats logged in.
	
	public static User getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(User currentUser) {
		Main.currentUser = currentUser;
	}

	public static ResultSet performQuery(String sql)
	{
		Statement stmt;
		ResultSet rs;
		
		try
		{
			stmt	=	conn.createStatement();
			rs		=	stmt.executeQuery(sql);
			
			return rs;
		}
		catch (SQLException e)
		{
			System.out.println("ERROR executeQuery - " + e.toString());
			//java.lang.System.exit(0); 
			return null;
		}
	}
	
	private static User generateUser(ResultSet rs)
	{
		if(rs == null) // Should never actually be null because we catch the exception earlier.
			return null;
		
		User user = new User();
		
		try
		{
			user.setUsername(rs.getString(2));
			user.setPassword(rs.getString(3));
			user.setUpvotes(rs.getInt(4));
			user.setDownvotes(rs.getInt(5));
			user.setBadges(rs.getInt(6));
			user.setAdmin(rs.getBoolean(7));
			
			return user;
		}
		catch (SQLException e)
		{
			System.out.println("ERROR executeQuery - " + e.toString());
			java.lang.System.exit(0); 
			return null;
		}
		
	}
	
	public static void main(String[] args)
	{
		ConfigurationManager.init();
		openConnection();
		Login.main(null); // Call the main GUI. In this case Login is the first GUI window.
		closeConnection();
	}
	
	private static ResultSet getUserData(String username)
	{
		Statement stmt;
		ResultSet rs;
		
		try
		{
			stmt	=	conn.createStatement();
			rs		=	stmt.executeQuery("SELECT * FROM Users WHERE name = '" + username + "'");
			
			return rs;
		}
		catch (SQLException e)
		{
			System.out.println("ERROR executeQuery - " + e.toString());
			java.lang.System.exit(0); 
			return null;
		}
		
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
			currentUser = generateUser(rs);
						
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
		} catch (Exception e) {
			System.err.println("Error checking login details.");
			e.printStackTrace();
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
			
			rs = getUserData(user);

			// This means there is already a user of this name
			if(rs.next())
				return false;
			
			
			// Otherwise we successfully registered.
			stmt = conn.createStatement();
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

}
