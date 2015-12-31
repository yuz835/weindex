package ca.weindex.dao;

import java.util.List;

import ca.weindex.common.model.OfferImage;

public interface OfferImageDao {
	public OfferImage getOfferImageById(int id);

	public List<OfferImage> getOfferImageByOfferId(int offerId);

	public OfferImage getOfferImageByNameType(String name, String type);

	public boolean insertOfferImage(OfferImage image);
	public boolean updateOfferImage(OfferImage image);
	
	public boolean updateOfferImageLogo(OfferImage image);

	public boolean deleteOfferImage(int id);

}
