/*
 * File          : JLiftFrontendClassDeclExt.java
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

import java.util.LinkedList;
import java.util.List;

import jliftfrontend.policy.ClassPolicyStatement;
import jliftfrontend.policy.JLiftPolicy;
import polyglot.ast.ClassDecl;
import polyglot.ast.Ext;
import polyglot.ast.Node;
import polyglot.ast.TypeNode;
import polyglot.types.Context;
import polyglot.types.SemanticException;

public class JLiftFrontendClassDeclExt extends JLiftFrontendExt_c {

	protected String paramLabel;

	public JLiftFrontendClassDeclExt() {
		this.paramLabel = null;
	}

	public String getParamLabel() {
		return this.paramLabel;
	}

	protected Ext paramLabel(String paramLabel) {
		JLiftFrontendClassDeclExt copy = (JLiftFrontendClassDeclExt) copy();
		copy.paramLabel = paramLabel;
		return copy;
	}


	@Override
	public Node processPolicy(JLiftPolicy policy, Context c) throws SemanticException {
		ClassDecl cd = (ClassDecl) node();
		ClassPolicyStatement cp = policy.getClassPolicyStatementForClassName(cd.type().fullName());

		TypeNode superClass = cd.superClass();
		TypeNode newSuperClass = superClass; 
		if (superClass != null) {
			JLiftFrontendTypeNodeExt superExt = (JLiftFrontendTypeNodeExt) superClass.ext();
			newSuperClass = (TypeNode) superExt.processPolicyForTypeNode(policy, c, false);
		}

		List<TypeNode> interfaces = cd.interfaces();
		List<TypeNode> newInterfaces = new LinkedList<TypeNode>();
		for(TypeNode interNode : interfaces) {
			JLiftFrontendTypeNodeExt interExt = (JLiftFrontendTypeNodeExt) interNode.ext();
			TypeNode newInterExt = (TypeNode) interExt.processPolicyForTypeNode(policy, c, false);
			newInterfaces.add(newInterExt);
		}

		ClassDecl returnCd = cd.superClass(newSuperClass).interfaces(newInterfaces);
		
		if (cp != null && cp.getParamLabel() != null) {
			return returnCd.ext(paramLabel(cp.getParamLabel()));
		}

		return returnCd;
	}
}
