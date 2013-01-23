package main;

import gui.Login;

import java.sql.SQLException;
import java.sql.Statement;

import objects.User;
import db.ConnectionManager;
import db.UserRetriever;

public class Main
{	
	private static User currentUser; // The current user thats logged in.
	
	public static User getCurrentUser() {
		// Make it update the currentUser
		if(currentUser == null)
			return null;
		
		User newUser = new UserRetriever().retrieveById(currentUser.getID());
		setCurrentUser(newUser);
		return currentUser;
	}

	public static void setCurrentUser(User _currentUser) {
		currentUser = _currentUser;
	}
	
	public static void main(String[] args)
	{
		ConfigurationManager.init();
		ConnectionManager.openConnection();
		Login.main(null); // Call the main GUI. In this case Login is the first GUI window.
		ConnectionManager.closeConnection();
		
	}
	
	/**
	 * 
	 * @param user Username
	 * @param password Password
	 * @return Returns true if successful and false otherwise. Also sets the currentUser appropriately
	 */
	public static boolean login(String user, String password)
	{
		UserRetriever ret = new UserRetriever();
	
		currentUser = ret.retrieveByUserAndPassword(user,password);
		
		if (currentUser==null)
			return false;
						
		return true;
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
		
		try
		{
			UserRetriever ret = new UserRetriever();
			if (ret.retrieveByName(user)!=null) {
				return false;
			}
			
			// Otherwise we successfully registered.
			//TODO: should be moved to db package
			stmt = ConnectionManager.conn.createStatement();
			stmt.executeUpdate("INSERT INTO Users (name, password, upvotes, downvotes, is_admin) "+
			                   "VALUES ('"+user+"', '"+password+"', 0, 0, 0)");
						
			// closing
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
}
