package ca.weindex.web.util;

import ca.weindex.common.model.Address;
import ca.weindex.common.model.BaseComment;
import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.Reddit;


public class ValidateUtil {
	public static Offer truncateOffer(Offer offer) {
		if (offer == null) {
			return new Offer();
		}
		if (offer.getName() != null) {
			offer.setName(escapeTag(offer.getName()));
			if (offer.getName().trim().length() > 75) {
				offer.setName(offer.getName().trim().substring(0, 75));
			}			
		}
		if (offer.getDesc() != null) {
			offer.setDesc(escapeTag(offer.getDesc()));
		}
		if (offer.getDesc() != null && offer.getDesc().trim().length() > 160) {
			offer.setDesc(offer.getDesc().trim().substring(0, 160));
		}
		if (offer.getPrice() != null) {
			String price = offer.getPrice();
			try {
				Double.parseDouble(price);
			} catch (Exception e) {
				price = "0.00";
			}
			if (price.trim().length() > 40) {
				price = price.trim().substring(0, 40);
			}
			offer.setPrice(price);
		}
		return offer;
	}
	
	public static Shop truncateShop(Shop shop) {
		if (shop == null) {
			return new Shop();
		}
		if (shop.getName() != null) {
			shop.setName(escapeTag(shop.getName()));
		}
		if (shop.getName() != null && shop.getName().trim().length() > 10) {
			shop.setName(shop.getName().trim().substring(0, 10));
		}
		shop.setDisplayName(escapeTag(shop.getDisplayName()));
		if (shop.getDisplayName() != null && shop.getDisplayName().trim().length() > 75) {
			shop.setDisplayName(shop.getDisplayName().trim().substring(0, 75));
		}
		shop.setDesc(escapeTag(shop.getDesc()));
		if (shop.getDesc() != null && shop.getDesc().trim().length() > 160) {
			shop.setDesc(shop.getDesc().trim().substring(0, 160));
		}
		shop.setCountry(escapeTag(shop.getCountry()));
		if (shop.getCountry() != null && shop.getCountry().trim().length() > 40) {
			shop.setCountry(shop.getCountry().trim().substring(0, 40));
		}
		shop.setCity(escapeTag(shop.getCity()));
		if (shop.getCity() != null && shop.getCity().trim().length() > 40) {
			shop.setCity(shop.getCity().trim().substring(0, 40));
		}
		shop.setAddress(escapeTag(shop.getAddress()));
		if (shop.getAddress() != null && shop.getAddress().trim().length() > 80) {
			shop.setAddress(shop.getAddress().trim().substring(0, 80));
		}
		shop.setZip(escapeTag(shop.getZip()));
		if (shop.getZip() != null && shop.getZip().trim().length() > 16) {
			shop.setZip(shop.getZip().trim().substring(0, 16));
		}
		shop.setTelephone(escapeTag(shop.getTelephone()));
		if (shop.getTelephone() != null && shop.getTelephone().trim().length() > 20) {
			shop.setTelephone(shop.getTelephone().trim().substring(0, 20));
		}
		shop.setBulletin(escapeTag(shop.getBulletin()));
		if (shop.getBulletin() != null && shop.getBulletin().trim().length() > 160) {
			shop.setBulletin(shop.getBulletin().trim().substring(0, 160));
		}
		if (shop.getOfferLabels() != null && shop.getOfferLabels().length() > 500) {
			shop.setOfferLabels(shop.getOfferLabels().trim().substring(0, 500));
		}
		if (shop.getBlogLabels() != null && shop.getBlogLabels().length() > 500) {
			shop.setBlogLabels(shop.getBlogLabels().trim().substring(0, 500));
		}
		return shop;
	}
	
	public static Address truncateAddress(Address addr) {
		if (addr == null) {
			return new Address();
		}
		addr.setCountry(escapeTag(addr.getCountry()));
		if (addr.getCountry() != null && addr.getCountry().trim().length() > 40) {
			addr.setCountry(addr.getCountry().trim().substring(0, 40));
		}
		addr.setCity(escapeTag(addr.getCity()));
		if (addr.getCity() != null && addr.getCity().trim().length() > 40) {
			addr.setCity(addr.getCity().trim().substring(0, 40));
		}
		addr.setAddress(escapeTag(addr.getAddress()));
		if (addr.getAddress() != null && addr.getAddress().trim().length() > 80) {
			addr.setAddress(addr.getAddress().trim().substring(0, 80));
		}
		addr.setZip(escapeTag(addr.getZip()));
		if (addr.getZip() != null && addr.getZip().trim().length() > 16) {
			addr.setZip(addr.getZip().trim().substring(0, 16));
		}
		return addr;
	}
	
	public static Blog truncateBlog(Blog blog) {
		if (blog == null) {
			return new Blog();
		}
		blog.setTitle(escapeTag(blog.getTitle()));
		if (blog.getTitle() != null && blog.getTitle().trim().length() > 75) {
			blog.setTitle(blog.getTitle().trim().substring(0, 75));
		}
		blog.setSummary(escapeTag(blog.getSummary()));
		if (blog.getSummary() != null && blog.getSummary().trim().length() > 160) {
			blog.setSummary(blog.getSummary().trim().substring(0, 160));
		}
		blog.setContent(escapeTag(blog.getContent()));
		if (blog.getContent() != null && blog.getContent().trim().length() > 5000000) {
			blog.setContent(blog.getContent().trim().substring(0, 5000000));
		}
		return blog;
	}

	public static Reddit truncateReddit(Reddit reddit) {
		if (reddit == null) {
			return new Reddit();
		}
		reddit.setTitle(escapeTag(reddit.getTitle()));
		if (reddit.getTitle() != null && reddit.getTitle().trim().length() > 75) {
			reddit.setTitle(reddit.getTitle().trim().substring(0, 75));
		}

		reddit.setLinkUrl(escapeTag(reddit.getLinkUrl()));
		if (reddit.getLinkUrl() != null && reddit.getLinkUrl().trim().length() > 75) {
			reddit.setLinkUrl(reddit.getLinkUrl().trim().substring(0, 75));
		}

		reddit.setDescription(escapeTag(reddit.getDescription()));
		if (reddit.getDescription() != null && reddit.getDescription().trim().length() > 160) {
			reddit.setDescription(reddit.getDescription().trim().substring(0, 160));
		}
		reddit.setContent(escapeTag(reddit.getContent()));
		if (reddit.getContent() != null && reddit.getContent().trim().length() > 5000000) {
			reddit.setContent(reddit.getContent().trim().substring(0, 5000000));
		}
		return reddit;
	}

	
	public static <T extends BaseComment> T truncateComment(T comment) {
		if (comment == null) {
			return null;
		}
		comment.setTitle(escapeTag(comment.getTitle()));
		if (comment.getTitle() != null && comment.getTitle().length() > 75) {
			comment.setTitle(comment.getTitle().substring(0, 75));
		}
		comment.setContent(escapeTag(comment.getContent()));
		if (comment.getContent() != null && comment.getContent().trim().length() > 160) {
			comment.setContent(comment.getContent().trim().substring(0, 160));
		}
		return comment;
	}
	
	public static String escapeTag(String src) {
		if (src != null) {
			String tmp = src;
			while (tmp.indexOf("<") != -1) {
				int index = tmp.indexOf("<");
				String pre = tmp.substring(0, index);
				String post = tmp.substring(index + 1);
				tmp = pre + "&lt;" + post;
			}
			
			while (tmp.indexOf(">") != -1) {
				int index = tmp.indexOf(">");
				String pre = tmp.substring(0, index);
				String post = tmp.substring(index + 1);
				tmp = pre + "&gt;" + post;
			}
			return tmp;
		}
		return src;
	}
}
