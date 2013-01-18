package gui;

import java.sql.SQLException;
import java.sql.Statement;

import objects.Comment;
import objects.User;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

import db.CommentRetriever;
import db.ConnectionManager;
import db.UserRetriever;

public class WriteComment {

	protected Shell shlWriteAComment;
	private Text text;
	private int currentLocationId;
	private User currentUser;
	private MainDisplay mainDisplay;
	
	public WriteComment() {}
	
	public WriteComment(User user, int locationId, MainDisplay mainDisplay) {
		super();
		this.currentUser = user;
		this.currentLocationId = locationId;
		this.mainDisplay = mainDisplay;
	}

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
		shlWriteAComment.setImage(SWTResourceManager.getImage(WriteComment.class, "/gui/tv.png"));
		shlWriteAComment.setSize(364, 300);
		shlWriteAComment.setText("Write a comment");
		
		text = new Text(shlWriteAComment, SWT.BORDER);
		text.setBounds(10, 10, 326, 188);
		
		Button btnSubmit = new Button(shlWriteAComment, SWT.NONE);
		btnSubmit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Statement stmt;
				
				try
				{
					String parsedText = text.getText().replaceAll("'", ""); // cant allow '
					
					if (text.getText().isEmpty() || parsedText.isEmpty()) {
						return;
					}
									
					// Otherwise we successfully registered.
					stmt = ConnectionManager.conn.createStatement();
					System.out.println("Attempting to do:\nINSERT INTO Comments (user_id, location_id, comment, upvotes, downvotes, is_check_in, date) "+
					                   "VALUES ("+currentUser.getID()+", "+currentLocationId+", '"+parsedText+"', 0, 0, 1, '"+new java.sql.Timestamp(new java.util.Date().getTime())+"')");
					
					// TODO: add time to the date.. its only 2013-01-18 without any time...
					stmt.executeUpdate("INSERT INTO Comments (user_id, location_id, comment, upvotes, downvotes, is_check_in, date) "+
					                   "VALUES ("+currentUser.getID()+", "+currentLocationId+", '"+parsedText+"', 0, 0, 1, '"+new java.sql.Timestamp(new java.util.Date().getTime())+"')");
					
					// Get the current comment_id. (which is maximal because comment_id is auto-incrementing)
					Comment c = new CommentRetriever().retrieveFirst(ConnectionManager.conn.prepareStatement("SELECT * FROM Comments WHERE comment_id = "+
					"( SELECT MAX(comment_id) FROM Comments)"));
					
					int commentId = c.getId();
					
					stmt.executeUpdate("INSERT INTO CommentOfUser (comment_id, user_id, vote) "+
			                   "VALUES ("+commentId+", "+currentUser.getID()+", 0)");
								
					// closing
					stmt.close();

					shlWriteAComment.close();
					shlWriteAComment.dispose();
					mainDisplay.loadCommentsByLocationId(currentLocationId);
									
				}
				catch (SQLException e)
				{
					System.out.println("ERROR executeQuery in WriteComment - " + e.toString());
					java.lang.System.exit(0); 
					return;
				}
			}
		});
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
