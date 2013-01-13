package gui;

import main.Main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
public class Login {

	protected Shell shlTvTraveler;
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
		shlTvTraveler.open();
		shlTvTraveler.layout();
		while (!shlTvTraveler.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlTvTraveler = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shlTvTraveler.setImage(SWTResourceManager.getImage(Login.class, "/gui/tv.png"));
		shlTvTraveler.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		shlTvTraveler.setMinimumSize(new Point(360, 280));
		shlTvTraveler.setSize(472, 291);
		shlTvTraveler.setText("TV Traveler");
		shlTvTraveler.setLayout(null);
		
		//SWT.CLOSE | SWT.TITLE | SWT.MIN
		txtLoginToTv = new Label(shlTvTraveler, SWT.WRAP | SWT.HORIZONTAL | SWT.SHADOW_IN | SWT.CENTER);
		txtLoginToTv.setImage(SWTResourceManager.getImage(Login.class, "/gui/title.png"));
		txtLoginToTv.setBounds(10, 10, 436, 88);
		txtLoginToTv.setAlignment(SWT.CENTER);
		txtLoginToTv.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		txtLoginToTv.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		
		Label lblUsername = new Label(shlTvTraveler, SWT.NONE);
		lblUsername.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblUsername.setBounds(85, 104, 70, 20);
		lblUsername.setText("Username:");
		
		Label lblPassword = new Label(shlTvTraveler, SWT.NONE);
		lblPassword.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblPassword.setBounds(85, 140, 70, 20);
		lblPassword.setText("Password:");
		
		lblError = new Label(shlTvTraveler, SWT.NONE);
		lblError.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblError.setText("Version: pre-alpha");
		lblError.setBounds(85, 179, 262, 26);
		
		txtUsername = new Text(shlTvTraveler, SWT.BORDER);
		txtUsername.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent arg0) {
				if (arg0.detail==SWT.TRAVERSE_RETURN) tryToLogin();
			}
		});
		txtUsername.setBounds(161, 104, 186, 26);
		
		txtPassword = new Text(shlTvTraveler, SWT.PASSWORD | SWT.BORDER);
		txtPassword.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent arg0) {
				if (arg0.detail==SWT.TRAVERSE_RETURN) tryToLogin();
			}
		});
		txtPassword.setBounds(160, 137, 187, 26);
		
		Button btnLogin = new Button(shlTvTraveler, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				tryToLogin();
			}
		});
		btnLogin.setBounds(85, 211, 90, 30);
		btnLogin.setText("Login");
		
		Button btnRegister = new Button(shlTvTraveler, SWT.NONE);
		btnRegister.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Registration r = new Registration(); 
				r.open();
			}
		});
		btnRegister.setBounds(274, 211, 90, 30);
		btnRegister.setText("Register");
		shlTvTraveler.setTabList(new Control[]{txtUsername, txtPassword, btnLogin, btnRegister});

	}
	
	private void tryToLogin() {
		String username = txtUsername.getText();
		String password = txtPassword.getText();
		
		// Need to check with the database correct username and password combination
		if(!(Main.login(username, password))) 
		{
			lblError.setText("No such user or invalid password!");
		}
		else
		{
			shlTvTraveler.close();
			shlTvTraveler.dispose();
			
			MainDisplay maindisplay = new MainDisplay();
			maindisplay.open();							
		}
	}
	
	// For setting errors 
	public void setError(String str)
	{
		lblError.setText(str);
	}
}
