package ca.weindex.common.model;

public class Address {
	private String country;
	private String city;
	private String address;
	private int longi;
	private int lati;
	private String zip;

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

	public int getLongi() {
		return longi;
	}

	public void setLongi(int longi) {
		this.longi = longi;
	}

	public int getLati() {
		return lati;
	}

	public void setLati(int lati) {
		this.lati = lati;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}
