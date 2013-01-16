package gui;

import java.sql.ResultSet;
import java.sql.SQLException;

import db.ConnectionManager;

@Deprecated
public class SQLStub {
//TODO: this class should be unneeded at the end
	
	public static ResultSet performQuery(String sql) {
		try {
			return ConnectionManager.conn.prepareStatement(sql).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
