package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.BlogComment;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.dao.BlogCommentDao;

public class BlogCommentDaoImp extends SqlMapClientDaoSupport implements BlogCommentDao {

	public boolean createComment(BlogComment comment) {
		int i = getSqlMapClientTemplate().update("insertBlogComment", comment);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteComment(int id) {
		int i = getSqlMapClientTemplate().delete("deleteBlogComment", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public SearchResult<BlogComment> getBlogCommentList(int blogId, Pagination page) {
		SearchResult<BlogComment> sr = new SearchResult<BlogComment>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countBlogCommentByBlogId", blogId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(blogId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getBlogCommentByBlogId", request);
			if (list != null && !list.isEmpty()) {
				List<BlogComment> result = new ArrayList<BlogComment>();
				for (Object o : list) {
					result.add((BlogComment) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}

		}
		return sr;
	}

	public BlogComment getBlogComment(int id) {
		BlogComment comment = (BlogComment) getSqlMapClientTemplate().queryForObject("getBlogComment", id);
		return comment;
	}

}
