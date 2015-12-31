package ca.weindex.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.BlogComment;
import ca.weindex.common.model.BlogCommentMessage;
import ca.weindex.common.model.BlogImage;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;
import ca.weindex.dao.BlogCommentDao;
import ca.weindex.dao.BlogDao;
import ca.weindex.dao.BlogImageDao;
import ca.weindex.dao.MessageDao;
import ca.weindex.search.BlogIndexBuilder;
import ca.weindex.search.LuceneSearch;
import ca.weindex.services.BlogService;
import ca.weindex.services.ShopService;
import ca.weindex.services.UserService;

@Service
public class BlogServiceImp implements BlogService {
	@Autowired
	private BlogDao blogDao;

	@Autowired
	private BlogImageDao blogImageDao;

	@Autowired
	private BlogCommentDao blogCommentDao;

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private BlogIndexBuilder indexBuilder;

	@Autowired
	private ShopService shopService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LuceneSearch luceneSearch;

	public Blog getBlog(int id) {
		Blog blog = blogDao.getBlog(id);
		// if (blog != null) {
		// // get all comments
		// List<BlogComment> commentList =
		// blogCommentDao.getBlogCommentList(id);
		// blog.setCommentList(commentList);
		// }
		return blog;
	}

	public SearchResult<Blog> getBlogByShopId(int shopId, Pagination page) {
		return blogDao.getBlogByShopId(shopId, page);
	}

	public boolean insertBlog(Blog blog) {
		if (blog == null) {
			return false;
		}
		boolean b = blogDao.insertBlog(blog);
		if (b && blog.getId() > 0) {
			try {
				// update shop info
				shopService.refreshShop(blog.getShopId());
				shopService.refreshShopBlogLabels(blog.getShopId());
				indexBuilder.addBlogIndex(blog);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public boolean updateBlog(Blog blog) {
		if (blog == null) {
			return false;
		}
		boolean b = blogDao.updateBlog(blog);
		if (b && blog.getId() > 0) {
			try {
				shopService.refreshShopBlogLabels(blog.getShopId());
				blog.setAuthor(getAuthor(blog.getShopId()));
				indexBuilder.updateBlogIndex(blog);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	private String getAuthor(int shopId) {
		Shop shop = shopService.getShop(shopId);
		User user = userService.getUserById(shop.getUserId());
		return user.getUserName();
	}
	
	public BlogImage getBlogImage(String name, String type) {
		return blogImageDao.getBlogImageByNameType(name, type);
	}

	public boolean updateBlogImage(BlogImage image) {
		return blogImageDao.updateBlogImage(image);
	}


	public boolean insertBlogImage(BlogImage image) {
		return blogImageDao.insertBlogImage(image);
	}

	public boolean insertBlogComment(BlogComment comment, User creator, User blogAuthor) {
		boolean b = blogCommentDao.createComment(comment);
		if (b) {
			// send a message
			BlogCommentMessage msg = new BlogCommentMessage();
			msg.setSourceId(creator.getId());
			msg.setSource(creator.getUserName());
			msg.setDestId(blogAuthor.getId());
			msg.setDest(blogAuthor.getUserName());
			msg.setBlogId(comment.getBlogId());
			msg.setCommentTitle(comment.getTitle());
			try {
				updateBlogCommentNum(comment.getBlogId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Blog blog = this.getBlog(comment.getBlogId());
				msg.setBlogTitle(blog.getTitle());
				messageDao.insertMessage(msg);
			} catch (Exception e) {
				// do nothing
				e.printStackTrace();
			}
		}
		return b;
	}

	public SearchResult<BlogComment> getBlogComments(int blogId, Pagination page) {
		return blogCommentDao.getBlogCommentList(blogId, page);
	}

	public boolean deleteBlog(int id, int shopId) {
		boolean b = blogDao.deleteBlog(id);
		if (b && id > 0) {
			try {
				// update shop info
				shopService.refreshShop(shopId);
				shopService.refreshShopBlogLabels(shopId);
				indexBuilder.deleteBlogIndex(id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public SearchResult<Blog> searchBlog(String keyword, Pagination page) {
		if (keyword == null || keyword.trim().length() == 0) {
			//return offerDao.searchOffer("");
			keyword = "";
		}

		// return offerDao.searchOffer(keyword.trim());
		return luceneSearch.searchBlog(keyword, page);
	}

	public SearchResult<Blog> getBlogByShopLabelId(int shopId, int labelId, Pagination page) {
		return blogDao.getBlogByShopLabelId(shopId, labelId, page);
	}

	public SearchResult<Blog> getBlogByShopShopLabelId(int shopId, int shopLabelId, Pagination page) {
		return blogDao.getBlogByShopShopLabelId(shopId, shopLabelId, page);
	}

	public SearchResult<Blog> getBlogByLabelId(int labelId, Pagination page) {
		return blogDao.getBlogByLabelId(labelId, page);
	}

	public boolean addBlogVisitNum(int id) {
		return blogDao.addBlogVisitNum(id);
	}

	public boolean updateBlogCommentNum(int id) {
		return blogDao.updateBlogCommentNum(id);
	}

	public boolean deleteBlogComment(int id) {
		return blogCommentDao.deleteComment(id);
	}

	public BlogComment getBlogComment(int id) {
		return blogCommentDao.getBlogComment(id);
	}

	public boolean topBlogOfShop(Blog blog) {
		blogDao.updateBlogPosByShopId(blog.getShopId());
		return blogDao.updateBlogPos(blog);
	}

	public void rebuildBlogIndex(int blogId) {
		Blog blog = getBlog(blogId);
		if (blog == null) {
			return;
		}
		try {
			indexBuilder.deleteBlogIndex(blogId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			indexBuilder.addBlogIndex(blog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
