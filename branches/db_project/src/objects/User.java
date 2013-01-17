package objects;

public class User {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + badges;
		result = prime * result + downvotes;
		result = prime * result + id;
		result = prime * result + (isAdmin ? 1231 : 1237);
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + upvotes;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (badges != other.badges)
			return false;
		if (downvotes != other.downvotes)
			return false;
		if (id != other.id)
			return false;
		if (isAdmin != other.isAdmin)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (upvotes != other.upvotes)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	private String username;
	private String password;
	private int upvotes;
	private int downvotes;
	private int badges;
	private int id;
	private boolean isAdmin;
	
	public User()
	{
		username = "";
		password = "";
		upvotes = 0;
		downvotes = 0;
		badges = 0;
		isAdmin = false;
		id = -1;
	}
	
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
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return this.id;
	}
}
