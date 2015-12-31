package ca.weindex.common.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Reddit extends BaseFav {
	private final static SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm (zzz)");
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private int id;
	private int userId;
	private String author;
	private String title;
	private int type = 0;
	private String linkUrl;
	private String description;
	private String content;
	private String label;
	private int likedNum;
	private int favNum;
	private int visitNum;
	private int commentNum;
	private Date createTime;
	private Date updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getLikedNum() {
		return likedNum;
	}

	public void setLikedNum(int likedNum) {
		this.likedNum = likedNum;
	}

	public int getFavNum() {
		return favNum;
	}

	public void setFavNum(int favNum) {
		this.favNum = favNum;
	}

	public int getVisitNum() {
		return visitNum;
	}

	public void setVisitNum(int visitNum) {
		this.visitNum = visitNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
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

	public String getDate() {
		Date time = createTime;
		if (updateTime != null) {
			time = updateTime;
		}
		if (time == null) {
			time = new Date();
		}
		return dateFormat.format(time);
	}

}
