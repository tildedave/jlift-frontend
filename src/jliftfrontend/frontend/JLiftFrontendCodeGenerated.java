package jliftfrontend.frontend;

import polyglot.ast.NodeFactory;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.frontend.goals.CodeGenerated;
import polyglot.frontend.goals.Goal;
import polyglot.types.TypeSystem;
import polyglot.visit.Translator;

public class JLiftFrontendCodeGenerated extends CodeGenerated {

	protected JLiftFrontendCodeGenerated(Job job) {
		super(job);
	}
	
	@Override
	public Pass createPass(ExtensionInfo extInfo) {
	      TypeSystem ts = extInfo.typeSystem();
	        NodeFactory nf = extInfo.nodeFactory();
	        return new JLiftFrontendOutputPass(this, new Translator(job(), ts, nf, extInfo.targetFactory()));
	}

	public static Goal create(Job job) {
		return new JLiftFrontendCodeGenerated(job);
	}
}
