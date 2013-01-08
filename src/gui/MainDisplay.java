package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import com.roots.swtmap.MapWidget;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.CLabel;

public class MainDisplay {

	protected Shell shell;
	protected Display display;
	private Text txtSearch;
	private Table table;

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
		display = Display.getDefault();
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
		shell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(1150, 635);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmAccount = new MenuItem(menu, SWT.CASCADE);
		mntmAccount.setText("Account");
		
		Menu menu_1 = new Menu(mntmAccount);
		mntmAccount.setMenu(menu_1);
		
		MenuItem mntmProfile = new MenuItem(menu_1, SWT.NONE);
		mntmProfile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Profile profile = new Profile();
				profile.open();
			}
		});
		mntmProfile.setText("Profile");
		
		MenuItem mntmLogout = new MenuItem(menu_1, SWT.NONE);
		mntmLogout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
				shell.dispose();
				
				Login login = new Login();
				login.open();
			}
		});
		mntmLogout.setText("Logout");
		
		Group grpSearch = new Group(shell, SWT.NONE);
		FormData fd_grpSearch = new FormData();
		fd_grpSearch.bottom = new FormAttachment(0, 230);
		fd_grpSearch.right = new FormAttachment(0, 570);
		fd_grpSearch.top = new FormAttachment(0);
		fd_grpSearch.left = new FormAttachment(0, 10);
		grpSearch.setLayoutData(fd_grpSearch);
		grpSearch.setText("Search");
		
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
		FormData fd_grpDetails = new FormData();
		fd_grpDetails.bottom = new FormAttachment(0, 230);
		fd_grpDetails.right = new FormAttachment(0, 1122);
		fd_grpDetails.top = new FormAttachment(0);
		fd_grpDetails.left = new FormAttachment(0, 576);
		grpDetails.setLayoutData(fd_grpDetails);
		grpDetails.setText("Details");
		
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
		lblDetails9.setBounds(10, 200, 510, 20);
		
		
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 64);
		fd_composite.right = new FormAttachment(0, 64);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		
		Group grpMap = new Group(shell, SWT.NONE);
		FormData fd_grpMap = new FormData();
		fd_grpMap.bottom = new FormAttachment(0, 555);
		fd_grpMap.right = new FormAttachment(0, 570);
		fd_grpMap.top = new FormAttachment(0, 230);
		fd_grpMap.left = new FormAttachment(0, 10);
		grpMap.setLayoutData(fd_grpMap);
		grpMap.setText("Map");
		
		//Composite compositeMap = new Composite(grpMap, SWT.NONE);
		MapWidget map = new MapWidget(grpMap, SWT.NONE, new Point(9500,6500), 6);
		map.setBounds(10, 24, 540, 291);
		
		Group grpComments = new Group(shell, SWT.NONE);
		FormData fd_grpComments = new FormData();
		fd_grpComments.top = new FormAttachment(grpMap, 0, SWT.TOP);
		fd_grpComments.left = new FormAttachment(grpDetails, 0, SWT.LEFT);
		fd_grpComments.bottom = new FormAttachment(grpMap, 0, SWT.BOTTOM);
		fd_grpComments.right = new FormAttachment(100, -10);
		grpComments.setLayoutData(fd_grpComments);
		
		Label lblNewLabel = new Label(grpComments, SWT.CENTER);
		lblNewLabel.setLocation(10, 20);
		lblNewLabel.setSize(158, 37);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
		lblNewLabel.setText("Comments");
		
		table = new Table(grpComments, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setBounds(10, 59, 538, 256);
		
		TableColumn tblclmnUsername = new TableColumn(table, SWT.NONE);
		tblclmnUsername.setWidth(64);
		tblclmnUsername.setText("User");
		
		TableColumn tblclmnUpvotes = new TableColumn(table, SWT.NONE);
		tblclmnUpvotes.setWidth(69);
		tblclmnUpvotes.setText("Upvotes");
		
		TableColumn tblclmnDownvotes = new TableColumn(table, SWT.NONE);
		tblclmnDownvotes.setWidth(89);
		tblclmnDownvotes.setText("Downvotes");
		
		TableColumn tblclmnComment = new TableColumn(table, SWT.NONE);
		tblclmnComment.setWidth(333);
		tblclmnComment.setText("Comment");
		
		Button btnAddComment = new Button(grpComments, SWT.NONE);
		btnAddComment.setBounds(284, 20, 141, 37);
		btnAddComment.setText("Add Comment");
		
		Button btnNewButton = new Button(grpComments, SWT.NONE);
		btnNewButton.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_up_black.png"));
		btnNewButton.setBounds(442, 20, 41, 37);
		
		Button btnNewButton_1 = new Button(grpComments, SWT.NONE);
		btnNewButton_1.setImage(SWTResourceManager.getImage(MainDisplay.class, "/gui/thumbs_down_black.png"));
		btnNewButton_1.setBounds(489, 20, 41, 37);

	}
}
