package ca.weindex.services.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.weindex.common.model.Shop;
import ca.weindex.common.model.User;
import ca.weindex.common.util.UUIDGenerator;
import ca.weindex.dao.ShopDao;
import ca.weindex.dao.UserDao;
import ca.weindex.exceptions.DuplicateUserNameException;
import ca.weindex.exceptions.LoginFailedException;
import ca.weindex.services.UserService;

@Service
public class UserServiceImp implements UserService {
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ShopDao shopDao;
	
	public boolean checkUserName(String name) {
		return userDao.checkUserName(name);
	}
	
	public boolean checkEmail(String email) {
		return userDao.checkEmail(email);
	}
	
	public int getUserIdByName(String name) {
		return userDao.getUserIdByName(name);
	}
	
	public User signup(User user) throws DuplicateUserNameException, LoginFailedException {
		if (!checkUserName(user.getUserName())) {
			throw new DuplicateUserNameException();
		}
		boolean b = userDao.signup(user);
		if (b) {
			// create user successfully
			// then login user
			return login(user);
		}
		return null;
	}

	public User login(User user) throws LoginFailedException {
		User result = userDao.checkUserNamePwd(user);
		if (result != null) {
			// user exist
			// reset the failed times
			userDao.userLoginSuccess(user.getUserName());
			// get an UUID
			String uuid = UUIDGenerator.getUUID();
			
			// write it into user_session table
			result.setToken(uuid);
			boolean b = userDao.createLoginSession(result);
			if (b) {
				Shop shop = shopDao.getShopByUserId(result.getId());
				if (shop != null) {
					result.setWithShop(true);
					result.setShopName(shop.getName());
				} else {
					result.setWithShop(false);
					result.setShopName(null);
				}
				return result;
			}
		} else {
			// add failed times
			userDao.userLoginFailed(user.getUserName());
		}
		return null;
	}

	public User getUserByToken(String token) {
		User result = userDao.checkUserByToken(token);
		if (result != null) {
			Shop shop = shopDao.getShopByUserId(result.getId());
			if (shop != null) {
				result.setWithShop(true);
				result.setShopName(shop.getName());
			} else {
				result.setWithShop(false);
				result.setShopName(null);
			}
		}
		return result;
	}

	public boolean logout(User user) {
		userDao.removeUserSession(user);
		return true;
	}

	public boolean changePassword(User user, String newPassword) {
		User result = userDao.checkUserNamePwd(user);
		if (result == null) {
			return false;
		}
		// the old password is correct
		result.setPassword(newPassword);
		boolean b = userDao.updateUserPassword(result);
		return b;
	}

	public boolean updateUserInfo(User user) {
		return userDao.updateUserInfo(user);
	}

	public boolean updateUserAdmin(User user) {
		return userDao.updateUserAdmin(user);
	}

	public List<User> getUserList() {
		return userDao.getUserList();
	}

	public User getUserByName(String name) {
		return userDao.getUserByName(name);
	}

	public User getUserById(int id) {
		return userDao.getUserById(id);
	}

	public int getUserFailedTimes(String userName) {
		return userDao.getUserFailedTimes(userName);
	}

	public User getUserByWeiboId(String weiboId) {
		return userDao.getUserByWeiboId(weiboId);
	}

	public User getUserByFbId(String fbId) {
		return userDao.getUserByFbId(fbId);
	}

	public List<User> getUserWithoutWeiboId() {
		return userDao.getUserWithoutWeiboId();
	}

	public List<User> getUserWithoutFbId() {
		return userDao.getUserWithoutFbId();
	}

	public boolean updateUserWeiboId(User user) {
		return userDao.updateUserWeiboId(user);
	}

	public boolean updateUserFbId(User user) {
		return userDao.updateUserFbId(user);
	}

	public User fbSignup(User user) throws DuplicateUserNameException, LoginFailedException {
		boolean b = userDao.signup(user);
		if (b) {
			// create user successfully
			// then login user
			return fbLogin(user);
		}
		return null;
	}

	public User fbLogin(User user) throws LoginFailedException {
		User result = userDao.getUserByFbId(user.getFacebookId());
		if (result != null) {
			// user exist
			// reset the failed times
			userDao.userLoginSuccess(user.getUserName());
			// get an UUID
			String uuid = UUIDGenerator.getUUID();
			
			// write it into user_session table
			result.setToken(uuid);
			boolean b = userDao.createLoginSession(result);
			if (b) {
				Shop shop = shopDao.getShopByUserId(result.getId());
				if (shop != null) {
					result.setWithShop(true);
					result.setShopName(shop.getName());
				} else {
					result.setWithShop(false);
					result.setShopName(null);
				}
				return result;
			}
		} else {
			// add failed times
			userDao.userLoginFailed(user.getUserName());
		}
		return null;
	}

	public User weiboSignup(User user) throws DuplicateUserNameException, LoginFailedException {
		boolean b = userDao.signup(user);
		if (b) {
			// create user successfully
			// then login user
			return weiboLogin(user);
		}
		return null;
	}

	public User weiboLogin(User user) throws LoginFailedException {
		User result = userDao.getUserByWeiboId(user.getWeiboId());
		if (result != null) {
			// user exist
			// reset the failed times
			userDao.userLoginSuccess(user.getUserName());
			// get an UUID
			String uuid = UUIDGenerator.getUUID();
			
			// write it into user_session table
			result.setToken(uuid);
			boolean b = userDao.createLoginSession(result);
			if (b) {
				Shop shop = shopDao.getShopByUserId(result.getId());
				if (shop != null) {
					result.setWithShop(true);
					result.setShopName(shop.getName());
				} else {
					result.setWithShop(false);
					result.setShopName(null);
				}
				return result;
			}
		} else {
			// add failed times
			userDao.userLoginFailed(user.getUserName());
		}
		return null;
	}

	public boolean updateUserWeiboToken(User user) {
		return userDao.updateWeiboToken(user);
	}

	public boolean updateLostPwdToken(User user) {
		// six hours
		long expiry = ((long) 1000) * 60 * 60 * 6 + System.currentTimeMillis();
		user.setLostPwdTokenExpiry(expiry);
		return userDao.updateLostPwdToken(user);
	}

	public User getUserByLostPwdToken(String token) {
		if (token == null) {
			return null;
		}
		return userDao.getUserByLostPwdToken(token);
	}

	public boolean updatePasswordByLostPwdToken(User user) {
		return userDao.updatePasswordByLostPwdToken(user);
	}

	public User unbindWeibo(int id) {
		User user = getUserById(id);
		if (user != null) {
			user.setWeiboId(null);
			user.setWeiboToken(null);
			boolean b = userDao.updateUserWeiboId(user);
			if (b) {
				b = userDao.updateWeiboToken(user);
			}
			if (b) {
				Shop shop = shopDao.getShopByUserId(user.getId());
				if (shop != null) {
					user.setWithShop(true);
					user.setShopName(shop.getName());
				} else {
					user.setWithShop(false);
					user.setShopName(null);
				}
				return user;
			}
		}
		return null;
	}

	public User unbindFacebook(int id) {
		User user = getUserById(id);
		if (user != null) {
			user.setFacebookId(null);
			boolean b = userDao.updateUserFbId(user);
			if (b) {
				Shop shop = shopDao.getShopByUserId(user.getId());
				if (shop != null) {
					user.setWithShop(true);
					user.setShopName(shop.getName());
				} else {
					user.setWithShop(false);
					user.setShopName(null);
				}
				return user;
			}
		}
		return null;
	}

	
	
}
