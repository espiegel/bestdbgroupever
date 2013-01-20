package db;

import java.sql.ResultSet;

import objects.Location;

public class LocationRetriever extends RetrieverBase<Location> {

	private final String[] default_fields = {"location_id"};
	private final String[] search_fields = {"country", "city","place"};

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
	protected Location makeObject(ResultSet result_set) {
		try {
			return fillObjectByFields(result_set, new Location());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected String getOrderByField() {
		return "place";
	}
}
