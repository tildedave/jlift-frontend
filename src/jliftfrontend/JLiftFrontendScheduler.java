/*
 * File          : JLiftFrontendScheduler.java
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
package jliftfrontend;

import jliftfrontend.frontend.JLiftFrontendCodeGenerated;
import jliftfrontend.frontend.OutputInlineClassesGoal;
import jliftfrontend.frontend.PolicyParsedGoal;
import jliftfrontend.frontend.PrincipalsOutputGoal;
import jliftfrontend.visit.JLiftPolicyProcessingVisitor;
import jliftfrontend.visit.JLiftWarningVisitor;
import jliftfrontend.visit.RenameVisitors;
import polyglot.frontend.CyclicDependencyException;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.JLScheduler;
import polyglot.frontend.Job;
import polyglot.frontend.goals.Goal;
import polyglot.frontend.goals.VisitorGoal;
import polyglot.util.InternalCompilerError;
import polyglot.visit.LocalClassRemover;

public class JLiftFrontendScheduler extends JLScheduler {

	public JLiftFrontendScheduler(ExtensionInfo extInfo) {
		super(extInfo);
		addGoal(PolicyParsedGoal());
	}

	@Override
	public Goal Parsed(Job job) {
		Goal parsedGoal = super.Parsed(job);
		return parsedGoal;
	}

	@Override
	public Goal CodeGenerated(Job job) {
		Goal codeGeneratedGoal = JLiftFrontendCodeGenerated.create(job);
		try {
			addPrerequisiteDependency(codeGeneratedGoal, PolicyProcessed(job));
			addPrerequisiteDependency(codeGeneratedGoal, JLiftWarnings(job));
			addPrerequisiteDependency(codeGeneratedGoal, PrincipalsOutput());
		} catch (CyclicDependencyException e) {
			throw new InternalCompilerError("unexpected cyclic dependency: " + e);
		}
		return codeGeneratedGoal;
	}

	public Goal RemovedAnonymousInnerClasses(Job job) {
		Goal removedInnerClassGoal  = internGoal(new VisitorGoal(job, new LocalClassRemover(job, extInfo.typeSystem(), extInfo.nodeFactory())));
		try {
			addPrerequisiteDependency(removedInnerClassGoal, TypeChecked(job));
		}
		catch (CyclicDependencyException e) {
			throw new InternalCompilerError("unexpected cyclic dependency: " + e);
		}
		
		return removedInnerClassGoal;
	}

	private Goal PrincipalsOutput() {
		Goal principalOutputGoal = internGoal(PrincipalsOutputGoal.create(this));
		try {
			addPrerequisiteDependency(principalOutputGoal, PolicyParsedGoal());
		} catch (CyclicDependencyException e) {
			throw new InternalCompilerError("unexpected cyclic dependency: " + e);
		}
		return principalOutputGoal;
	}

	public Goal PolicyParsedGoal() {
		return internGoal(PolicyParsedGoal.create(this));
	}
	
	public Goal MembersRenamed(Job job) {
		Goal renameMembers = internGoal(new VisitorGoal(job, new RenameVisitors(job, extInfo.typeSystem(), extInfo.nodeFactory())));
		try {
			addPrerequisiteDependency(renameMembers, Parsed(job));
			addPrerequisiteDependency(Disambiguated(job), renameMembers);
		}
		catch (CyclicDependencyException e) {
			throw new InternalCompilerError("unexpected cyclic dependency: " + e);
		}
		
		return renameMembers;
	}

	public Goal PolicyProcessed(Job job) {
		Goal policyProcessed = internGoal(new VisitorGoal(job, 
				new JLiftPolicyProcessingVisitor(job, 
						extInfo.typeSystem(), 
						extInfo.nodeFactory(), 
						(jliftfrontend.ExtensionInfo) extInfo)));
		try {
			addPrerequisiteDependency(policyProcessed, PolicyParsedGoal());
		} catch (CyclicDependencyException e) {
			throw new InternalCompilerError("unexpected cyclic dependency: " + e);
		}
		return policyProcessed;
	}


	public Goal JLiftWarnings(Job job) {
		Goal warningsGoal = internGoal(new VisitorGoal(job,
				new JLiftWarningVisitor(job, 
						extInfo.typeSystem(), 
						extInfo.nodeFactory())));
		
		try {
			addPrerequisiteDependency(((jliftfrontend.ExtensionInfo) extInfo).getGenerateSignaturesGoal(), 
					warningsGoal);
			addPrerequisiteDependency(warningsGoal, RemovedAnonymousInnerClasses(job));
		}
		catch (CyclicDependencyException e) {
			throw new InternalCompilerError("unexpected cyclic dependency: " + e);
		}
		
		return warningsGoal;
	}

	public Goal OutputInlineClasses() {
		return internGoal(OutputInlineClassesGoal.create(extInfo));
	}
	
	
}
