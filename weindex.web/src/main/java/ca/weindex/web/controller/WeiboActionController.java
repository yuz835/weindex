package ca.weindex.web.controller;

import java.util.ArrayList;
import java.util.List;

import ca.weindex.common.model.SearchResult;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;


import ca.weindex.weibo4j.Timeline;
import ca.weindex.weibo4j.Friendships;
import ca.weindex.weibo4j.model.CommentWapper;
import ca.weindex.weibo4j.model.WeiboException;
import ca.weindex.weibo4j.org.json.JSONArray;
import ca.weindex.weibo4j.org.json.JSONException;

import ca.weindex.common.model.Pagination;
// import ca.weindex.common.model.User;
import ca.weindex.common.model.WeiboAction;
import ca.weindex.weibo4j.model.User;
import ca.weindex.weibo4j.model.UserWapper;
import ca.weindex.common.model.WeiboUserFollowed;
import ca.weindex.common.model.WeiboUser;
import ca.weindex.services.UserService;
import ca.weindex.services.WeiboActionService;


@Controller
@RequestMapping("/robot")
public class WeiboActionController {
	@Autowired
	private UserService userService;

	@Autowired
	private WeiboActionService weiboActionService;

	@RequestMapping
	public ModelAndView index(HttpSession session) {
		ModelAndView view = new ModelAndView("robot/index");
		return view;
	}

	
	// 机器人：CaptureRobot
	// 工作：抓取微博大号的粉丝，并且写入数据库
	static boolean caputureRobot = false;
	@RequestMapping(value = "/start_caputure_robot", method = RequestMethod.GET)
	public String startCaptureRobot() {
		if(caputureRobot == false) {
			//Thread captureThread0 = new CaptureThread("3212076985");// weindex
			Thread captureThread1 = new CaptureThread("3082655323");// jiaguotoutiao
			//Thread captureThread2 = new CaptureThread("3034128985");// jingshangzhidao
			
			//captureThread0.start();
			captureThread1.start();
			//captureThread2.start();

			caputureRobot = true;
		};
		System.out.println("Started caputureRobot...");
		return "redirect:/index.html";
	};

	void assignWeiboUser(User weiboUserInterface, WeiboUser weiboUser) {

	      weiboUser.setVerified_reason(weiboUserInterface.getVerified_reason());
	      weiboUser.setWeiboUserId(weiboUserInterface.getId());
	      weiboUser.setScreenName(weiboUserInterface.getScreenName()) ;
	      weiboUser.setName(weiboUserInterface.getName()) ;
	      weiboUser.setProvince(weiboUserInterface.getProvince()) ;
	      weiboUser.setCity(weiboUserInterface.getCity()) ;
	      weiboUser.setLocation(weiboUserInterface.getLocation()) ;
	      weiboUser.setDescription(weiboUserInterface.getDescription()) ;
	      weiboUser.setUrl(weiboUserInterface.getUrl()) ;
	      weiboUser.setProfileImageUrl(weiboUserInterface.getProfileImageUrl()) ;
	      weiboUser.setUserDomain(weiboUserInterface.getUserDomain()) ;
	      weiboUser.setGender(weiboUserInterface.getGender()) ;
	      weiboUser.setFollowersCount(weiboUserInterface.getFollowersCount()) ;
	      weiboUser.setFriendsCount(weiboUserInterface.getFriendsCount()) ;
	      weiboUser.setStatusesCount(weiboUserInterface.getStatusesCount()) ;
	      weiboUser.setFavouritesCount(weiboUserInterface.getFavouritesCount()) ;
	      weiboUser.setCreatedAt(weiboUserInterface.getCreatedAt()) ;
	      //weiboUser.setFollowing(weiboUserInterface.getFollowing()) ;
	      //weiboUser.setVerified(weiboUserInterface.getVerified()) ;
	      weiboUser.setVerifiedType(weiboUserInterface.getVerifiedType()) ;
	      //weiboUser.setAllowAllActMsg(weiboUserInterface.getAllowAllActMsg()) ;
	      //weiboUser.setAllowAllComment(weiboUserInterface.getAllowAllComment()) ;
	      //weiboUser.setFollowMe(weiboUserInterface.getFollowMe()) ;
	      weiboUser.setAvatarLarge(weiboUserInterface.getAvatarLarge()) ;
	      weiboUser.setOnlineStatus(weiboUserInterface.getOnlineStatus()) ;
	      weiboUser.setBiFollowersCount(weiboUserInterface.getBiFollowersCount()) ;
	      weiboUser.setRemark(weiboUserInterface.getRemark()) ;
	      weiboUser.setLang(weiboUserInterface.getLang()) ;
	      weiboUser.setVerifiedReason(weiboUserInterface.getVerifiedReason()) ;
	      weiboUser.setStatusId(weiboUserInterface.getStatusId()) ;

	};
	
	private class CaptureThread extends Thread {
		//String weiboUserId = "3212076985"; // Default微博：weindex 的微博号
		String weiboUserId;
		public CaptureThread(String weiboUserId) {
			this.weiboUserId = weiboUserId;
		};
		
	 	public void run() {

			Friendships fs = new Friendships();
			ca.weindex.common.model.User user = userService.getUserByWeiboId(weiboUserId);
		    String access_token = user.getWeiboToken();  
			Integer count = 200;
			Integer cursor = 0;
			Pagination page = new Pagination();
			page.setPageSize(100);
			int pageNum = 0;
			while(true) {
				page.setBeginIndex(pageNum);
				page.setEndIndex(pageNum+1);
				SearchResult<WeiboUser> sr = null;
				sr = weiboActionService.getWeiboUser(page); // weiboUser from Canada
				pageNum =+1;	
				
				for (WeiboUser wu: sr.getList()) {
					String weiboUserId = null;
					weiboUserId = wu.getWeiboUserId();
					cursor = 0;

					while (true) {
						fs.setToken(access_token);
						try {
							UserWapper wr = fs.getFollowersById(weiboUserId, count, cursor);
							for (int i=0; i< wr.getUsers().size();  i++) {
								Boolean in = weiboActionService.checkWeiboUserByWeiboUserId(wr.getUsers().get(i).getId());
								if(in == false) {
									WeiboUser weiboUserToInsert = new WeiboUser();
									assignWeiboUser(wr.getUsers().get(i), weiboUserToInsert);
									if((weiboUserToInsert.getCity() == 5 //|| //加拿大
											//weiboUserToInsert.getCity()  == 1 //美国
										)&& weiboUserToInsert.getProvince() == 400
									 ) {
										weiboActionService.insertWeiboUser(weiboUserToInsert);
									};
								};
							}
							
							cursor = (int)wr.getNextCursor();
						
							// 最后一次 因为当最后一次执行完毕后，users.getNextCursor()=0
							if(cursor == 0) {
								//System.out.println("caputureRobot Done for: ");
								//System.out.println(screenName);
								wu.setBiFollowersCount(-1); // set as processed
								// bifollowers is borrowed used as a flag
								weiboActionService.updateWeiboUser(wu);
								
								break;
							};
						} catch (WeiboException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					

						// 8秒钟查一次
						try {
							sleep(4 * 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			}
		}	
	}
	
	// 机器人：ClassifyRobot
	// 工作：自动转发分类信息, 并且给一些回复
	static boolean classifyRobot = false;
	@RequestMapping(value = "/start_classify_robot", method = RequestMethod.GET)
	public String startClassifyRobot() {
		if(classifyRobot == false) {
			// 采集@温哥华-经商助手的微博
			Thread classifyCollect0Thread = new ClassifyCollectThread("3087093415");
			// 采集@温哥华-经商知道的微博
			Thread classifyCollect1Thread = new ClassifyCollectThread("3034128985");
			// 温哥华-经商助手 去 转发
			Thread classifyAction0Thread = new ClassifyAction0Thread("3087093415", false);
			//Thread classifyAction1Thread = new ClassifyAction1Thread();

			classifyCollect0Thread.start();
			classifyCollect1Thread.start();
			classifyAction0Thread.start();
			//classifyAction1Thread.start();
			classifyRobot = true;
		};
		System.out.println("Started classifyRobot...");
		return "redirect:/index.html";
	};
	
	private class ClassifyCollectThread extends Thread {
		// 温哥华-经商助手微博ID 3087093415
		//String weiboUserId = "3087093415";
		String weiboUserId;
		public ClassifyCollectThread(String weiboUserId) {
			this.weiboUserId = weiboUserId;
		};
	    	String[] responseContentList = {
	    	"帮忙转发...",
	    	"帮转...",
	    	"转发转发...",
	    	"[ok]",
	    	"[呵呵]",
	    	"[哈哈]",
	    	"[话筒]", 
	    	"需要转发，请@温哥华-经商助手 或者 @温哥华-经商知道",
	    	"帮忙转发，请大家记得在原微博@温哥华-经商助手",
	    	"帮忙转发，分类信息请@温哥华-经商助手, 加拿大资讯请关注@温哥华-经商知道",
	    	"帮忙转发，分类信息请@温哥华-经商助手, 加拿大资讯请关注@温哥华-经商知道"
	    	};
	 	public void run() {
	 		while(true) {
				ca.weindex.common.model.User user = userService.getUserByWeiboId(weiboUserId);
		    	String access_token = user.getWeiboToken();  

	 			// 采集原微博at我的微博列表
	 			Timeline tm = new Timeline();
	 			tm.client.setToken(access_token);
	 			ca.weindex.weibo4j.model.Paging paging = new ca.weindex.weibo4j.model.Paging (1, 20);
				ca.weindex.weibo4j.org.json.JSONObject mentionIds = new ca.weindex.weibo4j.org.json.JSONObject();
				JSONArray jsonarray = new JSONArray();
				try {
					mentionIds = tm.getMentionsIds(paging, 0, 0, 1);
			        	jsonarray = (JSONArray) mentionIds.get("statuses"); 
					for (int i = 0; i < jsonarray.length(); i++) {
						WeiboAction wa = new WeiboAction();
						wa.setWeiboUserId(weiboUserId);
						try {
							wa.setWeiboId(jsonarray.getString(i));
						} catch (JSONException e) { }
						wa.setActionType(0); //@我的微博
						Boolean in = weiboActionService.checkWeiboAction(wa);
						if(in == false) {
							int r = (int)(Math.random()*(responseContentList.length-1)); 
							wa.setResponseContent(responseContentList[r]);
							weiboActionService.insertWeiboAction(wa);
						}
					};
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// 1分钟采集一次
				try {
					sleep(60 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// 采集微博评论中at我的微博
				/*ca.weindex.weibo4j.Comments cm = new ca.weindex.weibo4j.Comments();
				cm.client.setToken(access_token);
				try {
					CommentWapper cw = cm.getCommentMentions(paging, 0, 0);
					for (int i = 0; i < cw.getComments().size(); i++) {
						WeiboAction wa = new WeiboAction();
						wa.setWeiboUserId(weiboUserId);
						wa.setWeiboId(cw.getComments().get(i).getStatus().getId());
						wa.setWeiboCommentId(cw.getComments().get(i).getIdstr());
						wa.setActionType(1); //评论中@我的微博
						Boolean in = weiboActionService.checkWeiboAction(wa);
						if(in == false) {
							wa.setResponseContent("亲，需要转发，记住在原微博@ 本博 哦");
							weiboActionService.insertWeiboAction(wa);
						}
					};
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
	 		}
		}	
	}


	//处理at我的微博
	private class ClassifyAction0Thread extends Thread {
		// 温哥华-经商助手微博ID 3087093415
		String weiboUserId;
		Boolean allWeiboUser = false;
		public ClassifyAction0Thread(String weiboUserId, Boolean allWeiboUser) {
			this.weiboUserId = weiboUserId;
			this.allWeiboUser = allWeiboUser;
		};
	 	public void run() {
	 		while(true) {
	 			ca.weindex.common.model.User user = userService.getUserByWeiboId(weiboUserId);
		    	String access_token = user.getWeiboToken();  

	 			// 采集原微博at我的微博列表
	 			Timeline tm = new Timeline();
	 			tm.client.setToken(access_token);
				WeiboAction wa = new WeiboAction();
				wa.setActionType(0);
				if(allWeiboUser == true) {
					wa.setWeiboUserId(weiboUserId);
	 			} else {
					wa.setWeiboUserId("ALL");
	 			}
	 			List<WeiboAction> wl = weiboActionService.getNewActionList(wa);
	 			for (int i = 0; i < wl.size(); i++) {
					try {
						tm.Repost(wl.get(i).getWeiboId(), wl.get(i).getResponseContent(), 3);
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 更新数据库为完成
					weiboActionService.setActionComplete(wl.get(i).getId());
					// 随机产生时间延时 2到22分钟
					int r = 2 + (int)(Math.random()*20); 
					try {
						sleep(r*60 * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
				
				// 1分钟delay
				try {
					sleep(60 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 		}
		}	
	}
	
	//处理评论中at我的评论
	private class ClassifyAction1Thread extends Thread {
		// 温哥华-经商助手微博ID 3087093415
		String weiboUserId;
		public ClassifyAction1Thread(String weiboUserId) {
			this.weiboUserId = weiboUserId;
		};
	 	public void run() {
	 		while(true) {
	 			ca.weindex.common.model.User user = userService.getUserByWeiboId(weiboUserId);
		    	String access_token = user.getWeiboToken();  

	 			// 采集评论中at我的评论列表
				ca.weindex.weibo4j.Comments cm = new ca.weindex.weibo4j.Comments();				
				cm.client.setToken(access_token);
				WeiboAction wa = new WeiboAction();
				wa.setActionType(1);
				wa.setWeiboUserId(weiboUserId);
	 			List<WeiboAction> wl = weiboActionService.getNewActionList(wa);
	 			for (int i = 0; i < wl.size(); i++) {
	 				try {
						cm.replyComment(wl.get(i).getWeiboCommentId(), wl.get(i).getWeiboId(), wl.get(i).getResponseContent(), 0, 1);
					} catch (WeiboException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//cm.replyComment(
					//		wl.get(i).getWeiboCommentId(), //评论ID
					//		wl.get(i).getWeiboId(), //微博ID
					//		wl.get(i).getResponseContent(), //回复内容	
					//		0,	//at用户名
					//		1	//回复给原微博
					//		);
					
					// 更新数据库为完成
					weiboActionService.setActionComplete(wl.get(i).getId());
					// 随机产生时间延时 5到30分钟
					//int r = 5 + (int)(Math.random()*25); 
					int r = 1;
					try {
						sleep(r*60 * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
				
				// 1分钟delay
				try {
					sleep(60 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 		}
		}	
	}

	
	static boolean friendshipRobot = false;
	@RequestMapping(value = "/start_friendship_robot", method = RequestMethod.GET)
	public String startFriendshipRobot() {
		if(friendshipRobot == false) {
			String[] keyWordList0= {"店", "铺", "奢", "买", "卖", "闲置", "移民", "留学", "律师", "购", "达人", "正品", "美", "shop", "顾问", "中介", "房", "车", "衣", "搬", "旅馆", "住宿", "接送", "旅游", "机票", "税", "保健", "补习", "宠物", "摄影", "保险"};
			Thread friendshipThread0 = new FriendshipThread("3087093415", "温哥华-经商助手", 1190, keyWordList0);
			friendshipThread0.start();
		};
		System.out.println("Started friendRobot...");
		return "redirect:/index.html";
	};

	
	
	//自动关注机器人
	// weiboUserId: 微博账号
	
	private class FriendshipThread extends Thread {
		String weiboUserId;
		String screenName;
		String[] keyWordList; 
		int	maxCount = 1350; //最多关注数目
		int maxIndex = 169507; //数据库里面抓取的微博用户总数
		public FriendshipThread(String weiboUserId, String screenName, int maxCount, String[] keyWordList) {
			this.weiboUserId = weiboUserId;
			this.screenName = screenName;
			this.maxCount = maxCount;
			this.keyWordList = keyWordList;
		};
		
	 	public void run() {
			Friendships fs = new Friendships();	
			ca.weindex.weibo4j.Users weiboUserShow = new ca.weindex.weibo4j.Users();

			ca.weindex.common.model.User user = userService.getUserByWeiboId(weiboUserId);
		    String access_token = user.getWeiboToken();  

	 		// set token
			fs.client.setToken(access_token);
			weiboUserShow.client.setToken(access_token);

			
	 		// check if it's been added in database, otherwise, created it
	 		Boolean in = weiboActionService.checkWeiboUserFollowedByWeiboUserId(weiboUserId);
	 		if(in == false) {
	 			WeiboUserFollowed newWuf = new WeiboUserFollowed();
	 			newWuf.setWeiboUserId(weiboUserId);
	 			newWuf.setScreenName(screenName);
	 			weiboActionService.insertWeiboUserFollowed(newWuf);
	 		};
	 			 		
	 		
	 		// 一些loop变量
	 		int firstIndex = 0; // first followed ID index in weibo_user table
	 		int lastIndex  = 0; // last followed ID index in weibo_user table
	 		String destroyWeiboId = ""; // 要取消关注的微博用户ID
	 		String createWeiboId = "";  // 要关注的微博用户ID
	 		Boolean overFlow = false;   // flag，当关注达到maxCount的时候，设置为真 
	 		int friendsCount = 0;		//already friended count
	 		// check the current friendsCount number by WEIBO api usershow 
	 		try {
				ca.weindex.weibo4j.model.User weiboUser = weiboUserShow.showUserById(weiboUserId);
				friendsCount = weiboUser.getFriendsCount();
				if(friendsCount > maxCount) {
					overFlow = true;
				};
			} catch (WeiboException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	 		
	 		while(true) {
		 		WeiboUserFollowed wuf = weiboActionService.getWeiboUserFollowedByWeiboUserId(weiboUserId);
		 		firstIndex = wuf.getFirstFollowedIndex();
		 		lastIndex  = wuf.getLatestFollowedIndex();
	 	 		if(firstIndex > maxIndex) {break;}
 	 			if(lastIndex > maxIndex) {break;}

		 		// 如果关注数目大于maxCount， 则要定时取消关注
		 		if(overFlow == false) {
		 			if(friendsCount > maxCount) {
		 				overFlow = true;
		 			}
		 		}
		 		
		 		///---------------取消关注-------
		 		// 
 				//一天最多200个关注，7.2分钟关注一次
 				//
		 		//
		 		if(overFlow) {
 					// 取消关注动作
					Boolean matched = false; //找到了要取消关注的微博id
 					while(matched == false){
 						WeiboUser wu = weiboActionService.getWeiboUserById(firstIndex);
			 	 		firstIndex += 1; 
			 	 		
			 	 		if(firstIndex > maxIndex) {break;}
			 	 		
 						if(wu != null) {
 							String currScreenName = wu.getScreenName();
 							for (String o : keyWordList) {
 								if(currScreenName.indexOf(o) != -1) {
 		 							destroyWeiboId = wu.getWeiboUserId(); // 取消关注
 		 							try {
 		 								fs.destroyFriendshipsDestroyById(destroyWeiboId);
 		 							} catch (WeiboException e) {
 		 								// TODO Auto-generated catch block
 		 								e.printStackTrace();
 		 							}
 		 							friendsCount -= 1;
 		 		 	 				wuf.setFirstFollowedIndex(firstIndex);
 		 		 	 	 			weiboActionService.updateWeiboUserFollowed(wuf);
 		 		 	 	 			matched = true;
 		 		 	 	 			break;
 								}
 							}
 						}
 					}
 					
 					//等一等时间due to rate limit 3.6 min
 					try {
						sleep(36*6 * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 				}
		 		
		 		
		 		
		 		///---------------关注-------
		 		//
 	 			// update the index
				//
		 		Boolean found = false; //找到了要取消关注的微博id
				while(found == false){
	 	 			lastIndex += 1;

	 	 			if(lastIndex > maxIndex) {break;}

	 	 			WeiboUser wu = weiboActionService.getWeiboUserById(lastIndex);
					if(wu != null) {
						String currScreenName = wu.getScreenName();
						for (String o : keyWordList) {
							if(currScreenName.indexOf(o) != -1) {
								createWeiboId = wu.getWeiboUserId(); // 关注
			 	 				try {
									fs.createFriendshipsById(createWeiboId);
								} catch (WeiboException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		 						friendsCount += 1;
		 		 	 			wuf.setLatestFollowedIndex(lastIndex);
		 		 	 	 		weiboActionService.updateWeiboUserFollowed(wuf);
		 		 	 	 		found = true;
		 		 	 	 		break;
							}
						}
					}
				}

 	 			
 	 			// add delay
 				if(overFlow) {
 					try {
						sleep(36*6 * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 			} else{
 					try {
						sleep(72*6 * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 			}

 				
	 		}	
	 	}
	}
	
	
	// these are for fetching twitters
	static boolean twitterRobotRunning = false;
	@RequestMapping(value = "/starttwitterrobot", method = RequestMethod.GET)
	public String startTwitterRobot(HttpSession session) {
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
