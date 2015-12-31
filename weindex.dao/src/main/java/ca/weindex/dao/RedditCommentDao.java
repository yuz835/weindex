package ca.weindex.dao;

import ca.weindex.common.model.RedditComment;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;

public interface RedditCommentDao {
	public boolean createComment(RedditComment comment);

	public SearchResult<RedditComment> getCommentList(int redditId, Pagination page);

	public boolean updateComment(RedditComment comment);

	public boolean deleteComment(int id);

	public RedditComment getComment(int id);

	public boolean likeRedditComment(int id);

	public boolean dislikeRedditComment(int id);
	
};
