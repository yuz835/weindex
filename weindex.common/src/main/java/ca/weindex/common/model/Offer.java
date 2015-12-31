package ca.weindex.common.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Offer extends BaseFav {
	private final static SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm (zzz)");

	private int id;
	private int shopId;
	private String name;
	private String desc;
	private String label;
	private String shopLabel;
	private String imgUrl;
	private String price;
	private int quality = 100;
	private int type = 0;
	private int visible = 0;
	private Date updateTime;
	private String thumbListImgUrl;
	private String thumbShowImgUrl;
	
	private List<Label> labelList;
	private List<ShopLabel> shopLabelList;
	
	private int visitNum;
	private int commentNum;
	private int pos;
	
	private List<OfferImage> imageList;
	
	private int userId;
	private String userName;
	private String shopName;
	private String shopDispName;
	private String telephone;
	private String city;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	// recover html description
	public String getHtmlDesc() {
	 	String src = desc;
		src = replace(src, "&lt;", "<");
		src = replace(src, "&gt;", ">");
		return src;
		
	};

	private String replace(String src, String from, String to) {
		if (src == null || from == null || from.length() == 0 || to == null) {
			return src;
		}
		while (src.indexOf(from) != -1) {
			int index = src.indexOf(from);
			String pre = src.substring(0, index);
			String post = src.substring(index + from.length());
			src = pre + to + post;
		}
		return src;
	}


	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getThumbListImgUrl() {
		return thumbListImgUrl;
	}

	public void setThumbListImgUrl(String thumbListImgUrl) {
		this.thumbListImgUrl = thumbListImgUrl;
	}

	public String getThumbShowImgUrl() {
		return thumbShowImgUrl;
	}

	public void setThumbShowImgUrl(String thumbShowImgUrl) {
		this.thumbShowImgUrl = thumbShowImgUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isProduct() {
		if (type == 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isService() {
		if (type != 0) {
			return true;
		} else {
			return false;
		}
	}

	public void setProduct(boolean b) {
		if (b) {
			type = 0;
		} else {
			type = 1;
		}
	}

	public void setService(boolean b) {
		if (b) {
			type = 1;
		} else {
			type = 0;
		}
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

//	public boolean isVisible() {
//		if (visible == 0) {
//			return true;
//		} else {
//			return false;
//		}
//	}

	public void setVisible(boolean visible) {
		if (visible) {
			this.visible = 0;
		} else {
			this.visible = 1;
		}
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getTime() {
		Date time = updateTime;
		if (time == null) {
			time = new Date();
		}
		return timeFormat.format(time);
	}

	public List<Label> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<Label> labelList) {
		this.labelList = labelList;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public String getShopLabel() {
		return shopLabel;
	}

	public void setShopLabel(String shopLabel) {
		this.shopLabel = shopLabel;
	}

	public List<ShopLabel> getShopLabelList() {
		return shopLabelList;
	}

	public void setShopLabelList(List<ShopLabel> shopLabelList) {
		this.shopLabelList = shopLabelList;
	}

	public int getVisitNum() {
		return visitNum;
	}

	public void setVisitNum(int visitNum) {
		this.visitNum = visitNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public List<OfferImage> getImageList() {
		return imageList;
	}

	public void setImageList(List<OfferImage> imageList) {
		this.imageList = imageList;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getShopDispName() {
		return shopDispName;
	}

	public void setShopDispName(String shopDispName) {
		this.shopDispName = shopDispName;
	}
}
