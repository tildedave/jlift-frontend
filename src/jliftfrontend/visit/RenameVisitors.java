package jliftfrontend.visit;

import polyglot.ast.Formal;
import polyglot.ast.Local;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;

public class RenameVisitors extends ContextVisitor {

	public RenameVisitors(Job job, TypeSystem ts, NodeFactory nf) {
		super(job, ts, nf);
	}

	@Override
	protected Node leaveCall(Node parent, Node old, Node n, NodeVisitor v)
			throws SemanticException {
		if (n instanceof LocalDecl) {
			LocalDecl ld = (LocalDecl) n;
			String name = ld.name();
			if (name.equals("principal"))
				return ld.id(ld.id().id("principal_"));
			if (name.equals("label"))
				return ld.id(ld.id().id("label_"));
		}
		if (n instanceof Local) {
			Local l = (Local) n;
			String name = l.name();
			System.err.println("l: " + l + " with name " + name);
			if (name.equals("principal"))
				return l.id(l.id().id("principal_"));
			if (name.equals("label"))
				return l.id(l.id().id("label_"));
		}
		if (n instanceof Formal) {
			Formal f = (Formal) n;
			String name = f.name();
			if (name.equals("principal")) {
				return f.id(f.id().id("principal_"));
			}
			if (name.equals("label"))
				return f.id(f.id().id("label_"));
		}
		return super.leaveCall(parent, old, n, v);
	}

}
