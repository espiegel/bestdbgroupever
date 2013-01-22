package gui;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

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
	private String userLat;
	private String userLng;
	private String userAddress;
	private String filePath;
	private Composite parent;
	private Browser browser;
	private String map;
	private boolean loaded;
	private final gui.MainDisplay display;
	private final gui.NewLocationWindow locWindow;

	/**
	 * 
	 * @param parent
	 *            Composite object - will be the parent of the map widget
	 * @param fileName
	 *            map html file path
	 */
	public MapWidget(Composite parent, String filePath,gui.MainDisplay display,gui.NewLocationWindow locWindow) {
		this.parent = parent;
		this.filePath = filePath;
		this.display=display;
		this.locWindow=locWindow;
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
				String[] title = arg0.title.split("#");
				if(title[0].equals("clicked")) // user clicked a marker
				{
					setLat(title[1]);
					setLng(title[2]);
					if (display!=null) display.loadCommentsByLocationCoord(getLat(), getLng());
				}
				if(title[0].equals("choose")) //user made a new marker
				{
					setUserLat(title[1]);
					setUserLng(title[2]);
					setUserAddress(title[3]);
					try {
						locWindow.addLocation(getUserLat(), getUserLng(), getUserAddress());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(title[0].equals("address")) //geo code of address completed
				{
					if(!title[1].equals("undefined")){
						setUserLat(title[1]);
						setUserLng(title[2]);
						setUserAddress(title[3]);
						addMarker(getUserLat(), getUserLng(), getUserAddress());
					}
					else
					{
						setUserLat("undefined");
						setUserLng("undefined");
						setUserAddress("undefined");
					}
					try {
						locWindow.addLocation(getUserLat(), getUserLng(), getUserAddress());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
			return browserExecute("setZoom", params);
		}
		return false;
	}
	/**
	 * starts listen to user clicking on map
	 * add new marker to map on user click
	 * to get info about the click see TitleListener
	 * @return true if the operation was successful and false
	 *         otherwise
	 */
	public boolean startListen(){
		System.out.println("lol");
		if (isLoaded()) {
			String[] params = {};
			return browserExecute("startListen", params);
		}
		return false;
	}

	public boolean codeAddress(String address){
		if (isLoaded()) {
			String[] params = {address};
			return browserExecute("codeAddress", params);
		}
		return false;
	}
	
	public boolean clearAllMarkers(){
		if (isLoaded()) {
		String[] temp={};
		return browserExecute("clearOverlays",temp) ;
		}
		return false;
	}
	private boolean browserExecute(String funcName, String[] params) {
		return browser.execute(buildCommand(funcName, params));
	}

	/*private Object browserEvaluate(String funcName, String[] params) {
		return browser.evaluate("return "+buildCommand(funcName, params));
	}*/
	
	private String buildCommand(String funcName, String[] params){
		String paramsStr = "";
		int length = params.length;
		if (length >= 1)
			paramsStr = "\"" + params[0] + "\"";
		for (int i = 1; i < params.length; i++)
			paramsStr += ",\"" + params[i] + "\"";
		String command = funcName + "(" + paramsStr + ");";
		return command;
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

	public void setUserLat(String userLat) {
		this.userLat = userLat;
	}

	public String getUserLat() {
		return userLat;
	}

	public void setUserLng(String userLng) {
		this.userLng = userLng;
	}

	public String getUserLng() {
		return userLng;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserAddress() {
		return userAddress;
	}
}
