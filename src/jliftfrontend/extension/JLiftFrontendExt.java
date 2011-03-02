/*
 * File          : JLiftFrontendExt.java
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
package jliftfrontend.extension;

import java.util.Collection;

import jliftfrontend.policy.JLiftPolicy;
import jliftfrontend.policy.PolicyDescriptor;
import polyglot.ast.Ext;
import polyglot.ast.Node;
import polyglot.types.Context;
import polyglot.types.SemanticException;

public interface JLiftFrontendExt {
	Node processPolicy(JLiftPolicy policy, Context context) throws SemanticException;
	Ext policyDescriptors(Collection<PolicyDescriptor> policyDescriptors) throws SemanticException;
	Collection<PolicyDescriptor> getPolicyDescriptors();
}
