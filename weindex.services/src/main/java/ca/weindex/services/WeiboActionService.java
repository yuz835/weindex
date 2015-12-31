package ca.weindex.services;

import java.util.List;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.WeiboAction;
import ca.weindex.common.model.WeiboUser;
import ca.weindex.common.model.WeiboUserFollowed;

public interface WeiboActionService {

	public boolean insertWeiboAction(WeiboAction action); 
	public boolean checkWeiboAction(WeiboAction action);
	public List<WeiboAction> getNewActionList(WeiboAction action); 
	public boolean setActionComplete(int id); 

	public boolean insertWeiboUser(WeiboUser weiboUser);
	public boolean checkWeiboUserByWeiboUserId(String weiboUserId);
	public SearchResult<WeiboUser> getWeiboUser(Pagination page);
	public WeiboUser getWeiboUserByWeiboUserId(String weiboUserId);
	public WeiboUser getWeiboUserById(int id);
	public SearchResult<WeiboUser> getWeiboUserByCityAndBiFollowerCount(WeiboUser weiboUser, Pagination page);
	public boolean updateWeiboUser(WeiboUser weiboUser);


	public boolean insertWeiboUserFollowed(WeiboUserFollowed item); 
	public boolean updateWeiboUserFollowed(WeiboUserFollowed item);
	public boolean deleteWeiboUserFollowedByWeiboUserId(String weiboUserId);
	public boolean  checkWeiboUserFollowedByWeiboUserId(String weiboUserId);
	public WeiboUserFollowed  getWeiboUserFollowedByWeiboUserId(String weiboUserId);



}
