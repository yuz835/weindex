package ca.weindex.dao;

import ca.weindex.common.model.OfferComment;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;

public interface OfferCommentDao {
	public boolean createComment(OfferComment comment);

	public SearchResult<OfferComment> getOfferCommentList(int offerId, Pagination page);

	public boolean deleteComment(int id);
	
	public OfferComment getOfferComment(int id);
}
