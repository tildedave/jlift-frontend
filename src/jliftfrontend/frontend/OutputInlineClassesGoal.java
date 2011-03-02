package jliftfrontend.frontend;

import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Pass;
import polyglot.frontend.goals.AbstractGoal;
import polyglot.frontend.goals.Goal;

public class OutputInlineClassesGoal extends AbstractGoal {

	protected ExtensionInfo extInfo;

	protected OutputInlineClassesGoal(ExtensionInfo extInfo) {
		super(null);
		this.extInfo = extInfo;
	}

	@Override
	public Pass createPass(ExtensionInfo extInfo) {
		return new OutputInlineClassesPass(this, extInfo);
	}

	public static Goal create(ExtensionInfo extInfo) {
		return new OutputInlineClassesGoal(extInfo);
	}

}
