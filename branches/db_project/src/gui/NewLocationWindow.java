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
	private Text text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewLocationWindow(Shell parent) {
		super(parent, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		setText("הוסף מיקום חדש");
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
		
		map = new MapWidget(shell, "map.html",null);
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(70, 30, 163, 19);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(98, 60, 135, 28);
		btnNewButton.setText("Add new location");
		map.init();
		map.getBrowser().setBounds(0, 240, 640, 400);
	}

}
