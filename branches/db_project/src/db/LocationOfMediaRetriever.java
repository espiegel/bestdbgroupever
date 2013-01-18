package db;

import java.sql.ResultSet;
import objects.LocationOfMedia;


public class LocationOfMediaRetriever extends RetrieverBase<LocationOfMedia>{
	private final String[] default_fields = {"media_id,location_id"};
	private final String[] search_fields = {"scene_episode"};

	@Override
	protected String getTableNames() {
		return "Locations, LocationOfMedia";
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
	protected LocationOfMedia makeObject(ResultSet result_set) {
		try {
			return fillObjectByFields(result_set, new LocationOfMedia());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
