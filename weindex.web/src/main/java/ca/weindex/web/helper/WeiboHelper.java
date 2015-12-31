package ca.weindex.web.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.weindex.common.enums.WeiboTaskStatusEnum;
import ca.weindex.common.enums.WeiboTaskTypeEnum;
import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Reddit;
import ca.weindex.common.model.BlogImage;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.OfferImage;
import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;
import ca.weindex.common.model.WeiboTask;
import ca.weindex.services.BlogService;
import ca.weindex.services.OfferService;
import ca.weindex.services.UserService;
import ca.weindex.services.WeiboTaskService;

import com.alibaba.fastjson.JSONObject;

@Component
public class WeiboHelper {
	@Autowired
	private WeiboTaskService service;

	@Autowired
	private UserService userService;

	private boolean inited = false;

	private static ExecutorService pool = Executors.newCachedThreadPool();

	@Autowired
	private OfferService offerService;

	@Autowired
	private BlogService blogService;

	public void init() {
		if (inited) {
			return;
		}
		// init two thread, one is use to check which shop/blog/offer publish
		// tasks are up to time, the other one is use to publish these tasks
		Thread checkTaskThread = new CheckTaskThread();
		checkTaskThread.start();
	}

	private static void startTask(Thread trd) {
		pool.execute(trd);
	}

	private class CheckTaskThread extends Thread {

		public void run() {
			while (true) {
				// check UNPUBLISHED tasks, if it is up to time, change it
				// status to NEED_TO_PUBLISH
				// System.out.println("----------------");
				try {
					WeiboTask task = service.getOneUnstartedWeiboTask();
					if (task == null || task.getStatusEnum() != WeiboTaskStatusEnum.WAITING) {
						// no task or error
						// wait one min, continue
						try {
							// wait one minute
							sleep(60 * 1000);
						} catch (InterruptedException e) {
							// do nothing
						}
						continue;
					}
					// got task
					//System.out.println(">>>>>>>>>>>>>>got task: " + task.getId() + ", time: " + task.getTaskTime());
					// publish it
					PublishThread publishTrd = new PublishThread(task);
					startTask(publishTrd);
					continue;
				} catch (Throwable t) {
					// do nothing
				}
				try {
					// wait one minute
					sleep(60 * 1000);
				} catch (InterruptedException e) {
					// do nothing
				}
			}
		}

	}

	private class PublishThread extends Thread {
		private WeiboTask task;

		public PublishThread(WeiboTask task) {
			this.task = task;
		}

		public void run() {
			if (task == null) {
				return;
			}

			// publish tasks which are NEED_TO_PUBLISH and update status to
			// PUBLISHED or PUBLISH_FAILED
			try {
				//System.out.println("===================" + task.getId());
				User user = userService.getUserById(task.getUserId());
				if (user == null) {
					task.setStatus(WeiboTaskStatusEnum.FAILED.getStatus());
					service.updateWeiboTaskStatus(task);
					return;
				}
				String weiboId = user.getWeiboId();
				String token = user.getWeiboToken();
				if (weiboId == null || weiboId.length() == 0 || token == null || token.length() == 0) {
					task.setStatus(WeiboTaskStatusEnum.FAILED.getStatus());
					service.updateWeiboTaskStatus(task);
					// TODO here we need to create a message to user
					return;
				}
				// check token
				String queryUrl = "https://api.weibo.com/2/users/show.json?access_token=" + token + "&uid=" + weiboId;
				JSONObject testObj = JsonHelper.get(queryUrl);
				boolean check = false;
				if (testObj != null) {
					String name = testObj.getString("screen_name");
					if (name != null) {
						check = true;
					}
				}
				if (!check) {
					// token is not valid
					task.setStatus(WeiboTaskStatusEnum.FAILED.getStatus());
					service.updateWeiboTaskStatus(task);
					// TODO here we need to create a message to user
					return;
				}
				if (task.getTypeEnum() == WeiboTaskTypeEnum.OFFER) {
					// offer
					Offer offer = offerService.getOffer(task.getTaskId());
					if (offer == null) {
						task.setStatus(WeiboTaskStatusEnum.FAILED.getStatus());
						service.updateWeiboTaskStatus(task);
						return;
					}
					offer.setImageList(offerService.getOfferImageByOfferId(offer.getId()));
					boolean b = publishOfferWeibo(offer, user, null);
					if (b) {
						task.setStatus(WeiboTaskStatusEnum.SUCCESSED.getStatus());
						service.updateWeiboTaskStatus(task);
						return;
					} else {
						task.setStatus(WeiboTaskStatusEnum.FAILED.getStatus());
						service.updateWeiboTaskStatus(task);
						return;
					}
				} else if (WeiboTaskTypeEnum.BLOG == task.getTypeEnum()) {
					// blog
					Blog blog = blogService.getBlog(task.getTaskId());
					if (blog == null) {
						task.setStatus(WeiboTaskStatusEnum.FAILED.getStatus());
						service.updateWeiboTaskStatus(task);
						return;
					}
					
					String blogContent = blog.getContent();
					String tag = "[img:";
					int index = blogContent.indexOf(tag);
					String imgUrl = null;
					while (index != -1) {
						int postIndex = blogContent.indexOf("]", index);
						if (postIndex != -1) {
							String pre = blogContent.substring(0, index);
							String post = blogContent.substring(postIndex + 1);
							if (imgUrl == null) {
								imgUrl = blogContent.substring(index + tag.length(), postIndex);
							}
							blogContent = pre + post;
						}
						index = blogContent.indexOf(tag, index + 1);
					}
					blog.setContent(blogContent);
					/*if (imgUrl != null && imgUrl.indexOf("/show/") != -1) {
						imgUrl = imgUrl.substring(imgUrl.indexOf("/show/") + 6);
						if (imgUrl.length() > 0 && imgUrl.indexOf(".") != -1) {
							String imgName = imgUrl.substring(0, imgUrl.lastIndexOf("."));
							String imgType = imgUrl.substring(imgUrl.lastIndexOf(".") + 1);
							BlogImage image = blogService.getBlogImage(imgName, imgType);
							if (image != null) {
								blog.setImage(image);
							}
						}
					}*/
					
					boolean b = publishBlogWeibo(blog, user, null);
					if (b) {
						task.setStatus(WeiboTaskStatusEnum.SUCCESSED.getStatus());
						service.updateWeiboTaskStatus(task);
						return;
					} else {
						task.setStatus(WeiboTaskStatusEnum.FAILED.getStatus());
						service.updateWeiboTaskStatus(task);
						return;
					}
				}
			} catch (Exception e) {
				task.setStatus(WeiboTaskStatusEnum.FAILED.getStatus());
				service.updateWeiboTaskStatus(task);
				return;
			}

		}
	}

	public static boolean publishOfferWeibo(Offer offer, User user, String content) {
		String pre = "【" + offer.getName() + ", $" + offer.getPrice() + "】 ";
		String post = "详情点击 http://weindex.ca/offer/view/" + offer.getId() + ".html ";
		int length = 165 - (pre.length() + post.length());
		String desc = offer.getDesc();
		if (desc.length() > length) {
			desc = desc.substring(0, length - 3) + "...";
		}
		String weiboContent = pre + desc + post;
		// String weiboContent = "我分享了一条来自 Weindex.ca 的商品信息: " + offer.getName()
		// + " http://weindex.ca/offer/view/"
		// + offer.getId() + ".html " + new Date();
		try {
			weiboContent = URLEncoder.encode(weiboContent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (offer.getImageList() == null || offer.getImageList().isEmpty()) {
			String url = "https://api.weibo.com/2/statuses/update.json?status=" + weiboContent + "&access_token="
					+ user.getWeiboToken();
			JSONObject json = JsonHelper.post(url);
			if (json.getString("error_code") == null) {
				return true;
			} else {
				return false;
			}
		} else {
//			String url = "https://api.weibo.com/2/statuses/upload.json?status=" + weiboContent + "&access_token="
//					+ user.getWeiboToken();
			String url = "https://api.weibo.com/2/statuses/upload.json?access_token="
					+ user.getWeiboToken();
			Map<String, String> params = new HashMap<String, String>();
			params.put("status", weiboContent);
			List<OfferImage> list = offer.getImageList();
			OfferImage image = null;
			for (OfferImage img : list) {
				if (img.getOfferLogo() > 0) {
					image = img;
					break;
				}
			}
			if (image == null) {
				image = list.get(0);
			}
			byte[] fileContent = image.getImg();
			String fileName = image.getName() + "." + image.getType();

			JSONObject json = JsonHelper.post(url, params, "pic", fileName, fileContent);
			if (json.getString("error_code") == null) {
				return true;
			} else {
				return false;
			}

		}
	}

	public static boolean publishBlogWeibo(Blog blog, User user, String content) {
		String blogContent = blog.getContent();
		String pre = "【" + blog.getTitle() + "】";
		String post = "详情点击http://weindex.ca/blog/view/" + blog.getId() + ".html";
		// zhangeth int length = 140 - (pre.length() + post.length());
		int length = 165 - (pre.length() + post.length());
		if (blogContent.length() > length) {
			blogContent = blogContent.substring(0, length - 3) + "...";
		}
		String weiboContent = pre + blogContent + post;
		// String weiboContent = "我分享了一条来自 Weindex.ca 的博文: " + blog.getTitle() +
		// " http://weindex.ca/blog/view/"
		// + blog.getId() + ".html " + new Date();
		try {
			weiboContent = URLEncoder.encode(weiboContent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (blog.getImage() == null) {
			String url = "https://api.weibo.com/2/statuses/update.json?status=" + weiboContent + "&access_token="
					+ user.getWeiboToken();
			JSONObject json = JsonHelper.post(url);
			if (json.getString("error_code") == null) {
				return true;
			} else {
				return false;
			}
		} else {
			String url = "https://api.weibo.com/2/statuses/upload.json?access_token="
					+ user.getWeiboToken();
			Map<String, String> params = new HashMap<String, String>();
			params.put("status", weiboContent);
			BlogImage image = blog.getImage();
			byte[] fileContent = image.getImg();
			String fileName = image.getName() + "." + image.getType();
			JSONObject json = JsonHelper.post(url, params, "pic", fileName, fileContent);
			if (json.getString("error_code") == null) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean publishShopWeibo(Shop shop, User user, String content) {
		String weiboContent = "我分享了一条来自 Weindex.ca 的商铺信息: " + shop.getDisplayName() + " http://weindex.ca/"
				+ shop.getName() + " " + new Date();
		try {
			weiboContent = URLEncoder.encode(weiboContent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "https://api.weibo.com/2/statuses/update.json?status=" + weiboContent + "&access_token="
				+ user.getWeiboToken();
		JSONObject json = JsonHelper.post(url);
		if (json.getString("error_code") == null) {
			return true;
		} else {
			return false;
		}
	}


	public static boolean publishRedditWeibo(Reddit reddit, User user, String content) {
	 	String shareContent = null;
		if(reddit.getType() == 0) {
			shareContent = reddit.getDescription();
		} else {
			shareContent = reddit.getContent();
		};
		String pre = "【" + reddit.getTitle() + "】";
		String post = "详情点击http://weindex.ca/link/view/" + reddit.getId() + ".html";
		int length = 165 - (pre.length() + post.length());
		if (shareContent.length() > length) {
			shareContent = shareContent.substring(0, length - 3) + "...";
		}
		String weiboContent = pre + shareContent + post;
		try {
			weiboContent = URLEncoder.encode(weiboContent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
		//if (blog.getImage() == null) {
		//	String url = "https://api.weibo.com/2/statuses/update.json?status=" + weiboContent + "&access_token="
		//			+ user.getWeiboToken();
		//	JSONObject json = JsonHelper.post(url);
		//	if (json.getString("error_code") == null) {
		//		return true;
		//	} else {
		//		return false;
		//	}
		//} else {
		//	String url = "https://api.weibo.com/2/statuses/upload.json?access_token="
		//			+ user.getWeiboToken();
		//	Map<String, String> params = new HashMap<String, String>();
		//	params.put("status", weiboContent);
		//	//BlogImage image = blog.getImage();
		//	byte[] fileContent = image.getImg();
		//	String fileName = image.getName() + "." + image.getType();
		//	JSONObject json = JsonHelper.post(url, params, "pic", fileName, fileContent);
		//	if (json.getString("error_code") == null) {
		//		return true;
		//	} else {
		//		return false;
		//	}
		//}
	}

}
