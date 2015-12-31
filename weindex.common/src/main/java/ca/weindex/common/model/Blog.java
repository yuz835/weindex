package ca.weindex.common.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Blog extends BaseFav {
	private final static SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm (zzz)");
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private int id;
	private int shopId;
	private String title;
	private String summary;
	private String content;
	private String label;
	private String shopLabel;
	private int visible = 0;
	private Date createTime;
	private Date updateTime;

	private String author;

	private List<Label> labelList;
	private List<ShopLabel> shopLabelList;

	private int visitNum;
	private int commentNum;
	private int pos;

	private BlogImage image;

	// private List<BlogComment> commentList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		if (content == null || content.length() == 0) {
			return content;
		}
		summary = content;
		String tag = "[img:";
		int index = summary.indexOf(tag);
		while (index != -1) {
			int postIndex = summary.indexOf("]", index);
			if (postIndex != -1) {
				String pre = summary.substring(0, index);
				String post = summary.substring(postIndex + 1);
				summary = pre + " " + post;
			}
			index = summary.indexOf(tag);
		}

		if (summary.length() > 120) {
			summary = summary.substring(0, 120);
		}

		while (summary.contains("<")) {
			index = summary.indexOf("<");
			String pre = summary.substring(0, index);
			String post = summary.substring(index + 1);
			summary = pre + "&lt;" + post;
		}
		while (summary.contains(">")) {
			index = summary.indexOf(">");
			String pre = summary.substring(0, index);
			String post = summary.substring(index + 1);
			summary = pre + "&gt;" + post;
		}

		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
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

	// public List<BlogComment> getCommentList() {
	// return commentList;
	// }
	//
	// public void setCommentList(List<BlogComment> commentList) {
	// this.commentList = commentList;
	// }

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

	public List<Label> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<Label> labelList) {
		this.labelList = labelList;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getShopLabel() {
		return shopLabel;
	}

	public void setShopLabel(String shopLabel) {
		this.shopLabel = shopLabel;
	}

	public List<ShopLabel> getShopLabelList() {
		return shopLabelList;
	}

	public void setShopLabelList(List<ShopLabel> shopLabelList) {
		this.shopLabelList = shopLabelList;
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

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public BlogImage getImage() {
		return image;
	}

	public void setImage(BlogImage image) {
		this.image = image;
	}
}
