/*
 * File          : PolicyParsedPass.java
 * Project       : jlift-frontend
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 11, 2008 11:33:24 AM 
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jliftfrontend.frontend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import jliftfrontend.ExtensionInfo;
import jliftfrontend.JLiftFrontendOptions;
import jliftfrontend.policy.JLiftPolicy;
import jliftfrontend.policy.parse.PolicyLexer;
import jliftfrontend.policy.parse.PolicyParser;
import polyglot.frontend.AbstractPass;
import polyglot.frontend.Pass;
import polyglot.frontend.goals.Goal;
import polyglot.types.SemanticException;
import polyglot.util.ErrorInfo;
import polyglot.util.InternalCompilerError;

public class PolicyParsedPass extends AbstractPass implements Pass {

	protected ExtensionInfo extInfo;

	public PolicyParsedPass(Goal goal, ExtensionInfo extInfo) {
		super(goal);
		this.extInfo = extInfo;
	}

	@Override
	public boolean run() {
		JLiftPolicy jp = getJLiftPolicyFromFile();
		
		if (jp == null)
			return false;
			
		try {
			jp.sanityCheck(extInfo.typeSystem());
		} catch (SemanticException e) {
			extInfo.compiler().errorQueue().enqueue(
					new ErrorInfo(ErrorInfo.SEMANTIC_ERROR, e.getMessage(), e.position()));
			return false;
		}
		extInfo.setPolicy(jp);
		return true;
	}

	private JLiftPolicy getJLiftPolicyFromFile() {
		String policyFile = JLiftFrontendOptions.getInstance().getPolicyFile();
		PolicyLexer lexer;
		try {
			lexer = new PolicyLexer(new FileInputStream(new File(policyFile)));
		} catch (FileNotFoundException e) {
			throw new InternalCompilerError("could not find policy file " + policyFile);
		}
		PolicyParser parser = new PolicyParser(lexer);
		JLiftPolicy jp;
		try {
			jp = (JLiftPolicy) parser.parse().value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return jp;
	}

}
