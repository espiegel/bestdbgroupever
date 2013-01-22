package gui;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.SQLException;

import objects.Location;
import objects.LocationOfMedia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import db.Updater;

public class NewLocationWindow extends Dialog {

	protected Object result;
	protected Shell shell;
	private MapWidget map;
	private Text address;
	private Text scene;
	private int media_id;
	private static CharsetEncoder encoder = Charset.forName("US-ASCII")
			.newEncoder();
	private String place, country, city;
	private LocationOfMedia lom = null;
	private Location location = null;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public NewLocationWindow(Shell parent, int media_id) {
		super(parent, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		setText("window");
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
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(640, 640);
		shell.setText(getText());

		map = new MapWidget(shell, "map.html", null, this);
		map.init();
		map.getBrowser().setBounds(0, 240, 640, 400);

		address = new Text(shell, SWT.BORDER);
		address.setBounds(70, 30, 163, 19);

		scene = new Text(shell, SWT.BORDER);
		scene.setBounds(70, 110, 180, 19);
		//scene.setMessage("Add scene...");

		Button okBtn = new Button(shell, SWT.NONE);
		okBtn.setBounds(98, 60, 135, 28);
		okBtn.setText("Add New Location");

		okBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String scene_episode = (scene.getText().equals("") ? "Unknown"
						: canonicalize(scene.getText()));
				lom = new LocationOfMedia(media_id, location, scene_episode);
				try {
					if (location == null || lom == null)
						return;
					Updater updater = new Updater();
					updater.addLocation(location);
					updater.addLocationOfMedia(lom);
					shell.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		Button searchBtn = new Button(shell, SWT.NONE);
		searchBtn.setBounds(200, 60, 135, 28);
		searchBtn.setText("Search");
		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String text = address.getText();
				if (!text.equals("")) {
					map.codeAddress(text);
				}
			}
		});
		Button btnPlaceMarker = new Button(shell, SWT.NONE);
		btnPlaceMarker.setBounds(400, 60, 135, 28);
		btnPlaceMarker.setText("Choose From Map");
		btnPlaceMarker.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
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
