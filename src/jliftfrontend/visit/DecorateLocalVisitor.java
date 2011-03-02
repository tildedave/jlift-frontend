package jliftfrontend.visit;

import java.util.Collection;

import jliftfrontend.extension.JLiftFrontendExt;
import jliftfrontend.policy.PolicyDescriptor;
import jliftfrontend.policy.ProcedurePolicyStatement;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.TypeNode;
import polyglot.types.SemanticException;
import polyglot.visit.NodeVisitor;

public class DecorateLocalVisitor extends NodeVisitor {

	protected ProcedurePolicyStatement pps;
	protected SemanticException ex;

	public DecorateLocalVisitor(ProcedurePolicyStatement mps) {
		this.pps = mps;
		this.ex = null;
	}
	
	
	
	@Override
	public Node leave(Node old, Node n, NodeVisitor v) {
		if (n instanceof LocalDecl) {
			LocalDecl ld = (LocalDecl) n;
			Collection<PolicyDescriptor> policyDescriptors = pps.getPolicyDescriptorsForLocal(ld.name());
			if (policyDescriptors != null) {
				JLiftFrontendExt ext = (JLiftFrontendExt) ld.type().ext();
				try {
					return ld.type((TypeNode) ld.type().ext(ext.policyDescriptors(policyDescriptors)));
				} catch (SemanticException e) {
					this.ex = e;
					return ld;
				}
			}
		}

		return n;
	}

	// this is hacky -- but we don't have the information to make a ContextVisitor (and it 
	// doesn't seem like that's even the right thing in this situation).  We should be able
	// to get around this by incorporating local policy into the original visitor.
	// TODO: handle local policy in the original visitor
	public SemanticException getException() {
		return ex;
	}
	
}
