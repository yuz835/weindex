package ca.weindex.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ca.weindex.common.constants.SessionConstants;
import ca.weindex.common.enums.LabelTypeEnum;
import ca.weindex.common.model.Address;
import ca.weindex.common.model.Blog;
import ca.weindex.common.model.BlogImage;
import ca.weindex.common.model.Label;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.OfferImage;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.ShopComment;
import ca.weindex.common.model.ShopLabel;
import ca.weindex.common.model.User;
import ca.weindex.common.model.UserFavoriteShop;
import ca.weindex.common.util.UUIDGenerator;
import ca.weindex.services.BlogService;
import ca.weindex.services.FavoriteService;
import ca.weindex.services.LabelService;
import ca.weindex.services.OfferService;
import ca.weindex.services.ShopService;
import ca.weindex.services.UserService;
import ca.weindex.web.helper.JsonHelper;
import ca.weindex.web.helper.LabelHelper;
import ca.weindex.web.helper.WeiboHelper;
import ca.weindex.web.util.ValidateUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import net.coobird.thumbnailator.*;
import net.coobird.thumbnailator.geometry.Positions;
import java.awt.image.BufferedImage;

@Controller
@RequestMapping("shop")
public class ShopController {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ShopService shopService;

	@Autowired
	private OfferService offerService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private LabelService labelService;

	@Autowired
	private FavoriteService favoriteService;

	@Autowired
	private UserService userService;

	@RequestMapping("/view/{shopName}")
	public ModelAndView index(@PathVariable String shopName, String type, @ModelAttribute("page") Pagination page,
			HttpSession session) {
		Shop shop = shopService.getShop(shopName);
		if (shop == null) {
			return new ModelAndView("redirect:/");
		}
		ModelAndView view = new ModelAndView("shop/index");
		view.addObject("shop", shop);

		List<Label> allLabelList = labelService.getLabel();
		view.addObject("allLabelList", allLabelList);

		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		boolean isOwner = false;
		if (user != null) {
			if (user.getId() == shop.getUserId()) {
				isOwner = true;
			}

			UserFavoriteShop favShop = new UserFavoriteShop();
			favShop.setShopId(shop.getId());
			favShop.setUserId(user.getId());
			boolean b = favoriteService.checkFavShop(favShop);
			view.addObject("favorited", b);
		} else {
			view.addObject("favorited", false);
		}

		if ("blog".equalsIgnoreCase(type)) {
			SearchResult<Blog> blogs = blogService.getBlogByShopId(shop.getId(), page);
			view.addObject("blogList", blogs);
			view.addObject("type", "blog");
		} else if ("offer".equalsIgnoreCase(type)) {
			SearchResult<Offer> list = offerService.getOfferByShopId(shop.getId(), !isOwner, page);
			populateOfferListLabels(list.getList(), allLabelList);
			view.addObject("offerList", list);
			view.addObject("type", "offer");
		} else {
			view.addObject("type", "index");
			Pagination indexPage = new Pagination();
			indexPage.setPageSize(8);
			SearchResult<Blog> blogs = blogService.getBlogByShopId(shop.getId(), indexPage);
			view.addObject("blogList", blogs);
			SearchResult<Offer> list = offerService.getOfferByShopId(shop.getId(), !isOwner, indexPage);
			populateOfferListLabels(list.getList(), allLabelList);
			view.addObject("offerList", list);

			// get comment
			SearchResult<ShopComment> comments = shopService.getShopComments(shop.getId(), indexPage);
			view.addObject("commentList", comments);
		}
		
		String shopImageUrl = shop.getLogoUrl();
		if (shopImageUrl != null && shopImageUrl.length() > 0) {
			if (!shopImageUrl.startsWith("http")) {
				shopImageUrl = "http://weindex.ca" + shopImageUrl;
			}
			view.addObject("shopImageUrl", shopImageUrl);
		}
		// weibo share
		String shopContent = shop.getDesc();
		if (shopContent == null) {
			shopContent = "";
		}
		String pre = "【" + shop.getDisplayName() + "】 ";
		String post = " http://weindex.ca/" + shop.getName();
		int length = 130 - (pre.length() + post.length());
		if (shopContent.length() > length) {
			shopContent = shopContent.substring(0, length - 3) + "...";
		}
		String weiboContent = pre + shopContent + post;
		try {
			weiboContent = URLEncoder.encode(weiboContent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// do nothing
		}
		view.addObject("shopShareContent", weiboContent);

		return view;
	}

	private void populateOfferListLabels(List<Offer> list, List<Label> labels) {
		if (list == null || list.isEmpty()) {
			return;
		}
		for (Offer offer : list) {
			List<Label> myLabels = LabelHelper.convertLabel(offer.getLabel(), labels);
			offer.setLabelList(myLabels);
		}

	}

	@RequestMapping(value = "/open", method = RequestMethod.GET)
	public String showOpenPage() {
		return "shop/open";
	}

	@RequestMapping(value = "/open", method = RequestMethod.POST)
	public ModelAndView open(String shopName, String dispName, HttpSession session) {
		if (shopName == null || shopName.length() < 4 || dispName == null || dispName.length() == 0
				|| dispName.contains(" ")) {
			return new ModelAndView("redirect:/");
		}
		Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]{3,19}");
		if (!p.matcher(shopName).matches()) {
			return new ModelAndView("redirect:/");
		}
		
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			// clear session info
			Shop shop = new Shop();
			shop.setUserId(user.getId());
			shop.setName(shopName);
			shop.setDisplayName(dispName);
			shop = ValidateUtil.truncateShop(shop);
			Shop temp = shopService.getShop(shop.getName());
			if (temp != null) {
				ModelAndView view = new ModelAndView("shop/open");
				String msg = "该店铺域名已被占用, 请尝试使用其他的名称";
				view.addObject("msg", msg);
				view.addObject("shopName", shopName);
				view.addObject("dispName", dispName);
				return view;
			}
			boolean b = shopService.openShop(shop);
			if (b) {
				// need update the user
				user.setWithShop(true);
				user.setShopName(shopName);
				return new ModelAndView("redirect:/" + shopName);
			}
		}
		return new ModelAndView("redirect:/index.html");
	}

	@RequestMapping(value = "/dname", method = RequestMethod.GET)
	public ModelAndView editDisplayName(HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Shop shop = shopService.getShop(user.getShopName());
			if (shop != null) {
				ModelAndView view = new ModelAndView("shop/dname");
				view.addObject("shop", shop);
				return view;
			}
		}
		ModelAndView view = new ModelAndView("redirect:/index.html");
		return view;
	}

	@RequestMapping(value = "/dname/edit", method = RequestMethod.POST)
	public String editDisplayName(String displayName, String shopName, HttpSession session) {
		if (displayName == null || displayName.length() == 0 || displayName.contains(" ")) {
			return "shop/desc";
		}
		if (displayName.trim().length() > 20) {
			displayName = displayName.trim().substring(0, 20);
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			int userId = user.getId();
			boolean b = shopService.updateDisplayName(userId, displayName);
			if (b) {
				return "redirect:/" + shopName;
			}
		}
		return "shop/desc";
	}

	@RequestMapping(value = "/desc", method = RequestMethod.GET)
	public ModelAndView editDesc(HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Shop shop = shopService.getShop(user.getShopName());
			if (shop != null) {
				ModelAndView view = new ModelAndView("shop/desc");
				view.addObject("shop", shop);
				return view;
			}
		}
		ModelAndView view = new ModelAndView("redirect:/index.html");
		return view;
	}

	@RequestMapping(value = "/desc/edit", method = RequestMethod.POST)
	public String editDesc(String desc, String shopName, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			int userId = user.getId();
			if (desc != null && desc.trim().length() > 160) {
				desc = desc.trim().substring(0, 160);
			}
			boolean b = shopService.updateDesc(userId, desc);
			if (b) {
				return "redirect:/" + shopName;
			}
		}
		return "shop/desc";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Shop shop = shopService.getShop(user.getShopName());
			if (shop != null) {
				ModelAndView view = new ModelAndView("shop/edit");
				view.addObject("shop", shop);

				List<Label> labelList = labelService.getLabelByType(LabelTypeEnum.SHOP);
				//for (Label l : labelList) {
				//	System.out.println("hoho");
				//	System.out.println(l.getCnName());
				//};
				view.addObject("labelList", labelList);
				List<Label> allLabelList = labelService.getLabel();
				view.addObject("allLabelList", allLabelList);
				List<Label> myLabels = LabelHelper.convertLabel(shop.getLabel(), labelList);
				view.addObject("mylabels", myLabels);

				return view;
			}
		}
		ModelAndView view = new ModelAndView("redirect:/index.html");
		return view;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(String displayName, String desc, String label, String telephone, String bulletin,
			String shopName, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		Shop shop = shopService.getShop(shopName);
		if (user == null || shop == null || shop.getUserId() != user.getId()) {
			return "redirect:/" + shopName;
		}
		// FIXME check dispname, desc, telephone, bulletin

		shop.setDisplayName(displayName);
		shop.setTelephone(telephone);
		shop.setDesc(desc);
		shop.setBulletin(bulletin);
		shop.setLabel(label);
		shop = ValidateUtil.truncateShop(shop);
		shopService.updateShop(shop);
		return "redirect:/" + shopName;
	}

	@RequestMapping(value = "/label", method = RequestMethod.GET)
	public ModelAndView editLabel(HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Shop shop = shopService.getShop(user.getShopName());
			if (shop != null) {
				List<Label> labelList = labelService.getLabelByType(LabelTypeEnum.SHOP);
				ModelAndView view = new ModelAndView("shop/label");
				view.addObject("shop", shop);
				view.addObject("labels", labelList);
				return view;
			}
		}
		ModelAndView view = new ModelAndView("redirect:/index.html");
		return view;
	}

	@RequestMapping("/{shopId}/label/delete")
	public String deleteLabel(@PathVariable int shopId, String labelId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Shop shop = shopService.getShop(shopId);
			if (shop != null && (shop.getName().equals(user.getShopName()) || user.isAdmin())) {
				String l = shop.getLabel();
				if (l != null && l.length() > 0) {
					String[] ls = l.split(",");
					StringBuilder sb = new StringBuilder();
					for (String ll : ls) {
						if (ll.equals(labelId)) {
							continue;
						}
						if (sb.length() > 0) {
							sb.append(",");
						}
						sb.append(ll);
					}
					shopService.updateLabel(shop.getUserId(), sb.toString());
				}
				return "redirect:/shop/label.html";
			}
		}
		return "redirect:/index.html";
	}

	@RequestMapping("/{shopId}/label/add")
	public String addLabel(@PathVariable int shopId, int labelId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Shop shop = shopService.getShop(shopId);
			if (shop != null && (shop.getName().equals(user.getShopName()) || user.isAdmin())) {
				String l = shop.getLabel();
				if (l != null) {
					if (l.startsWith(Integer.toString(labelId)) || l.endsWith("," + labelId)
							|| l.contains("," + labelId + ",")) {
						// the label is here
						return "redirect:/shop/label.html";
					}
					if (l.length() > 0) {
						l += "," + labelId;
					} else {
						l = Integer.toString(labelId);
					}
					shopService.updateLabel(shop.getUserId(), l);
					return "redirect:/shop/label.html";
				}
				return "redirect:/shop/label.html";
			}
		}
		return "redirect:/index.html";
	}

	@RequestMapping(value = "/label/edit", method = RequestMethod.POST)
	public String editLabel(String label, String shopName, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			int userId = user.getId();
			boolean b = shopService.updateLabel(userId, label);
			if (b) {
				return "redirect:/" + shopName;
			}
		}
		return "shop/label";
	}

	@RequestMapping(value = "{shopId}/offer/add", method = RequestMethod.GET)
	public ModelAndView addOffer(@PathVariable int shopId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Shop shop = shopService.getShop(user.getShopName());
			if (shop != null && shop.getId() == shopId) {
				ModelAndView view = new ModelAndView("shop/addOffer");
				view.addObject("shop", shop);
				//List<Label> labelList = labelService.getLabelByType(LabelTypeEnum.OFFER);
				int labelLevel = 1; 
				List<Label> parentLabelList = labelService.getLabelByTypeLevel(LabelTypeEnum.OFFER, labelLevel);
				view.addObject("parentLabelList", parentLabelList);
				//view.addObject("labelList", labelList);

				return view;
			}
		}
		ModelAndView view = new ModelAndView("redirect:/index.html");
		return view;
	}
      
	@RequestMapping(value = "{shopId}/offer/add", method = RequestMethod.POST)
	public String addOffer(@PathVariable int shopId, String name, String desc, String label, String shopLabel,
			String imgUrl, String price, int quality, int type, int visible, @RequestParam("file") MultipartFile file,
			@RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2,
			@RequestParam("file3") MultipartFile file3,
			@RequestParam("file4") MultipartFile file4,
			HttpSession session) throws IOException {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		ArrayList<MultipartFile> picList = new ArrayList<MultipartFile>();
		picList.add(file);
		picList.add(file1);
		picList.add(file2);
		picList.add(file3);
		picList.add(file4);

		if (user != null) {
			// FIXME validate input parameters
			if (name == null || name.trim().length() == 0 || file == null) {
				return "redirect:/shop/" + shopId + "/offer/add.html";
			}
			Shop shop = shopService.getShop(user.getShopName());
			if (shop != null && shop.getId() == shopId) {
				Offer offer = new Offer();
				offer.setName(name);
				offer.setDesc(desc);
				offer.setLabel(label);
				offer.setShopLabel(shopLabel);
				offer.setImgUrl(imgUrl);
				offer.setPrice(price);
				offer.setQuality(quality);
				offer.setType(type);
				offer.setVisible(visible);
				offer.setShopId(shopId);
				offer = ValidateUtil.truncateOffer(offer);
				boolean b = offerService.createOffer(shop, offer);
				if (b) {
					// insert pic
					for (int i = 0; i < picList.size(); i++) {
						MultipartFile tmpFile = picList.get(i);
						String fileName = picList.get(i).getOriginalFilename();
						if (fileName.indexOf(".") != -1) {
							try {
								String imgtype = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
								String imgName = UUIDGenerator.getUUID().toLowerCase();
								OfferImage image = new OfferImage();
								// generate thumb images
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
									if(i == 0) { // the first one is the default pic
										image.setOfferLogo(1);
									};
									image.setImg(thumbImageBytes);
									image.setOfferId(offer.getId());
									image.setName(imgName);
									image.setType(imgtype);
									image.setThumbType(0);
									b = offerService.insertOfferImage(image);
									if (b) {
										if(i==0){// the first one is the default pic
											// update the offer's img url
											imgUrl = "/offerimage/show/" + imgName + "." + imgtype;
											offer = offerService.getOffer(offer.getId());
											offer.setImgUrl(imgUrl);
											offerService.updateOffer(offer);
										};
									}
				                    				                    
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
									if(i == 0) { // the first one is the default pic
										image.setOfferLogo(1);
									};
									b = offerService.insertOfferImage(image);
									if (b) {
										if(i==0){// the first one is the default pic
											// update the offer's thumb show img url
											imgUrl = "/offerimage/show/" + "thumb_show_"+ imgName + "." + imgtype;
											offer = offerService.getOffer(offer.getId());
											offer.setThumbShowImgUrl(imgUrl);
											offerService.updateOffer(offer);
										}
									}

									
									// 7. second thumb list image 145 x 145
									// scrop the image 
									thumbImage = Thumbnails.of(thumbImage)
				                    .sourceRegion(Positions.TOP_LEFT, 300,300)  
				                    .size(140, 140)   // image size
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
									if(i == 0) { // the first one is the default pic
										image.setOfferLogo(1);
									};									
									b = offerService.insertOfferImage(image);	
									if (b) {
										if(i==0){// the first one is the default pic
											// update the offer's thumb show img url
											imgUrl = "/offerimage/show/" + "thumb_list_"+ imgName + "." + imgtype;
											offer = offerService.getOffer(offer.getId());
											offer.setThumbListImgUrl(imgUrl);
											offerService.updateOffer(offer);
										}
									}
				                }


							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					return "redirect:/offer/view/" + offer.getId() + ".html";
				}
			}
		}
		return "redirect:/index.html";
	}

	@RequestMapping("useraddress")
	public String copyAddressFromUserInfo(String shopName, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Address addr = new Address();
			addr.setCountry(user.getCountry());
			addr.setCity(user.getCity());
			addr.setAddress(user.getAddress());
			addr.setZip(user.getZip());
			addr.setLongi(user.getLongi());
			addr.setLati(user.getLati());
			addr = ValidateUtil.truncateAddress(addr);
			boolean b = shopService.updateAddress(user.getId(), addr);
			if (b) {
				return "redirect:/" + shopName;
			}
		}
		return "redirect:/index.html";
	}

	@RequestMapping("weibo")
	public String publishWeibo(String shopName, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null || user.getWeiboId() == null || user.getWeiboToken() == null) {
			return "redirect:/shop/view/" + shopName + ".html";
		}
		Shop shop = shopService.getShop(shopName);
		if (shop == null) {
			return "redirect:/";
		}
		boolean b = WeiboHelper.publishShopWeibo(shop, user, "");
		if (b) {
			return "redirect:/shop/view/" + shopName + ".html?weibo=true";
		}
		return "redirect:/shop/view/" + shopName + ".html";
	}

	@RequestMapping(value = "uploadImage", method = RequestMethod.GET)
	public ModelAndView uploadImage(String shopName, String imgType, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		Shop shop = shopService.getShop(shopName);
		if (user == null || shop == null || user.getId() != shop.getUserId()) {
			return new ModelAndView("redirect:/shop/view/" + shopName + ".html");
		}
		ModelAndView view = new ModelAndView("shop/uploadShopImage");
		view.addObject("shop", shop);
		view.addObject("shopName", shopName);
		view.addObject("imgType", imgType);
		return view;
	}

	@RequestMapping(value = "uploadImage", method = RequestMethod.POST)
	public ModelAndView uploadImage(@RequestParam("file") MultipartFile file, String imgType, String shopName,
			HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		Shop shop = shopService.getShop(shopName);
		if (user == null || shop == null || user.getId() != shop.getUserId()) {
			return new ModelAndView("redirect:/shop/view/" + shopName + ".html");
		}
		if (!file.isEmpty()) {
			try {
				byte[] bt = file.getBytes();
				if (file.getSize() > 500000 || bt.length > 500000) {
					// file is too large
					ModelAndView view = new ModelAndView("shop/uploadShopImage");
					view.addObject("shopName", shopName);
					view.addObject("imgType", imgType);
					view.addObject("toolarge", true);
					return view;
				}
				String fileName = file.getOriginalFilename();
				if (fileName.indexOf(".") != -1) {
					String type = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					String imgName = UUIDGenerator.getUUID().toLowerCase();
					BlogImage image = new BlogImage();
                    
					// 1. read the file stream, and get the height and width
                    double thumbScale = 1.0;
					BufferedImage thumbImage = Thumbnails.of(file.getInputStream())
                    .scale(1f) 
                    .asBufferedImage(); 
					double imgWidth = 0;
					double imgHeight = 0;
					double ratio = 1.0;
					if ("banner".equals(imgType)) { // 
						imgWidth = 960.0;
						imgHeight = 220.0;
					} else { // logo 170x170
						imgWidth = 170.0;
						imgHeight = 170.0;
					};
					ratio = imgWidth/imgHeight;
					if(thumbImage.getHeight() > imgHeight || thumbImage.getWidth() > imgWidth){
						if(ratio*thumbImage.getHeight() > imgWidth) { // too high
                    		thumbScale = imgWidth/thumbImage.getWidth();
						} else {
                    		thumbScale = imgHeight/thumbImage.getHeight();
						}
                    } else {	
                    	thumbScale = 1;
                    }
                    
                    // 2. scale the image to max width/height 
                    thumbImage = Thumbnails.of(thumbImage)
                    .scale(thumbScale) 
                    .asBufferedImage(); 
                    
                    // 3.scrop the image to pre-defined width x height
                    thumbImage = Thumbnails.of(thumbImage)
                    .sourceRegion(Positions.TOP_LEFT, (int)imgWidth,(int)imgHeight)  
                    .size((int)imgWidth, (int)imgHeight)   // image size
                    .keepAspectRatio(false)
                    .asBufferedImage();           
                    
                    // 4. write the org image to database first
                    ByteArrayOutputStream baos = new ByteArrayOutputStream( 1000 );
                    ImageIO.write(thumbImage, type, baos );
                    baos.flush();
                    byte[] thumbImageBytes = baos.toByteArray();
                    baos.close();
					image.setImg(thumbImageBytes);
					image.setThumbType(0);				
					image.setName(imgName);
					image.setType(type);
					boolean b = blogService.insertBlogImage(image);
					if (b) {
						// update the offer's img url
						String imgUrl = "/shopimage/show/" + imgName + "." + type;
						if ("banner".equals(imgType)) {
							shop.setBannerUrl(imgUrl);
						} else {
							shop.setLogoUrl(imgUrl);
						}
						shopService.updateShop(shop);
					} else {
						throw new IOException();
					}
				} else {
					throw new IOException();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			ModelAndView view = new ModelAndView("redirect:/" + shopName);
			return view;
		} else {
			ModelAndView view = new ModelAndView("shop/uploadShopImage");
			view.addObject("shopName", shopName);
			view.addObject("imgType", imgType);
			return view;
		}
	}

	@RequestMapping(value = "/address", method = RequestMethod.GET)
	public ModelAndView address(HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Shop shop = shopService.getShop(user.getShopName());
			if (shop != null) {
				ModelAndView view = new ModelAndView("shop/address");
				view.addObject("shop", shop);
				return view;
			}
		}
		ModelAndView view = new ModelAndView("redirect:/index.html");
		return view;
	}

	@RequestMapping("modifyAddress")
	public String modifyAddress(String shopName, Address addr, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		Shop shop = shopService.getShop(shopName);
		if (user == null || shop == null || shop.getUserId() != user.getId()) {
			return "redirect:/" + shopName;
		}
		addr = ValidateUtil.truncateAddress(addr);
		boolean b = shopService.updateAddress(user.getId(), addr);

		if (b) {
			return "redirect:/" + shopName;
		}
		return "redirect:/index.html";
	}

	@RequestMapping(value = "comment/add", method = RequestMethod.POST)
	public String addComment(int shopId, String title, String content, HttpServletRequest request, HttpSession session) {
		if (content == null || content.length() == 0 || content.length() > 150) {
			String referrer = request.getHeader("referer");
			return "redirect:" + referrer;
		}
		if (title == null || title.length() == 0) {
			title = "comment title";
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user != null) {
			Shop shop = shopService.getShop(shopId);
			if (shop == null) {
				return "redirect:/";
			}
			int authorId = shop.getUserId();
			User author = userService.getUserById(authorId);
			if (author == null) {
				return "redirect:/";
			}
			ShopComment comment = new ShopComment();
			comment.setShopId(shopId);
			comment.setTitle(title);
			comment.setContent(content);
			comment.setCreatorId(user.getId());
			comment.setCreatorName(user.getUserName());
			comment = ValidateUtil.truncateComment(comment);
			boolean b = shopService.insertShopComment(comment, user, author);
			if (b) {
				return "redirect:/" + shop.getName();
			}
		}
		return "redirect:/";
	}

	@RequestMapping("comment/del")
	public String delComment(int id, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}

		Shop shop = shopService.getShopByUserId(user.getId());
		if (shop == null) {
			return "redirect:/";
		}

		ShopComment comment = shopService.getShopComment(id);
		if (comment == null || shop.getId() != comment.getShopId()) {
			return "redirect:/" + shop.getName();
		}

		boolean b = shopService.deleteShopComment(id);
		if (b) {
			return "redirect:/" + shop.getName();
		} else {
			return "redirect:/" + shop.getName();
		}
	}

	@RequestMapping("label/offer")
	public ModelAndView labelOfferList(int shopId, int labelId, Pagination page, HttpSession session) {
		Shop shop = shopService.getShop(shopId);
		if (shop == null) {
			return new ModelAndView("redirect:/");
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		boolean isOwner = false;
		if (user != null) {
			if (user.getId() == shop.getUserId()) {
				isOwner = true;
			}
		}
		SearchResult<Offer> result = offerService.getOfferByShopLabelId(shopId, labelId, !isOwner, page);
		ModelAndView view = new ModelAndView("shop/labelOfferList");
		view.addObject("shop", shop);
		view.addObject("offerList", result);
		view.addObject("labelId", labelId);
		List<Label> allLabelList = labelService.getLabel();
		view.addObject("allLabelList", allLabelList);
		return view;
	}

	@RequestMapping("label/blog")
	public ModelAndView labelBlogList(int shopId, int labelId, Pagination page) {
		Shop shop = shopService.getShop(shopId);
		if (shop == null) {
			return new ModelAndView("redirect:/");
		}
		SearchResult<Blog> result = blogService.getBlogByShopLabelId(shopId, labelId, page);
		ModelAndView view = new ModelAndView("shop/labelBlogList");
		view.addObject("shop", shop);
		view.addObject("blogList", result);
		view.addObject("labelId", labelId);
		List<Label> allLabelList = labelService.getLabel();
		view.addObject("allLabelList", allLabelList);
		return view;
	}

	@RequestMapping("shoplabel/offer")
	public ModelAndView shopLabelOfferList(int shopId, int labelId, Pagination page, HttpSession session) {
		Shop shop = shopService.getShop(shopId);
		if (shop == null) {
			return new ModelAndView("redirect:/");
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		boolean isOwner = false;
		if (user != null) {
			if (user.getId() == shop.getUserId()) {
				isOwner = true;
			}
		}

		SearchResult<Offer> result = offerService.getOfferByShopShopLabelId(shopId, labelId, !isOwner, page);
		ModelAndView view = new ModelAndView("shop/labelOfferList");
		view.addObject("shop", shop);
		view.addObject("offerList", result);
		view.addObject("labelId", labelId);
		List<Label> allLabelList = labelService.getLabel();
		view.addObject("allLabelList", allLabelList);
		return view;
	}

	@RequestMapping("shoplabel/blog")
	public ModelAndView shopLabelBlogList(int shopId, int labelId, Pagination page) {
		Shop shop = shopService.getShop(shopId);
		if (shop == null) {
			return new ModelAndView("redirect:/");
		}
		SearchResult<Blog> result = blogService.getBlogByShopShopLabelId(shopId, labelId, page);
		ModelAndView view = new ModelAndView("shop/labelBlogList");
		view.addObject("shop", shop);
		view.addObject("blogList", result);
		view.addObject("labelId", labelId);
		List<Label> allLabelList = labelService.getLabel();
		view.addObject("allLabelList", allLabelList);
		return view;
	}

	@RequestMapping("useWeiboLogo")
	public String useWeiboLogo(int shopId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		Shop shop = shopService.getShop(shopId);
		if (shop == null || shop.getUserId() != user.getId()) {
			return "redirect:/";
		}
		user = userService.getUserById(user.getId());
		if (user == null) {
			return "redirect:/";
		}
		String weiboId = user.getWeiboId();
		String token = user.getWeiboToken();
		if (weiboId == null || weiboId.length() == 0 || token == null || token.length() == 0) {
			return "redirect:https://api.weibo.com/oauth2/authorize?client_id=198216231&response_type=code&redirect_uri=http://weindex.ca/user/weibo/login/success.html";
		}
		// check token
		String queryUrl = "https://api.weibo.com/2/users/show.json?access_token=" + token + "&uid=" + weiboId;
		JSONObject resp = JsonHelper.get(queryUrl);
		boolean check = false;
		if (resp != null) {
			String name = resp.getString("screen_name");
			if (name != null) {
				check = true;
			}
		}
		if (check) {
			String profileUrl = resp.getString("profile_image_url");
			if (profileUrl != null && profileUrl.trim().length() > 0) {
				shop.setLogoUrl(profileUrl);
				boolean b = shopService.updateShop(shop);
				if (b) {
					return "redirect:/" + shop.getName();
				} else {
					return "redirect:/shop/uploadImage.html?shopName=" + shop.getName() + "&imgType=logo";
				}
			}
		} else {
			// re login
			return "redirect:https://api.weibo.com/oauth2/authorize?client_id=198216231&response_type=code&redirect_uri=http://weindex.ca/user/weibo/login/success.html";
		}

		return "redirect:/";
	}

	@RequestMapping("useFbLogo")
	public String useFbLogo(int shopId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		Shop shop = shopService.getShop(shopId);
		if (shop == null || shop.getUserId() != user.getId()) {
			return "redirect:/";
		}
		user = userService.getUserById(user.getId());
		if (user == null) {
			return "redirect:/";
		}
		String fbId = user.getFacebookId();
		if (fbId == null || fbId.length() == 0) {
			return "redirect:https://www.facebook.com/dialog/oauth?client_id=144269832392534&state=${uuid}&redirect_uri=http://weindex.ca/user/fb/login/success.html";
		}
		String profileUrl = "https://graph.facebook.com/" + fbId + "/picture?type=large";
		shop.setLogoUrl(profileUrl);
		boolean b = shopService.updateShop(shop);
		if (b) {
			return "redirect:/" + shop.getName();
		} else {
			return "redirect:/shop/uploadImage.html?shopName=" + shop.getName() + "&imgType=logo";
		}
	}

	@RequestMapping("labels")
	public ModelAndView listShopLabel(int shopId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Shop shop = shopService.getShop(shopId);
		if (shop == null || shop.getUserId() != user.getId()) {
			return new ModelAndView("redirect:/");
		}
		ModelAndView view = new ModelAndView("shop/labels");
		view.addObject("shop", shop);

		List<Label> allLabelList = labelService.getLabel();
		view.addObject("allLabelList", allLabelList);

		return view;
	}

	@RequestMapping(value = "labels/add", method = RequestMethod.GET)
	public ModelAndView addShopLabel(int shopId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return new ModelAndView("redirect:/");
		}
		Shop shop = shopService.getShop(shopId);
		if (shop == null || shop.getUserId() != user.getId()) {
			return new ModelAndView("redirect:/");
		}
		ModelAndView view = new ModelAndView("shop/addLabel");
		view.addObject("shop", shop);

		List<Label> allLabelList = labelService.getLabel();
		view.addObject("allLabelList", allLabelList);

		return view;
	}

	@RequestMapping(value = "labels/add", method = RequestMethod.POST)
	public String addShopLabel(ShopLabel label, HttpSession session) {
		if (label.getCnName() == null || label.getCnName().trim().length() == 0 || label.getEnName() == null
				|| label.getEnName().trim().length() == 0) {
			return "redirect:/shop/labels/add.html?shopId=" + label.getShopId();
		}
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		Shop shop = shopService.getShop(label.getShopId());
		if (shop == null || shop.getUserId() != user.getId()) {
			return "redirect:/";
		}
		boolean b = shopService.insertShopLabel(label);
		if (b) {
			return "redirect:/shop/labels.html?shopId=" + label.getShopId();
		} else {
			return "redirect:/shop/labels/add.html";
		}
	}

	@RequestMapping("labels/del")
	public String delShopLabel(int shopId, int labelId, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return "redirect:/";
		}
		Shop shop = shopService.getShop(shopId);
		if (shop == null || shop.getUserId() != user.getId()) {
			return "redirect:/";
		}
		ShopLabel label = shopService.getShopLabel(labelId);
		if (label == null || label.getShopId() != shopId) {
			return "redirect:/";
		}
		boolean b = shopService.deleteShopLabel(labelId);
		if (b) {
			return "redirect:/shop/labels.html?shopId=" + label.getShopId();
		} else {
			return "redirect:/shop/labels/add.html";
		}
	}

	@RequestMapping("visitlogo/{id}")
	public String addShopVisitNum(@PathVariable int id) {
		shopService.addShopVisitNum(id);
		return "json";
	}
	

	@RequestMapping(value="getsubcategorylist", method=RequestMethod.GET)
	public @ResponseBody String getSubCategoryList(HttpSession session, int parentLabelId, HttpServletResponse response) {
	    response.setContentType("text/xml;charset=UTF-8");    
	    PrintWriter out = null;    
	    try {  
	        out = response.getWriter();    
	    } catch (IOException e) {    
	        e.printStackTrace();    
	    }  
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null) {
			return ("redirect:/");
		}
		//System.out.println("parentLabelId");
		//System.out.println(parentLabelId);
									     // offer, levelId 0, parentLabelId	
		List<Label> result = labelService.getLabelByTypeLevelCategory(LabelTypeEnum.OFFER, 0, parentLabelId);		
	    JSONArray array = new JSONArray();    
	    JSONObject member = null;    
	       try {  
	        for (Label p:result) {    
	            member = new JSONObject();        
	            member.put("cnName", p.getCnName());  
	            member.put("id", p.getId());    
	            array.add(member);
	        }  
	       } catch (JSONException e) {  
	        e.printStackTrace();  
	    }  
	    out.print(array.toString());  
	    return null;
        
	}	
}
