package db;

import java.util.List;

public class LimitsToken<T> {
	private List<T> _loadedresults;
	private final RetrieverBase<T> _ret;
	private int _curr_index = 0;
	private boolean _reached_end_in_db;
	private String[] _search_field_values;

	public LimitsToken(List<T> results, RetrieverBase<T> retriever, String[] search_field_values) {
		_loadedresults = results;
		_ret = retriever;
		_reached_end_in_db = _loadedresults.size()<RetrieverBase.MAX_ROWS_PER_QUERY;
		_search_field_values = search_field_values;
	}
	
	public List<T> curr() {
		final int max_items = RetrieverBase.MAX_ROWS_PER_QUERY;
		if (_loadedresults.size()<=max_items) {
			return _loadedresults;
		}
		
		int toindex = Math.min(_loadedresults.size(), _curr_index+max_items);
		
		return _loadedresults.subList(_curr_index, toindex);
	}
	
	//returns is there more left to prev afterwards
	public boolean prev() {
		if (_curr_index>0) {
			_curr_index-=RetrieverBase.MAX_ROWS_PER_QUERY;
		}
		return _curr_index>0;
	}
	
	//returns is there more left to next afterwards
	public boolean next() {
		final int new_curr = _curr_index+RetrieverBase.MAX_ROWS_PER_QUERY;
		if (new_curr<_loadedresults.size()-1) {
			_curr_index = new_curr;
			return ((new_curr+RetrieverBase.MAX_ROWS_PER_QUERY)<(_loadedresults.size()-1)) || !_reached_end_in_db;
		}
		
		//then new_curr isn't in results list
		
		if (_reached_end_in_db) { //Already checked in DB and found out we were at the end
			return false;
		}
		
		T last_item = _loadedresults.get(_loadedresults.size()-1);
		List<T> next_results = _ret.searchNextForToken(_search_field_values, last_item);
		_loadedresults.addAll(next_results);
		_curr_index = new_curr;
		return !(_reached_end_in_db=(next_results.size()<RetrieverBase.MAX_ROWS_PER_QUERY));
	}
	
	public boolean isEmpty() {
		return _loadedresults.isEmpty();
	}
	
	public boolean getForwardable() {
		final int new_curr = _curr_index+RetrieverBase.MAX_ROWS_PER_QUERY;
		if (new_curr<_loadedresults.size()-1) return true;
		return !_reached_end_in_db;
	}
	
	public boolean getBackable() {
		return _curr_index>0;
	}
}
