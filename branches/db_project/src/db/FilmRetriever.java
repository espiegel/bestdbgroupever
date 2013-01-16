package db;

import java.sql.ResultSet;

import objects.Film;

public class FilmRetriever extends RetrieverBase<Film> {

	@Override
	protected String getTableNames() {
		return "Media, Films";
	}

	@Override
	protected String getJoinLine() {
		return "Media.media_id = Films.media_id";
	}
	
	final String[] default_fields = {"Media.media_id"};

	@Override
	protected String[] getDefaultFields() {
		return default_fields;
	}

	@Override
	protected Film makeObject(ResultSet result_set) {
		try {
			return fillObjectByFields(result_set, new Film());
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
