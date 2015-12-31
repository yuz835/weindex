package ca.weindex.common.model;

public class UserLikedComment extends BaseFav {
	private int id;
	private int commentId;
	private int userId;
	private int type; //GENERAL(0), SHOP(1), OFFER(2), BLOG(3), REDDIT(4);

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
