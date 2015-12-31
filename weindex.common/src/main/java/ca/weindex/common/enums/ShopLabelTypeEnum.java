package ca.weindex.common.enums;

public enum ShopLabelTypeEnum {
	OFFER(0), BLOG(1);

	private int type;

	private ShopLabelTypeEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
