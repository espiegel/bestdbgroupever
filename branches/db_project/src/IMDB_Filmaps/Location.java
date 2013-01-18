package IMDB_Filmaps;
import java.io.Serializable;

public class Location implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String place;
	private String scene;
	private String coordinates;
	private String city;
	private String country;

	public Location() {
	}

	public Location(String place, String scene, String coordinates,String city,String country) {
		this.place = place;
		this.scene = scene;
		this.coordinates = coordinates;
		this.city=city;
		this.country=country;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPlace() {
		return place;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getScene() {
		return scene;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getCoordinates() {
		return coordinates;
	}
	public String getLat(){
		return getCoordinates().substring(0, getCoordinates().indexOf(","));
	}
	public String getLng(){
		return getCoordinates().substring(getCoordinates().indexOf(",") + 1);
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("<Location>\r\n");
		sb.append("\t\t\t<City>" + getCity()+"</City>\r\n");
		sb.append("\t\t\t<Country>" + getCountry()+"</Country>\r\n");
		sb.append("\t\t\t<Place>" + getPlace()+"</Place>\r\n");
		sb.append("\t\t\t<Scene>" + getScene()+"</Scene>\r\n");
		sb.append("\t\t\t<Coordinates>" + getCoordinates()+"</Coordinates>\r\n");
		sb.append("\t\t</Location>\r\n");
		return sb.toString();
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}
}
