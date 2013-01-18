package objects;

public class CommentOfUser {
	private int comment_id;
	private int user_id;
	private int vote;
	
	@Override
	public String toString() {
		return "CommentOfUser [comment_id=" + comment_id + ", user_id="
				+ user_id + ", vote=" + vote + "]";
	}
	
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getVote() {
		return vote;
	}
	public void setVote(int vote) {
		this.vote = vote;
	}
}
