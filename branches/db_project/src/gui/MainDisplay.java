package gui;


import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import main.Main;
import objects.Comment;
import objects.CommentOfUser;
import objects.Film;
import objects.Location;
import objects.LocationOfMedia;
import objects.Media;
import objects.TVShow;
import objects.User;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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

import db.CommentOfUserRetriever;
import db.CommentRetriever;
import db.ConnectionManager;
import db.FilmRetriever;
import db.LocationOfMediaRetriever;
import db.LocationRetriever;
import db.MediaByActorRetriever;
import db.TVRetriever;
import db.UserRetriever;

public class MainDisplay {

	protected Shell shlTvTraveler;
	protected Display display;
	private Text txtSearch;
	private Table commentTable;
	private String currentSearch = "";
	private ArrayList<Integer> listIds = new ArrayList<Integer>();
	private ArrayList<Integer> commentIds = new ArrayList<Integer>();
	private MapWidget map;
	private int currentLocationId;
	private CLabel lblCurrentLocation=null;
	private Comment currentComment = null;
	
	public Comment getCurrentComment() {
		return currentComment;
	}

	public void setCurrentComment(Comment currentComment) {
		this.currentComment = currentComment;
	}

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
		RowLayout rl_grpDetails = new RowLayout(SWT.HORIZONTAL);
		rl_grpDetails.wrap = false;
		grpDetails.setLayout(rl_grpDetails);
		FormData fd_grpDetails = new FormData();
		fd_grpDetails.bottom = new FormAttachment(0, 230);
		fd_grpDetails.right = new FormAttachment(0, 1122);
		fd_grpDetails.top = new FormAttachment(0);
		fd_grpDetails.left = new FormAttachment(0, 576);
		grpDetails.setLayoutData(fd_grpDetails);
		grpDetails.setText("Details");
		
		final Label lblPic_1 = new Label(grpDetails, SWT.NONE);
		lblPic_1.setLayoutData(new RowData(150, 200));
		
		SashForm sfDetails = new SashForm(grpDetails, SWT.VERTICAL);
		sfDetails.setLayoutData(new RowData(220, 200));
		
		final Composite compExtra = new Composite(grpDetails, SWT.NONE);
		compExtra.setOrientation(SWT.VERTICAL);
		compExtra.setLayout(new GridLayout(1, false));
		compExtra.setLayoutData(new RowData(150, 200));
		compExtra.setVisible(false);
		
		Label lblNewLabel = new Label(compExtra, SWT.NONE);
		lblNewLabel.setText("Actors");
		
		final List lstActors = new List(compExtra, SWT.BORDER | SWT.V_SCROLL);
		lstActors.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		//lstActors.setLayoutData(new RowData(150, 200-lblNewLabel.getBounds().height));
		
		
		
		final Label lblDetails1_1 = new Label(sfDetails, SWT.WRAP | SWT.HORIZONTAL);
		
		final Label lblDetails2_1 = new Label(sfDetails, SWT.WRAP | SWT.HORIZONTAL);
		
		final Label lblDetails3_1 = new Label(sfDetails, SWT.WRAP | SWT.HORIZONTAL);
		
		final Label lblDetails4_1 = new Label(sfDetails, SWT.WRAP | SWT.HORIZONTAL);
		
		final Label lblDetails5_1 = new Label(sfDetails, SWT.WRAP | SWT.HORIZONTAL);
		
		final Label lblDetails6_1 = new Label(sfDetails, SWT.WRAP | SWT.HORIZONTAL);
		sfDetails.setWeights(new int[] {1, 1, 1, 1, 1, 1});
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
		
		final Button btnUpvote = new Button(grpComments, SWT.NONE);
		btnUpvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_up_black.png"));
		btnUpvote.setBounds(442, 20, 41, 37);
		
		final Button btnDownvote = new Button(grpComments, SWT.NONE);
		btnDownvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_down_black.png"));
		btnDownvote.setBounds(489, 20, 41, 37);
		
		// Take care of upvotes
		btnUpvote.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				// Do nothing if no comment selected
				if(currentComment == null)
					return;
				
				Statement stmt;
				
				// Get the current vote. -1 for downvote. 0 for nothing. +1 for upvote.
				int vote = checkUserVote();
				
				// User has already upvoted. undo the upvote
				if(vote == 1)
				{
					btnUpvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_up_black.png"));
					
					try {
						stmt = ConnectionManager.conn.createStatement();
						
						// Update these tables with new upvote
						stmt.executeUpdate("UPDATE CommentOfUser SET vote=0 WHERE comment_id="+currentComment.getId());
						
						stmt.executeUpdate("UPDATE Comments SET upvotes=upvotes-1 WHERE comment_id="+currentComment.getId());

						stmt.executeUpdate("UPDATE Users SET upvotes=upvotes-1 WHERE user_id="+currentComment.getUser_id());
						
						stmt.executeUpdate("UPDATE Locations SET upvotes=upvotes-1 WHERE location_id="+currentComment.getLocation_id());
						
						stmt.close();
						
						// Reload comment table
						loadCommentsByLocationId(currentLocationId);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				// Perform an upvote
				// Add an upvote to the Comments table; add an upvote to the user who wrote the comment; add an upvote to the location itself
				// also add an upvote to the CommentOfUser table 
				if(vote == 0)
				{
					btnUpvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_up.png"));
								
					try {
						stmt = ConnectionManager.conn.createStatement();
						
						// Update these tables with new upvote
						stmt.executeUpdate("UPDATE CommentOfUser SET vote=1 WHERE comment_id="+currentComment.getId());
						
						stmt.executeUpdate("UPDATE Comments SET upvotes=upvotes+1 WHERE comment_id="+currentComment.getId());

						stmt.executeUpdate("UPDATE Users SET upvotes=upvotes+1 WHERE user_id="+currentComment.getUser_id());
						
						stmt.executeUpdate("UPDATE Locations SET upvotes=upvotes+1 WHERE location_id="+currentComment.getLocation_id());
						
						stmt.close();
						
						// Reload comment table
						loadCommentsByLocationId(currentLocationId);
					} catch (SQLException e) {
						e.printStackTrace();
					}					
				}
				
				// This was previously downvoted. change now to upvote
				if(vote == -1)
				{
					btnUpvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_up.png"));
					btnDownvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_down_black.png"));
					
					try {
						stmt = ConnectionManager.conn.createStatement();
						
						// Update these tables with new upvote
						stmt.executeUpdate("UPDATE CommentOfUser SET vote=1 WHERE comment_id="+currentComment.getId());
						
						stmt.executeUpdate("UPDATE Comments SET upvotes=upvotes+1, downvotes=downvotes-1 WHERE comment_id="+currentComment.getId());

						stmt.executeUpdate("UPDATE Users SET upvotes=upvotes+1, downvotes=downvotes-1 WHERE user_id="+currentComment.getUser_id());
						
						stmt.executeUpdate("UPDATE Locations SET upvotes=upvotes+1, downvotes=downvotes-1 WHERE location_id="+currentComment.getLocation_id());
						
						stmt.close();
						
						// Reload comment table
						loadCommentsByLocationId(currentLocationId);
					} catch (SQLException e) {
						e.printStackTrace();
					}					
				}
				
			}
		});

		// Take care of downvotes
		btnDownvote.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				// Do nothing if no comment selected
				if(currentComment == null)
					return;
				
				Statement stmt;
				
				// Get the current vote. -1 for downvote. 0 for nothing. +1 for upvote.
				int vote = checkUserVote();
				
				// User has already downvoted. undo the downvote
				if(vote == -1)
				{
					btnDownvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_down_black.png"));
					
					try {
						stmt = ConnectionManager.conn.createStatement();
						
						// Update these tables with new downvote
						stmt.executeUpdate("UPDATE CommentOfUser SET vote=0 WHERE comment_id="+currentComment.getId());
						
						stmt.executeUpdate("UPDATE Comments SET downvotes=downvotes-1 WHERE comment_id="+currentComment.getId());

						stmt.executeUpdate("UPDATE Users SET downvotes=downvotes-1 WHERE user_id="+currentComment.getUser_id());
						
						stmt.executeUpdate("UPDATE Locations SET downvotes=downvotes-1 WHERE location_id="+currentComment.getLocation_id());
						
						stmt.close();
						
						// Reload comment table
						loadCommentsByLocationId(currentLocationId);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				// Perform a downvote
				// Add a downvote to the Comments table; add a downvote to the user who wrote the comment; add a downvote to the location itself
				// also add a downvote to the CommentOfUser table 
				if(vote == 0)
				{
					btnDownvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_down.png"));
								
					try {
						stmt = ConnectionManager.conn.createStatement();
						
						// Update these tables with new downvote
						stmt.executeUpdate("UPDATE CommentOfUser SET vote=-1 WHERE comment_id="+currentComment.getId());
						
						stmt.executeUpdate("UPDATE Comments SET downvotes=downvotes+1 WHERE comment_id="+currentComment.getId());

						stmt.executeUpdate("UPDATE Users SET downvotes=downvotes+1 WHERE user_id="+currentComment.getUser_id());
						
						stmt.executeUpdate("UPDATE Locations SET downvotes=downvotes+1 WHERE location_id="+currentComment.getLocation_id());
						
						stmt.close();
						
						// Reload comment table
						loadCommentsByLocationId(currentLocationId);
					} catch (SQLException e) {
						e.printStackTrace();
					}					
				}
				
				// This was previously upvoted. change now to downvote
				if(vote == 1)
				{
					btnUpvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_up_black.png"));
					btnDownvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_down.png"));
					
					try {
						stmt = ConnectionManager.conn.createStatement();
						
						// Update these tables with new downvote
						stmt.executeUpdate("UPDATE CommentOfUser SET vote=-1 WHERE comment_id="+currentComment.getId());
						
						stmt.executeUpdate("UPDATE Comments SET upvotes=upvotes-1, downvotes=downvotes+1 WHERE comment_id="+currentComment.getId());

						stmt.executeUpdate("UPDATE Users SET upvotes=upvotes-1, downvotes=downvotes+1 WHERE user_id="+currentComment.getUser_id());
						
						stmt.executeUpdate("UPDATE Locations SET upvotes=upvotes-1, downvotes=downvotes+1 WHERE location_id="+currentComment.getLocation_id());
						
						stmt.close();
						
						// Reload comment table
						loadCommentsByLocationId(currentLocationId);
					} catch (SQLException e) {
						e.printStackTrace();
					}					
				}
				
			}
		});
		
		
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
		
		final Button btnRadioMediaByActor = new Button(group, SWT.RADIO);
		btnRadioMediaByActor.setBounds(10, 120, 120, 20);
		btnRadioMediaByActor.setText("Media By Actor");

		
		final List list = new List(grpSearch, SWT.BORDER | SWT.V_SCROLL);
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				clearAllLabels(lblDetails1_1, lblDetails2_1, lblDetails3_1,
						lblDetails4_1, lblDetails5_1, lblDetails6_1, lblPic_1);
				
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
						
						String name = canonicalize(show.name);
						String director =canonicalize(show.directors);
						String first = canonicalize(show.first_episode);
						String last = canonicalize(show.last_episode);
						int numSeasons = show.num_seasons;
						int numEpisodes = show.num_episodes;

						setPictureLabel(show, lblPic_1);

						lblDetails1_1.setText("Name: "+name);
						lblDetails2_1.setText("Director(s): "+director);
						lblDetails3_1.setText("First Episode: "+first.toString());
						lblDetails4_1.setText("Last Episode: "+last.toString());
						lblDetails5_1.setText("Number of Seasons: "+String.valueOf(numSeasons));
						lblDetails6_1.setText("Number of Episodes: "+String.valueOf(numEpisodes));
						
						//Make actors column visible:
						compExtra.setVisible(true);
						
						addLocationMarkers(id);
						
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
					
					String name = canonicalize(film.name);
					String director =canonicalize(film.directors);
					String release = canonicalize(film.release_date);

					setPictureLabel(film, lblPic_1);
					
					lblDetails1_1.setText("Name: "+name);
					lblDetails2_1.setText("Director(s): "+director);
					lblDetails3_1.setText("Release Date: "+release);
					
					//Make actors column visible:
					compExtra.setVisible(true);
					
					addLocationMarkers(id);
					
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
					lblPic_1.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/noimage.jpg"));

					
					lblDetails1_1.setText("Place: "+place);
					lblDetails2_1.setText("Country: "+country);
					lblDetails3_1.setText("City: "+city);
					lblDetails4_1.setText("Latitude: "+lat+", Longtitude: "+lng);
					lblDetails5_1.setText("Upvotes: "+up+", Downvotes: "+down);
					
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
			
			if (currentSearch.equals("Media By Actor")) {
				System.out.println("hi");
				Media media = new MediaByActorRetriever().retrieve(id);

				try {
					if (media == null) {
						System.out.println("Empty resultset");
						return;
					}
					boolean isTV = media.isTV == 1;
					TVShow tvMedia = null;
					Film filmMedia = null;
					
					if (isTV)
						tvMedia = new TVRetriever().retrieve(id);
					else
						filmMedia = new FilmRetriever().retrieve(id);
					
					String name = canonicalize(media.name);
					String director = canonicalize(media.directors);

					String first = null;
					String last = null;
					int numSeasons = -1;
					int numEpisodes = -1;
					String release = null;

					if (isTV) {
						first = canonicalize(tvMedia.first_episode);
						last = canonicalize(tvMedia.last_episode);
						numSeasons = tvMedia.num_seasons;
						numEpisodes = tvMedia.num_episodes;
					} else
						release = canonicalize(filmMedia.release_date);

					setPictureLabel(media, lblPic_1);

					lblDetails1_1.setText("Name: " + name);
					lblDetails2_1.setText("Director(s): " + director);

					if (isTV) {
						lblDetails3_1.setText("First Episode: "
								+ first.toString());
						lblDetails4_1.setText("Last Episode: "
								+ last.toString());
						lblDetails5_1.setText("Number of Seasons: "
								+ String.valueOf(numSeasons));
						lblDetails6_1.setText("Number of Episodes: "
								+ String.valueOf(numEpisodes));
					} else
						lblDetails3_1.setText("Release Date: " + release);

					java.util.List<Location> locations = new LocationRetriever().retrieve(ConnectionManager.conn
							.prepareStatement("Select * FROM Locations, LocationOfMedia WHERE Locations.location_id = LocationOfMedia.location_id AND "
									+ "LocationOfMedia.media_id = " + id));
					// Put all location markers on the map
					map.clearAllMarkers();

					for (Location l : locations)
						map.addMarker(l.lat, l.lng, l.place);
					
					//Make actors column visible:
					compExtra.setVisible(true);

					// TODO: Zoom Out on all of these markers ???

				} catch (Exception e) {
					e.printStackTrace();
				}

			} // End of MediaByActor If
			
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
				compExtra.setVisible(false);
				lstActors.removeAll();
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
				
				if (btnRadioMediaByActor.getSelection()) {

					setCurrentSearch("Media By Actor");
					MediaByActorRetriever ret = new MediaByActorRetriever();
					java.util.List<Media> medias = ret.searchBySearchField(text);

					if (medias.isEmpty()) {
						System.out.println("Empty resultset");
						return;
					}

					for (Media media : medias) {
						list.add(canonicalize(media.name));
						listIds.add(media.media_id);
					}
				}					
							
			}
		});
		btnSearch.setBounds(441, 26, 109, 30);
		btnSearch.setText("Search");
		
		// All listeners should either be at the end or in a different file...
		commentTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int index = commentTable.getSelectionIndex();
				
				int id = -1;
				id = commentIds.get(index);
				setCurrentComment(new CommentRetriever().retrieveFirst("comment_id="+id));				
				
				// Set the button images accordingly...
				btnDownvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_down"+(checkUserVote()==-1?"":"_black")+".png"));
				btnUpvote.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_up"+(checkUserVote()==1?"":"_black")+".png"));

			}
		});
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

	private void clearCommentTable() {
		// Clear the Comment Table
		commentTable.removeAll();
		commentIds.removeAll(commentIds);
		currentComment = null;
	}
	
	public void loadCommentsByLocationId(int id) {
		// Now loading all the comments
		java.util.List<Comment> commentList = new CommentRetriever().searchBySearchField(String.valueOf(id));
		
		// Clear the comment table
		clearCommentTable();
		
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
			commentIds.add(c.getId());
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
	public void addLocationMarkers(int media_id) throws SQLException{
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

	/**
	 * 
	 * @return -1 for downvote. 0 for no votes. +1 for upvote.
	 */
	private int checkUserVote() {
		// Check if the user hasn't already upvoted the current selected comment
		CommentOfUser commentOfUser = new CommentOfUserRetriever().retrieveFirst("comment_id="+currentComment.getId()+
				" AND user_id="+Main.getCurrentUser().getID());
		
		// Add a new entry to CommentOfUser Table
		if(commentOfUser == null)
		{
			// TODO: remove this println later
			System.out.println("Created new row in CommentOfUser table");
			
			try {
			Statement stmt;
			stmt = ConnectionManager.conn.createStatement();
						
			stmt.executeUpdate("INSERT INTO CommentOfUser (comment_id, user_id, vote) "+
							   "VALUES ("+currentComment.getId()+", "+Main.getCurrentUser().getID()+", 0)");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return 0;
		}
		
		// This means he has already upvoted
		return commentOfUser.getVote();
	}
	
	private void setPictureLabel(final Media media, final Label lblPic) {
		if (media.image==null || media.image.isEmpty()) {
			lblPic.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/noimage.jpg"));
		} else {
			display.asyncExec(new Runnable() {
				
				@Override
				public void run() {
					try {
						ImageData image = media.getImage();
						
						if (image==null) {
							//lblPic.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/noimage.jpg"));
						} else {
							lblPic.setImage(new Image(display, image));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
