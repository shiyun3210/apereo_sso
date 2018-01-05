package org.jasig.cas.zk.client;

import javax.validation.constraints.NotNull;

import org.jasig.cas.zk.service.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component("userCenterClient")
public class UserCenterClient {
	
	@NotNull
	@Autowired
	@Qualifier("userCenterDaoService")
	private UserCenterService userCenterService;
	
	
	
}
