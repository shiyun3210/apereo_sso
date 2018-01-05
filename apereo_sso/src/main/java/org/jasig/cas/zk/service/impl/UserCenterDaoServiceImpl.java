package org.jasig.cas.zk.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;

import org.jasig.cas.zk.service.UserCenterService;
import org.jasig.cas.zk.util.DbTools;
import org.springframework.stereotype.Service;

@Service("userCenterDaoService")
public class UserCenterDaoServiceImpl implements UserCenterService {
	
	public Map<String,Object> getUser(String username,int userType){
		String sql = "";
		if(userType==1){
			sql = "select gu.userId,gu.parentId,gu.companyId,gu.mobilePhone,gu.password,gu.lastLoginDate,gu.contactName_cn,gu.usageStatus,gc.auditStatus "
					+ "from gy_user gu left join gy_company gc on gu.companyId=gc.id where gu.mobilePhone=?";
		}else{
			sql = "select userId,email,password,`status`,contact from kj_user where email=?";
		}
		try {
			return DbTools.getHashValue(sql, DbTools.makeParams(username));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 最后登录时间
	 * @param userType
	 * @param userId
	 */
	public void updateLastLoginTime(int userType,int userId){
		String sql = "";
		if(userType==1){
			sql = "update gy_user set lastLoginDate=now() where userId=?";
		}else{
			
			sql = "update kj_user set lastLoginDate=now() where userId=?";
		}
		try {
			DbTools.executeUpdate(sql,DbTools.makeParams(userId));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
