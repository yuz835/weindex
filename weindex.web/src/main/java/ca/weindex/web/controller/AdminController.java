package ca.weindex.web.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;


import ca.weindex.weibo4j.Timeline;
import ca.weindex.weibo4j.model.Status;
import ca.weindex.weibo4j.model.StatusWapper;
import ca.weindex.weibo4j.model.WeiboException;
import ca.weindex.weibo4j.org.json.JSONArray;
import ca.weindex.weibo4j.org.json.JSONException;

import ca.weindex.common.constants.SessionConstants;
import ca.weindex.common.model.HomepageOffer;
import ca.weindex.common.model.Label;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.OfferImage;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;
import ca.weindex.services.AdminService;
import ca.weindex.services.HomePageService;
import ca.weindex.services.LabelService;
import ca.weindex.services.OfferService;
import ca.weindex.services.ShopService;
import ca.weindex.services.UserService;
import ca.weindex.services.BlogService;

import ca.weindex.web.helper.AdminHelper;
import ca.weindex.web.helper.IndexBuildHelper;
import ca.weindex.common.enums.LabelTypeEnum;
import ca.weindex.common.model.BlogImage;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserService userService;

	@Autowired
	private OfferService offerService;

	@Autowired
	private ShopService shopService;

	@Autowired
	private HomePageService homePageService;
	
	@Autowired
	private BlogService blogService;

	@Autowired
	private LabelService labelService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private IndexBuildHelper indexBuildHelper;
		
	@RequestMapping
	public ModelAndView index(HttpSession session) {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		List<User> list = userService.getUserList();
		ModelAndView view = new ModelAndView("admin/index");
		view.addObject("list", list);
		return view;
	}

	@RequestMapping("admin/set")
	public ModelAndView setAdmin(int id, HttpSession session) {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		User user = new User();
		user.setId(id);
		user.setAdmin(true);
		boolean b = userService.updateUserAdmin(user);
		if (b) {
			return new ModelAndView("redirect:/admin.html");
		} else {
			return new ModelAndView("redirect:/index.html");
		}
	}

	@RequestMapping("admin/remove")
	public ModelAndView removeAdmin(int id, HttpSession session) {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		User user = new User();
		user.setId(id);
		user.setAdmin(false);
		boolean b = userService.updateUserAdmin(user);
		if (b) {
			if (id == ((User) session.getAttribute(SessionConstants.SESSION_ATTR_USER)).getId()) {
				// current user, please login again
				return new ModelAndView("redirect:/user/logout.html");
			}
			return new ModelAndView("redirect:/admin.html");
		} else {
			return new ModelAndView("redirect:/index.html");
		}
	}

	@RequestMapping("offer/search")
	public ModelAndView searchOffer(String keyword, HttpSession session) {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		List<Offer> list = offerService.searchOffer(keyword, new Pagination()).getList();
		ModelAndView view = new ModelAndView("admin/searchOfferList");
		view.addObject("list", list);
		return view;
	}

	@RequestMapping("offer/list")
	public ModelAndView listOffer(HttpSession session) {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		List<HomepageOffer> list = homePageService.getHomepageOfferList();
		ModelAndView view = new ModelAndView("admin/offerList");
		view.addObject("list", list);
		return view;
	}

	@RequestMapping("offer/add")
	public ModelAndView addOffer(int id, HttpSession session) {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		HomepageOffer offer = new HomepageOffer();
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		offer.setOfferId(id);
		offer.setCreatorId(user.getId());
		offer.setCreatorName(user.getUserName());
		boolean b = homePageService.insertHomepageOffer(offer);
		if (b) {
			return new ModelAndView("redirect:/admin/offer/list.html");
		} else {
			return new ModelAndView("redirect:/index.html");
		}
	}

	@RequestMapping("offer/delete")
	public ModelAndView deleteOffer(int id, HttpSession session) {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		boolean b = homePageService.deleteHomepageOffer(id);
		if (b) {
			return new ModelAndView("redirect:/admin/offer/list.html");
		} else {
			return new ModelAndView("redirect:/index.html");
		}
	}

	@RequestMapping("label")
	public ModelAndView label(HttpSession session) {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		//List<Label> list = labelService.getLabel();
		List<Label> generalLabelList = labelService.getLabelByType(LabelTypeEnum.GENERAL);
		List<Label> shopLabelList = labelService.getLabelByType(LabelTypeEnum.SHOP);
		List<Label> offerLabelList = labelService.getLabelByType(LabelTypeEnum.OFFER);
		List<Label> blogLabelList = labelService.getLabelByType(LabelTypeEnum.BLOG);
		
		ModelAndView view = new ModelAndView("admin/labelList");
		view.addObject("generalLabelList", generalLabelList);
		view.addObject("shopLabelList", shopLabelList);
		view.addObject("offerLabelList", offerLabelList);
		view.addObject("blogLabelList", blogLabelList);

		return view;
	}

	@RequestMapping(value = "/label/add", method = RequestMethod.GET)
	public String addLabel(HttpSession session) {
		if (!checkAdmin(session)) {
			return "redirect:/index.html";
		}
		return "admin/addLabel";
	}

	@RequestMapping(value = "/label/add", method = RequestMethod.POST)
	public String addLabel(Label label, HttpSession session) {
		if (!checkAdmin(session)) {
			return "redirect:/index.html";
		}
		if (label.getCnName() == null || label.getEnName() == null) {
			return "redirect:/admin/label/add.html";
		}
		label.setVisible(false);
		boolean b = labelService.insertLabel(label);
		if (b) {
			return "redirect:/admin/label.html";
		} else {
			return "redirect:/admin/label/add.html";
		}
	}

	@RequestMapping(value = "/label/edit", method = RequestMethod.GET)
	public ModelAndView editLabel(int type, HttpSession session) {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		ModelAndView view = new ModelAndView("/admin/editLabel");
		LabelTypeEnum tmpType;
		if(type == 0) {
			tmpType = LabelTypeEnum.GENERAL;
		} else if(type == 1) {
			tmpType = LabelTypeEnum.SHOP;

		} else if(type == 2) {
			tmpType = LabelTypeEnum.OFFER;

		} else if (type == 3) {
			tmpType = LabelTypeEnum.BLOG;
		} else {
			return new ModelAndView("redirect:/index.html");
		}
		 List<Label> labelList = labelService.getLabelByType(tmpType);
		 view.addObject("labelList", labelList);
		 return view;
	}

	@RequestMapping(value = "/label/edit", method = RequestMethod.POST)
	public String editLabel(int type, Label label, HttpSession session) {
		if (!checkAdmin(session)) {
			return "redirect:/index.html";
		}
		labelService.updateLabel(label);
		return "redirect:/admin/label/edit.html?type="+type;
	}

	@RequestMapping("/label/delete")
	public String deleteLabel(int id, HttpSession session) {
		if (!checkAdmin(session)) {
			return "redirect:/index.html";
		}
		boolean b = labelService.deleteLabel(id);
		if (b) {
			return "redirect:/admin/label.html";
		} else {
			return "redirect:/index.html";
		}
	}

	@RequestMapping("/label/{id}/visible")
	public String visibleLabel(@PathVariable int id, HttpSession session) {
		if (!checkAdmin(session)) {
			return "redirect:/index.html";
		}
		boolean b = labelService.setLabelVisible(id);
		if (b) {
			return "redirect:/admin/label.html";
		} else {
			return "redirect:/index.html";
		}
	}

	@RequestMapping("/label/{id}/unvisible")
	public String unvisibleLabel(@PathVariable int id, HttpSession session) {
		if (!checkAdmin(session)) {
			return "redirect:/index.html";
		}
		boolean b = labelService.setLabelUnvisible(id);
		if (b) {
			return "redirect:/admin/label.html";
		} else {
			return "redirect:/index.html";
		}
	}

	@RequestMapping(value="/label/{id}/pos", method=RequestMethod.GET)
	public ModelAndView posLabel(@PathVariable int id, HttpSession session) {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		Label label = labelService.getLabelById(id);
		if (label == null) {
			return new ModelAndView("redirect:/admin/label.html");
		}
		ModelAndView view = new ModelAndView("admin/labelPos");
		view.addObject("label", label);
		return view;
	}

	@RequestMapping(value="/label/{id}/pos", method=RequestMethod.POST)
	public ModelAndView posLabel(@PathVariable int id, int pos, HttpSession session) {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		boolean b = labelService.updateLabelPos(id, pos);
		if (b) {
			return new ModelAndView("redirect:/admin/label.html");
		}
		return new ModelAndView("redirect:/index.html");
	}

	private boolean checkAdmin(HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.SESSION_ATTR_USER);
		if (user == null || !adminHelper.isAdmin(user)) {
			return false;
		} else {
			return true;
		}
	}

	@RequestMapping("verify/shop")
	public String verifyShop(int shopId, HttpSession session) {
		if (checkAdmin(session)) {
			Shop shop = shopService.getShop(shopId);
			if (shop != null) {
				boolean b = adminService.verifyShop(shopId);
				if (b) {
					return "redirect:/shop/view/" + shop.getName() + ".html";
				}
			}
		}
		return "redirect:/";
	}
	
	@RequestMapping("unverify/shop")
	public String unverifyShop(int shopId, HttpSession session) {
		if (checkAdmin(session)) {
			Shop shop = shopService.getShop(shopId);
			if (shop != null) {
				boolean b = adminService.unverifyShop(shopId);
				if (b) {
					return "redirect:/shop/view/" + shop.getName() + ".html";
				}
			}
		}
		return "redirect:/";
	}
	
	
	@RequestMapping("buildIndex")
	public String buildIndex(HttpSession session) {
		if (!checkAdmin(session)) {
			return "redirect:/";
		}
		indexBuildHelper.buildIndex();
		return "redirect:/";
	}
	// generate banner images and shop logo
	@RequestMapping(value = "/genshopthumbs", method = RequestMethod.GET)
	public ModelAndView genshopthumbs(HttpSession session) throws IOException {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		int startShopId = 0;
		int endShopId = 100;
		// generate shop banner / logo thumb 
		for (int i=startShopId; i<=endShopId; i++) {
	    	System.out.println("Debug-2...");

			Shop currentShop = shopService.getShop(i);
	    	System.out.println("Debug-1...");

			if(currentShop != null) {
		    	System.out.println("Debug-12...");
		    	
		    	String bannerUrl = currentShop.getBannerUrl();
		    	String bannerImageName = null;
		    	String bannerImageType = null;
		    	BlogImage bannerImage = new BlogImage();
		    	if(bannerUrl != null) {
		    		 bannerImageName = bannerUrl.substring(16, bannerUrl.lastIndexOf("."));
		    		 bannerImageType = bannerUrl.substring(bannerUrl.lastIndexOf(".")+1);
		 			 bannerImage =  blogService.getBlogImage(bannerImageName, bannerImageType);
		    	};

	    	System.out.println("Debug-13...");
		    	String logoUrl = currentShop.getLogoUrl();
		    	String logoImageName = null;
		    	String logoImageType = null;
		    	BlogImage logoImage = new BlogImage();
		    	if(logoUrl != null) {
		    		logoImageName = logoUrl.substring(16, logoUrl.lastIndexOf("."));
		    		logoImageType = logoUrl.substring(logoUrl.lastIndexOf(".")+1);
					logoImage = blogService.getBlogImage(logoImageName, logoImageType);
		    	};
		    	System.out.println("bannerImageName is" + bannerImageName);
		    	System.out.println("bannerImageType is" + bannerImageType);
		    	
		    	System.out.println("logoImageName is" + logoImageName);
		    	System.out.println("logoImageType is" + logoImageType);
		    	System.out.println("current shopId is: " + i);

			// 1. read the file stream, and get the height and width
            double thumbScale = 1.0;
	    	System.out.println("Debug0...");
		
            for(int j=0; j<=1; j++) {
	            	String type;
	            	BlogImage image = new BlogImage();
	            	InputStream orgImage;
	            	if (j==0) { // 
	            		 if(bannerUrl == null) {
	            			 continue;
	            		 };
	            		 orgImage = new ByteArrayInputStream(bannerImage.getImg());
	            	} else {
	            		if(logoUrl == null || 
	            				(logoUrl.lastIndexOf("facebook") != -1) ||
	            				(logoUrl.lastIndexOf("sina") != -1) // ppl might use facebook or weibo header
	            				) {
	            			continue;
	            		}
	            		 orgImage = new ByteArrayInputStream(logoImage.getImg());
	            	};
	            	BufferedImage thumbImage = Thumbnails.of(orgImage)
					.scale(1f) 
					.asBufferedImage(); 
	            	double imgWidth = 0;
	            	double imgHeight = 0;
	            	double ratio = 1.0;
	            	if (j==0) { // 
	            		type = bannerImageType;
	            		image = bannerImage; 
						imgWidth = 960.0;
						imgHeight = 220.0;
					} else { // logo 170x170
						type = logoImageType;
						image = logoImage; 
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
			    	System.out.println("Debug1...");
     
			    	// 2. scale the image to max width/height 
			    	thumbImage = Thumbnails.of(thumbImage)
			    	.scale(thumbScale) 
			    	.asBufferedImage(); 
			    	System.out.println("Debug2...");

			    	// 3.scrop the image to pre-defined width x height
			    	thumbImage = Thumbnails.of(thumbImage)
			    	.sourceRegion(Positions.TOP_LEFT, (int)imgWidth,(int)imgHeight)  
			    	.size((int)imgWidth, (int)imgHeight)   // image size
			    	.keepAspectRatio(false)
			    	.asBufferedImage();           
			    	System.out.println("Debug3...");

			    	// 4. update the org image to database first
			    	ByteArrayOutputStream baos = new ByteArrayOutputStream( 1000 );
			    	ImageIO.write(thumbImage, type, baos );
			    	baos.flush();
			    	byte[] thumbImageBytes = baos.toByteArray();
			    	baos.close();
					image.setImg(thumbImageBytes);
			    	System.out.println("Debug4...");

					blogService.updateBlogImage(image);
			    	System.out.println("Debug5...");

			};
		};
		};
		return new ModelAndView("redirect:/index.html");
	};
	
	
	/// generate the thums for the products
	@RequestMapping(value = "/genofferthumbs", method = RequestMethod.GET)
	public ModelAndView genofferthumbs(HttpSession session) throws IOException {
		if (!checkAdmin(session)) {
			return new ModelAndView("redirect:/index.html");
		}
		
		int startOfferId = 0;
		int endOfferId = 300;
		// generate shop banner / logo thumb 
		for (int i=startOfferId; i<=endOfferId; i++) {
	    	System.out.println("current offer is: " + i);
			Offer currentOffer = offerService.getOffer(i);
	    	List <OfferImage> offerImageList  = offerService.getOfferImageByOfferId(i);
	    	// first generate all the thumbs
			for (OfferImage image : offerImageList) {
				if(image.getThumbType() == 0) {
			    	System.out.println("dealing org image");
					// regenerate the org image
		        	InputStream orgImage;
		   		    orgImage = new ByteArrayInputStream(image.getImg());
					// thumb 1
					double thumbScale = 1.0;
					// 1. read the file stream, and get the height and width
					BufferedImage thumbImage = Thumbnails.of(orgImage)
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
					ImageIO.write(thumbImage, image.getType(), baos );
					baos.flush();
					byte[] thumbImageBytes = baos.toByteArray();
					baos.close();
					image.setImg(thumbImageBytes);
					image.setThumbType(0);
					offerService.updateOfferImage(image);
					
					// 4.scrop the image to 300x300
					String imageName = image.getName();
					// generate thumb show img
					String thumbShowImageName = "thumb_show_"+imageName;
					if(offerService.getOfferImage(thumbShowImageName, image.getType()) == null) {
				    	System.out.println("dealing thumb show image");
						thumbImage = Thumbnails.of(thumbImage)
						.sourceRegion(Positions.TOP_LEFT, 600,600)  
						.size(300, 300)   // image size
						.keepAspectRatio(false)
						.asBufferedImage(); 
						
						// 5. conver bufferedImage to byet[] list
						baos = new ByteArrayOutputStream( 1000 );
						ImageIO.write(thumbImage, image.getType(), baos );
						baos.flush();
						thumbImageBytes = baos.toByteArray();
						baos.close();
						
						// 6. write the thumb show image to DATABASE
						OfferImage thumbShowImage = new OfferImage();
						thumbShowImage.setOfferId(image.getOfferId());
						thumbShowImage.setName("thumb_show_"+ image.getName());
						thumbShowImage.setType(image.getType());
						thumbShowImage.setImg(thumbImageBytes);
						thumbShowImage.setThumbType(1);
						offerService.insertOfferImage(thumbShowImage);	
						if(image.getOfferLogo() > 0) {
							String imgUrl = "/offerimage/show/" + "thumb_show_"+ image.getName() + "." + image.getType();
							currentOffer.setThumbShowImgUrl(imgUrl);
							offerService.updateOffer(currentOffer);
						}
					};
					
					// 7. second thumb list image 145 x 145
					// scrop the image 
					// generate thumb list img
					String thumbListImageName = "thumb_list_"+imageName;
					if(offerService.getOfferImage(thumbListImageName, image.getType()) == null) {	  
				    	System.out.println("dealing thumb list image");
						thumbImage = Thumbnails.of(thumbImage)
						.sourceRegion(Positions.TOP_LEFT, 230,230)  
						.size(145, 145)   // image size
						.keepAspectRatio(false)
						.asBufferedImage(); 
						    		
						// 8. conver bufferedImage to byet[] list
						baos = new ByteArrayOutputStream( 1000 );
						ImageIO.write(thumbImage, image.getType(), baos );
						baos.flush();
						thumbImageBytes = baos.toByteArray();
						baos.close();
						    		
						// 9. write the thumb show image to DATABASE
						OfferImage thumbListImage = new OfferImage();
						thumbListImage.setOfferId(image.getOfferId());
						thumbListImage.setName("thumb_list_"+ image.getName());
						thumbListImage.setType(image.getType());thumbListImage.setImg(thumbImageBytes);
						thumbListImage.setThumbType(2);
						offerService.insertOfferImage(thumbListImage);	
						// update thumb url
						if(image.getOfferLogo() > 0) {
							String imgUrl = "/offerimage/show/" + "thumb_list_"+ image.getName() + "." + image.getType();
							currentOffer.setThumbListImgUrl(imgUrl);
							offerService.updateOffer(currentOffer);
						};
					};
				};
			};
		};
		return new ModelAndView("redirect:/index.html");
	};	
	

	// fetch the @ me weibo, and re-weibo them
	static boolean weiboRobotRunning = false;
	@RequestMapping(value = "/startweiborobot", method = RequestMethod.GET)
	public String startWeiboRobot(HttpSession session) {
		if (!checkAdmin(session)) {
			return "redirect:/index.html";
		}
		if(weiboRobotRunning == false) {
			Thread checkWeiboThread = new CheckWeiboThread();
			checkWeiboThread.start();
			weiboRobotRunning = true;
		};
		System.out.println("Start weibo h0h0...");
		return "redirect:/index.html";
	};
	
	
	private class CheckWeiboThread extends Thread {
		//温哥华-经商助手 微博 ID 3087093415
		User user = userService.getUserByWeiboId("3087093415");
	    String access_token = user.getWeiboToken();  
	    
		List<String> weiboIdList = new ArrayList<String>();
		String status = "帮忙转发; 分类信息需要转发, 请@温哥华-经商助手; 加拿大资讯请关注（@温哥华-经商知道）";
		public void run() {
			while (true) {
				int weibo_sent = 0;
				System.out.println("Start weibo xexe...");					
				Timeline tm = new Timeline();
		        JSONArray jsonarray;
				tm.client.setToken(access_token);
				//
				/*ca.weindex.weibo4j.Users um = new ca.weindex.weibo4j.Users();
				um.client.setToken(access_token);
				try {
					ca.weindex.weibo4j.model.User weiboUser = um.showUserById("3087093415");
					System.out.println("debug0");
					System.out.println(weiboUser);
					System.out.println("debug1");

				} catch (WeiboException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 */
				try {
					ca.weindex.weibo4j.model.Paging paging = new ca.weindex.weibo4j.model.Paging (1, 50);
					ca.weindex.weibo4j.org.json.JSONObject mentionIds = tm.getMentionsIds(paging, 0, 0, 1);
					//System.out.println(mentionIds);
			        jsonarray = (JSONArray) mentionIds.get("statuses"); 
					for (int i = 0; i < jsonarray.length(); i++) {
					    if(weiboIdList.contains(jsonarray.getString(i)) == false && weiboIdList.size() < 80) {
					    	weibo_sent += 1;
					    	weiboIdList.add(jsonarray.getString(i));
							tm.Repost(jsonarray.getString(i), status, 3);
							sleep(60 * 20 * 1000);
					    } else {
					    	if(weiboIdList.size() == 80) { 
					    		weiboIdList.remove(0);
					    	};
					    };
					}
					if(weibo_sent < 6) {
						sleep(600 * 1000);
					};
				} catch (WeiboException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					sleep(20 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}	
	}

	
	// these are for fetching twitters
	static boolean twitterRobotRunning = false;
	@RequestMapping(value = "/starttwitterrobot", method = RequestMethod.GET)
	public String startTwitterRobot(HttpSession session) {
		if (!checkAdmin(session)) {
			return "redirect:/index.html";
		}
		// init two thread, one is use to check which shop/blog/offer publish
		// tasks are up to time, the other one is use to publish these tasks
		if(twitterRobotRunning == false) {
			Thread checkTwittersThread = new CheckTwittersThread();
			checkTwittersThread.start();
			twitterRobotRunning = true;
		};
		System.out.println("Start twitter haha...");
		return "redirect:/index.html";
	}

	
	private class CheckTwittersThread extends Thread {
		List<String> tweetIdList = new ArrayList<String>();
		ConfigurationBuilder builder = new ConfigurationBuilder();

		// GET THE CONSUMER KEY AND SECRET KEY FROM THE STRINGS XML
		String TWITTER_CONSUMER_KEY = "l1mdBspHx1Fv4S572ZTvJw";
		String TWITTER_CONSUMER_SECRET = "GJtTA02h0waySD7e8P4RNba4R0OcM7n25QUqmkGn4tU";

		// TWITTER ACCESS TOKEN
		String twit_access_token = "1538103096-Kt91eziuDL4my3UJ4rXTsLLPUCceA5Mke57u5wu";

		// TWITTER ACCESS TOKEN SECRET
		String twit_access_token_secret = "vJB4TLf1ANKukp2JCFydoEhe24TDG8LK5ULkMgsAcQ";
		
		public void run() {
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			builder.setOAuthAccessToken(twit_access_token);
			builder.setOAuthAccessTokenSecret(twit_access_token_secret);
			
			AccessToken accessToken = new AccessToken(twit_access_token, twit_access_token_secret);
			Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

			while (true) {
				int tweets_sent = 0;
				try {
					//sleep(120 * 1000);
					// twitter.setOAuthConsumer(consumerKey, consumerSecret);
					Paging paging = new Paging(1, 10);
					List<twitter4j.Status> statuses = new ArrayList<twitter4j.Status>();
					statuses = twitter.getHomeTimeline(paging);
					// NOW LOOP THROUGH THE statuses AND FETCH INDIVIDUAL DETAILS
					for (int i = 0; i < statuses.size(); i++) {
					    String strTweetID = String.valueOf(statuses.get(i).getId());
					    if(tweetIdList.contains(strTweetID) == false && tweetIdList.size() < 50) {
					    	tweets_sent += 1;
					    	System.out.println(statuses.get(i).getUser().getScreenName()  + ": " + statuses.get(i).getText());
					    	tweetIdList.add(strTweetID);
							sleep(20 * 1000);
					    } else {
					    	if(tweetIdList.size() == 50) { 
					    		tweetIdList.remove(0);
					    	};
					    };
					}
					if(tweets_sent < 3) {
						sleep(80 * 1000);
					};
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}	
	}
}
