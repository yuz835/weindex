package ca.weindex.web.helper;

import ca.weindex.common.model.User;

public class AdminHelper {
	private String rootUser;
	
	public boolean isAdmin(User user) {
		if (user.getUserName().equals(rootUser) || user.isAdmin()) {
			return true;
		} else {
			return false;
		}
	}

	public String getRootUser() {
		return rootUser;
	}

	public void setRootUser(String rootUser) {
		this.rootUser = rootUser;
	}

}
