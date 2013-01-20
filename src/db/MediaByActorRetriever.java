package db;

import java.sql.ResultSet;

import objects.Media;

public class MediaByActorRetriever extends
		RetrieverBase<Media> {

	private final String[] default_fields = {"Media.media_id" };
	private final String[] search_fields = { "Actors.name" };

	@Override
	protected String getTableNames() {
		return "Media, Actors, ActorsInMedia";
	}

	@Override
	protected String getJoinLine() {
		return "Actors.actor_id = ActorsInMedia.actor_id AND ActorsInMedia.media_id = Media.media_id";
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

}
