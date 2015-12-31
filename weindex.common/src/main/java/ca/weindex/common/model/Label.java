package ca.weindex.common.model;

public class Label {
	private int id;
	private String cnName;
	private String enName;
	private int type = 0;
	private String desc;
	private int visible;
	private int pos;
	// this is the parent label ID
	private int parentCategoryId = 0;
	//  
	private int currentCategoryId = 0;
	// label level 0 为最低，往上逐渐加
	private int levelId = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		if (visible > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setVisible(boolean visible) {
		if (visible) {
			this.visible = 1;
		} else {
			this.visible = 0;
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getParentCategoryId() {
		return parentCategoryId;
	};

	public void setParentCategoryId(int parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public int getCurrentCategoryId() {
		return currentCategoryId;
	};

	public void setCurrentCategoryId(int currentCategoryId) {
		this.currentCategoryId = currentCategoryId;
	}

	public int getLevelId() {
		return levelId;
	};

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}



}



