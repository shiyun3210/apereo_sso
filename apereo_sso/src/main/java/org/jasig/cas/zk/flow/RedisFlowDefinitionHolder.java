package org.jasig.cas.zk.flow;

import org.springframework.core.style.ToStringCreator;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.registry.FlowDefinitionConstructionException;
import org.springframework.webflow.definition.registry.FlowDefinitionHolder;

class RedisFlowDefinitionHolder implements FlowDefinitionHolder {
	
	/**
	 * The held flow definition.
	 */
	private final FlowDefinition flowDefinition;

	/**
	 * Creates the static flow definition holder.
	 * @param flowDefinition the flow to hold
	 */
	public RedisFlowDefinitionHolder(FlowDefinition flowDefinition) {
		this.flowDefinition = flowDefinition;
	}

	public String getFlowDefinitionId() {
		return flowDefinition.getId();
	}

	public String getFlowDefinitionResourceString() {
		return flowDefinition.getClass().getName();
	}

	public FlowDefinition getFlowDefinition() throws FlowDefinitionConstructionException {
		return flowDefinition;
	}

	public void refresh() throws FlowDefinitionConstructionException {
		// nothing to do
	}

	public void destroy() {
		flowDefinition.destroy();
	}

	public boolean equals(Object o) {
		if (!(o instanceof RedisFlowDefinitionHolder)) {
			return false;
		}
		RedisFlowDefinitionHolder other = (RedisFlowDefinitionHolder) o;
		return flowDefinition.equals(other.flowDefinition);
	}

	public int hashCode() {
		return flowDefinition.hashCode();
	}

	public String toString() {
		return new ToStringCreator(this).append("flowDefinition", flowDefinition).toString();
	}

}
