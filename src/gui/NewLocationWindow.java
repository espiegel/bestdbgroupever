package gui;

import java.sql.SQLException;

import objects.Location;
import objects.LocationOfMedia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import db.Updater;

public class NewLocationWindow extends Dialog {

	protected Object result;
	protected Shell shell;
	private MapWidget map;
	private Text address;
	private Text scene;
	private int media_id;
	private String place, country, city;
	private LocationOfMedia lom = null;
	private Location location = null;
	private Display display;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public NewLocationWindow(Shell parent, int media_id, String name) {
		super(parent, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		setText("Add a location for " + name);
		this.media_id = media_id;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	private final static Color EMPTY_MESSAGE_COLOR = SWTResourceManager.getColor(SWT.COLOR_GRAY);
	private final static Color TEXT_COLOR = SWTResourceManager.getColor(SWT.COLOR_BLACK);
	
	private boolean flgEmptySearchText = true;
	private final static String EMPTY_TEXT_SEARCH = "-search for a place by address/name-";
	
	private boolean flgEmptySceneText = true;
	private final static String EMPTY_TEXT_SCENE = "-what scene is it in? what's special about it?-";
	
	private Label lblNewLabel;
	
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(520, 520);
		shell.setText(getText());

		map = new MapWidget(shell, "map.html", null, this);
		map.init();
		map.getBrowser().setBounds(0, 170, 520, 350);

		address = new Text(shell, SWT.BORDER | SWT.CENTER);
		address.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (flgEmptySearchText) {
					address.setText("");
					address.setForeground(TEXT_COLOR);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				flgEmptySearchText = address.getText().isEmpty();
				if (flgEmptySearchText) {
					address.setText(EMPTY_TEXT_SEARCH);
					address.setForeground(EMPTY_MESSAGE_COLOR);
				}
			}
		});
		address.setText(EMPTY_TEXT_SEARCH);
		address.setForeground(EMPTY_MESSAGE_COLOR);
		address.setBounds(10, 10, 365, 19);

		scene = new Text(shell, SWT.BORDER);
		scene.setBounds(10, 129, 365, 19);
		scene.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (flgEmptySceneText) {
					scene.setText("");
					scene.setForeground(TEXT_COLOR);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				flgEmptySceneText = scene.getText().isEmpty();
				if (flgEmptySceneText) {
					scene.setText(EMPTY_TEXT_SCENE);
					scene.setForeground(EMPTY_MESSAGE_COLOR);
				}
			}
		});
		scene.setText(EMPTY_TEXT_SCENE);
		scene.setForeground(EMPTY_MESSAGE_COLOR);

		Button okBtn = new Button(shell, SWT.NONE);
		okBtn.setBounds(381, 125, 135, 28);
		okBtn.setText("Add");

		okBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String scene_episode = (flgEmptySceneText ? "Unknown"
						: canonicalize(scene.getText()));
				lom = new LocationOfMedia(media_id, location, scene_episode);
				try {
					if (location == null || lom == null)
						return;
					Updater updater = new Updater();
					updater.addLocation(location);
					updater.addLocationOfMedia(lom);
					MessageBox messageBox= new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setMessage("Location added :)");
					messageBox.open();
					shell.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		Button searchBtn = new Button(shell, SWT.NONE);
		searchBtn.setBounds(378, 6, 135, 28);
		searchBtn.setText("Search");
		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String text = address.getText();
				if (!flgEmptySearchText) {
					map.codeAddress(text);
				}
			}
		});
		Button btnPlaceMarker = new Button(shell, SWT.NONE);
		btnPlaceMarker.setBounds(133, 60, 135, 28);
		btnPlaceMarker.setText("Choose From Map");
		
		lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.BOLD));
		lblNewLabel.setBounds(177, 35, 44, 19);
		lblNewLabel.setText("-OR-");
		btnPlaceMarker.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Cursor c = new Cursor(display, SWT.CURSOR_CROSS);
				shell.setCursor(c);
				map.startListen();
			}
		});

	}

	public void addLocation(String lat, String lng, String address)
			throws SQLException {
		if (!address.equals("undefined")) {
			splitLocation(address);
			location = new Location(0, lat, lng, country, city, place, 0, 0);
		}
	}

	public String canonicalize(String str) {

		str = str.replaceAll("'", "&#039");
		str = str.replaceAll("&", "&amp;");

		return str;

	}

	private void splitLocation(String location) {
		String[] locations = location.split(", ");

		if (locations.length == 0) {
			place = location;
			city = "";
			country = "";
		} else if (locations.length == 1) {
			place = locations[0];
			city = "";
			country = "";
		} else if (locations.length == 2) {
			place = "";
			city = locations[0];
			country = locations[1];
		} else {
			place = locations[0];
			for (int i = 1; i < locations.length - 2; i++) {
				if (locations[i] != null)
					if (!locations[i].equals(""))
						place += "," + locations[i];
			}
			city = locations[locations.length - 2];
			country = locations[locations.length - 1];
		}
		city = canonicalize(city);
		place = canonicalize(place);
		country = canonicalize(country);
	}

}
