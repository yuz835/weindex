package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.HomepageOffer;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.dao.HomePageDao;

public class HomePageDaoImp extends SqlMapClientDaoSupport implements HomePageDao {
	public SearchResult<Offer> getRecentOffer(Pagination page) {
		SearchResult<Offer> sr = new SearchResult<Offer>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countOffer");
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getRecentOffer", request);
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

	public SearchResult<Shop> getRecentShop(Pagination page) {
		SearchResult<Shop> sr = new SearchResult<Shop>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countShop");
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getRecentShop", request);
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

	public SearchResult<Blog> getRecentBlog(Pagination page) {
		SearchResult<Blog> sr = new SearchResult<Blog>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countBlog");
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getRecentBlog", request);
			if (list != null && !list.isEmpty()) {
				List<Blog> result = new ArrayList<Blog>();
				for (Object o : list) {
					result.add((Blog) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public boolean insertHomepageOffer(HomepageOffer offer) {
		int i = getSqlMapClientTemplate().update("insertHomepageOffer", offer);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteHomepageOffer(int id) {
		int i = getSqlMapClientTemplate().delete("deleteHomepageOffer", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public List<HomepageOffer> getHomepageOfferList() {
		List<?> list = getSqlMapClientTemplate().queryForList("getTopHomepageOffer");
		if (list != null && !list.isEmpty()) {
			List<HomepageOffer> result = new ArrayList<HomepageOffer>();
			for (Object o : list) {
				result.add((HomepageOffer) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public SearchResult<Offer> getHomepageDetailOfferList(Pagination page) {
		SearchResult<Offer> sr = new SearchResult<Offer>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countTopHomepageOffer");
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getTopHomepageOfferDetail", request);
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

}
