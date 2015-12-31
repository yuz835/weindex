package ca.weindex.dao;

import ca.weindex.common.model.Reddit;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;

public interface RedditDao {
	public Reddit getReddit(int id);
	public SearchResult<Reddit> getRedditByUserId(int userId, Pagination page);
	public boolean insertReddit(Reddit reddit);
	public boolean updateReddit(Reddit reddit);
	public boolean deleteReddit(int id);
	public SearchResult<Reddit> getRedditByLabelId(int labelId, Pagination page);
	
	public boolean addRedditCommentNum(int id);
	public boolean addRedditVisitNum(int id);
	public boolean likeReddit(int id);
	public boolean dislikeReddit(int id);
	public boolean favReddit(int id);
	public boolean disfavReddit(int id);
	
}
