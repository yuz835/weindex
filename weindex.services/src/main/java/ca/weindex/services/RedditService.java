package ca.weindex.services;

import ca.weindex.common.model.Reddit;
import ca.weindex.common.model.RedditComment;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.User;

public interface RedditService {
	public Reddit getReddit(int id);
	public SearchResult<Reddit> getRedditByUserId(int userId, Pagination page);
	public SearchResult<Reddit> getRedditByLabelId(int labelId, Pagination page);
		
	public boolean insertReddit(Reddit reddit);
	public boolean updateReddit(Reddit reddit);
	public boolean deleteReddit(int id);
	
	public SearchResult<RedditComment> getRedditComments(int redditId, Pagination page);
	public boolean insertRedditComment(RedditComment comment, User creator);
	public boolean updateRedditComment(RedditComment comment);
	public boolean deleteRedditComment(int id);
	public RedditComment getRedditComment(int id);
	
	public boolean addRedditVisitNum(int id);
	public boolean addRedditCommentNum(int id);

	public boolean likeReddit(int id);
	public boolean dislikeReddit(int id);
	public boolean voteMinusOneReddit(int id);
	public boolean votePlusOneReddit(int id);
	public boolean favReddit(int id);
	public boolean disfavReddit(int id);
	public boolean likeRedditComment(int id);
	public boolean dislikeRedditComment(int id);


	
}
