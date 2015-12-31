package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Reddit;
import ca.weindex.common.model.RedditComment;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.UserFavoriteBlog;
import ca.weindex.common.model.UserFavoriteOffer;
import ca.weindex.common.model.UserFavoriteShop;
import ca.weindex.common.model.UserLikedReddit;
import ca.weindex.common.model.UserDislikedReddit;
import ca.weindex.common.model.UserFavoriteReddit;
import ca.weindex.common.model.UserLikedComment;
import ca.weindex.common.model.UserDislikedComment;
import ca.weindex.dao.FavoriteDao;

public class FavoriteDaoImp extends SqlMapClientDaoSupport implements FavoriteDao {

	public boolean addFavShop(UserFavoriteShop shop) {
		int i = getSqlMapClientTemplate().update("addFavShop", shop);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean addFavOffer(UserFavoriteOffer offer) {
		int i = getSqlMapClientTemplate().update("addFavOffer", offer);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean addFavBlog(UserFavoriteBlog blog) {
		int i = getSqlMapClientTemplate().update("addFavBlog", blog);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}


	public boolean addLikedReddit(UserLikedReddit reddit) {
		int i = getSqlMapClientTemplate().update("addLikedReddit", reddit);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean addDislikedReddit(UserDislikedReddit reddit) {
		int i = getSqlMapClientTemplate().update("addDislikedReddit", reddit);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean addFavReddit(UserFavoriteReddit reddit) {
		int i = getSqlMapClientTemplate().update("addFavReddit", reddit);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean addLikedComment(UserLikedComment comment) {
		int i = getSqlMapClientTemplate().update("addLikedComment", comment);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean addDislikedComment(UserDislikedComment comment) {
		int i = getSqlMapClientTemplate().update("addDislikedComment", comment);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean delFavShop(UserFavoriteShop shop) {
		int i = getSqlMapClientTemplate().delete("delFavShop", shop);
		if (i > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean delFavOffer(UserFavoriteOffer offer) {
		int i = getSqlMapClientTemplate().delete("delFavOffer", offer);
		if (i > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean delFavBlog(UserFavoriteBlog blog) {
		int i = getSqlMapClientTemplate().delete("delFavBlog", blog);
		if (i > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean delLikedReddit(UserLikedReddit reddit) {
		int i = getSqlMapClientTemplate().delete("delLikedReddit", reddit);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean delDislikedReddit(UserDislikedReddit reddit) {
		int i = getSqlMapClientTemplate().delete("delDislikedReddit", reddit);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean delFavReddit(UserFavoriteReddit reddit) {
		int i = getSqlMapClientTemplate().delete("delFavReddit", reddit);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean delLikedComment(UserLikedComment comment) {
		int i = getSqlMapClientTemplate().delete("delLikedComment", comment);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean delDislikedComment(UserDislikedComment comment) {
		int i = getSqlMapClientTemplate().delete("delDislikedComment", comment);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	};


	public boolean updateFavShop(UserFavoriteShop shop) {
		int i = getSqlMapClientTemplate().update("updateFavShop", shop);
		if (i > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean updateFavOffer(UserFavoriteOffer offer) {
		int i = getSqlMapClientTemplate().update("updateFavOffer", offer);
		if (i > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean updateFavBlog(UserFavoriteBlog blog) {
		int i = getSqlMapClientTemplate().update("updateFavBlog", blog);
		if (i > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean updateLikedReddit(UserLikedReddit reddit) {
		int i = getSqlMapClientTemplate().update("updateLikedReddit", reddit);
		if (i > 0) {
			return true;
		} else {
			return false;
		}

	};

	public boolean updateDislikedReddit(UserDislikedReddit reddit) {
		int i = getSqlMapClientTemplate().update("updateDislikedReddit", reddit);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean updateFavReddit(UserFavoriteReddit reddit) {
		int i = getSqlMapClientTemplate().update("updateFavoriteReddit", reddit);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean checkFavShop(UserFavoriteShop shop) {
		List<?> list = getSqlMapClientTemplate().queryForList("checkFavShop", shop);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean checkFavOffer(UserFavoriteOffer offer) {
		List<?> list = getSqlMapClientTemplate().queryForList("checkFavOffer", offer);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean checkFavBlog(UserFavoriteBlog blog) {
		List<?> list = getSqlMapClientTemplate().queryForList("checkFavBlog", blog);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean checkLikedReddit(UserLikedReddit reddit) {
		List<?> list = getSqlMapClientTemplate().queryForList("checkLikedReddit", reddit);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean checkDislikedReddit(UserDislikedReddit reddit) {
		List<?> list = getSqlMapClientTemplate().queryForList("checkDislikedReddit", reddit);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean checkFavReddit(UserFavoriteReddit reddit) {
		List<?> list = getSqlMapClientTemplate().queryForList("checkFavReddit", reddit);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean checkLikedComment(UserLikedComment comment) {
		List<?> list = getSqlMapClientTemplate().queryForList("checkLikedComment", comment);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	};

	public boolean checkDislikedComment(UserDislikedComment comment) {
		List<?> list = getSqlMapClientTemplate().queryForList("checkDislikedComment", comment);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	};


	public SearchResult<Offer> getUserFavoriteOffer(int userId, Pagination page) {
		SearchResult<Offer> sr = new SearchResult<Offer>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countUserFavOffer", userId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(userId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getUserFavOffer", request);
			if (list != null && !list.isEmpty()) {
				List<Offer> result = new ArrayList<Offer>();
				for (Object o : list) {
					result.add((Offer) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Blog> getUserFavoriteBlog(int userId, Pagination page) {
		SearchResult<Blog> sr = new SearchResult<Blog>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countUserFavBlog", userId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(userId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getUserFavBlog", request);
			if (list != null && !list.isEmpty()) {
				List<Blog> result = new ArrayList<Blog>();
				for (Object o : list) {
					result.add((Blog) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Shop> getUserFavoriteShop(int userId, Pagination page) {
		SearchResult<Shop> sr = new SearchResult<Shop>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countUserFavShop", userId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(userId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getUserFavShop", request);
			if (list != null && !list.isEmpty()) {
				List<Shop> result = new ArrayList<Shop>();
				for (Object o : list) {
					result.add((Shop) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Offer> getUserPublicFavoriteOffer(int userId, Pagination page) {
		SearchResult<Offer> sr = new SearchResult<Offer>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countUserPublicFavOffer", userId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(userId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getUserPublicFavOffer", request);
			if (list != null && !list.isEmpty()) {
				List<Offer> result = new ArrayList<Offer>();
				for (Object o : list) {
					result.add((Offer) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Blog> getUserPublicFavoriteBlog(int userId, Pagination page) {
		SearchResult<Blog> sr = new SearchResult<Blog>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countUserPublicFavBlog", userId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(userId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getUserPublicFavBlog", request);
			if (list != null && !list.isEmpty()) {
				List<Blog> result = new ArrayList<Blog>();
				for (Object o : list) {
					result.add((Blog) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Shop> getUserPublicFavoriteShop(int userId, Pagination page) {
		SearchResult<Shop> sr = new SearchResult<Shop>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countUserPublicFavShop", userId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(userId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getUserPublicFavShop", request);
			if (list != null && !list.isEmpty()) {
				List<Shop> result = new ArrayList<Shop>();
				for (Object o : list) {
					result.add((Shop) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}


	public SearchResult<Reddit> getUserLikedReddit(int userId, Pagination page) {
		SearchResult<Reddit> sr = new SearchResult<Reddit>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countUserLikedReddit", userId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(userId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getUserLikedReddit", request);
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
	
	};

	public SearchResult<Reddit> getUserDislikedReddit(int userId, Pagination page) {
		SearchResult<Reddit> sr = new SearchResult<Reddit>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countUserDislikedReddit", userId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(userId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getUserDislikedReddit", request);
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
	};

	public SearchResult<Reddit> getUserFavoriteReddit(int userId, Pagination page) {
		SearchResult<Reddit> sr = new SearchResult<Reddit>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countUserFavReddit", userId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(userId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getUserFavReddit", request);
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
	};

	public SearchResult<RedditComment> getUserLikedRedditComment(int userId, Pagination page) {
		SearchResult<RedditComment> sr = new SearchResult<RedditComment>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countUserLikedRedditComment", userId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(userId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getUserLikedRedditComment", request);
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
	};


	public SearchResult<RedditComment> getUserDislikedRedditComment(int userId, Pagination page) {
		SearchResult<RedditComment> sr = new SearchResult<RedditComment>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countUserDislikedRedditComment", userId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(userId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getUserDislikedRedditComment", request);
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
	};




}
