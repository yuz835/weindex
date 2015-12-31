package ca.weindex.web;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

public class MyViewResolver extends VelocityLayoutViewResolver {

	@Override
	public View resolveViewName(String viewName, Locale locale)
			throws Exception {
		if (viewName.startsWith("redirect:")) {
			return super.resolveViewName(viewName, locale);
		}
		String lang = locale.getLanguage();
		String tempViewName = viewName;
		if (lang != null) {
			if (lang.contains("_")) {
				lang = lang.substring(0, lang.indexOf("_"));
			}
			tempViewName = tempViewName + "." + lang.toLowerCase();
		}
		View tempView = super.resolveViewName(tempViewName, locale);
		if (tempView == null) {
			View view = super.resolveViewName(viewName, locale);
			return view;
		} else {
			return tempView;
		}
	}

}
