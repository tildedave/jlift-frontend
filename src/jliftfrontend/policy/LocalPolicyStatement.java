package jliftfrontend.policy;

import java.util.List;


public class LocalPolicyStatement extends ProcedurePolicyElement {

	protected String localName;

	public LocalPolicyStatement(String id, List<PolicyDescriptor> pd) {
		super(ProcedurePolicyElement.LOCAL, pd);
		this.localName = id;
	}
	
	public String getLocalName() {
		return this.localName;
	}
}
