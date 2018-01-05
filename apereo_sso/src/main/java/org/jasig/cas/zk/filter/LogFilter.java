package org.jasig.cas.zk.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.zk.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * 拦截器 日志记录
 * @author syf
 *
 */
public class LogFilter extends HandlerInterceptorAdapter  {

	private final Logger logger = LoggerFactory.getLogger(LogFilter.class);
	private static final String USER_AGENT = "user-agent";
//	private static final String ACCESS_BEGIN_TIME = "access_begin_time";
	
//	@Autowired
//	private MongoTemplate mongoTemplate;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		String userAgent = request.getHeader(USER_AGENT);
		if (StringUtils.isNotBlank(userAgent)){	
			logger.info("user-agent:" + userAgent+" IP:"+RequestUtil.getIpAddr(request));
		}
//		request.setAttribute(ACCESS_BEGIN_TIME, System.currentTimeMillis());
		return true;
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) throws Exception{
	}

}