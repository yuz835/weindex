package ca.weindex.common.model;

public class UserDislikedReddit extends BaseFav {
	private int id;
	private int redditId;
	private int userId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRedditId() {
		return redditId;
	}

	public void setRedditId(int redditId) {
		this.redditId = redditId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


}
