package ca.weindex.common.model;

import java.util.HashMap;
import java.util.Map;

import ca.weindex.common.enums.MessageTypeEnum;

import com.alibaba.fastjson.JSON;

public class BlogCommentMessage extends Message {
	private int blogId;
	private String blogTitle;
	private String commentTitle;

	public BlogCommentMessage() {
		super();
		this.setType(MessageTypeEnum.BLOG_COMMENT);
	}
	
	public int getBlogId() {
		return blogId;
	}

	public void setBlogId(int blogId) {
		this.blogId = blogId;
	}

	public String getCommentTitle() {
		return commentTitle;
	}

	public void setCommentTitle(String commentTitle) {
		this.commentTitle = commentTitle;
	}
	
	public String getContent() {
		String content = super.getContent();
		if (content == null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("blogId", Integer.toString(blogId));
			map.put("blogTitle", blogTitle);
			map.put("title", commentTitle);
			content = JSON.toJSONString(map);
			super.setContent(content);
		}
		return content;
	}

	public String getBlogTitle() {
		return blogTitle;
	}

	public void setBlogTitle(String blogTitle) {
		this.blogTitle = blogTitle;
	}
}
