package ca.weindex.services.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.HomepageOffer;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.dao.HomePageDao;
import ca.weindex.services.HomePageService;

@Service
public class HomePageServiceImp implements HomePageService {
	@Autowired
	private HomePageDao homePageDao;
	
	public SearchResult<Offer> getRecentOffer(Pagination page) {
		return homePageDao.getRecentOffer(page);
	}

	public SearchResult<Shop> getRecentShop(Pagination page) {
		return homePageDao.getRecentShop(page);
	}

	public SearchResult<Blog> getRecentBlog(Pagination page) {
		return homePageDao.getRecentBlog(page);
	}

	public boolean insertHomepageOffer(HomepageOffer offer) {
		return homePageDao.insertHomepageOffer(offer);
	}

	public boolean deleteHomepageOffer(int id) {
		return homePageDao.deleteHomepageOffer(id);
	}

	public List<HomepageOffer> getHomepageOfferList() {
		return homePageDao.getHomepageOfferList();
	}

	public SearchResult<Offer> getHomepageDetailOfferList(Pagination page) {
		return homePageDao.getHomepageDetailOfferList(page);
	}

}
