package db;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.ResultSet;

public class ImageRetriever extends RetrieverBase<byte[]> {

	@Override
	protected String getTableNames() {
		return "ImageBinaries";
	}

	@Override
	protected String getJoinLine() {
		return "";
	}
	
	private final static String[] DEFFIELDS = {"media_id"}; 

	@Override
	protected String[] getDefaultFields() {
		return DEFFIELDS;
	}

	@Override
	protected String[] getFieldForGeneralSearch() {
		return null;
	}

	@Override
	protected byte[] makeObject(ResultSet result_set) {
		try {
			InputStream is = result_set.getBlob("image").getBinaryStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			
			int nRead;
			byte[] data = new byte[65535];
			while ((nRead = is.read(data, 0, data.length)) != -1) {
				  buffer.write(data, 0, nRead);
			}
			
			buffer.flush();
			
			return buffer.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
