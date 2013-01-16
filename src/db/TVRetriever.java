package db;

import java.sql.ResultSet;

import objects.TVShow;

public class TVRetriever extends RetrieverBase<TVShow> {

	@Override
	protected String getTableNames() {
		return "Media, TV";
	}

	@Override
	protected String getJoinLine() {
		return "Media.media_id = TV.media_id";
	}
	
	final String[] default_fields = {"Media.media_id"};

	@Override
	protected String[] getDefaultFields() {
		return default_fields;
	}

	@Override
	protected TVShow makeObject(ResultSet result_set) {
		try {
			return fillObjectByFields(result_set, new TVShow());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private final String[] search_fields={"name"}; 

	@Override
	protected String[] getFieldForGeneralSearch() {
		return search_fields;
	}

}
