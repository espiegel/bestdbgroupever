package objects;

public class User {

	private String username;
	private String password;
	private int upvotes;
	private int downvotes;
	private int badges;
	private boolean isAdmin;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public int getBadges() {
		return badges;
	}
	public void setBadges(int badges) {
		this.badges = badges;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
