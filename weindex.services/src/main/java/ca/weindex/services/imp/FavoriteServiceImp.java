package ca.weindex.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.FavBlogMessage;
import ca.weindex.common.model.FavOfferMessage;
import ca.weindex.common.model.FavShopMessage;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;
import ca.weindex.common.model.UserFavoriteBlog;
import ca.weindex.common.model.UserFavoriteOffer;
import ca.weindex.common.model.UserFavoriteShop;
import ca.weindex.common.model.Reddit;
import ca.weindex.common.model.UserLikedReddit;
import ca.weindex.common.model.UserDislikedReddit;
import ca.weindex.common.model.UserFavoriteReddit;
import ca.weindex.common.model.UserLikedComment;
import ca.weindex.common.model.UserDislikedComment;
import ca.weindex.common.enums.LabelTypeEnum;
import ca.weindex.common.model.ShopComment;
import ca.weindex.common.model.BlogComment;
import ca.weindex.common.model.OfferComment;
import ca.weindex.common.model.RedditComment;
import ca.weindex.dao.FavoriteDao;
import ca.weindex.services.BlogService;
import ca.weindex.services.FavoriteService;
import ca.weindex.services.MessageService;
import ca.weindex.services.OfferService;
import ca.weindex.services.ShopService;
import ca.weindex.services.UserService;
import ca.weindex.services.RedditService;


@Service
public class FavoriteServiceImp implements FavoriteService {
	@Autowired
	private ShopService shopService;

	@Autowired
	private OfferService offerService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private UserService userService;

	@Autowired
	private RedditService redditService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private FavoriteDao dao;

	public boolean addFavShop(UserFavoriteShop shop) {
		int shopId = shop.getShopId();
		Shop shopEntity = shopService.getShop(shopId);
		if (shopEntity == null) {
			return false;
		}
		boolean b = dao.addFavShop(shop);
		if (b) {
			try {
				// send a message
				FavShopMessage msg = new FavShopMessage();
				msg.setSourceId(-1);
				msg.setSource("Admin");

				User shopOwner = userService.getUserById(shopEntity.getUserId());
				msg.setDestId(shopOwner.getId());
				msg.setDest(shopOwner.getUserName());

				msg.setShopId(shopId);
				msg.setUserId(shop.getUserId());
				User sender = userService.getUserById(shop.getUserId());
				msg.setUserName(sender.getUserName());
				messageService.insertMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public boolean addFavOffer(UserFavoriteOffer offer) {
		int offerId = offer.getOfferId();
		Offer offerEntity = offerService.getOffer(offerId);
		if (offerEntity == null) {
			return false;
		}
		boolean b = dao.addFavOffer(offer);
		if (b) {
			try {
				// send a message
				FavOfferMessage msg = new FavOfferMessage();
				msg.setSourceId(-1);
				msg.setSource("Admin");
				Shop shop = shopService.getShop(offerEntity.getShopId());
				User shopOwner = userService.getUserById(shop.getUserId());
				msg.setDestId(shopOwner.getId());
				msg.setDest(shopOwner.getUserName());

				msg.setOfferId(offerId);
				msg.setOfferName(offerEntity.getName());
				msg.setUserId(shop.getUserId());
				User sender = userService.getUserById(shop.getUserId());
				msg.setUserName(sender.getUserName());
				messageService.insertMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public boolean addFavBlog(UserFavoriteBlog blog) {
		int blogId = blog.getBlogId();
		Blog blogEntity = blogService.getBlog(blogId);
		if (blogEntity == null) {
			return false;
		}
		boolean b = dao.addFavBlog(blog);
		if (b) {
			try {
				// send a message
				FavBlogMessage msg = new FavBlogMessage();
				msg.setSourceId(-1);
				msg.setSource("Admin");
				Shop shop = shopService.getShop(blogEntity.getShopId());
				User shopOwner = userService.getUserById(shop.getUserId());
				msg.setDestId(shopOwner.getId());
				msg.setDest(shopOwner.getUserName());

				msg.setBlogId(blogId);
				msg.setBlogTitle(blogEntity.getTitle());
				msg.setUserId(shop.getUserId());
				User sender = userService.getUserById(shop.getUserId());
				msg.setUserName(sender.getUserName());
				messageService.insertMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public boolean addLikedReddit(UserLikedReddit reddit) {
		int redditId = reddit.getRedditId();
		Reddit redditEntity = redditService.getReddit(redditId);
		if (redditEntity == null) {
			return false;
		}
		boolean b = dao.addLikedReddit(reddit);
		// TODO: add likedredditmsg
		return b;
	};

	public boolean addDislikedReddit(UserDislikedReddit reddit) {
		int redditId = reddit.getRedditId();
		Reddit redditEntity = redditService.getReddit(redditId);
		if (redditEntity == null) {
			return false;
		}
		boolean b = dao.addDislikedReddit(reddit);
		// TODO: add msg
		return b;
	};

	public boolean addFavReddit(UserFavoriteReddit reddit) {
		int redditId = reddit.getRedditId();
		Reddit redditEntity = redditService.getReddit(redditId);
		if (redditEntity == null) {
			return false;
		}
		boolean b = dao.addFavReddit(reddit);
		// TODO: add msg
		return b;
	};

	public boolean addLikedComment(UserLikedComment comment) {
		int commentId = comment.getCommentId();
		if(comment.getType() == LabelTypeEnum.BLOG.getType()) {
			BlogComment commentEntity = blogService.getBlogComment(commentId);
			if (commentEntity == null) {
				return false;
			}
		} else if(comment.getType() == LabelTypeEnum.SHOP.getType()) {
			ShopComment commentEntity = shopService.getShopComment(commentId);
			if (commentEntity == null) {
				return false;
			}
		} else if(comment.getType() == LabelTypeEnum.OFFER.getType()) {
			OfferComment commentEntity = offerService.getOfferComment(commentId);
			if (commentEntity == null) {
				return false;
			}
		} else if(comment.getType() == LabelTypeEnum.REDDIT.getType()) {
			RedditComment commentEntity = redditService.getRedditComment(commentId);
			if (commentEntity == null) {
				return false;
			}
		} else {
			// what's this??
			return false;
		}; 
		
		boolean b = dao.addLikedComment(comment);
		// TODO: add msg
		return b;
	};
	
	public boolean addDislikedComment(UserDislikedComment comment) {
		int commentId = comment.getCommentId();
		if(comment.getType() == LabelTypeEnum.BLOG.getType()) {
			BlogComment commentEntity = blogService.getBlogComment(commentId);
			if (commentEntity == null) {
				return false;
			}
		} else if(comment.getType() == LabelTypeEnum.SHOP.getType()) {
			ShopComment commentEntity = shopService.getShopComment(commentId);
			if (commentEntity == null) {
				return false;
			}
		} else if(comment.getType() == LabelTypeEnum.OFFER.getType()) {
			OfferComment commentEntity = offerService.getOfferComment(commentId);
			if (commentEntity == null) {
				return false;
			}
		} else if(comment.getType() == LabelTypeEnum.REDDIT.getType()) {
			RedditComment commentEntity = redditService.getRedditComment(commentId);
			if (commentEntity == null) {
				return false;
			}
		} else {
			// what's this??
			return false;
		}; 
		
		boolean b = dao.addDislikedComment(comment);
		// TODO: add msg
		return b;
	};


	public boolean delFavShop(UserFavoriteShop shop) {
		return dao.delFavShop(shop);
	}

	public boolean delFavOffer(UserFavoriteOffer offer) {
		return dao.delFavOffer(offer);
	}

	public boolean delFavBlog(UserFavoriteBlog blog) {
		return dao.delFavBlog(blog);
	}


	public boolean delLikedReddit(UserLikedReddit reddit) {
		return dao.delLikedReddit(reddit);
	};

	public boolean delDislikedReddit(UserDislikedReddit reddit) {
		return dao.delDislikedReddit(reddit);
	};

	public boolean delFavReddit(UserFavoriteReddit reddit) {
		return dao.delFavReddit(reddit);
	};

	public boolean delLikedComment(UserLikedComment comment) {
		return dao.delLikedComment(comment);
	};

	public boolean delDislikedComment(UserDislikedComment comment) {
		return dao.delDislikedComment(comment);
	};

	public boolean updateFavShop(UserFavoriteShop shop) {
		return dao.updateFavShop(shop);
	}

	public boolean updateFavOffer(UserFavoriteOffer offer) {
		return dao.updateFavOffer(offer);
	}

	public boolean updateFavBlog(UserFavoriteBlog blog) {
		return dao.updateFavBlog(blog);
	}

	public boolean updateLikedReddit(UserLikedReddit reddit) {
		return dao.updateLikedReddit(reddit);
	};

	public boolean updateDislikedReddit(UserDislikedReddit reddit) {
		return dao.updateDislikedReddit(reddit);
	};

	public boolean updateFavReddit(UserFavoriteReddit reddit) {
		return dao.updateFavReddit(reddit);
	
	};

	public boolean checkFavShop(UserFavoriteShop shop) {
		return dao.checkFavShop(shop);
	}

	public boolean checkFavOffer(UserFavoriteOffer offer) {
		return dao.checkFavOffer(offer);
	}

	public boolean checkFavBlog(UserFavoriteBlog blog) {
		return dao.checkFavBlog(blog);
	}

	public boolean checkLikedReddit(UserLikedReddit reddit) {
		return dao.checkLikedReddit(reddit);
	};

	public boolean checkDislikedReddit(UserDislikedReddit reddit) {
		return dao.checkDislikedReddit(reddit);
	
	};

	public boolean checkFavReddit(UserFavoriteReddit reddit) {
		return dao.checkFavReddit(reddit);
	};

	public boolean checkLikedComment(UserLikedComment comment) {
		return dao.checkLikedComment(comment);
	
	};

	public boolean checkDislikedComment(UserDislikedComment comment) {
		return dao.checkDislikedComment(comment);
	};

	public SearchResult<Offer> getUserFavoriteOffer(int userId, boolean isAll, Pagination page) {
		if (isAll) {
			return dao.getUserFavoriteOffer(userId, page);
		} else {
			return dao.getUserPublicFavoriteOffer(userId, page);
		}
	}

	public SearchResult<Blog> getUserFavoriteBlog(int userId, boolean isAll, Pagination page) {
		if (isAll) {
			return dao.getUserFavoriteBlog(userId, page);
		} else {
			return dao.getUserPublicFavoriteBlog(userId, page);
		}
	}

	public SearchResult<Shop> getUserFavoriteShop(int userId, boolean isAll, Pagination page) {
		if (isAll) {
			return dao.getUserFavoriteShop(userId, page);
		} else {
			return dao.getUserPublicFavoriteShop(userId, page);
		}
	}

	public SearchResult<Reddit> getUserLikedReddit(int userId, Pagination page) {
		return dao.getUserLikedReddit(userId, page);
	};

	public SearchResult<Reddit> getUserDislikedReddit(int userId, Pagination page) {
		return dao.getUserDislikedReddit(userId, page);
	};

	public SearchResult<Reddit> getUserFavReddit(int userId, Pagination page) {
		return dao.getUserFavoriteReddit(userId, page);
	};

	public SearchResult<RedditComment> getUserLikedRedditComment(int userId, Pagination page) {
		return dao.getUserLikedRedditComment(userId, page);
	};

	public SearchResult<RedditComment> getUserDislikedRedditComment(int userId, Pagination page) {
		return dao.getUserLikedRedditComment(userId, page);
	};



}
