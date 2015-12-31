package ca.weindex.common.model;

import java.util.HashMap;
import java.util.Map;

import ca.weindex.common.enums.MessageTypeEnum;

import com.alibaba.fastjson.JSON;

public class OfferCommentMessage extends Message {
	private int offerId;
	private String offerTitle;
	private String commentTitle;

	public OfferCommentMessage() {
		super();
		this.setType(MessageTypeEnum.OFFER_COMMENT);
	}
	
	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
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
			map.put("offerId", Integer.toString(offerId));
			map.put("offerTitle", offerTitle);
			map.put("title", commentTitle);
			content = JSON.toJSONString(map);
			super.setContent(content);
		}
		return content;
	}

	public String getOfferTitle() {
		return offerTitle;
	}

	public void setOfferTitle(String offerTitle) {
		this.offerTitle = offerTitle;
	}
}
