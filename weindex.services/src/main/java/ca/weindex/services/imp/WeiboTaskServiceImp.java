package ca.weindex.services.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.weindex.common.enums.WeiboTaskStatusEnum;
import ca.weindex.common.model.WeiboTask;
import ca.weindex.dao.WeiboTaskDao;
import ca.weindex.services.WeiboTaskService;

@Service
public class WeiboTaskServiceImp implements WeiboTaskService {
	@Autowired
	private WeiboTaskDao dao;
	
	public boolean insertWeiboTask(WeiboTask task) {
		return dao.insertWeiboTask(task);
	}

	public boolean deleteWeiboTask(int id) {
		return dao.deleteWeiboTask(id);
	}

	public boolean updateWeiboTaskStatus(WeiboTask task) {
		return dao.updateWeiboTaskStatus(task);
	}

	public WeiboTask getWeiboTask(int id) {
		return dao.getWeiboTask(id);
	}

	public WeiboTask getOneUnstartedWeiboTask() {
		List<WeiboTask> list = dao.getUnstartedWeiboTask(1);
		if (list == null || list.isEmpty() || list.size() != 1) {
			return null;
		}
		WeiboTask task = list.get(0);
		if (task.getStatusEnum() != WeiboTaskStatusEnum.UNSTARTED) {
			return null;
		}
		task.setStatus(WeiboTaskStatusEnum.WAITING.getStatus());
		boolean b = dao.updateWeiboTaskStatus(task);
		if (b) {
			return task;
		} else {
			return null;
		}
	}

	public List<WeiboTask> getWeiboTaskByUserId(int userId) {
		return dao.getWeiboTaskByUserId(userId);
	}

	public List<WeiboTask> getWeiboTaskByOfferId(int offerId) {
		return dao.getWeiboTaskByOfferId(offerId);
	}

	public List<WeiboTask> getWeiboTaskByBlogId(int blogId) {
		return dao.getWeiboTaskByBlogId(blogId);
	}

}
