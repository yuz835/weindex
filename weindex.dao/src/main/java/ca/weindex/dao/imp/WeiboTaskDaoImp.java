package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.WeiboTask;
import ca.weindex.dao.WeiboTaskDao;

public class WeiboTaskDaoImp extends SqlMapClientDaoSupport implements WeiboTaskDao {

	public boolean insertWeiboTask(WeiboTask task) {
		int i = getSqlMapClientTemplate().update("insertWeiboTask", task);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteWeiboTask(int id) {
		int i = getSqlMapClientTemplate().delete("deleteWeiboTask", id);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateWeiboTaskStatus(WeiboTask task) {
		int i = getSqlMapClientTemplate().update("updateWeiboTaskStatus", task);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public WeiboTask getWeiboTask(int id) {
		WeiboTask task = (WeiboTask) getSqlMapClientTemplate().queryForObject("getWeiboTask", id);
		return task;
	}

	public List<WeiboTask> getUnstartedWeiboTask(int number) {
		List<?> list = getSqlMapClientTemplate().queryForList("getOneUnstartedWeiboTask", number);
		List<WeiboTask> result = new ArrayList<WeiboTask>();
		for (Object o : list) {
			result.add((WeiboTask) o);
		}
		return result;
	}

	public List<WeiboTask> getWeiboTaskByUserId(int userId) {
		List<?> list = getSqlMapClientTemplate().queryForList("getWeiboTaskByUserId", userId);
		List<WeiboTask> result = new ArrayList<WeiboTask>();
		for (Object o : list) {
			result.add((WeiboTask) o);
		}
		return result;
	}

	public List<WeiboTask> getWeiboTaskByOfferId(int offerId) {
		List<?> list = getSqlMapClientTemplate().queryForList("getWeiboTaskByOfferId", offerId);
		List<WeiboTask> result = new ArrayList<WeiboTask>();
		for (Object o : list) {
			result.add((WeiboTask) o);
		}
		return result;
	}

	public List<WeiboTask> getWeiboTaskByBlogId(int blogId) {
		List<?> list = getSqlMapClientTemplate().queryForList("getWeiboTaskByBlogId", blogId);
		List<WeiboTask> result = new ArrayList<WeiboTask>();
		for (Object o : list) {
			result.add((WeiboTask) o);
		}
		return result;
	}

}
