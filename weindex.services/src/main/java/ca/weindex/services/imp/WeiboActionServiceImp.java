package ca.weindex.services.imp;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.WeiboAction;
import ca.weindex.common.model.WeiboUser;
import ca.weindex.common.model.WeiboUserFollowed;
import ca.weindex.dao.WeiboActionDao;
import ca.weindex.dao.WeiboUserDao;
import ca.weindex.dao.WeiboUserFollowedDao;

import ca.weindex.services.WeiboActionService;

@Service
public class WeiboActionServiceImp implements WeiboActionService{
	@Autowired
	private WeiboActionDao weiboActionDao;

	@Autowired
	private WeiboUserDao weiboUserDao;

	@Autowired
	private WeiboUserFollowedDao weiboUserFollowedDao;


	public boolean insertWeiboAction(WeiboAction action){
		return weiboActionDao.insertWeiboAction(action);
	};

	public boolean checkWeiboAction(WeiboAction action) {
		return weiboActionDao.checkWeiboAction(action);
	};

 
	public List<WeiboAction> getNewActionList(WeiboAction action) {
		return weiboActionDao.getNewActionList(action);
	};
 
	public boolean setActionComplete(int id) {
		return weiboActionDao.setActionComplete(id);
	}; 

//
	public boolean insertWeiboUser(WeiboUser weiboUser) {
		return weiboUserDao.insertWeiboUser(weiboUser);
	};

	public boolean checkWeiboUserByWeiboUserId(String weiboUserId) {
		return weiboUserDao.checkWeiboUserByWeiboUserId(weiboUserId);
	};
	
	public WeiboUser getWeiboUserByWeiboUserId(String weiboUserId) {
		return weiboUserDao.getWeiboUserByWeiboUserId(weiboUserId);
	};

	public WeiboUser getWeiboUserById(int id) {
		return weiboUserDao.getWeiboUserById(id);
	};


	public SearchResult<WeiboUser> getWeiboUser(Pagination page) {
		return weiboUserDao.getWeiboUser();
	};

	public SearchResult<WeiboUser> getWeiboUserByCityAndBiFollowerCount(WeiboUser weiboUser, Pagination page) {
		return weiboUserDao.getWeiboUserByCityAndBiFollowerCount(weiboUser, page);
	};

	public boolean updateWeiboUser(WeiboUser weiboUser) {
		return weiboUserDao.updateWeiboUser(weiboUser);
	};

//
	public boolean insertWeiboUserFollowed(WeiboUserFollowed item) {
		return weiboUserFollowedDao.insertWeiboUserFollowed(item);
	}; 

	public boolean updateWeiboUserFollowed(WeiboUserFollowed item) {
		return weiboUserFollowedDao.updateWeiboUserFollowed(item);
	};

	public boolean deleteWeiboUserFollowedByWeiboUserId(String weiboUserId){
		return weiboUserFollowedDao.deleteWeiboUserFollowedByWeiboUserId(weiboUserId);
	};

	public boolean  checkWeiboUserFollowedByWeiboUserId(String weiboUserId){
		return weiboUserFollowedDao.checkWeiboUserFollowedByWeiboUserId(weiboUserId);
	};

	public WeiboUserFollowed  getWeiboUserFollowedByWeiboUserId(String weiboUserId) {
		return weiboUserFollowedDao.getWeiboUserFollowedByWeiboUserId(weiboUserId);
	};


}
