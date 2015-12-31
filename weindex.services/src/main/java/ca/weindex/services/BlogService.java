package ca.weindex.services;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.BlogComment;
import ca.weindex.common.model.BlogImage;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.User;

public interface BlogService {
	public Blog getBlog(int id);
	public SearchResult<Blog> getBlogByShopId(int shopId, Pagination page);
	public SearchResult<Blog> getBlogByShopLabelId(int shopId, int labelId, Pagination page);
	public SearchResult<Blog> getBlogByShopShopLabelId(int shopId, int shopLabelId, Pagination page);
	public SearchResult<Blog> getBlogByLabelId(int labelId, Pagination page);
		
	public boolean insertBlog(Blog blog);
	public boolean updateBlog(Blog blog);
	public boolean deleteBlog(int id, int shopId);
	
	public BlogImage getBlogImage(String name, String type);
	public boolean updateBlogImage(BlogImage image);
	public boolean insertBlogImage(BlogImage image);
	
	public SearchResult<BlogComment> getBlogComments(int blogId, Pagination page);
	public boolean insertBlogComment(BlogComment comment, User creator, User blogAuthor);
	public boolean deleteBlogComment(int id);
	public BlogComment getBlogComment(int id);
	
	public SearchResult<Blog> searchBlog(String keyword, Pagination page);

	public boolean addBlogVisitNum(int id);
	public boolean updateBlogCommentNum(int id);
	
	public boolean topBlogOfShop(Blog blog);
	
	public void rebuildBlogIndex(int blogId);
}
