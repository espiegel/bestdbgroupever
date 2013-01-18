package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import objects.CommentOfUser;


public class CommentOfUserRetriever extends RetrieverBase<CommentOfUser> {

	private final String[] default_fields = {"comment_id"};
	private final String[] search_fields = {"comment_id"};

	@Override
	protected String getTableNames() {
		return "CommentOfUser";
	}

	@Override
	protected String getJoinLine() {
		return "";
	}

	@Override
	protected String[] getDefaultFields() {
		return default_fields; 
	}

	@Override
	protected String[] getFieldForGeneralSearch() {
		return search_fields;
	}

	@Override
	protected CommentOfUser makeObject(ResultSet rs) {
		if(rs == null) // Should never actually be null
			return null;
		
		CommentOfUser commentOfUser = new CommentOfUser();
		
		try
		{
			commentOfUser.setComment_id(rs.getInt(1));
			commentOfUser.setUser_id(rs.getInt(2));
			commentOfUser.setVote(rs.getInt(3));
			
			return commentOfUser;
		}
		catch (SQLException e)
		{
			e.printStackTrace(); 
			return null;
		}
	}
}
