package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import objects.User;

public class UserRetriever extends RetrieverBase<User> { 

	@Override
	protected String getTableNames() {
		return "Users";
	}

	@Override
	protected User makeObject(ResultSet rs) {
		if(rs == null) // Should never actually be null
			return null;
		
		User user = new User();
		
		try
		{
			user.setID(rs.getInt(1));
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
			e.printStackTrace(); 
			return null;
		}
	}
	
	final String[] def_fieldnames = {"name"};

	@Override
	protected String[] getDefaultFields() {
		return def_fieldnames;
	}

	@Override
	protected String getJoinLine() {
		return "";
	}

	@Override
	protected String[] getFieldForGeneralSearch() {
		return def_fieldnames;
	}

	public User retrieveByUserAndPassword(String user, String password) {
		return retrieveFirst("name = '" + user + "' AND password = '" + password +"'");
	}

	public User retrieveById(int id) {
		return retrieveFirst("user_id = " + id);
	}
	
	public User retrieveByName(String name) {
		return retrieveFirst("name = '" + name+"'");
	}
}