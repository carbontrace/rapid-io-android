package io.rapid;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


abstract class FilterGroup implements Filter {
	Set<Filter> filters = new HashSet<>();


	public FilterGroup(Filter... filters) {
		this.filters.addAll(Arrays.asList(filters));
	}


	protected abstract String getKeyword();


	@Override
	public String toJson() throws JSONException {
		JSONObject root = new JSONObject();
		JSONArray jsonFilters = new JSONArray();
		for(Filter filter : filters) {
			jsonFilters.put(new JSONObject(filter.toJson()));
		}
		root.put(getKeyword(), jsonFilters);
		return root.toString();
	}


	public void add(Filter filter) {
		filters.add(filter);
	}

}