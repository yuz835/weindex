package ca.weindex.common.model;

public abstract class Image {
	private int id;
	private String name;
	private String type;
	private String desc;
	// Default 0 means it's not thumb pic
	// others are all for user define
	private int thumbType = 0;
	private byte[] img;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getThumbType() {
		return thumbType;
	}

	public void setThumbType(int i) {
		this.thumbType = i;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

}
