package org.jasig.cas.zk.asyn;

import org.jasig.cas.zk.service.UserCenterService;

public class LastLoginTimeProcessor implements Runnable {
	
	private int userType;
	private int userId;
	private UserCenterService userCenterService;
	
	
	public LastLoginTimeProcessor(int userType,int userId,UserCenterService userCenterService) {
		this.userType = userType;
		this.userId = userId;
		this.userCenterService = userCenterService;
	}
	
	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public UserCenterService getUserCenterService() {
		return userCenterService;
	}

	public void setUserCenterService(UserCenterService userCenterService) {
		this.userCenterService = userCenterService;
	}
	
	@Override
	public void run() {
		userCenterService.updateLastLoginTime(userType, userId);
	}

}
