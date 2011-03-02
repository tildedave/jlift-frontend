/*
 * File          : ArrayBaseLabelPolicyDescriptor.java
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


public class ArrayBaseLabelPolicyDescriptor extends PolicyDescriptor {
	
	protected String labelString;
	
	public ArrayBaseLabelPolicyDescriptor(String labelString) {
		this.labelString = labelString;
	}
	
	public String getArrayBaseLabelString() {
		return labelString;
	}
}
