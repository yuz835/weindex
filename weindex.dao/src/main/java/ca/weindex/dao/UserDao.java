package ca.weindex.dao;

import java.util.List;

import ca.weindex.common.model.User;

public interface UserDao {
	public boolean checkUserName(String name);
	
	public boolean checkEmail(String email);

	public int getUserIdByName(String name);
	
	public User getUserByName(String name);
	
	public User getUserById(int id);
	
	public boolean signup(User user);

	public User checkUserNamePwd(User user);

	public boolean createLoginSession(User user);

	public User checkUserByToken(String token);

	public boolean removeUserSession(User user);

	public boolean updateUserPassword(User user);

	public boolean updateUserInfo(User user);

	public boolean updateUserAdmin(User user);

	public boolean updateWeiboToken(User user);

	public List<User> getUserList();
	
	public int getUserFailedTimes(String userName);
	
	public void userLoginFailed(String userName);
	
	public void userLoginSuccess(String userName);
	
	public User getUserByWeiboId(String weiboId);
	
	public User getUserByFbId(String fbId);
	
	public List<User> getUserWithoutWeiboId();
	
	public List<User> getUserWithoutFbId();
	
	public boolean updateUserWeiboId(User user);
	
	public boolean updateUserFbId(User user);
	
	public boolean updateLostPwdToken(User user);
	
	public User getUserByLostPwdToken(String token);
	
	public boolean updatePasswordByLostPwdToken(User user);
}
