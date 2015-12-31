package ca.weindex.dao;

import ca.weindex.common.model.BlogComment;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;

public interface BlogCommentDao {
	public boolean createComment(BlogComment comment);

	public SearchResult<BlogComment> getBlogCommentList(int blogId, Pagination page);

	public boolean deleteComment(int id);

	public BlogComment getBlogComment(int id);
}
