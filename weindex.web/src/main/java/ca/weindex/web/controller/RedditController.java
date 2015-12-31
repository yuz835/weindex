package ca.weindex.web.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import ca.weindex.common.model.Reddit;
import ca.weindex.common.model.RedditComment;
import ca.weindex.common.model.UserLikedReddit;
import ca.weindex.common.model.UserDislikedReddit;
import ca.weindex.common.model.UserFavoriteReddit;
import ca.weindex.common.model.UserLikedComment;
import ca.weindex.common.model.UserDislikedComment;
import ca.weindex.common.model.BlogImage;
import ca.weindex.common.model.Label;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.User;
import ca.weindex.common.util.UUIDGenerator;
import ca.weindex.common.util.Ubb2html;
import ca.weindex.services.RedditService;
import ca.weindex.services.BlogService;
import ca.weindex.services.FavoriteService;
import ca.weindex.services.LabelService;
import ca.weindex.services.UserService;
//import ca.weindex.web.helper.*;
//import ca.weindex.web.util.*;
import ca.weindex.web.helper.LabelHelper;
import ca.weindex.web.helper.WeiboHelper;
import ca.weindex.web.util.ValidateUtil;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("link")
public class RedditController {

	@Autowired
	private RedditService redditService;

	@Autowired
	private BlogService blogService;
	
	@Autowired
	private LabelService labelService;

	@Autowired
	private FavoriteService favoriteService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		ModelAndView view = new ModelAndView("link/add");
		List<Label> labelList = labelService.getLabelByType(LabelTypeEnum.REDDIT);
		view.addObject("labelList", labelList);
		List<Label> allLabelList = labelService.getLabel();
		view.addObject("allLabelList", allLabelList);
		return view;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(String title,  String linkUrl, String description, String content, String label,
			HttpSession session) {
		int type = 0;
		if (title == null || title.length() == 0) {
			return "redirect:/";
		}
		System.out.println("Debug1");

		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		
		Reddit reddit = new Reddit();
		reddit.setAuthor(user.getUserName());
		reddit.setUserId(user.getId());
		reddit.setTitle(title);
		if(type == 0) { // reddit
			reddit.setLinkUrl(linkUrl);
			reddit.setDescription(description);
		} else { // article
			reddit.setContent(content);
		};
		reddit.setLabel(label);
		reddit = ValidateUtil.truncateReddit(reddit);
		boolean b = redditService.insertReddit(reddit);
		if (b) {
			int redditId = reddit.getId();
			return "redirect:/link/view/" + redditId + ".html";
		} else {
			return "redirect:/";

		}
	}

	@RequestMapping("/delete")
	public String delete(int redditId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		Reddit reddit = redditService.getReddit(redditId);
		if (reddit != null) {
			if (reddit.getUserId() != user.getId()) {
				return "redirect:/";
			}
			redditService.deleteReddit(redditId);
		}
		return "redirect:/";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(int redditId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Reddit reddit = redditService.getReddit(redditId);
		if (reddit != null) {
			if (reddit.getUserId() != user.getId()) {
				return new ModelAndView("redirect:/");
			}
			ModelAndView view = new ModelAndView("link/edit");
			view.addObject("link", reddit);
			List<Label> labelList = labelService.getLabelByType(LabelTypeEnum.REDDIT);
			view.addObject("labelList", labelList);
			List<Label> myLabels = LabelHelper.convertLabel(reddit.getLabel(), labelList);
			view.addObject("mylabels", myLabels);
			return view;
		}
		ModelAndView view = new ModelAndView("redirect:/index.html");
		return view;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(int redditId, String title, String linkUrl, String description, String content, String label,
			HttpSession session) {
		if (title == null || title.length() == 0) {
			return "redirect:/";
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		Reddit reddit = redditService.getReddit(redditId);
		if (reddit.getUserId() != user.getId()) {
			return "redirect:/";
		}
		//Reddit reddit = new Reddit();
		reddit.setAuthor(user.getUserName());
		reddit.setTitle(title);
		reddit.setLinkUrl(linkUrl);
		reddit.setDescription(description);
		reddit.setLabel(label);
		reddit.setContent(content);
		reddit = ValidateUtil.truncateReddit(reddit);
		boolean b = redditService.updateReddit(reddit);
		if (b) {
			return "redirect:/link/view/" + redditId + ".html";
		} else {
			return "redirect:/";
		}
	}

	@RequestMapping("/view/{id}")
	public ModelAndView view(@PathVariable int id, HttpSession session) {
		Reddit reddit = redditService.getReddit(id);
		Pagination page = new Pagination();
		page.setPageSize(10000); // we don't use page here, so set to a large value
		if (reddit == null) {
			return new ModelAndView("redirect:/index.html");
		}
		String content = escapeHtml(reddit.getContent());
		ModelAndView view = new ModelAndView("link/view");
		// backwards to the previous image html
		String redditImageUrl = null;
		if (content != null) {
			String tag = "[img:";
			int index = content.indexOf(tag);
			while (index != -1) {
				int postIndex = content.indexOf("]", index);
				if (postIndex != -1) {
					String pre = content.substring(0, index);
					String post = content.substring(postIndex + 1);
					String imgUrl = content.substring(index + tag.length(), postIndex);
					if (redditImageUrl == null) {
						redditImageUrl = "http://weindex.ca" + imgUrl;
						view.addObject("redditImageUrl", redditImageUrl);
					}
					content = pre + "<p> [img]" + imgUrl + "[/img]</p>" + post;
				}
				index = content.indexOf(tag, index + 1);
			}
		} 
		if(content != null) {
			content = Ubb2html.decode(content);
			String[] ss = content.split("\\n");
			StringBuilder sb = new StringBuilder();
			for (String s : ss) {
				sb.append("<p>");
				sb.append(s);
				sb.append("</p>");
			}
			content = sb.toString();
		};

		view.addObject("reddit", reddit);
		view.addObject("content", content);

		// weibo share
		String shareContent = null;
		if(reddit.getType() == 0) {
			shareContent = reddit.getDescription();
		} else {
			shareContent = reddit.getContent();
		};
		
		shareContent = escapeImgUrl(shareContent);
		
		String pre = "【" + reddit.getTitle() + "】 ";
		String post = "详情点击";
		int length = 200 - (pre.length() + post.length());
		if (shareContent.length() > length) {
			shareContent = shareContent.substring(0, length - 3) + "...";
		}
		String weiboContent = pre + shareContent + post;
		try {
			weiboContent = URLEncoder.encode(weiboContent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// do nothing
		}
		view.addObject("redditShareContent", weiboContent);

		// get comment
		SearchResult<RedditComment> comments = redditService.getRedditComments(id, page);
		// sort the list based on the score and level of each comment by using recursive algorithm
		List<RedditComment> orgCommentList = comments.getList();
		List<RedditComment> sortedCommentList = new ArrayList<RedditComment>();
		int parentCommentId = 0;
		while (orgCommentList.size() !=0 ) {
			RedditComment maxScoredComment = getMaxScoreComment(orgCommentList, parentCommentId);
			if(maxScoredComment != null) {
				sortedCommentList.add(maxScoredComment);
				orgCommentList.remove(maxScoredComment);
				parentCommentId = maxScoredComment.getId();
			} else { // 已经到根部，然后开始往回走
				for (RedditComment tmpComment: sortedCommentList) {
					if(tmpComment.getId() == parentCommentId) {
						parentCommentId = tmpComment.getTraceCommentId();
						break;
					};
				};
			};
		};
		
		comments.setList(sortedCommentList);
		view.addObject("commentList", comments);		
		return view;
	}

	private RedditComment getMaxScoreComment(List<RedditComment> orgCommentList, int parentCommentId) {
		List<RedditComment> curCommentList = new ArrayList<RedditComment>();
		for (RedditComment tmpComment: orgCommentList) {
			if(tmpComment.getTraceCommentId() == parentCommentId) {
				curCommentList.add(tmpComment);
			};
		};
		if(curCommentList.size() != 0) {
			RedditComment maxScoredComment = curCommentList.get(0);
			for (RedditComment tmpComment: curCommentList) {
				 if(maxScoredComment.getLikedNum() < tmpComment.getLikedNum()) {
					 maxScoredComment = tmpComment;
				 };
			};
			return maxScoredComment;
		} else {
			return null;
		}
	}
	

	private String escapeHtml(String src) {
		src = replace(src, "<", "&lt;");
		src = replace(src, ">", "&gt;");
		return src;
	}
	
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
	public String addComment(int redditId, int commentId, int level, String content, HttpServletRequest request, HttpSession session) {
		int parentCommentId = commentId;
		int parentLevel = level;
		if (content == null || content.length() == 0 || content.length() > 150) {
			String referrer = request.getHeader("referer");
			return "redirect:" + referrer;
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Reddit reddit = redditService.getReddit(redditId);
			if (reddit == null) {
				return "redirect:/";
			}

			int authorId = reddit.getUserId();
			User author = userService.getUserById(authorId);
			if (author == null) {
				return "redirect:/";
			}
			RedditComment comment = new RedditComment();
			comment.setRedditId(redditId);
			comment.setTraceCommentId(parentCommentId);
			comment.setLevel(parentLevel + 1);
			comment.setContent(content);
			comment.setCreatorId(user.getId());
			comment.setCreatorName(user.getUserName());
			comment = ValidateUtil.truncateComment(comment);
			boolean b = redditService.insertRedditComment(comment, user);
			if (b) {
				return "redirect:/link/view/" +  redditId + ".html";
			}
		}
		return "redirect:/link/view/" +  redditId + ".html";
	}
	
	@RequestMapping("comment/del")
	// actuallt it's wont delete the comment but just overrite the contents
	// as "本条评论已被作者删除..."
	public String delComment(int id, HttpSession session) {

		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}

		RedditComment comment = redditService.getRedditComment(id);
		if (comment == null) {
			return "redirect:/";
		}
		Reddit reddit = redditService.getReddit(comment.getRedditId());
		if (reddit == null) {
			return "redirect:/";
		}
		if (comment.getCreatorId() != user.getId()) {
			return "redirect:/";
		}
		String delContent = "本条评论已被" + user.getUserName() + "删除...";
		comment.setContent(delContent);
		boolean b = redditService.updateRedditComment(comment);
		//boolean b = redditService.deleteRedditComment(id);
		if (b) {
			return "redirect:/link/view/" + reddit.getId() + ".html";
		} else {
			return "redirect:/link/view/" + reddit.getId() + ".html";
		}
	}
	
	
	@RequestMapping("likecomment/{id}")
    public ModelAndView likeComment(@PathVariable("id") int id, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		ModelAndView view = new ModelAndView("json");
		Map<String, Object> result = new HashMap<String, Object>();
		if(user != null) {
			// check if user already liked it
			UserLikedComment likedComment = new UserLikedComment();
			likedComment.setCommentId(id);
			likedComment.setUserId(user.getId());
			likedComment.setType(LabelTypeEnum.REDDIT.getType());
			boolean liked = favoriteService.checkLikedComment(likedComment);
			if(liked == false) {
				// check if user already disliked it
				UserDislikedComment dislikedComment = new UserDislikedComment();
				dislikedComment.setCommentId(id);
				dislikedComment.setUserId(user.getId());
				dislikedComment.setType(LabelTypeEnum.REDDIT.getType());
				boolean disliked = favoriteService.checkDislikedComment(dislikedComment);
				if(disliked) {
					// remove from disliked, and increase likednum by 1
					// and delete from user disliked 
					redditService.likeRedditComment(id); //increase likednum by 1
					favoriteService.delDislikedComment(dislikedComment);
				}
				redditService.likeRedditComment(id);
				favoriteService.addLikedComment(likedComment);
			} else {
				redditService.dislikeRedditComment(id); //decrease likednum by 1
				favoriteService.delLikedComment(likedComment);
			}
			RedditComment redditComment = redditService.getRedditComment(id);
			result.put("likednum", redditComment.getLikedNum());
			result.put("result", "success");
		} else {
			result.put("result", "failed");
		}
		String content = JSON.toJSONString(result);
		view.addObject("content", content);
		return view;
	}

	
	@RequestMapping("dislikecomment/{id}")
    public ModelAndView dislikeComment(@PathVariable("id") int id, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		ModelAndView view = new ModelAndView("json");
		Map<String, Object> result = new HashMap<String, Object>();
		if(user != null) {
			// check if user already liked it
			UserDislikedComment dislikedComment = new UserDislikedComment();
			dislikedComment.setCommentId(id);
			dislikedComment.setUserId(user.getId());
			dislikedComment.setType(LabelTypeEnum.REDDIT.getType());
			boolean disliked = favoriteService.checkDislikedComment(dislikedComment);
			if(disliked == false) {
				// check if user already liked it
				UserLikedComment likedComment = new UserLikedComment();
				likedComment.setCommentId(id);
				likedComment.setUserId(user.getId());
				likedComment.setType(LabelTypeEnum.REDDIT.getType());
				boolean liked = favoriteService.checkLikedComment(likedComment);
				if(liked) {
					// remove from liked, and decrease likednum by 1
					// and delete from user liked 
					redditService.dislikeRedditComment(id); //decrease likednum by 1
					favoriteService.delLikedComment(likedComment);
				}
				redditService.dislikeRedditComment(id);
				favoriteService.addDislikedComment(dislikedComment);
			} else {
				redditService.likeRedditComment(id); //increase likednum by 1
				favoriteService.delDislikedComment(dislikedComment);
			}
			RedditComment redditComment = redditService.getRedditComment(id);
			result.put("likednum", redditComment.getLikedNum());
			result.put("result", "success");
		} else {
			result.put("result", "failed");
		}
		String content = JSON.toJSONString(result);
		view.addObject("content", content);
		return view;
	}

	
	
	@RequestMapping("weibo")
	public String publishWeibo(int redditId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null || user.getWeiboId() == null || user.getWeiboToken() == null) {
			return "redirect:/link/view/" + redditId + ".html";
		}
		Reddit reddit = redditService.getReddit(redditId);
		if (reddit == null) {
			return "redirect:/";
		}
		boolean b = WeiboHelper.publishRedditWeibo(reddit, user, "");
		if (b) {
			return "redirect:/link/view/" + redditId + ".html?weibo=true";
		}
		return "redirect:/link/view/" + redditId + ".html";
	}

	@RequestMapping("visitlink/{id}")
	public String addRedditVisitNum(@PathVariable("id") int id ) {
		redditService.addRedditVisitNum(id);
		Reddit reddit = redditService.getReddit(id);
		return "redirect:"+ reddit.getLinkUrl();
	}

	@RequestMapping("likelink/{id}")
    public ModelAndView likeReddit(@PathVariable("id") int id, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		ModelAndView view = new ModelAndView("json");
		Map<String, Object> result = new HashMap<String, Object>();
		if(user != null) {
			// check if user already liked it
			UserLikedReddit likedReddit = new UserLikedReddit();
			likedReddit.setRedditId(id);
			likedReddit.setUserId(user.getId());
			boolean liked = favoriteService.checkLikedReddit(likedReddit);
			if(liked == false) {
				// check if user already disliked it
				UserDislikedReddit dislikedReddit = new UserDislikedReddit();
				dislikedReddit.setRedditId(id);
				dislikedReddit.setUserId(user.getId());
				boolean disliked = favoriteService.checkDislikedReddit(dislikedReddit);
				if(disliked) {
					// remove from disliked, and increase likednum by 1
					// and delete from user disliked 
					redditService.votePlusOneReddit(id);
					favoriteService.delDislikedReddit(dislikedReddit);
				}
				redditService.likeReddit(id);
				favoriteService.addLikedReddit(likedReddit);
			} else {
				redditService.voteMinusOneReddit(id);
				favoriteService.delLikedReddit(likedReddit);
			}
			Reddit reddit = redditService.getReddit(id);
			result.put("likednum", reddit.getLikedNum());
			result.put("result", "success");
		} else {
			result.put("result", "failed");
		}
		String content = JSON.toJSONString(result);
		view.addObject("content", content);
		return view;
	}

	@RequestMapping("dislikelink/{id}")
	public ModelAndView dislikeReddit(@PathVariable("id") int id, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		ModelAndView view = new ModelAndView("json");
		Map<String, Object> result = new HashMap<String, Object>();
		if(user != null) {
			// check if user already disliked it
			UserDislikedReddit dislikedReddit = new UserDislikedReddit();
			dislikedReddit.setRedditId(id);
			dislikedReddit.setUserId(user.getId());
			boolean disliked = favoriteService.checkDislikedReddit(dislikedReddit);
			if(disliked == false) {
				// check if user already disliked it
				UserLikedReddit likedReddit = new UserLikedReddit();
				likedReddit.setRedditId(id);
				likedReddit.setUserId(user.getId());
				boolean liked = favoriteService.checkLikedReddit(likedReddit);
				if(liked) {
					// remove from liked, and decrease likednum by 1
					// and delete from user liked 
					redditService.voteMinusOneReddit(id);
					favoriteService.delLikedReddit(likedReddit);					
				}
				redditService.dislikeReddit(id);
				favoriteService.addDislikedReddit(dislikedReddit);
			} else {
				redditService.votePlusOneReddit(id);
				favoriteService.delDislikedReddit(dislikedReddit);
			}
			Reddit reddit = redditService.getReddit(id);
			result.put("likednum", reddit.getLikedNum());
			result.put("result", "success");
		} else {
			result.put("result", "failed");
		}
		String content = JSON.toJSONString(result);
		view.addObject("content", content);
		return view;
	}
	
	@RequestMapping("favlink/{id}")
    public ModelAndView favReddit(@PathVariable("id") int id, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		ModelAndView view = new ModelAndView("json");
		Map<String, Object> result = new HashMap<String, Object>();
		if(user != null) {
			// check if user already fav it
			UserFavoriteReddit favoriteReddit = new UserFavoriteReddit();
			favoriteReddit.setRedditId(id);
			favoriteReddit.setUserId(user.getId());
			boolean b = favoriteService.checkFavReddit(favoriteReddit);
			if(b == false) {
				redditService.favReddit(id);
				favoriteService.addFavReddit(favoriteReddit);
			} else {
				redditService.disfavReddit(id);
				favoriteService.delFavReddit(favoriteReddit);
			}
			Reddit reddit = redditService.getReddit(id);
			result.put("favnum", reddit.getFavNum());
			result.put("result", "success");
		} else {
			result.put("result", "failed");
		}
		String content = JSON.toJSONString(result);
		view.addObject("content", content);
		return view;
	}


}
