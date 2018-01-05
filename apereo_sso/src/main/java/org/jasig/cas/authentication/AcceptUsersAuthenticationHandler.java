package org.jasig.cas.authentication;

import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.zk.enums.ClientInfoEnum;
import org.jasig.cas.zk.service.UserCenterService;
import org.jasig.cas.zk.util.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;

import java.security.GeneralSecurityException;
//import java.security.NoSuchAlgorithmException;
//import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * Handler that contains a list of valid users and passwords. Useful if there is
 * a small list of users that we wish to allow. An example use case may be if
 * there are existing handlers that make calls to LDAP, etc. but there is a need
 * for additional users we don't want in LDAP. With the chain of command
 * processing of handlers, this handler could be added to check before LDAP and
 * provide the list of additional users. The list of acceptable users is stored
 * in a map. The key of the map is the username and the password is the object
 * retrieved from doing map.get(KEY).
 * <p>
 * Note that this class makes an unmodifiable copy of whatever map is provided
 * to it.
 *
 * @author Scott Battaglia
 * @author Marvin S. Addison
 *
 * @since 3.0.0
 */
@Component("acceptUsersAuthenticationHandler")
public class AcceptUsersAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

//    /** The default separator in the file. */
//    private static final String DEFAULT_SEPARATOR = "::";
//    private static final Pattern USERS_PASSWORDS_SPLITTER_PATTERN = Pattern.compile(DEFAULT_SEPARATOR);

//    /** The list of users we will accept. */
//    private Map<String, String> users;

//    @Value("${accept.authn.users:}")
//    private String acceptedUsers;
    
    @NotNull
    @Autowired
    @Qualifier("userCenterDaoService")
    private UserCenterService userCenterService;

    /**
     * Initialize map of accepted users.
     */
//    @PostConstruct
//    public void init() {
//        if (StringUtils.isNotBlank(this.acceptedUsers) && this.users == null) {
//            final Set<String> usersPasswords = org.springframework.util.StringUtils.commaDelimitedListToSet(this.acceptedUsers);
//            final Map<String, String> parsedUsers = new HashMap<>();
//            for (final String usersPassword : usersPasswords) {
//                final String[] splitArray = USERS_PASSWORDS_SPLITTER_PATTERN.split(usersPassword);
//                parsedUsers.put(splitArray[0], splitArray[1]);
//            }
//            setUsers(parsedUsers);
//        }
//    }

    @Override
    protected final HandlerResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential credential)
            throws GeneralSecurityException, PreventedException {

//        if (users == null || users.isEmpty()) {
//            throw new FailedLoginException("No user can be accepted because none is defined");
//        }
//        final String username = credential.getUsername();
//        final String cachedPassword = this.users.get(username);
//
//        if (cachedPassword == null) {
//           logger.debug("{} was not found in the map.", username);
//           throw new AccountNotFoundException(username + " not found in backing map.");
//        }
//
//        final String encodedPassword = this.getPasswordEncoder().encode(credential.getPassword());
//        if (!cachedPassword.equals(encodedPassword)) {
//            throw new FailedLoginException();
//        }
    	String username = credential.getUsername();
    	ClientInfoEnum clientInfo = ClientInfoEnum.getInstance(credential.getClientId());
    	if(clientInfo==null){
    		logger.info("未识别的客户端ID："+credential.getClientId());
    		throw new FailedLoginException();
    	}
    	Map<String,Object> userCenterInfo = userCenterService.getUser(username,clientInfo.getType());
    	if(userCenterInfo==null||userCenterInfo.isEmpty()){
    		logger.info("不存在的账号!");
    		throw new FailedLoginException();
    	}
    	if(clientInfo.getType()==1){
    		if(userCenterInfo.get("usageStatus").toString().equals("2")){
        		logger.info("供应链平台账号被禁用!");
        		throw new FailedLoginException();
        	}
    	}else{
    		if(userCenterInfo.get("status").toString().equals("2")){
        		logger.info("跨境平台账号被禁用!");
        		throw new FailedLoginException();
        	}
    	}
    	if(!PasswordHash.validatePassword(credential.getPassword(), userCenterInfo.get("password").toString())){
    		logger.info("密码错误:"+username);
    		throw new FailedLoginException();
    	}
    	userCenterInfo.put("contactNameCn", userCenterInfo.get("contactName_cn"));
    	userCenterInfo.remove("contactName_cn");
        return createHandlerResult(credential, this.principalFactory.createPrincipal(username,userCenterInfo), null);
    }
    
//    /**
//     * @param users The users to set.
//     */
//    public final void setUsers(@NotNull final Map<String, String> users) {
//        this.users = Collections.unmodifiableMap(users);
//    }
}
