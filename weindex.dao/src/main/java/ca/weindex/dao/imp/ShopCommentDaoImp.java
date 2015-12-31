package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.ShopComment;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.dao.ShopCommentDao;

public class ShopCommentDaoImp extends SqlMapClientDaoSupport implements ShopCommentDao {

	public boolean createComment(ShopComment comment) {
		int i = getSqlMapClientTemplate().update("insertShopComment", comment);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteComment(int id) {
		int i = getSqlMapClientTemplate().delete("deleteShopComment", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public SearchResult<ShopComment> getShopCommentList(int shopId, Pagination page) {
		SearchResult<ShopComment> sr = new SearchResult<ShopComment>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countShopCommentByShopId", shopId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(shopId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getShopCommentByShopId", request);
			if (list != null && !list.isEmpty()) {
				List<ShopComment> result = new ArrayList<ShopComment>();
				for (Object o : list) {
					result.add((ShopComment) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}

		}
		return sr;
	}

	public ShopComment getShopComment(int id) {
		ShopComment comment = (ShopComment) getSqlMapClientTemplate().queryForObject("getShopComment", id);
		return comment;
	}

}
