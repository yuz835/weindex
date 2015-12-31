package ca.weindex.common.model;

import java.util.ArrayList;
import java.util.List;

public class SearchResult<T> extends Pagination {
	private List<T> list = new ArrayList<T>();

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
