package ca.weindex.common.enums;

public enum MessageTypeEnum {
	GENERAL(0, "General Message"),
	BLOG_COMMENT(1, "Comment Blog"),
	SHOP_FAVORITE(2, "Favorite Shop"),
	OFFER_FAVORITE(3, "Favorite Offer"),
	BLOG_FAVORITE(4, "Favorite Blog"),
	OFFER_SHARE_FAILED(5, "Share Offer to Weibo Failed"),
	SHOP_SHARE_FAILED(6, "Share Shop to Weibo Failed"),
	BLOG_SHARE_FAILED(7, "Share Blog to Weibo Failed"),
	SHOP_VERIFED(8, "Your shop is verifed"),
	SHOP_UNVERIFED(9, "Your shop is unverifed"),
	SHOP_COMMENT(10, "Comment Shop"),
	OFFER_COMMENT(11, "Comment Offer"),
	;

	private int type;
	private String desc;

	private MessageTypeEnum(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
