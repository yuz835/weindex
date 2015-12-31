package ca.weindex.common.model;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import ca.weindex.common.enums.MessageTypeEnum;

public class Message {
	private int id;
	private int type;
	private String title;
	private String content;
	private String source;
	private int sourceId;
	private String dest;
	private int destId;
	private Date createTime;
	private int readed = 0;

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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public int getDestId() {
		return destId;
	}

	public void setDestId(int destId) {
		this.destId = destId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getReaded() {
		return readed;
	}

	public void setReaded(int readed) {
		this.readed = readed;
	}

	public void setReaded(boolean readed) {
		if (readed) {
			this.readed = 1;
		} else {
			this.readed = 0;
		}
	}

	public boolean isReaded() {
		if (readed > 0) {
			return true;
		} else {
			return false;
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setType(MessageTypeEnum type) {
		this.type = type.getType();
	}

	public String getContentText() {
		try {
			if (type == MessageTypeEnum.BLOG_COMMENT.getType()) {
				JSONObject json = JSONObject.parseObject(content);
				String blogId = json.getString("blogId");
				String blogTitle = json.getString("blogTitle");
				String source = this.getSource();
				String txt = "<a href=\"profile/" + sourceId + ".html\">" + source + "</a> 对您的博文 <a href=\"/blog/view/"
						+ blogId + ".html\">" + blogTitle + "</a> 进行了评论.";
				return txt;
			} else if (type == MessageTypeEnum.SHOP_COMMENT.getType()) {
				JSONObject json = JSONObject.parseObject(content);
				String shopName = json.getString("shopName");
				String shopTitle = json.getString("shopTitle");
				String source = this.getSource();
				String txt = "<a href=\"profile/" + sourceId + ".html\">" + source + "</a> 对您的店铺 <a href=\"/"
						+ shopName + "\">" + shopTitle + "</a> 进行了评论.";
				return txt;
			} else if (type == MessageTypeEnum.OFFER_COMMENT.getType()) {
				JSONObject json = JSONObject.parseObject(content);
				String offerId = json.getString("offerId");
				String offerTitle = json.getString("offerTitle");
				String source = this.getSource();
				String txt = "<a href=\"profile/" + sourceId + ".html\">" + source + "</a> 对您的商品 <a href=\"/offer/view/"
						+ offerId + ".html\">" + offerTitle + "</a> 进行了评论.";
				return txt;
			} else if (type == MessageTypeEnum.SHOP_FAVORITE.getType()) {
				JSONObject json = JSONObject.parseObject(content);
				String userId = json.getString("userId");
				String userName = json.getString("userName");
				String txt = "<a href=\"profile/" + userId + ".html\">" + userName + "</a> 收藏了您的店铺.";
				return txt;
			} else if (type == MessageTypeEnum.OFFER_FAVORITE.getType()) {
				JSONObject json = JSONObject.parseObject(content);
				String userId = json.getString("userId");
				String userName = json.getString("userName");
				String offerId = json.getString("offerId");
				String offerName = json.getString("offerName");
				String txt = "<a href=\"profile/" + userId + ".html\">" + userName
						+ "</a> 收藏了您的商品 <a href=\"/offer/view/" + offerId + ".html\">" + offerName + "</a>.";
				return txt;
			} else if (type == MessageTypeEnum.BLOG_FAVORITE.getType()) {
				JSONObject json = JSONObject.parseObject(content);
				String userId = json.getString("userId");
				String userName = json.getString("userName");
				String blogId = json.getString("blogId");
				String blogTitle = json.getString("blogTitle");
				String txt = "<a href=\"profile/" + userId + ".html\">" + userName
						+ "</a> 收藏了您的博文 <a href=\"/blog/view/" + blogId + ".html\">" + blogTitle + "</a>.";
				return txt;
			} else if (type == MessageTypeEnum.OFFER_SHARE_FAILED.getType()) {
			} else if (type == MessageTypeEnum.SHOP_SHARE_FAILED.getType()) {
			} else if (type == MessageTypeEnum.BLOG_SHARE_FAILED.getType()) {
			} else if (type == MessageTypeEnum.SHOP_VERIFED.getType()) {
				return "您的店铺被管理员设为认证店铺!";
			} else if (type == MessageTypeEnum.SHOP_UNVERIFED.getType()) {
				return "您的店铺被管理员取消了认证!";
			} else {
			}
		} catch (Exception e) {
			// do nothing
		}
		String temp = content;
		if (temp != null) {
			while (temp.indexOf("<") != -1) {
				int index = temp.indexOf("<");
				String pre = temp.substring(0, index);
				String post = temp.substring(index + 1);
				temp = pre + "&lt;" + post;
			}
			while (temp.indexOf(">") != -1) {
				int index = temp.indexOf(">");
				String pre = temp.substring(0, index);
				String post = temp.substring(index + 1);
				temp = pre + "&gt;" + post;
			}
		}
		return temp;
	}
}
