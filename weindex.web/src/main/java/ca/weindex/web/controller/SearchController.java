package ca.weindex.web.controller;

import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ca.weindex.common.model.Label;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.services.BlogService;
import ca.weindex.services.FavoriteService;
import ca.weindex.services.HomePageService;
import ca.weindex.services.LabelService;
import ca.weindex.services.OfferService;
import ca.weindex.services.ShopService;
import ca.weindex.services.UserService;
import ca.weindex.web.helper.AdminHelper;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/search")
public class SearchController {
	@Autowired
	private OfferService offerService;

	@Autowired
	private ShopService shopService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private LabelService labelService;
	
	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private UserService userService;

	@Autowired
	private FavoriteService favoriteService;

	@Autowired
	private HomePageService homePageService;

	@RequestMapping
	public ModelAndView search(String keyword, String type, Pagination page) {
		int typeNum = 1;
		try {
			typeNum = Integer.parseInt(type);
		} catch (Exception e) {
			typeNum = 1;
		}
		if (typeNum < 1 || typeNum > 3) {
			typeNum = 1;
		}
		SearchResult<?> result;
		ModelAndView view;

		List<Label> labelList = labelService.getLabel();		
		String searchUri = getSearchUri(keyword, type);
		switch (typeNum) {
		case 1:
			if (keyword == null || keyword.length() == 0) {
				view = new ModelAndView("redirect:/index/offer.html");
				return view;
			}
			result = offerService.searchOffer(keyword, page);
			view = new ModelAndView("offer/searchlist");
			view.addObject("result", result);
			view.addObject("keyword", keyword);
			view.addObject("type", type);
			view.addObject("labels", labelList);
			view.addObject("searchUri", searchUri);
			return view;
		case 2:
			if (keyword == null || keyword.length() == 0) {
				view = new ModelAndView("redirect:/index/shop.html");
				return view;
			}
			result = shopService.searchShop(keyword, page);
			view = new ModelAndView("shop/searchlist");
			view.addObject("result", result);
			view.addObject("keyword", keyword);
			view.addObject("type", type);
			view.addObject("labels", labelList);
			view.addObject("searchUri", searchUri);
			return view;
		case 3:
			if (keyword == null || keyword.length() == 0) {
				view = new ModelAndView("redirect:/index/blog.html");
				return view;
			}
			result = blogService.searchBlog(keyword, page);
			view = new ModelAndView("blog/searchlist");
			view.addObject("result", result);
			view.addObject("keyword", keyword);
			view.addObject("type", type);
			view.addObject("labels", labelList);
			view.addObject("searchUri", searchUri);
			return view;
		}

		view = new ModelAndView("redirect:/index/offer.html");
		return view;
	}

	private String getSearchUri(String keyword, String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("/search.html?keyword=");
		try {
			sb.append(URLEncoder.encode(keyword, "UTF-8"));
		} catch (Exception e) {
			sb.append(keyword);
		}
		sb.append("&type=");
		sb.append(type);
		return sb.toString();
	}
	
	@RequestMapping("map")
	public ModelAndView map() {
		ModelAndView view = new ModelAndView("mapSearch");
		view.addObject("ismap", true);
		return view;
	}

	@RequestMapping("mapSearch")
	public ModelAndView mapSearch(double startLat, double endLat, double startLng, double endLng, Pagination page) {
		int startLati = (int) (startLat * 10000000);
		int endLati = (int) (endLat * 10000000);
		int startLongi = (int) (startLng * 10000000);
		int endLongi = (int) (endLng * 10000000);
		// System.out.println(startLati + " : " + endLati + " : " + startLongi + " : " + endLongi);
		SearchResult<Shop> result = shopService.searchShopByRegion(startLati, endLati, startLongi, endLongi, page);
		String content = JSON.toJSONString(result);
		ModelAndView view = new ModelAndView("json");
		view.addObject("content", content);
		return view;
	}
}
