package ca.weindex.services;

import java.util.List;

import ca.weindex.common.model.Offer;
import ca.weindex.common.model.OfferComment;
import ca.weindex.common.model.OfferImage;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;

public interface OfferService {
	public SearchResult<Offer> getOfferByShopId(int shopId, boolean isOpen, Pagination page);
	public SearchResult<Offer> getOfferByShopLabelId(int shopId, int labelId, boolean isOpen, Pagination page);
	public SearchResult<Offer> getOfferByShopShopLabelId(int shopId, int shopLabelId, boolean isOpen, Pagination page);
	public SearchResult<Offer> getOfferByLabelId(int labelId, Pagination page);
	public boolean createOffer(Shop shop, Offer offer);
	public Offer getOffer(int id);
	public boolean updateOffer(Offer offer);
	public boolean deleteOffer(int id, int shopId);
	
	public OfferImage getOfferImage(String name, String type);
	public boolean insertOfferImage(OfferImage image);
	public boolean updateOfferImage(OfferImage image);
	public List<OfferImage> getOfferImageByOfferId(int offerId);
	
	public SearchResult<OfferComment> getOfferComments(int offerId, Pagination page);
	public boolean insertOfferComment(OfferComment comment, User creator, User blogAuthor);
	public boolean deleteOfferComment(int id);
	public OfferComment getOfferComment(int id);
	
	public SearchResult<Offer> searchOffer(String keyword, Pagination page);
	
	public boolean updateOfferLogo(int offerId, int offerImageId);
	
	public boolean deleteOfferImage(int offerImageId);
	public OfferImage getOfferImageById(int offerImageId);

	public boolean addOfferVisitNum(int id);
	public boolean updateOfferCommentNum(int id);
	
	public boolean topOfferOfShop(Offer offer);
	
	public void rebuildOfferIndex(int offerId);

}
