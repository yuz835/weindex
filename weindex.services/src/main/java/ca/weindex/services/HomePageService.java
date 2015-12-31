package ca.weindex.services;

import java.util.List;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.HomepageOffer;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;

public interface HomePageService {

	public SearchResult<Offer> getRecentOffer(Pagination page);
	public SearchResult<Shop> getRecentShop(Pagination page);
	public SearchResult<Blog> getRecentBlog(Pagination page);

	public boolean insertHomepageOffer(HomepageOffer offer);
	public boolean deleteHomepageOffer(int id);
	public List<HomepageOffer> getHomepageOfferList();

	public SearchResult<Offer> getHomepageDetailOfferList(Pagination page);

}
