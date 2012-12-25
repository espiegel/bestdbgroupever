package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class Registration {

	protected Shell shlRegistration;
	private Text username;
	private Text password;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Registration window = new Registration();
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
		shlRegistration.open();
		shlRegistration.layout();
		while (!shlRegistration.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlRegistration = new Shell();
		shlRegistration.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		shlRegistration.setSize(407, 260);
		shlRegistration.setText("Registration");
		
		Label lblEnterADesired = new Label(shlRegistration, SWT.NONE);
		lblEnterADesired.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblEnterADesired.setBounds(24, 45, 160, 20);
		lblEnterADesired.setText("Choose your username:");
		
		Label lblEnterAPassword = new Label(shlRegistration, SWT.NONE);
		lblEnterAPassword.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblEnterAPassword.setBounds(24, 91, 160, 20);
		lblEnterAPassword.setText("Choose your password:");
		
		username = new Text(shlRegistration, SWT.BORDER);
		username.setBounds(190, 45, 175, 26);
		
		password = new Text(shlRegistration, SWT.BORDER);
		password.setBounds(190, 85, 175, 26);
		
		Button btnRegister = new Button(shlRegistration, SWT.NONE);
		btnRegister.setBounds(47, 154, 100, 50);
		btnRegister.setText("Register");
		
		Button btnNewButton = new Button(shlRegistration, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlRegistration.close();
				shlRegistration.dispose();
			}
		});
		btnNewButton.setBounds(238, 154, 100, 50);
		btnNewButton.setText("Exit");

	}

}
