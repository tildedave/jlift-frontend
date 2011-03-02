/*
 * File          : JLiftFrontendMethodDeclExt.java
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
import java.util.LinkedList;
import java.util.List;

import jliftfrontend.policy.ClassPolicyStatement;
import jliftfrontend.policy.JLiftPolicy;
import jliftfrontend.policy.PolicyDescriptor;
import jliftfrontend.policy.ProcedurePolicyElement;
import jliftfrontend.policy.ProcedurePolicyStatement;
import jliftfrontend.visit.DecorateLocalVisitor;
import polyglot.ast.Block;
import polyglot.ast.Ext;
import polyglot.ast.Formal;
import polyglot.ast.MethodDecl;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.ast.TypeNode;
import polyglot.types.Context;
import polyglot.types.SemanticException;

public class JLiftFrontendMethodDeclExt extends JLiftFrontendExt_c {

	protected Collection<PolicyDescriptor> returnAspect;
	protected Collection<PolicyDescriptor> beginAspect;

	public Collection<PolicyDescriptor> returnAspect() {
		return returnAspect;
	}

	public Collection<PolicyDescriptor> beginAspect() {
		return beginAspect;
	}

	public JLiftFrontendMethodDeclExt returnAspect(Collection<PolicyDescriptor> returnAspect) {
		JLiftFrontendMethodDeclExt copyExt = (JLiftFrontendMethodDeclExt) copy();
		copyExt.returnAspect = returnAspect;
		return copyExt;
	}

	public JLiftFrontendMethodDeclExt beginAspect(Collection<PolicyDescriptor> beginAspect) {
		JLiftFrontendMethodDeclExt copyExt = (JLiftFrontendMethodDeclExt) copy();
		copyExt.beginAspect = beginAspect;
		return copyExt;
	}	

	@Override
	public Node processPolicy(JLiftPolicy policy, Context c) throws SemanticException {
		MethodDecl md = (MethodDecl) node();

		String className = c.currentClass().fullName();
		ClassPolicyStatement cps = policy.getClassPolicyStatementForClassName(className);
		if (cps == null)
			return md;

		ProcedurePolicyStatement mps = cps.getMethodPolicyStatementForMethodName(md.name());
		if (mps == null)
			return md;

		// first off, aspects of return, begin
		Collection<PolicyDescriptor> returnAspect = mps.getPolicyDescriptorsForAspect(ProcedurePolicyElement.RETURN);
		Collection<PolicyDescriptor> beginAspect = mps.getPolicyDescriptorsForAspect(ProcedurePolicyElement.BEGIN);

		Ext methodExt = md.ext();
		methodExt = decorateReturnAspect(returnAspect, methodExt);
		methodExt = decorateBeginAspect(beginAspect, methodExt);

		List<Formal> newFormals = declorateFormals(md, mps);
		TypeNode newReturnType = decorateReturnType(md, mps);
		Block newBody = decorateBody(md, mps);

		MethodDecl returnDecl = (MethodDecl) ((MethodDecl) md.ext(methodExt)).formals(newFormals).returnType(newReturnType).body(newBody);
		sanityCheckPolicyDescriptors(returnDecl);
		return returnDecl;
	}

	private Block decorateBody(MethodDecl md, ProcedurePolicyStatement mps) throws SemanticException {
		Block b = md.body();
		DecorateLocalVisitor decorateVisitor = new DecorateLocalVisitor(mps);
		Block newBlock = (Block) b.visit(decorateVisitor);
		if (decorateVisitor.getException() != null)
			throw decorateVisitor.getException();
		
		return newBlock;
	}

	private Ext decorateReturnAspect(Collection<PolicyDescriptor> returnAspect,
			Ext methodExt) {
		if (returnAspect != null) {
			methodExt = ((JLiftFrontendMethodDeclExt) methodExt).returnAspect(returnAspect);
		}
		return methodExt;
	}

	private Ext decorateBeginAspect(Collection<PolicyDescriptor> beginAspect,
			Ext methodExt) {
		if (beginAspect != null) {
			methodExt = ((JLiftFrontendMethodDeclExt) methodExt).beginAspect(beginAspect);
		}
		return methodExt;
	}

	private List<Formal> declorateFormals(MethodDecl md,
			ProcedurePolicyStatement mps) throws SemanticException {
		// next, aspects on the typenodes of the formals
		List<Formal> newFormals = new LinkedList<Formal>();
		for(Formal f : (List<Formal>) md.formals()) {
			Collection<PolicyDescriptor> formalAspect = mps.getPolicyDescriptorsForAspect(ProcedurePolicyElement.FORMAL, f.name());
			if (formalAspect != null) {
				Ext newTypeNodeExt = ((JLiftFrontendExt) f.type().ext()).policyDescriptors(formalAspect);
				newFormals.add(f.type((TypeNode) f.type().ext(newTypeNodeExt)));
			}
			else {
				newFormals.add(f);
			}
		}
		return newFormals;
	}

	private TypeNode decorateReturnType(MethodDecl md,
			ProcedurePolicyStatement mps) throws SemanticException {
		TypeNode newReturnType = md.returnType();
		Collection<PolicyDescriptor> returnvalueAspect = mps.getPolicyDescriptorsForAspect(ProcedurePolicyElement.RETURNVALUE);
		if (returnvalueAspect != null) {
			newReturnType = (TypeNode) newReturnType.ext(((JLiftFrontendExt) newReturnType.ext()).policyDescriptors(returnvalueAspect));
		}
		return newReturnType;
	}

	@Override
	protected void sanityCheckPolicyDescriptors(Node node)
	throws SemanticException {
		MethodDecl md = (MethodDecl) node;
		Collection<PolicyDescriptor> beginAspect = ((JLiftFrontendMethodDeclExt) md.ext()).beginAspect();
		Collection<PolicyDescriptor> returnAspect = ((JLiftFrontendMethodDeclExt) md.ext()).returnAspect();

		verifyPolicyDescriptors(beginAspect, "begin label of the method " + md.name(), node.position(), true, false, false);
		verifyPolicyDescriptors(returnAspect, "return label of the method " + md.name(), node.position(), true, false, false);
		
		Collection<PolicyDescriptor> returnTypeDescriptors = ((JLiftFrontendExt) md.returnType().ext()).getPolicyDescriptors();
		if (returnTypeDescriptors != null && returnTypeDescriptors.size() > 0 && md.returnType().type().isVoid()) {
			throw new SemanticException("cannot label the return value of a method returning void");
		}
	}
}
