package objects;

public class Actor {
	public int actor_id;
	public int freebase_id;
	public String name;
	
	public Actor(){}
	
	public Actor(int actor_id, int freebase_id, String name) {
		super();
		this.actor_id = actor_id;
		this.freebase_id = freebase_id;
		this.name = name;
	}

	public void setActor_id(int actor_id) {
		this.actor_id = actor_id;
	}

}
