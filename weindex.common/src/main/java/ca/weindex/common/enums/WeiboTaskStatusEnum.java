package ca.weindex.common.enums;

public enum WeiboTaskStatusEnum {
	UNSTARTED(0),
	WAITING(1),
	SUCCESSED(2),
	FAILED(3);
	
	private int status;
	
	private WeiboTaskStatusEnum(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
	
	public static WeiboTaskStatusEnum getTaskStatus(int status) {
		WeiboTaskStatusEnum[] values = WeiboTaskStatusEnum.values();
		for (WeiboTaskStatusEnum t :values) {
			if (t.status == status) {
				return t;
			}
		}
		return UNSTARTED;
	}
}
