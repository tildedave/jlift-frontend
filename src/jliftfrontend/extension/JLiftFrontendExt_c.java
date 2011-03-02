/*
 * File          : JLiftFrontendExt_c.java
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

import jliftfrontend.policy.ArrayBaseLabelPolicyDescriptor;
import jliftfrontend.policy.JLiftPolicy;
import jliftfrontend.policy.LabelPolicyDescriptor;
import jliftfrontend.policy.ParamPolicyDescriptor;
import jliftfrontend.policy.PolicyDescriptor;
import polyglot.ast.Ext;
import polyglot.ast.Ext_c;
import polyglot.ast.Node;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.util.Position;

public class JLiftFrontendExt_c extends Ext_c implements JLiftFrontendExt {

	protected Collection<PolicyDescriptor> policyDescriptors;
	
	public JLiftFrontendExt_c() {
		this.policyDescriptors = null;
	}
	
	public Ext policyDescriptors(
			Collection<PolicyDescriptor> policyDescriptors) throws SemanticException {
		JLiftFrontendExt_c copy = (JLiftFrontendExt_c) copy();
		copy.policyDescriptors = policyDescriptors;
		copy.sanityCheckPolicyDescriptors(node());
		return copy;
	}
	
	protected void sanityCheckPolicyDescriptors(Node node) throws SemanticException {
	}

	public Collection<PolicyDescriptor> getPolicyDescriptors() {
		return policyDescriptors;
	}

	public Node processPolicy(JLiftPolicy policy, Context c) throws SemanticException {
		return node();
	}

	protected void verifyPolicyDescriptors(
			Collection<PolicyDescriptor> pds, String programElementName,
			Position position, boolean canSeeLabel, boolean canSeeParameter, boolean canSeeArrayType) throws SemanticException {

		if (pds != null) {
			String seenLabelDescriptor = null;
			String seenParamDescriptor = null;
			String seenArrayBaseLabelDescriptor = null;
			
			for(PolicyDescriptor pd : pds) {
				if (pd instanceof LabelPolicyDescriptor) {
					if (!canSeeLabel)
						throw new SemanticException("cannot apply label to " + programElementName);
					if (seenLabelDescriptor != null)
						throw new SemanticException("can only apply one label to " + programElementName + ": told to apply both " + ((LabelPolicyDescriptor) pd).getLabelString() + " and " + seenLabelDescriptor);
					seenLabelDescriptor = ((LabelPolicyDescriptor) pd).getLabelString();
				}
				if (pd instanceof ParamPolicyDescriptor) {
					if (!canSeeParameter)
						throw new SemanticException("cannot apply parameter label to " + programElementName);
					if (seenParamDescriptor != null)
						throw new SemanticException("can only apply one parameter label to " + programElementName + ": told to apply both " + ((ParamPolicyDescriptor) pd).getParamLabelString() +" and " + seenParamDescriptor);
					seenParamDescriptor = ((ParamPolicyDescriptor) pd).getParamLabelString();
				}
				if (pd instanceof ArrayBaseLabelPolicyDescriptor) {
					if (!canSeeArrayType)
						throw new SemanticException("cannot apply array base label to " + programElementName);
					if (seenArrayBaseLabelDescriptor != null)
						throw new SemanticException("can only apply one array base label to " + programElementName + ": told to apply both " + ((ArrayBaseLabelPolicyDescriptor) pd).getArrayBaseLabelString() +" and " + seenParamDescriptor);
					seenArrayBaseLabelDescriptor = ((ArrayBaseLabelPolicyDescriptor) pd).getArrayBaseLabelString();
				}
			}
		}
	}
}
