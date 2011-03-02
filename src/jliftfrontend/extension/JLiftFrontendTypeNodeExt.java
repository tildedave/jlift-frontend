/*
 * File          : JLiftFrontendTypeNodeExt.java
 * Project       : jlift-frontend
 * Description   : TypeNode extension for JLift Frontend
 * Author(s)     : dhking
 *
 * Created       : Feb 11, 2008 11:45:20 AM 
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jliftfrontend.extension;

import java.util.Collection;
import java.util.LinkedList;

import jliftfrontend.policy.ArrayBaseLabelPolicyDescriptor;
import jliftfrontend.policy.ClassPolicyStatement;
import jliftfrontend.policy.JLiftPolicy;
import jliftfrontend.policy.ParamPolicyDescriptor;
import jliftfrontend.policy.PolicyDescriptor;
import polyglot.ast.Node;
import polyglot.ast.TypeNode;
import polyglot.types.ArrayType;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;

public class JLiftFrontendTypeNodeExt extends JLiftFrontendExt_c {

	protected boolean instanceClass;

	public JLiftFrontendTypeNodeExt() {
		this.instanceClass = false;
	}

	@Override
	protected void sanityCheckPolicyDescriptors(Node n) throws SemanticException {
		TypeNode tn = (TypeNode) n;

		if (this.policyDescriptors == null || tn == null)
			return;

		for (PolicyDescriptor pd : this.policyDescriptors) {
			if (pd instanceof ArrayBaseLabelPolicyDescriptor && !tn.type().isArray()) {
				throw new SemanticException("cannot give an array base label to a non-array type", tn.position());
			}
			if (pd instanceof ParamPolicyDescriptor && tn.type().isPrimitive()) {
				throw new SemanticException("cannot parameterize a primitive type", tn.position());
			}
		}

		super.verifyPolicyDescriptors(this.policyDescriptors, "type node " + tn, tn.position(), true, true, true);
	}

	public Node processPolicyForTypeNode(JLiftPolicy policy, Context c, boolean canParameterizeWithThis)
	throws SemanticException {
		TypeNode tn = (TypeNode) node();
		Type type = tn.type();
		if (type.isArray()) {
			ArrayType at = (ArrayType) type;
			type = at.base();
		}

		ClassPolicyStatement cp = null;
		if (type instanceof ParsedClassType) {
			ParsedClassType ct = (ParsedClassType) type;
			cp = policy.getClassPolicyStatementForClassName(ct.fullName());
		}

		if (cp != null && cp.getParamLabel() != null) {
			// we should parameterize this class.  but with what?

			// if the containing class has a policy, we will label it with that policy
			// otherwise, we will label it with {this}.
			ClassType containingCt = c.currentClass();
			// note: containingCt might be null in certain cases
			if (containingCt != null) {
				ClassPolicyStatement containingPolicy = policy.getClassPolicyStatementForClassName(containingCt.fullName());

				Collection<PolicyDescriptor> ppdC = new LinkedList<PolicyDescriptor>();
				ParamPolicyDescriptor ppd;
				if (containingPolicy != null) {
					ppd = new ParamPolicyDescriptor(cp.getParamLabel());
				}
				else {
					// label with this?
					if (canParameterizeWithThis) {
						ppd = new ParamPolicyDescriptor("{this}");
					}
					else {
						ppd = new ParamPolicyDescriptor("{}");
					}
				}
				ppdC.add(ppd);

				return tn.ext(policyDescriptors(ppdC));
			}
		}
		return tn;
	}

	public JLiftFrontendTypeNodeExt instanceClass(boolean b) {
		JLiftFrontendTypeNodeExt copy = (JLiftFrontendTypeNodeExt) copy();
		copy.instanceClass = b;

		return copy;
	}

	public boolean instanceClass() {
		return this.instanceClass;
	}
}
