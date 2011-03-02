/*
 * File          : JLiftPolicy.java
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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;

public class JLiftPolicy {
	protected List<PolicyStatement> policyStatements;

	public JLiftPolicy(List<PolicyStatement> policyStatements) {
		this.policyStatements = policyStatements;
	}

	public List<PolicyStatement> getPolicyStatements() {
		return policyStatements;
	}

	public Collection<PrincipalPolicyStatement> getPrincipalPolicyStatements() {
		Collection<PrincipalPolicyStatement> principalStatements = new LinkedList<PrincipalPolicyStatement>();
		for(PolicyStatement ps : policyStatements) {
			if (ps instanceof PrincipalPolicyStatement) {
				principalStatements.add((PrincipalPolicyStatement) ps);
			}
		}
		return principalStatements;
	}

	public Collection<ClassPolicyStatement> getClassPolicyStatements() {
		Collection<ClassPolicyStatement> principalStatements = new LinkedList<ClassPolicyStatement>();
		for(PolicyStatement ps : policyStatements) {
			if (ps instanceof ClassPolicyStatement) {
				principalStatements.add((ClassPolicyStatement) ps);
			}
		}
		return principalStatements;
	}

	public ClassPolicyStatement getClassPolicyStatementForClassName(String name) {
		for(PolicyStatement ps : policyStatements) {
			if (ps instanceof ClassPolicyStatement) {
				ClassPolicyStatement cps = (ClassPolicyStatement) ps;
				if (cps.className.equals(name) || name.matches(cps.className)) {
					return cps;
				}
			}
		}

		return null;
	}

	public void sanityCheck(TypeSystem ts) throws SemanticException {
		Collection<ClassPolicyStatement> classPolicyStatements = getClassPolicyStatements();
		for(ClassPolicyStatement cps : classPolicyStatements) {
			ParsedClassType ct = null;
			if (!cps.isRegexPolicy()) {
				// TODO: currently broken for inner classes
//				try {
//					System.err.println("ts: " + ts.systemResolver() + " and " + ts.systemResolver().find("FrontendTest3"));
////					System.err.println("ts: " + ts);
//					ct = (ParsedClassType) ts.systemResolver().find(cps.getClassName());
////					System.err.println("found: " + ct);
//				} catch (SemanticException e) {
//					System.err.println(e);
//					throw new SemanticException("specified class " + cps.getClassName() + " in policy does not exist");
//				}
//				cps.sanityCheck(ct, ts);
			}
		}
	}

	public Set<ParsedClassType> getClassesToParameterize(TypeSystem ts) {
		return null;
	}
	
}
