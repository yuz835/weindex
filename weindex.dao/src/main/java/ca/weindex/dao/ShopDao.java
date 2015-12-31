package ca.weindex.dao;

import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;

public interface ShopDao {
	public Shop getShop(int id);
	public Shop getShopByUserId(int userId);
	
	public Shop getShopByShopName(String shopName);
	
	public boolean createShop(Shop shop);
	
	public boolean updateShop(Shop shop);
	
	public SearchResult<Shop> getShopByLabelId(int labelId, Pagination page);

	public boolean refreshShop(int id);
	public boolean refreshShopOfferLabels(int id);
	public boolean refreshShopBlogLabels(int id);
	
	public boolean addShopVisitNum(int id);
	public boolean updateShopCommentNum(int id);
}
