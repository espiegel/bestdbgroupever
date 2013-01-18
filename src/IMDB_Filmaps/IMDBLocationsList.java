package IMDB_Filmaps;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.*;

/**
 * A class that finds all locations of a given media name. The input file should
 * be IMDB location list
 * 
 * @author Yoav
 * 
 */
public class IMDBLocationsList {
	private File f;

	private Map<String, String> locations;
	private CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder(); // or
																				// "ISO-8859-1"
	/**
	 * 
	 * @param IMDBListBeforeParsing
	 *            locations of media list Path from IMDB site
	 *            ftp://ftp.fu-berlin
	 *            .de/pub/misc/movies/database/locations.list.gz
	 */
	public IMDBLocationsList(String IMDBListBeforeParsing) {
		this.locations = new HashMap<String, String>();
		setF(new File(IMDBListBeforeParsing));
	}

	/**
	 * 
	 * @param searchString
	 *            name of the media
	 * @return true if found, false otherwise
	 * @throws FileNotFoundException
	 */
	public boolean find(String searchString) throws FileNotFoundException {
		boolean result = false;
		boolean globalresult = false;
		Scanner in = null;
		in = new Scanner(new FileReader(getF()));
		while (in.hasNextLine()) {
			String curline = in.nextLine();
			result = matchPattern(curline, searchString);
			// if found media
			if (result) {
				String[] locationANDscene = getLocation(curline);
				// if succeeded to find location,scene/episode
				if (locationANDscene != null) {
					if (locations.get(locationANDscene[0]) == null)
						locations.put(locationANDscene[0], locationANDscene[1]);
					globalresult = true;
				}
			}
			// if it was the last occurrence of the media
			// or if we have 10 different results
			if (globalresult && !result || locations.size() == 5)
				break;
		}
		return globalresult;
	}

	public File getF() {
		return f;
	}

	public Map<String, String> getLocations() {
		return locations;
	}

	public void setF(File f) {
		this.f = f;
	}

	/**
	 * 
	 * @param curline
	 *            current line in locations list
	 * @return [0] = location and [1] = scene/episode
	 */
	private String[] getLocation(String curline) {
		String[] locationANDscene = new String[2];
		// get episode from line
		int episodeLB = curline.indexOf('{');
		if (episodeLB > 0) {
			int episodeRB = curline.indexOf('}');
			String episode = curline.substring(episodeLB + 1, episodeRB);
			if (isValid(episode))
				locationANDscene[1] = episode.replaceAll("'", "&#039");
			else
				locationANDscene[1] = "no info";
		} else
			locationANDscene[1] = "no info";
		// location from line
		int firstTab = curline.indexOf('\t');
		curline = curline.substring(firstTab + 1).trim();
		int lastTab = curline.lastIndexOf('\t');
		if (lastTab > 0)
			curline = curline.substring(0, lastTab);
		if (isValid(curline))
			locationANDscene[0] = curline.replaceAll("'", "&#039");
		else
			return null;
		return locationANDscene;
	}

	private boolean isValid(String input) {
		return encoder.canEncode(input);
	}

	/**
	 * 
	 * @param curline
	 *            current line in locations list
	 * @param searchString
	 *            name of media to search
	 * @return true if found media, and false otherwise
	 */
	private boolean matchPattern(String curline, String searchString) {
		int yearLB = curline.indexOf('(');
		int name = curline.indexOf(searchString);
		if (name < yearLB && name >= 0)
			return true;
		return false;
	}

}
