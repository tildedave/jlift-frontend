/*
 * File          : JLiftFrontendFieldDeclExt.java
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

import jliftfrontend.policy.ClassPolicyStatement;
import jliftfrontend.policy.FieldPolicyStatement;
import jliftfrontend.policy.JLiftPolicy;
import jliftfrontend.policy.ParamPolicyDescriptor;
import jliftfrontend.policy.PolicyDescriptor;
import polyglot.ast.Ext;
import polyglot.ast.FieldDecl;
import polyglot.ast.Node;
import polyglot.ast.TypeNode;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.SemanticException;

public class JLiftFrontendFieldDeclExt extends JLiftFrontendExt_c {

	@Override
	public Node processPolicy(JLiftPolicy policy, Context c) throws SemanticException {
		// get policy for this field
		FieldDecl f = (FieldDecl) node();
		
		ClassType ct = c.currentClass();
		
		String completeName = ct.fullName();
		ClassPolicyStatement cp = policy.getClassPolicyStatementForClassName(completeName);
		if (cp == null)
			return f;
			
		FieldPolicyStatement fps = cp.getFieldPolicyStatementForFieldName(f.name());
		if (fps == null)
			return f;
			
		Collection<PolicyDescriptor> descriptors = fps.getPolicyDescriptors();
		ParamPolicyDescriptor ppd = null;
		boolean shouldNotParameterize = f.declType().isPrimitive() || f.declType().toString().equals("java.lang.String");
		if (fps.getFieldName().contains("*") && shouldNotParameterize) {
			for(PolicyDescriptor pd : descriptors) {
				if (pd instanceof ParamPolicyDescriptor)
					ppd = (ParamPolicyDescriptor) pd;
			}
		}
		if (ppd != null) {
			descriptors.remove(ppd);
		}
		
		TypeNode tn = f.type();
		Ext newExt = ((JLiftFrontendExt) tn.ext()).policyDescriptors(descriptors);
		tn = (TypeNode) tn.ext(newExt);
		
		return f.type(tn);
	}
}
