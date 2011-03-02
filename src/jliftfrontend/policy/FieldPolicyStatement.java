/*
 * File          : FieldPolicyStatement.java
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

import java.util.List;

import polyglot.ast.Node;

public class FieldPolicyStatement extends MemberPolicyStatement {

	protected String fieldName;
	protected List<PolicyDescriptor> policyElements;

	public FieldPolicyStatement(String fieldName,
			List<PolicyDescriptor> policyElements) {
		super();
		this.fieldName = fieldName;
		this.policyElements = policyElements;
	}

	public String getFieldName() {
		return fieldName;
	}

	public List<PolicyDescriptor> getPolicyDescriptors() {
		return policyElements;
	}
}
