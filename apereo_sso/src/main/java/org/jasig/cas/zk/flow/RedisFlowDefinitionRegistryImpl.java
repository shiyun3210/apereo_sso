package org.jasig.cas.zk.flow;

import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.zk.redis.RedisFlowDefinitionHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.style.ToStringCreator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.registry.FlowDefinitionConstructionException;
import org.springframework.webflow.definition.registry.FlowDefinitionHolder;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistryImpl;
import org.springframework.webflow.definition.registry.NoSuchFlowDefinitionException;

@Component("redisFlowDefinitionRegistryImpl")
public class RedisFlowDefinitionRegistryImpl implements FlowDefinitionRegistry {

	private static final Log logger = LogFactory.getLog(FlowDefinitionRegistryImpl.class);
	
	@NotNull
	@Autowired
	@Qualifier("redisFlowDefinitionHolderService")
	private RedisFlowDefinitionHolderService redisFlowDefinitionHolderService;
	
	/**
	 * An optional parent flow definition registry.
	 */
	private FlowDefinitionRegistry parent;
	
	
	// implementing FlowDefinitionLocator

	public FlowDefinition getFlowDefinition(String id) throws NoSuchFlowDefinitionException,
			FlowDefinitionConstructionException {
		Assert.hasText(id, "An id is required to lookup a FlowDefinition");
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Getting FlowDefinition with id '" + id + "'");
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>"+id);
			return getFlowDefinitionHolder(id).getFlowDefinition();
		} catch (NoSuchFlowDefinitionException e) {
			if (parent != null) {
				// try parent
				return parent.getFlowDefinition(id);
			}
			throw e;
		}
	}


	public boolean containsFlowDefinition(String flowId) {
		return redisFlowDefinitionHolderService.getFlowDefinitionHolder(flowId)!=null?true:false;
	}

	public int getFlowDefinitionCount() {
		return redisFlowDefinitionHolderService.getFlowCount();
	}

	public String[] getFlowDefinitionIds() {
		return redisFlowDefinitionHolderService.getFlowKey();
	}

	public FlowDefinitionRegistry getParent() {
		return parent;
	}

	public void setParent(FlowDefinitionRegistry parent) {
		this.parent = parent;
	}

	public void registerFlowDefinition(FlowDefinitionHolder definitionHolder) {
		System.out.println("ffffffff");
		Assert.notNull(definitionHolder, "The holder of the flow definition to register is required");
		if (logger.isDebugEnabled()) {
			logger.debug("Registering flow definition '" + definitionHolder.getFlowDefinitionResourceString()
					+ "' under id '" + definitionHolder.getFlowDefinitionId() + "'");
		}
		redisFlowDefinitionHolderService.addFlowDefinitionHolder(definitionHolder);
	}

	public void registerFlowDefinition(FlowDefinition definition) {
		System.out.println("ffffffffffffffffffffff");
		registerFlowDefinition(new RedisFlowDefinitionHolder(definition));
	}

	public void destroy() {
		redisFlowDefinitionHolderService.destroy();
	}


	/**
	 * Returns the identified flow definition holder. Throws an exception if it cannot be found.
	 */
	private FlowDefinitionHolder getFlowDefinitionHolder(String id) throws NoSuchFlowDefinitionException {
		FlowDefinitionHolder holder = redisFlowDefinitionHolderService.getFlowDefinitionHolder(id);
		if (holder == null) {
			throw new NoSuchFlowDefinitionException(id);
		}
		return holder;
	}

	public String toString() {
		return new ToStringCreator(this).append("flowIds", getFlowDefinitionIds()).append("parent", parent).toString();
	}
	
}
