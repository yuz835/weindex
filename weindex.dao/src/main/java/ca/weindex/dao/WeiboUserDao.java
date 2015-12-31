package ca.weindex.dao;

import java.util.List;


import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.WeiboUser;

public interface WeiboUserDao {
	public boolean insertWeiboUser(WeiboUser weiboUser);
	public boolean checkWeiboUserByWeiboUserId(String weiboUserId);
	public WeiboUser getWeiboUserByWeiboUserId(String weiboUserId);
	public WeiboUser getWeiboUserById(int id);
	public SearchResult<WeiboUser> getWeiboUser();
	public SearchResult<WeiboUser> getWeiboUserByCityAndBiFollowerCount(WeiboUser weiboUser, Pagination page);
	public boolean updateWeiboUser(WeiboUser weiboUser);


}
