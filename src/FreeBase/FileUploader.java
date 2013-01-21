package FreeBase;

import java.io.*;
import java.sql.*;
import java.util.*;
/**
 * upload Media - TV/Films including actors from Freebase TSV Files
 * Warning !!! - freebase_id in these files are different from the 
 * freebase_id which come from Uploader
 * @author Yoav
 *
 */
public class FileUploader {
	private String mediaTable = "Media", tvTable = "TV", filmTable = "Films",
	actorsTable = "Actors";
	private String actinmediaTable = "ActorsInMedia";	
	private Connection connect;
	private Scanner media;
	private File cast;
	private boolean tv;
	/**
	 * 
	 * @param connect a connection to DB
	 * @param media the media file - film.tsv/tv_program.tsv
	 * @param cast the cast file - performance.tsv (Films) /regular_tv_appearance.tsv (TV)
	 * @param tv 1=upload tv shows , 0=upload films
	 * @throws FileNotFoundException
	 */
	public FileUploader(Connection connect ,File media,File cast,boolean tv) throws FileNotFoundException{
		this.connect=connect;
		this.media=new Scanner(new FileReader(media));
		this.cast=cast;
		this.tv=tv;	
	}
	
	/**
	 * start to upload
	 * @param limit the number of media you want to upload
	 * @throws FileNotFoundException
	 * @throws SQLException
	 */
	public void upload(int limit) throws FileNotFoundException, SQLException{
		int num=1;
		media.nextLine();
		while(media.hasNext() && num<=limit){
			String name="",freebase_id="",program_creator="",firstep="",lastep="";
			String nume="",nums="";
			String cast="";
			String release_date="",directed_by="";
			String curMedia = media.nextLine();
			curMedia=curMedia.replaceAll("'", "&#039");
			String[] fields = curMedia.split("\t", -1);
			for (int i = 0; i < fields.length; ++i) {
			    if ("".equals(fields[i])) fields[i] = null;
			}	
			name=fields[0];
			freebase_id=fields[1];
			System.out.println(name+","+freebase_id);
			if(tv){
				program_creator=fields[2];
				firstep=fields[3];
				lastep=fields[4];
				nume=fields[5];
				nums=fields[19];
				cast=fields[13];
				
				//System.out.println(program_creator+","+firstep+","+lastep
				//		+","+nume+","+nums+","+cast);
				
			}
			else
			{
				release_date=fields[2];
				directed_by=fields[3];
				cast=fields[13];
				//System.out.println(release_date+","+directed_by+","+cast);
			}
			String media_id;
			//upload to Media
			if(tv){
				//upload to TV
				media_id=uploadMedia(freebase_id, name, firstep, lastep, nume, nums, program_creator, "");
			}
			else{
				//upload to Film
				media_id=uploadMedia(freebase_id, name, "", "", "", "", directed_by, release_date);
			}
			//System.out.println("Cast:");
			if(cast!=null)
				uploadCast(cast,media_id);
			num++;
		}
	}
	/*
	 * uploads to Actors and ActorsInMedia tables
	 */
	private void uploadCast(String castList,String media_id) throws FileNotFoundException, SQLException{
		String insert3 = "INSERT IGNORE INTO " + actorsTable + " ("
		+ "freebase_id,name" + ") VALUES ";
		String insert4 = "INSERT IGNORE INTO " + actinmediaTable + " ("
		+ "actor_id,media_id,char_name" + ") VALUES ";
		String lastid = "SELECT LAST_INSERT_ID() AS id";
		Scanner castScanner = new Scanner(new FileReader(cast));
		String[] castIDs = castList.split(",");	
		String temp,values,actor_id;
		boolean characters=false;
		ResultSet rs,rs1;
		Statement stmt = null;
		stmt = connect.createStatement();
		castScanner.nextLine();
		while(castScanner.hasNext()){
			String curActor = castScanner.nextLine();
			curActor=curActor.replaceAll("'", "&#039");
			String[] fields = curActor.split("\t", -1);
			for (int i = 0; i < fields.length; ++i) {
			    if ("".equals(fields[i])) fields[i] = null;
			}
			String actorfreebaseid="",name="",character="";
			actorfreebaseid=fields[1];
			name=fields[2];
			if(tv)
				character=fields[3];
			else
				character=fields[5];
			if(Arrays.asList(castIDs).contains(actorfreebaseid)){
				//System.out.println(name+","+actorfreebaseid+","+character);
				// insert into Actors table
				rs1 = connect.createStatement().executeQuery(
						"SELECT actor_id FROM Actors WHERE freebase_id='"
								+ actorfreebaseid + "'");
				if (!rs1.next()) {
					temp = insert3;
					values = "('" + actorfreebaseid + "','" + name + "')";
					insert3 += values;
					connect.createStatement().execute(insert3);
					insert3 = temp;
					rs = connect.createStatement().executeQuery(lastid);
					rs.next();
					actor_id = rs.getString("id");
				} else
					actor_id = rs1.getString("actor_id");
				
				// insert into ActorsInMedia table
					rs1 = connect.createStatement().executeQuery(
							"SELECT * FROM ActorsInMedia WHERE actor_id='"
									+ actor_id + "' AND media_id='"+ media_id + "'");	
					
				if (!rs1.next()){	
				values = "('" + actor_id + "','" + media_id + "','"
						+ character + "'),";
				insert4 += values;
				characters=true;
				}
				
			}
		}
		
		if(characters){
			String temp1 = insert4.substring(0, insert4.length() - 1);
			stmt.addBatch(temp1);
			stmt.executeBatch();
		}
	}
	/*
	 * uploads to Media and TV/Films table
	 */
	private String uploadMedia(String freebaseid,String name,String firstep,String lastep,String nume,String nums,String creatorsNames,String release_date) throws SQLException{
		String insert1 = "INSERT IGNORE INTO " + mediaTable + " ("
		+ "freebase_id,name,directors,isTv" + ") VALUES ";
		String insert2;
		String lastid = "SELECT LAST_INSERT_ID() AS id";
		String isTv,temp,values;
		String media_id;
		ResultSet rs,rs1;
		Statement stmt = null;
		stmt = connect.createStatement();
		if(tv){
		insert2 = "INSERT IGNORE INTO "
			+ tvTable
			+ " ("
			+ "media_id,first_episode,last_episode,num_seasons,num_episodes"
			+ ") VALUES ";
		isTv="1";
		}
		else{
			insert2 = "INSERT IGNORE INTO " + filmTable + " ("
			+ "media_id,release_date" + ") VALUES ";
			isTv="0";
		}
		rs1 = connect.createStatement().executeQuery(
				"SELECT media_id FROM Media WHERE freebase_id='"
						+ freebaseid + "'");
		if (!rs1.next()) {
			temp = insert1;
			values = "('" + freebaseid + "','" + name + "','"
					+ creatorsNames +"','" + isTv
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
						+ lastep + "','" + nums + "','" + nume + "')";
			else
				values = "('" + media_id + "','" + release_date + "')";
			insert2 += values;
			stmt.addBatch(insert2);
			stmt.executeBatch();
		} else
			media_id = rs1.getString("media_id");
		return media_id;
	}
}
