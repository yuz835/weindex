package ca.weindex.services.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.weindex.common.model.Address;
import ca.weindex.common.model.Label;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.ShopComment;
import ca.weindex.common.model.ShopCommentMessage;
import ca.weindex.common.model.ShopLabel;
import ca.weindex.common.model.User;
import ca.weindex.dao.LabelDao;
import ca.weindex.dao.MessageDao;
import ca.weindex.dao.ShopCommentDao;
import ca.weindex.dao.ShopDao;
import ca.weindex.dao.ShopLabelDao;
import ca.weindex.dao.UserDao;
import ca.weindex.search.LuceneSearch;
import ca.weindex.search.ShopIndexBuilder;
import ca.weindex.services.ShopService;

@Service
public class ShopServiceImp implements ShopService {
	@Autowired
	private ShopDao shopDao;

	@Autowired
	private ShopCommentDao shopCommentDao;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private LabelDao labelDao;

	@Autowired
	private ShopLabelDao shopLabelDao;

	@Autowired
	private ShopIndexBuilder indexBuilder;

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private LuceneSearch luceneSearch;

	public boolean openShop(Shop shop) {
		if (shop.getDisplayName() == null || shop.getDisplayName().length() == 0) {
			shop.setDisplayName(shop.getName());
		}
		boolean b = shopDao.createShop(shop);
		if (b && shop.getId() > 0) {
			try {
				indexBuilder.addShopIndex(shop);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public Shop getShop(String shopName) {
		Shop shop = shopDao.getShopByShopName(shopName);
		if (shop != null) {
			// get user info
			int userId = shop.getUserId();
			User user = userDao.getUserById(userId);
			shop.setOwner(user);

			
			// get labels
			List<Label> labelList = labelDao.getLabel();
			this.updateLabel(shop, labelList);
			
			List<ShopLabel> shopLabelList = shopLabelDao.getShopLabelByShopId(shop.getId());
			shop.setShopLabelList(shopLabelList);
		}
		return shop;
	}

	public boolean updateDesc(int userId, String desc) {
		Shop shop = shopDao.getShopByUserId(userId);
		if (shop != null) {
			shop.setDesc(desc);
			boolean b = shopDao.updateShop(shop);
			if (b) {
				updateShopIndex(shop);
			}
			return b;
		}
		return false;
	}

	public boolean updateDisplayName(int userId, String displayName) {
		Shop shop = shopDao.getShopByUserId(userId);
		if (shop != null) {
			shop.setDisplayName(displayName);
			boolean b = shopDao.updateShop(shop);
			if (b) {
				updateShopIndex(shop);
			}
			return b;
		}
		return false;
	}

	public boolean updateLabel(int userId, String label) {
		Shop shop = shopDao.getShopByUserId(userId);
		if (shop != null) {
			shop.setLabel(label);
			boolean b = shopDao.updateShop(shop);
			if (b) {
				updateShopIndex(shop);
			}
			return b;
		}
		return false;
	}

	public Shop getShop(int id) {
		Shop shop = shopDao.getShop(id);
		if (shop != null) {
			// get user info
			int userId = shop.getUserId();
			User user = userDao.getUserById(userId);
			shop.setOwner(user);

			// get labels
			List<Label> labelList = labelDao.getLabel();
			this.updateLabel(shop, labelList);
			
			List<ShopLabel> shopLabelList = shopLabelDao.getShopLabelByShopId(id);
			shop.setShopLabelList(shopLabelList);
		}
		return shop;
	}

	public boolean updateAddress(int userId, Address address) {
		Shop shop = shopDao.getShopByUserId(userId);
		if (shop != null) {
			shop.updateAddress(address);
			boolean b = shopDao.updateShop(shop);
			if (b) {
				updateShopIndex(shop);
			}
			return b;
		}
		return false;
	}

	public Shop getShopWithDetail(int id) {
		Shop shop = shopDao.getShop(id);
		if (shop != null) {
			// get user info
			int userId = shop.getUserId();
			User user = userDao.getUserById(userId);
			shop.setOwner(user);
			// get labels
			List<Label> labelList = labelDao.getLabel();
			this.updateLabel(shop, labelList);
			
			List<ShopLabel> shopLabelList = shopLabelDao.getShopLabelByShopId(id);
			shop.setShopLabelList(shopLabelList);
		}
		return shop;
	}

	private void updateShopIndex(Shop shop) {
		if (shop.getId() > 0) {
			try {
				indexBuilder.updateShopIndex(shop);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public SearchResult<Shop> searchShop(String keyword, Pagination page) {
		if (keyword == null || keyword.trim().length() == 0) {
			// return offerDao.searchOffer("");
			keyword = "";
		}

		// return offerDao.searchOffer(keyword.trim());
		return luceneSearch.searchShop(keyword, page);
	}

	public SearchResult<Shop> searchShopByRegion(int startLati, int endLati, int startLongi, int endLongi,
			Pagination page) {
		// System.out.println(startLati + " : " + endLati + " : " + startLongi + " : " + endLongi);
		return luceneSearch.searchShopByRegion(startLati, endLati, startLongi, endLongi, page);
	}

	public boolean updateShop(Shop shop) {
		boolean b = shopDao.updateShop(shop);
		if (b) {
			updateShopIndex(shop);
		}
		return b;
	}

	public SearchResult<ShopComment> getShopComments(int shopId, Pagination page) {
		return shopCommentDao.getShopCommentList(shopId, page);
	}

	public boolean insertShopComment(ShopComment comment, User creator, User blogAuthor) {
		boolean b = shopCommentDao.createComment(comment);
		if (b) {
			// send a message
			ShopCommentMessage msg = new ShopCommentMessage();
			msg.setSourceId(creator.getId());
			msg.setSource(creator.getUserName());
			msg.setDestId(blogAuthor.getId());
			msg.setDest(blogAuthor.getUserName());
			msg.setShopId(comment.getShopId());
			msg.setCommentTitle(comment.getTitle());
			try {
				updateShopCommentNum(comment.getShopId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Shop shop = this.getShop(comment.getShopId());
				msg.setShopName(shop.getName());
				msg.setShopTitle(shop.getDisplayName());
				messageDao.insertMessage(msg);
			} catch (Exception e) {
				// do nothing
				e.printStackTrace();
			}
		}
		return b;
	}

	public boolean refreshShop(int id) {
		return shopDao.refreshShop(id);
	}

	public Shop getShopByUserId(int userId) {
		Shop shop = shopDao.getShopByUserId(userId);
		if (shop != null) {
			// get labels
			List<Label> labelList = labelDao.getLabel();
			this.updateLabel(shop, labelList);

			List<ShopLabel> shopLabelList = shopLabelDao.getShopLabelByShopId(shop.getId());
			shop.setShopLabelList(shopLabelList);
		}
		return shop;
	}

	public boolean refreshShopOfferLabels(int id) {
		return shopDao.refreshShopOfferLabels(id);
	}

	public boolean refreshShopBlogLabels(int id) {
		return shopDao.refreshShopBlogLabels(id);
	}
	
	private void updateLabel(Shop shop, List<Label> labelList) {
		// get labels
		String label = shop.getLabel();
		if (label != null) {
			List<Label> list = new ArrayList<Label>();
			list.addAll(labelList);
			shop.setLabelList(popupLabelList(label, list));
		}
		String offerLabels = shop.getOfferLabels();
		if (offerLabels != null) {
			List<Label> list = new ArrayList<Label>();
			list.addAll(labelList);
			shop.setOfferLabelList(popupLabelList(offerLabels, list));
		}
		String blogLabels = shop.getBlogLabels();
		if (blogLabels != null) {
			List<Label> list = new ArrayList<Label>();
			list.addAll(labelList);
			shop.setBlogLabelList(popupLabelList(blogLabels, list));
		}
	}
	
	private List<Label> popupLabelList(String label, List<Label> list) {
		List<Label> result = new ArrayList<Label>();
		if (label != null) {
			String[] labels = label.split(",");
			for (String l : labels) {
				for (int i = 0; i < list.size(); i++) {
					Label lab = list.get(i);
					if (l.equals(Integer.toString(lab.getId()))) {
						result.add(lab);
						list.remove(i);
						break;
					}
				}
			}
		}
		return result;
	}

	public boolean insertShopLabel(ShopLabel label) {
		return shopLabelDao.insertShopLabel(label);
	}

	public boolean updateShopLabel(ShopLabel label) {
		return shopLabelDao.updateShopLabel(label);
	}

	public boolean deleteShopLabel(int id) {
		return shopLabelDao.deleteShopLabel(id);
	}

	public ShopLabel getShopLabel(int id) {
		return shopLabelDao.getShopLabelById(id);
	}
	
	public boolean addShopVisitNum(int id) {
		return shopDao.addShopVisitNum(id);
	}

	public boolean updateShopCommentNum(int id) {
		return shopDao.updateShopCommentNum(id);
	}

	public boolean deleteShopComment(int id) {
		return shopCommentDao.deleteComment(id);
	}

	public ShopComment getShopComment(int id) {
		return shopCommentDao.getShopComment(id);
	}

	public void rebuildShopIndex(int id) {
		try {
			indexBuilder.deleteShopIndex(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Shop shop = getShopWithDetail(id);
		if (shop != null) {
			try {
				indexBuilder.addShopIndex(shop);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
