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
}
