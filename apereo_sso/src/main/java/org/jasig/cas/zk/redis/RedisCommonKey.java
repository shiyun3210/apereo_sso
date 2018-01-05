package org.jasig.cas.zk.redis;

public class RedisCommonKey {
	
	private static final String FLAG = "@";
	
	private static final String SERVICE_TICKET_REGISTRY = "service_ticket_registry_@";
	
	private static final String LOGIN_TICKET_REGISTRY = "login_ticket_registry_@";
	
	private static final String TGT_TICKET_REGISTRY = "tgt_ticket_registry_@";
	
	private static final String FLOW_DEFINITION_REGISTRY = "flow_definition_registry_@";
	
	private static final String CAS_SERVER_HTTPSESSION = "cas_server_session_@";
	
	private static final String CAS_CONVERSATION_KEY = "cas_conversation_@";
	
	public static final String getServiceTicketKey(String key){
		return SERVICE_TICKET_REGISTRY.replaceFirst(FLAG, key);
	}
	
	public static final String getLoginTicketKey(String key){
		return LOGIN_TICKET_REGISTRY.replaceFirst(FLAG, key);
	}
	
	public static final String getTgtTicketKey(String key){
		return TGT_TICKET_REGISTRY.replaceFirst(FLAG, key);
	}
	
	public static final String getFLowRegistryKey(String key){
		return FLOW_DEFINITION_REGISTRY.replaceFirst(FLAG, key);
	}
	
	public static final String getCasServerSessionKey(String key){
		return CAS_SERVER_HTTPSESSION.replaceFirst(FLAG, key);
	}
	
	public static final String getCasConversationKey(String key){
		return CAS_CONVERSATION_KEY.replaceFirst(FLAG, key);
	}
}
