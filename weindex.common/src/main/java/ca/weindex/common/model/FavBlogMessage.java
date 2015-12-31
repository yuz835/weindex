package ca.weindex.common.model;

import java.util.HashMap;
import java.util.Map;

import ca.weindex.common.enums.MessageTypeEnum;

import com.alibaba.fastjson.JSON;

public class FavBlogMessage extends Message {
	private int blogId;
	private String blogTitle;
	private int userId;
	private String userName;
	
	public FavBlogMessage() {
		super();
		this.setType(MessageTypeEnum.OFFER_FAVORITE);
	}

	public String getContent() {
		String content = super.getContent();
		if (content == null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("blogId", Integer.toString(blogId));
			map.put("blogTitle", blogTitle);
			map.put("userId", Integer.toString(userId));
			map.put("userName", userName);
			content = JSON.toJSONString(map);
			super.setContent(content);
		}
		return content;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getBlogId() {
		return blogId;
	}

	public void setBlogId(int blogId) {
		this.blogId = blogId;
	}

	public String getBlogTitle() {
		return blogTitle;
	}

	public void setBlogTitle(String blogTitle) {
		this.blogTitle = blogTitle;
	}

}
