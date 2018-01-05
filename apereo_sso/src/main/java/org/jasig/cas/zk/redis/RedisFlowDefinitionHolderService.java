package org.jasig.cas.zk.redis;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.webflow.definition.registry.FlowDefinitionHolder;


@Component("redisFlowDefinitionHolderService")
public class RedisFlowDefinitionHolderService {
	
	@Autowired
	private RedisTemplate<String, FlowDefinitionHolder> redisTemplate;
	
	
	public void addFlowDefinitionHolder(FlowDefinitionHolder flowDefinitionHolder){
		if(flowDefinitionHolder==null||flowDefinitionHolder.getFlowDefinitionId()==null){
			throw new NullPointerException("flowDefinitionHolder");
		}
		try {
			redisTemplate.opsForValue().set(RedisCommonKey.getFLowRegistryKey(flowDefinitionHolder.getFlowDefinitionId()), flowDefinitionHolder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public FlowDefinitionHolder getFlowDefinitionHolder(String flowId){
		if(StringUtils.isNotBlank(flowId)){
			try {
				return redisTemplate.opsForValue().get(RedisCommonKey.getFLowRegistryKey(flowId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void delFlowDefinitionHolder(String flowId){
		if(StringUtils.isNotBlank(flowId)){
			try {
				redisTemplate.delete(RedisCommonKey.getFLowRegistryKey(flowId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getFlowCount(){
		try {
			return redisTemplate.keys(RedisCommonKey.getFLowRegistryKey("")).size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public String[] getFlowKey(){
		try {
			Set<String> setKeys = redisTemplate.keys(RedisCommonKey.getFLowRegistryKey(""));
			String[] keys = new String[setKeys.size()];
			int index = 0;
			for (String key : setKeys) {
				key = key.replaceFirst(RedisCommonKey.getFLowRegistryKey(""), "");
				keys[index] = key;
				index++;
			}
			return keys;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[0];
	}
	
	public void destroy(){
		try {
			redisTemplate.delete(redisTemplate.keys(RedisCommonKey.getFLowRegistryKey("")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
