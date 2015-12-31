package ca.weindex.common.model;

public class OfferImage extends Image {
	private int offerId;
	private int offerLogo = 0;
	
	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public int getOfferLogo() {
		return offerLogo;
	}

	public void setOfferLogo(int offerLogo) {
		this.offerLogo = offerLogo;
	}
}
