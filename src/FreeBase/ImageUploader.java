package FreeBase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

/**
 * @author maayan
 * This class will take a list of partial image urls from freebase,
 * reads them from HTTP and uploads them to the db.
 */
public class ImageUploader {
	private static final int MAX_BLOB_SIZE_BYTES = 65535;
	private Connection _connection;
	private boolean flgAutoCommitChanged = false;
	private Map<Integer, String> _list = new HashMap<Integer, String>();
	
	private static final int IMAGE_BATCH_SIZE = 10; //The number of images processed each wave
	private static final int EXTRA_THREADS = IMAGE_BATCH_SIZE + 1;// +1 for uploading to db

	public ImageUploader(Connection db_connection) {
		_connection = db_connection;
	}

	public void addFile(int media_id,String filePath){
		try {
			final byte[] image = performImageLoading(new FileInputStream(filePath));
			Map<Integer, Future<byte[]>> mockResults = new HashMap<Integer, Future<byte[]>>(1);
			mockResults.put(media_id, new Future<byte[]>() {

				@Override
				public boolean cancel(boolean mayInterruptIfRunning) {
					return false;
				}

				@Override
				public byte[] get() throws InterruptedException,
						ExecutionException {
					return image;
				}

				@Override
				public byte[] get(long timeout, TimeUnit unit)
						throws InterruptedException, ExecutionException,
						TimeoutException {
					return image;
				}

				@Override
				public boolean isCancelled() {
					return false;
				}

				@Override
				public boolean isDone() {
					return true;
				}
			});
			performDBInsertion(mockResults);
			if (!_connection.getAutoCommit()) _connection.commit();
		} catch (Exception e) {
			System.err.println("Error loading image to DB.");
			e.printStackTrace();
		}
	}
	
	public void add(int media_id, String image_url) {
		_list.put(media_id, image_url);
	}
	
	public void perform() {
		if (_list.isEmpty()) return;
		
		try {
			if (_connection.getAutoCommit()) {
				_connection.setAutoCommit(false);
				flgAutoCommitChanged = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ExecutorService service = Executors.newFixedThreadPool(EXTRA_THREADS);		
		while(!_list.isEmpty()) {
			final Map<Integer, Future<byte[]>> results = new HashMap<Integer, Future<byte[]>>(IMAGE_BATCH_SIZE);
			for (int i=0;i<IMAGE_BATCH_SIZE && !_list.isEmpty();++i) {
				final Entry<Integer, String> curr = _list.entrySet().iterator().next();
				final String curr_url = curr.getValue();
				final Integer curr_id = curr.getKey();
				_list.remove(curr_id);
				
				final Callable<byte[]> task = new Callable<byte[]>() {
					
					@Override
					public byte[] call() throws Exception {
						return performImageLoading(curr_url);
					}
				};
				results.put(curr_id, service.submit(task));
			}
			
			service.submit(new Runnable() {
				
				@Override
				public void run() {
					performDBInsertion(results);
				}
			});
		}
		
		service.shutdown();
		while(!service.isTerminated())
			;
		
		if (flgAutoCommitChanged) {
			try {
				_connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	final int IMAGE_BUFFER_SIZE = 65536; 
	private byte[] performImageLoading(String curr_url) throws Exception {
		final URL url = new URL("http://img.freebase.com/api/trans/image_thumb"+curr_url+"?maxheight=200&mode=fit&maxwidth=150");
		InputStream is = url.openStream();
		return performImageLoading(is);
	}

	private byte[] performImageLoading(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		int nRead;
		byte[] data = new byte[IMAGE_BUFFER_SIZE];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
		}
		
		buffer.flush();
		
		final byte[] loaded_image = buffer.toByteArray();
		
		if (loaded_image.length>MAX_BLOB_SIZE_BYTES) {
			return convertToSmaller(loaded_image);
		}
		return loaded_image;
	}
	
	private static byte[] convertToSmaller(byte[] loaded_image) throws IOException {
		System.out.println("Transcoding now image of size " + loaded_image.length/1024 + " kb."); //TODO remove
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream(65536);
		ImageData idata = new ImageData(new ByteArrayInputStream(loaded_image));
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[]{idata};
		loader.save(buffer, SWT.IMAGE_PNG);
		buffer.flush();
		System.out.println("Finished transcoding now image of size " + loaded_image.length/1024 + " kb to size " + buffer.size()/1024 + " kb."); //TODO remove
		return buffer.toByteArray();
	}

	private synchronized void performDBInsertion(Map<Integer, Future<byte[]>> results) {
		PreparedStatement statement;
		try {
			statement = _connection.prepareStatement("INSERT INTO ImageBinaries (media_id, image) VALUES (?, ?)");
		} catch (SQLException e) {
			System.err.println("Unable to create a prepared statement.");
			e.printStackTrace();
			return;
		}
		
		for (Entry<Integer, Future<byte[]>> entry : results.entrySet()) {
			final Future<byte[]> future = entry.getValue();
			final Integer media_id = entry.getKey();
			byte[] result;
			try {
				result = future.get();
			} catch (Exception e) {
				System.err.println("Unable to get thread results for id " + media_id);
				e.printStackTrace();
				continue;
			}
			
			try {
			statement.clearParameters();
			statement.setInt(1, media_id);
			statement.setBlob(2, new ByteArrayInputStream(result));
			statement.execute();
			} catch (SQLException e) {
				System.err.println("Error setting and executing prepared statement for " + media_id);
				e.printStackTrace();
			}
		}
		
		try {
			if (!_connection.getAutoCommit()) _connection.commit();
		} catch (SQLException e) {
			System.err.println("Error commiting changes to DB.");
			e.printStackTrace();
		}
	}
}
