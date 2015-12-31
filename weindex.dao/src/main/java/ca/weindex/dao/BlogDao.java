package ca.weindex.dao;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;

public interface BlogDao {
	public Blog getBlog(int id);
	public SearchResult<Blog> getBlogByShopId(int shopId, Pagination page);
	public boolean insertBlog(Blog blog);
	public boolean updateBlog(Blog blog);
	public boolean deleteBlog(int id);
	public SearchResult<Blog> getBlogByLabelId(int labelId, Pagination page);
	public SearchResult<Blog> getBlogByShopLabelId(int shopId, int labelId, Pagination page);
	public SearchResult<Blog> getBlogByShopShopLabelId(int shopId, int shopLabelId, Pagination page);

	public boolean addBlogVisitNum(int id);
	public boolean updateBlogCommentNum(int id);
	
	public void updateBlogPosByShopId(int shopId);
	public boolean updateBlogPos(Blog blog);
}
