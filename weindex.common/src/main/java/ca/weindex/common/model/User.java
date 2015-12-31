package ca.weindex.common.model;

import ca.weindex.common.util.Md5Util;

public class User {
	private static final String ENCRYPT_PREFIX = "ames.it";

	private int id;
	private String userName;
	private String password;
	private String email;
	private String pwdMd5;
	private String token;

	private int admin;

	private String weiboId;
	private String weiboToken;
	private String weiboShowVerifyId;
	private String facebookId;

	private boolean withShop;
	private String shopName;

	// private Address address;

	private String country;
	private String city;
	private String address;
	private Integer longi;
	private Integer lati;
	private String zip;

	private String lostPwdToken;
	private long lostPwdTokenExpiry;
	
	private int unreadedMessageNum;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.pwdMd5 = Md5Util.getMd5(ENCRYPT_PREFIX + password);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwdMd5() {
		return pwdMd5;
	}

	public void setPwdMd5(String pwdMd5) {
		this.pwdMd5 = pwdMd5;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isWithShop() {
		return withShop;
	}

	public void setWithShop(boolean withShop) {
		this.withShop = withShop;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
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

	public int getAdmin() {
		return admin;
	}

	public void setAdmin(int admin) {
		this.admin = admin;
	}

	public boolean isAdmin() {
		if (admin > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void setAdmin(boolean isAdmin) {
		if (isAdmin) {
			admin = 1;
		} else {
			admin = 0;
		}
	}

	public String getWeiboId() {
		return weiboId;
	}

	public void setWeiboId(String weiboId) {
		this.weiboId = weiboId;
	}

	public String getWeiboShowVerifyId() {
		return weiboShowVerifyId;
	}

	public void setWeiboShowVerifyId(String weiboShowVerifyId) {
		this.weiboShowVerifyId = weiboShowVerifyId;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getWeiboToken() {
		return weiboToken;
	}

	public void setWeiboToken(String weiboToken) {
		this.weiboToken = weiboToken;
	}

	public String getLostPwdToken() {
		return lostPwdToken;
	}

	public void setLostPwdToken(String lostPwdToken) {
		this.lostPwdToken = lostPwdToken;
	}

	public long getLostPwdTokenExpiry() {
		return lostPwdTokenExpiry;
	}

	public void setLostPwdTokenExpiry(long lostPwdTokenExpiry) {
		this.lostPwdTokenExpiry = lostPwdTokenExpiry;
	}

	public int getUnreadedMessageNum() {
		return unreadedMessageNum;
	}

	public void setUnreadedMessageNum(int unreadedMessageNum) {
		this.unreadedMessageNum = unreadedMessageNum;
	}
}
