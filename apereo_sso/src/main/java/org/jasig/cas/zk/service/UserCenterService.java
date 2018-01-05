package org.jasig.cas.zk.service;

import java.util.Map;

public interface UserCenterService {
	public Map<String,Object> getUser(String username,int userType);
	/**
	 * 最后登录时间
	 * @param userType
	 * @param userId
	 */
	public void updateLastLoginTime(int userType,int userId);
}
