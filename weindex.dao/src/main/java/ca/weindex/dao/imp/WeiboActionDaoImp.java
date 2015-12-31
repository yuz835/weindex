package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchRequest;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.WeiboAction;
import ca.weindex.weibo4j.model.User;
import ca.weindex.common.model.WeiboUserFollowed;
import ca.weindex.dao.WeiboActionDao;

public class WeiboActionDaoImp extends SqlMapClientDaoSupport implements WeiboActionDao {

	public boolean insertWeiboAction(WeiboAction action) {
		int i = getSqlMapClientTemplate().update("insertWeiboAction", action);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public List<WeiboAction> getNewActionList(WeiboAction action) {
		List<?> list = new ArrayList();
		if(action.getWeiboUserId() == "ALL") { 
			list = getSqlMapClientTemplate().queryForList("getAllNewActionList", action);
		} else {
			list = getSqlMapClientTemplate().queryForList("getNewActionList", action);
		}
		if (list != null && !list.isEmpty()) {
			List<WeiboAction> result = new ArrayList<WeiboAction>();
			for (Object o : list) {
				result.add((WeiboAction) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public boolean checkWeiboAction(WeiboAction action) {
		int i = 0;
		if(action.getActionType() == 0) {
			i = (Integer) getSqlMapClientTemplate().queryForObject("checkActionType0", action);
		} else
		if(action.getActionType() == 1) {
			i = (Integer) getSqlMapClientTemplate().queryForObject("checkActionType1", action);
		} else
		if(action.getActionType() == 2) {
			i = (Integer) getSqlMapClientTemplate().queryForObject("checkActionType2", action);
		} else
		if(action.getActionType() == 3) {
			i = (Integer) getSqlMapClientTemplate().queryForObject("checkActionType3", action);
		};

		if (i == 0) {
			return false;
		} else {
			return true;
		}

	};


	public boolean setActionComplete(int id) {
		int i = getSqlMapClientTemplate().update("setActionComplete", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}


}
