package ca.weindex.common.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseComment {
	private final static SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm (zzz)");

	private int id;
	private String title;
	private String content;
	private int visible;
	private int creatorId;
	private int likedNum;
	private String creatorName;
	private int traceCommentId;
	private Date createTime;
	private Date updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	public int getLikedNum() {
		return likedNum;
	}

	public void setLikedNum(int likedNum) {
		this.likedNum = likedNum;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public int getTraceCommentId() {
		return traceCommentId;
	}

	public void setTraceCommentId(int traceCommentId) {
		this.traceCommentId = traceCommentId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getTime() {
		Date time = createTime;
		if (updateTime != null) {
			time = updateTime;
		}
		if (time == null) {
			time = new Date();
		}
		return timeFormat.format(time);
	}

	// recover html content
	public String getHtmlContent() {
	 	String src = content;
		src = replace(src, "&lt;", "<");
		src = replace(src, "&gt;", ">");
		return src;
		
	};

	private String replace(String src, String from, String to) {
		if (src == null || from == null || from.length() == 0 || to == null) {
			return src;
		}
		while (src.indexOf(from) != -1) {
			int index = src.indexOf(from);
			String pre = src.substring(0, index);
			String post = src.substring(index + from.length());
			src = pre + to + post;
		}
		return src;
	}

}
