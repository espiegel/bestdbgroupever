package objects;

public class TVShow extends Media {
	public String first_episode;
	public String last_episode;
	public int num_seasons;
	public int num_episodes;
	
	public TVShow(){super();}

	public TVShow(String first_episode, String last_episode, int num_seasons,
			int num_episodes) {
		super();
		this.first_episode = first_episode;
		this.last_episode = last_episode;
		this.num_seasons = num_seasons;
		this.num_episodes = num_episodes;
	}
	
}
