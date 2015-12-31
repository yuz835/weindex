package ca.weindex.common.model;

import java.util.Date;

public class WeiboUserFollowed {
	private int id;
	private String weiboUserId;	// weibo user id
	private String screenName;	// weibo screen name
	private int firstFollowedIndex; // 第一个关注的微博在数据库表weibo_user中的行ID 
					// 从ID 小到大follow
	private int latestFollowedIndex;// 最近关注的微博在数据库表weibo_user中的行ID 
					// 从ID 小到大follow

	private Date created;		

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWeiboUserId() {
		return weiboUserId;
	}

	public void setWeiboUserId(String weiboUserId) {
		this.weiboUserId = weiboUserId;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public int getFirstFollowedIndex() {
		return firstFollowedIndex;
	}

	public  void setFirstFollowedIndex(int firstFollowedIndex) {
		this.firstFollowedIndex = firstFollowedIndex;
	}

	public int getLatestFollowedIndex() {
		return latestFollowedIndex;
	}

	public  void setLatestFollowedIndex(int latestFollowedIndex) {
		this.latestFollowedIndex = latestFollowedIndex;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCreated() {
		return created;
	}
	

}

