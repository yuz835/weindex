package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.OfferComment;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.dao.OfferCommentDao;

public class OfferCommentDaoImp extends SqlMapClientDaoSupport implements OfferCommentDao {

	public boolean createComment(OfferComment comment) {
		int i = getSqlMapClientTemplate().update("insertOfferComment", comment);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteComment(int id) {
		int i = getSqlMapClientTemplate().delete("deleteOfferComment", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public SearchResult<OfferComment> getOfferCommentList(int offerId, Pagination page) {
		SearchResult<OfferComment> sr = new SearchResult<OfferComment>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countOfferCommentByOfferId", offerId);
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(offerId);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getOfferCommentByOfferId", request);
			if (list != null && !list.isEmpty()) {
				List<OfferComment> result = new ArrayList<OfferComment>();
				for (Object o : list) {
					result.add((OfferComment) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}

		}
		return sr;
	}

	public OfferComment getOfferComment(int id) {
		OfferComment comment = (OfferComment) getSqlMapClientTemplate().queryForObject("getOfferComment", id);
		return comment;
	}

}
