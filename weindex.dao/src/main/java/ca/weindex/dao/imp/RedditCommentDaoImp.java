package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.RedditComment;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.dao.RedditCommentDao;

public class RedditCommentDaoImp extends SqlMapClientDaoSupport implements RedditCommentDao {

	public boolean createComment(RedditComment comment) {
		int i = getSqlMapClientTemplate().update("insertRedditComment", comment);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateComment(RedditComment comment) {
		int i = getSqlMapClientTemplate().update("updateRedditComment", comment);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}


	public boolean deleteComment(int id) {
		int i = getSqlMapClientTemplate().delete("deleteRedditComment", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public SearchResult<RedditComment> getCommentList(int redditId, Pagination page) {
		SearchResult<RedditComment> sr = new SearchResult<RedditComment>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countRedditCommentByRedditId", redditId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(redditId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getRedditCommentByRedditId", request);
			if (list != null && !list.isEmpty()) {
				List<RedditComment> result = new ArrayList<RedditComment>();
				for (Object o : list) {
					result.add((RedditComment) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}

		}
		return sr;
	}

	public RedditComment getComment(int id) {
		RedditComment comment = (RedditComment) getSqlMapClientTemplate().queryForObject("getRedditComment", id);
		return comment;
	}

	public boolean likeRedditComment(int id) {
		int i = getSqlMapClientTemplate().update("likeRedditComment", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	};
	
	public boolean dislikeRedditComment(int id) {
		int i = getSqlMapClientTemplate().update("dislikeRedditComment", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	};


}
