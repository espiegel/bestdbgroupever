package objects;

public class LocationOfMedia {
		public int media_id;
		public Location location;
		public String scene_episode;
		
		public LocationOfMedia(){}

		public LocationOfMedia(int media_id, Location location,
				String scene_episode) {
			super();
			this.media_id = media_id;
			this.location = location;
			this.scene_episode = scene_episode;
		}
		
}
