/*
 * File          : PrincipalPolicyStatement.java
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

import polyglot.ast.Node;

public class PrincipalPolicyStatement extends PolicyStatement {
	
	protected String principalName;
	
	public PrincipalPolicyStatement(String principalName) {
		this.principalName = principalName;
	}

	public String getPrincipalName() {
		return principalName;
	}
}
