package org.jasig.cas.zk.redis;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class RedisSessionDAO extends EnterpriseCacheSessionDAO {  
  
    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class); 
    
    @Autowired
    private RedisShiroSessionService redisShiroSessionService;
    
    
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        logger.info("创建shiro session>>>"+sessionId);
        redisShiroSessionService.setShiroSession((SimpleSession)session);
        return sessionId;
    }
    
    protected Session doReadSession(Serializable sessionId) {
    	if(sessionId == null){  
            logger.error("sessionId is null");  
            return null;  
        } 
    	logger.info("获取shiro session>>>"+sessionId);
    	return redisShiroSessionService.getShiroSession(sessionId);
    }

    protected void doUpdate(Session session) {
        //does nothing - parent class persists to cache.
//    	if(session == null || session.getId() == null){  
//            logger.error("session or session id is null");  
//            return;  
//        } 
//    	logger.info("更新shiro session>>>"+session.getId());
//    	redisShiroSessionService.setShiroSession((SimpleSession)session);
    	
    }

    protected void doDelete(Session session) {
    	if(session == null || session.getId() == null){  
            logger.error("session or session id is null");  
            return;  
        } 
    	logger.info("删除shiro session>>>"+session.getId());
    	redisShiroSessionService.delShiroSession(session.getId());
    }

}  