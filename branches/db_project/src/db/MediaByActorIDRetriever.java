package db;

import java.sql.ResultSet;

import objects.Media;

public class MediaByActorIDRetriever extends
		RetrieverBase<Media> {

	private final String[] default_fields = {"actor_id" };
	private final String[] search_fields = { "Actors.name" };

	@Override
	protected String getTableNames() {
		return "Media, ActorsInMedia";
	}

	@Override
	protected String getJoinLine() {
		return "ActorsInMedia.media_id = Media.media_id";
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
	protected Media makeObject(ResultSet result_set) {
		try {
			return fillObjectByFields(result_set, new Media());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public java.util.List<Media> searchByID(int id) {
		return retrieve("actor_id = " + id);
	}

	@Override
	protected String getOrderByField() {
		return "Media.name";
	}
}
