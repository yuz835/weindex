package ca.weindex.common.model;

import java.util.ArrayList;
import java.util.List;

public class Shop extends BaseFav {
	private int id;
	private int userId;
	private String name;
	private String displayName;
	private String desc;
	private String bulletin;
	private String label;
	private List<Label> labelList;
	private String offerLabels;
	private List<Label> offerLabelList;
	private String blogLabels;
	private List<Label> blogLabelList;
	private int verified;

	private List<ShopLabel> shopLabelList;
	
	private String logoUrl;
	private String bannerUrl;

	private int offerNum;
	private int blogNum;

	private int visitNum;
	private int commentNum;

	// private Address address;

	private String country;
	private String city;
	private String address;
	private Integer longi;
	private Integer lati;
	private String zip;

	private String telephone;

	private User owner;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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

	public boolean isVerified() {
		if (verified == 1) {
			return true;
		} else {
			return false;
		}
	}

	public void setVerified(boolean verified) {
		if (verified) {
			this.verified = 1;
		} else {
			this.verified = 0;
		}
	}

	public int getVerified() {
		return verified;
	}

	public void setVerified(int verified) {
		this.verified = verified;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getLongi() {
		return longi;
	}

	public void setLongi(Integer longi) {
		this.longi = longi;
	}

	public Integer getLati() {
		return lati;
	}

	public void setLati(Integer lati) {
		this.lati = lati;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	// public Address getAddress() {
	// return address;
	// }
	//
	// public void setAddress(Address address) {
	// this.address = address;
	// }

	public void updateAddress(Address addr) {
		this.country = addr.getCountry();
		this.city = addr.getCity();
		this.address = addr.getAddress();
		this.zip = addr.getZip();
		this.longi = addr.getLongi();
		this.lati = addr.getLati();
	}

	public List<Label> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<Label> labelList) {
		this.labelList = labelList;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public int getOfferNum() {
		return offerNum;
	}

	public void setOfferNum(int offerNum) {
		this.offerNum = offerNum;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public int getBlogNum() {
		return blogNum;
	}

	public void setBlogNum(int blogNum) {
		this.blogNum = blogNum;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getBulletin() {
		return bulletin;
	}

	public void setBulletin(String bulletin) {
		this.bulletin = bulletin;
	}

	public boolean isHasBulletin() {
		if (bulletin != null && bulletin.trim().length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<String> getBulletinContent() {
		List<String> list = new ArrayList<String>();
		if (bulletin != null && bulletin.trim().length() > 0) {
			String temp = bulletin.trim();
			while (temp.contains("\n")) {
				int index = temp.indexOf("\n");
				String pre = temp.substring(0, index).trim();
				temp = temp.substring(index + 1).trim();
				if (pre.length() > 0) {
					list.add(pre);
				}
			}
			list.add(temp);
		}
		return list;

	}

	public String getOfferLabels() {
		return offerLabels;
	}

	public void setOfferLabels(String offerLabels) {
		this.offerLabels = offerLabels;
	}

	public List<Label> getOfferLabelList() {
		return offerLabelList;
	}

	public void setOfferLabelList(List<Label> offerLabelList) {
		this.offerLabelList = offerLabelList;
	}

	public String getBlogLabels() {
		return blogLabels;
	}

	public void setBlogLabels(String blogLabels) {
		this.blogLabels = blogLabels;
	}

	public List<Label> getBlogLabelList() {
		return blogLabelList;
	}

	public void setBlogLabelList(List<Label> blogLabelList) {
		this.blogLabelList = blogLabelList;
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

}
