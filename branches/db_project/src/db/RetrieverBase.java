package db;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import objects.ObjectID;

/**
 * @author maayan
 *	This class is to be inherited and used to implement classes that serve as the main gateway to retrieve information
 *	from the database. It asks for certain information from the inheritors (represented by the abstract methods) and
 *	supplies common functions to retrieve data using the objects package objects as output.
 * @param <T>
 */
public abstract class RetrieverBase<T> {
	
	public final static int MAX_ROWS_PER_QUERY = 200000; //<=0 to disable
	
	/**
	 * @return The table names that are used to retrieve from. For example, the string "foo, bar"
	 */
	protected abstract String getTableNames();
	
	/**
	 * @return The part of the where statement that performs the join for (conceptual) objects that span across tables. For example, for a film the data is both in "film" and "media" tables. 
	 */
	protected abstract String getJoinLine();
	
	/**
	 * @return The fields with which we would commonly want to retrieve a specific item. For example, the username for User. 
	 */
	protected abstract String[] getDefaultFields();
	
	/**
	 * @return A general search here is a "LIKE '%...%'" retrieval, used for a broad search. The fields here will be compared using the "LIKE" operator and ORed.  
	 */
	protected abstract String[] getFieldForGeneralSearch();
	
	/**
	 * @param result_set A result set that is currently pointing to a row
	 * @return a db_project concrete Java object representing the item in the row.
	 */
	protected abstract T makeObject(ResultSet result_set);
	
	/**
	 * @return null or empty string if not to order by anything, the field name to order by otherwise
	 */
	protected String getOrderByField() {
		return null;
	}
	
	/**
	 * @return Returns if OrderBy should be ASC (versus DESC)
	 */
	protected boolean getOrderByAsc() {
		return true;
	}
	
	/**
	 * Tries to fill automatically using reflection the fields of an 'objects' object from the result set.
	 * Works only on items objects with public fields whose name corresponds to the fieldnames in the db.
	 * To be called by the inheritor where appropriate.
	 * @param result_set
	 * @return
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	protected T fillObjectByFields(ResultSet result_set, T instance) throws IllegalArgumentException, IllegalAccessException, SQLException {
		for (java.lang.reflect.Field field : instance.getClass().getFields()) {
			final String fieldname = field.getName();
			field.set(instance, result_set.getObject(fieldname));
		}
		
		return instance;
	}
	
	/**
	 * Makes a select statement with the proper join AND the added where conditions, if any 
	 * @param added_where_conditions added where conditions, if any
	 * @return
	 */
	protected String makeSelect(String added_where_conditions) {
		final StringBuilder sb = new StringBuilder("SELECT * FROM ");
		sb.append(getTableNames());
		
		final String join_line = getJoinLine();
		boolean flgAddedJoin = false;
		
		if (join_line!=null && !join_line.isEmpty()) {
			sb.append(" WHERE ");
			sb.append(join_line);
			flgAddedJoin = true;
		}
		
		if (added_where_conditions!=null && !added_where_conditions.isEmpty()) {
			sb.append(flgAddedJoin?" AND ":" WHERE ");
			sb.append(added_where_conditions);
		}
		
		final String orderbyfield = getOrderByField();
		if (orderbyfield!=null && !orderbyfield.isEmpty()) {
			sb.append(" ORDER BY ");
			sb.append(orderbyfield);
			sb.append(getOrderByAsc()?" ASC":" DESC");
		}
		
		return sb.toString();
	}
	
	/**
	 * Automatically handles the join for the tables.
	 * @param where_clause added where conditions, if any
	 * @return the first object returned for the where_clause, null if non.
	 */
	public T retrieveFirst (String where_clause) {
		List<T> all_results = retrieve(where_clause);
		
		if (all_results.isEmpty()) return null;
		
		return all_results.get(0);
	}
	
	/**
	 * @param statement
	 * @return the first object returned for the query, null if non.
	 */
	public T retrieveFirst (PreparedStatement statement) {
		List<T> all_results = retrieve(statement);
		
		if (all_results.isEmpty()) return null;
		
		return all_results.get(0);
	}
	
	/**
	 * Automatically handles the join for the tables.
	 * @param where_clause added where conditions, if any
	 * @return A list of all the returned objects, empty list if non.
	 */
	public List<T> retrieve(String where_clause) {
		final String select_statement = makeSelect(where_clause);
		try {
			return retrieve(ConnectionManager.conn.prepareStatement(select_statement));
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		} 
	}
	
	/**
	 * @param statement
	 * @return A list of all the returned objects, empty list if non.
	 */
	public List<T> retrieve(PreparedStatement statement) {
		ResultSet result_set = null;
		
		try {
			result_set = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<T> result_list = new LinkedList<T>();
		
		try {
			while(result_set!=null && result_set.next()) {
				result_list.add(makeObject(result_set));
			}
			result_set.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result_list;
	}
	
	PreparedStatement default_statement = null;
	
	private PreparedStatement makeDefaultStatement() throws SQLException {
		String[] default_fields = getDefaultFields();
		StringBuilder full_sql = new StringBuilder();
		for (int i = 0; i < default_fields.length; i++) {
			final String fieldname = default_fields[i];
			full_sql.append(fieldname);
			full_sql.append(" = ?");
			if (i<default_fields.length-1) { //not last one
				full_sql.append(" AND ");
			}
		}
		return ConnectionManager.conn.prepareStatement(makeSelect(full_sql.toString()));
	}
	
	PreparedStatement general_search_statement = null;
	
	private String makeGeneralSearchQuery(boolean above_values) throws SQLException {
		String[] search_fields = getFieldForGeneralSearch();
		StringBuilder full_sql = new StringBuilder();
		for (int i = 0; i < search_fields.length; i++) {
			final String search_field = search_fields[i];
			full_sql.append(search_field);
			full_sql.append(" LIKE ?");
			if (i<search_fields.length-1) { //not last one
				full_sql.append(" OR ");
			}
		}
		
		if (above_values) {
			full_sql.append(" AND ? > ?");
		}
		
		String sql_statement = makeSelect(full_sql.toString());
		
		if (MAX_ROWS_PER_QUERY>0) {
			sql_statement = sql_statement + " LIMIT " + MAX_ROWS_PER_QUERY;
		}
		
		return sql_statement;
	}
	
	private PreparedStatement makeGeneralSearchStatement() throws SQLException {
		return ConnectionManager.conn.prepareStatement(makeGeneralSearchQuery(false));
	}
	
	/**
	 * Used for a common search for the inheritor related elements. This is used for searching over strings,
	 * for example when searching on a name. The fields will be searched with the LIKE operator and the LIKE statements
	 * will be ORed.
	 * The fields (and their order) are determined by the inheritor.
	 * @param field_values
	 * @return A list of all the found objects, empty list if non. 
	 */
	public LimitsToken<T> searchBySearchField(String... field_values) {
		try {
			if (general_search_statement==null) general_search_statement=makeGeneralSearchStatement();
			if (field_values.length!=general_search_statement.getParameterMetaData().getParameterCount()) {
				System.err.println("Unable to search on " + getTableNames() + ", wrong number of parameters given.");
				return null;
			}
			general_search_statement.clearParameters();
			
			for (int i = 0; i < field_values.length; i++) {
				final String field_value = field_values[i];
				general_search_statement.setString(i+1, "%"+field_value+"%");
			}
			
			return new LimitsToken<T>(retrieve(general_search_statement), this, field_values);
		} catch (Exception e) {
			e.printStackTrace();
			return new LimitsToken<T>(new LinkedList<T>(), this, field_values);
		}
	}
	
	List<T> searchNextForToken(String[] field_values, T last_item) {
		try {
			String next_statement = makeGeneralSearchQuery(true);
			String TOKEN = "\r#TOKEN#\r";
			next_statement = next_statement.replace("?", TOKEN);
			
			int i = 0;
			for (; i < field_values.length; i++) {
				final String field_value = field_values[i];
				next_statement = next_statement.replaceFirst(TOKEN, "'%" + field_value.replace("'", "\\'") + "%'");
			}
//			for (int j=0; j < ; i++,j++) {
//				final String field_value = field_values[i];
//				next_statement.setString(i+1, "%"+field_value+"%");
//			}
			ObjectID index_field = null;
			Field field_with_anno = null;
			for (Field field : last_item.getClass().getFields()) {
				index_field = (field_with_anno=field).getAnnotation(ObjectID.class);
				if (index_field!=null) break;
			}
			if (index_field==null) throw new Exception("No annotation of type LimitIndex in " + last_item.getClass().getName());
			String index_field_name = index_field.value().isEmpty()?field_with_anno.getName():index_field.value();
			next_statement = next_statement.replaceFirst(TOKEN, index_field_name);
			Object val = field_with_anno.get(last_item);
			if (val instanceof String) {
				next_statement = next_statement.replaceFirst(TOKEN, "'" + val.toString().replace("'", "\\'") + "'");
			} else {
				next_statement = next_statement.replaceFirst(TOKEN, val.toString());
			}
			System.out.println("Generated next statement: " + next_statement);
			return retrieve(ConnectionManager.conn.prepareStatement(next_statement));
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	/**
	 * Used to retrieve a specific object from the database, identified by the default fields (these are set by the inheritor).
	 * The order of evaluation of the received values is determined by the order of the fields in the inheritor class.
	 * @param default_field_values
	 * @return The object defined by this values, null if non.
	 */
	public T retrieve(Object... default_field_values) {
		try {
			if(default_statement==null) default_statement=makeDefaultStatement();
			if (default_field_values.length!=default_statement.getParameterMetaData().getParameterCount()) {
				System.err.println("Unable to retrieve from " + getTableNames() + ", wrong number of parameters given.");
				return null;
			}
			
			default_statement.clearParameters();
			
			for (int i = 0; i < default_field_values.length; i++) {
				final Object val = default_field_values[i];
				default_statement.setObject(i+1, val);//numbering for parameters starts from 1...
			}
			
			return retrieveFirst(default_statement);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
