package db;

import java.sql.ResultSet;

import objects.Comment;

public class CommentRetriever extends RetrieverBase<Comment> {

	private final String[] default_fields = {"comment_id", "user_id","location_id"};
	private final String[] search_fields = {"comment"};

	@Override
	protected String getTableNames() {
		return "Locations";
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
	protected Comment makeObject(ResultSet result_set) {
		try {
			return fillObjectByFields(result_set, new Comment());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
