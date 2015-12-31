package ca.weindex.services;

import java.util.List;

import ca.weindex.common.model.WeiboTask;

public interface WeiboTaskService {
	public boolean insertWeiboTask(WeiboTask task);
	public boolean deleteWeiboTask(int id);
	public boolean updateWeiboTaskStatus(WeiboTask task);
	public WeiboTask getWeiboTask(int id);
	public WeiboTask getOneUnstartedWeiboTask();
	public List<WeiboTask> getWeiboTaskByUserId(int userId);
	public List<WeiboTask> getWeiboTaskByOfferId(int offerId);
	public List<WeiboTask> getWeiboTaskByBlogId(int blogId);
}
