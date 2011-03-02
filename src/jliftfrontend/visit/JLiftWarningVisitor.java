package jliftfrontend.visit;

import polyglot.ast.Catch;
import polyglot.ast.ClassLit;
import polyglot.ast.Initializer;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorInfo;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;

public class JLiftWarningVisitor extends ContextVisitor {

	public JLiftWarningVisitor(Job job, TypeSystem ts, NodeFactory nf) {
		super(job, ts, nf);
	}

	@Override
	protected Node leaveCall(Node old, Node n, NodeVisitor v)
	throws SemanticException {
		// reflection
		if (n instanceof ClassLit) {
			warn("JLift does not analyze information flows through reflection.  Please attempt to " +
				 "rewrite the code so that any methods invoked on a class through reflection are made " +
				 "explicit.", n.position());
		}
		// catching runtime exceptions
		if (n instanceof Catch) {
			Catch c = (Catch) n;
			if (ts.isUncheckedException(c.catchType())) {
				warn("siggen does not add runtime exceptions to method signatures, as this " +
					 "information is not present in the JRE.  Therefore, this code will be unreachable " + 
					 "unless you add " + c.catchType() + " to the signature of a method inside the try " +
					 "block.", n.position());
			}
		}
		if (n instanceof New) {
			New nw = (New) n;
			if (nw.body() != null) {
				die("JLift cannot analyze information flows that arise from anonymous inner classes.  " +
					 "Please refactor anonymous inner classes to private inner classes or top-level " +
					 "class declarations.", 
					 n.position());
			}
		}
		if (n instanceof Initializer) {
			warn("siggen does not currently track flows through static initializer blocks.  This code " +
				 "should be manually inspected to verify that it does not cause any illegal information " +
				 " flows.", n.position());
		}
		return super.leaveCall(old, n, v);
	}

	private void die(String string, Position position) throws SemanticException {
		throw new SemanticException(string, position);
	}

	protected void warn(String string, Position pos) {
		job.compiler().errorQueue().enqueue(new ErrorInfo(ErrorInfo.WARNING, string, pos));
	}

}
