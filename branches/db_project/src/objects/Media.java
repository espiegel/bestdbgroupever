package objects;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.swt.graphics.ImageData;

import db.ImageRetriever;

public class Media {
	@ObjectID("Media.media_id")
	public int media_id;
	@ObjectDisplayField
	public String name;
	public String freebase_id;
	public String directors;
	public String image;
	public int isTV;
	
	private ImageData _concImage = null; 
	
	public Media(){}
	
	public Media(int media_id, String name, String freebase_id,
			String directors, String image, int isTV) {
		super();
		this.media_id = media_id;
		this.name = name;
		this.freebase_id = freebase_id;
		this.directors = directors;
		this.image = image;
		this.isTV = isTV;
	}

	public ImageData getImage() {
		if (_concImage!=null)
			return _concImage;
		
		ImageRetriever rt = new ImageRetriever();
		
		try {
			byte[] new_image = rt.retrieve(media_id);
			InputStream image_stream;
			
			if (new_image==null) {
				if (image!=null && !image.isEmpty()) {
					URL url = new URL("http://img.freebase.com/api/trans/image_thumb"+image+"?maxheight=200&mode=fit&maxwidth=150");
					image_stream = url.openStream();
				} else {
					return null;
				}
			} else {
				image_stream = new ByteArrayInputStream(new_image);
			}
			
			return new ImageData(image_stream);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
