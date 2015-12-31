package ca.weindex.dao.imp;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.BlogImage;
import ca.weindex.dao.BlogImageDao;

public class BlogImageDaoImp extends SqlMapClientDaoSupport implements
		BlogImageDao {

	public BlogImage getBlogImageById(int id) {
		BlogImage result = (BlogImage) getSqlMapClientTemplate()
				.queryForObject("getBlogImage", id);
		return result;
	}

	public BlogImage getBlogImageByNameType(String name, String type) {
		BlogImage image = new BlogImage();
		image.setName(name);
		image.setType(type);
		BlogImage result = (BlogImage) getSqlMapClientTemplate()
				.queryForObject("getBlogImageByNameType", image);
		return result;
	}

	public boolean insertBlogImage(BlogImage image) {
		int i = getSqlMapClientTemplate().update("insertBlogImage", image);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateBlogImage(BlogImage image) {
		int i = getSqlMapClientTemplate().update("updateBlogImage", image);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}


	public boolean deleteBlogImage(int id) {
		int i = getSqlMapClientTemplate().delete("deleteBlogImage", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

}
