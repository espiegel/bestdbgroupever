package gui;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.widgets.Composite;

public class MapWidget {
	private String lat;
	private String lng;
	private String filePath;
	private Composite parent;
	private Browser browser;
	private String map;
	private boolean loaded;
	private final gui.MainDisplay display;

	/**
	 * 
	 * @param parent
	 *            Composite object - will be the parent of the map widget
	 * @param fileName
	 *            map html file path
	 */
	public MapWidget(Composite parent, String filePath,gui.MainDisplay display) {
		this.parent = parent;
		this.filePath = filePath;
		this.display=display;
	}

	public void init() {
		try {
			browser = new Browser(parent, SWT.NONE);
		} catch (SWTError e) {
			System.err.println("Couldn't instantiate browser");
		}
		loaded = false;
		map = getTextFile(filePath);
		browser.setText(map);
		browser.addProgressListener(new ProgressListener() {
			public void completed(ProgressEvent arg0) {
				System.out.println("Map loaded!");
				loaded = true;
			}

			public void changed(ProgressEvent arg0) {
			}
		});
		browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent arg0) {
				String[] title = arg0.title.split(",");
				if(title[0].equals("clicked"))
				{
					setLat(title[1]);
					setLng(title[2]);
					display.loadCommentsByLocationCoord(getLat(), getLng());
				}
			}
		});
		
	}
	/**
	 * Should only be called after map loaded !
	 * 
	 * @param lat
	 *            Latitude
	 * @param lng
	 *            Longitude
	 * @param info
	 *            information of the marker
	 * @return true if the operation was successful and false
	 *         otherwise
	 */
	public boolean addMarker(String lat, String lng, String info) {
		if (isLoaded()) {
			String tempinfo="Location: "+info;
			String[] params = { lat, lng, tempinfo };

			return browserExecute("newMarker", params);
		}
		return false;
	}

	public boolean addMarker(String lat, String lng, String info,String scene) {
		String tempinfo=info+"<BR/>Scene/Episode: "+scene;
		return addMarker(lat, lng,tempinfo);
	}
	
	/**
	 * Should only be called after map loaded !
	 * 
	 * @param lat
	 *            Latitude
	 * @param lng
	 *            Longitude
	 * @return true if the operation was successful and false
	 *         otherwise
	 */
	public boolean setCenter(String lat, String lng) {
		if (isLoaded()) {
			this.lat = lat;
			this.lng = lng;
			String[] params = { lat, lng };
			return browserExecute("setCenter", params);
		}
		return false;
	}
	public boolean setZoom(int zoom){
		if (isLoaded()) {
			String[] params = { Integer.toString(zoom)};
			return browserExecute("setCenter", params);
		}
		return false;
	}
	public boolean clearAllMarkers(){
		if (isLoaded()) {
		String[] temp={};
		return browserExecute("clearOverlays",temp);
		}
		return false;
	}
	private boolean browserExecute(String funcName, String[] params) {
		String paramsStr = "";
		int length = params.length;
		if (length >= 1)
			paramsStr = "\"" + params[0] + "\"";
		for (int i = 1; i < params.length; i++)
			paramsStr += ",\"" + params[i] + "\"";
		String command = funcName + "(" + paramsStr + ");";
		return browser.execute(command);
	}

	private static String getTextFile(String file) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Cannot load map file!");
		}
		String content = contentBuilder.toString();
		return content;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLat() {
		return lat;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLng() {
		return lng;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public Browser getBrowser() {
		return browser;
	}

	public void setFileName(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return filePath;
	}

	public void setParent(Composite parent) {
		this.parent = parent;
	}

	public Composite getParent() {
		return parent;
	}

	public boolean isLoaded() {
		return loaded;
	}
}
