package gui;

import main.Main;

import objects.User;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Profile {

	protected Shell shlProfile;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Profile window = new Profile();
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
		shlProfile.open();
		shlProfile.layout();
		while (!shlProfile.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		User currentUser;
		
		currentUser = Main.getCurrentUser();

		shlProfile = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shlProfile.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		shlProfile.setSize(275, 452);
		
		Label lblUsername = new Label(shlProfile, SWT.NONE);
		lblUsername.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblUsername.setBounds(10, 46, 83, 20);
		lblUsername.setText("Username:");
		
		Label lblUserName = new Label(shlProfile, SWT.BORDER);
		lblUserName.setBounds(99, 46, 150, 20);
		
		Label lblNewLabel_1 = new Label(shlProfile, SWT.NONE);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblNewLabel_1.setBounds(10, 82, 83, 20);
		lblNewLabel_1.setText("Password:");
		
		Label lblPassword = new Label(shlProfile, SWT.BORDER);
		lblPassword.setBounds(99, 82, 150, 20);
		
		Label lblUpvotes = new Label(shlProfile, SWT.NONE);
		lblUpvotes.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblUpvotes.setBounds(10, 118, 83, 20);
		lblUpvotes.setText("Upvotes:");
		
		Label lblUpvotes_1 = new Label(shlProfile, SWT.NONE);
		lblUpvotes_1.setBounds(99, 118, 70, 20);
		
		Label lblNewLabel_4 = new Label(shlProfile, SWT.NONE);
		lblNewLabel_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblNewLabel_4.setBounds(10, 155, 83, 20);
		lblNewLabel_4.setText("Downvotes:");
		
		Label lblDownvotes = new Label(shlProfile, SWT.BORDER);
		lblDownvotes.setBounds(99, 155, 70, 20);
		
		Label lblAdmin = new Label(shlProfile, SWT.NONE);
		lblAdmin.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblAdmin.setBounds(10, 193, 83, 20);
		lblAdmin.setText("Admin:");
		
		Label lblIsAdmin = new Label(shlProfile, SWT.BORDER);
		lblIsAdmin.setBounds(99, 193, 70, 20);
		
		Group grpBadges = new Group(shlProfile, SWT.NONE);
		grpBadges.setText("Badges");
		grpBadges.setBounds(10, 229, 239, 125);
		
		Button btnOk = new Button(shlProfile, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlProfile.close();
				shlProfile.dispose();
			}
		});
		btnOk.setBounds(63, 376, 140, 30);
		btnOk.setText("Ok");
		
		Label lblProfile = new Label(shlProfile, SWT.NONE);
		lblProfile.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		lblProfile.setForeground(SWTResourceManager.getColor(SWT.COLOR_INFO_FOREGROUND));
		lblProfile.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblProfile.setAlignment(SWT.CENTER);
		lblProfile.setBounds(10, 10, 249, 30);
		lblProfile.setText("User Profile");
		
		if(currentUser != null)
		{
			shlProfile.setText("Profile of "+currentUser.getUsername());
			
			lblUserName.setText(currentUser.getUsername());
			lblPassword.setText(currentUser.getPassword());
			lblUpvotes.setText(String.valueOf(currentUser.getUpvotes()));
			lblDownvotes.setText(String.valueOf(currentUser.getDownvotes()));
			lblIsAdmin.setText((currentUser.isAdmin()?"Yes":"No"));
			
			// TODO: Badges...
		}

	}
}
