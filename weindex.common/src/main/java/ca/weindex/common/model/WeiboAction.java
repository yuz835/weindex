package ca.weindex.common.model;

import java.util.Date;

public class WeiboAction {
	private int id;
	private String weiboUserId; // weibo user ID
	private String weiboId;
	private String weiboCommentId;
	private int actionType; // 0 @我的微博
				// 1 @到我的评论
				// 2 我收到的评论
				// 3 定时发布微博
					
	private int responseType; // 0 retweet and comment
				// 1 retweet
				// 2 comment
				// to be defined
	
	private int actionStatus; // 0 pending
				  // 1 completed
				
	private String responseContent; // 微博内容，或者微博评论内容
	private String imageName; // 图片id，来自blogImage，用来定时发布微博

	private Date receivedTime;
	private Date responseTime;

	
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


	public String getWeiboId() {
		return weiboId;
	}


	public  void setWeiboId(String weiboId) {
		this.weiboId = weiboId;
	}

	public  String getWeiboCommentId() {
		return weiboCommentId;
	}

	public  void setWeiboCommentId(String weiboCommentId) {
		this.weiboCommentId = weiboCommentId;
	}

	public  int getActionType() {
		return actionType;
	}


	public  void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public  int getResponseType() {
		return responseType;
	}

	public  void setResponseType(int responseType) {
		this.responseType = responseType;
	}

	public  int getActionStatus() {
		return actionStatus;
	}


	public  void setActionStatus(int actionStatus) {
		this.actionStatus = actionStatus;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	public  String getImageName() {
		return imageName;
	}

	public  void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Date getReceivedTime() {
		return receivedTime;
	}

	public  void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
	}


	public Date getResponseTime() {
		return responseTime;
	}

	public  void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}


}
