package gui;

import objects.*;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import db.Updater;

public class AdminPanel {

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
		lblStatus.setBounds(10, 29, 224, 48);
		
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
							update.addMedia( (Media)ob, "0"); // This will probably cause an error. Need to change 0 to an integer later.
							break;
						case TV:
							update.addTV((TVShow)ob);
							break;
						case FILM:
							update.addFilm((Film)ob);
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
					return;
				}
				
				lblStatus.setText("Added entry successfully");
			}
		});
		btnAdd.setText("Add");
		btnAdd.setBounds(259, 29, 174, 48);
		
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
					return;
				}
				
				lblStatus.setText("Added entry successfully");
			}
		});
		btnUpdate.setText("Update");
		btnUpdate.setBounds(439, 29, 162, 48);
		
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
					Media media = new Media(Integer.parseInt(lbl1.getText()), lbl3.getText(), lbl2.getText(),
							lbl4.getText(), lbl5.getText(), Integer.parseInt(lbl6.getText()));
					return media;
				
				case TV:
					TVShow tv = new TVShow(Integer.parseInt(lbl1.getText()), lbl2.getText(), lbl3.getText(),
							Integer.parseInt(lbl4.getText()), Integer.parseInt(lbl5.getText()));
					return tv;
				
				case FILM:
					Film film = new Film(Integer.parseInt(lbl1.getText()), lbl2.getText());
					return film;
				
				case ACTOR:
					Actor actor = new Actor(Integer.parseInt(lbl1.getText()), Integer.parseInt(lbl2.getText()), lbl3.getText());
					return actor;
				
				case ACTORINMEDIA:
					ActorInMedia AIM = new ActorInMedia(Integer.parseInt(lbl1.getText()), Integer.parseInt(lbl2.getText()), lbl3.getText());
					return AIM;
				
				case LOCATION:
					Location loc = new Location(Integer.parseInt(lbl1.getText()), lbl2.getText(), lbl3.getText(),
							lbl4.getText(), lbl5.getText(), lbl6.getText(), Integer.parseInt(lbl7.getText()), Integer.parseInt(lbl8.getText()));
					return loc;
				
				case LOCATIONOFMEDIA:
					Location loc2 = new Location();
					loc2.setLocation_id(Integer.parseInt(lbl2.getText()));
					
					LocationOfMedia LOM = new LocationOfMedia(Integer.parseInt(lbl1.getText()), loc2, lbl3.getText());
					return LOM;
				
				default:
					return null;
			}
		}
		catch(Exception ex)
		{
			lblStatus.setText("Error Adding Entry");
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
			break;
			
			case TV:
				lbl1.setText("media_id");
				lbl2.setText("first_episode");
				lbl3.setText("last_episode");
				lbl4.setText("num_seasons");
				lbl5.setText("num_episodes");
			break;
			
			case FILM:
				lbl1.setText("media_id");
				lbl2.setText("release_date");
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
			break;
		}
	}
}
