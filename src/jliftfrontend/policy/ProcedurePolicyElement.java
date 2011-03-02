/*
 * File          : ProcedurePolicyElement.java
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

import polyglot.ast.Node;

public class ProcedurePolicyElement {

	public static final class ProcedurePolicyElementKind {
		protected String kindName;
		
		private ProcedurePolicyElementKind(String kindName) {
			this.kindName = kindName;
		}
	}

	public static final ProcedurePolicyElementKind LOCAL = new ProcedurePolicyElementKind("local");
	public static final ProcedurePolicyElementKind BEGIN = new ProcedurePolicyElementKind("begin"); 
	public static final ProcedurePolicyElementKind RETURN = new ProcedurePolicyElementKind("return"); 
	public static final ProcedurePolicyElementKind RETURNVALUE = new ProcedurePolicyElementKind("returnvalue"); 
	public static final ProcedurePolicyElementKind FORMAL = new ProcedurePolicyElementKind("formal"); 
	
	protected ProcedurePolicyElementKind methodAspect;
	protected String formalName;
	protected Collection<PolicyDescriptor> policyDescriptors;

	public ProcedurePolicyElement(ProcedurePolicyElementKind kind, String formalName, Collection<PolicyDescriptor> policyDescriptors) {
		this.methodAspect = kind;
		this.formalName = formalName;
		this.policyDescriptors = policyDescriptors;
	}
	
	public ProcedurePolicyElement(ProcedurePolicyElementKind kind, Collection<PolicyDescriptor> policyDescriptors) {
		this(kind, null, policyDescriptors);
	}
	

	public ProcedurePolicyElementKind getMethodAspect() {
		return methodAspect;
	}

	public Collection<PolicyDescriptor> getPolicyDescriptors() {
		return policyDescriptors;
	}
	
	public String getFormalName() {
		return this.formalName;
	}
}
