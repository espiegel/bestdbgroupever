package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import objects.Comment;

public class CommentRetriever extends RetrieverBase<Comment> {

	private final String[] default_fields = {"location_id"};
	private final String[] search_fields = {"location_id"};

	@Override
	protected String getTableNames() {
		return "Comments";
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
	protected Comment makeObject(ResultSet rs) {
		if(rs == null) // Should never actually be null
			return null;
		
		Comment comment = new Comment();
		
		try
		{
			comment.setId(rs.getInt(1));
			comment.setUser_id(rs.getInt(2));
			comment.setLocation_id(rs.getInt(3));
			comment.setComment(rs.getString(4));
			comment.setUpvotes(rs.getInt(5));
			comment.setDownvotes(rs.getInt(6));
			comment.setIs_check_in(rs.getInt(7));
			comment.setDatetime(rs.getDate(9));
			
			return comment;
		}
		catch (SQLException e)
		{
			e.printStackTrace(); 
			return null;
		}
	}
	
	@Override
	protected String getOrderByField() {
		return "date";
	}
	
	@Override
	protected boolean getOrderByAsc() {
		return false;
	}
}
