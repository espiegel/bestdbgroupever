package objects;

public class ActorInMedia {
	public int actor_id;
	public String name;
	public int media_id;
	public String char_name;
	
	public ActorInMedia() {}

	public ActorInMedia(int actor_id, String name, int media_id,
			String char_name) {
		super();
		this.actor_id = actor_id;
		this.name = name;
		this.media_id = media_id;
		this.char_name = char_name;
	}
	
	public ActorInMedia(int actor_id, int media_id,
			String char_name) {
		super();
		this.actor_id = actor_id;
		this.media_id = media_id;
		this.char_name = char_name;
	}
}
