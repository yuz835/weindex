package ca.weindex.common.model;

public class UserContactor {
	private int contactorId;
	private String contactor;
	private int readedNum;
	private int totalNum;

	public int getContactorId() {
		return contactorId;
	}

	public void setContactorId(int contactorId) {
		this.contactorId = contactorId;
	}

	public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public int getReadedNum() {
		return readedNum;
	}

	public void setReadedNum(int readedNum) {
		this.readedNum = readedNum;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	

	public int getUnreadedNum() {
		return totalNum - readedNum;
	}
}
