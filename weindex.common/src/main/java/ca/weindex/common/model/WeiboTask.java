package ca.weindex.common.model;

import java.util.Date;

import ca.weindex.common.enums.WeiboTaskStatusEnum;
import ca.weindex.common.enums.WeiboTaskTypeEnum;

public class WeiboTask {
	private int id;
	private int userId;
	private int type;
	private int taskId;
	private String content;
	private Date taskTime;
	private int status;
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(Date taskTime) {
		this.taskTime = taskTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public WeiboTaskTypeEnum getTypeEnum() {
		return WeiboTaskTypeEnum.getTaskType(type);
	}
	
	public WeiboTaskStatusEnum getStatusEnum() {
		return WeiboTaskStatusEnum.getTaskStatus(status);
	}
}
