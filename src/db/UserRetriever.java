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
			user.setID(rs.getInt("user_id"));
			user.setUsername(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setUpvotes(rs.getInt("upvotes"));
			user.setDownvotes(rs.getInt("downvotes"));
			user.setAdmin(rs.getBoolean("is_admin"));
			
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
	
	@Override
	protected String getOrderByField() {
		return "name";
	}
}