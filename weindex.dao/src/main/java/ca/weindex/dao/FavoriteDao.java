package ca.weindex.dao;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Reddit;
import ca.weindex.common.model.RedditComment;
import ca.weindex.common.model.Pagination;
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



public interface FavoriteDao {
	public boolean addFavShop(UserFavoriteShop shop);

	public boolean addFavOffer(UserFavoriteOffer offer);

	public boolean addFavBlog(UserFavoriteBlog blog);

	public boolean addLikedReddit(UserLikedReddit reddit);

	public boolean addDislikedReddit(UserDislikedReddit reddit);

	public boolean addFavReddit(UserFavoriteReddit reddit);

	public boolean addLikedComment(UserLikedComment comment);

	public boolean addDislikedComment(UserDislikedComment comment);

	public boolean delFavShop(UserFavoriteShop shop);

	public boolean delFavOffer(UserFavoriteOffer offer);

	public boolean delFavBlog(UserFavoriteBlog blog);

	public boolean delLikedReddit(UserLikedReddit reddit);

	public boolean delDislikedReddit(UserDislikedReddit reddit);

	public boolean delFavReddit(UserFavoriteReddit reddit);

	public boolean delLikedComment(UserLikedComment comment);

	public boolean delDislikedComment(UserDislikedComment comment);

	public boolean updateFavShop(UserFavoriteShop shop);

	public boolean updateFavOffer(UserFavoriteOffer offer);

	public boolean updateFavBlog(UserFavoriteBlog blog);

	public boolean updateLikedReddit(UserLikedReddit reddit);

	public boolean updateDislikedReddit(UserDislikedReddit reddit);

	public boolean updateFavReddit(UserFavoriteReddit reddit);

	public boolean checkFavShop(UserFavoriteShop shop);

	public boolean checkFavOffer(UserFavoriteOffer offer);

	public boolean checkFavBlog(UserFavoriteBlog blog);

	public boolean checkLikedReddit(UserLikedReddit reddit);

	public boolean checkDislikedReddit(UserDislikedReddit reddit);

	public boolean checkFavReddit(UserFavoriteReddit reddit);

	public boolean checkLikedComment(UserLikedComment comment);

	public boolean checkDislikedComment(UserDislikedComment comment);

	public SearchResult<Offer> getUserFavoriteOffer(int userId, Pagination page);

	public SearchResult<Blog> getUserFavoriteBlog(int userId, Pagination page);

	public SearchResult<Shop> getUserFavoriteShop(int userId, Pagination page);

	public SearchResult<Offer> getUserPublicFavoriteOffer(int userId, Pagination page);

	public SearchResult<Blog> getUserPublicFavoriteBlog(int userId, Pagination page);

	public SearchResult<Shop> getUserPublicFavoriteShop(int userId, Pagination page);

	public SearchResult<Reddit> getUserLikedReddit(int userId, Pagination page);

	public SearchResult<Reddit> getUserDislikedReddit(int userId, Pagination page);

	public SearchResult<Reddit> getUserFavoriteReddit(int userId, Pagination page);

	public SearchResult<RedditComment> getUserLikedRedditComment(int userId, Pagination page);

	public SearchResult<RedditComment> getUserDislikedRedditComment(int userId, Pagination page);


}
