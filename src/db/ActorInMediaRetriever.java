package db;

import java.sql.ResultSet;
import java.util.List;

import objects.ActorInMedia;

public class ActorInMediaRetriever extends RetrieverBase<ActorInMedia> {

	private final static String[] def_fields = {"actor_id", "media_id"};
	private final static String[] search_fields = {"char_name"};

	@Override
	protected String getTableNames() {
		return "Actors, ActorsInMedia";
	}

	@Override
	protected String getJoinLine() {
		return "Actors.actor_id = ActorsInMedia.actor_id";
	}

	@Override
	protected String[] getDefaultFields() {
		return def_fields;
	}

	@Override
	protected String[] getFieldForGeneralSearch() {
		return search_fields;
	}

	@Override
	protected ActorInMedia makeObject(ResultSet result_set) {
		try {
			return fillObjectByFields(result_set, new ActorInMedia());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<ActorInMedia> retrieveByMediaID(int media_id) {
		return retrieve("media_id = " + media_id);
	}

}
