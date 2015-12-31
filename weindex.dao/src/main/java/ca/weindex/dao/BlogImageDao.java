package ca.weindex.dao;

import ca.weindex.common.model.BlogImage;

public interface BlogImageDao {

	public BlogImage getBlogImageById(int id);

	public BlogImage getBlogImageByNameType(String name, String type);

	public boolean insertBlogImage(BlogImage image);
	public boolean updateBlogImage(BlogImage image);

	public boolean deleteBlogImage(int id);


}
