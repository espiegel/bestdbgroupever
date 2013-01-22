package db;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import FreeBase.Uploader;
import IMDB_Filmaps.IMDBGeoCoding;
import IMDB_Filmaps.XMLParser;

import com.narphorium.freebase.services.exceptions.FreebaseServiceException;
/**
 * GUI should call one of this class methods using one of the examples
 * in the comment in the constructor.
 * @author Yoav
 *
 */
public class DataUploader {


	public DataUploader(){
		/* example of all options*/
		
		/*Connection connect = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String address = "jdbc:mysql://localhost:3305/DbMysql02";
			String user = "DbMysql02", pass = "DbMysql02";
			connect = DriverManager.getConnection(address, user, pass);
		} catch (Exception e) {

		}*/
		
		// IMDB Upload
		/*
		String IMDBListBeforeParsing = "IMDB\\IMDBLocations.list";
		String geoCodeHTML = "IMDB\\geocode.html";
		String IMDBListAfterParsing = "IMDB\\IMDBLocations.xml";
		int startNum = 8;
		int IMDBLimit = 2; // how much media to parse
		boolean IMDB_OK = false;
		try {
			IMDB_OK = IMDBUpload(connect, IMDBListBeforeParsing,
					IMDBListAfterParsing, geoCodeHTML, startNum, IMDBLimit);
		} catch (IOException e) {
			System.out.println("IOException");
		} catch (SQLException e) {
			System.out.println("SQLException");
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException");
		} catch (SAXException e) {
			System.out.println("SAXException");
		}
		if (!IMDB_OK)
			System.out.println("error parsing :" + IMDBListAfterParsing + " !");
		*/
		
		// Filmaps Upload
		/*
		int FilmapsLimit = 10;
		String FilmapsLocationsPath = "Filmaps\\FilmapsLocations.list";
		boolean Filmaps_OK = false;
		try {
			Filmaps_OK = FilmapsUpload(connect, FilmapsLocationsPath,
					FilmapsLimit);
		} catch (IOException e) {
			System.out.println("IOException");
		} catch (SQLException e) {
			System.out.println("SQLException");
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException");
		} catch (SAXException e) {
			System.out.println("SAXException");
		}
		if (!Filmaps_OK)
			System.out.println("error parsing filmaps !");
		*/
		
		// Freebase Upload
		/*
		int FreebaseLimit = 1;// will be 10*500=5000
		// TV Shows
		try {
			FreebaseUpload(connect, FreebaseLimit, true);
		} catch (IOException e) {
			System.out.println("IOException");
		} catch (SQLException e) {
			System.out.println("SQLException");
		} catch (FreebaseServiceException e) {
			System.out.println("FreebaseServiceException");
		}*/
		
		// Films
		/*try {
			FreebaseUpload(connect, FreebaseLimit, false);
		} catch (IOException e) {
			System.out.println("IOException");
		} catch (SQLException e) {
			System.out.println("SQLException");
		} catch (FreebaseServiceException e) {
			System.out.println("FreebaseServiceException");
		}*/
	}
	public static boolean IMDBUpload(Connection connect,
			String IMDBListBeforeParsing, String IMDBListAfterParsing,
			String geoCodeHTML, int startNum, int limit) throws IOException,
			SQLException, ParserConfigurationException,
			SAXException {
		IMDBGeoCoding geoCoder = new IMDBGeoCoding(connect,
				IMDBListBeforeParsing, IMDBListAfterParsing, geoCodeHTML,
				startNum, limit);
		geoCoder.initAndStart();
		
		//int geoCodingStatus = geoCoder.getStatus();
		//System.out.println("geocoded "+geoCodingStatus+" from "+limit);
		
		XMLParser parser = new XMLParser(true, IMDBListAfterParsing, 0);
		boolean ok = parser.run();
		
		//int parsingStatus = parser.getStatus();
		//System.out.println("parsed "+parsingStatus+" from "+limit);
		
		if (ok) {
			Uploader uploader = new Uploader(connect);
			int numUploaded = uploader.uploadLocations(parser.getMyMovies());
			
			//int uploadStatus = uploader.getStatus();
			//System.out.println("uploaded "+uploadStatus+" from "+parser.getMyMovies().size());
			
			System.out.println("Upload Completed !");
			System.out.println("Locations Of Media Uploaded: " + numUploaded);
			return true;
		}
		return false;
	}

	public static boolean FilmapsUpload(Connection connect,
			String FilmapsLocationsPath, int limit)
			throws ParserConfigurationException, SAXException, IOException,
			SQLException {
		XMLParser parser = new XMLParser(false, FilmapsLocationsPath,
				limit);
		boolean ok = parser.run();
		
		//int parsingStatus = parser.getStatus();
		//System.out.println("parsed "+parsingStatus+" from "+limit);
		
		if (ok) {
			Uploader uploader = new Uploader(connect);
			int numUploaded = uploader.uploadLocations(parser.getMyMovies());
			
			//int uploadStatus = uploader.getStatus();
			//System.out.println("uploaded "+uploadStatus+" from "+parser.getMyMovies().size());
			
			System.out.println("Upload Completed !");
			System.out.println("Locations Of Media Uploaded: " + numUploaded);
			return true;
		}
		return false;
	}

	public static void FreebaseUpload(Connection connect, int limit, boolean TV)
			throws IOException, SQLException, FreebaseServiceException {
		Uploader uploader = new Uploader(connect);
		if (TV)
			uploader.uploadFreeBaseTVShows(limit);
		else
			uploader.uploadFreeBaseFilms(limit);
		
		//int uploadStatus = uploader.getStatus();
		//System.out.println("uploaded "+uploadStatus+" from "+limit*500);
		
		System.out.println("Upload Completed !");
	}

}
