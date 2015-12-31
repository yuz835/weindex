package ca.weindex.common.model;

import java.util.HashMap;
import java.util.Map;

import ca.weindex.common.enums.MessageTypeEnum;

import com.alibaba.fastjson.JSON;

public class FavOfferMessage extends Message {
	private int offerId;
	private String offerName;
	private int userId;
	private String userName;

	public FavOfferMessage() {
		super();
		this.setType(MessageTypeEnum.OFFER_FAVORITE);
	}

	public String getContent() {
		String content = super.getContent();
		if (content == null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("offerId", Integer.toString(offerId));
			map.put("userId", Integer.toString(userId));
			map.put("userName", userName);
			map.put("offerName", offerName);
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

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
}
