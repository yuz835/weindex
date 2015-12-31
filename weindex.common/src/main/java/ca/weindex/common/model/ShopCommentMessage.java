package ca.weindex.common.model;

import java.util.HashMap;
import java.util.Map;

import ca.weindex.common.enums.MessageTypeEnum;

import com.alibaba.fastjson.JSON;

public class ShopCommentMessage extends Message {
	private int shopId;
	private String shopTitle;
	private String shopName;
	private String commentTitle;

	public ShopCommentMessage() {
		super();
		this.setType(MessageTypeEnum.SHOP_COMMENT);
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
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
			map.put("shopId", Integer.toString(shopId));
			map.put("shopName", shopName);
			map.put("shopTitle", shopTitle);
			map.put("title", commentTitle);
			content = JSON.toJSONString(map);
			super.setContent(content);
		}
		return content;
	}

	public String getShopTitle() {
		return shopTitle;
	}

	public void setShopTitle(String shopTitle) {
		this.shopTitle = shopTitle;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
}
