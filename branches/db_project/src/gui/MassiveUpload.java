package gui;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.xml.sax.SAXException;

import FreeBase.FileUploader;

import com.narphorium.freebase.services.exceptions.FreebaseServiceException;

import db.DataUploader;

public class MassiveUpload extends Dialog {

	protected Object result;
	protected Shell shlBatchUploadScreen;
	private Text txtIMDBPath;
	private Text txtFilmapsLimit;
	private Text txtFilmapsPath;
	private Text txtFreebaseLimit;
	private Text txtTsvLimit;
	private Text txtTsvMediaPath;
	private Text txtTsvCastPath;
	private Label lblInfo;
	private ProgressBar progressBar;
	private volatile Display display;
	private Button btnUpload;
	private volatile Connection conn = null;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public MassiveUpload(Shell parent) {
		super(parent, SWT.TITLE);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlBatchUploadScreen.open();
		shlBatchUploadScreen.layout();
		display = getParent().getDisplay();
		while (!shlBatchUploadScreen.isDisposed()) {
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
		shlBatchUploadScreen = new Shell(getParent(), getStyle());
		shlBatchUploadScreen.setSize(450, 363);
		shlBatchUploadScreen.setText("Batch upload screen");
		
		final Button rdbIMDB = new Button(shlBatchUploadScreen, SWT.RADIO);
		rdbIMDB.setSelection(true);
		rdbIMDB.setBounds(10, 37, 91, 18);
		rdbIMDB.setText("IMDB:");
		
		Label lblImportFrom = new Label(shlBatchUploadScreen, SWT.NONE);
		lblImportFrom.setBounds(10, 11, 91, 14);
		lblImportFrom.setText("Import from");
		
		Label lblListAddressFor = new Label(shlBatchUploadScreen, SWT.NONE);
		lblListAddressFor.setBounds(167, 40, 71, 14);
		lblListAddressFor.setText("list to parse:");
		
		txtIMDBPath = new Text(shlBatchUploadScreen, SWT.BORDER);
		txtIMDBPath.setText("IMDB\\IMDBLocations.list");
		txtIMDBPath.setBounds(265, 40, 150, 19);
		
		final Button rdbFilmaps = new Button(shlBatchUploadScreen, SWT.RADIO);
		rdbFilmaps.setBounds(10, 74, 91, 18);
		rdbFilmaps.setText("Filmaps:");
		
		txtFilmapsLimit = new Text(shlBatchUploadScreen, SWT.BORDER);
		txtFilmapsLimit.setBounds(375, 104, 40, 19);
		
		Label label = new Label(shlBatchUploadScreen, SWT.NONE);
		label.setText("limit to top N (optional):");
		label.setBounds(167, 104, 134, 14);
		
		Label label_1 = new Label(shlBatchUploadScreen, SWT.NONE);
		label_1.setText("list to parse:");
		label_1.setBounds(167, 77, 71, 14);
		
		txtFilmapsPath = new Text(shlBatchUploadScreen, SWT.BORDER);
		txtFilmapsPath.setText("Filmaps\\FilmapsLocations.list");
		txtFilmapsPath.setBounds(265, 77, 150, 19);
		
		final Button rdbFreebase = new Button(shlBatchUploadScreen, SWT.RADIO);
		rdbFreebase.setText("Freebase:");
		rdbFreebase.setBounds(10, 129, 91, 18);
		
		txtFreebaseLimit = new Text(shlBatchUploadScreen, SWT.BORDER);
		txtFreebaseLimit.setBounds(375, 147, 40, 19);
		
		Label label_2 = new Label(shlBatchUploadScreen, SWT.NONE);
		label_2.setText("limit to top N (optional):");
		label_2.setBounds(167, 150, 134, 14);
		
		final Combo cmbFreebaseType = new Combo(shlBatchUploadScreen, SWT.NONE);
		cmbFreebaseType.setItems(new String[] {"TV Shows", "Films"});
		cmbFreebaseType.select(0);
		cmbFreebaseType.setBounds(167, 129, 97, 22);
		
		final Button rdbTsv = new Button(shlBatchUploadScreen, SWT.RADIO);
		rdbTsv.setText("Freebase (TSV files):");
		rdbTsv.setBounds(10, 172, 134, 18);
		
		final Combo cmbTsvType = new Combo(shlBatchUploadScreen, SWT.NONE);
		cmbTsvType.setItems(new String[] {"TV Shows", "Films"});
		cmbTsvType.setBounds(167, 172, 97, 22);
		cmbTsvType.select(0);
		
		Label label_3 = new Label(shlBatchUploadScreen, SWT.NONE);
		label_3.setText("limit to top N (optional):");
		label_3.setBounds(167, 193, 134, 14);
		
		txtTsvLimit = new Text(shlBatchUploadScreen, SWT.BORDER);
		txtTsvLimit.setBounds(375, 190, 40, 19);
		
		Label lblMediaFile = new Label(shlBatchUploadScreen, SWT.NONE);
		lblMediaFile.setText("media file:");
		lblMediaFile.setBounds(167, 213, 71, 14);
		
		txtTsvMediaPath = new Text(shlBatchUploadScreen, SWT.BORDER);
		txtTsvMediaPath.setText("TV\\tv_program.tsv");
		txtTsvMediaPath.setBounds(265, 213, 150, 19);
		
		Label lblCastDetails = new Label(shlBatchUploadScreen, SWT.NONE);
		lblCastDetails.setText("cast details:");
		lblCastDetails.setBounds(167, 238, 71, 14);
		
		txtTsvCastPath = new Text(shlBatchUploadScreen, SWT.BORDER);
		txtTsvCastPath.setText("TV\\regular_tv_appearance.tsv");
		txtTsvCastPath.setBounds(265, 238, 150, 19);
		
		btnUpload = new Button(shlBatchUploadScreen, SWT.NONE);
		btnUpload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnUpload.setEnabled(false);
				Cursor c = new Cursor(display, SWT.CURSOR_WAIT);
				shlBatchUploadScreen.setCursor(c);
				display.asyncExec(new Runnable() {
					
					@Override
					public void run() {
						try {
							conn = db.ConnectionManager.makeConnection();
							if (rdbIMDB.getSelection()) {
								DataUploader.IMDBUpload(conn, txtIMDBPath.getText(), "IMDB\\IMDBLocations.xml", "IMDB\\geocode.html", 0, Integer.MAX_VALUE);
							} else if (rdbFilmaps.getSelection()) {
								DataUploader.FilmapsUpload(conn, txtFilmapsPath.getText(), parseText(txtFilmapsLimit, Integer.MAX_VALUE));
							} else if (rdbFreebase.getSelection()) {
								int limit = parseText(txtFreebaseLimit, Integer.MAX_VALUE);
								
								if (limit%500==0 || limit==Integer.MAX_VALUE)
									limit/=500;
								else
									limit=limit/500+1;
									
								DataUploader.FreebaseUpload(conn, limit, cmbFreebaseType.getSelectionIndex()==0);
							} else if (rdbTsv.getSelection()) {
								FileUploader fu = new FileUploader(conn, new File(txtTsvMediaPath.getText()), new File(txtTsvCastPath.getText()), cmbTsvType.getSelectionIndex()==0);
								fu.upload(parseText(txtTsvLimit, Integer.MAX_VALUE));
							}
							if (!conn.getAutoCommit()) conn.commit();
							conn.close();
						} catch (SQLException ex) {
							showInfo("Error in dealing with the DB", true);
							ex.printStackTrace();
							finish();
							return;
						} catch (IOException ex) {
							showInfo("Error reading files", true);
							ex.printStackTrace();
							finish();
							return;
						} catch (FreebaseServiceException ex) {
							showInfo("Error connecting to freebase", true);
							ex.printStackTrace();
							finish();
							return;
						} catch (SAXException ex) {
							showInfo("Error parsing XML", true);
							ex.printStackTrace();
							finish();
							return;
						} catch (ParserConfigurationException ex) {
							showInfo("Error in XML parser configuration", true);
							ex.printStackTrace();
							finish();
							return;
						}
						finish();
						showInfo("Finished", false);
					}
				});
			}
		});
		btnUpload.setBounds(298, 263, 117, 28);
		btnUpload.setText("Start upload");
		
		progressBar = new ProgressBar(shlBatchUploadScreen, SWT.NONE);
		progressBar.setBounds(10, 302, 405, 14);
		progressBar.setVisible(false);
		
		lblInfo = new Label(shlBatchUploadScreen, SWT.NONE);
		lblInfo.setBounds(10, 322, 405, 14);
		lblInfo.setText("Error Text");
		lblInfo.setVisible(false);
		
		Button btnCancel = new Button(shlBatchUploadScreen, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (conn!=null) {
					MessageBox mb = new MessageBox(shlBatchUploadScreen, SWT.YES | SWT.NO | SWT.ICON_WARNING);
					mb.setMessage("Are you sure you want to exit in the middle of an operation? This might have unintended consequances.");
					int res = mb.open();
					if (res==SWT.NO) return;
					try {
						conn.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				shlBatchUploadScreen.close();
			}
		});
		btnCancel.setBounds(201, 263, 94, 28);
		btnCancel.setText("Cancel");
		
		Label lblNoticeBatchUpload = new Label(shlBatchUploadScreen, SWT.WRAP);
		lblNoticeBatchUpload.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		lblNoticeBatchUpload.setBounds(10, 200, 134, 83);
		lblNoticeBatchUpload.setText("Notice batch upload can take several hours for hundreds of items, depending on DB load");
	}

	private static int parseText(Text txt, int default_val) {
		final String str = txt.getText();
		if (str.isEmpty()) {
			return default_val;
		} else {
			try {
				return Integer.parseInt(str);
			} catch (Exception e) {
				return default_val;
			}
		}
	}
	
	private synchronized void finish() {
		conn = null;
		btnUpload.setEnabled(true);
		Cursor c = new Cursor(display, SWT.CURSOR_ARROW);
		shlBatchUploadScreen.setCursor(c);
	}
	
	private void showInfo(String text, boolean error) {
		lblInfo.setVisible(true);
		if (error) {
			lblInfo.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_RED));
		} else {
			lblInfo.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_BLACK));
		}
		lblInfo.setText(text);
	}
}
