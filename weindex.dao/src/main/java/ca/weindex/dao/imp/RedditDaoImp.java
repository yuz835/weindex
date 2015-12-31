package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.Reddit;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.User;
import ca.weindex.dao.RedditDao;

public class RedditDaoImp extends SqlMapClientDaoSupport implements RedditDao {

	public Reddit getReddit(int id) {
		Reddit reddit = (Reddit) getSqlMapClientTemplate().queryForObject("getReddit", id);
		return reddit;
	}

	public SearchResult<Reddit> getRedditByUserId(int userId, Pagination page) {
		SearchResult<Reddit> sr = new SearchResult<Reddit>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countRedditByUserId", userId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(userId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getRedditByUserId", request);
			if (list != null && !list.isEmpty()) {
				List<Reddit> result = new ArrayList<Reddit>();
				for (Object o : list) {
					result.add((Reddit) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public boolean insertReddit(Reddit reddit) {
		int id = (Integer) getSqlMapClientTemplate().insert("insertReddit", reddit);
		if (id > 0) {
			reddit.setId(id);
			return true;
		} else {
			return false;
		}
	}

	public boolean updateReddit(Reddit reddit) {
		int i = getSqlMapClientTemplate().update("updateReddit", reddit);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteReddit(int id) {
		int i = getSqlMapClientTemplate().delete("deleteReddit", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public SearchResult<Reddit> getRedditByLabelId(int labelId, Pagination page) {
		SearchResult<Reddit> sr = new SearchResult<Reddit>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countRedditByLabelId", labelId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(labelId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getRedditByLabelId", request);
			if (list != null && !list.isEmpty()) {
				List<Reddit> result = new ArrayList<Reddit>();
				for (Object o : list) {
					result.add((Reddit) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public boolean addRedditCommentNum(int id) {
		int i = getSqlMapClientTemplate().update("addRedditCommentNum", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}



	public boolean addRedditVisitNum(int id) {
		int i = getSqlMapClientTemplate().update("addRedditVisitNum", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean likeReddit(int id) {
		int i = getSqlMapClientTemplate().update("likeReddit", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
		
	};

	public boolean dislikeReddit(int id) {
		int i = getSqlMapClientTemplate().update("dislikeReddit", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
		
	};

	public boolean favReddit(int id) {
		int i = getSqlMapClientTemplate().update("favReddit", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
		
	};

	public boolean disfavReddit(int id) {
		int i = getSqlMapClientTemplate().update("disfavReddit", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
		
	};



}
