package gui;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.Main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainDisplay {

	protected Shell shlTvTraveler;
	protected Display display;
	private Text txtSearch;
	private Table table;
	private String currentSearch = "";
	
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
		FormData fd_grpDetails = new FormData();
		fd_grpDetails.bottom = new FormAttachment(0, 230);
		fd_grpDetails.right = new FormAttachment(0, 1122);
		fd_grpDetails.top = new FormAttachment(0);
		fd_grpDetails.left = new FormAttachment(0, 576);
		grpDetails.setLayoutData(fd_grpDetails);
		grpDetails.setText("Details");
		
		final Label lblDetails1 = new Label(grpDetails, SWT.BORDER);
		lblDetails1.setBounds(235, 20, 285, 25);
		
		final Label lblDetails2 = new Label(grpDetails, SWT.BORDER);
		lblDetails2.setBounds(235, 57, 285, 25);
		
		final Label lblDetails3 = new Label(grpDetails, SWT.BORDER);
		lblDetails3.setBounds(235, 92, 285, 25);
		
		final Label lblDetails4 = new Label(grpDetails, SWT.BORDER);
		lblDetails4.setBounds(235, 127, 285, 25);
		
		final Label lblDetails5 = new Label(grpDetails, SWT.BORDER);
		lblDetails5.setBounds(235, 162, 285, 25);
		
		final Label lblDetails6 = new Label(grpDetails, SWT.BORDER);
		lblDetails6.setBounds(235, 200, 285, 25);
		
		final Label lblPic = new Label(grpDetails, SWT.NONE);
		lblPic.setBounds(10, 20, 200, 200);
		
		Group grpSearch = new Group(shlTvTraveler, SWT.NONE);
		FormData fd_grpSearch = new FormData();
		fd_grpSearch.bottom = new FormAttachment(0, 230);
		fd_grpSearch.right = new FormAttachment(0, 570);
		fd_grpSearch.top = new FormAttachment(0);
		fd_grpSearch.left = new FormAttachment(0, 10);
		grpSearch.setLayoutData(fd_grpSearch);
		grpSearch.setText("Search");
		
		txtSearch = new Text(grpSearch, SWT.BORDER);
		txtSearch.setBounds(10, 28, 421, 26);		
		
		Group group = new Group(grpSearch, SWT.NONE);
		group.setBounds(10, 60, 153, 160);
		
		final Button btnRadioTv = new Button(group, SWT.RADIO);
		btnRadioTv.setBounds(10, 20, 111, 20);
		btnRadioTv.setText("Television");
		
		final Button btnRadioFilm = new Button(group, SWT.RADIO);
		btnRadioFilm.setBounds(10, 55, 111, 20);
		btnRadioFilm.setText("Film");
		
		final Button btnRadioLocation = new Button(group, SWT.RADIO);
		btnRadioLocation.setBounds(10, 91, 111, 20);
		btnRadioLocation.setText("Location");
		
		final Button btnRadioUsername = new Button(group, SWT.RADIO);
		btnRadioUsername.setBounds(10, 127, 111, 20);
		btnRadioUsername.setText("Username");
		
		final List list = new List(grpSearch, SWT.BORDER | SWT.V_SCROLL);
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int index = list.getSelectionIndex();
				String sql = "";
				
				// Need to clean the labels...
				lblDetails1.setText("");
				lblDetails2.setText("");
				lblDetails3.setText("");
				lblDetails4.setText("");
				lblDetails5.setText("");
				lblDetails6.setText("");
				lblPic.setImage(null);
				
				String currentSelection = list.getItem(index);				
				if(currentSelection == null || currentSelection.isEmpty())
					return;
				
				int id = -1;
				
				String[] split = currentSelection.split("ID:");
				if(split.length != 2)
					return;
				
				id = Integer.parseInt(split[1]);
				
				if(currentSearch.equals("TV"))
				{
					ResultSet rs;
					sql = "SELECT * FROM Media, TV WHERE Media.media_id = TV.media_id AND Media.media_id = "+id;
					rs = Main.performQuery(sql);
								
					try
					{
						if(rs == null || !rs.next())
						{
							System.out.println("Empty resultset");
							return;
						}
						
						String address = rs.getString("image");
						
						String name = canonicalize(rs.getString("name"));
						String director =canonicalize(rs.getString("directors"));
						String first = canonicalize(rs.getString("first_episode"));
						String last = canonicalize(rs.getString("last_episode"));
						int numSeasons = rs.getInt("num_seasons");
						int numEpisodes = rs.getInt("num_episodes");

						if(address!=null && address.length()>1){
							final URL url = new URL("http://img.freebase.com/api/trans/image_thumb"+address+"?maxheight=200&mode=fit&maxwidth=150");
							
							display.asyncExec(new Runnable() {
								
								@Override
								public void run() {
									Image img;
									try {
										img = new Image(display, url.openStream());
										lblPic.setImage(img);
										
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
						}

						lblDetails1.setText("Name: "+name);
						lblDetails2.setText("Director(s): "+director);
						lblDetails3.setText("First Episode: "+first.toString());
						lblDetails4.setText("Last Episode: "+last.toString());
						lblDetails5.setText("Number of Seasons: "+String.valueOf(numSeasons));
						lblDetails6.setText("Number of Episodes: "+String.valueOf(numEpisodes));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
				}
			}
		});
		list.setBounds(185, 72, 365, 148);
		

		
		Button btnSearch = new Button(grpSearch, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String sql = null;
				String text = txtSearch.getText();
				ResultSet rs;
				
				list.removeAll(); // Initialize the list
				
				if(btnRadioTv.getSelection())
				{
					setCurrentSearch("TV");
					sql = "SELECT * FROM Media, TV WHERE Media.media_id = TV.media_id AND Media.name LIKE '%"+text+"%'";
					rs = Main.performQuery(sql);
					
					if(rs == null)
					{
						System.out.println("Empty resultset");
						return;
					}						
										
					try
					{					
						while(rs.next())
						{
							list.add(canonicalize(rs.getString("name"))+" | ID:"+rs.getString("media_id"));
						}
						
					}
					catch (SQLException e)
					{
						System.out.println("Sql error!");
						e.printStackTrace();
					}
				}
				
				if(btnRadioFilm.getSelection())
				{
					setCurrentSearch("Film");
					sql = "SELECT * FROM Media, Films WHERE Media.media_id = Films.media_id AND Media.name LIKE '%"+text+"%'";
					rs = Main.performQuery(sql);
					
					try
					{
						if(rs == null)
						{
							System.out.println("Empty resultset");
							return;
						}	
						
						while(rs.next())
						{
							list.add(canonicalize(rs.getString("name"))+" | ID:"+rs.getString("media_id"));
						}
						
					}
					catch (SQLException e)
					{
						System.out.println("Sql error!");
						e.printStackTrace();
					}
				}
				
				if(btnRadioUsername.getSelection())
				{
					setCurrentSearch("User");
					sql = "SELECT * FROM Users WHERE name LIKE '%"+text+"%'";
					rs = Main.performQuery(sql);
					
					try
					{
						if(rs == null)
						{
							System.out.println("Empty resultset");
							return;
						}	
						
						while(rs.next())
						{
							list.add(canonicalize(rs.getString("name"))+" | ID:"+rs.getString("user_id"));
						}
					}
					catch (SQLException e)
					{
						System.out.println("Sql error!");
						e.printStackTrace();
					}
				}
				if(btnRadioLocation.getSelection())
				{
					setCurrentSearch("Location");
					sql = "SELECT * FROM Locations WHERE country LIKE '%"+text+"%' OR city LIKE '%"+text+"%' OR street LIKE '%"+text+"%'";
					rs = Main.performQuery(sql);
					
					try
					{
						if(rs == null)
						{
							System.out.println("Empty resultset");
							return;
						}	
						
						while(rs.next())
						{
							list.add(canonicalize(rs.getString("name"))+" | ID:"+rs.getString("location_id"));
						}
					}
					catch (SQLException e)
					{
						System.out.println("Sql error!");
						e.printStackTrace();
					}
				}
					
							
			}
		});
		btnSearch.setBounds(441, 26, 109, 30);
		btnSearch.setText("Search");
		
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 64);
		fd_composite.right = new FormAttachment(0, 64);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		
		Group grpMap = new Group(shlTvTraveler, SWT.NONE);
		FormData fd_grpMap = new FormData();
		fd_grpMap.bottom = new FormAttachment(0, 555);
		fd_grpMap.right = new FormAttachment(0, 570);
		fd_grpMap.top = new FormAttachment(0, 230);
		fd_grpMap.left = new FormAttachment(0, 10);
		grpMap.setLayoutData(fd_grpMap);
		grpMap.setText("Map");
		
		//Composite compositeMap = new Composite(grpMap, SWT.NONE);
		/*MapWidget map = new MapWidget(grpMap, SWT.NONE, new Point(9500,6500), 6);
		map.setBounds(10, 24, 540, 291);*/
		
		//new google map
		/*look at the documentation in the MapWidget class*/
		MapWidget map = new MapWidget(grpMap, "map.txt");
		map.init();
		map.getBrowser().setBounds(10, 24, 540, 291);
		
		Group grpComments = new Group(shlTvTraveler, SWT.NONE);
		FormData fd_grpComments = new FormData();
		fd_grpComments.top = new FormAttachment(grpMap, 0, SWT.TOP);
		fd_grpComments.left = new FormAttachment(grpDetails, 0, SWT.LEFT);
		fd_grpComments.bottom = new FormAttachment(grpMap, 0, SWT.BOTTOM);
		fd_grpComments.right = new FormAttachment(100, -10);
		grpComments.setLayoutData(fd_grpComments);
		
		Label lblNewLabel = new Label(grpComments, SWT.CENTER);
		lblNewLabel.setLocation(10, 20);
		lblNewLabel.setSize(158, 37);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
		lblNewLabel.setText("Comments");
		
		table = new Table(grpComments, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setBounds(10, 59, 538, 256);
		
		TableColumn tblclmnUsername = new TableColumn(table, SWT.NONE);
		tblclmnUsername.setWidth(64);
		tblclmnUsername.setText("User");
		
		TableColumn tblclmnUpvotes = new TableColumn(table, SWT.NONE);
		tblclmnUpvotes.setWidth(69);
		tblclmnUpvotes.setText("Upvotes");
		
		TableColumn tblclmnDownvotes = new TableColumn(table, SWT.NONE);
		tblclmnDownvotes.setWidth(89);
		tblclmnDownvotes.setText("Downvotes");
		
		TableColumn tblclmnComment = new TableColumn(table, SWT.NONE);
		tblclmnComment.setWidth(333);
		tblclmnComment.setText("Comment");
		
		Button btnAddComment = new Button(grpComments, SWT.NONE);
		btnAddComment.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				WriteComment wc = new WriteComment();
				wc.open();
			}
		});
		btnAddComment.setBounds(284, 20, 141, 37);
		btnAddComment.setText("Add Comment");
		
		Button btnNewButton = new Button(grpComments, SWT.NONE);
		btnNewButton.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_up_black.png"));
		btnNewButton.setBounds(442, 20, 41, 37);
		
		Button btnNewButton_1 = new Button(grpComments, SWT.NONE);
		btnNewButton_1.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_down_black.png"));
		btnNewButton_1.setBounds(489, 20, 41, 37);

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
}
