package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewLocationWindow extends Dialog {

	protected Object result;
	protected Shell shell;
	private MapWidget map;
	private Text adress;
	private Text scene;
	private int media_id;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewLocationWindow(Shell parent/*, int media_id*/) {
		super(parent, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		setText("window");
		//this.media_id = media_id;
		}

	/**
	 * Open the dialog.
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
		
		map = new MapWidget(shell, "map.html",null,this);
		
		adress = new Text(shell, SWT.BORDER);
		adress.setBounds(70, 30, 163, 19);
		
		scene = new Text(shell, SWT.BORDER);
		scene.setBounds(70, 110, 180, 19);
		scene.setMessage("Add scene...");
		
		Button searchBtn = new Button(shell, SWT.NONE);
		searchBtn.setBounds(98, 60, 135, 28);
		searchBtn.setText("Add New Location");
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(200, 60, 135, 28);
		btnNewButton.setText("Search");
		
		map.init();
		map.getBrowser().setBounds(0, 240, 640, 400);
	}
	
	public void addLocation(String lat, String lng, String address) {
		
	}
	
}
