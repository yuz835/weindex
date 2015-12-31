package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.WeiboUserFollowed;
import ca.weindex.dao.WeiboUserFollowedDao;

public class WeiboUserFollowedDaoImp extends SqlMapClientDaoSupport implements WeiboUserFollowedDao {


	public boolean insertWeiboUserFollowed(WeiboUserFollowed item) {
		int i = getSqlMapClientTemplate().update("insertWeiboUserFollowed", item);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateWeiboUserFollowed(WeiboUserFollowed item) {
		int i = getSqlMapClientTemplate().update("updateWeiboUserFollowed", item);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
		
	}

	public boolean checkWeiboUserFollowedByWeiboUserId(String weiboUserId) {
		int i = (Integer) getSqlMapClientTemplate().queryForObject("checkWeiboUserFollowedByWeiboUserId", weiboUserId);
		if (i == 0) {
			return false;
		} else {
			return true;
		}
	}
	

	public boolean deleteWeiboUserFollowedByWeiboUserId(String weiboUserId) {
		int i = getSqlMapClientTemplate().delete("deleteWeiboUserFollowedByWeiboUserId", weiboUserId);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public WeiboUserFollowed  getWeiboUserFollowedByWeiboUserId(String weiboUserId) {
		WeiboUserFollowed result =  (WeiboUserFollowed) getSqlMapClientTemplate().queryForObject("getWeiboUserFollowedByWeiboUserId", weiboUserId);

		if (result != null) {
			return result;
		}
		return null;

	};


}
