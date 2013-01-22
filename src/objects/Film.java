package objects;

public class Film extends Media {
	public String release_date;
	
	public Film(){super();}

	public Film(String release_date) {
		super();
		this.release_date = release_date;
	}
	
	public Film(int media_id, String release_date) {
		super();
		this.release_date = release_date;
		
		this.setMedia_id(media_id);
	}
}
