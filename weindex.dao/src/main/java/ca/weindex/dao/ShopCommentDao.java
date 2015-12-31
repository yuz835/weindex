package ca.weindex.dao;

import ca.weindex.common.model.ShopComment;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;

public interface ShopCommentDao {
	public boolean createComment(ShopComment comment);

	public SearchResult<ShopComment> getShopCommentList(int shopId, Pagination page);

	public boolean deleteComment(int id);
	
	public ShopComment getShopComment(int id);
}
