package FreeBase;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import IMDB_Filmaps.Location;
import IMDB_Filmaps.Movie;

import com.narphorium.freebase.query.Query;
import com.narphorium.freebase.query.io.QueryParser;
import com.narphorium.freebase.results.Result;
import com.narphorium.freebase.services.ReadService;
import com.narphorium.freebase.services.exceptions.FreebaseServiceException;

/**
 * A class that can: upload TV shows from Freebase. upload Films From Freebase.
 * upload Films from Filmaps List<Movie> object generated by XMLParser. upload
 * TV Shows/Films from IMDB List<Movie> object generated by XMLParser.
 * 
 * @author Yoav
 * 
 */
public class Uploader {
	private Connection connect;
	private String mediaTable = "Media", tvTable = "TV", filmTable = "Films",
			actorsTable = "Actors";
	private String actinmediaTable = "ActorsInMedia";
	private String locTable = "Locations";
	private int num = 0;

	/**
	 * 
	 * @param connect
	 *            Connection to DB
	 */
	public Uploader(Connection connect) {
		this.connect = connect;
	}

	/**
	 * delete all films
	 * 
	 * @throws SQLException
	 */
	public void deleteFilms() throws SQLException {
		delete(1);
	}

	/**
	 * delete all locations
	 * 
	 * @throws SQLException
	 */
	public void deleteLocations() throws SQLException {
		delete(2);
	}

	/**
	 * delete all TV shows
	 * 
	 * @throws SQLException
	 */
	public void deleteTVShows() throws SQLException {
		delete(0);
	}

	/**
	 * upload Films from FreeBase
	 * 
	 * @throws IOException
	 * @throws SQLException
	 * @throws FreebaseServiceException
	 */
	public void uploadFreeBaseFilms(int limit) throws IOException,
			SQLException, FreebaseServiceException {
		insert(false, limit);
	}

	/**
	 * upload TV shows from FreeBase
	 * 
	 * @throws IOException
	 * @throws SQLException
	 * @throws FreebaseServiceException
	 */
	public void uploadFreeBaseTVShows(int limit) throws IOException,
			SQLException, FreebaseServiceException {
		insert(true, limit);
	}

	/**
	 * 
	 * @param myMovies
	 *            movie list generated by XMLParser
	 * @return number of locations of media uploaded
	 * @throws SQLException
	 * @throws Exception
	 */
	public int uploadLocations(List<Movie> myMovies) throws SQLException {
		int numLoc = 0;
		if (myMovies.size() == 0) {
			System.out
					.println("error: you should parse first/restore serialized movie list");
			System.exit(0);
		}
		System.out.println("Number of Medias in list :" + myMovies.size());
		ResultSet rs, rs1;
		ReadService readService = new ReadService();
		readService.setCursor(true);
		int i = 0;
		boolean error = false,locationOfMedia=false;
		String media_id;
		String locTable = "Locations", medialocTable = "LocationOfMedia";
		String insert5 = "INSERT IGNORE INTO " + locTable + " ("
				+ "lat,lng,country,city,place,upvotes,downvotes" + ") VALUES ";
		String insert6 = "INSERT IGNORE INTO " + medialocTable + " ("
				+ "media_id,location_id,scene_episode" + ") VALUES ";
		String lastid = "SELECT LAST_INSERT_ID() AS id";
		for (Movie m : myMovies) {
			i++;
			System.out.println("Uploading Media:" + i);
			String name = m.getTitle();
			if (name != null)
				name = name.replaceAll("'", "&#039");
			if (name.contains("\""))
				continue;
			// get media_id of media
			rs1 = connect.createStatement().executeQuery(
					"SELECT media_id FROM Media WHERE name='" + name + "'");
			if (rs1.next())
				media_id = rs1.getString("media_id");
			else
				continue;
			Set<Location> locations = m.getLocations();
			String location_id = "", values = "";
			for (Location l : locations) {
				String lat = l.getLat();
				String lng = l.getLng();
				String place = l.getPlace();
				if (place != null)
					place = place.replaceAll("'", "&#039");
				String scene = l.getScene();
				if (scene != null)
					scene = scene.replaceAll("'", "&#039");
				String city = l.getCity();
				if (city != null)
					city = city.replaceAll("'", "&#039");
				String country = l.getCountry();
				if (country != null)
					country = country.replaceAll("'", "&#039");
				numLoc++;
				// check if location exists
				rs1 = connect.createStatement().executeQuery(
						"SELECT location_id FROM Locations WHERE lat='" + lat
								+ "' AND lng='" + lng + "'");
				if (rs1.next())
					location_id = rs1.getString("location_id");
				else {
					// insert into Locations table
					String temp = insert5;
					values = "('" + lat + "','" + lng + "','" + country + "','"
							+ city + "','" + place + "',0,0)";
					insert5 += values;
					connect.createStatement().execute(insert5);
					insert5 = temp;
					rs = connect.createStatement().executeQuery(lastid);
					rs.next();
					location_id = rs.getString("id");
				}
				rs1 = connect.createStatement().executeQuery(
						"SELECT * FROM LocationOfMedia WHERE media_id='" + media_id
								+ "' AND location_id='" + location_id + "' AND scene_episode='" + scene + "'");
				// insert into LocationsOfMedia table
				if(!rs1.next()){
				values = "('" + media_id + "','" + location_id + "','" + scene
						+ "'),";
				insert6 += values;
				locationOfMedia=true;
				}
			}

		}
		if (!error) {
			if(locationOfMedia){
			insert6 = insert6.substring(0, insert6.length() - 1);
			connect.createStatement().execute(insert6);
			}
			return numLoc;
		}
		return 0;

	}

	/**
	 * 
	 * @param table
	 *            0 = all TV shows, 1 = all movies, 2 = all locations
	 * @throws SQLException
	 */
	private void delete(int table) throws SQLException {
		Statement stmt = null;
		stmt = connect.createStatement();
		switch (table) {
		case 0:
			stmt.addBatch("DELETE FROM " + mediaTable + " WHERE isTv = 1");
			break;
		case 1:
			stmt.addBatch("DELETE FROM " + mediaTable + " WHERE isTv = 0");
			break;
		case 2:
			stmt.addBatch("DELETE FROM " + locTable);
			break;
		}

		stmt.executeBatch();
		stmt.clearBatch();
	}

	/**
	 * insert TV shows/films form freebase
	 * 
	 * @param tv
	 *            true=TV shows, false=films
	 * @throws IOException
	 * @throws SQLException
	 * @throws FreebaseServiceException
	 */
	private void insert(boolean tv, int limit) throws IOException,
			SQLException, FreebaseServiceException {
		System.out
				.println("IMPORTANT: a full Freebase import takes at least 60 minutes and must not be interrupted until completion.\nAll of the DB's current data will be overwritten.\nPress any key to start importing...");
		System.in.read();
		int batchNum = 1;
		String insert2 = null;
		String mql_path = null;
		if (tv) {
			insert2 = "INSERT IGNORE INTO "
					+ tvTable
					+ " ("
					+ "media_id,first_episode,last_episode,num_seasons,num_episodes"
					+ ") VALUES ";
			mql_path = "MQL\\tv.mql";
		} else {
			insert2 = "INSERT IGNORE INTO " + filmTable + " ("
					+ "media_id,release_date" + ") VALUES ";
			mql_path = "MQL\\film.mql";
		}
		ReadService readService = new ReadService();
		readService.setCursor(true);
		String cursor;
		ImageUploader iu = new ImageUploader(connect);
		cursor = insertMedia(readService, tv, insert2, mql_path, iu);
		batchNum++;
		while (!cursor.equals("FALSE") && cursor != null
				&& !cursor.equals("false") && batchNum <= limit) {
			readService.setCursor(cursor);
			cursor = insertMedia(readService, tv, insert2, mql_path, iu);
			batchNum++;
		}
		
		iu.perform();
	}

	@SuppressWarnings("unchecked")
	private String insertMedia(ReadService readService, boolean tv,
			String insert2, String mql_path, ImageUploader iu) throws SQLException, IOException,
			FreebaseServiceException {

		String insert1 = "INSERT IGNORE INTO " + mediaTable + " ("
				+ "freebase_id,name,directors,image,isTv" + ") VALUES ";
		String insert3 = "INSERT IGNORE INTO " + actorsTable + " ("
				+ "freebase_id,name" + ") VALUES ";
		String insert4 = "INSERT IGNORE INTO " + actinmediaTable + " ("
				+ "actor_id,media_id,char_name" + ") VALUES ";

		String lastid = "SELECT LAST_INSERT_ID() AS id";
		String media_id = "", actor_id = "";
		String values = "", temp = "";
		String image = "", firstep = "", lastep = "", release_date = "", name = "", freebaseid = "", creatorsNames = "";
		String actor = "", character = "", actorfreebaseid = "";
		String isTv = "";
		Long nume = null, nums = null;
		boolean flag = true, tv_or_film = false, hasResults = false;
		boolean hasCharName=false,characters=false;
		ResultSet rs;
		QueryParser queryParser = new QueryParser();
		Query query;
		query = queryParser.parse(new File(mql_path));
		Statement stmt = null;
		Result result = null;
		stmt = connect.createStatement();
		com.narphorium.freebase.results.ResultSet results = readService.read(
				query, "");
		while (results.hasNext()) {
			hasResults = true;
			try {
				num++;
				System.out.println("Uploading Media:" + num);
				Iterator<Object> iter1;
				result = results.next();

				// get image
				List<Object> img = result.getCollection("img");
				iter1 = img.iterator();
				if (iter1.hasNext())
					image = ((Map<String, String>) iter1.next()).get("id");

				// get base properties
				if (tv) {
					firstep = result.getString("firstep");
					lastep = result.getString("lastep");
					nume = (Long) result.getObject("nume");
					nums = (Long) result.getObject("nums");
					isTv = "1";
				} else {
					release_date = result.getString("rdate");
					isTv = "0";
				}
				name = result.getString("nam");
				if (name != null)
					name = name.replaceAll("'", "&#039");
				freebaseid = result.getString("freebaseid");

				// get creators
				List<Object> creators = result.getCollection("creators");
				iter1 = creators.iterator();
				if (iter1.hasNext())
					creatorsNames = ((Map<String, String>) iter1.next())
							.get("name");
				while (iter1.hasNext())
					creatorsNames += ", "
							+ ((Map<String, String>) iter1.next()).get("name");
				if (creatorsNames != null)
					creatorsNames = creatorsNames.replaceAll("'", "&#039");

				// insert into Media table
				ResultSet rs1 = connect.createStatement().executeQuery(
						"SELECT media_id FROM Media WHERE freebase_id='"
								+ freebaseid + "'");
				if (!rs1.next()) {
					temp = insert1;
					values = "('" + freebaseid + "','" + name + "','"
							+ creatorsNames + "','" + image + "','" + isTv
							+ "')";
					insert1 += values;
					connect.createStatement().execute(insert1);
					insert1 = temp;
					rs = connect.createStatement().executeQuery(lastid);
					rs.next();
					media_id = rs.getString("id");

					// insert into TV/Films table
					if (tv)
						values = "('" + media_id + "','" + firstep + "','"
								+ lastep + "','" + nums + "','" + nume + "'),";
					else
						values = "('" + media_id + "','" + release_date + "'),";
					insert2 += values;
					tv_or_film = true;
					
					//Add id and image (if applicable) to the ImageUploader for later addition
					if (image!="") iu.add(Integer.parseInt(media_id), image);
				} else
					media_id = rs1.getString("media_id");

				// get cast
				List<Object> cast = result.getCollection("cast");
				iter1 = cast.iterator();
				while (iter1.hasNext()) {
					Object o = iter1.next();
					Map<String, String> Object = ((Map<String, Map<String, String>>) o)
							.get("actor");
					if (Object != null) {
						actor = Object.get("name");
						actorfreebaseid = Object.get("mid");
						if (actor != null)
							actor = actor.replaceAll("'", "&#039");
					}
					Object = ((Map<String, Map<String, String>>) o)
							.get("character");
					if (Object != null) {
						character = Object.get("name");
						if (character != null)
						{
							character = character.replaceAll("'", "&#039");
							hasCharName=true;
							characters=true;
						}
						else
							hasCharName=false;
					}
					else
						hasCharName=false;

					// insert into Actors table
					rs1 = connect.createStatement().executeQuery(
							"SELECT actor_id FROM Actors WHERE freebase_id='"
									+ actorfreebaseid + "'");
					if (!rs1.next()) {
						temp = insert3;
						values = "('" + actorfreebaseid + "','" + actor + "')";
						insert3 += values;
						connect.createStatement().execute(insert3);
						insert3 = temp;
						rs = connect.createStatement().executeQuery(lastid);
						rs.next();
						actor_id = rs.getString("id");
					} else
						actor_id = rs1.getString("actor_id");

					// insert into ActorsInMedia table
					if(hasCharName){
					rs1 = connect.createStatement().executeQuery(
								"SELECT * FROM ActorsInMedia WHERE actor_id='"
										+ actor_id + "' AND media_id='"+ media_id + "' AND char_name='"+ character + "'");	
						
					if (!rs1.next()){	
					values = "('" + actor_id + "','" + media_id + "','"
							+ character + "'),";
					insert4 += values;
					}
					}
				}
			} catch (Exception ex) {
				flag = false;
			}
		}// end while
		if (flag && hasResults) {

			String temp1;
			// TV/Films table insert
			if (tv_or_film) {
				temp1 = insert2.substring(0, insert2.length() - 1);
				stmt.addBatch(temp1);
			}

			// Actors in Media insert
			if(characters){
				temp1 = insert4.substring(0, insert4.length() - 1);
			stmt.addBatch(temp1);
			}
			if(tv_or_film || characters)
				stmt.executeBatch();

			if (readService.getCursor() instanceof Boolean)
				return "false";
			return (String) readService.getCursor();

		}

		return null;
	}
}
