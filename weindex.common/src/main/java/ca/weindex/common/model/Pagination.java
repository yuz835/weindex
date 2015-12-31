package ca.weindex.common.model;

public class Pagination {
	private int pageNum = 1;
	private int pageSize = 16;
	private int beginIndex;
	private int endIndex;
	private int totalNum;

	public Pagination() {
		init();
	}

	private void init() {
		if (pageNum < 1) {
			pageNum = 1;
		}
		beginIndex = (pageNum - 1) * pageSize;
		endIndex = pageNum * pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		if (pageNum < 1) {
			pageNum = 1;
		}
		this.pageNum = pageNum;
		init();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		init();
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getTotalPage() {
		if (totalNum <= 0) {
			return 0;
		}
		if (totalNum % pageSize == 0) {
			return totalNum / pageSize;
		} else {
			return (totalNum / pageSize) + 1;
		}
	}
	
	public int getNextPage() {
		int totalPage = getTotalPage();
		int nextPage = pageNum + 1;
		if (nextPage > totalPage) {
			nextPage = totalPage;
		}
		return nextPage;
	}
	
	public int getPreviousPage() {
		int previousPage = pageNum - 1;
		if (previousPage < 1) {
			previousPage = 1;
		}
		return previousPage;
	}
}
