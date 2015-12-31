package ca.weindex.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ca.weindex.common.constants.SessionConstants;
import ca.weindex.common.model.Message;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;
import ca.weindex.common.model.UserContactor;
import ca.weindex.common.util.UUIDGenerator;
import ca.weindex.exceptions.DuplicateUserNameException;
import ca.weindex.exceptions.LoginFailedException;
import ca.weindex.services.MessageService;
import ca.weindex.services.ShopService;
import ca.weindex.services.UserService;
import ca.weindex.web.helper.AdminHelper;
import ca.weindex.web.helper.EmailHelper;
import ca.weindex.web.helper.JsonHelper;
import ca.weindex.web.util.ValidateUtil;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ShopService shopService;
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private AdminHelper adminHelper;
	
	@Autowired
	private EmailHelper emailHelper;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(String userName, String password, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		int failedTimes = userService.getUserFailedTimes(userName);
		if (failedTimes >= 5) {
			ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
			reCaptcha.setPrivateKey("6Lft9doSAAAAANdlvt-99gUxrSkfy-6n6w0i9ss-");
			String remoteAddr = request.getRemoteAddr();
			String challenge = request.getParameter("recaptcha_challenge_field");
			String uresponse = request.getParameter("recaptcha_response_field");
			if (challenge == null || challenge.length() == 0 || uresponse == null || uresponse.length() == 0) {
				// we need the captcha
				ModelAndView loginView = new ModelAndView("redirect:/user/login.html?needCaptcha=true");
				return loginView;
			}
			ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
			if (!reCaptchaResponse.isValid()) {
				ModelAndView loginView = new ModelAndView("redirect:/index.html");
				return loginView;
			}
		}
		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		try {
			User result = userService.login(user);
			if (result != null) {
				if (adminHelper.isAdmin(result)) {
					result.setAdmin(true);
				}
				// user login
				storeLoginedUser(result, response, session);
				// display the home page
				ModelAndView loginView = new ModelAndView("redirect:/index.html");
				return loginView;
			}
		} catch (LoginFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// display the login page
		ModelAndView loginView = new ModelAndView("redirect:/user/login.html");
		loginView.addObject("userName", userName);
		return loginView;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(String needCaptcha, String userName) {
		ModelAndView view = new ModelAndView("user/login");
		view.addObject("needCaptcha", needCaptcha);
		view.addObject("userName", userName);
		String uuid = UUIDGenerator.getUUID();
		view.addObject("uuid", uuid);

		return view;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView signup(String userName, String password, String password2, String email, String termCheck, HttpServletResponse response,
			HttpSession session) {
		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		user.setEmail(email);
		if (!"true".equals(termCheck)) {
			ModelAndView view = getSignUpView(user);
			String msg = "请认真阅读并同意经商知道的《使用协议》";
			view.addObject("msg", msg);
			return view;
		}
		if (userName == null || userName.trim().length() < 6 || userName.trim().length() > 16) {
			ModelAndView view = getSignUpView(user);
			String msg = "请输入长度为 6 到 16 个字符的用户名";
			view.addObject("msg", msg);
			return view;
		}
		if (!checkUserName(userName)) {
			ModelAndView view = getSignUpView(user);
			String msg = "用户名只能包含字母和数字, 且必须以字母开始";
			view.addObject("msg", msg);
			return view;
		}
		if (password == null || password.length() < 6 || password.length() > 30) {
			ModelAndView view = getSignUpView(user);
			String msg = "请使用长度应在 6 到 30 之间的密码";
			view.addObject("msg", msg);
			return view;
		}
		if (!password.equals(password2)) {
			ModelAndView view = getSignUpView(user);
			String msg = "请重复输入密码";
			view.addObject("msg", msg);
			return view;
		}
		user.setUserName(userName.trim());
		if (email == null || email.trim().length() == 0) {
			ModelAndView view = getSignUpView(user);
			String msg = "请输入电子邮件地址";
			view.addObject("msg", msg);
			return view;
		}
		if (!checkEmail(email)) {
			ModelAndView view = getSignUpView(user);
			String msg = "电子邮件格式不正确";
			view.addObject("msg", msg);
			return view;
		}
		boolean b = userService.checkEmail(email);
		if (!b) {
			ModelAndView view = getSignUpView(user);
			String msg = "电子邮件地址已被注册, 请使用其他的邮件地址";
			view.addObject("msg", msg);
			return view;
		}
		try {
			User result = userService.signup(user);
			emailHelper.sendLoginSuccessMail(userName, email);
			if (result != null) {
				// user login
				storeLoginedUser(result, response, session);
				// display the home page
				ModelAndView loginView = new ModelAndView("redirect:/index.html");
				return loginView;
			} else {
				// display the login page
				ModelAndView loginView = new ModelAndView("user/login");
				loginView.addObject("userName", userName);
				loginView.addObject("password", password);
				return loginView;
			}
		} catch (DuplicateUserNameException e) {
			ModelAndView view = getSignUpView(user);
			String msg = "用户名已存在, 请重新输入";
			view.addObject("msg", msg);
			return view;
		} catch (LoginFailedException e) {
			ModelAndView view = getSignUpView(user);
			String msg = "注册失败, 请稍后再试或联系管理员";
			view.addObject("msg", msg);
			return view;
		}
//		// display the login page
//		ModelAndView loginView = new ModelAndView("user/signup");
//		loginView.addObject("userName", userName);
//		loginView.addObject("password", password);
//		loginView.addObject("email", email);
//		return loginView;
	}

	private boolean checkUserName(String name) {
		String reg = "[a-zA-Z][a-zA-Z\\d]+";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(name);
		return m.matches();
	}
	
	private boolean checkEmail(String email) {
		String reg = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	
	private ModelAndView getSignUpView(User user) {
		ModelAndView view = new ModelAndView("user/login");
		String uuid = UUIDGenerator.getUUID();
		view.addObject("user", user);
		view.addObject("uuid", uuid);
		return view;
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView showSignup() {
		ModelAndView view = new ModelAndView("user/login");
		String uuid = UUIDGenerator.getUUID();
		view.addObject("uuid", uuid);
		return view;
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpSession session, HttpServletResponse response) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			// clear session info
			userService.logout(user);
			session.invalidate();
		}
		// clear cookie
		Cookie cookie = new Cookie("token", "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		// display the home page
		return "redirect:/index.html";
	}

	@RequestMapping(value = "/modify/pwd", method = RequestMethod.GET)
	public String modifyPassword() {
		return "user/changePassword";
	}

	@RequestMapping(value = "/modify/pwd", method = RequestMethod.POST)
	public ModelAndView modifyPassword(String oldPassword, String newPassword, String newPassword2, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/index.html");
		}
		
		ModelAndView view = new ModelAndView("user/changePassword");
		if (newPassword == null || newPassword.length() < 6 || newPassword.length() > 30) {
			String msg = "请使用长度应在 6 到 30 之间的密码";
			view.addObject("msg", msg);
			return view;
		}
		if (!newPassword.equals(newPassword2)) {
			String msg = "请重复输入密码";
			view.addObject("msg", msg);
			return view;
		}
		user.setPassword(oldPassword);
		boolean b = userService.changePassword(user, newPassword);
		if (b) {
			return new ModelAndView("redirect:/index.html");
		}
		return view;
	}

	@RequestMapping("/address")
	public ModelAndView address(HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/index.html");
		}
		ModelAndView view = new ModelAndView("user/address");
		view.addObject("user", user);
		return view;
	}

	@RequestMapping("/modifyAddress")
	public String modifyAddress(String country, String city, String address, String zip, String longi, String lati,
			HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/index.html";
		}
		int longiL = 0, latiL = 0;
		if (longi != null) {
			if (longi.contains(".")) {
				longi = longi.substring(0, longi.indexOf("."));
			}
			longiL = Integer.parseInt(longi);
		}
		if (lati != null) {
			if (lati.contains(".")) {
				lati = lati.substring(0, lati.indexOf("."));
			}
			latiL = Integer.parseInt(lati);
		}
		// Address oldAddr = user.getAddress();
		// Address addr = new Address();
		user.setCountry(country);
		user.setCity(city);
		user.setAddress(address);
		user.setZip(zip);
		user.setLongi(longiL);
		user.setLati(latiL);
		// user.setAddress(addr);
		boolean b = userService.updateUserInfo(user);
		if (b) {
			return "redirect:/index.html";
		} else {
			// user.setAddress(oldAddr);
			return "redirect:/index.html";
		}
	}

	@RequestMapping("profile/{id}")
	public ModelAndView profile(@PathVariable int id) {
		User user = userService.getUserById(id);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Shop shop = shopService.getShopByUserId(id);
		ModelAndView view = new ModelAndView("user/profile");
		view.addObject("user", user);
		view.addObject("shop", shop);
		return view;
	}
	
	@RequestMapping("unbindWeibo")
	public String unbindWeibo(HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		User updatedUser = userService.unbindWeibo(user.getId());
		if (updatedUser != null) {
			session.setAttribute(SessionConstants.SESSION_ATTR_USER, updatedUser);
		}
		return "redirect:/user/profile/" + user.getId() + ".html";
	}
	
	@RequestMapping("unbindFacebook")
	public String unbindFacebook(HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		User updatedUser = userService.unbindFacebook(user.getId());
		if (updatedUser != null) {
			session.setAttribute(SessionConstants.SESSION_ATTR_USER, updatedUser);
		}
		return "redirect:/user/profile/" + user.getId() + ".html";
	}
	
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	public ModelAndView messages(HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/index.html");
		}
		int unreadNum = messageService.getUserUnreadMessageNumber(user.getId());
		List<Message> list = messageService.getMessageList(user.getId());
		ModelAndView view = new ModelAndView("user/message");
		view.addObject("user", user);
		view.addObject("list", list);
		view.addObject("unreadNum", unreadNum);
		return view;
	}

	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public ModelAndView message(int to, HttpSession session) {
		User user = userService.getUserById(to);
		User sourceUser = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (sourceUser == null || user == null) {
			return new ModelAndView("redirect:/index.html");
		}
		List<Message> list = messageService.getMessageByUserId(sourceUser.getId(), to);
		ModelAndView view = new ModelAndView("user/sendMsg");
		view.addObject("user", user);
		view.addObject("list", list);
		return view;
	}

	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public String message(int destId, String title, String content, HttpSession session) {
		if (content == null || content.length() == 0 || content.length() > 160) {
			return "redirect:/user/message.html?to=" + destId;
		}
		content = ValidateUtil.escapeTag(content);
		User destUser = userService.getUserById(destId);
		User sourceUser = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (sourceUser == null || destUser == null) {
			return "redirect:/index.html";
		}
		title = "msg title";
		Message message = new Message();
		message.setTitle(title);
		message.setContent(content);
		message.setSource(sourceUser.getUserName());
		message.setSourceId(sourceUser.getId());
		message.setDest(destUser.getUserName());
		message.setDestId(destId);
		boolean b = messageService.insertMessage(message);
		if (b) {
			return "redirect:/user/message.html?to=" + destId;
		} else {
			return "redirect:/index.html";
		}
	}

	@RequestMapping("/contactor")
	public ModelAndView contactor(HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/index.html");
		}
		List<UserContactor> list = messageService.getUserContactor(user.getId());
		ModelAndView view = new ModelAndView("user/contactor");
		view.addObject("list", list);
		return view;
	}

	@RequestMapping("/login/fb")
	public ModelAndView fbLogin() {
		ModelAndView view = new ModelAndView("user/fbLogin");
		String uuid = UUIDGenerator.getUUID();
		view.addObject("uuid", uuid);
		return view;
	}

	@RequestMapping("/fb/login/success")
	public ModelAndView fbLoginSuccess(String code, String state, HttpServletResponse response, HttpSession session) {
		String url = "https://graph.facebook.com/oauth/access_token?client_id=144269832392534&client_secret=905798b5d1c1f64098fa321af1b03f66&code="
				+ code + "&redirect_uri=http://weindex.ca/user/fb/login/success.html";

		ModelAndView view = new ModelAndView("user/fbSuccess");

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse resp = httpclient.execute(get);
			HttpEntity entity = resp.getEntity();
			InputStream is = entity.getContent();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte b;
			while ((b = (byte) is.read()) != -1) {
				os.write(b);
			}
			is.close();
			byte[] bt = os.toByteArray();
			String content = new String(bt);
			view.addObject("content", content);
			String queryUrl = "https://graph.facebook.com/me?" + content;
			JSONObject obj = JsonHelper.get(queryUrl);
			if (obj != null) {
				String id = obj.getString("id");
				String name = obj.getString("name");
				String link = obj.getString("link");
				view.addObject("uid", id);
				view.addObject("name", name);
				view.addObject("desc", link);

				// check if the fb id exists
				User user = userService.getUserByFbId(id);
				if (user != null) {
					user = userService.fbLogin(user);
					storeLoginedUser(user, response, session);
					// display the home page
					ModelAndView loginView = new ModelAndView("redirect:/index.html");
					return loginView;
				} else {
					user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
					if (user != null) {
						user.setFacebookId(id);
						userService.updateUserFbId(user);
						return new ModelAndView("redirect:/user/profile/" + user.getId() + ".html");
					}
					// no user use this fb id
					// store the id into session
					session.setAttribute("fbid", id);
					// store the id into cookie
					Cookie cookie = new Cookie("fbid", id);
					cookie.setMaxAge(1000 * 60 * 60 * 24);
					cookie.setPath("/");
					response.addCookie(cookie);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}
		return view;
	}

	@RequestMapping(value = "/fb/bind", method = RequestMethod.GET)
	public ModelAndView fbBindUser(String needCaptcha, HttpServletRequest request, HttpSession session) {
		String fbid = getFbId(request, session);
		if (fbid == null) {
			return new ModelAndView("redirect:/index.html");
		}
		ModelAndView view = new ModelAndView("user/fbBind");
		view.addObject("needCaptcha", needCaptcha);
		return view;

	}

	@RequestMapping(value = "/fb/bind", method = RequestMethod.POST)
	public String fbBindUser(String userName, String password, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String fbid = getFbId(request, session);
		if (fbid == null) {
			return "redirect:/index.html";
		}

		int failedTimes = userService.getUserFailedTimes(userName);
		if (failedTimes >= 5) {
			ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
			reCaptcha.setPrivateKey("6Lft9doSAAAAANdlvt-99gUxrSkfy-6n6w0i9ss-");
			String remoteAddr = request.getRemoteAddr();
			String challenge = request.getParameter("recaptcha_challenge_field");
			String uresponse = request.getParameter("recaptcha_response_field");
			if (challenge == null || challenge.length() == 0 || uresponse == null || uresponse.length() == 0) {
				// we need the captcha
				return "redirect:/user/fb/bind.html?needCaptcha=true";
			}
			ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
			if (!reCaptchaResponse.isValid()) {
				return "redirect:/index.html";
			}
		}
		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		try {
			User result = userService.login(user);
			if (result != null) {
				if (adminHelper.isAdmin(result)) {
					result.setAdmin(true);
				}

				result.setFacebookId(fbid);
				boolean b = userService.updateUserFbId(result);
				if (b) {
					// user login
					storeLoginedUser(result, response, session);
					// display the home page
					return "redirect:/index.html";
				}
			}
		} catch (LoginFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// display the login page
		return "redirect:/user/fb/bind.html?userName=" + userName;

	}

	@RequestMapping(value = "/fb/create", method = RequestMethod.GET)
	public String fbCreateUser(HttpServletRequest request, HttpSession session) {
		String fbid = getFbId(request, session);
		if (fbid == null) {
			return "redirect:/index.html";
		} else {
			return "user/fbCreate";
		}
	}

	@RequestMapping(value = "/fb/create", method = RequestMethod.POST)
	public ModelAndView fbCreateUser(String userName, String password, String password2, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		if (userName == null || userName.trim().length() < 6 || userName.trim().length() > 16) {
			ModelAndView view = new ModelAndView("user/fbCreate");
			String msg = "请输入长度为 6 到 16 个字符的用户名";
			view.addObject("msg", msg);
			view.addObject("userName", userName);
			return view;
		}
		if (!checkUserName(userName)) {
			ModelAndView view = new ModelAndView("user/fbCreate");
			String msg = "用户名只能包含字母和数字, 且必须以字母开始";
			view.addObject("msg", msg);
			view.addObject("userName", userName);
			return view;
		}
		if (password == null || password.length() < 6 || password.length() > 30) {
			ModelAndView view = new ModelAndView("user/fbCreate");
			String msg = "请使用长度应在 6 到 30 之间的密码";
			view.addObject("msg", msg);
			view.addObject("userName", userName);
			return view;
		}
		if (!password.equals(password2)) {
			ModelAndView view = new ModelAndView("user/fbCreate");
			String msg = "请重复输入密码";
			view.addObject("msg", msg);
			view.addObject("userName", userName);
			return view;
		}
		boolean b = userService.checkUserName(userName);
		if (!b) {
			ModelAndView view = new ModelAndView("user/fbCreate");
			String msg = "该用户名已存在";
			view.addObject("msg", msg);
			view.addObject("userName", userName);
			return view;
		}
		String fbid = getFbId(request, session);
		if (fbid == null) {
			return new ModelAndView("redirect:/index.html");
		}
		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		user.setFacebookId(fbid);
		try {
			User result = userService.fbSignup(user);
			if (result != null) {
				storeLoginedUser(result, response, session);
			}
		} catch (DuplicateUserNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ModelAndView view = new ModelAndView("user/fbCreate");
			String msg = "该用户名已存在";
			view.addObject("msg", msg);
			view.addObject("userName", userName);
			return view;
		} catch (LoginFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ModelAndView("redirect:/index.html");
		}
		return new ModelAndView("redirect:/index.html");
	}

	private String getFbId(HttpServletRequest request, HttpSession session) {
		String fbid = (String) session.getAttribute("fbid");
		if (fbid == null) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				String name = c.getName();
				if ("fbid".equals(name)) {
					if (fbid.equals(c.getValue())) {
						return fbid;
					}
				}
			}
		}
		return null;
	}

	@RequestMapping("/login/weibo")
	public String weiboLogin() {
		return "user/weiboLogin";
	}

	@RequestMapping("/weibo/login/success")
	public ModelAndView weiboLoginSuccess(String code, HttpServletResponse response, HttpSession session) {
		String redirectUri = "http://weindex.ca/user/weibo/login/success.html";
		try {
			redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
		} catch (Exception e) {
			// do nothing
		}
		String url = "https://api.weibo.com/oauth2/access_token?client_id=198216231&client_secret=2eb180f1785ef140a1cbe8764c48fee0&grant_type=authorization_code&redirect_uri="
				+ redirectUri + "&code=" + code;
		JSONObject obj = JsonHelper.post(url);
		ModelAndView view = new ModelAndView("user/weiboSuccess");
		if (obj != null) {
			String accessToken = obj.getString("access_token");
			String uid = obj.getString("uid");
			view.addObject("token", accessToken);
			// System.out.println("=====-------------" + accessToken);
			view.addObject("uid", uid);
			String queryUrl = "https://api.weibo.com/2/users/show.json?access_token=" + accessToken + "&uid=" + uid;
			JSONObject testObj = JsonHelper.get(queryUrl);
			if (testObj != null) {
				String name = testObj.getString("screen_name");
				String desc = testObj.getString("location");
				view.addObject("name", name);
				view.addObject("desc", desc);
			}

			// check if the weibo id exists
			User user = userService.getUserByWeiboId(uid);
			if (user != null) {
				try {
					user = userService.weiboLogin(user);
					user.setWeiboToken(accessToken);
					userService.updateUserWeiboToken(user);
					storeLoginedUser(user, response, session);
					// display the home page
					ModelAndView loginView = new ModelAndView("redirect:/index.html");
					return loginView;
				} catch (Exception e) {
					ModelAndView loginView = new ModelAndView("redirect:/index.html");
					return loginView;
				}
			} else {
				user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
				if (user != null) {
					user.setWeiboId(uid);
					boolean b = userService.updateUserWeiboId(user);
					if (b) {
						user.setWeiboToken(accessToken);
						b = userService.updateUserWeiboToken(user);
					}
					return new ModelAndView("redirect:/user/profile/" + user.getId() + ".html");
				}
				// no user use this fb id
				// store the id into session
				session.setAttribute("weiboid", uid);
				session.setAttribute("weibotoken", accessToken);
				// store the id into cookie
				Cookie cookie = new Cookie("weiboid", uid);
				cookie.setMaxAge(1000 * 60 * 60 * 24);
				cookie.setPath("/");
				response.addCookie(cookie);
			}

		}
		return view;
	}


	@RequestMapping(value = "/weibo/bind", method = RequestMethod.GET)
	public ModelAndView weiboBindUser(String needCaptcha, HttpServletRequest request, HttpSession session) {
		String weiboId = getWeiboId(request, session);
		if (weiboId == null) {
			return new ModelAndView("redirect:/index.html");
		}
		ModelAndView view = new ModelAndView("user/weiboBind");
		view.addObject("needCaptcha", needCaptcha);
		return view;

	}

	@RequestMapping(value = "/weibo/bind", method = RequestMethod.POST)
	public String weiboBindUser(String userName, String password, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String weiboId = getWeiboId(request, session);
		if (weiboId == null) {
			return "redirect:/index.html";
		}
		String accessToken = (String) session.getAttribute("weibotoken");

		int failedTimes = userService.getUserFailedTimes(userName);
		if (failedTimes >= 5) {
			ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
			reCaptcha.setPrivateKey("6Lft9doSAAAAANdlvt-99gUxrSkfy-6n6w0i9ss-");
			String remoteAddr = request.getRemoteAddr();
			String challenge = request.getParameter("recaptcha_challenge_field");
			String uresponse = request.getParameter("recaptcha_response_field");
			if (challenge == null || challenge.length() == 0 || uresponse == null || uresponse.length() == 0) {
				// we need the captcha
				return "redirect:/user/fb/bind.html?needCaptcha=true";
			}
			ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
			if (!reCaptchaResponse.isValid()) {
				return "redirect:/index.html";
			}
		}
		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		try {
			User result = userService.login(user);
			if (result != null) {
				if (adminHelper.isAdmin(result)) {
					result.setAdmin(true);
				}

				result.setWeiboId(weiboId);
				result.setWeiboToken(accessToken);
				// set weibo Show verify ID
				// it's calculated based on hex_md5 of the weibo register date (last 8 numbers)
				//ca.weindex.weibo4j.model.User um = new User();
				//um.client.setToken(access_token);
				
				boolean b = userService.updateUserWeiboId(result);
				if (b) {
					// user login
					storeLoginedUser(result, response, session);
					// display the home page
					return "redirect:/index.html";
				}
			}
		} catch (LoginFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// display the login page
		return "redirect:/user/weibo/bind.html?userName=" + userName;

	}

	@RequestMapping(value = "/weibo/create", method = RequestMethod.GET)
	public String weiboCreateUser(HttpServletRequest request, HttpSession session) {
		String weiboid = getWeiboId(request, session);
		if (weiboid == null) {
			return "redirect:/index.html";
		} else {
			return "user/weiboCreate";
		}
	}

	@RequestMapping(value = "/weibo/create", method = RequestMethod.POST)
	public ModelAndView weiboCreateUser(String userName, String password, String password2, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		if (userName == null || userName.trim().length() < 6 || userName.trim().length() > 16) {
			ModelAndView view = new ModelAndView("user/weiboCreate");
			String msg = "请输入长度为 6 到 16 个字符的用户名";
			view.addObject("msg", msg);
			view.addObject("userName", userName);
			return view;
		}
		if (!checkUserName(userName)) {
			ModelAndView view = new ModelAndView("user/weiboCreate");
			String msg = "用户名只能包含字母和数字, 且必须以字母开始";
			view.addObject("msg", msg);
			view.addObject("userName", userName);
			return view;
		}
		if (password == null || password.length() < 6 || password.length() > 30) {
			ModelAndView view = new ModelAndView("user/weiboCreate");
			String msg = "请使用长度应在 6 到 30 之间的密码";
			view.addObject("msg", msg);
			view.addObject("userName", userName);
			return view;
		}
		if (!password.equals(password2)) {
			ModelAndView view = new ModelAndView("user/weiboCreate");
			String msg = "请重复输入密码";
			view.addObject("msg", msg);
			view.addObject("userName", userName);
			return view;
		}
		boolean b = userService.checkUserName(userName);
		if (!b) {
			ModelAndView view = new ModelAndView("user/weiboCreate");
			String msg = "该用户名已存在";
			view.addObject("msg", msg);
			view.addObject("userName", userName);
			return view;
		}
		String weiboId = getWeiboId(request, session);
		if (weiboId == null) {
			return new ModelAndView("redirect:/index.html");
		}
		String accessToken = (String) session.getAttribute("weibotoken");
		
		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		user.setWeiboId(weiboId);
		user.setWeiboToken(accessToken);
		try {
			User result = userService.weiboSignup(user);
			if (result != null) {
				storeLoginedUser(result, response, session);
			}
		} catch (DuplicateUserNameException e) {
			ModelAndView view = new ModelAndView("user/weiboCreate");
			String msg = "该用户名已存在";
			view.addObject("msg", msg);
			view.addObject("userName", userName);
			return view;
		} catch (LoginFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ModelAndView("redirect:/index.html");
		}
		return new ModelAndView("redirect:/index.html");
	}

	private String getWeiboId(HttpServletRequest request, HttpSession session) {
		String weiboId = (String) session.getAttribute("weiboid");
		if (weiboId == null) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				String name = c.getName();
				if ("weiboid".equals(name)) {
					if (weiboId.equals(c.getValue())) {
						return weiboId;
					}
				}
			}
		}
		return null;
	}


	private void storeLoginedUser(User user, HttpServletResponse response, HttpSession session) {
		// user login
		Cookie cookie = new Cookie("token", user.getToken());
		cookie.setMaxAge(1000 * 60 * 60 * 24 * 365); // one year
		cookie.setPath("/");
		response.addCookie(cookie);
		session.setAttribute(SessionConstants.SESSION_ATTR_USER, user);
	}
	
	@RequestMapping("showResetPwd")
	public String showResetPwd() {
		return "user/resetPwd";
	}
	
	@RequestMapping(value="showResetPwd", method=RequestMethod.POST)
	public ModelAndView requestResetPwd(String name) {
		String msg;
		ModelAndView view;
		if (name == null || name.trim().length() == 0) {
			msg = "请输入用户名";
			view = new ModelAndView("user/resetPwd");
			view.addObject("message", msg);
			return view;
		}
		User user = userService.getUserByName(name);
		if (user == null) {
			msg = "请输入正确的用户名";
			view = new ModelAndView("user/resetPwd");
			view.addObject("message", msg);
			return view;
		}
		String email = user.getEmail();
		if (email == null || email.length() == 0 || !email.contains("@")) {
			msg = "该用户未正确设置电子邮件地址, 无法找回密码";
			view = new ModelAndView("user/resetPwd");
			view.addObject("message", msg);
			view.addObject("name", name);
			return view;
		}
		String token = UUIDGenerator.getUUID();
		user.setLostPwdToken(token);
		boolean b = userService.updateLostPwdToken(user);
		if (!b) {
			msg = "系统出现错误, 请重试";
			view = new ModelAndView("user/resetPwd");
			view.addObject("message", msg);
			view.addObject("name", name);
			return view;
		}
		b = emailHelper.sendLostPwdMail(name, email, token);
		if (!b) {
			msg = "邮件发送失败, 请重试";
			view = new ModelAndView("user/resetPwd");
			view.addObject("message", msg);
			view.addObject("name", name);
			return view;
		}
		view = new ModelAndView("user/resetPwdSubmitted");
		return view;
	}
	
	@RequestMapping(value="resetPwd", method=RequestMethod.GET)
	public ModelAndView resetPwd(String name, String token) {
		if (name == null || name.length() == 0 || token == null || token.length() == 0) {
			ModelAndView view = new ModelAndView("redirect:/");
			return view;
		}
		User user = userService.getUserByLostPwdToken(token);
		String msg;
		ModelAndView view;
		if (user == null) {
			msg = "提交的 token 错误或已过期, 请重试";
			view = new ModelAndView("user/resetPwd");
			view.addObject("message", msg);
			return view;
		}
		if (!user.getUserName().equals(name)) {
			msg = "提交的用户名不正确, 请重试";
			view = new ModelAndView("user/resetPwd");
			view.addObject("name", name);
			view.addObject("message", msg);
			return view;
		}
		view = new ModelAndView("user/resetPwdFrm");
		view.addObject("userid", user.getId());
		view.addObject("token", token);
		return view;
	}
	
	@RequestMapping(value="resetPwd", method=RequestMethod.POST)
	public ModelAndView submitResetPwd(int id, String token, String password, String password2) {
		ModelAndView view = new ModelAndView("user/resetPwdFrm");
		view.addObject("token", token);
		view.addObject("userid", id);
		String msg;
		if (password == null || password.length() < 6) {
			msg = "请输入不少于 6 个字符的密码";
			view.addObject("message", msg);
			return view;
		}
		if (!password.equals(password2)) {
			msg = "请正确重复输入密码";
			view.addObject("message", msg);
			return view;
		}
		User user = userService.getUserByLostPwdToken(token);
		if (user == null) {
			msg = "提交的 token 错误或已过期, 请重试";
			view = new ModelAndView("user/resetPwd");
			view.addObject("message", msg);
			return view;
		}
		if (user.getId() != id) {
			msg = "提交的信息有错误, 请重试";
			view = new ModelAndView("user/resetPwd");
			view.addObject("message", msg);
			return view;
		}
		user.setPassword(password);
		user.setToken(token);
		boolean b = userService.updatePasswordByLostPwdToken(user);
		if (!b) {
			msg = "重置密码失败, 请重试";
			view.addObject("message", msg);
			return view;
		}
		view = new ModelAndView("redirect:/user/login.html");
		return view;
	}
}
