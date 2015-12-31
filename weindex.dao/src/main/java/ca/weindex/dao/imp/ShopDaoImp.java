package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.dao.ShopDao;

public class ShopDaoImp extends SqlMapClientDaoSupport implements ShopDao {

	public Shop getShop(int id) {
		Shop result = (Shop) getSqlMapClientTemplate().queryForObject(
				"getShop", id);
		if (result != null) {
			result.setOfferNum(getShopOfferNum(result.getId()));
		}
		return result;
	}

	public Shop getShopByUserId(int userId) {
		Shop result = (Shop) getSqlMapClientTemplate().queryForObject(
				"getShopByUserId", userId);
		if (result != null) {
			result.setOfferNum(getShopOfferNum(result.getId()));
		}
		return result;
	}

	public boolean createShop(Shop shop) {
		int id = (Integer) getSqlMapClientTemplate().insert("insertShop", shop);
		if (id > 0) {
			shop.setId(id);
			return true;
		} else {
			return false;
		}
	}

	public Shop getShopByShopName(String shopName) {
		Shop result = (Shop) getSqlMapClientTemplate().queryForObject(
				"getShopByShopName", shopName);
		if (result != null) {
			result.setOfferNum(getShopOfferNum(result.getId()));
		}
		return result;
	}

	public boolean updateShop(Shop shop) {
		int i = getSqlMapClientTemplate().update("updateShop", shop);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean refreshShop(int id) {
		Shop shop = getShop(id);
		if (shop == null) {
			return false;
		}
		int offerNum = getShopOfferNum(id);
		int blogNum = getShopBlogNum(id);
		shop.setOfferNum(offerNum);
		shop.setBlogNum(blogNum);
		return updateShop(shop);
	}

	private int getShopOfferNum(int id) {
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countOfferByShopId", id);
		if (count != null) {
			return count;
		} else {
			return 0;
		}
	}

	private int getShopBlogNum(int id) {
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countBlogByShopId", id);
		if (count != null) {
			return count;
		} else {
			return 0;
		}
	}

	public SearchResult<Shop> getShopByLabelId(int labelId, Pagination page) {
		SearchResult<Shop> sr = new SearchResult<Shop>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countShopByLabelId", labelId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(labelId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getShopByLabelId", request);
			if (list != null && !list.isEmpty()) {
				List<Shop> result = new ArrayList<Shop>();
				for (Object o : list) {
					result.add((Shop) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public boolean refreshShopOfferLabels(int id) {
		List<?> list = getSqlMapClientTemplate().queryForList("getOfferLabelsByShopId", id);
		Set<String> set = new HashSet<String>();
		if (list != null && !list.isEmpty()) {
			for (Object o : list) {
				String l = (String) o;
				if (l != null && l.length() > 0) {
					String[] ls = l.split(",");
					for (String label : ls) {
						if (label != null && label.trim().length() > 0) {
							set.add(label.trim());
						}
					}
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (String l : set) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(l);
		}
		String label = sb.toString();
		Shop shop = getShop(id);
		shop.setOfferLabels(label);
		return this.updateShop(shop);
	}

	public boolean refreshShopBlogLabels(int id) {
		List<?> list = getSqlMapClientTemplate().queryForList("getBlogLabelsByShopId", id);
		Set<String> set = new HashSet<String>();
		if (list != null && !list.isEmpty()) {
			for (Object o : list) {
				String l = (String) o;
				if (l != null && l.length() > 0) {
					String[] ls = l.split(",");
					for (String label : ls) {
						if (label != null && label.trim().length() > 0) {
							set.add(label.trim());
						}
					}
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (String l : set) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(l);
		}
		String label = sb.toString();
		Shop shop = getShop(id);
		shop.setBlogLabels(label);
		return this.updateShop(shop);
	}
	
	public boolean addShopVisitNum(int id) {
		int i = getSqlMapClientTemplate().update("addShopVisitNum", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateShopCommentNum(int id) {
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countShopCommentByShopId", id);
		if (count != null) {
			Shop shop = new Shop();
			shop.setId(id);
			shop.setCommentNum(count);
			int i = getSqlMapClientTemplate().update("updateShopCommentNum", shop);
			if (i == 1) {
				return true;
			}
		}
		return false;
	}


}
