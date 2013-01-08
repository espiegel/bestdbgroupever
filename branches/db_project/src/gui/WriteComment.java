package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class WriteComment {

	protected Shell shlWriteAComment;
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			WriteComment window = new WriteComment();
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
		shlWriteAComment.open();
		shlWriteAComment.layout();
		while (!shlWriteAComment.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlWriteAComment = new Shell();
		shlWriteAComment.setSize(364, 300);
		shlWriteAComment.setText("Write a comment");
		
		text = new Text(shlWriteAComment, SWT.BORDER);
		text.setBounds(10, 10, 326, 188);
		
		Button btnSubmit = new Button(shlWriteAComment, SWT.NONE);
		btnSubmit.setBounds(10, 204, 140, 40);
		btnSubmit.setText("Submit");
		
		Button btnBack = new Button(shlWriteAComment, SWT.NONE);
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlWriteAComment.close();
				shlWriteAComment.dispose();
			}
		});
		btnBack.setBounds(196, 204, 140, 40);
		btnBack.setText("Back");

	}
}
