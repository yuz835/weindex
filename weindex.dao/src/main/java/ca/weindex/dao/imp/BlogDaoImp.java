package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.ShopLabelSearchRequest;
import ca.weindex.dao.BlogDao;

public class BlogDaoImp extends SqlMapClientDaoSupport implements BlogDao {

	public Blog getBlog(int id) {
		Blog blog = (Blog) getSqlMapClientTemplate().queryForObject("getBlog", id);
		return blog;
	}

	public SearchResult<Blog> getBlogByShopId(int shopId, Pagination page) {
		SearchResult<Blog> sr = new SearchResult<Blog>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countBlogByShopId", shopId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(shopId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getBlogByShopId", request);
			if (list != null && !list.isEmpty()) {
				List<Blog> result = new ArrayList<Blog>();
				for (Object o : list) {
					result.add((Blog) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public boolean insertBlog(Blog blog) {
		int id = (Integer) getSqlMapClientTemplate().insert("insertBlog", blog);
		if (id > 0) {
			blog.setId(id);
			return true;
		} else {
			return false;
		}
	}

	public boolean updateBlog(Blog blog) {
		int i = getSqlMapClientTemplate().update("updateBlog", blog);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteBlog(int id) {
		int i = getSqlMapClientTemplate().delete("deleteBlog", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public SearchResult<Blog> getBlogByLabelId(int labelId, Pagination page) {
		SearchResult<Blog> sr = new SearchResult<Blog>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countBlogByLabelId", labelId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(labelId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getBlogByLabelId", request);
			if (list != null && !list.isEmpty()) {
				List<Blog> result = new ArrayList<Blog>();
				for (Object o : list) {
					result.add((Blog) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Blog> getBlogByShopLabelId(int shopId, int labelId, Pagination page) {
		SearchResult<Blog> sr = new SearchResult<Blog>();
		ShopLabelSearchRequest request = new ShopLabelSearchRequest();
		request.setShopId(shopId);
		request.setLabelId(labelId);
		request.setPage(page);
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countBlogByShopLabelId", request);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			List<?> list = getSqlMapClientTemplate().queryForList("getBlogByShopLabelId", request);
			if (list != null && !list.isEmpty()) {
				List<Blog> result = new ArrayList<Blog>();
				for (Object o : list) {
					result.add((Blog) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public SearchResult<Blog> getBlogByShopShopLabelId(int shopId, int shopLabelId, Pagination page) {
		SearchResult<Blog> sr = new SearchResult<Blog>();
		ShopLabelSearchRequest request = new ShopLabelSearchRequest();
		request.setShopId(shopId);
		request.setLabelId(shopLabelId);
		request.setPage(page);
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countBlogByShopShopLabelId", request);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			List<?> list = getSqlMapClientTemplate().queryForList("getBlogByShopShopLabelId", request);
			if (list != null && !list.isEmpty()) {
				List<Blog> result = new ArrayList<Blog>();
				for (Object o : list) {
					result.add((Blog) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;
	}

	public boolean addBlogVisitNum(int id) {
		int i = getSqlMapClientTemplate().update("addBlogVisitNum", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateBlogCommentNum(int id) {
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countBlogCommentByBlogId", id);
		if (count != null) {
			Shop shop = new Shop();
			shop.setId(id);
			shop.setCommentNum(count);
			int i = getSqlMapClientTemplate().update("updateBlogCommentNum", shop);
			if (i == 1) {
				return true;
			}
		}
		return false;
	}

	public void updateBlogPosByShopId(int shopId) {
		getSqlMapClientTemplate().update("updateBlogPosByShopId", shopId);		
	}

	public boolean updateBlogPos(Blog blog) {
		int i = getSqlMapClientTemplate().update("updateBlogPos", blog);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}
}
