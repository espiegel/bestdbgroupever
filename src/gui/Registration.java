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
import main.*;

public class Registration {

	protected Shell shlRegistration;
	private Text txtUsername;
	private Text txtPassword;
	private Label lblError;
	
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
		shlRegistration = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shlRegistration.setImage(SWTResourceManager.getImage(Registration.class, "/gui/tv.png"));
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
		
		txtUsername = new Text(shlRegistration, SWT.BORDER);
		txtUsername.setBounds(190, 45, 175, 26);
		
		txtPassword = new Text(shlRegistration, SWT.BORDER);
		txtPassword.setBounds(190, 85, 175, 26);
		
		Button btnRegister = new Button(shlRegistration, SWT.NONE);
		btnRegister.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String username = txtUsername.getText();
				String password = txtPassword.getText();
				
				if(username.isEmpty() || password.isEmpty())
				{
					lblError.setText("You must specify a username and a password.");
				}
				// Need to check with the database correct username and password combination
				else if(!(Main.register(username, password))) 
				{
					lblError.setText("There is already a user with that name!");
				}
				else
				{
					shlRegistration.close();
					shlRegistration.dispose();
					
					
					//MainDisplay maindisplay = new MainDisplay();
					//maindisplay.open();							
				}
				
			}
		});
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
		btnNewButton.setText("Back");
		
		lblError = new Label(shlRegistration, SWT.NONE);
		lblError.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblError.setBounds(47, 129, 291, 20);

	}
	
	// For setting errors 
	public void setError(String str)
	{
		lblError.setText(str);
	}
}
