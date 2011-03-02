/*
 * File          : ClassPolicyStatement.java
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
import java.util.List;

import polyglot.types.FieldInstance;
import polyglot.types.MethodInstance;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;

public class ClassPolicyStatement extends PolicyStatement {
	protected String className;
	protected List<MemberPolicyStatement> memberPolicies; 
	protected String paramLabel;

	public ClassPolicyStatement(String className,
			List<MemberPolicyStatement> memberPolicies, String paramLabel) {
		this.className = className;
		this.memberPolicies = memberPolicies;
		this.paramLabel = paramLabel;
	}

	public String getClassName() {
		return className;
	}

	public List<MemberPolicyStatement> getMemberPolicies() {
		return memberPolicies;
	}

	public String getParamLabel() {
		return paramLabel;
	}

	public FieldPolicyStatement getFieldPolicyStatementForFieldName(String fieldName) {
		for(MemberPolicyStatement mps : memberPolicies) {
			if (mps instanceof FieldPolicyStatement) {
				FieldPolicyStatement fps = (FieldPolicyStatement) mps;
				if (fps.getFieldName().equals(fieldName) || fieldName.matches(fps.getFieldName())) {
					return fps;
				}
			}
		}

		return null;
	}

	public ProcedurePolicyStatement getMethodPolicyStatementForMethodName(String name) {
		for(MemberPolicyStatement memps : memberPolicies) {
			if (memps instanceof ProcedurePolicyStatement) {
				ProcedurePolicyStatement mps = (ProcedurePolicyStatement) memps;
				if (mps.getMethodName().equals(name)) {
					return mps;
				}
			}
		}

		return null;
	}

	/**
	 * perform a sanity check of the policy statements.  i.e. each of the fields and 
	 * methods named inside it actually exist.  TODO: integrate this with the sanity 
	 * checking inside the extension objects
	 * @param ct
	 * @param ts
	 * @throws SemanticException
	 */
	public void sanityCheck(ParsedClassType ct, TypeSystem ts) throws SemanticException {
		for(MemberPolicyStatement mps : memberPolicies) {
			if (mps instanceof FieldPolicyStatement) {
				FieldPolicyStatement fps = (FieldPolicyStatement) mps;
				try {
					FieldInstance fi = ts.findField(ct, fps.getFieldName());
				} catch (SemanticException e) {
					throw new SemanticException("field " + fps.getFieldName() + " does not exist inside class " + ct);
				}
			}
			if (mps instanceof ProcedurePolicyStatement) {
				ProcedurePolicyStatement pps = (ProcedurePolicyStatement) mps;
				// TODO: generalize this for constructors
				boolean foundMethod = false;
				for (MethodInstance mi : (Collection<MethodInstance>) ct.methods()) {
					if (mi.name().equals(pps.getMethodName())) {
						foundMethod = true;
						break;
					}
				}
				if (!foundMethod)
					throw new SemanticException("method " + pps.getMethodName() + " does not exist inside class " + ct);
			}
		}
	}

	public boolean isRegexPolicy() {
		// ugly for now
		return this.className.contains("*");
	}
}
