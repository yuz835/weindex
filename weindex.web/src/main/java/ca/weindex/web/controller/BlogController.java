package ca.weindex.web.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;



import ca.weindex.common.constants.SessionConstants;
import ca.weindex.common.enums.LabelTypeEnum;
import ca.weindex.common.model.Blog;
import ca.weindex.common.model.BlogComment;
import ca.weindex.common.model.BlogImage;
import ca.weindex.common.model.Label;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;
import ca.weindex.common.model.UserFavoriteBlog;
import ca.weindex.common.util.UUIDGenerator;
import ca.weindex.common.util.Ubb2html;
import ca.weindex.services.BlogService;
import ca.weindex.services.FavoriteService;
import ca.weindex.services.LabelService;
import ca.weindex.services.ShopService;
import ca.weindex.services.UserService;
import ca.weindex.web.helper.LabelHelper;
import ca.weindex.web.helper.WeiboHelper;
import ca.weindex.web.util.ValidateUtil;

import com.alibaba.fastjson.JSON;
import com.sun.mail.imap.protocol.Status;

@Controller
@RequestMapping("blog")
public class BlogController {
	@Autowired
	private ShopService shopService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private LabelService labelService;

	@Autowired
	private FavoriteService favoriteService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(int shopId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Shop shop = shopService.getShop(shopId);
		if (shop == null || shop.getUserId() != user.getId()) {
			return new ModelAndView("redirect:/");
		}
		ModelAndView view = new ModelAndView("blog/add");
		view.addObject("shop", shop);
		List<Label> labelList = labelService.getLabelByType(LabelTypeEnum.BLOG);
		view.addObject("labelList", labelList);
		List<Label> allLabelList = labelService.getLabel();
		view.addObject("allLabelList", allLabelList);
		return view;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(int shopId, String shopName, String title, String summary, String label, String shopLabel, String content,
			HttpSession session) {
		if (title == null || title.length() == 0) {
			return "redirect:/";
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		Shop shop = shopService.getShop(shopId);
		if (shop == null || shop.getUserId() != user.getId()) {
			return "redirect:/";
		}
		Blog blog = new Blog();
		blog.setAuthor(user.getUserName());
		blog.setShopId(shopId);
		blog.setTitle(title);
		blog.setSummary(summary);
		blog.setLabel(label);
		blog.setShopLabel(shopLabel);
		blog.setContent(content);
		blog = ValidateUtil.truncateBlog(blog);
		boolean b = blogService.insertBlog(blog);
		if (b) {
			return "redirect:/" + shopName + "?type=blog";
		} else {
			return "redirect:/";
		}
	}

	@RequestMapping("/delete")
	public String delete(int blogId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		Blog blog = blogService.getBlog(blogId);
		if (blog != null) {
			Shop shop = shopService.getShopWithDetail(blog.getShopId());
			if (shop == null || shop.getUserId() != user.getId()) {
				return "redirect:/";
			}
			boolean b = blogService.deleteBlog(blogId, shop.getId());
			if (b) {
				return "redirect:/shop/view/" + shop.getName() + ".html?type=blog";
			}
		}
		return "redirect:/";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(int blogId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Blog blog = blogService.getBlog(blogId);
		if (blog != null) {
			Shop shop = shopService.getShopWithDetail(blog.getShopId());
			if (shop == null || shop.getUserId() != user.getId()) {
				return new ModelAndView("redirect:/");
			}
			ModelAndView view = new ModelAndView("blog/edit");
			view.addObject("blog", blog);
			view.addObject("shop", shop);
			List<Label> labelList = labelService.getLabelByType(LabelTypeEnum.BLOG);
			view.addObject("labelList", labelList);
			List<Label> myLabels = LabelHelper.convertLabel(blog.getLabel(), labelList);
			view.addObject("mylabels", myLabels);

			List<Label> shopLabels = LabelHelper.convertLabel(blog.getShopLabel(), shop.getShopLabelList());
			view.addObject("shopLabels", shopLabels);
			
			UserFavoriteBlog favBlog = new UserFavoriteBlog();
			favBlog.setBlogId(blogId);
			favBlog.setUserId(user.getId());
			boolean b = favoriteService.checkFavBlog(favBlog);
			view.addObject("favorited", b);

			return view;
		}
		ModelAndView view = new ModelAndView("redirect:/index.html");
		return view;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(int shopId, int blogId, String title, String summary, String label, String shopLabel, String content,
			HttpSession session) {
		if (title == null || title.length() == 0) {
			return "redirect:/";
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		Shop shop = shopService.getShop(shopId);
		if (shop == null || shop.getUserId() != user.getId()) {
			return "redirect:/";
		}
		Blog blog = new Blog();
		blog.setAuthor(user.getUserName());
		blog.setId(blogId);
		blog.setShopId(shopId);
		blog.setTitle(title);
		blog.setSummary(summary);
		blog.setLabel(label);
		blog.setShopLabel(shopLabel);
		blog.setContent(content);
		blog = ValidateUtil.truncateBlog(blog);
		boolean b = blogService.updateBlog(blog);
		if (b) {
			return "redirect:/blog/view/in/" + blogId + ".html";
		} else {
			return "redirect:/";
		}
	}

	@RequestMapping("/view/{id}")
	public ModelAndView view(@PathVariable int id, @ModelAttribute("page") Pagination page, HttpSession session) {
		Blog blog = blogService.getBlog(id);
		if (blog == null) {
			return new ModelAndView("redirect:/index.html");
		}
		String content = escapeHtml(blog.getContent());
		ModelAndView view = new ModelAndView("blog/view");
		// backwards to the previous image html
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
					content = pre + "<p> [img]" + imgUrl + "[/img]</p>" + post;
				}
				index = content.indexOf(tag, index + 1);
			}
		} 
		content = Ubb2html.decode(content);
		String[] ss = content.split("\\n");
		StringBuilder sb = new StringBuilder();
		for (String s : ss) {
			sb.append("<p>");
			sb.append(s);
			sb.append("</p>");
		}
		content = sb.toString();
		/*String blogImageUrl = null;
		if (content != null) {
			String[] ss = content.split("\\n");
			StringBuilder sb = new StringBuilder();
			for (String s : ss) {
				sb.append("<p>");
				sb.append(s);
				sb.append("</p>");
			}
			content = sb.toString();

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
					content = pre + "<p><img class=\"contentImage\" src=\"" + imgUrl + "\" /></p>" + post;
				}
				index = content.indexOf(tag, index + 1);
			}
		} */
		view.addObject("blog", blog);
		view.addObject("content", content);

		// weibo share
		String blogContent = blog.getContent();
		if (blogContent == null) {
			blogContent = "";
		} else {
			blogContent = escapeImgUrl(blogContent);
		};
		String pre = "【" + blog.getTitle() + "】 ";
		String post = "详情点击";
		//zhangeth2013/04/17String post = " http://weindex.ca/blog/view/" + blog.getId() + ".html";
		//zhangeth 2013/04/17 int length = 130 - (pre.length() + post.length());
		int length = 200 - (pre.length() + post.length());
		if (blogContent.length() > length) {
			blogContent = blogContent.substring(0, length - 3) + "...";
		}
		String weiboContent = pre + blogContent + post;
		try {
			weiboContent = URLEncoder.encode(weiboContent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// do nothing
		}
		view.addObject("blogShareContent", weiboContent);

		// get comment
		SearchResult<BlogComment> comments = blogService.getBlogComments(id, page);
		view.addObject("commentList", comments);

		int shopId = blog.getShopId();
		Shop shop = shopService.getShopWithDetail(shopId);
		view.addObject("shop", shop);
		List<Label> labelList = labelService.getLabel();
		view.addObject("labelList", labelList);
		List<Label> allLabelList = labelService.getLabel();
		view.addObject("allLabelList", allLabelList);
		List<Label> myLabels = LabelHelper.convertLabel(blog.getLabel(), labelList);
		view.addObject("mylabels", myLabels);

		List<Label> shopLabels = LabelHelper.convertLabel(blog.getShopLabel(), shop.getShopLabelList());
		view.addObject("shopLabels", shopLabels);
		
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			UserFavoriteBlog favBlog = new UserFavoriteBlog();
			favBlog.setBlogId(id);
			favBlog.setUserId(user.getId());
			boolean b = favoriteService.checkFavBlog(favBlog);
			view.addObject("favorited", b);
		} else {
			view.addObject("favorited", false);
		}

		return view;
	}

	private String escapeHtml(String src) {
		src = replace(src, "<", "&lt;");
		src = replace(src, ">", "&gt;");
		return src;
	}
	
	// zhangeth 2013/04/24
	// delete the img url when sharing to weibo
	private String escapeImgUrl(String content){
        	String tag = "[img:";
        	int index = content.indexOf(tag);
        	while (index != -1) {
        	        int postIndex = content.indexOf("]", index);
        	        if (postIndex != -1) {
        	                String pre = content.substring(0, index);
        	                String post = content.substring(postIndex + 1);
        	                content = pre + post;
        	        }
        	        index = content.indexOf(tag, index + 1);
        	}
		
		return content;

	}

	private String replace(String src, String from, String to) {
		if (src == null || from == null || from.length() == 0 || to == null) {
			return src;
		}
		while (src.indexOf(from) != -1) {
			int index = src.indexOf(from);
			String pre = src.substring(0, index);
			String post = src.substring(index + from.length());
			src = pre + to + post;
		}
		return src;
	}
	
	@RequestMapping(value = "uploadImage", method = RequestMethod.POST)
	public ModelAndView uploadImage(@RequestParam("file") MultipartFile file) {
		Map<String, String> result = new HashMap<String, String>();
		if (!file.isEmpty()) {
			try {
				byte[] bt = file.getBytes();
				//if (file.getSize() > 500000 || bt.length > 500000) {
					// file is too large
				//	ModelAndView view = new ModelAndView("json");
				//	result.put("success", "false");
				//	result.put("toolarge", "true");
				//	String content = JSON.toJSONString(result);
				//	view.addObject("content", content);
				//	return view;
				//}
				String fileName = file.getOriginalFilename();
				if (fileName.indexOf(".") != -1) {
					String type = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					String imgName = UUIDGenerator.getUUID().toLowerCase();
					BlogImage image = new BlogImage();
					image.setName(imgName);
					image.setType(type);
					//image.setImg(bt);

                    // Compress image 
					double thumbScale = 1.0;      
                    // 1. read the file stream, and get the height and width
                    BufferedImage thumbImage = Thumbnails.of(file.getInputStream())
                    .scale(1f) 
                    .asBufferedImage(); 
                    
                    if(thumbImage.getWidth() > 600){
                    	thumbScale = 600f/thumbImage.getWidth();
                  
                    	// 2. scale the image to max width/height 600	
                    	thumbImage = Thumbnails.of(thumbImage)
                    			.scale(thumbScale) 
                    			.asBufferedImage(); 
                    
                    	// 3. set the compressed image 
                    	ByteArrayOutputStream baos = new ByteArrayOutputStream( 1000 );
                    	ImageIO.write(thumbImage, type, baos );
                    	baos.flush();
                    	byte[] thumbImageBytes = baos.toByteArray();
                    	baos.close();
                    	image.setImg(thumbImageBytes);
                    } else {
    					image.setImg(bt);
                    }
					boolean b = blogService.insertBlogImage(image);
					if (b) {
						// update the offer's img url
						String imgUrl = "/blogimage/show/" + imgName + "." + type;
						ModelAndView view = new ModelAndView("json");
						result.put("success", "true");
						result.put("url", imgUrl);
						String content = JSON.toJSONString(result);
						view.addObject("content", content);
						return view;

					} else {
						throw new IOException();
					}
				} else {
					throw new IOException();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ModelAndView view = new ModelAndView("json");
		return view;
	}

	@RequestMapping("image/{name}/{type}/show")
	public ModelAndView showImage(@PathVariable String name, @PathVariable String type, HttpServletResponse response) {
		BlogImage image = blogService.getBlogImage(name, type);
		if (image != null) {
			if (type.equals("png")) {
				response.setContentType("image/png");
			} else if (type.equals("gif")) {
				response.setContentType("image/gif");
			} else {
				response.setContentType("image/jpeg");
			}
			response.setContentLength(image.getImg().length);
			try {
				ServletOutputStream output = response.getOutputStream();
				output.write(image.getImg());
				output.flush();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();

			}
			return null;
		} else {
			return null;
		}
	}

	@RequestMapping(value = "comment/add", method = RequestMethod.POST)
	public String addComment(int blogId, String title, String content, HttpServletRequest request, HttpSession session) {
		if (content == null || content.length() == 0 || content.length() > 150) {
			String referrer = request.getHeader("referer");
			return "redirect:" + referrer;
		}
		if (title == null || title.length() == 0) {
			title = "comment title";
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Blog blog = blogService.getBlog(blogId);
			if (blog == null) {
				return "redirect:/";
			}
			Shop shop = shopService.getShop(blog.getShopId());
			if (shop == null) {
				return "redirect:/";
			}
			int authorId = shop.getUserId();
			User author = userService.getUserById(authorId);
			if (author == null) {
				return "redirect:/";
			}
			BlogComment comment = new BlogComment();
			comment.setBlogId(blogId);
			comment.setTitle(title);
			comment.setContent(content);
			comment.setCreatorId(user.getId());
			comment.setCreatorName(user.getUserName());
			comment = ValidateUtil.truncateComment(comment);
			boolean b = blogService.insertBlogComment(comment, user, author);
			if (b) {
				return "redirect:/blog/view/"  + blogId + ".html";
			}
		}
		return "redirect:/blog/view/" + blogId + ".html";
	}
	
	@RequestMapping("comment/del")
	public String delComment(int id, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}

		BlogComment comment = blogService.getBlogComment(id);
		if (comment == null) {
			return "redirect:/";
		}
		Blog blog = blogService.getBlog(comment.getBlogId());
		if (blog == null) {
			return "redirect:/";
		}
		Shop shop = shopService.getShop(blog.getShopId());
		if (shop == null) {
			return "redirect:/";
		}
		if (shop.getUserId() != user.getId() && comment.getCreatorId() != user.getId()) {
			return "redirect:/";
		}
		boolean b = blogService.deleteBlogComment(id);
		if (b) {
			return "redirect:/blog/view/" + blog.getId() + ".html";
		} else {
			return "redirect:/blog/view/" + blog.getId() + ".html";
		}
	}
	
	@RequestMapping("weibo")
	public String publishWeibo( int blogId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null || user.getWeiboId() == null || user.getWeiboToken() == null) {
			return "redirect:/blog/view/" + blogId + ".html";
		}
		Blog blog = blogService.getBlog(blogId);
		if (blog == null) {
			return "redirect:/";
		}
		boolean b = WeiboHelper.publishBlogWeibo(blog, user, "");
		if (b) {
			return "redirect:/blog/view/" + blogId + ".html?weibo=true";
		}
		return "redirect:/blog/view/" +  blogId + ".html";
	}

	@RequestMapping("visitlogo/{id}")
	public String addBlogVisitNum(@PathVariable("id") int id ) {
		blogService.addBlogVisitNum(id);
		return "json";
	}
	
	@RequestMapping("top/{id}")
	public String top(@PathVariable("id") int id, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		Blog blog = blogService.getBlog(id);
		if (blog != null) {
			Shop shop = shopService.getShopWithDetail(blog.getShopId());
			if (shop == null || shop.getUserId() != user.getId()) {
				return "redirect:/";
			}
			blog.setPos(10);
			boolean b = blogService.topBlogOfShop(blog);
			if (b) {
				return "redirect:/blog/view/" + id + ".html";
			}
		}
		return "redirect:/";
	}
}
