package ca.weindex.dao;

import java.util.List;

import ca.weindex.common.model.WeiboTask;

public interface WeiboTaskDao {
	public boolean insertWeiboTask(WeiboTask task);
	public boolean deleteWeiboTask(int id);
	public boolean updateWeiboTaskStatus(WeiboTask task);
	public WeiboTask getWeiboTask(int id);
	public List<WeiboTask> getUnstartedWeiboTask(int number);
	public List<WeiboTask> getWeiboTaskByUserId(int userId);
	public List<WeiboTask> getWeiboTaskByOfferId(int offerId);
	public List<WeiboTask> getWeiboTaskByBlogId(int blogId);
}
