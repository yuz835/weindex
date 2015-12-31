package ca.weindex.dao;

import java.util.List;

import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;

public interface OfferDao {
	public Offer getOffer(int id);
	
	public boolean insertOffer(Offer offer);
	
	public boolean updateOffer(Offer offer);
	
	public boolean deleteOffer(int id);
	
	public SearchResult<Offer> getOfferByShopId(int shopId, Pagination page);
	public SearchResult<Offer> getOpenOfferByShopId(int shopId, Pagination page);
	public SearchResult<Offer> getRecentOfferByShopId(int shopId, Pagination page);
	
	public List<Offer> searchOffer(String keyword);
	
	public SearchResult<Offer> getOfferByLabelId(int labelId, Pagination page);
	public SearchResult<Offer> getOfferByShopLabelId(int shopId, int labelId, Pagination page);
	public SearchResult<Offer> getOfferByShopShopLabelId(int shopId, int shopLabelId, Pagination page);
	public SearchResult<Offer> getOpenOfferByShopLabelId(int shopId, int labelId, Pagination page);
	public SearchResult<Offer> getOpenOfferByShopShopLabelId(int shopId, int shopLabelId, Pagination page);
	
	public boolean addOfferVisitNum(int id);
	public boolean updateOfferCommentNum(int id);

	public void updateOfferPosByShopId(int shopId);
	public boolean updateOfferPos(Offer offer);
}
