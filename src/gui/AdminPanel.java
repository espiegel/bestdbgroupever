package gui;

import objects.Actor;
import objects.ActorInMedia;
import objects.Film;
import objects.Location;
import objects.LocationOfMedia;
import objects.Media;
import objects.TVShow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import db.Updater;

public class AdminPanel {

	private Media currentMedia = null;
	private Location currentLocation = null;
	
	protected Shell shellAdminPanel;
	private Text txt1;
	private Text txt2;
	private Text txt3;
	private Text txt4;
	private Text txt5;
	private Text txt6;
	private Text txt7;
	private Text txt8;

	private Label lbl1;
	private Label lbl2;
	private Label lbl3;
	private Label lbl4;
	private Label lbl5;
	private Label lbl6;
	private Label lbl7;
	private Label lbl8;
	private Label lblStatus;
	
	final private int NONE = 0;
	final private int MEDIA = 1;
	final private int TV = 2;
	final private int FILM = 3;
	final private int ACTOR = 4;
	final private int ACTORINMEDIA = 5;
	final private int LOCATION = 6;
	final private int LOCATIONOFMEDIA = 7;
	
	private int currentSelection = 0;
	private Button btnExit;
	private Button btnDelete;
	
	public Media getCurrentMedia() {
		return currentMedia;
	}

	public void setCurrentMedia(Media currentMedia) {
		this.currentMedia = currentMedia;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AdminPanel window = new AdminPanel();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shellAdminPanel.open();
		shellAdminPanel.layout();
		while (!shellAdminPanel.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shellAdminPanel = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shellAdminPanel.setImage(SWTResourceManager.getImage(AdminPanel.class, "/gui/tv.png"));
		shellAdminPanel.setSize(649, 471);
		shellAdminPanel.setText("Admin Panel");
		
		Group grpType = new Group(shellAdminPanel, SWT.NONE);
		grpType.setBounds(10, 0, 611, 81);
		
		Button btnMedia = new Button(grpType, SWT.RADIO);
		btnMedia.setBounds(10, 24, 111, 20);
		btnMedia.setText("Media");
		
		Button btnTv = new Button(grpType, SWT.RADIO);
		btnTv.setBounds(10, 50, 111, 20);
		btnTv.setText("TV");
		
		Button btnFilm = new Button(grpType, SWT.RADIO);
		btnFilm.setBounds(127, 24, 111, 20);
		btnFilm.setText("Film");
		
		Button btnActor = new Button(grpType, SWT.RADIO);
		btnActor.setBounds(127, 50, 111, 20);
		btnActor.setText("Actor");
		
		Button btnActorinmedia = new Button(grpType, SWT.RADIO);
		btnActorinmedia.setBounds(259, 24, 111, 20);
		btnActorinmedia.setText("ActorInMedia");
		
		Button btnLocation = new Button(grpType, SWT.RADIO);
		btnLocation.setBounds(259, 50, 111, 20);
		btnLocation.setText("Location");
		
		Button btnLocationofmedia = new Button(grpType, SWT.RADIO);
		btnLocationofmedia.setBounds(404, 24, 146, 20);
		btnLocationofmedia.setText("LocationOfMedia");
		
		Group grpDetails = new Group(shellAdminPanel, SWT.NONE);
		grpDetails.setBounds(10, 87, 611, 230);
		
		lbl1 = new Label(grpDetails, SWT.BORDER);
		lbl1.setBounds(10, 20, 121, 20);
		
		txt1 = new Text(grpDetails, SWT.BORDER);
		txt1.setBounds(137, 18, 464, 20);
		
		lbl2 = new Label(grpDetails, SWT.BORDER);
		lbl2.setBounds(10, 46, 121, 20);
		
		txt2 = new Text(grpDetails, SWT.BORDER);
		txt2.setBounds(137, 44, 464, 20);
		
		lbl3 = new Label(grpDetails, SWT.BORDER);
		lbl3.setBounds(10, 72, 121, 20);
		
		txt3 = new Text(grpDetails, SWT.BORDER);
		txt3.setBounds(137, 70, 464, 20);
		
		lbl4 = new Label(grpDetails, SWT.BORDER);
		lbl4.setBounds(10, 98, 121, 20);
		
		txt4 = new Text(grpDetails, SWT.BORDER);
		txt4.setBounds(137, 98, 464, 20);
		
		lbl5 = new Label(grpDetails, SWT.BORDER);
		lbl5.setBounds(10, 124, 121, 20);
		
		txt5 = new Text(grpDetails, SWT.BORDER);
		txt5.setBounds(137, 124, 464, 20);
		
		lbl6 = new Label(grpDetails, SWT.BORDER);
		lbl6.setBounds(10, 150, 121, 20);
		
		txt6 = new Text(grpDetails, SWT.BORDER);
		txt6.setBounds(137, 148, 464, 20);
		
		lbl7 = new Label(grpDetails, SWT.BORDER);
		lbl7.setBounds(10, 176, 121, 20);
		
		txt7 = new Text(grpDetails, SWT.BORDER);
		txt7.setBounds(137, 176, 464, 20);
		
		lbl8 = new Label(grpDetails, SWT.BORDER);
		lbl8.setBounds(10, 202, 121, 20);
		
		txt8 = new Text(grpDetails, SWT.BORDER);
		txt8.setBounds(137, 202, 464, 20);
		
		Group grpStatus = new Group(shellAdminPanel, SWT.NONE);
		grpStatus.setBounds(10, 329, 611, 87);
		
		lblStatus = new Label(grpStatus, SWT.BORDER);
		lblStatus.setBounds(10, 29, 162, 48);
		
		Button btnAdd = new Button(grpStatus, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				if(currentSelection == NONE)
					return;
				
				try
				{
					Object ob = createCurrentObject();
					if(ob == null)
					{
						lblStatus.setText("Error in new entry");
						return;
					}
					
					Updater update = new Updater();
					
					switch(currentSelection)
					{
						case MEDIA:
							update.addMedia( (Media)ob, "0"); // This will probably cause an error in the future. Need to change "0" to an integer later.
							break;
						case TV:
							Media m = new Media(((TVShow)ob).media_id, "<TBD>", "", "<TBD>", "", 1);
							update.addTV(m, (TVShow)ob);
							break;
						case FILM:
							Media med = new Media(((Film)ob).media_id, "<TBD>", "", "<TBD>", "", 0);
							update.addFilm(med, (Film)ob);
							break;
						case ACTOR:
							update.addActor((Actor)ob);
							break;
						case ACTORINMEDIA:
							update.addActorInMedia((ActorInMedia)ob);
							break;
						case LOCATION:
							update.addLocation((Location)ob);
							break;
						case LOCATIONOFMEDIA:
							update.addLocationOfMedia((LocationOfMedia)ob);
							break;						
					}
				}
				catch(Exception ex)
				{
					lblStatus.setText("Error adding entry");
					ex.printStackTrace();
					return;
				}
				
				lblStatus.setText("Added entry successfully");
			}
		});
		btnAdd.setText("Add New Entry");
		btnAdd.setBounds(286, 29, 120, 48);
		
		Button btnUpdate = new Button(grpStatus, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				if(currentSelection == NONE)
					return;
				
				try
				{
					Object ob = createCurrentObject();
					if(ob == null)
					{
						lblStatus.setText("Error in new entry");
						return;
					}
					
					Updater update = new Updater();
					
					switch(currentSelection)
					{
						case MEDIA:
							update.updateMedia( (Media)ob); 
							break;
						case TV:
							update.updateTV((TVShow)ob);
							break;
						case FILM:
							update.updateFilm((Film)ob);
							break;
						case ACTOR:
							update.updateActor((Actor)ob);
							break;
						case ACTORINMEDIA:
							update.updateActorInMedia((ActorInMedia)ob);
							break;
						case LOCATION:
							update.updateLocation((Location)ob);
							break;
						case LOCATIONOFMEDIA:
							update.updateLocationOfMedia((LocationOfMedia)ob);
							break;						
					}
				}
				catch(Exception ex)
				{
					lblStatus.setText("Error adding entry");
					ex.printStackTrace();
					return;
				}
				
				lblStatus.setText("Updated entry successfully");
			}
		});
		btnUpdate.setText("Update Existing");
		btnUpdate.setBounds(412, 29, 120, 48);
		
		btnExit = new Button(grpStatus, SWT.NONE);
		btnExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shellAdminPanel.close();
				shellAdminPanel.dispose();
			}
		});
		btnExit.setBounds(538, 29, 59, 48);
		btnExit.setText("Exit");
		
		btnDelete = new Button(grpStatus, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
				Updater update = new Updater();
				int id = Integer.parseInt(txt1.getText());
				switch(currentSelection)
				{
					case TV:
					case FILM:
					case MEDIA:
						update.deleteMedia(id);
						break;
					case ACTOR:
						update.deleteActor(id);
						break;
					case ACTORINMEDIA:
						int actor_id = id;
						int media_id = Integer.parseInt(txt2.getText());
						update.deleteActorInMedia(actor_id, media_id);
						break;
					case LOCATION:
						update.deleteLocation(id);
						break;
					case LOCATIONOFMEDIA:
						int med_id = id;
						int location_id = Integer.parseInt(txt2.getText());
						update.deleteLocationOfMedia(location_id, med_id);
						break;						
				}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnDelete.setText("Delete By ID");
		btnDelete.setBounds(160, 29, 120, 48);
		
		btnMedia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				changeSelection(MEDIA);
			}
		});

		btnTv.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				changeSelection(TV);
			}
		});
		
		btnFilm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				changeSelection(FILM);
			}
		});
		
		btnActor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				changeSelection(ACTOR);
			}
		});
		
		btnActorinmedia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				changeSelection(ACTORINMEDIA);
			}
		});
		
		btnLocation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				changeSelection(LOCATION);
			}
		});
		
		btnLocationofmedia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				changeSelection(LOCATIONOFMEDIA);
			}
		});
	}
	
	private void changeSelection(int selection)
	{
		currentSelection = NONE;
		updateDetails();
		currentSelection = selection;
		updateDetails();
	}
	
	protected Object createCurrentObject()
	{
		try
		{
			switch(currentSelection)
			{
				case MEDIA:
					Media media = new Media(Integer.parseInt(txt1.getText()), txt3.getText(), txt2.getText(),
							txt4.getText(), txt5.getText(), Integer.parseInt(txt6.getText()));
					return media;
				
				case TV:
					TVShow tv = new TVShow(Integer.parseInt(txt1.getText()), txt2.getText(), txt3.getText(),
							Integer.parseInt(txt4.getText()), Integer.parseInt(txt5.getText()));
					return tv;
				
				case FILM:
					Film film = new Film(Integer.parseInt(txt1.getText()), txt2.getText());
					return film;
				
				case ACTOR:
					Actor actor = new Actor(Integer.parseInt(txt1.getText()),txt2.getText(), txt3.getText());
					return actor;
				
				case ACTORINMEDIA:
					ActorInMedia AIM = new ActorInMedia(Integer.parseInt(txt1.getText()), Integer.parseInt(txt2.getText()), txt3.getText());
					return AIM;
				
				case LOCATION:
					Location loc = new Location(Integer.parseInt(txt1.getText()), txt2.getText(), txt3.getText(),
							txt4.getText(), txt5.getText(), txt6.getText(), Integer.parseInt(txt7.getText()), Integer.parseInt(txt8.getText()));
					return loc;
				
				case LOCATIONOFMEDIA:
					Location loc2 = new Location();
					loc2.setLocation_id(Integer.parseInt(txt2.getText()));
					
					LocationOfMedia LOM = new LocationOfMedia(Integer.parseInt(txt1.getText()), loc2, txt3.getText());
					return LOM;
				
				default:
					return null;
			}
		}
		catch(Exception ex)
		{
			lblStatus.setText("Error Adding Entry");
			ex.printStackTrace();
			return null;
		}
	}

	private void updateDetails()
	{
		switch(currentSelection)
		{
			case MEDIA:
				lbl1.setText("media_id");
				lbl2.setText("freebase_id");
				lbl3.setText("name");
				lbl4.setText("directors");
				lbl5.setText("image");
				lbl6.setText("isTv");
				
				if(currentMedia != null)
				{
					txt1.setText(String.valueOf(currentMedia.media_id));
					txt2.setText(currentMedia.freebase_id);
					txt3.setText(currentMedia.name);
					txt4.setText(currentMedia.directors);
					txt5.setText(currentMedia.image);
					txt6.setText(String.valueOf(currentMedia.isTV));
				}
			break;
			
			case TV:
				lbl1.setText("media_id");
				lbl2.setText("first_episode");
				lbl3.setText("last_episode");
				lbl4.setText("num_seasons");
				lbl5.setText("num_episodes");
				
				if(currentMedia != null && currentMedia instanceof TVShow)
				{
					txt1.setText(String.valueOf(currentMedia.media_id));
					txt2.setText(((TVShow)currentMedia).first_episode);
					txt3.setText(((TVShow)currentMedia).last_episode);
					txt4.setText(String.valueOf(((TVShow)currentMedia).num_seasons));
					txt5.setText(String.valueOf(((TVShow)currentMedia).num_episodes));
				}
			break;
			
			case FILM:
				lbl1.setText("media_id");
				lbl2.setText("release_date");
				
				if(currentMedia != null && currentMedia instanceof Film)
				{
					txt1.setText(String.valueOf(currentMedia.media_id));
					txt2.setText(((Film)currentMedia).release_date);
				}
			break;
			
			case ACTOR:
				lbl1.setText("actor_id");
				lbl2.setText("freebase_id");
				lbl3.setText("name");
			break;
			
			case ACTORINMEDIA:
				lbl1.setText("actor_id");
				lbl2.setText("media_id");
				lbl3.setText("char_name");
			break;
			
			case LOCATION:
				lbl1.setText("location_id");
				lbl2.setText("lat");
				lbl3.setText("lng");
				lbl4.setText("country");
				lbl5.setText("city");
				lbl6.setText("place");
				lbl7.setText("upvotes");
				lbl8.setText("downvotes");
				
				if(currentLocation != null)
				{
					txt1.setText(String.valueOf(currentLocation.location_id));
					txt2.setText(currentLocation.lat);
					txt3.setText(currentLocation.lng);
					txt4.setText(currentLocation.country);
					txt5.setText(currentLocation.city);
					txt6.setText(currentLocation.place);
					txt7.setText(String.valueOf(currentLocation.upvotes));
					txt8.setText(String.valueOf(currentLocation.downvotes));
				}
			break;
			
			case LOCATIONOFMEDIA:
				lbl1.setText("media_id");
				lbl2.setText("location_id");
				lbl3.setText("scene_episode");
			break;
			
			default:
				lbl1.setText("");
				lbl2.setText("");
				lbl3.setText("");
				lbl4.setText("");
				lbl5.setText("");
				lbl6.setText("");
				lbl7.setText("");
				lbl8.setText("");
				
				txt1.setText("");
				txt2.setText("");
				txt3.setText("");
				txt4.setText("");
				txt5.setText("");
				txt6.setText("");
				txt7.setText("");
				txt8.setText("");
				
			break;
		}
	}
}
