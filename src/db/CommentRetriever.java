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
			comment.setId(rs.getInt("comment_id"));
			comment.setUser_id(rs.getInt("user_id"));
			comment.setLocation_id(rs.getInt("location_id"));
			comment.setComment(rs.getString("comment"));
			comment.setUpvotes(rs.getInt("upvotes"));
			comment.setDownvotes(rs.getInt("downvotes"));
			comment.setIs_check_in(rs.getInt("is_check_in"));
			comment.setDatetime(rs.getDate("date"));
			
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

	public java.util.List<Comment> retrieveByID(int id) {
		return retrieve("location_id = " + id);
	}
}
