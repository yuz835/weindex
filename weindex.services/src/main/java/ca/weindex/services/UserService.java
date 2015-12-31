package ca.weindex.services;

import java.util.List;

import ca.weindex.common.model.User;
import ca.weindex.exceptions.DuplicateUserNameException;
import ca.weindex.exceptions.LoginFailedException;

public interface UserService {
	public boolean checkUserName(String name);
	public boolean checkEmail(String email);

	public int getUserIdByName(String name);

	public User getUserByName(String name);
	public User getUserById(int id);
	
	public User signup(User user) throws DuplicateUserNameException, LoginFailedException;
	
	public User login(User user) throws LoginFailedException;
	
	public User getUserByToken(String token);
	
	public boolean logout(User user);
	
	public boolean changePassword(User user, String newPassword);
	
	public boolean updateUserInfo(User user);

	public boolean updateUserAdmin(User user);
	
	public List<User> getUserList();
	
	public int getUserFailedTimes(String userName);

	public User getUserByWeiboId(String weiboId);
	
	public User getUserByFbId(String fbId);

	public List<User> getUserWithoutWeiboId();
	
	public List<User> getUserWithoutFbId();
	
	public boolean updateUserWeiboId(User user);

	public boolean updateUserWeiboToken(User user);
	
	public boolean updateUserFbId(User user);
	
	public User fbSignup(User user) throws DuplicateUserNameException, LoginFailedException;

	public User fbLogin(User user) throws LoginFailedException;

	public User weiboSignup(User user) throws DuplicateUserNameException, LoginFailedException;

	public User weiboLogin(User user) throws LoginFailedException;

	public boolean updateLostPwdToken(User user);
	
	public User getUserByLostPwdToken(String token);
	
	public boolean updatePasswordByLostPwdToken(User user);

	public User unbindWeibo(int id);
	
	public User unbindFacebook(int id);
	
}
