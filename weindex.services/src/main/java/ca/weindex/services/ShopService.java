package ca.weindex.services;

import ca.weindex.common.model.Address;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.ShopComment;
import ca.weindex.common.model.ShopLabel;
import ca.weindex.common.model.User;

public interface ShopService {
	public boolean openShop(Shop shop);
	
	public Shop getShop(int id);
	public Shop getShopByUserId(int userId);

	public Shop getShopWithDetail(int id);
	
	public Shop getShop(String shopName);
	
	public boolean updateDisplayName(int userId, String displayName);
	
	public boolean updateDesc(int userId, String desc);
	
	public boolean updateLabel(int userId, String label);
	
	public boolean updateAddress(int userId, Address address);
	
	public SearchResult<ShopComment> getShopComments(int shopId, Pagination page);
	public boolean insertShopComment(ShopComment comment, User creator, User blogAuthor);
	public boolean deleteShopComment(int id);
	public ShopComment getShopComment(int id);
	
	public SearchResult<Shop> searchShop(String keyword, Pagination page);

	public SearchResult<Shop> searchShopByRegion(int startLati, int endLati, int startLongi, int endLongi, Pagination page);
	
	public boolean updateShop(Shop shop);

	public boolean refreshShop(int id);
	public boolean refreshShopOfferLabels(int id);
	public boolean refreshShopBlogLabels(int id);

	public boolean insertShopLabel(ShopLabel label);
	public boolean updateShopLabel(ShopLabel label);
	public boolean deleteShopLabel(int id);
	public ShopLabel getShopLabel(int id);

	public boolean addShopVisitNum(int id);
	public boolean updateShopCommentNum(int id);

	public void rebuildShopIndex(int id);
}
