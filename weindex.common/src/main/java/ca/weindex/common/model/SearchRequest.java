package ca.weindex.common.model;

public class SearchRequest {
	public Object value;
	public Pagination page;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Pagination getPage() {
		return page;
	}

	public void setPage(Pagination page) {
		this.page = page;
	}
}
