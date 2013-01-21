package db;

import java.sql.*;

import objects.*;

/**
 * A class that handles all admin insert/update/delete operations 
 * @author Yoav
 *
 */
public class Updater {
	private final String mediaTable = "Media", tvTable = "TV",
			filmTable = "Films", actorsTable = "Actors",
			locTable = "Locations";
	private final String actinmediaTable = "ActorsInMedia";
	private final String medialocTable = "LocationOfMedia";

	private String insertMedia = "INSERT IGNORE INTO " + mediaTable + " ("
			+ "freebase_id,name,directors,image,isTv" + ") VALUES ";

	private String insertTV = "INSERT IGNORE INTO " + tvTable + " ("
			+ "media_id,first_episode,last_episode,num_seasons,num_episodes"
			+ ") VALUES ";

	private String insertFilm = "INSERT IGNORE INTO " + filmTable + " ("
			+ "media_id,release_date" + ") VALUES ";

	private String insertActor = "INSERT IGNORE INTO " + actorsTable + " ("
			+ "freebase_id,name" + ") VALUES ";

	private String insertActorInMedia = "INSERT IGNORE INTO " + actinmediaTable
			+ " (" + "actor_id,media_id,char_name" + ") VALUES ";

	private String insertLocation = "INSERT IGNORE INTO " + locTable + " ("
			+ "lat,lng,country,city,place,upvotes,downvotes" + ") VALUES ";

	private String insertLocationOfMedia = "INSERT IGNORE INTO "
			+ medialocTable + " (" + "media_id,location_id,scene_episode"
			+ ") VALUES ";
	private String lastid = "SELECT LAST_INSERT_ID() AS id";
	private Connection connect;
	private ResultSet rs;
	private String values;
	private String query;

	public Updater() {
		connect = ConnectionManager.conn;
	}

	/* Insert */
	/**
	 * 
	 * @param lom
	 * @return true if insertion succeeded , false if LocationOfMedia already
	 *         exists
	 * @throws SQLException
	 */
	public boolean addLocationOfMedia(LocationOfMedia lom) throws SQLException {
		int media_id = lom.media_id;
		int location_id = lom.location.location_id;
		String scene = lom.scene_episode;
		rs = connect.createStatement().executeQuery(
				"SELECT * FROM LocationOfMedia WHERE media_id='" + media_id
						+ "' AND location_id='" + location_id
						+ "' AND scene_episode='" + scene + "'");
		if (!rs.next()) {
			values = "('" + media_id + "','" + location_id + "','" + scene
					+ "')";
			connect.createStatement().execute(insertLocationOfMedia + values);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param loc
	 * @return the location_id of the new Location. if Location already exists
	 *         returns his location_id
	 * @throws SQLException
	 */
	public String addLocation(Location loc) throws SQLException {
		String lat = loc.lat, lng = loc.lng;
		rs = connect.createStatement().executeQuery(
				"SELECT location_id FROM Locations WHERE lat='" + lat
						+ "' AND lng='" + lng + "'");
		if (rs.next())
			return rs.getString("location_id");
		else {
			values = "('" + lat + "','" + lng + "','" + loc.country + "','"
					+ loc.city + "','" + loc.place + "',0,0)";
			connect.createStatement().execute(insertLocation + values);
			rs = connect.createStatement().executeQuery(lastid);
			rs.next();
			return rs.getString("id");
		}
	}

	/**
	 * 
	 * @param aim
	 * @return true if insertion succeeded , false if ActorInMedia already
	 *         exists
	 * @throws SQLException
	 */
	public boolean addActorInMedia(ActorInMedia aim) throws SQLException {
		int actor_id = aim.actor_id;
		int media_id = aim.media_id;
		String character = aim.char_name;
		rs = connect.createStatement().executeQuery(
				"SELECT * FROM ActorsInMedia WHERE actor_id='" + actor_id
						+ "' AND media_id='" + media_id + "' AND char_name='"
						+ character + "'");
		if (!rs.next()) {
			values = "('" + actor_id + "','" + media_id + "','" + character
					+ "')";
			connect.createStatement().execute(insertActorInMedia + values);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param act
	 * @return the actor_id of the new Actor. if Actor already exists returns
	 *         his actor_id
	 * @throws SQLException
	 */
	public String addActor(Actor act) throws SQLException {
		int actorfreebaseid = act.freebase_id;
		rs = connect.createStatement().executeQuery(
				"SELECT actor_id FROM Actors WHERE freebase_id='"
						+ actorfreebaseid + "'");
		if (rs.next())
			return rs.getString("actor_id");
		else {
			values = "('" + actorfreebaseid + "','" + act.name + "')";
			connect.createStatement().execute(insertActor + values);
			rs = connect.createStatement().executeQuery(lastid);
			rs.next();
			return rs.getString("id");
		}

	}

	/**
	 * 
	 * @param med
	 * @param show
	 * @return true if insertion succeeded , false if Media already exists
	 * @throws SQLException
	 */
	public boolean addTV(Media med, TVShow show) throws SQLException {
		String media_id = addMedia(med, "1");
		if (media_id == null)
			return false;
		values = "('" + media_id + "','" + show.first_episode + "','"
				+ show.last_episode + "','" + show.num_seasons + "','"
				+ show.num_episodes + "')";
		connect.createStatement().execute(insertTV + values);
		return true;
	}

	/**
	 * 
	 * @param med
	 * @param film
	 * @return true if insertion succeeded , false if Media already exists
	 * @throws SQLException
	 */
	public boolean addFilm(Media med, Film film) throws SQLException {
		String media_id = addMedia(med, "1");
		if (media_id == null)
			return false;
		values = "('" + media_id + "','" + film.release_date + "')";
		connect.createStatement().execute(insertFilm + values);
		return true;
	}

	private String addMedia(Media med, String isTv) throws SQLException {
		String freebaseid = med.freebase_id;
		rs = connect.createStatement().executeQuery(
				"SELECT media_id FROM Media WHERE freebase_id='" + freebaseid
						+ "'");
		if (!rs.next()) {
			values = "('" + freebaseid + "','" + med.name + "','"
					+ med.directors + "','" + med.image + "','" + isTv + "')";
			connect.createStatement().execute(insertMedia + values);
			rs = connect.createStatement().executeQuery(lastid);
			rs.next();
			return rs.getString("id");
		}
		return null;
	}

	/* Update */
	public void updateTV(TVShow tv) throws SQLException {
		query = "UPDATE " + tvTable + " SET first_episode=" + tv.first_episode
				+ " , last_episode=" + tv.last_episode + " , num_seasons="
				+ tv.num_seasons + " , num_episodes=" + tv.num_episodes
				+ " WHERE media_id=" + tv.media_id;
		connect.createStatement().execute(query);
	}

	public void updateFilm(Film film) throws SQLException {
		query = "UPDATE " + filmTable + " SET release_date="
				+ film.release_date + " WHERE media_id=" + film.media_id;
		connect.createStatement().execute(query);
	}

	public void updateMedia(Media med) throws SQLException {
		query = "UPDATE " + mediaTable + " SET name=" + med.name
				+ " , freebase_id=" + med.freebase_id + " , directors="
				+ med.directors + " , image=" + med.image + " , isTv="
				+ med.isTV + " WHERE media_id=" + med.media_id;
		connect.createStatement().execute(query);
	}

	public void updateLocation(Location loc) throws SQLException {
		query = "UPDATE " + locTable + " SET country=" + loc.country
				+ " , city=" + loc.city + " , place=" + loc.place
				+ " , upvotes=" + loc.upvotes + " , downvotes=" + loc.downvotes
				+ " WHERE location_id=" + loc.location_id + " AND lat="
				+ loc.lat + " AND lng=" + loc.lng;
		connect.createStatement().execute(query);
	}

	public void updateActor(Actor act) throws SQLException {
		query = "UPDATE " + actorsTable + " SET name=" + act.name
				+ " , freebase_id=" + act.freebase_id + " WHERE actor_id="
				+ act.actor_id;
		connect.createStatement().execute(query);
	}

	public void updateLocationOfMedia(LocationOfMedia lom) throws SQLException {
		query = "UPDATE " + medialocTable + " SET scene_episode="
				+ lom.scene_episode + " WHERE media_id=" + lom.media_id
				+ " AND location_id=" + lom.location.location_id;
		connect.createStatement().execute(query);
	}

	public void updateActorInMedia(ActorInMedia aim) throws SQLException {
		query = "UPDATE " + actinmediaTable + " SET char_name=" + aim.char_name
				+ " WHERE media_id=" + aim.media_id + " AND actor_id="
				+ aim.actor_id;
		connect.createStatement().execute(query);
	}

	/* Delete */
	public void deleteMedia(Media med) throws SQLException {
		query = "DELETE FROM " + mediaTable + " WHERE media_id=" + med.media_id;
		connect.createStatement().execute(query);
	}

	public void deleteLocation(Location loc) throws SQLException {
		query = "DELETE FROM " + locTable + " WHERE location_id="
				+ loc.location_id;
		connect.createStatement().execute(query);
	}

	public void deleteActor(Actor act) throws SQLException {
		query = "DELETE FROM " + actorsTable + " WHERE actor_id="
				+ act.actor_id;
		connect.createStatement().execute(query);
	}

	public void deleteLocationOfMedia(LocationOfMedia lom) throws SQLException {
		query = "DELETE FROM " + medialocTable + " WHERE media_id="
				+ lom.media_id + " AND location_id=" + lom.location.location_id;
		connect.createStatement().execute(query);
	}

	public void deleteActorInMedia(ActorInMedia aim) throws SQLException {
		query = "DELETE FROM " + actinmediaTable + " WHERE media_id="
				+ aim.media_id + " AND actor_id=" + aim.actor_id;
		connect.createStatement().execute(query);
	}

}
