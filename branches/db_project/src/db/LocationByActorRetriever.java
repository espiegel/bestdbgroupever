package db;

import java.sql.ResultSet;

import objects.Location;

public class LocationByActorRetriever extends RetrieverBase<Location> {

	private final String[] default_fields = {"Actors.actor_id"}; 
	private final String[] search_fields = { "Actors.name" };
	
	@Override
	protected String getTableNames() {
		return "Actors, ActorsInMedia, LocationOfMedia, Locations";
	}

	@Override
	protected String getJoinLine() {
		String join1 = "Actors.actor_id = ActorsInMedia.actor_id";
		String join2 = "ActorsInMedia.media_id = LocationOfMedia.media_id";
		String join3 = "LocationOfMedia.location_id = Locations.location_id";
		return join1+" AND "+join2+" AND "+join3;
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
