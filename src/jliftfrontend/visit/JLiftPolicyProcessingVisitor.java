package jliftfrontend.visit;

import jliftfrontend.extension.JLiftFrontendExt;
import jliftfrontend.policy.JLiftPolicy;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import jliftfrontend.ExtensionInfo;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;

public class JLiftPolicyProcessingVisitor extends ContextVisitor {

	protected ExtensionInfo theExtInfo;
	

	public JLiftPolicyProcessingVisitor(Job job, TypeSystem ts, NodeFactory nf, ExtensionInfo extInfo) {
		super(job, ts, nf);
		this.theExtInfo = extInfo;
	}

	@Override
	protected Node leaveCall(Node n) throws SemanticException {
		JLiftPolicy thePolicy = theExtInfo.getPolicy();
		return ((JLiftFrontendExt) n.ext()).processPolicy(thePolicy, context());
	}
	
}
