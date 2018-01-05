package org.jasig.cas.zk.redis;

import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.mgt.SimpleSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class RedisShiroSessionService {
	
	@Autowired
	private RedisTemplate<String,SimpleSession> redisTemplate;
	
	private static Logger logger = LoggerFactory.getLogger(RedisShiroSessionService.class); 
	
	
	/**
	 * 设置 shirosession
	 * @author syf
	 * @created 2016年10月28日
	 * @param simpleSession
	 */
	public void setShiroSession(SimpleSession simpleSession){
		if(simpleSession != null && simpleSession.getId() != null){  
			try {
	        	redisTemplate.opsForValue().set(RedisCommonKey.getCasServerSessionKey(simpleSession.getId()+""),simpleSession,30, TimeUnit.MINUTES);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }else{
	    	logger.info("simpleSession is null");
	    }
        
	}
	
	/**
	 * 获取 shirosession
	 * @author syf
	 * @created 2016年10月28日
	 * @param sid
	 * @return
	 */
	public SimpleSession getShiroSession(Object sid){
		SimpleSession simpleSession = null;
		try {
			return simpleSession = redisTemplate.opsForValue().get(RedisCommonKey.getCasServerSessionKey(sid+""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return simpleSession;
	}
	
	/**
	 * 删除 shirosession
	 * @author syf
	 * @created 2016年10月28日
	 * @param sid
	 */
	public void delShiroSession(Object sid){
		try {
			redisTemplate.delete(RedisCommonKey.getCasServerSessionKey(sid+""));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
