package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.WeiboUser;
import ca.weindex.dao.WeiboUserDao;

public class WeiboUserDaoImp extends SqlMapClientDaoSupport implements WeiboUserDao {

	public boolean insertWeiboUser(WeiboUser weiboUser) {
		int i = (Integer) getSqlMapClientTemplate().queryForObject("checkWeiboUserByWeiboUserId", weiboUser.getWeiboUserId());
		if (i == 0) {
			int j = getSqlMapClientTemplate().update("insertWeiboUser", weiboUser);
			if (j == 1) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	
	public boolean checkWeiboUserByWeiboUserId(String weiboUserId) {
		int i = (Integer) getSqlMapClientTemplate().queryForObject("checkWeiboUserByWeiboUserId", weiboUserId);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	};
		
	public WeiboUser getWeiboUserByWeiboUserId(String weiboUserId){
		List<?> list =  getSqlMapClientTemplate().queryForList("getWeiboUserByWeiboUserId", weiboUserId);
		return (WeiboUser) list.get(0);
	}

	public WeiboUser getWeiboUserById(int id){
		WeiboUser result = (WeiboUser)  getSqlMapClientTemplate().queryForObject("getWeiboUserById", id);
		if (result != null) {
			return result;
		}
		return null;

	}


	public SearchResult<WeiboUser> getWeiboUser() {
		SearchResult<WeiboUser> sr = new SearchResult<WeiboUser>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countWeiboUser");
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			List<?> list = getSqlMapClientTemplate().queryForList("getWeiboUser");
			if (list != null && !list.isEmpty()) {
				List<WeiboUser> result = new ArrayList<WeiboUser>();
				for (Object o : list) {
					result.add((WeiboUser) o);
				}
				sr.setList(result);
				return sr;
			}
		}
		return sr;
	
	};


	public SearchResult<WeiboUser> getWeiboUserByCityAndBiFollowerCount(WeiboUser weiboUser, Pagination page){
		SearchResult<WeiboUser> sr = new SearchResult<WeiboUser>();
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject("countWeiboUser");
		if (count != null && count > 0) {
			sr.setTotalNum(count);
			SearchRequest request = new SearchRequest();
			request.setValue(weiboUser);
			request.setPage(page);
			List<?> list = getSqlMapClientTemplate().queryForList("getWeiboUserByCityAndBiFollowerCount", request);
			if (list != null && !list.isEmpty()) {
				List<WeiboUser> result = new ArrayList<WeiboUser>();
				for (Object o : list) {
					result.add((WeiboUser) o);
				}
				sr.setList(result);
				sr.setPageNum(page.getPageNum());
				sr.setPageSize(page.getPageSize());
				return sr;
			}
		}
		return sr;


	};

	public boolean updateWeiboUser(WeiboUser weiboUser) {
		int i = getSqlMapClientTemplate().update("updateWeiboUser", weiboUser);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
		
	};


}
