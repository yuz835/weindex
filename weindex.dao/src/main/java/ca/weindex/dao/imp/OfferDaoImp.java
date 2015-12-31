package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.ShopLabelSearchRequest;
import ca.weindex.dao.OfferDao;

public class OfferDaoImp extends SqlMapClientDaoSupport implements OfferDao {

	public Offer getOffer(int id) {
		Offer result = (Offer) getSqlMapClientTemplate().queryForObject("getOffer", id);
		return result;
	}

	public boolean insertOffer(Offer offer) {
		int id = (Integer) getSqlMapClientTemplate().insert("insertOffer", offer);
		if (id > 0) {
			offer.setId(id);
			return true;
		} else {
			return false;
		}
//		int i = getSqlMapClientTemplate().update("insertOffer", offer);
//		if (i == 1) {
//			return true;
//		} else {
//			return false;
//		}
	}

	public SearchResult<Offer> getOfferByShopId(int shopId, Pagination page) {
		SearchResult<Offer> sr = new SearchResult<Offer>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countOfferByShopId", shopId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(shopId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getOfferByShopId", request);
			if (list != null && !list.isEmpty()) {
				List<Offer> result = new ArrayList<Offer>();
				for (Object o : list) {
					result.add((Offer) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Offer> getOpenOfferByShopId(int shopId, Pagination page) {
		SearchResult<Offer> sr = new SearchResult<Offer>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countOpenOfferByShopId", shopId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(shopId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getOpenOfferByShopId", request);
			if (list != null && !list.isEmpty()) {
				List<Offer> result = new ArrayList<Offer>();
				for (Object o : list) {
					result.add((Offer) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Offer> getRecentOfferByShopId(int shopId, Pagination page) {
		SearchResult<Offer> sr = new SearchResult<Offer>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countOpenOfferByShopId", shopId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(shopId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getRecentOfferByShopId", request);
			if (list != null && !list.isEmpty()) {
				List<Offer> result = new ArrayList<Offer>();
				for (Object o : list) {
					result.add((Offer) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}


	public boolean updateOffer(Offer offer) {
		int i = getSqlMapClientTemplate().update("updateOffer", offer);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public List<Offer> searchOffer(String keyword) {
		List<?> list = getSqlMapClientTemplate().queryForList("searchOffer", keyword.toLowerCase());
		if (list != null && !list.isEmpty()) {
			List<Offer> result = new ArrayList<Offer>();
			for (Object o : list) {
				result.add((Offer) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public boolean deleteOffer(int id) {
		int i = getSqlMapClientTemplate().delete("deleteOffer", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public SearchResult<Offer> getOfferByLabelId(int labelId, Pagination page) {
		SearchResult<Offer> sr = new SearchResult<Offer>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countOfferByLabelId", labelId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(labelId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getOfferByLabelId", request);
			if (list != null && !list.isEmpty()) {
				List<Offer> result = new ArrayList<Offer>();
				for (Object o : list) {
					result.add((Offer) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Offer> getOfferByShopLabelId(int shopId, int labelId, Pagination page) {
		SearchResult<Offer> sr = new SearchResult<Offer>();
		ShopLabelSearchRequest request = new ShopLabelSearchRequest();
		request.setShopId(shopId);
		request.setLabelId(labelId);
		request.setPage(page);
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countOfferByShopLabelId", request);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			List<?> list = getSqlMapClientTemplate().queryForList("getOfferByShopLabelId", request);
			if (list != null && !list.isEmpty()) {
				List<Offer> result = new ArrayList<Offer>();
				for (Object o : list) {
					result.add((Offer) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Offer> getOpenOfferByShopLabelId(int shopId, int labelId, Pagination page) {
		SearchResult<Offer> sr = new SearchResult<Offer>();
		ShopLabelSearchRequest request = new ShopLabelSearchRequest();
		request.setShopId(shopId);
		request.setLabelId(labelId);
		request.setPage(page);
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countOpenOfferByShopLabelId", request);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			List<?> list = getSqlMapClientTemplate().queryForList("getOpenOfferByShopLabelId", request);
			if (list != null && !list.isEmpty()) {
				List<Offer> result = new ArrayList<Offer>();
				for (Object o : list) {
					result.add((Offer) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Offer> getOfferByShopShopLabelId(int shopId, int shopLabelId, Pagination page) {
		SearchResult<Offer> sr = new SearchResult<Offer>();
		ShopLabelSearchRequest request = new ShopLabelSearchRequest();
		request.setShopId(shopId);
		request.setLabelId(shopLabelId);
		request.setPage(page);
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countOfferByShopShopLabelId", request);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			List<?> list = getSqlMapClientTemplate().queryForList("getOfferByShopShopLabelId", request);
			if (list != null && !list.isEmpty()) {
				List<Offer> result = new ArrayList<Offer>();
				for (Object o : list) {
					result.add((Offer) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Offer> getOpenOfferByShopShopLabelId(int shopId, int shopLabelId, Pagination page) {
		SearchResult<Offer> sr = new SearchResult<Offer>();
		ShopLabelSearchRequest request = new ShopLabelSearchRequest();
		request.setShopId(shopId);
		request.setLabelId(shopLabelId);
		request.setPage(page);
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countOpenOfferByShopShopLabelId", request);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			List<?> list = getSqlMapClientTemplate().queryForList("getOpenOfferByShopShopLabelId", request);
			if (list != null && !list.isEmpty()) {
				List<Offer> result = new ArrayList<Offer>();
				for (Object o : list) {
					result.add((Offer) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public boolean addOfferVisitNum(int id) {
		int i = getSqlMapClientTemplate().update("addOfferVisitNum", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateOfferCommentNum(int id) {
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countOfferCommentByOfferId", id);
		if (count != null) {
			Shop shop = new Shop();
			shop.setId(id);
			shop.setCommentNum(count);
			int i = getSqlMapClientTemplate().update("updateOfferCommentNum", shop);
			if (i == 1) {
				return true;
			}
		}
		return false;
	}

	public void updateOfferPosByShopId(int shopId) {
		getSqlMapClientTemplate().update("updateOfferPosByShopId", shopId);		
		
	}

	public boolean updateOfferPos(Offer offer) {
		int i = getSqlMapClientTemplate().update("updateOfferPos", offer);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}
}
