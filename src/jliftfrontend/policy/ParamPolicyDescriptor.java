/*
 * File          : ParamPolicyDescriptor.java
 * Project       : jlift-frontend
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 11, 2008 11:33:26 AM 
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jliftfrontend.policy;

public class ParamPolicyDescriptor extends PolicyDescriptor {

	protected String paramLabelString;
	
	public ParamPolicyDescriptor(String paramLabelString) {
		this.paramLabelString = paramLabelString;
	}

	public String getParamLabelString() {
		return paramLabelString;
	}
}
