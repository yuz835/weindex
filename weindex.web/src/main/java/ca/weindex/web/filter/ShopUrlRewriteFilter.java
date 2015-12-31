package ca.weindex.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ShopUrlRewriteFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest hsr = (HttpServletRequest) request;
		String uri = hsr.getRequestURI();
		if (uri != null && uri.length() > 0 && !uri.equals("/") && !uri.contains(".") && uri.indexOf("/", 1) < 0) {
			uri = "/shop/view" + uri + ".html";
			String queryString = hsr.getQueryString();
			if (queryString != null && queryString.length() > 0) {
				// uri = uri + "?" + queryString;
			}
			hsr.getRequestDispatcher(uri).forward(request, response);
			return;
		} else if (uri != null && uri.startsWith("/offerimage/show/")) {
			// view image
			String imageName = uri.substring(17);
			String type = "jpg";
			if (imageName.indexOf(".") != -1) {
				type = imageName.substring(imageName.lastIndexOf(".") + 1);
				imageName = imageName.substring(0, imageName.lastIndexOf("."));
			}
			String imgUrl = "/offer/image/" + imageName + "/" + type + "/show.html";
			hsr.getRequestDispatcher(imgUrl).forward(request, response);
			return;
		} else if (uri != null && (uri.startsWith("/blogimage/show/") || uri.startsWith("/shopimage/show/"))) {
			// view image
			String imageName = uri.substring(16);
			String type = "jpg";
			if (imageName.indexOf(".") != -1) {
				type = imageName.substring(imageName.lastIndexOf(".") + 1);
				imageName = imageName.substring(0, imageName.lastIndexOf("."));
			}
			String imgUrl = "/blog/image/" + imageName + "/" + type + "/show.html";
			hsr.getRequestDispatcher(imgUrl).forward(request, response);
			return;
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
