package ca.weindex.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ca.weindex.common.constants.SessionConstants;
import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;
import ca.weindex.common.model.UserFavoriteBlog;
import ca.weindex.common.model.UserFavoriteOffer;
import ca.weindex.common.model.UserFavoriteShop;
import ca.weindex.services.FavoriteService;
import ca.weindex.services.HomePageService;
import ca.weindex.services.ShopService;
import ca.weindex.services.UserService;
import ca.weindex.web.helper.AdminHelper;

@Controller
@RequestMapping("/user/favorite")
public class FavoriteController {
	@Autowired
	private FavoriteService favoriteService;

	@Autowired
	private ShopService shopService;

	@Autowired
	private UserService userService;

	@Autowired
	private HomePageService homePageService;

	@Autowired
	private AdminHelper adminHelper;

	@RequestMapping("/{id}")
	public ModelAndView index(@PathVariable int id, int type, Pagination page, HttpSession session) {
		ModelAndView view;
		User user = userService.getUserById(id);
		if (user == null) {
			view = new ModelAndView("redirect:/");
			return view;
		}

		User curUser = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		boolean isAll = false;
		if (curUser != null && curUser.getId() == id) {
			isAll = true;
		}
		if (type == 1) {
			// shop
			SearchResult<Shop> result = favoriteService.getUserFavoriteShop(id, isAll, page);
			view = new ModelAndView("user/favShop");
			view.addObject("result", result);
			view.addObject("type", 1);
		} else if (type == 2) {
			// blog
			SearchResult<Blog> result = favoriteService.getUserFavoriteBlog(id, isAll, page);
			view = new ModelAndView("user/favBlog");
			view.addObject("result", result);
			view.addObject("type", 2);
		} else {
			// offer
			SearchResult<Offer> result = favoriteService.getUserFavoriteOffer(id, isAll, page);
			view = new ModelAndView("user/favOffer");
			view.addObject("result", result);
			view.addObject("type", 0);
		}
		view.addObject("user", user);
		view.addObject("isOwner", isAll);
		initView(view);

		return view;
	}

	private void initView(ModelAndView view) {
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
	}

	@RequestMapping("shop/add")
	public String addFavShop(int shopId, HttpSession session) {
		Shop s = shopService.getShop(shopId);
		if (s == null) {
			return "redirect:/";
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			UserFavoriteShop shop = new UserFavoriteShop();
			shop.setShopId(shopId);
			shop.setUserId(user.getId());
			boolean b = favoriteService.addFavShop(shop);
			if (b) {
				return "redirect:/" + s.getName();
			}
		}
		return "redirect:/" + s.getName();
	}

	@RequestMapping("offer/add")
	public String addFavOffer(int offerId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			UserFavoriteOffer offer = new UserFavoriteOffer();
			offer.setOfferId(offerId);
			offer.setUserId(user.getId());
			boolean b = favoriteService.addFavOffer(offer);
			if (b) {
				return "redirect:/offer/view/" + offerId + ".html";
			}
		}
		return "redirect:/offer/view/" + offerId + ".html";
	}

	@RequestMapping("blog/add")
	public String addFavBlog(int blogId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			UserFavoriteBlog blog = new UserFavoriteBlog();
			blog.setBlogId(blogId);
			blog.setUserId(user.getId());
			boolean b = favoriteService.addFavBlog(blog);
			if (b) {
				return "redirect:/blog/view/" + blogId + ".html";
			}
		}
		return "redirect:/blog/view/" + blogId + ".html";
	}

	@RequestMapping("shop/del")
	public String delFavShop(int shopId, String src, HttpSession session) {
		Shop s = shopService.getShop(shopId);
		if (s == null) {
			return "redirect:/";
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			UserFavoriteShop shop = new UserFavoriteShop();
			shop.setShopId(shopId);
			shop.setUserId(user.getId());
			boolean b = favoriteService.delFavShop(shop);
			if (b) {
				if ("fav".equals(src)) {
					return "redirect:/user/favorite/" + user.getId() + ".html?type=1";
				}
				return "redirect:/" + s.getName();
			}
		}
		return "redirect:/" + s.getName();
	}

	@RequestMapping("offer/del")
	public String delFavOffer(int offerId, String src, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			UserFavoriteOffer offer = new UserFavoriteOffer();
			offer.setOfferId(offerId);
			offer.setUserId(user.getId());
			boolean b = favoriteService.delFavOffer(offer);
			if (b) {
				if ("fav".equals(src)) {
					return "redirect:/user/favorite/" + user.getId() + ".html?type=0";
				}
				return "redirect:/offer/view/" + offerId + ".html";
			}
		}
		return "redirect:/offer/view/" + offerId + ".html";
	}

	@RequestMapping("offer/open")
	public String openFavOffer(int offerId, String src, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			UserFavoriteOffer offer = new UserFavoriteOffer();
			offer.setOfferId(offerId);
			offer.setUserId(user.getId());
			offer.setPublicFav(0);
			boolean b = favoriteService.updateFavOffer(offer);
			if (b) {
				if ("fav".equals(src)) {
					return "redirect:/user/favorite/" + user.getId() + ".html?type=0";
				}
				return "redirect:/offer/view/" + offerId + ".html";
			}
		}
		return "redirect:/offer/view/" + offerId + ".html";
	}

	@RequestMapping("offer/close")
	public String closeFavOffer(int offerId, String src, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			UserFavoriteOffer offer = new UserFavoriteOffer();
			offer.setOfferId(offerId);
			offer.setUserId(user.getId());
			offer.setPublicFav(1);
			boolean b = favoriteService.updateFavOffer(offer);
			if (b) {
				if ("fav".equals(src)) {
					return "redirect:/user/favorite/" + user.getId() + ".html?type=0";
				}
				return "redirect:/offer/view/" + offerId + ".html";
			}
		}
		return "redirect:/offer/view/" + offerId + ".html";
	}

	@RequestMapping("shop/open")
	public String openFavShop(int shopId, String src, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Shop entity = shopService.getShop(shopId);
			if (entity != null) {
				UserFavoriteShop shop = new UserFavoriteShop();
				shop.setShopId(shopId);
				shop.setUserId(user.getId());
				shop.setPublicFav(0);
				boolean b = favoriteService.updateFavShop(shop);
				if (b) {
					if ("fav".equals(src)) {
						return "redirect:/user/favorite/" + user.getId() + ".html?type=1";
					}
					return "redirect:/" + entity.getName();
				}
			}
		}
		return "redirect:/";
	}

	@RequestMapping("shop/close")
	public String closeFavShop(int shopId, String src, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Shop entity = shopService.getShop(shopId);
			if (entity != null) {
				UserFavoriteShop shop = new UserFavoriteShop();
				shop.setShopId(shopId);
				shop.setUserId(user.getId());
				shop.setPublicFav(1);
				boolean b = favoriteService.updateFavShop(shop);
				if (b) {
					if ("fav".equals(src)) {
						return "redirect:/user/favorite/" + user.getId() + ".html?type=1";
					}
					return "redirect:/" + entity.getName();
				}
			}
		}
		return "redirect:/";
	}

	@RequestMapping("blog/open")
	public String openFavBlog(int blogId, String src, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			UserFavoriteBlog blog = new UserFavoriteBlog();
			blog.setBlogId(blogId);
			blog.setUserId(user.getId());
			blog.setPublicFav(0);
			boolean b = favoriteService.updateFavBlog(blog);
			if (b) {
				if ("fav".equals(src)) {
					return "redirect:/user/favorite/" + user.getId() + ".html?type=2";
				}
				return "redirect:/blog/view/" + blogId + ".html";
			}
		}
		return "redirect:/blog/view/" + blogId + ".html";
	}

	@RequestMapping("blog/close")
	public String closeFavBlog(int blogId, String src, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			UserFavoriteBlog blog = new UserFavoriteBlog();
			blog.setBlogId(blogId);
			blog.setUserId(user.getId());
			blog.setPublicFav(1);
			boolean b = favoriteService.updateFavBlog(blog);
			if (b) {
				if ("fav".equals(src)) {
					return "redirect:/user/favorite/" + user.getId() + ".html?type=2";
				}
				return "redirect:/blog/view/" + blogId + ".html";
			}
		}
		return "redirect:/blog/view/" + blogId + ".html";
	}

	@RequestMapping("blog/del")
	public String delFavBlog(int blogId, String src, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			UserFavoriteBlog blog = new UserFavoriteBlog();
			blog.setBlogId(blogId);
			blog.setUserId(user.getId());
			boolean b = favoriteService.delFavBlog(blog);
			if (b) {
				if ("fav".equals(src)) {
					return "redirect:/user/favorite/" + user.getId() + ".html?type=2";
				}
				return "redirect:/blog/view/" + blogId + ".html";
			}
		}
		return "redirect:/blog/view/" + blogId + ".html";
	}
}
