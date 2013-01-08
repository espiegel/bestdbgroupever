package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import main.*;
public class Login {

	protected Shell shlTvTraveller;
	private Label txtLoginToTv;
	private Text txtUsername;
	private Text txtPassword;
	private Label lblError;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Login window = new Login();
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
		shlTvTraveller.open();
		shlTvTraveller.layout();
		while (!shlTvTraveller.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlTvTraveller = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shlTvTraveller.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		shlTvTraveller.setMinimumSize(new Point(360, 280));
		shlTvTraveller.setSize(360, 280);
		shlTvTraveller.setText("TV Traveller");
		shlTvTraveller.setLayout(null);
		
		//SWT.CLOSE | SWT.TITLE | SWT.MIN
		txtLoginToTv = new Label(shlTvTraveller, SWT.BORDER | SWT.WRAP | SWT.HORIZONTAL | SWT.SHADOW_IN | SWT.CENTER);
		txtLoginToTv.setBounds(70, 10, 210, 36);
		txtLoginToTv.setAlignment(SWT.CENTER);
		txtLoginToTv.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		txtLoginToTv.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		txtLoginToTv.setText("Login to TV Traveller");
		
		Label lblUsername = new Label(shlTvTraveller, SWT.NONE);
		lblUsername.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblUsername.setBounds(39, 78, 70, 20);
		lblUsername.setText("Username:");
		
		Label lblPassword = new Label(shlTvTraveller, SWT.NONE);
		lblPassword.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblPassword.setBounds(39, 114, 70, 20);
		lblPassword.setText("Password:");
		
		lblError = new Label(shlTvTraveller, SWT.NONE);
		lblError.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblError.setText("Version: pre-alpha");
		lblError.setBounds(39, 153, 262, 36);
		
		txtUsername = new Text(shlTvTraveller, SWT.BORDER);
		txtUsername.setBounds(115, 75, 186, 26);
		
		txtPassword = new Text(shlTvTraveller, SWT.BORDER);
		txtPassword.setBounds(114, 111, 187, 26);
		
		Button btnLogin = new Button(shlTvTraveller, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String username = txtUsername.getText();
				String password = txtPassword.getText();
				
				// Need to check with the database correct username and password combination
				if(!(Main.login(username, password))) 
				{
					lblError.setText("No such user or invalid password!");
				}
				else
				{
					shlTvTraveller.close();
					shlTvTraveller.dispose();
					
					MainDisplay maindisplay = new MainDisplay();
					maindisplay.open();							
				}
			}
		});
		btnLogin.setBounds(40, 195, 90, 30);
		btnLogin.setText("Login");
		
		Button btnRegister = new Button(shlTvTraveller, SWT.NONE);
		btnRegister.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlTvTraveller.close();
				shlTvTraveller.dispose();
				
				Registration r = new Registration(); 
				r.open();
			}
		});
		btnRegister.setBounds(220, 195, 90, 30);
		btnRegister.setText("Register");

	}
	
	// For setting errors 
	public void setError(String str)
	{
		lblError.setText(str);
	}
}
