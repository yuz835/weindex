package ca.weindex.common.enums;

public enum WeiboTaskTypeEnum {
	OFFER(0),
	BLOG(1),
	UNKNOWN(-1),
	;
	
	private int type;
	
	private WeiboTaskTypeEnum(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static WeiboTaskTypeEnum getTaskType(int type) {
		WeiboTaskTypeEnum[] values = WeiboTaskTypeEnum.values();
		for (WeiboTaskTypeEnum t :values) {
			if (t.type == type) {
				return t;
			}
		}
		return UNKNOWN;
	}
}
