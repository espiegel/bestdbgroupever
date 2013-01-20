package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import objects.Location;
import objects.LocationOfMedia;

public class LocationOfMediaRetriever extends RetrieverBase<LocationOfMedia> {
	private final String[] default_fields = { "media_id,location_id" };
	private final String[] search_fields = { "scene_episode" };

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
			LocationOfMedia obj = new LocationOfMedia();
			obj.getClass().getField("media_id")
					.set(obj, result_set.getObject("media_id"));
			obj.getClass().getField("scene_episode")
					.set(obj, result_set.getObject("scene_episode"));

			Location loc = fillLocationByFields(result_set, new Location());
			obj.getClass().getField("location").set(obj, loc);

			return obj;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	protected Location fillLocationByFields(ResultSet result_set,
			Location instance) throws IllegalArgumentException,
			IllegalAccessException, SQLException {
		for (java.lang.reflect.Field field : instance.getClass().getFields()) {
			final String fieldname = field.getName();
			field.set(instance, result_set.getObject(fieldname));
		}

		return instance;
	}
}
