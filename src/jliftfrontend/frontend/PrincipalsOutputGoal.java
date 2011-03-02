package jliftfrontend.frontend;

import jliftfrontend.JLiftFrontendScheduler;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Pass;
import polyglot.frontend.goals.AbstractGoal;
import polyglot.frontend.goals.Goal;

public class PrincipalsOutputGoal extends AbstractGoal {

	public PrincipalsOutputGoal() {
		super(null);
	}
	
	@Override
	public Pass createPass(ExtensionInfo extInfo) {
		return new PrincipalsOutputPass(this, (jliftfrontend.ExtensionInfo) extInfo);
	}

	public static Goal create(JLiftFrontendScheduler scheduler) {
		return new PrincipalsOutputGoal();
	}

}
