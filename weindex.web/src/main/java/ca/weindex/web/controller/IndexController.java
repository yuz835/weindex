package ca.weindex.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Label;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.services.FavoriteService;
import ca.weindex.services.HomePageService;
import ca.weindex.services.LabelService;
import ca.weindex.services.OfferService;
import ca.weindex.services.UserService;
import ca.weindex.web.helper.AdminHelper;
import ca.weindex.web.helper.LabelHelper;

@Controller
@RequestMapping("/index")
public class IndexController {
	@Autowired
	private HomePageService homePageService;

	@Autowired
	private UserService userService;

	@Autowired
	private OfferService offerService;

	@Autowired
	private FavoriteService favoriteService;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private LabelService labelService;

	@RequestMapping
	public ModelAndView home() {
		Pagination indexPage = new Pagination();
		indexPage.setPageNum(1);
		indexPage.setPageSize(15);
		SearchResult<Offer> recentOffers = homePageService.getRecentOffer(indexPage);
		SearchResult<Shop> recentShops = homePageService.getRecentShop(indexPage);
		SearchResult<Blog> recentBlogs = homePageService.getRecentBlog(indexPage);

		// FIXME we use root's favorite as the recomended list in homepage
		String rootUserName = adminHelper.getRootUser();
		int rootUserId = userService.getUserIdByName(rootUserName);
		SearchResult<Offer> hotOffers;
		SearchResult<Shop> hotShops;
		SearchResult<Blog> hotBlogs;
		if (rootUserId >= 0) {
			hotOffers = favoriteService.getUserFavoriteOffer(rootUserId, false, indexPage);
			hotShops = favoriteService.getUserFavoriteShop(rootUserId, false, indexPage);
			hotBlogs = favoriteService.getUserFavoriteBlog(rootUserId, false, indexPage);
		} else {
			hotOffers = homePageService.getHomepageDetailOfferList(indexPage);
			hotShops = new SearchResult<Shop>();
			hotBlogs = new SearchResult<Blog>();
		}

		List<Label> labelList = labelService.getLabel();

		ModelAndView view = new ModelAndView("index");
		view.addObject("recentOffers", recentOffers.getList());
		view.addObject("recentShops", recentShops.getList());
		view.addObject("recentBlogs", recentBlogs.getList());
		view.addObject("hotOffers", hotOffers.getList());
		view.addObject("hotShops", hotShops.getList());
		view.addObject("hotBlogs", hotBlogs.getList());
		view.addObject("labels", labelList);
		return view;
	}

	@RequestMapping("/offer")
	public ModelAndView offer(String type, Pagination page) {
		SearchResult<Offer> result;
		page.setPageSize(20);
		ModelAndView view = new ModelAndView("index/offer");
		if ("hot".equalsIgnoreCase(type)) {
			String rootUserName = adminHelper.getRootUser();
			int rootUserId = userService.getUserIdByName(rootUserName);
			if (rootUserId >= 0) {
				result = favoriteService.getUserFavoriteOffer(rootUserId, false, page);
			} else {
				result = homePageService.getHomepageDetailOfferList(page);
			}
			view.addObject("type", "hot");
		} else {
			result = homePageService.getRecentOffer(page);
			view.addObject("type", "new");
		}
		view.addObject("result", result);

		List<Label> labelList = labelService.getLabel();
		view.addObject("labels", labelList);

		// FIXME we use root's favorite as the recomended list in homepage
		Pagination indexPage = new Pagination();
		String rootUserName = adminHelper.getRootUser();
		int rootUserId = userService.getUserIdByName(rootUserName);
		SearchResult<Offer> hotOffers;
		SearchResult<Shop> hotShops;
		SearchResult<Blog> hotBlogs;
		if (rootUserId >= 0) {
			hotOffers = favoriteService.getUserFavoriteOffer(rootUserId, false, indexPage);
			hotShops = favoriteService.getUserFavoriteShop(rootUserId, false, indexPage);
			hotBlogs = favoriteService.getUserFavoriteBlog(rootUserId, false, indexPage);
		} else {
			hotOffers = homePageService.getHomepageDetailOfferList(indexPage);
			hotShops = new SearchResult<Shop>();
			hotBlogs = new SearchResult<Blog>();
		}

		view.addObject("hotOffers", hotOffers.getList());
		view.addObject("hotShops", hotShops.getList());
		view.addObject("hotBlogs", hotBlogs.getList());

		return view;
	}

	@RequestMapping("/shop")
	public ModelAndView shop(String type, Pagination page) {
		SearchResult<Shop> result;
		// page.setPageSize(20);
		ModelAndView view = new ModelAndView("index/shop");
		if ("hot".equalsIgnoreCase(type)) {
			String rootUserName = adminHelper.getRootUser();
			int rootUserId = userService.getUserIdByName(rootUserName);
			if (rootUserId >= 0) {
				result = favoriteService.getUserFavoriteShop(rootUserId, false, page);
			} else {
				result = new SearchResult<Shop>();
			}
			view.addObject("type", "hot");
		} else {
			result = homePageService.getRecentShop(page);
			view.addObject("type", "new");
		}
		view.addObject("result", result);

		List<Label> labelList = labelService.getLabel();
		view.addObject("labels", labelList);

		populateShopListLabels(result.getList(), labelList);
		
		// FIXME we use root's favorite as the recomended list in homepage
		Pagination indexPage = new Pagination();
		String rootUserName = adminHelper.getRootUser();
		int rootUserId = userService.getUserIdByName(rootUserName);
		SearchResult<Offer> hotOffers;
		SearchResult<Shop> hotShops;
		SearchResult<Blog> hotBlogs;
		if (rootUserId >= 0) {
			hotOffers = favoriteService.getUserFavoriteOffer(rootUserId, false, indexPage);
			hotShops = favoriteService.getUserFavoriteShop(rootUserId, false, indexPage);
			hotBlogs = favoriteService.getUserFavoriteBlog(rootUserId, false, indexPage);
		} else {
			hotOffers = homePageService.getHomepageDetailOfferList(indexPage);
			hotShops = new SearchResult<Shop>();
			hotBlogs = new SearchResult<Blog>();
		}

		view.addObject("hotOffers", hotOffers.getList());
		view.addObject("hotShops", hotShops.getList());
		view.addObject("hotBlogs", hotBlogs.getList());

		return view;
	}

	@RequestMapping("/blog")
	public ModelAndView blog(String type, Pagination page) {
		SearchResult<Blog> result;
		// page.setPageSize(20);
		ModelAndView view = new ModelAndView("index/blog");
		if ("hot".equalsIgnoreCase(type)) {
			String rootUserName = adminHelper.getRootUser();
			int rootUserId = userService.getUserIdByName(rootUserName);
			if (rootUserId >= 0) {
				result = favoriteService.getUserFavoriteBlog(rootUserId, false, page);
			} else {
				result = new SearchResult<Blog>();
			}
			view.addObject("type", "hot");
		} else {
			result = homePageService.getRecentBlog(page);
			view.addObject("type", "new");
		}
		List<Label> labelList = labelService.getLabel();
		view.addObject("labels", labelList);

		for (Blog blog : result.getList()) {
			List<Label> myLabels = LabelHelper.convertLabel(blog.getLabel(), labelList);
			blog.setLabelList(myLabels);

 /// Start zhangeth 2013/04/22
 // here we use shoplabel to store the image url in the blog
                         String content = blog.getContent();
                         String blogImageUrl = null;
                         if (content != null) {
                                 String tag = "[img:";
                                 int index = content.indexOf(tag);
                                 while (index != -1) {
                                         int postIndex = content.indexOf("]", index);
                                         if (postIndex != -1) {
                                                 String pre = content.substring(0, index);
                                                 String post = content.substring(postIndex + 1);
                                                 String imgUrl = content.substring(index + tag.length(), postIndex);
                                                 if (blogImageUrl == null) {
                                                         blogImageUrl = "http://weindex.ca" + imgUrl;
                                                         view.addObject("blogImageUrl", blogImageUrl);
                                                 }
                                                 // just need one pic, then break
                                                 content = imgUrl;
                                                 break;
                                         }
                                 }
                         }
                         blog.setShopLabel(content);
 // End zhangeth
                 }

		view.addObject("result", result);

		// FIXME we use root's favorite as the recomended list in homepage
		Pagination indexPage = new Pagination();
		String rootUserName = adminHelper.getRootUser();
		int rootUserId = userService.getUserIdByName(rootUserName);
		SearchResult<Offer> hotOffers;
		SearchResult<Shop> hotShops;
		SearchResult<Blog> hotBlogs;
		if (rootUserId >= 0) {
			hotOffers = favoriteService.getUserFavoriteOffer(rootUserId, false, indexPage);
			hotShops = favoriteService.getUserFavoriteShop(rootUserId, false, indexPage);
			hotBlogs = favoriteService.getUserFavoriteBlog(rootUserId, false, indexPage);
		} else {
			hotOffers = homePageService.getHomepageDetailOfferList(indexPage);
			hotShops = new SearchResult<Shop>();
			hotBlogs = new SearchResult<Blog>();
		}

		view.addObject("hotOffers", hotOffers.getList());
		view.addObject("hotShops", hotShops.getList());
		view.addObject("hotBlogs", hotBlogs.getList());

		return view;
	}

	@RequestMapping("json")
	public ModelAndView json() {
		ModelAndView view = new ModelAndView("json");
		view.addObject("content", "mycontent");
		return view;
	}

	@RequestMapping("test")
	public String test() {
		return "index/test";
	}
	
	private void populateShopListLabels(List<Shop> list, List<Label> labels) {
		if (list == null || list.isEmpty()) {
			return;
		}
		for (Shop shop : list) {
			List<Label> myLabels = LabelHelper.convertLabel(shop.getLabel(), labels);
			shop.setLabelList(myLabels);
		}

	}

}
