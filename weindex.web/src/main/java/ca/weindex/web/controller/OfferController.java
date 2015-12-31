package ca.weindex.web.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ca.weindex.common.constants.SessionConstants;
import ca.weindex.common.enums.LabelTypeEnum;
import ca.weindex.common.model.Label;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.OfferComment;
import ca.weindex.common.model.OfferImage;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;
import ca.weindex.common.model.UserFavoriteOffer;
import ca.weindex.common.util.UUIDGenerator;
import ca.weindex.services.FavoriteService;
import ca.weindex.services.LabelService;
import ca.weindex.services.OfferService;
import ca.weindex.services.ShopService;
import ca.weindex.services.UserService;
import ca.weindex.web.helper.LabelHelper;
import ca.weindex.web.helper.WeiboHelper;
import ca.weindex.web.util.ValidateUtil;

@Controller
@RequestMapping("offer")
public class OfferController {
	@Autowired
	private OfferService offerService;

	@Autowired
	private ShopService shopService;

	@Autowired
	private LabelService labelService;

	@Autowired
	private FavoriteService favoriteService;

	@Autowired
	private UserService userService;

	@RequestMapping("/view/{id}")
	public ModelAndView index(@PathVariable int id, @ModelAttribute("page") Pagination page, HttpSession session,
			HttpServletRequest request) {
		Offer offer = offerService.getOffer(id);
		if (offer != null) {
			Shop shop = shopService.getShopWithDetail(offer.getShopId());
			ModelAndView view = new ModelAndView("offer/index");
			view.addObject("offer", offer);
			view.addObject("shop", shop);
			List<Label> labelList = labelService.getLabel();
			view.addObject("labelList", labelList);
			List<Label> allLabelList = labelService.getLabel();
			view.addObject("allLabelList", allLabelList);
			List<Label> myLabels = LabelHelper.convertLabel(offer.getLabel(), labelList);
			view.addObject("mylabels", myLabels);

			List<Label> shopLabels = LabelHelper.convertLabel(offer.getShopLabel(), shop.getShopLabelList());
			view.addObject("shopLabels", shopLabels);
			
			User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
			if (user != null) {
				if (offer.getVisible() != 0 && user.getId() != shop.getUserId()) {
					// offer is invisible
					return new ModelAndView("redirect:/");
				}
				UserFavoriteOffer favOffer = new UserFavoriteOffer();
				favOffer.setOfferId(id);
				favOffer.setUserId(user.getId());
				boolean b = favoriteService.checkFavOffer(favOffer);
				view.addObject("favorited", b);
			} else {
				view.addObject("favorited", false);
			}
			if ("true".equals(request.getParameter("weibo"))) {
				view.addObject("weibo", true);
			} else {
				view.addObject("weibo", false);
			}

			// get comment
			SearchResult<OfferComment> comments = offerService.getOfferComments(id, page);
			view.addObject("commentList", comments);

			// get image list
			List<OfferImage> imageList = offerService.getOfferImageByOfferId(id);			// real size
			// real size image list
			List<OfferImage> imageRealList  = new ArrayList<OfferImage>(); 			
			
			if (imageList != null && imageList.size() > 0) {
				//view.addObject("imageList", imageList);
				
				OfferImage image = null;
				for (OfferImage img : imageList) {
					if(img.getThumbType() == 0) {
						imageRealList.add(img);
					};
					if (img.getOfferLogo() > 0 && img.getThumbType() == 1) {
						image = img;
					} 
				}
				if (image == null) {
					image = imageList.get(0);
				}
				String imgUrl = "http://weindex.ca/offer/image/" + image.getName() + "/" + image.getType() + "/show.html";
				view.addObject("offerImageUrl", imgUrl);
			}
			view.addObject("imageRealList", imageRealList);
			
			OfferImage logoImg = imageRealList.get(0);
			for (OfferImage img : imageRealList) {
				if(img.getOfferLogo() > 0) {
					logoImg = img;
					break;
				}; 
			}
			view.addObject("logoImg", logoImg);

			
			// weibo share content
			String pre = "【" + offer.getName() + ", 价格: $" + offer.getPrice() + "】";
			String post = "详情点击"; //zhangeth http://weindex.ca/offer/view/" + offer.getId() + ".html ";
			int length = 200 - (pre.length() + post.length());
			String desc = offer.getDesc();
			if (desc == null) {
				desc = "";
			}
			if (desc.length() > length) {
				desc = desc.substring(0, length - 3) + "...";
			}
			String weiboContent = pre + desc + post;
			try {
				weiboContent = URLEncoder.encode(weiboContent, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// do nothing
			}
			view.addObject("offerShareContent", weiboContent);
			
			return view;
		}
		ModelAndView view = new ModelAndView("redirect:/index.html");
		return view;
	}

	@RequestMapping("/delete")
	public String delete(int offerId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		Offer offer = offerService.getOffer(offerId);
		if (offer != null) {
			Shop shop = shopService.getShopWithDetail(offer.getShopId());
			if (shop == null || shop.getUserId() != user.getId()) {
				return "redirect:/";
			}
			boolean b = offerService.deleteOffer(offerId, shop.getId());
			if (b) {
				return "redirect:/shop/view/" + shop.getName() + ".html";
			}
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(int offerId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Offer offer = offerService.getOffer(offerId);
		if (offer != null) {
			Shop shop = shopService.getShopWithDetail(offer.getShopId());
			if (shop.getUserId() != user.getId()) {
				return new ModelAndView("redirect:/");
			}
			ModelAndView view = new ModelAndView("offer/edit");
			view.addObject("offer", offer);
			view.addObject("shop", shop);
			List<Label> labelList = labelService.getLabelByType(LabelTypeEnum.OFFER);
			view.addObject("labelList", labelList);		
			int labelLevel = 1; 
			List<Label> parentLabelList = labelService.getLabelByTypeLevel(LabelTypeEnum.OFFER, labelLevel);
			view.addObject("parentLabelList", parentLabelList);
			List<Label> allLabelList = labelService.getLabel();
			view.addObject("allLabelList", allLabelList);
			List<Label> myLabels = LabelHelper.convertLabel(offer.getLabel(), labelList);
			view.addObject("mylabels", myLabels);

			List<Label> shopLabels = LabelHelper.convertLabel(offer.getShopLabel(), shop.getShopLabelList());
			view.addObject("shopLabels", shopLabels);
			
			UserFavoriteOffer favOffer = new UserFavoriteOffer();
			favOffer.setOfferId(offerId);
			favOffer.setUserId(user.getId());
			boolean b = favoriteService.checkFavOffer(favOffer);
			view.addObject("favorited", b);

			return view;
		}
		ModelAndView view = new ModelAndView("redirect:/index.html");
		return view;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editOffer(int shopId, int offerId, String name, String desc, String label, String shopLabel, String imgUrl,
			String thumbListImgUrl, String thumbShowImgUrl,
			String price, int quality, int type, int visible, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Shop shop = shopService.getShop(user.getShopName());
			if (shop != null && shop.getId() == shopId) {
				Offer offer = new Offer();
				offer.setId(offerId);
				offer.setName(name);
				offer.setDesc(desc);
				offer.setLabel(label);
				offer.setShopLabel(shopLabel);
				offer.setImgUrl(imgUrl);
				offer.setThumbListImgUrl(thumbListImgUrl);
				offer.setThumbShowImgUrl(thumbShowImgUrl);
				offer.setPrice(price);
				offer.setQuality(quality);
				offer.setType(type);
				offer.setVisible(visible);
				offer.setShopId(shopId);
				offer = ValidateUtil.truncateOffer(offer);
				boolean b = offerService.updateOffer(offer);
				if (b) {
					return "redirect:/offer/view/" + offerId + ".html";
				}
			}
		}
		return "redirect:/index.html";
	}

	@RequestMapping("manageImage/{offerId}")
	public ModelAndView manageOfferImage(@PathVariable int offerId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Offer offer = offerService.getOffer(offerId);
		if (offer == null) {
			return new ModelAndView("redirect:/");
		}
		Shop shop = shopService.getShopWithDetail(offer.getShopId());
		if (shop.getUserId() != user.getId()) {
			return new ModelAndView("redirect:/");
		}
		List<OfferImage> list = offerService.getOfferImageByOfferId(offerId);
		List<OfferImage> listImagelist = new ArrayList<OfferImage>(); 			
		for (OfferImage img : list) {
			if(img.getThumbType() == 2) {
				listImagelist.add(img);
			};
		}

		ModelAndView view = new ModelAndView("offer/images");
		view.addObject("offer", offer);
		view.addObject("shop", shop);
		view.addObject("list", listImagelist);
		return view;
	}

	@RequestMapping("{offerId}/deleteImage")
	public ModelAndView deleteOfferImage(@PathVariable int offerId, int offerImageId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Offer offer = offerService.getOffer(offerId);
		if (offer == null) {
			return new ModelAndView("redirect:/");
		}
		Shop shop = shopService.getShopWithDetail(offer.getShopId());
		if (shop.getUserId() != user.getId()) {
			return new ModelAndView("redirect:/");
		}
		OfferImage imageThumbListTodel = offerService.getOfferImageById(offerImageId);		
		String imageName = imageThumbListTodel.getName();
		String imageType = imageThumbListTodel.getType();
		// delete the prefix "thumb_list_"
		imageName = imageName.substring(11);
		OfferImage imageTodel = offerService.getOfferImage(imageName, imageType);
		OfferImage imageThumbShowTodel = offerService.getOfferImage("thumb_show_" + imageName, imageType);
		// get and delete the thumb images as well
		boolean b = offerService.deleteOfferImage(offerImageId);
		if(imageTodel != null) {
			offerService.deleteOfferImage(imageTodel.getId());
		};
		if(imageThumbShowTodel != null) {
			offerService.deleteOfferImage(imageThumbShowTodel.getId());
		};
		if (b) {
			return new ModelAndView("redirect:/offer/manageImage/" + offerId + ".html");
		} else {
			return new ModelAndView("redirect:/offer/manageImage/" + offerId + ".html?deleteSuccess=false");
		}
	}

	@RequestMapping("{offerId}/defaultLogo")
	public ModelAndView setOfferDefaultImage(@PathVariable int offerId, int offerImageId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Offer offer = offerService.getOffer(offerId);
		if (offer == null) {
			return new ModelAndView("redirect:/");
		}
		Shop shop = shopService.getShopWithDetail(offer.getShopId());
		if (shop.getUserId() != user.getId()) {
			return new ModelAndView("redirect:/");
		}
		boolean b = offerService.updateOfferLogo(offerId, offerImageId);
		if (b) {
			// Thumb list image
			OfferImage image = offerService.getOfferImageById(offerImageId);	
			String imageName = image.getName();
			String imgtype = image.getType();
			// delete the prefix "thumb_list_"
			imageName = imageName.substring(11);
			
			// update the urls
			String ImageUrl = "/offerimage/show/" + imageName + "." + imgtype;
			offer.setImgUrl(ImageUrl);
			ImageUrl = "/offerimage/show/" + "thumb_show_"+ imageName + "." + imgtype;
			offer.setThumbShowImgUrl(ImageUrl);
			ImageUrl = "/offerimage/show/" + "thumb_list_"+ imageName + "." + imgtype;
			offer.setThumbListImgUrl(ImageUrl);			
			offerService.updateOffer(offer);
			return new ModelAndView("redirect:/offer/manageImage/" + offerId + ".html");
		} else {
			return new ModelAndView("redirect:/offer/manageImage/" + offerId + ".html?updateSuccess=false");
		}
	}

	@RequestMapping(value = "upload", method = RequestMethod.GET)
	public ModelAndView uploadImage(int offerId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Offer offer = offerService.getOffer(offerId);
		Shop shop = null;
		if (offer != null) {
			shop = shopService.getShopWithDetail(offer.getShopId());
			if (shop == null || shop.getUserId() != user.getId()) {
				return new ModelAndView("redirect:/");
			}
		}
		List<OfferImage> list = offerService.getOfferImageByOfferId(offerId);
		if (list.size() >= 15) { // every offer image has 3 copy of imgs due to thumb
			// Ë∂Ö‰∫Ü
			return new ModelAndView("redirect:/offer/manageImage/" + offerId + ".html");
		}
		ModelAndView view = new ModelAndView("shop/uploadImage");
		view.addObject("offerId", offerId);
		view.addObject("shop", shop);
		return view;
	}

	@RequestMapping(value = "uploadImage", method = RequestMethod.POST)
	public ModelAndView uploadImage(@RequestParam("file") MultipartFile file, int offerId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Offer offer = offerService.getOffer(offerId);
		if (offer != null) {
			Shop shop = shopService.getShopWithDetail(offer.getShopId());
			if (shop.getUserId() != user.getId()) {
				return new ModelAndView("redirect:/");
			}
		}
		List<OfferImage> list = offerService.getOfferImageByOfferId(offerId);
		if (list.size() >= 15) {
			// should be 15 as it includes thumb images as well
			return new ModelAndView("redirect:/offer/manageImage/" + offerId + ".html");
		}
		if (!file.isEmpty()) {
			try {
				String fileName = file.getOriginalFilename();
				if (fileName.indexOf(".") != -1) {
					MultipartFile tmpFile = file;
					String imgtype = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					String imgName = UUIDGenerator.getUUID().toLowerCase();
					OfferImage image = new OfferImage();
					// generate thumb image
					if(tmpFile.getSize()>0) {               				                    
						// thumb 1
						double thumbScale = 1.0;
						// 1. read the file stream, and get the height and width
						BufferedImage thumbImage = Thumbnails.of(tmpFile.getInputStream())
						.scale(1f) 
						.asBufferedImage(); 
						if(thumbImage.getHeight() > 600 || thumbImage.getWidth() > 600){
							if(thumbImage.getHeight() > thumbImage.getWidth()) {
								thumbScale = 600f/thumbImage.getWidth();
							} else {
								thumbScale = 600f/thumbImage.getHeight();
							};
						} else {
							thumbScale = 1;
						}
		                    	
						// 2. scale the image to max width/height 600	
						thumbImage = Thumbnails.of(thumbImage)
						.scale(thumbScale) 
						.asBufferedImage(); 
						
						// 3. write the org image to database first
						ByteArrayOutputStream baos = new ByteArrayOutputStream( 1000 );
						ImageIO.write(thumbImage, imgtype, baos );
						baos.flush();
						byte[] thumbImageBytes = baos.toByteArray();
						baos.close();
						image.setImg(thumbImageBytes);
						image.setOfferId(offer.getId());
						image.setName(imgName);
						image.setType(imgtype);
						image.setThumbType(0);
						offerService.insertOfferImage(image);
										                    
						// 4.scrop the image to 300x300
						thumbImage = Thumbnails.of(thumbImage)
						.sourceRegion(Positions.TOP_LEFT, 600,600)  
						.size(300, 300)   // image size
						.keepAspectRatio(false)
						.asBufferedImage(); 
						
						// 5. conver bufferedImage to byet[] list
						baos = new ByteArrayOutputStream( 1000 );
						ImageIO.write(thumbImage, imgtype, baos );
						baos.flush();
						thumbImageBytes = baos.toByteArray();
						baos.close();
						
						// 6. write the thumb show image to DATABASE
						image = new OfferImage();
						image.setOfferId(offer.getId());
						image.setName("thumb_show_"+ imgName);
						image.setType(imgtype);
						image.setImg(thumbImageBytes);
						image.setThumbType(1);
						offerService.insertOfferImage(image);						
						
						// 7. second thumb list image 145 x 145
						// scrop the image 
						thumbImage = Thumbnails.of(thumbImage)
						.sourceRegion(Positions.TOP_LEFT, 230,230)  
						.size(145, 145)   // image size
						.keepAspectRatio(false)
						.asBufferedImage(); 
						    		
						// 8. conver bufferedImage to byet[] list
						baos = new ByteArrayOutputStream( 1000 );
						ImageIO.write(thumbImage, imgtype, baos );
						baos.flush();
						thumbImageBytes = baos.toByteArray();
						baos.close();
						    		
						// 9. write the thumb show image to DATABASE
						image = new OfferImage();
						image.setOfferId(offer.getId());
						image.setName("thumb_list_"+ imgName);
						image.setType(imgtype);
						image.setImg(thumbImageBytes);
						image.setThumbType(2);
						offerService.insertOfferImage(image);	
					} else {
						throw new IOException();
					}
				} else {
					throw new IOException();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			ModelAndView view = new ModelAndView("redirect:/offer/manageImage/" + offerId + ".html");
			return view;
		} else {
			ModelAndView view = new ModelAndView("shop/uploadImage");
			view.addObject("offerId", offerId);
			return view;
		}
	}

	@RequestMapping("image/{name}/{type}/show.html")
	public ModelAndView showImage(@PathVariable String name, @PathVariable String type, HttpServletResponse response) {
		OfferImage image = offerService.getOfferImage(name, type);
		if (image != null) {
			if (type.equals("png")) {
				response.setContentType("image/png");
			} else if (type.equals("gif")) {
				response.setContentType("image/gif");
			} else {
				response.setContentType("image/jpeg");
			}
			response.setContentLength(image.getImg().length);
			try {
				ServletOutputStream output = response.getOutputStream();
				output.write(image.getImg());
				output.flush();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();

			}
			return null;
		} else {
			return null;
		}
	}

	// @RequestMapping("search")
	// public ModelAndView search(String keyword) {
	// if (keyword == null || keyword.length() == 0) {
	// ModelAndView view = new ModelAndView("redirect:/index.html");
	// return view;
	// }
	// List<Offer> list = offerService.searchOffer(keyword);
	// ModelAndView view = new ModelAndView("offer/searchlist");
	// view.addObject("list", list);
	// return view;
	// }

	@RequestMapping("weibo")
	public String publishWeibo(int offerId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null || user.getWeiboId() == null || user.getWeiboToken() == null) {
			return "redirect:/offer/view/" + offerId + ".html";
		}
		Offer offer = offerService.getOffer(offerId);
		if (offer == null) {
			return "redirect:/";
		}
		boolean b = WeiboHelper.publishOfferWeibo(offer, user, "");
		if (b) {
			return "redirect:/offer/view/" + offerId + ".html?weibo=true";
		}
		return "redirect:/offer/view/" + offerId + ".html";
	}

	@RequestMapping(value = "comment/add", method = RequestMethod.POST)
	public String addComment(int offerId, String title, String content, HttpServletRequest request, HttpSession session) {
		if (content == null || content.length() == 0 || content.length() > 150) {
			String referrer = request.getHeader("referer");
			return "redirect:" + referrer;
		}
		if (title == null || title.length() == 0) {
			title = "comment title";
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Offer offer = offerService.getOffer(offerId);
			if (offer == null) {
				return "redirect:/";
			}
			Shop shop = shopService.getShop(offer.getShopId());
			if (shop == null) {
				return "redirect:/";
			}
			int authorId = shop.getUserId();
			User author = userService.getUserById(authorId);
			if (author == null) {
				return "redirect:/";
			}
			OfferComment comment = new OfferComment();
			comment.setOfferId(offerId);
			comment.setTitle(title);
			comment.setContent(content);
			comment.setCreatorId(user.getId());
			comment.setCreatorName(user.getUserName());
			comment = ValidateUtil.truncateComment(comment);
			boolean b = offerService.insertOfferComment(comment, user, author);
			if (b) {
				return "redirect:/offer/view/" + offerId + ".html";
			}
		}
		return "redirect:/offer/view/" + offerId + ".html";
	}

	@RequestMapping("comment/del")
	public String delComment(int id, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		OfferComment comment = offerService.getOfferComment(id);
		if (comment == null) {
			return "redirect:/";
		}
		Offer offer = offerService.getOffer(comment.getOfferId());
		if (offer == null) {
			return "redirect:/";
		}
		Shop shop = shopService.getShop(offer.getShopId());
		if (shop == null || shop.getUserId() != user.getId()) {
			return "redirect:/";
		}
		boolean b = offerService.deleteOfferComment(id);
		if (b) {
			return "redirect:/offer/view/" + offer.getId() + ".html";
		} else {
			return "redirect:/offer/view/" + offer.getId() + ".html";
		}
	}

	@RequestMapping("visitlogo/{id}")
	public String addOfferVisitNum(@PathVariable int id) {
		offerService.addOfferVisitNum(id);
		return "json";
	}
	
	@RequestMapping("top/{id}")
	public String top(@PathVariable int id, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		Offer offer = offerService.getOffer(id);
		if (offer != null) {
			Shop shop = shopService.getShopWithDetail(offer.getShopId());
			if (shop == null || shop.getUserId() != user.getId()) {
				return "redirect:/";
			}
			offer.setPos(10);
			boolean b = offerService.topOfferOfShop(offer);
			if (b) {
				return "redirect:/offer/view/" + id + ".html";
			}
		}
		return "redirect:/";
	}
}
