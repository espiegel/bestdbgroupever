package db;

import java.sql.ResultSet;

import objects.MediaByActor;

public class MediaByActorRetriever extends
		RetrieverBase<MediaByActor> {

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
	protected MediaByActor makeObject(ResultSet result_set) {
		try {
			MediaByActor mba = new MediaByActor();
			mba.display = result_set.getString("Media.name") + " - " + result_set.getString("Actors.name");
			for (java.lang.reflect.Field field : mba.media.getClass().getFields()) {
				final String fieldname = field.getName();
				field.set(mba.media, result_set.getObject(fieldname));
			}
			mba.media_id = mba.media.media_id;
			return mba;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected String getOrderByField() {
		return "Media.name";
	}
}
