package ca.weindex.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

public class MyLocaleResolver extends AbstractLocaleResolver {

	public Locale resolveLocale(HttpServletRequest request) {
		String domain = request.getServerName();
		String l = null;
		if (domain.startsWith("cn.")) {
			l = "zh_CN";
		} else if (domain.startsWith("en.")) {
			l = "en_US";
		} else {
			l = request.getParameter("lang");
			if (l == null || l.length() == 0) {
				l = "zh_CN";
			}
		}
		Locale locale = new Locale(l);
		return locale;
	}

	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		// TODO Auto-generated method stub

	}

}
