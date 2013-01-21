package gui;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Composite;

public class YouTubeWidget {
	private String filePath;
	private Composite parent;
	private Browser browser;
	private String html;
	private boolean loaded;
	private String videoName;

	/**
	 * 
	 * @param parent
	 *            Composite object - will be the parent of the map widget
	 * @param fileName
	 *            map html file path
	 */
	public YouTubeWidget(Composite parent, String filePath,String videoName) {
		this.parent = parent;
		this.filePath = filePath;
		this.videoName=videoName;
	}

	public void init() {
		try {
			browser = new Browser(parent, SWT.NONE);
		} catch (SWTError e) {
			System.err.println("Couldn't instantiate browser");
		}
		loaded = false;
		html = getTextFile(filePath);
		browser.setText(html);
		browser.addProgressListener(new ProgressListener() {
			public void completed(ProgressEvent arg0) {
				loaded = true;
				setVideo(videoName);
			}

			public void changed(ProgressEvent arg0) {
			}
		});
		
		
	}
	
	public boolean setVideo(String name){
		if (isLoaded()) {
			String[] params = { name};
			return browserExecute("startVideo", params);
		}
		return false;
	}
	

	private boolean browserExecute(String funcName, String[] params) {
		return browser.execute(buildCommand(funcName, params));
	}

	private String buildCommand(String funcName, String[] params){
		String paramsStr = "";
		int length = params.length;
		if (length >= 1)
			paramsStr = "\"" + params[0] + "\"";
		for (int i = 1; i < params.length; i++)
			paramsStr += ",\"" + params[i] + "\"";
		String command = funcName + "(" + paramsStr + ");";
		return command;
	}
	
	private static String getTextFile(String file) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Cannot load map file!");
		}
		String content = contentBuilder.toString();
		return content;
	}


	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public Browser getBrowser() {
		return browser;
	}

	public void setFileName(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return filePath;
	}

	public void setParent(Composite parent) {
		this.parent = parent;
	}

	public Composite getParent() {
		return parent;
	}

	public boolean isLoaded() {
		return loaded;
	}

	
}
