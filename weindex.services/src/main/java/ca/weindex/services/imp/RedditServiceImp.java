package ca.weindex.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.weindex.common.model.Reddit;
import ca.weindex.common.model.RedditComment;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.User;
import ca.weindex.dao.RedditCommentDao;
import ca.weindex.dao.RedditDao;
import ca.weindex.dao.MessageDao;
import ca.weindex.search.LuceneSearch;
import ca.weindex.services.RedditService;
import ca.weindex.services.UserService;

@Service
public class RedditServiceImp implements RedditService {
	@Autowired
	private RedditDao redditDao;

	@Autowired
	private RedditCommentDao redditCommentDao;

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private UserService userService;
	
	@Autowired
	private LuceneSearch luceneSearch;

	public Reddit getReddit(int id) {
		Reddit reddit = redditDao.getReddit(id);
		return reddit;
	}

	public SearchResult<Reddit> getRedditByUserId(int userId, Pagination page) {
		return redditDao.getRedditByUserId(userId, page);
	}

	public SearchResult<Reddit> getRedditByLabelId(int labelId, Pagination page) {
		return redditDao.getRedditByLabelId(labelId, page);
	}

	public boolean insertReddit(Reddit reddit) {
		if (reddit == null) {
			return false;
		}
		boolean b = redditDao.insertReddit(reddit);
		if (b && reddit.getId() > 0) {
			try {
				// update user info
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public boolean updateReddit(Reddit reddit) {
		if (reddit == null) {
			return false;
		}
		boolean b = redditDao.updateReddit(reddit);
		if (b && reddit.getId() > 0) {
			try {
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public boolean deleteReddit(int id) {
		boolean b = redditDao.deleteReddit(id);
		if (b && id > 0) {
			try {
				// update user info
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}


	public boolean insertRedditComment(RedditComment comment, User creator) {
		boolean b = redditCommentDao.createComment(comment);
		if (b) {
			// TODO send a message
			try {
				addRedditCommentNum(comment.getRedditId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public boolean updateRedditComment(RedditComment comment) {
		return redditCommentDao.updateComment(comment);
	}

	public boolean deleteRedditComment(int id) {
		return redditCommentDao.deleteComment(id);
	}


	public SearchResult<RedditComment> getRedditComments(int redditId, Pagination page) {
		return redditCommentDao.getCommentList(redditId, page);
	}
	
	public boolean addRedditCommentNum(int id) {
		return redditDao.addRedditCommentNum(id);
	}


	public RedditComment getRedditComment(int id) {
		return redditCommentDao.getComment(id);
	}


	public boolean addRedditVisitNum(int id) {
		return redditDao.addRedditVisitNum(id);
	}

	public boolean likeReddit(int id){
		return redditDao.likeReddit(id);
	};

	public boolean dislikeReddit(int id) {
		return redditDao.dislikeReddit(id);
	};

	public boolean favReddit(int id) {
		return redditDao.favReddit(id);
	};
	
	public boolean disfavReddit(int id) {
		return redditDao.disfavReddit(id);
	};

	public boolean voteMinusOneReddit(int id) {
		return redditDao.dislikeReddit(id);
	};
	public boolean votePlusOneReddit(int id) {
		return redditDao.likeReddit(id);
	};

	public boolean likeRedditComment(int id){
		return redditCommentDao.likeRedditComment(id);
	};

	public boolean dislikeRedditComment(int id) {
		return redditCommentDao.dislikeRedditComment(id);
	};

}
