/*
 * File          : PolicyParsedGoal.java
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
package jliftfrontend.frontend;

import jliftfrontend.JLiftFrontendScheduler;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Pass;
import polyglot.frontend.goals.AbstractGoal;
import polyglot.frontend.goals.EmptyGoal;
import polyglot.frontend.goals.Goal;

public class PolicyParsedGoal extends AbstractGoal {

	protected PolicyParsedGoal() {
		super(null);
	}
	
	@Override
	public Pass createPass(ExtensionInfo extInfo) {
		return new PolicyParsedPass(this, (jliftfrontend.ExtensionInfo) extInfo);
	}

	public static Goal create(JLiftFrontendScheduler scheduler) {
		return scheduler.internGoal(new PolicyParsedGoal());
	}

}
