package ca.weindex.common.enums;

public enum LabelTypeEnum {
	GENERAL(0), SHOP(1), OFFER(2), BLOG(3), REDDIT(4);

	private int type;

	private LabelTypeEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
