package objects;


public class Location {
	@ObjectID
	public int location_id;
	public String lat;
	public String lng;
	public String country;
	public String city;
	@ObjectDisplayField
	public String place;
	public int upvotes;
	public int downvotes;
	
	public Location(){}

	public Location(int location_id, String lat, String lng, String country,
			String city, String place, int upvotes, int downvotes) {
		super();
		this.location_id = location_id;
		this.lat = lat;
		this.lng = lng;
		this.country = country;
		this.city = city;
		this.place = place;
		this.upvotes = upvotes;
		this.downvotes = downvotes;
	}

	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}
	
}
