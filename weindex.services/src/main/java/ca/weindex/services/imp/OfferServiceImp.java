package ca.weindex.services.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.weindex.common.model.Offer;
import ca.weindex.common.model.OfferComment;
import ca.weindex.common.model.OfferCommentMessage;
import ca.weindex.common.model.OfferImage;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;
import ca.weindex.dao.MessageDao;
import ca.weindex.dao.OfferCommentDao;
import ca.weindex.dao.OfferDao;
import ca.weindex.dao.OfferImageDao;
import ca.weindex.search.LuceneSearch;
import ca.weindex.search.OfferIndexBuilder;
import ca.weindex.services.OfferService;
import ca.weindex.services.ShopService;
import ca.weindex.services.UserService;

@Service
public class OfferServiceImp implements OfferService {
	@Autowired
	private OfferDao offerDao;

	@Autowired
	private OfferImageDao offerImageDao;

	@Autowired
	private OfferCommentDao offerCommentDao;

	@Autowired
	private OfferIndexBuilder indexBuilder;

	@Autowired
	private ShopService shopService;

	@Autowired
	private UserService userService;

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private LuceneSearch luceneSearch;

	public SearchResult<Offer> getOfferByShopId(int shopId, boolean isOpen, Pagination page) {
		if (isOpen) {
			SearchResult<Offer> list = offerDao.getOpenOfferByShopId(shopId, page);
			return list;
		} else {
			SearchResult<Offer> list = offerDao.getOfferByShopId(shopId, page);
			return list;
		}
	}

	public boolean createOffer(Shop shop, Offer offer) {
		boolean b = offerDao.insertOffer(offer);
		if (b && offer.getId() > 0 && offer.getVisible() == 0) {
			try {
				// update shop info
				shopService.refreshShop(shop.getId());
				shopService.refreshShopOfferLabels(shop.getId());
				User user = userService.getUserById(shop.getUserId());
				offer.setUserName(user.getUserName());
				indexBuilder.addOfferIndex(offer, shop);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public Offer getOffer(int id) {
		return offerDao.getOffer(id);
	}

	public OfferImage getOfferImage(String name, String type) {
		return offerImageDao.getOfferImageByNameType(name, type);
	}

	public boolean insertOfferImage(OfferImage image) {
		return offerImageDao.insertOfferImage(image);
	}

	public boolean updateOfferImage(OfferImage image) {
		return offerImageDao.updateOfferImage(image);
	}


	public List<OfferImage> getOfferImageByOfferId(int offerId) {
		return offerImageDao.getOfferImageByOfferId(offerId);
	}

	public boolean updateOffer(Offer offer) {
		boolean b = offerDao.updateOffer(offer);
		if (b && offer.getId() > 0) {
			try {
				if (offer.getVisible() == 0) {
					Shop shop = shopService.getShop(offer.getShopId());
					shopService.refreshShopOfferLabels(shop.getId());
					User user = userService.getUserById(shop.getUserId());
					offer.setUserName(user.getUserName());
					indexBuilder.updateOfferIndex(offer, shop);
				} else {
					// hide the offer
					indexBuilder.deleteOfferIndex(offer.getId());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public SearchResult<Offer> searchOffer(String keyword, Pagination page) {
		if (keyword == null || keyword.trim().length() == 0) {
			// return offerDao.searchOffer("");
			keyword = "";
		}

		// return offerDao.searchOffer(keyword.trim());
		return luceneSearch.searchOffer(keyword, page);
	}

	public boolean deleteOffer(int id, int shopId) {
		boolean b = offerDao.deleteOffer(id);
		if (b) {
			try {
				// update shop info
				shopService.refreshShop(shopId);
				shopService.refreshShopOfferLabels(shopId);
				indexBuilder.deleteOfferIndex(id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public SearchResult<OfferComment> getOfferComments(int offerId, Pagination page) {
		return offerCommentDao.getOfferCommentList(offerId, page);
	}

	public boolean insertOfferComment(OfferComment comment, User creator, User blogAuthor) {
		boolean b = offerCommentDao.createComment(comment);
		if (b) {
			// send a message
			OfferCommentMessage msg = new OfferCommentMessage();
			msg.setSourceId(creator.getId());
			msg.setSource(creator.getUserName());
			msg.setDestId(blogAuthor.getId());
			msg.setDest(blogAuthor.getUserName());
			msg.setOfferId(comment.getOfferId());
			msg.setCommentTitle(comment.getTitle());
			try {
				updateOfferCommentNum(comment.getOfferId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Offer offer = this.getOffer(comment.getOfferId());
				msg.setOfferTitle(offer.getName());
				messageDao.insertMessage(msg);
			} catch (Exception e) {
				// do nothing
				e.printStackTrace();
			}
		}
		return b;
	}

	public SearchResult<Offer> getOfferByShopLabelId(int shopId, int labelId, boolean isOpen, Pagination page) {
		if (isOpen) {
			return offerDao.getOpenOfferByShopLabelId(shopId, labelId, page);
		} else {
			return offerDao.getOfferByShopLabelId(shopId, labelId, page);
		}
	}

	public SearchResult<Offer> getOfferByShopShopLabelId(int shopId, int shopLabelId, boolean isOpen, Pagination page) {
		if (isOpen) {
			return offerDao.getOpenOfferByShopShopLabelId(shopId, shopLabelId, page);
		} else {
			return offerDao.getOfferByShopShopLabelId(shopId, shopLabelId, page);
		}
	}

	public SearchResult<Offer> getOfferByLabelId(int labelId, Pagination page) {
		return offerDao.getOfferByLabelId(labelId, page);
	}

	public boolean updateOfferLogo(int offerId, int offerImageId) {
		Offer offer = getOffer(offerId);
		if (offer == null) {
			return false;
		}
		List<OfferImage> list = offerImageDao.getOfferImageByOfferId(offerId);
		if (list == null || list.isEmpty()) {
			return false;
		}
		List<OfferImage> temp = new ArrayList<OfferImage>();
		OfferImage newOne = null;
		for (OfferImage img : list) {
			if (img.getId() != offerImageId && img.getOfferLogo() != 0) {
				img.setOfferLogo(0);
				temp.add(img);
				continue;
			}
			if (img.getId() == offerImageId) {
				img.setOfferLogo(1);
				newOne = img;
				continue;
			}
		}
		if (newOne == null) {
			return false;
		}
		for (OfferImage img : temp) {
			boolean b = offerImageDao.updateOfferImageLogo(img);
			if (!b) {
				return false;
			}
		}
		boolean b = offerImageDao.updateOfferImageLogo(newOne);
		if (!b) {
			return false;
		}
		String imgUrl = "/offer/image/" + newOne.getName() + "/" + newOne.getType() + "/show.html";
		offer.setImgUrl(imgUrl);
		b = updateOffer(offer);
		return b;
	}

	public boolean deleteOfferImage(int offerImageId) {
		OfferImage image = offerImageDao.getOfferImageById(offerImageId);
		if (image == null || image.getOfferLogo() != 0) {
			return false;
		}
		return offerImageDao.deleteOfferImage(offerImageId);
	}
	
	public OfferImage getOfferImageById(int offerImageId) {
		return offerImageDao.getOfferImageById(offerImageId);
	}

	public boolean addOfferVisitNum(int id) {
		return offerDao.addOfferVisitNum(id);
	}

	public boolean updateOfferCommentNum(int id) {
		return offerDao.updateOfferCommentNum(id);
	}

	public boolean deleteOfferComment(int id) {
		return offerCommentDao.deleteComment(id);
	}

	public OfferComment getOfferComment(int id) {
		return offerCommentDao.getOfferComment(id);
	}

	public boolean topOfferOfShop(Offer offer) {
		offerDao.updateOfferPosByShopId(offer.getShopId());
		return offerDao.updateOfferPos(offer);
	}

	public void rebuildOfferIndex(int offerId) {
		Offer offer = getOffer(offerId);
		if (offer != null) {
			try {
				indexBuilder.deleteOfferIndex(offerId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Shop shop = shopService.getShopWithDetail(offer.getShopId());
			if (shop != null) {
				try {
					User user = userService.getUserById(shop.getUserId());
					offer.setUserName(user.getUserName());
					indexBuilder.addOfferIndex(offer, shop);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
