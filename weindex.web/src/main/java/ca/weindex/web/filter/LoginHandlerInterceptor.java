package ca.weindex.web.filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ca.weindex.common.constants.SessionConstants;
import ca.weindex.common.model.User;
import ca.weindex.services.MessageService;
import ca.weindex.services.UserService;
import ca.weindex.web.helper.AdminHelper;

public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private AdminHelper adminHelper;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		User user = (User) session
				.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("token".equals(cookie.getName())) {
						String token = cookie.getValue();
						if (token != null && token.length() > 0) {
							user = userService.getUserByToken(token);
							if (user != null) {
								if (adminHelper.isAdmin(user)) {
									user.setAdmin(true);
								}
								session.setAttribute(
										SessionConstants.SESSION_ATTR_USER,
										user);
							}
						} else {
							user = null;
							cookie.setMaxAge(0);
							response.addCookie(cookie);
						}
						break;
					}
				}
			}
		}
		if (user != null) {
			// update user's message
			int num = messageService.getUserUnreadMessageNumber(user.getId());
			user.setUnreadedMessageNum(num);
		}
		return super.preHandle(request, response, handler);
	}

}
