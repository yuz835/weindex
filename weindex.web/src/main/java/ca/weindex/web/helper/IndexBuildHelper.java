package ca.weindex.web.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.services.BlogService;
import ca.weindex.services.HomePageService;
import ca.weindex.services.OfferService;
import ca.weindex.services.ShopService;

@Component
public class IndexBuildHelper {
	private boolean finished = true;

	@Autowired
	private OfferService offerService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private BlogService blogService;
	@Autowired
	private HomePageService homePageService;
	
	public void buildIndex() {
		if (finished) {
			Thread trd = new Builder(this);
			trd.start();
		}
	}

	private class Builder extends Thread {
		private IndexBuildHelper helper;

		public Builder(IndexBuildHelper helper) {
			this.helper = helper;
		}

		public void run() {
			System.out.println("started!");
			helper.finished = false;

			try {
				Pagination page = new Pagination();
				page.setPageSize(50);
				SearchResult<Shop> shopList = homePageService.getRecentShop(page);
				System.out.println("get shop: " + shopList.getTotalNum());
				while (shopList != null && shopList.getList() != null && !shopList.getList().isEmpty()) {
					for (Shop s : shopList.getList()) {
						int shopId = s.getId();
						
						// get all shop offer
						Pagination offerPage = new Pagination();
						offerPage.setPageSize(20);
						SearchResult<Offer> offerList = offerService.getOfferByShopId(shopId, true, offerPage);
						while (offerList != null && offerList.getList() != null && !offerList.getList().isEmpty()) {
							for (Offer offer : offerList.getList()) {
								System.out.println("----------- rebuild offer: " + offer.getId());
								offerService.rebuildOfferIndex(offer.getId());
							}
							offerPage.setPageNum(offerPage.getPageNum() + 1);
							offerList = offerService.getOfferByShopId(shopId, true, offerPage);
						}
						
						// get all shop blogs
						Pagination blogPage = new Pagination();
						blogPage.setPageSize(20);
						SearchResult<Blog> blogList = blogService.getBlogByShopId(shopId, blogPage);
						while (blogList != null && blogList.getList() != null && !blogList.getList().isEmpty()) {
							for (Blog blog : blogList.getList()) {
								System.out.println("----------- rebuild blog: " + blog.getId());
								blogService.rebuildBlogIndex(blog.getId());
							}
							blogPage.setPageNum(blogPage.getPageNum() + 1);
							blogList = blogService.getBlogByShopId(shopId, blogPage);
						}
						
						System.out.println("----------- rebuild shop: " + shopId);
						
						// rebuild shop index
						shopService.rebuildShopIndex(shopId);
						sleep(100); // sleep 0.1 second
					}
					page.setPageNum(page.getPageNum() + 1);
					shopList = homePageService.getRecentShop(page);
				}
			} catch (Exception e) {

			} finally {
				helper.finished = true;
			}
			helper.finished = true;
		}
	}
}
