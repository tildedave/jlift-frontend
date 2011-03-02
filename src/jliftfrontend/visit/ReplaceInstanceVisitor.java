package jliftfrontend.visit;

import java.util.Collection;

import jliftfrontend.extension.JLiftFrontendImportExt;
import jliftfrontend.extension.JLiftFrontendTypeNodeExt;
import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.Expr;
import polyglot.ast.Import;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.ProcedureCall;
import polyglot.ast.TypeNode;
import polyglot.ext.siggen.util.SignatureContents;
import polyglot.frontend.Job;
import polyglot.types.FunctionInstance;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;

public class ReplaceInstanceVisitor extends ContextVisitor {

	protected SignatureContents sc;

	public ReplaceInstanceVisitor(Job job, TypeSystem ts, NodeFactory nf, SignatureContents sc) {
		super(job, ts, nf);
		this.sc = sc;
	}

	@Override
	protected Node leaveCall(Node n) throws SemanticException {
		if (n instanceof ProcedureCall && n instanceof Expr) {
			ProcedureCall c = (ProcedureCall) n;
			if (c.procedureInstance() instanceof FunctionInstance) {
				Type returnType = ((FunctionInstance) c.procedureInstance()).returnType();
				if (sc.getInstanceMembers().contains(c.procedureInstance()) &&
						sc.getInstanceClasses().contains(returnType)) {
					// only if it returns a type  (don't need to worry about super/this calls)
					CanonicalTypeNode castNode = nf.CanonicalTypeNode(c.position(), returnType);
					castNode = (CanonicalTypeNode) castNode.ext(((JLiftFrontendTypeNodeExt) castNode.ext()).instanceClass(true));
					return nf.Cast(c.position(), castNode, (Expr) c);
				}
			}
		}
		if (n instanceof TypeNode) {
			TypeNode tn = (TypeNode) n;
			if (sc.getInstanceClasses().contains(tn.type())) {
				System.err.println(tn.type()  + " is an instance class");
				JLiftFrontendTypeNodeExt tnExt = (JLiftFrontendTypeNodeExt) tn.ext();
				tnExt = tnExt.instanceClass(true);
				Node returnTn = tn.ext(tnExt);
				return returnTn;
			}
		}
		if (n instanceof Import) {
			Import in = (Import) n;
			if (in.kind().equals(Import.CLASS)) {
				Collection<ParsedClassType> instanceClasses = sc.getInstanceClasses();
				for(ParsedClassType ct : instanceClasses) {
					if (ct.fullName().equals(in.name())) {
						return in.ext(((JLiftFrontendImportExt) in.ext()).instanceImport(true));
					}
				}
			}
		}
		return super.leaveCall(n);
	}

}
