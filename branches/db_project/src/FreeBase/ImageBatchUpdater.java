package FreeBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.ConnectionManager;

public class ImageBatchUpdater {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		ConnectionManager.openConnection();
		
		PreparedStatement statement = ConnectionManager.conn.prepareStatement("SELECT media_id, image FROM Media WHERE image!=\"\" AND media_id NOT IN (SELECT media_id FROM ImageBinaries)");
		ResultSet result_set = statement.executeQuery();
		
		ImageUploader iu = new ImageUploader(ConnectionManager.conn);
		while(result_set!=null && result_set.next()) {
			iu.add(result_set.getInt("media_id"), result_set.getString("image"));
		}
		result_set.close();
		statement.close();
		iu.perform();
		
		
		ConnectionManager.closeConnection();
	}

}
