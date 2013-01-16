package db;

public class Field {
	private String _fieldname;
	private int _type; //from java.sql.Types

	public Field(String fieldname, int sqltype) {
		_fieldname = fieldname;
		_type = sqltype;
	}
	
	public String getFieldname() {
		return _fieldname;
	}

	public int getType() {
		return _type;
	}
}
