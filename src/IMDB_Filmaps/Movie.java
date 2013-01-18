package IMDB_Filmaps;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Movie implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String poster;
	private Set<Location> locations;

	public Movie() {
		setLocations(new HashSet<Location>());
	}

	public Movie(String title, String poster) {
		setLocations(new HashSet<Location>());
		this.title = title;
		this.poster = poster;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getPoster() {
		return poster;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

	public Set<Location> getLocations() {
		return locations;
	}
	public String toString(){
		Iterator<Location> it;
		it = getLocations().iterator();
		StringBuffer sb = new StringBuffer();
		sb.append("<Movie>\r\n");
		sb.append("\t<Title>" + getTitle()+"</Title>\r\n") ;
		sb.append("\t<Poster>" + getPoster()+"/<Poster>\r\n");
		sb.append("\t<Locations>\r\n");
		while (it.hasNext())
			sb.append("\t\t"+it.next());
		sb.append("\t</Locations>\r\n");
		sb.append("</Movie>");
		return sb.toString();
	}

}
