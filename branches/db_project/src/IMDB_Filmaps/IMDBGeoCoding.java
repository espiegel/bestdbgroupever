package IMDB_Filmaps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * class that takes a IMDB locations list , geo code locations of media from
 * database and save it in an xml file . this file will be parses and uploaded
 * by XMLParser
 * 
 * @author Yoav
 * 
 */
public class IMDBGeoCoding {
	private Browser browser;
	private IMDBLocationsList IMDBLocationsList;
	private Map<String, String> LocationsList;
	private Iterator<String> locIT;
	private String browserText;
	private String name;
	private boolean ok = false;
	private String geoCodeHTML;
	private int i = 1;
	private int limit;
	private int startNum;
	private ResultSet mediaRS;
	private int locationsSize = 0;
	private Connection connect;
	private String IMDBListBeforeParsing;
	private String IMDBListAfterParsing;
	private File outputFile;
	private Shell shell;

	/**
	 * 
	 * @param connect
	 *            Connection to DB
	 * @param IMDBListBeforeParsing
	 *            locations of media list Path from IMDB site
	 *            ftp://ftp.fu-berlin
	 *            .de/pub/misc/movies/database/locations.list.gz
	 * @param IMDBListAfterParsing
	 *            output xml file Path
	 * @param geoCodeHTML
	 *            gro-coding html
	 * @param limit
	 *            number of media to geo code
	 * @param startNum
	 *            starting index of media in Media table (start is 0)
	 * @throws IOException
	 *             if IMDBListBeforeParsing not readable
	 */
	public IMDBGeoCoding(Connection connect, String IMDBListBeforeParsing,
			String IMDBListAfterParsing, String geoCodeHTML, int startNum,
			int limit) throws IOException {
		this.connect = connect;
		this.IMDBListBeforeParsing = IMDBListBeforeParsing;
		this.IMDBListAfterParsing = IMDBListAfterParsing;
		this.geoCodeHTML = getTextFile(geoCodeHTML);
		this.limit = limit;
		this.startNum = startNum;
		outputFile = new File(IMDBListAfterParsing);
		if(outputFile.exists()) // delete information
			saveToFile("",false);
		String start = "<Document>\r\n";
		saveToFile(start,true);
	}
	
	public void initAndStart() throws  SQLException,
	IOException {
		initAndStart(new Display());
	}

	/**
	 * start all process
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	public void initAndStart(Display display) throws  SQLException,
			IOException {
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		SashForm sash = new SashForm(shell, SWT.HORIZONTAL);
		try {
			browser = new Browser(sash, SWT.NONE);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: "
					+ e.getMessage());
			display.dispose();
			return;
		}
		browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent arg0) {
				ok = arg0.title.startsWith("tableloaded");
				if (arg0.title.startsWith("error")) {
					ok = true;
					locationsSize = 0;
				}
				if (ok) {
					ok = false;
					if (locationsSize != 0)
						saveLocations();
					if (i <= limit) {
						try {
							start();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else
						finish();
				}
			}
		});
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}

			public void completed(ProgressEvent event) {
				addToTable();
			}
		});
		getMovieList();
		start();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * adds geo coded locations to html table in the browser
	 */
	private void addToTable() {
		if (locationsSize == 0)
			browser.execute("window.document.title=\"tableloaded\";");
		while (locIT.hasNext()) {
			String address = locIT.next().toString();
			String scene = LocationsList.get(address);
			browser.execute("codeAddress('" + address + "','" + scene + "');");
		}

	}

	/**
	 * appends xml signature to file
	 */
	private void finish() {
		System.out.println("Geo Coding finished!");
		System.out.println("Output XML :"+IMDBListAfterParsing);
		String end = "</Document>";
		saveToFile(end,true);
		shell.dispose();
	}

	/**
	 * get media list from database
	 * 
	 * @throws SQLException
	 */
	private void getMovieList() throws SQLException {
		mediaRS = connect.createStatement().executeQuery(
				"SELECT name FROM Media LIMIT " + startNum + "," + limit);
	}

	/**
	 * 
	 * @return next media from database
	 * @throws SQLException
	 */
	private String getNextName() throws SQLException {
		if (mediaRS.next()) {
			System.out.println("Geo coding media: "+i);
			i++;
			return mediaRS.getString("name").replaceAll("&#039", "'");
		}
		return null;
	}

	/**
	 * read geoCodeHTML file
	 * 
	 * @param file
	 *            geoCodeHTML file
	 * @return
	 * @throws IOException
	 */
	private String getTextFile(String file) throws IOException {
		StringBuilder contentBuilder = new StringBuilder();
		BufferedReader in = new BufferedReader(new FileReader(file));
		String str;
		while ((str = in.readLine()) != null) {
			contentBuilder.append(str);
		}
		in.close();

		String content = contentBuilder.toString();
		return content;
	}

	/**
	 * parse html text and convert it to xml
	 * 
	 * @return true if parsing succeeded, false otherwise
	 */
	private boolean ParseString() {
		int j = browserText.indexOf("<TBODY>");
		if (j < 0) {
			System.out.println("parsing error <TBODY>");
			return false;
		}
		browserText = browserText.substring(j + "<TBODY>".length() + 1);
		browserText = "<movie>\n<title>" + name + "</title>" + browserText;
		int j1 = browserText.indexOf("</TBODY>");
		if (j1 < 0) {
			System.out.println("parsing error </TBODY>");
			return false;
		}
		browserText = browserText.substring(0, j1);
		browserText = browserText.replace("<TD>", "");
		browserText = browserText.replace("</TD>", "");
		browserText = browserText.replace("<TR>", "");
		browserText = browserText.replace("</TR>", "");
		browserText = browserText.replace("\r\n\r\n", "\r\n");
		browserText = browserText.replace("\n\r\n", "\r\n");
		browserText = browserText.replace("\r\n#coordinates#", "#coordinates#");
		browserText = browserText.replace("#/coordinates#\r\n",
				"#/coordinates#");
		browserText += "\n</movie>";
		browserText += "\r\n";
		browserText = browserText.replaceAll("#coordinates#", "<coordinates>");
		browserText = browserText
				.replaceAll("#/coordinates#", "</coordinates>");
		browserText = browserText.replaceAll("#scene#", "<scene>");
		browserText = browserText.replaceAll("#/scene#", "</scene></location>");
		browserText = browserText.replaceAll("#place#", "<location><place>");
		browserText = browserText.replaceAll("#/place#", "</place>");
		return true;
	}

	/**
	 * saves current media locations to file
	 */
	private void saveLocations() {
		browserText = browser.getText();
		if (ParseString()) {
			saveToFile(browserText,true);
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
		}
	}

	private void saveToFile(String s,boolean append) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(outputFile, append);
			fileWriter.write(s);
			fileWriter.close();
		} catch (Exception ex) {
			System.out.println("error saving locations to :"
					+ IMDBListAfterParsing);
		}
	}

	/**
	 * starts the geo coding process
	 * 
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	private void start() throws SQLException, FileNotFoundException {
		browser.setText(geoCodeHTML);
		name = getNextName();
		if (name == null) {
			System.exit(0);
		}
		IMDBLocationsList = new IMDBLocationsList(IMDBListBeforeParsing);
		IMDBLocationsList.find(name);
		LocationsList = IMDBLocationsList.getLocations();
		locationsSize = LocationsList.keySet().size();
		locIT = LocationsList.keySet().iterator();
	}
	public int getStatus(){
		return i;
	}

}