package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class YouTube {
	private String videoName;
	private Display display ;
	protected Shell shlYouTube;
	public YouTube(String videoName){
		this.videoName=videoName;
	}
	


	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shlYouTube.open();
		shlYouTube.layout();
		while (!shlYouTube.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {

		shlYouTube = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shlYouTube.setText("Video Trailer");
		shlYouTube.setSize(450, 430);
		YouTubeWidget widget=new YouTubeWidget(shlYouTube, "youtube.html",videoName);
		widget.init();
		widget.getBrowser().setBounds(display.getBounds());

	}
}
