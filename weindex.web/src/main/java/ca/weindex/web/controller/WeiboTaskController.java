package ca.weindex.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ca.weindex.common.constants.SessionConstants;
import ca.weindex.common.enums.WeiboTaskStatusEnum;
import ca.weindex.common.enums.WeiboTaskTypeEnum;
import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;
import ca.weindex.common.model.WeiboTask;
import ca.weindex.services.BlogService;
import ca.weindex.services.OfferService;
import ca.weindex.services.ShopService;
import ca.weindex.services.UserService;
import ca.weindex.services.WeiboTaskService;
import ca.weindex.web.helper.JsonHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/weibo")
public class WeiboTaskController {
	private static final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm Z");

	@Autowired
	private UserService userService;

	@Autowired
	private OfferService offerService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private ShopService shopService;

	@Autowired
	private WeiboTaskService taskService;

	@RequestMapping("offer/{offerId}")
	public ModelAndView offer(@PathVariable int offerId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Offer offer = offerService.getOffer(offerId);
		if (offer == null) {
			return new ModelAndView("redirect:/");
		}
		Shop shop = shopService.getShop(offer.getShopId());
		//if (shop == null || shop.getUserId() != user.getId()) {
		if (shop == null) {
			return new ModelAndView("redirect:/");
		}
		List<WeiboTask> list = taskService.getWeiboTaskByOfferId(offerId);
		ModelAndView view = new ModelAndView("weibo/task");
		view.addObject("shop", shop);
		view.addObject("offer", offer);
		view.addObject("list", list);
		return view;
	}

	@RequestMapping("offer/delTask")
	public ModelAndView delOfferTask(int taskId, int offerId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Offer offer = offerService.getOffer(offerId);
		if (offer == null) {
			return new ModelAndView("redirect:/");
		}
		Shop shop = shopService.getShop(offer.getShopId());
		if (shop == null || shop.getUserId() != user.getId()) {
			return new ModelAndView("redirect:/");
		}
		WeiboTask task = taskService.getWeiboTask(taskId);
		if (task == null || task.getTypeEnum() != WeiboTaskTypeEnum.OFFER || task.getTaskId() != offerId
				|| task.getStatusEnum() == WeiboTaskStatusEnum.WAITING) {
			return new ModelAndView("redirect:/weibo/offer/" + offerId + ".html");
		}
		boolean b = taskService.deleteWeiboTask(taskId);
		if (b) {
			return new ModelAndView("redirect:/weibo/offer/" + offerId + ".html");
		} else {
			return new ModelAndView("redirect:/weibo/offer/" + offerId + ".html");
		}
	}

	@RequestMapping("blog/{blogId}")
	public ModelAndView blog(@PathVariable int blogId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Blog blog = blogService.getBlog(blogId);
		if (blog == null) {
			return new ModelAndView("redirect:/");
		}
		Shop shop = shopService.getShop(blog.getShopId());
		//if (shop == null || shop.getUserId() != user.getId()) {
		// to let everybody can timer the weibo
		if (shop == null) {
			return new ModelAndView("redirect:/");
		}
		List<WeiboTask> list = taskService.getWeiboTaskByBlogId(blogId);
		ModelAndView view = new ModelAndView("weibo/task");
		view.addObject("shop", shop);
		view.addObject("blog", blog);
		view.addObject("list", list);
		return view;
	}

	@RequestMapping("blog/delTask")
	public ModelAndView delBlogTask(int taskId, int blogId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Blog blog = blogService.getBlog(blogId);
		if (blog == null) {
			return new ModelAndView("redirect:/");
		}
		Shop shop = shopService.getShop(blog.getShopId());
		if (shop == null || shop.getUserId() != user.getId()) {
			return new ModelAndView("redirect:/");
		}
		WeiboTask task = taskService.getWeiboTask(taskId);
		if (task == null || task.getTypeEnum() != WeiboTaskTypeEnum.BLOG || task.getTaskId() != blogId
				|| task.getStatusEnum() == WeiboTaskStatusEnum.WAITING) {
			return new ModelAndView("redirect:/weibo/blog/" + blogId + ".html");
		}
		boolean b = taskService.deleteWeiboTask(taskId);
		if (b) {
			return new ModelAndView("redirect:/weibo/blog/" + blogId + ".html");
		} else {
			return new ModelAndView("redirect:/weibo/blog/" + blogId + ".html");
		}
	}


	@RequestMapping("/addTask")
	public ModelAndView addTask(int id, int type, String time, int offset, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		Map<String, Object> result = new HashMap<String, Object>();
		if (user != null) {
			// check user weibo id & weibo token
			// first, update user
			user = userService.getUserById(user.getId());
			String weiboId = user.getWeiboId();
			String token = user.getWeiboToken();
			if (weiboId == null || weiboId.length() == 0 || token == null || token.length() == 0) {
				// no weibo id / token
				result.put("result", "failed");
				result.put("code", 1);
			} else {
				// check token
				String queryUrl = "https://api.weibo.com/2/users/show.json?access_token=" + token + "&uid=" + weiboId;
				JSONObject testObj = JsonHelper.get(queryUrl);
				boolean check = false;
				if (testObj != null) {
					String name = testObj.getString("screen_name");
					if (name != null) {
						check = true;
					}
				}
				if (check) {
					int timeZoneNum = offset / 60;
					if (timeZoneNum > 12 || timeZoneNum < -12) {
						// time format is not correct
						System.out.println("--------" + timeZoneNum + "-----" + offset);
						result.put("result", "failed");
						result.put("code", 3);
					}
					String timezone;
					if (timeZoneNum > 0) {
						timezone = timeZoneNum + "00";
						if (timezone.length() == 3) {
							timezone = "-0" + timezone;
						} else {
							timezone = "-" + timezone;
						}
					} else {
						timeZoneNum = -timeZoneNum;
						timezone = timeZoneNum + "00";
						if (timezone.length() == 3) {
							timezone = "0" + timezone;
						}
						timezone = "+" + timezone;
					}
					// System.out.println(id);
					// System.out.println(type);
					// System.out.println(time);
					// System.out.println(timezone);
					try {
						Date date = format.parse(time + " " + timezone);
						WeiboTask task = new WeiboTask();
						task.setUserId(user.getId());
						task.setType(type);
						task.setTaskId(id);
						task.setTaskTime(date);
						task.setStatus(WeiboTaskStatusEnum.UNSTARTED.getStatus());
						boolean b = taskService.insertWeiboTask(task);
						if (b) {
							result.put("result", "success");
						} else {
							// add task failed
							result.put("result", "failed");
							result.put("code", 4);
						}
					} catch (ParseException e) {
						// time format is not correct
						e.printStackTrace();
						result.put("result", "failed");
						result.put("code", 3);
					} catch (Exception e) {
						// unknow exception
						result.put("result", "failed");
						result.put("code", 5);
					}
				} else {
					// check token failed
					result.put("result", "failed");
					result.put("code", 2);
				}
			}
		} else {
			result.put("result", "failed");
			result.put("code", 0);
		}

		ModelAndView view = new ModelAndView("json");
		String content = JSON.toJSONString(result);
		view.addObject("content", content);
		return view;
	}
}
