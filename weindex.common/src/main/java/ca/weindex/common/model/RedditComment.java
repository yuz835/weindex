package ca.weindex.common.model;

public class RedditComment extends BaseComment {

	private int level;
	private int likedNum;
	private int redditId;

	public int getRedditId() {
		return redditId;
	}

	public void setRedditId(int redditId) {
		this.redditId = redditId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
