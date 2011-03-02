/*
 * File          : JLiftFrontendCanonicalTypeNodeDel.java
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

import jliftfrontend.policy.ArrayBaseLabelPolicyDescriptor;
import jliftfrontend.policy.LabelPolicyDescriptor;
import jliftfrontend.policy.ParamPolicyDescriptor;
import jliftfrontend.policy.PolicyDescriptor;
import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.JL_c;
import polyglot.types.ArrayType;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.util.CodeWriter;
import polyglot.visit.Translator;

public class JLiftFrontendCanonicalTypeNodeDel extends JL_c {

	@Override
	public void translate(CodeWriter w, Translator tr) {
		JLiftFrontendTypeNodeExt ext = (JLiftFrontendTypeNodeExt) node().ext();

		CanonicalTypeNode ctn = (CanonicalTypeNode) node();
		Type type = ctn.type();

		if (ext.getPolicyDescriptors() != null) {
			String labelString = "";
			String paramString = "";
			String arraybaseLabelString = "";

			for(PolicyDescriptor pd : ext.getPolicyDescriptors()) {
				if (pd instanceof LabelPolicyDescriptor) {
					labelString = ((LabelPolicyDescriptor) pd).getLabelString();
				}
				if (pd instanceof ParamPolicyDescriptor) {
					paramString = ((ParamPolicyDescriptor) pd).getParamLabelString();
				}
				if (pd instanceof ArrayBaseLabelPolicyDescriptor) {
					arraybaseLabelString = ((ArrayBaseLabelPolicyDescriptor) pd).getArrayBaseLabelString();
				}
			}


			String appendString = "";
			if (!paramString.equals("") && !type.isArray()) {
				appendString += "[" + paramString + "]";
			}
			appendString += labelString + " ";

			String typeString;
			if (type.isArray()) {
				typeString = addArrayBaseAndParameterStringToArrayType((ArrayType) type, arraybaseLabelString, paramString, tr.context()); 
			}
			else {
				typeString = type.translate(tr.context());
			}

			w.write(getInlineClassString(typeString, ext.instanceClass()) + appendString);
		}
		else {
			String typeString = type.translate(tr.context());
			w.write(getInlineClassString(typeString, ext.instanceClass()));
		}
	}

	private String addArrayBaseAndParameterStringToArrayType(ArrayType type,
			String arraybaseLabelString, String paramString, Resolver c) {
		String baseString;
		if (type.base() instanceof ArrayType) {
			baseString = addArrayBaseAndParameterStringToArrayType(type, arraybaseLabelString, paramString, c);
		}
		else {
			baseString = type.base().translate(c);
			baseString += "[" + paramString + "]";
			baseString += arraybaseLabelString;
		}

		return baseString + "[]";
	}

	public String getInlineClassString(String oldClassString, boolean isInline) {
		if (!isInline)
			return oldClassString;
			
		if (oldClassString.contains(".")) {
			int lasti = oldClassString.lastIndexOf(".");
			return oldClassString.substring(0, lasti) + "." + "Inline" + oldClassString.substring(lasti + 1);
		}
		
		return "Inline" + oldClassString;
	}
}
