package ca.weindex.dao;

import java.util.List;


import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.WeiboUserFollowed;


public interface WeiboUserFollowedDao {
	public boolean insertWeiboUserFollowed(WeiboUserFollowed item); 
	public boolean updateWeiboUserFollowed(WeiboUserFollowed item);
	public boolean deleteWeiboUserFollowedByWeiboUserId(String weiboUserId);
	public boolean  checkWeiboUserFollowedByWeiboUserId(String weiboUserId);
	public WeiboUserFollowed  getWeiboUserFollowedByWeiboUserId(String weiboUserId);



}
