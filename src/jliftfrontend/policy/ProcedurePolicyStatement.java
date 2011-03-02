/*
 * File          : ProcedurePolicyStatement.java
 * Project       : jlift-frontend
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 11, 2008 11:33:25 AM 
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jliftfrontend.policy;

import java.util.Collection;
import java.util.List;

import jliftfrontend.policy.ProcedurePolicyElement.ProcedurePolicyElementKind;

public class ProcedurePolicyStatement extends MemberPolicyStatement {

	protected String methodName;
	protected List<ProcedurePolicyElement> policyElements;
	
	public ProcedurePolicyStatement(String methodName,
			List<ProcedurePolicyElement> policyElements) {
		this.methodName = methodName;
		this.policyElements = policyElements;
	}

	public String getMethodName() {
		return methodName;
	}

	public List<ProcedurePolicyElement> getPolicyElements() {
		return policyElements;
	}

	public Collection<PolicyDescriptor> getPolicyDescriptorsForAspect(ProcedurePolicyElementKind aspect, String optFormalName) {
		for(ProcedurePolicyElement mpe : policyElements) {
			if (mpe.getMethodAspect().equals(aspect)) {
				if (mpe.getMethodAspect().equals(ProcedurePolicyElement.FORMAL)) {
					if (mpe.getFormalName().equals(optFormalName))
						return mpe.getPolicyDescriptors();
				}
				if (mpe instanceof LocalPolicyStatement) {
					LocalPolicyStatement lps = (LocalPolicyStatement) mpe;
					if (lps.getLocalName().equals(optFormalName)) {
						return lps.getPolicyDescriptors();
					}
				}
				return mpe.getPolicyDescriptors();
			}
		}
		
		return null;
	}

	public Collection<PolicyDescriptor> getPolicyDescriptorsForAspect(
			ProcedurePolicyElementKind kind) {
		return getPolicyDescriptorsForAspect(kind, null);
	}

	public Collection<PolicyDescriptor> getPolicyDescriptorsForLocal(String name) {
		for(ProcedurePolicyElement mpe : policyElements) {
			if (mpe instanceof LocalPolicyStatement) {
				LocalPolicyStatement lps = (LocalPolicyStatement) mpe;
				if (lps.getLocalName().equals(name)) {
					return lps.getPolicyDescriptors();
				}
			}
		}
		return null;
	}
}
