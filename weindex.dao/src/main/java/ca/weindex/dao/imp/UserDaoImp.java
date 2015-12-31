package ca.weindex.dao.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.weindex.common.model.User;
import ca.weindex.common.model.UserSession;
import ca.weindex.dao.UserDao;

public class UserDaoImp extends SqlMapClientDaoSupport implements UserDao {

	public boolean checkUserName(String name) {
		Integer i = (Integer) getSqlMapClientTemplate().queryForObject("checkUserName", name);
		if (i != null && i > 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean checkEmail(String email) {
		Integer i = (Integer) getSqlMapClientTemplate().queryForObject("checkEmail", email);
		if (i != null && i > 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public int getUserIdByName(String name) {
		Integer i = (Integer) getSqlMapClientTemplate().queryForObject("getUserIdByName", name);
		if (i != null && i > 0) {
			return i;
		} else {
			return -1;
		}
	}
	
	public boolean signup(User user) throws DataAccessException {
		int i = getSqlMapClientTemplate().update("insertUser", user);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public User checkUserNamePwd(User user) {
		User result = (User) getSqlMapClientTemplate().queryForObject("getUserByNamePwd", user);
		if (result != null && result.getUserName().equals(user.getUserName())) {
			return result;
		}
		return null;
	}

	public boolean createLoginSession(User user) {
		UserSession session = new UserSession();
		session.setUserId(user.getId());
		session.setToken(user.getToken());
		session.setCreateTime(System.currentTimeMillis());
		session.setExpiryTime(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 365)); // one
																							// year
		int i = getSqlMapClientTemplate().update("insertUserSession", session);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public User checkUserByToken(String token) {
		UserSession session = new UserSession();
		session.setToken(token);
		session.setExpiryTime(System.currentTimeMillis());
		User result = (User) getSqlMapClientTemplate().queryForObject("checkLoginByToken", session);
		return result;
	}

	public boolean removeUserSession(User user) {
		UserSession session = new UserSession();
		session.setUserId(user.getId());
		session.setToken(user.getToken());
		getSqlMapClientTemplate().delete("removeUserSession", session);
		return false;
	}

	public boolean updateUserPassword(User user) {
		int i = getSqlMapClientTemplate().update("updateUserPassword", user);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateUserInfo(User user) {
		int i = getSqlMapClientTemplate().update("updateUser", user);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public List<User> getUserList() {
		List<?> list = getSqlMapClientTemplate().queryForList("getUserList");
		if (list != null) {
			List<User> result = new ArrayList<User>();
			for (Object o : list) {
				result.add((User) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public boolean updateUserAdmin(User user) {
		int i = getSqlMapClientTemplate().update("updateUserAdmin", user);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public User getUserById(int id) {
		User result = (User) getSqlMapClientTemplate().queryForObject("getUserById", id);
		if (result != null) {
			return result;
		}
		return null;
	}

	public User getUserByName(String name) {
		User result = (User) getSqlMapClientTemplate().queryForObject("getUserByName", name);
		if (result != null) {
			return result;
		}
		return null;
	}

	public int getUserFailedTimes(String userName) {
		Integer i = (Integer) getSqlMapClientTemplate().queryForObject("getUserLoginFailedTimes", userName);
		if (i != null) {
			return i;
		}
		return 0;
	}

	public void userLoginFailed(String userName) {
		getSqlMapClientTemplate().update("userLoginFailed", userName);
	}

	public void userLoginSuccess(String userName) {
		getSqlMapClientTemplate().update("userLoginSuccess", userName);
	}

	public User getUserByWeiboId(String weiboId) {
		User user = (User) getSqlMapClientTemplate().queryForObject("getUserByWeiboId", weiboId);
		return user;
	}

	public User getUserByFbId(String fbId) {
		User user = (User) getSqlMapClientTemplate().queryForObject("getUserByFbId", fbId);
		return user;
	}

	public List<User> getUserWithoutWeiboId() {
		List<?> list = getSqlMapClientTemplate().queryForList("getUserWithoutWeiboId");
		if (list != null) {
			List<User> result = new ArrayList<User>();
			for (Object o : list) {
				result.add((User) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public List<User> getUserWithoutFbId() {
		List<?> list = getSqlMapClientTemplate().queryForList("getUserWithoutFbId");
		if (list != null) {
			List<User> result = new ArrayList<User>();
			for (Object o : list) {
				result.add((User) o);
			}
			return result;
		}
		return Collections.emptyList();
	}

	public boolean updateUserWeiboId(User user) {
		int i = getSqlMapClientTemplate().update("updateUserWeiboId", user);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateWeiboToken(User user) {
		int i = getSqlMapClientTemplate().update("updateWeiboToken", user);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateUserFbId(User user) {
		int i = getSqlMapClientTemplate().update("updateUserFbId", user);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateLostPwdToken(User user) {
		int i = getSqlMapClientTemplate().update("updateLostPwdToken", user);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	public User getUserByLostPwdToken(String token) {
		if (token == null || token.trim().length() == 0) {
			return null;
		}
		User query = new User();
		query.setLostPwdToken(token);
		query.setLostPwdTokenExpiry(System.currentTimeMillis());
		User user = (User) getSqlMapClientTemplate().queryForObject("getUserByLostPwdToken", query);
		return user;
	}

	public boolean updatePasswordByLostPwdToken(User user) {
		user.setLostPwdTokenExpiry(System.currentTimeMillis());
		int i = getSqlMapClientTemplate().update("updatePasswordByLostPwdToken", user);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}
}
