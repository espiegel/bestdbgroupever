package gui;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.Main;
import objects.Comment;
import objects.Film;
import objects.Location;
import objects.LocationOfMedia;
import objects.TVShow;
import objects.User;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import db.CommentRetriever;
import db.ConnectionManager;
import db.FilmRetriever;
import db.LocationByActorRetriever;
import db.LocationOfMediaRetriever;
import db.LocationRetriever;
import db.TVRetriever;
import db.UserRetriever;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.custom.CLabel;

public class MainDisplay {

	protected Shell shlTvTraveler;
	protected Display display;
	private Text txtSearch;
	private Table commentTable;
	private String currentSearch = "";
	private ArrayList<Integer> listIds = new ArrayList<Integer>();
	private MapWidget map;
	private int currentLocationId;
	private CLabel lblCurrentLocation=null;
	
	public int getCurrentLocationId() {
		return currentLocationId;
	}

	public void setCurrentLocationId(int currentLocationId) {
		this.currentLocationId = currentLocationId;
	}

	public String getCurrentSearch() {
		return currentSearch;
	}

	public void setCurrentSearch(String currentSearch) {
		this.currentSearch = currentSearch;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainDisplay window = new MainDisplay();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shlTvTraveler.open();
		shlTvTraveler.layout();
		while (!shlTvTraveler.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private URL getFullUrl(String address)
	{
		try {
			return new URL("http://img.freebase.com/api/trans/image_thumb"+address+"?maxheight=200&mode=fit&maxwidth=150");
		} catch (MalformedURLException e) {
			System.err.println("Couldn't get the url");
			return null;
		}
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlTvTraveler = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shlTvTraveler.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/tv.png"));
		shlTvTraveler.setSize(1150, 635);
		shlTvTraveler.setText("TV Traveler");
		shlTvTraveler.setLayout(new FormLayout());
		
		Menu menu = new Menu(shlTvTraveler, SWT.BAR);
		shlTvTraveler.setMenuBar(menu);
		
		MenuItem mntmAccount = new MenuItem(menu, SWT.CASCADE);
		mntmAccount.setText("Account");
		
		Menu menu_1 = new Menu(mntmAccount);
		mntmAccount.setMenu(menu_1);
		
		MenuItem mntmProfile = new MenuItem(menu_1, SWT.NONE);
		mntmProfile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Profile profile = new Profile();
				profile.open();
			}
		});
		mntmProfile.setText("Profile");
		
		MenuItem mntmLogout = new MenuItem(menu_1, SWT.NONE);
		mntmLogout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlTvTraveler.close();
				shlTvTraveler.dispose();
				
				Login login = new Login();
				login.open();
			}
		});
		mntmLogout.setText("Logout");
		
		Group grpDetails = new Group(shlTvTraveler, SWT.NONE);
		FormData fd_grpDetails = new FormData();
		fd_grpDetails.bottom = new FormAttachment(0, 230);
		fd_grpDetails.right = new FormAttachment(0, 1122);
		fd_grpDetails.top = new FormAttachment(0);
		fd_grpDetails.left = new FormAttachment(0, 576);
		grpDetails.setLayoutData(fd_grpDetails);
		grpDetails.setText("Details");
		
		
		
		final Label lblDetails1 = new Label(grpDetails, SWT.BORDER | SWT.WRAP | SWT.HORIZONTAL);
		lblDetails1.setBounds(185, 20, 335, 25);
		
		final Label lblDetails2 = new Label(grpDetails, SWT.BORDER | SWT.WRAP | SWT.HORIZONTAL);
		lblDetails2.setBounds(185, 57, 335, 25);
		
		final Label lblDetails3 = new Label(grpDetails, SWT.BORDER | SWT.WRAP | SWT.HORIZONTAL);
		lblDetails3.setBounds(185, 92, 335, 25);
		
		final Label lblDetails4 = new Label(grpDetails, SWT.BORDER | SWT.WRAP | SWT.HORIZONTAL);
		lblDetails4.setBounds(185, 127, 335, 25);
		
		final Label lblDetails5 = new Label(grpDetails, SWT.BORDER | SWT.WRAP | SWT.HORIZONTAL);
		lblDetails5.setBounds(185, 162, 335, 25);
		
		final Label lblDetails6 = new Label(grpDetails, SWT.BORDER | SWT.WRAP | SWT.HORIZONTAL);
		lblDetails6.setBounds(185, 200, 335, 25);
		
		final Label lblPic = new Label(grpDetails, SWT.NONE);
		lblPic.setBounds(10, 20, 150, 200);
		// End of details group
		

		Group grpMap = new Group(shlTvTraveler, SWT.NONE);
		FormData fd_grpMap = new FormData();
		fd_grpMap.bottom = new FormAttachment(0, 555);
		fd_grpMap.right = new FormAttachment(0, 570);
		fd_grpMap.top = new FormAttachment(0, 230);
		fd_grpMap.left = new FormAttachment(0, 10);
		grpMap.setLayoutData(fd_grpMap);
		grpMap.setText("Map");
		
		//new google map
		/*look at the documentation in the MapWidget class*/
		map = new MapWidget(grpMap, "map.html",this);
		map.init();
		map.getBrowser().setBounds(10, 24, 540, 291);
		
		
		Group grpComments = new Group(shlTvTraveler, SWT.NONE);
		grpComments.setText("Comments");
		FormData fd_grpComments = new FormData();
		fd_grpComments.top = new FormAttachment(grpMap, 0, SWT.TOP);
		fd_grpComments.left = new FormAttachment(grpDetails, 0, SWT.LEFT);		
		
		fd_grpComments.bottom = new FormAttachment(grpMap, 0, SWT.BOTTOM);
		fd_grpComments.right = new FormAttachment(100, -10);
		grpComments.setLayoutData(fd_grpComments);
		
		lblCurrentLocation = new CLabel(grpComments, SWT.BORDER | SWT.WRAP);
		lblCurrentLocation.setRightMargin(0);
		lblCurrentLocation.setLeftMargin(0);
		lblCurrentLocation.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblCurrentLocation.setAlignment(SWT.CENTER);
		lblCurrentLocation.setBounds(17, 24, 261, 29);
		lblCurrentLocation.setText("");
		
		commentTable = new Table(grpComments, SWT.BORDER | SWT.FULL_SELECTION);
		commentTable.setHeaderVisible(true);
		commentTable.setBounds(10, 59, 538, 256);
		
		TableColumn tblclmnDate = new TableColumn(commentTable, SWT.NONE);
		tblclmnDate.setWidth(89);
		tblclmnDate.setText("Date");
		
		TableColumn tblclmnUsername = new TableColumn(commentTable, SWT.NONE);
		tblclmnUsername.setWidth(64);
		tblclmnUsername.setText("User");
		
		TableColumn tblclmnUpvotes = new TableColumn(commentTable, SWT.NONE);
		tblclmnUpvotes.setWidth(69);
		tblclmnUpvotes.setText("Upvotes");
		
		TableColumn tblclmnDownvotes = new TableColumn(commentTable, SWT.NONE);
		tblclmnDownvotes.setWidth(89);
		tblclmnDownvotes.setText("Downvotes");
		
		TableColumn tblclmnComment = new TableColumn(commentTable, SWT.NONE);
		tblclmnComment.setWidth(333);
		tblclmnComment.setText("Comment");
		
		Button btnAddComment = new Button(grpComments, SWT.NONE);
		final MainDisplay mainDisplay = this;
		btnAddComment.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(lblCurrentLocation.getText().isEmpty())
					return;
				
				WriteComment wc = new WriteComment(Main.getCurrentUser(), currentLocationId, mainDisplay);
				wc.open();
			}
		});
		btnAddComment.setBounds(284, 20, 141, 37);
		btnAddComment.setText("Add Comment");
		
		Button btnUpvote = new Button(grpComments, SWT.NONE);
		btnUpvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_up_black.png"));
		btnUpvote.setBounds(442, 20, 41, 37);
		
		Button btnDownvote = new Button(grpComments, SWT.NONE);
		btnDownvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_down_black.png"));
		btnDownvote.setBounds(489, 20, 41, 37);
		
		
		
		Group grpSearch = new Group(shlTvTraveler, SWT.NONE);
		FormData fd_grpSearch = new FormData();
		fd_grpSearch.bottom = new FormAttachment(0, 230);
		fd_grpSearch.right = new FormAttachment(0, 570);
		fd_grpSearch.top = new FormAttachment(0);
		fd_grpSearch.left = new FormAttachment(0, 10);
		grpSearch.setLayoutData(fd_grpSearch);
		grpSearch.setText("Search");
		
		final Button btnSearch = new Button(grpSearch, SWT.NONE);
		
		txtSearch = new Text(grpSearch, SWT.BORDER);
		txtSearch.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent arg0) {
				if (arg0.detail==SWT.TRAVERSE_RETURN) {
					btnSearch.notifyListeners(SWT.Selection, null);
				}
			}
		});
		txtSearch.setBounds(10, 28, 421, 26);		
		
		Group group = new Group(grpSearch, SWT.NONE);
		group.setBounds(10, 60, 153, 160);
		
		final Button btnRadioTv = new Button(group, SWT.RADIO);
		btnRadioTv.setSelection(true);
		btnRadioTv.setBounds(10, 20, 111, 20);
		btnRadioTv.setText("Television");
		
		final Button btnRadioFilm = new Button(group, SWT.RADIO);
		btnRadioFilm.setBounds(10, 45, 111, 20);
		btnRadioFilm.setText("Film");
		
		final Button btnRadioLocation = new Button(group, SWT.RADIO);
		btnRadioLocation.setBounds(10, 70, 111, 20);
		btnRadioLocation.setText("Location");
		
		final Button btnRadioUsername = new Button(group, SWT.RADIO);
		btnRadioUsername.setBounds(10, 95, 111, 20);
		btnRadioUsername.setText("Username");
		
		final Button btnRadioLocByActor = new Button(group, SWT.RADIO);
		btnRadioLocByActor.setBounds(10, 120, 120, 20);
		btnRadioLocByActor.setText("Location By Actor");

		
		final List list = new List(grpSearch, SWT.BORDER | SWT.V_SCROLL);
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				clearAllLabels(lblDetails1, lblDetails2, lblDetails3,
						lblDetails4, lblDetails5, lblDetails6, lblPic);
				
				int index = list.getSelectionIndex();
				int id = -1;
			
				id = listIds.get(index);
				
				if(currentSearch.equals("TV"))
				{
					TVShow show = new TVRetriever().retrieve(id);
								
					try
					{
						if(show == null)
						{
							System.out.println("Empty resultset");
							return;
						}
						
						String address = show.image;
						
						String name = canonicalize(show.name);
						String director =canonicalize(show.directors);
						String first = canonicalize(show.first_episode);
						String last = canonicalize(show.last_episode);
						int numSeasons = show.num_seasons;
						int numEpisodes = show.num_episodes;

						if(address!=null && address.length()>1){
							final URL url = getFullUrl(address);
							
							display.asyncExec(new Runnable() {
								
								@Override
								public void run() {
									Image img;
									try {
										img = new Image(display, url.openStream());
										lblPic.setImage(img);
										
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							});
						}
						else
						{
							lblPic.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/noimage.jpg"));
						}
						lblDetails1.setText("Name: "+name);
						lblDetails2.setText("Director(s): "+director);
						lblDetails3.setText("First Episode: "+first.toString());
						lblDetails4.setText("Last Episode: "+last.toString());
						lblDetails5.setText("Number of Seasons: "+String.valueOf(numSeasons));
						lblDetails6.setText("Number of Episodes: "+String.valueOf(numEpisodes));
						
						/*java.util.List<Location> locations = new LocationRetriever().retrieve(ConnectionManager.conn.prepareStatement(
								"Select * FROM Locations, LocationOfMedia WHERE Locations.location_id = LocationOfMedia.location_id AND "+
						        "LocationOfMedia.media_id = "+id));
						// Put all location markers on the map
						map.clearAllMarkers();
						
						for(Location l : locations)
							map.addMarker(l.lat, l.lng, l.place);*/
						addLocaionMarkers(id);
						
						// TODO: Zoom Out on all of these markers ???
						
					} catch (Exception e) {
						e.printStackTrace();
					}
						
				} // End of TV if
			
			if(currentSearch.equals("Film"))
			{
				Film film = new FilmRetriever().retrieve(id);
							
				try
				{
					if(film == null)
					{
						System.out.println("Empty resultset");
						return;
					}
					
					String address = film.image;
					
					String name = canonicalize(film.name);
					String director =canonicalize(film.directors);
					String release = canonicalize(film.release_date);

					if(address!=null && address.length()>1)
					{
						final URL url = getFullUrl(address);
						
						display.asyncExec(new Runnable() {
							
							@Override
							public void run() {
								Image img;
								try {
									img = new Image(display, url.openStream());
									lblPic.setImage(img);
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});
					}
					else
					{
						lblPic.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/noimage.jpg"));
					}
					
					lblDetails1.setText("Name: "+name);
					lblDetails2.setText("Director(s): "+director);
					lblDetails3.setText("Release Date: "+release);
					
					java.util.List<Location> locations = new LocationRetriever().retrieve(ConnectionManager.conn.prepareStatement(
							"Select * FROM Locations, LocationOfMedia WHERE Locations.location_id = LocationOfMedia.location_id AND "+
					        "LocationOfMedia.media_id = "+id));
					// Put all location markers on the map
					map.clearAllMarkers();
					
					for(Location l : locations)
						map.addMarker(l.lat, l.lng, l.place);
					
					// TODO: Zoom Out on all of these markers ???
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
					
			} // End of Film If
			
			if(currentSearch.equals("Location"))
			{
				Location location = new LocationRetriever().retrieve(id);
							
				try
				{
					if(location == null)
					{
						System.out.println("Empty resultset");
						return;
					}
					
					String country = location.country;
					String city = location.city;
					String place = location.place;
					float lat = Float.parseFloat(location.lat);
					float lng = Float.parseFloat(location.lng);
					int up = location.upvotes;
					int down = location.downvotes;
					
					// locations dont have images
					lblPic.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/noimage.jpg"));

					
					lblDetails1.setText("Place: "+place);
					lblDetails2.setText("Country: "+country);
					lblDetails3.setText("City: "+city);
					lblDetails4.setText("Latitude: "+lat+", Longtitude: "+lng);
					lblDetails5.setText("Upvotes: "+up+", Downvotes: "+down);
					
					// TODO: Add this location to the map according to the lat and lng.
					map.clearAllMarkers();
					map.addMarker(location.lat, location.lng, place);
					
					loadCommentsByLocationId(id);				
					lblCurrentLocation.setText(place);
					currentLocationId = id;
				} catch (Exception ex) {
					ex.printStackTrace();
				}				

			} // End of Location If
			
			if(currentSearch.equals("Location By Actor"))  //code replication..
			{ 
				Location location = new LocationRetriever().retrieve(id); 
							
				try
				{
					if(location == null)
					{
						System.out.println("Empty resultset");
						return;
					}
					
					String country = location.country;
					String city = location.city;
					String place = location.place;
					float lat = Float.parseFloat(location.lat);
					float lng = Float.parseFloat(location.lng);
					int up = location.upvotes;
					int down = location.downvotes;
					
					// locations dont have images
					lblPic.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/noimage.jpg"));

					
					lblDetails1.setText("Place: "+place);
					lblDetails2.setText("Country: "+country);
					lblDetails3.setText("City: "+city);
					lblDetails4.setText("Latitude: "+lat+", Longtitude: "+lng);
					lblDetails5.setText("Upvotes: "+up+", Downvotes: "+down);
					
					// TODO: Add this location to the map according to the lat and lng.
					map.clearAllMarkers();
					map.addMarker(location.lat, location.lng, place);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
					
			} // End of LocationByActor If
			
			if(currentSearch.equals("User"))
			{
				User user = new UserRetriever().retrieveById(id);
				if(user == null)
				{
					System.out.println("Empty resultset");
					return;
				}		
				
				boolean show_password = false;
				if(user.equals(Main.getCurrentUser()))
					show_password = true;
				
				Profile profile = new Profile(user, show_password);
				profile.open();
					
			}
			} // End of widgetSelected

			// NOTE: this method is part of the SelectionAdapater!
			private void clearAllLabels(final Label lblDetails1,
					final Label lblDetails2, final Label lblDetails3,
					final Label lblDetails4, final Label lblDetails5,
					final Label lblDetails6, final Label lblPic) {
				// Need to clean the labels...
				lblDetails1.setText("");
				lblDetails2.setText("");
				lblDetails3.setText("");
				lblDetails4.setText("");
				lblDetails5.setText("");
				lblDetails6.setText("");
				lblPic.setImage(null);
			}
		});
		list.setBounds(185, 72, 365, 148);
		

		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String text = txtSearch.getText();
				
				list.removeAll(); // Initialize the list
				listIds.removeAll(listIds); // Initialize the id list
				
				if(btnRadioTv.getSelection())
				{
					setCurrentSearch("TV");
					TVRetriever ret = new TVRetriever();
					java.util.List<TVShow> shows = ret.searchBySearchField(text);
					
					if(shows.isEmpty())
					{
						System.out.println("Empty resultset");
						return;
					}	
					
					for (TVShow show : shows) {
						list.add(canonicalize(show.name));
						listIds.add(show.media_id);
					}
				}
				
				if(btnRadioFilm.getSelection())
				{
					setCurrentSearch("Film");
					FilmRetriever ret = new FilmRetriever();
					java.util.List<Film> films = ret.searchBySearchField(text);
					
					if(films.isEmpty())
					{
						System.out.println("Empty resultset");
						return;
					}	
					
					for (Film film : films) {
						list.add(canonicalize(film.name));
						listIds.add(film.media_id);
					}
				}
				
				if(btnRadioUsername.getSelection())
				{
					setCurrentSearch("User");
					
					UserRetriever ret = new UserRetriever();
					java.util.List<User> users_with_name = ret.searchBySearchField(text);
					
					if(users_with_name == null)
					{
						System.out.println("Empty resultset");
						return;
					}	
					
					for (User user : users_with_name) {
						list.add(canonicalize(user.getUsername()));
						listIds.add(user.getID());
					}
				}
				if(btnRadioLocation.getSelection())
				{
					setCurrentSearch("Location");
					
					LocationRetriever ret = new LocationRetriever();
					java.util.List<Location> locs = ret.searchBySearchField(text, text, text);
					
					if(locs.isEmpty())
					{
						System.out.println("Empty resultset");
						return;
					}	
					
					for (Location location : locs) {
						list.add(canonicalize(location.place));
						listIds.add(location.location_id);
					}
				}
				
				if (btnRadioLocByActor.getSelection()) {

					setCurrentSearch("Location By Actor");
					LocationByActorRetriever ret = new LocationByActorRetriever();
					java.util.List<Location> locs = ret.searchBySearchField(text);

					if (locs.isEmpty()) {
						System.out.println("Empty resultset");
						return;
					}

					for (Location location : locs) {
						list.add(canonicalize(location.place));
						listIds.add(location.location_id);
					}
				}					
							
			}
		});
		btnSearch.setBounds(441, 26, 109, 30);
		btnSearch.setText("Search");
		
	}

	public String canonicalize(String str){
		if(str == null || str.equals("null") || str.equals(""))
			return "Unknown";
		else{
			str=str.replaceAll("&#039","'");
			str=str.replaceAll("&amp;", "&");

			return str;
		}
	}

	public void loadCommentsByLocationId(int id) {
		// Now loading all the comments
		java.util.List<Comment> commentList = new CommentRetriever().searchBySearchField(String.valueOf(id));
		commentTable.removeAll();
		
		int i = 0;
		TableItem ti;
		for(Comment c : commentList)
		{
			System.out.println(c);
			ti = new TableItem(commentTable, SWT.NONE, i);
			i++;
			String user = new UserRetriever().retrieveById(c.getUser_id()).getUsername();
			int upvotes = c.getUpvotes();
			int downvotes = c.getDownvotes();
			String comment = c.getComment();
			Date date = c.getDatetime();
			ti.setText(new String[]{date.toString(), user, String.valueOf(upvotes), String.valueOf(downvotes), comment});
		}
	}
	public void loadCommentsByLocationCoord(String lat,String lng){
		java.util.List<Location> locations=null ;
		try {
			 locations = new LocationRetriever().retrieve(ConnectionManager.conn.prepareStatement(
					 "Select * FROM Locations WHERE Locations.lat = \""+lat+"\" AND "+
						"Locations.lng = \""+lng+"\""));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int location_id=locations.get(0).location_id;
		currentLocationId=location_id;
		lblCurrentLocation.setText(locations.get(0).place);
		loadCommentsByLocationId(location_id);
	}
	public String getScene(int location_id,int media_id,java.util.List<LocationOfMedia> locationsOfMedia){
		for(LocationOfMedia locationOfMedia : locationsOfMedia){
			if(locationOfMedia.location_id == location_id &&
					locationOfMedia.media_id == media_id){
				return canonicalize(locationOfMedia.scene_episode);
			}
		}
		return "no info";
	}
	public void addLocaionMarkers(int media_id) throws SQLException{
		java.util.List<Location> locations = new LocationRetriever().retrieve(ConnectionManager.conn.prepareStatement(
				"Select * FROM Locations, LocationOfMedia WHERE Locations.location_id = LocationOfMedia.location_id AND "+
		        "LocationOfMedia.media_id = "+media_id));
		
		java.util.List<LocationOfMedia> locationsOfMedia = new LocationOfMediaRetriever().retrieve(ConnectionManager.conn.prepareStatement(
				"Select * FROM Locations, LocationOfMedia WHERE Locations.location_id = LocationOfMedia.location_id AND "+
		        "LocationOfMedia.media_id = "+media_id));
		// Put all location markers on the map
		map.clearAllMarkers();
		
		for(Location l : locations)
			map.addMarker(l.lat, l.lng, l.place,getScene(l.location_id, media_id, locationsOfMedia));
	}
}
