package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;

import com.roots.swtmap.MapBrowser;
import com.roots.swtmap.MapWidget;

public class MainDisplay {

	protected Shell shell;
	private Text txtSearch;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainDisplay window = new MainDisplay();
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
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1150, 635);
		shell.setText("SWT Application");
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmAccount = new MenuItem(menu, SWT.CASCADE);
		mntmAccount.setText("Account");
		
		Menu menu_1 = new Menu(mntmAccount);
		mntmAccount.setMenu(menu_1);
		
		MenuItem mntmLogout = new MenuItem(menu_1, SWT.NONE);
		mntmLogout.setText("Logout");
		
		Group grpSearch = new Group(shell, SWT.NONE);
		grpSearch.setText("Search");
		grpSearch.setBounds(10, 0, 560, 230);
		
		txtSearch = new Text(grpSearch, SWT.BORDER);
		txtSearch.setBounds(10, 28, 421, 26);
		
		Button btnSearch = new Button(grpSearch, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnSearch.setBounds(441, 26, 109, 30);
		btnSearch.setText("Search");
		
		Group group = new Group(grpSearch, SWT.NONE);
		group.setBounds(10, 60, 153, 160);
		
		Button btnRadioTv = new Button(group, SWT.RADIO);
		btnRadioTv.setBounds(10, 20, 111, 20);
		btnRadioTv.setText("Television");
		
		Button btnRadioFilm = new Button(group, SWT.RADIO);
		btnRadioFilm.setBounds(10, 55, 111, 20);
		btnRadioFilm.setText("Film");
		
		Button btnRadioLocation = new Button(group, SWT.RADIO);
		btnRadioLocation.setBounds(10, 91, 111, 20);
		btnRadioLocation.setText("Location");
		
		Button btnRadioUsername = new Button(group, SWT.RADIO);
		btnRadioUsername.setBounds(10, 127, 111, 20);
		btnRadioUsername.setText("Username");
		
		List list = new List(grpSearch, SWT.BORDER);
		list.setBounds(185, 72, 365, 148);
		
		Group grpDetails = new Group(shell, SWT.NONE);
		grpDetails.setText("Details");
		grpDetails.setBounds(576, 0, 546, 230);
		
		Label lblDetails1 = new Label(grpDetails, SWT.BORDER);
		lblDetails1.setBounds(10, 25, 247, 20);
		
		Label lblDetails2 = new Label(grpDetails, SWT.BORDER);
		lblDetails2.setBounds(273, 25, 247, 20);
		
		Label lblDetails3 = new Label(grpDetails, SWT.BORDER);
		lblDetails3.setBounds(10, 64, 247, 20);
		
		Label lblDetails4 = new Label(grpDetails, SWT.BORDER);
		lblDetails4.setBounds(273, 64, 247, 20);
		
		Label lblDetails5 = new Label(grpDetails, SWT.BORDER);
		lblDetails5.setBounds(10, 110, 247, 20);
		
		Label lblDetails6 = new Label(grpDetails, SWT.BORDER);
		lblDetails6.setBounds(273, 110, 247, 20);
		
		Label lblDetails7 = new Label(grpDetails, SWT.BORDER);
		lblDetails7.setBounds(10, 158, 247, 20);
		
		Label lblDetails8 = new Label(grpDetails, SWT.BORDER);
		lblDetails8.setBounds(273, 158, 247, 20);
		
		Label lblDetails9 = new Label(grpDetails, SWT.BORDER);
		lblDetails9.setBounds(10, 200, 247, 20);
		
		Label lblDetails10 = new Label(grpDetails, SWT.BORDER);
		lblDetails10.setBounds(273, 200, 247, 20);
		
		Group grpPhotosAndComments = new Group(shell, SWT.NONE);
		grpPhotosAndComments.setText("Photos and Comments");
		grpPhotosAndComments.setBounds(576, 230, 546, 325);
		
		Group grpMap = new Group(shell, SWT.NONE);
		grpMap.setText("Map");
		grpMap.setBounds(10, 230, 560, 325);
		
		//Composite compositeMap = new Composite(grpMap, SWT.NONE);
		MapWidget map = new MapWidget(grpMap, SWT.NONE, new Point(9500,6500), 6);
		map.setBounds(10, 24, 540, 291);

	}
}
