package ca.weindex.common.model;

import java.util.HashMap;
import java.util.Map;

import ca.weindex.common.enums.MessageTypeEnum;

import com.alibaba.fastjson.JSON;

public class FavShopMessage extends Message {
	private int shopId;
	private int userId;
	private String userName;

	public FavShopMessage() {
		super();
		this.setType(MessageTypeEnum.SHOP_FAVORITE);
	}

	public String getContent() {
		String content = super.getContent();
		if (content == null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("shopId", Integer.toString(shopId));
			map.put("userId", Integer.toString(userId));
			map.put("userName", userName);
			content = JSON.toJSONString(map);
			super.setContent(content);
		}
		return content;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
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
}
