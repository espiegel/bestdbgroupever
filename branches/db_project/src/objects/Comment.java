package objects;

import java.sql.Date;

public class Comment {
	private int id;
	private int user_id;
	private int location_id;
	private String comment;
	private int upvotes;
	private int downvotes;
	private int is_check_in;
	private String photo;
	private Date datetime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getLocation_id() {
		return location_id;
	}
	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getUpvotes() {
		return upvotes;
	}
	public void setUpvotes(int upvotes) {
		this.upvotes = upvotes;
	}
	public int getDownvotes() {
		return downvotes;
	}
	public void setDownvotes(int downvotes) {
		this.downvotes = downvotes;
	}
	public int getIs_check_in() {
		return is_check_in;
	}
	public void setIs_check_in(int is_check_in) {
		this.is_check_in = is_check_in;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
}
